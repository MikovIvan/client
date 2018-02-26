package com.example.dropbox;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import static com.example.dropbox.constants.Constants.PORT;
import static com.example.dropbox.constants.Constants.SERVER_IP;

public class MainActivity extends AppCompatActivity {

    private ListView fileListView;
    private String[] fileName;

    TextView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        ClientTask clientTask = new ClientTask(SERVER_IP,PORT);
        clientTask.execute();
    }

    public class ClientTask extends AsyncTask<String[], Void, String[]> {
        String dstIP;
        int dstPort;

        Socket socket = null;
        OutputStream out = null;
        InputStream in = null;
        ObjectInputStream inputObj = null;
        ObjectOutputStream outputObj = null;

        public ClientTask(String IP, int port) {
            this.dstIP = IP;
            this.dstPort = port;
        }

        @Override
        protected String[] doInBackground(String[]... params) {

            try {
                socket = new Socket(SERVER_IP, PORT);
                out = socket.getOutputStream();
                in = socket.getInputStream();
                outputObj = new ObjectOutputStream(out);
                inputObj = new ObjectInputStream(in);
                outputObj.writeObject("/getfiles");
                Object msg = new Object();
                msg = inputObj.readObject();
                if(msg instanceof String[]){
                    fileName = (String[]) msg;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
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
            return fileName;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            if(strings == null){
               view.setText("fail");
            } else {
                fileListView = findViewById(R.id.fileslist);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, strings);
                fileListView.setAdapter(adapter);
            }
        }
    }
}
