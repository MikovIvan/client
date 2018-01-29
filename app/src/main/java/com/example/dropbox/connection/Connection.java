package com.example.dropbox.connection;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connection implements Runnable{
    private final int PORT = 8189;
    private final String SERVER_IP = "localhost";
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
//
//    public Connection() {
//        try {
//            socket = new Socket(SERVER_IP, PORT);
//            in = new DataInputStream(socket.getInputStream());
//            out = new DataOutputStream(socket.getOutputStream());
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void run() {
        try {
            socket = new Socket(SERVER_IP, PORT);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
