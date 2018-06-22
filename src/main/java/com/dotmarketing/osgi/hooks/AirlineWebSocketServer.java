package com.dotmarketing.osgi.hooks;


import com.dotcms.repackage.org.directwebremoting.guice.ApplicationScoped;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.portlets.contentlet.ajax.ContentletAjax;
import com.dotmarketing.portlets.contentlet.business.ContentletAPI;
import com.dotmarketing.portlets.contentlet.business.ContentletAPIPostHook;
import com.dotmarketing.portlets.contentlet.business.ContentletAPIPostHookAbstractImp;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.util.Logger;
import com.google.gson.stream.JsonReader;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import javax.inject.Inject;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AirlineWebSocketServer  {

    private Object context = null;
    private ArrayList<Socket> clients;
    private ServerSocket server = null;
    private Socket client = null;
    private DataOutputStream out = null;
    private DataInputStream in = null;

    public AirlineWebSocketServer(Object context) throws IOException {
        this.context = context;


        Logger.info( context, "+++++++++++++++++++++++++++++++++++++++++++++++ \n" );
        Logger.info( context, "INSIDE AirlineWebSocketServer.start() - Websocket" );
        Logger.info( context, "+++++++++++++++++++++++++++++++++++++++++++++++ \n" );




        //read message from client
//        Logger.info(context, "Client says: "+ in.readUTF());

        //send message to client
//        out.writeUTF("Thank you for connecting to websocket");



        Logger.info(context, "Server Socket running!!");

    }


    //Get Data from dotCMS using contentletAPI
    private Contentlet getContentData(String id){
        return null;
    }

    public void start( int port) throws IOException, NoSuchAlgorithmException{
        this.server = new ServerSocket(port);

        Logger.info(context, "Server has started on 127.0.0.1:"+port+".\\r\\nWaiting for a connection...");

        this.connectClient();
    }

    public void start() throws IOException, NoSuchAlgorithmException{
        this.start(81);
    }

    public void stop() throws IOException{
        this.server.close();
    }

    private void connectClient() throws IOException, NoSuchAlgorithmException{
        try {

            this.client = server.accept();

            Logger.info(context, "Client with IP = "+ client.getInetAddress()+" has joined.");

            this.setUpStreams();

            //handle handshake
            this.initiateHandshake();

            this.clients.add(client);

            //send all data from contentlets
            this.sendContentletsData();

        }catch(SocketTimeoutException e){
            e.printStackTrace();
            throw e;
        }
    }

    private void setUpStreams() throws IOException{
        this.out = new DataOutputStream(client.getOutputStream());
        this.in = new DataInputStream(client.getInputStream());
    }

    public DataOutputStream getDataOutputStream(){
        return this.out;
    }

    public DataInputStream getINputDataInputStream(){
        return this.in;
    }

    public Socket getClient(){
        return this.client;
    }

    public ServerSocket getServer(){
        return this.server;
    }

    private void initiateHandshake() throws IOException, NoSuchAlgorithmException {
        String data = new Scanner(in,"UTF-8").useDelimiter("\\r\\n\\r\\n").next();
        Logger.info(context, "Replying to handshake .. \n");
        Logger.info(context, "Received Header:  \n");
        Logger.info(context, "\n"+data+"\n");
        Matcher get = Pattern.compile("^GET").matcher(data);

        if (get.find()) {
            Matcher match = Pattern.compile("Sec-WebSocket-Key: (.*)").matcher(data);
            match.find();
            byte[] response = ("HTTP/1.1 101 Switching Protocols\r\n"
                    + "Connection: Upgrade\r\n"
                    + "Upgrade: websocket\r\n"
                    + "Sec-WebSocket-Accept: "
                    + DatatypeConverter
                    .printBase64Binary(
                            MessageDigest
                                    .getInstance("SHA-1")
                                    .digest((match.group(1) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11")
                                            .getBytes("UTF-8")))
                    + "\r\n\r\n")
                    .getBytes("UTF-8");

            out.write(response, 0, response.length);
        }
    }

    private void sendContentletsData() throws IOException{
//        APILocator.getContentletAPI()
//        AirlineContentletService aContentletService = new AirlineContentletService();
//        out.writeUTF(aContentletService.findAllAirlineListing().toString());
        out.writeUTF("Server Data");
//        client.
    }


}
