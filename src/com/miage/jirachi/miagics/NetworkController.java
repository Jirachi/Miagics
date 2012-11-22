package com.miage.jirachi.miagics;

import java.io.IOException;
import java.net.Socket;

import android.util.Log;

public class NetworkController {
    private static NetworkController mSingleton = null;
    private Socket mSocket;
    private NetworkThread mThread;
    
    
    public static NetworkController getInstance() {
        if (mSingleton == null) {
            mSingleton = new NetworkController();
        }
        
        return mSingleton;
    }
    
    public NetworkController() {
       
    }
    
    public void connect(String ip, int port) throws IOException {
        mSocket = new Socket(ip, port);
        mSocket.setTcpNoDelay(true);
        mSocket.setOOBInline(true);
        
        mThread = new NetworkThread(mSocket);
        mThread.start();
    }
    
    public void update() {
        
    }
    
    public void send(BitStream packet) {
        try {
            mSocket.getOutputStream().write(packet.getBytesP());
        } catch (IOException e) {
            Log.e("Reseau", "Impossible d'envoyer un packet!");
        }
    }
}
