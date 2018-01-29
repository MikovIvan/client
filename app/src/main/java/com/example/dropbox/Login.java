package com.example.dropbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Login extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button btnLogin;
    private TextView registration;

    private final int PORT = 8189;
    private final String SERVER_IP = "localhost";
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//       new Thread(new com.example.dropbox.connection.Connection()).start();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.button_login);
        registration = findViewById(R.id.registration);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth();

            }
        });
    }

    public void onClick(View view) {
        if (view.equals(R.id.registration)) {
            Intent intent = new Intent(Login.this, Registration.class);
            startActivity(intent);
        }
    }


    public void start() {

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    try {
                        socket = new Socket(SERVER_IP, PORT);
                        in = new DataInputStream(socket.getInputStream());
                        out = new DataOutputStream(socket.getOutputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    while (true) {
                        String msg = in.readUTF();
                        if (msg.startsWith("/authok")) {
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            break;
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread1.start();
    }

    public void auth() {
        if (socket == null || socket.isClosed()) start();
        try {
            Log.v("EditText", email.getText().toString());
            Log.v("EditText", password.getText().toString());
            out.writeUTF("/auth " + email.getText() + " " + password.getText());
            email.setText("");
            password.setText("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
