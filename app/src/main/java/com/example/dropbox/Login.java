package com.example.dropbox;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static com.example.dropbox.Constants.PORT;
import static com.example.dropbox.Constants.SERVER_IP;


public class Login extends AppCompatActivity {

    EditText email;
    EditText password;
    private Button btnLogin;
    private TextView registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.button_login);
        registration = findViewById(R.id.registration);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClientTask clientTask = new ClientTask(SERVER_IP, PORT, email.getText().toString() + password.getText().toString());
                clientTask.execute();

            }
        });

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);
            }
        });
    }

    public class ClientTask extends AsyncTask<String, Void, String> {
        String dstIP;
        int dstPort;

        Socket socket = null;
        DataOutputStream out = null;
        DataInputStream in = null;

        public ClientTask(String IP, int port, String s) {
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
                out.writeUTF("/auth" + " " + email.getText().toString() + " " + password.getText().toString());
                serverMessage = in.readUTF();
                if (serverMessage.startsWith("/authok")) {
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                } else if(serverMessage.startsWith("/authfailed")){
                    Toast.makeText(Login.this, "Failed", Toast.LENGTH_SHORT).show();
                }
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
