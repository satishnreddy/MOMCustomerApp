package com.mswipetech.sdk.network;

import android.content.Context;
import android.os.Handler;


import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;


public class NetworkSocketManager extends Thread {
	public static final String log_tab = "NetWorkHandler=>";
	private static Socket mSocket = null;
	private static String mstConnectionErrDesc="";
	private static OutputStream ost =null;
	private static InputStream ist = null;
	public String mPostData = "";
	private boolean isParseCalled = false;
	
	private boolean isReadSocketStarted = false;
	private int iReconnect = 0;
	//private boolean connectionTimedout = false;

	
    boolean m_trucking = true;
	boolean m_runHttpMethod = true;
	Handler networkHandler = null;
	Context context = null;

	public NetworkSocketManager(Context context) 
	{
		this.context = context;
		//applicationData = (ApplicationData)context.getApplicationContext();		
		
	}
	public static void connectBG()
	{
		new Thread(new ConnectionSocket()).start();
		
	}
	
	public static void connect()
	{
		try{
			//Log.v(ApplicationData.packName, log_tab + " Connecting...");
				ConnectionSocket.socketConnetion();
					
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
	
	}
	
	public static void close()
	{
		try{
			//Log.v(ApplicationData.packName, log_tab + " Diconnecting...");
			
			try{
				if(ist!=null)
					ist.close();
			}catch(Exception ex){}
			finally{ ist = null;}
				
			try{
				if(ost!=null)
					ost.close();
			}catch(Exception ex){}
			finally{ ost = null;}
			if(mSocket!=null)
				mSocket.close();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			
			ist = null;
			ost = null;
			mSocket = null;
					
		}
	
	}
		
	@Override
	public void run() 
	{
		iReconnect =0; // incase of failure reconnects
		isParseCalled = false;
		readSocket();

	}
	
	private void readSocket()
	{
		
		String dataResponse = "";
		try{
			mstConnectionErrDesc = "";	
			//Log.v(ApplicationData.packName, log_tab + " Check for the connection to read and write");
			ConnectionSocket.socketConnetion();
			if(ost == null)
			{
				//Log.v(ApplicationData.packName, log_tab + " Reading the output channel");
					
					/*ost = new BufferedReader(
			            new InputStreamReader(
			            		socket.getInputStream()));;
					 */
					ost= new DataOutputStream(
				            mSocket.getOutputStream());
			}
			//Log.v(ApplicationData.packName, log_tab + " Writing to the output channel " + mPostData.length() + " bytes");				
			ost.write(mPostData.getBytes());
			
			
			
			if(ist == null)
			{
				//Log.v(ApplicationData.packName, log_tab + " Reading the input channel");			
				ist = mSocket.getInputStream();
			}
			byte[] buffer = new byte[4096];
			int read=0;
			//Log.v(ApplicationData.packName, log_tab + " Reading from the input channel");			
			
			read = ist.read(buffer, 0, 4096); //This is blocking
		    byte[] readdata = new byte[read];
		    System.arraycopy(buffer, 0, readdata, 0, read);
		    dataResponse = new String(readdata);
			//Log.v(ApplicationData.packName, log_tab + " Read the data from the input channel " + dataResponse.length() + " bytes");			
				
		}
		//if the connection is lost when writing or reading this below exception is thrown, so here try to reconnect
		//since the connection is done already done and chances are this could be lost.
		catch(SocketException ex){
			iReconnect++;
			if(iReconnect ==1)
			{
				ex.printStackTrace();
				close(); //flushed all the buffeer
				//Log.v(ApplicationData.packName, log_tab + " Reconneting... " + dataResponse.length() + " bytes");						
				readSocket();
			}
			/*mstConnectionErrDesc = "error connecting to mswipe " + ex.getMessage();
			mstConnectionErrDesc = context.getResources().getString(R.string.error_conencting_to_mswipe);
			*/
			
		}
		catch(SocketTimeoutException ex){
			/*mstConnectionErrDesc = "Error connecting to Mswipe " + ex.getMessage();
			mstConnectionErrDesc = context.getResources().getString(R.string.error_conencting_to_mswipe);
			*/
			ex.printStackTrace();
		
		}
		catch(Exception ex){
			//mstConnectionErrDesc = "Error connecting to Mswipe " + ex.getMessage();
			//mstConnectionErrDesc = context.getResources().getString(R.string.error_conencting_to_mswipe);
			ex.printStackTrace();
		}
		
		if(!isParseCalled)
		{
			isParseCalled = true;
			parse(dataResponse,mstConnectionErrDesc);
		}
		
	}
	
	static class ConnectionSocket implements Runnable {

		@Override
		public void run() {
			
				try{
					//Log.v(ApplicationData.packName, log_tab + " Connecting in back ground");
					socketConnetion();	
				}catch(Exception ex)
				{
					ex.printStackTrace();
				}
				
		}
		
		public static Socket socketConnetion() throws Exception
		{
			
			try 
			{
				//Log.v(ApplicationData.packName, log_tab + " Initializing connection");
				SocketAddress sockaddr = new InetSocketAddress("", 10);             
				if(mSocket==null)
				{
					//Log.v(ApplicationData.packName, log_tab + "Object created");			
					mSocket = new Socket();
					
				}
				//Log.v(ApplicationData.packName, log_tab + "Check for the connection");			
				
				if(!mSocket.isConnected())
				{
					//Log.v(ApplicationData.packName, log_tab + " Connecting...");
					mSocket.setSoTimeout(1000*60);
					mSocket.connect(sockaddr,1000*10);
					//Log.v(ApplicationData.packName, log_tab + " Connected...");
					
				}
				else{
					
					//Log.v(ApplicationData.packName, log_tab + " Connection already established...");

				}
				/*outToServer = new DataOutputStream(
						socket.getOutputStream());
			       
			        inFromServer = new BufferedReader(
			            new InputStreamReader(
			            		socket.getInputStream()));
			    */
				

			} 
			catch (UnknownHostException ex) {
				ex.printStackTrace();
				mstConnectionErrDesc = ex.getMessage();
				throw ex;
				
			} 
			catch (IOException ex) {
				mstConnectionErrDesc = ex.getMessage();
				ex.printStackTrace();
				throw ex;
			} 
			catch (Exception ex) {
				mstConnectionErrDesc = ex.getMessage();
				ex.printStackTrace();
				throw ex;
			}
			
			return mSocket;
		}

	}
	
	static class WriteSocket implements Runnable {

		@Override
		public void run() {
			
				try{
					//socketConnetion();	
				}catch(Exception ex)
				{
					
				}
				
		}
		
		public static void writeSocket(String data) throws Exception
		{
			
			try 
			{
				Socket socket = null;
				if(mSocket == null){
					socket = ConnectionSocket.socketConnetion();
				}
				if(socket.isConnected())
				{
					
					//incase if the socket is disconnection trap this in the exception and
					//try to recoonect
					if(ost == null)
					{
							//Log.v(ApplicationData.packName, log_tab + " Reading the output channel");
							
							/*ost = new BufferedReader(
					            new InputStreamReader(
					            		socket.getInputStream()));;
							 */
							ost= new DataOutputStream( socket.getOutputStream());
						
						
					
					}
					//Log.v(ApplicationData.packName, log_tab + " Writing to the output channel");				
					ost.write(data.getBytes());
				}

			} 
			catch (UnknownHostException ex) {
				ex.printStackTrace();
				mstConnectionErrDesc = ex.getMessage();
				throw ex;
				
			} 
			catch (IOException ex) {
				mstConnectionErrDesc = ex.getMessage();
				ex.printStackTrace();
				throw ex;
			} 
			catch (Exception ex) {
				mstConnectionErrDesc = ex.getMessage();
				ex.printStackTrace();
				throw ex;
			}
			
			
		}

	}
	
	static class ReadSocket implements Runnable {

		@Override
		public void run() {
			
				try{
					
					
					
				}catch(Exception ex)
				{
					
				}
				
		}
		
		
		
		public static void readSocket() throws Exception
		{
			
			try 
			{
				Socket socket = ConnectionSocket.socketConnetion();
				//incase if the socket is disconnection trap this in the exception and
				//try to recoonect
				if(ist == null)
				{
				//Log.v(ApplicationData.packName, log_tab + " Reading the input channel");
						
				ist = socket.getInputStream();
				
				
				}
				byte[] buffer = new byte[4096];
				int read=0;
				while(read != -1){
                    //publishProgress(tempdata);
                    read = ist.read(buffer, 0, 4096); //This is blocking
    			    byte[] readdata = new byte[read];
    			    System.arraycopy(buffer, 0, readdata, 0, read);
    			    //Log.v(ApplicationData.packName, log_tab + " Read the data from the input channel " + new String(readdata));
    				
				}
				

			} 
			catch (UnknownHostException ex) {
				ex.printStackTrace();
				mstConnectionErrDesc = ex.getMessage();
				throw ex;
				
			} 
			catch (IOException ex) {
				mstConnectionErrDesc = ex.getMessage();
				ex.printStackTrace();
				throw ex;
			} 
			catch (Exception ex) {
				mstConnectionErrDesc = ex.getMessage();
				ex.printStackTrace();
				throw ex;
			}
			
			
		}

	}
	
	protected void parse(String text,String errMsg) 
	{		
	}
	

	
}





























