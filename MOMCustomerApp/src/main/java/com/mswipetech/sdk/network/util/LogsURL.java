package com.mswipetech.sdk.network.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by satish on 9/18/13.
 */
public class LogsURL
{

	public static final boolean IS_DEBUGGING_ON = true;
	public static final String log_foldername = "mswipe";
	public static final String log_filename = "MS_URL_Logs";
	public static final String log_filextention = ".txt";
	public static final boolean fileWriteEnabled = true;
	public static final boolean adbWriteEnabled = true;
	public static LogsURL instance = new LogsURL();
	private boolean fileWriteCreationFailed = false;
	private boolean disableLogsTemporarly = false;

	//BufferedWriter bufWriter = null;
	File file = null;

	public void setDisableLogsTemporarly(boolean isableLogsTemporarly)
	{
		disableLogsTemporarly = isableLogsTemporarly;
	}

	public static void v (String LOG_TAG, String msg, boolean writeToFile, boolean writeToADB)
	{
		if (IS_DEBUGGING_ON)
		{
			final String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
			final String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
			final String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
			final int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
			//final String msgMetaData = "##" + Thread.currentThread() + "## " + className + "." + methodName + "() ## ln " + lineNumber + " ## msg => " + msg;
			final String msgMetaData =  "\n" + msg + "\n\n";

			if(instance.disableLogsTemporarly)
				return;

			if(fileWriteEnabled && writeToFile)
			{
				instance.writeToFile(msgMetaData);
			}

			if(adbWriteEnabled  && writeToADB )
			{
				Log.v(LOG_TAG, msgMetaData);
			}
		}
	}

	private void writeToFile(final String msg)
	{
		if(!fileWriteCreationFailed)
		{
			try{

				File fileWriter = getFileWriter();
				String terminalTime = new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
				BufferedWriter buf = new BufferedWriter(new FileWriter(fileWriter, true));
				buf.append(msg);
				buf.close();

			}catch(Exception ex){

				ex.printStackTrace();
			}
		}
	}

	private File getFileWriter() throws IOException
	{
		if(file == null) {
			file = createFileWriter();
		}
		else if(file.length() > 300000){	// after 300000 = 300KB we are creating new log file

			try {

				String root = Environment.getExternalStorageDirectory().toString();
				if (appModuleContext != null){
					root = appModuleContext.getExternalFilesDir(null).toString();
				}
				File myDir = new File(root + "/" + log_foldername);
				String terminalTime = new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
				File to = new File(myDir, log_filename + terminalTime + log_filextention);

				if(file.exists())
					file.renameTo(to);

			} catch (Exception e) {
				// TODO: handle exception
			}

			file = createFileWriter();

		}
		return file;
	}

	private File createFileWriter()
	{
		File filelog = null;

		try
		{
			String root = Environment.getExternalStorageDirectory().toString();
			if (appModuleContext != null){
				root = appModuleContext.getExternalFilesDir(null).toString();
			}
			File myDir = new File(root + "/" + log_foldername);
			myDir.mkdirs();
			filelog = new File (myDir, log_filename + log_filextention);
			filelog.createNewFile();

		} catch(Exception err) {

			fileWriteCreationFailed = true;
			err.printStackTrace();

		}

		return filelog;

	}

	private static Context appModuleContext = null;
	public static void setContext(Context context){
		appModuleContext = context;
		Log.d("n/wsdk url", "setContext: appModuleContext");
	}
}
