package com.example.dropbox;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static com.example.dropbox.constants.Constants.PORT;
import static com.example.dropbox.constants.Constants.SERVER_IP;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ClientTask clientTask = new ClientTask(SERVER_IP,PORT);
        clientTask.execute();
    }

    public class ClientTask extends AsyncTask<String, Void, String> {
        String dstIP;
        int dstPort;

        Socket socket = null;
        DataOutputStream out = null;
        DataInputStream in = null;

        public ClientTask(String IP, int port) {
            this.dstIP = IP;
            this.dstPort = port;
        }

        @Override
        protected String doInBackground(String... params) {
            String serverMessage = "Server message";

            try {
                socket = new Socket(SERVER_IP, PORT);
                out = new DataOutputStream(socket.getOutputStream());
                in = new DataInputStream(socket.getInputStream());
                out.writeUTF("/getfiles");
                serverMessage = in.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return serverMessage;
        }


    }
}
