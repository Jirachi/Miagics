package com.miage.jirachi.miagics;

import java.io.IOException;
import java.net.Socket;

import android.util.Log;

public class NetworkThread extends Thread {
    private Socket mSocket;
    
    public NetworkThread(Socket sock) {
        mSocket = sock;
    }
    
    @Override
    public void run() {
        while (!interrupted()) {
            try {
                if (mSocket.getInputStream().available() > 0) {
                    Log.i("TEST", "Data is available");
                }
                

                Thread.sleep(1);
            } catch (IOException e) {
                Log.e("Network", e.getMessage());
            } catch (InterruptedException e) {
               
            }
            
            
        }
    }
}
