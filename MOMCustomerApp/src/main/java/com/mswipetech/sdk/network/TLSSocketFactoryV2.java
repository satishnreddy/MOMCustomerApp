package com.mswipetech.sdk.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * Created by mswipe on 10/11/17.
 */

public class TLSSocketFactoryV2 extends SSLSocketFactory {

    private SSLSocketFactory internalSSLSocketFactory;
    private static final ArrayList<String> mCipherSuitesList = new ArrayList<String>(Arrays.asList("TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384","TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA"));

    public TLSSocketFactoryV2(SSLSocketFactory delegate) throws KeyManagementException, NoSuchAlgorithmException {
        internalSSLSocketFactory = delegate;
    }

    @Override
    public String[] getDefaultCipherSuites() {
        //return internalSSLSocketFactory.getDefaultCipherSuites();
        return setupPreferredDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        //return internalSSLSocketFactory.getSupportedCipherSuites();

        return setupPreferredSupportedCipherSuites();
    }

    @Override
    public Socket createSocket() throws IOException {
        return enableTLSOnSocket(internalSSLSocketFactory.createSocket());
    }

    @Override
    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
        return enableTLSOnSocket(internalSSLSocketFactory.createSocket(s, host, port, autoClose));
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        return enableTLSOnSocket(internalSSLSocketFactory.createSocket(host, port));
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
        return enableTLSOnSocket(internalSSLSocketFactory.createSocket(host, port, localHost, localPort));
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        return enableTLSOnSocket(internalSSLSocketFactory.createSocket(host, port));
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        return enableTLSOnSocket(internalSSLSocketFactory.createSocket(address, port, localAddress, localPort));
    }

    private Socket enableTLSOnSocket(Socket socket) {

        if(socket != null && (socket instanceof SSLSocket)) {
            ((SSLSocket)socket).setEnabledProtocols(new String[] {"TLSv1.2"});
        }

        if(socket != null)
        {
            ((SSLSocket)socket).setEnabledCipherSuites(mCipherSuitesList.toArray(new String[mCipherSuitesList.size()]));
        }

        return socket;
    }

    private static String[] setupPreferredDefaultCipherSuites()
    {
        return mCipherSuitesList.toArray(new String[mCipherSuitesList.size()]);
    }

    private static String[] setupPreferredSupportedCipherSuites()
    {
        return mCipherSuitesList.toArray(new String[mCipherSuitesList.size()]);
    }
}