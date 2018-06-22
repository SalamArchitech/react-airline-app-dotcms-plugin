package com.dotmarketing.osgi.hooks;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class SocketThread extends Thread implements Runnable {

    private AirlineWebSocketServer aWSocket;

    public SocketThread(Object context) throws IOException{
        aWSocket = new AirlineWebSocketServer(context);
    }

    public AirlineWebSocketServer getSocketServerClass(){
        return this.aWSocket;
    }

    @Override
    public void run() {

        //start the webScoket once the thread starts
        try {
            aWSocket.start(81);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Override
    public void interrupt() {

        //Stop the server before ending the Thread
        try {
            aWSocket.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.interrupt();
    }
}
