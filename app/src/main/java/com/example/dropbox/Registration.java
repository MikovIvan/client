package com.example.dropbox;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import static com.example.dropbox.constants.Constants.PORT;
import static com.example.dropbox.constants.Constants.SERVER_IP;

public class Registration extends AppCompatActivity {

    private EditText regName;
    private EditText regSurname;
    private EditText regEmail;
    private EditText regPassword;
    private EditText regConfirmPassword;
    private Button regBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        regName = findViewById(R.id.reg_name);
        regSurname = findViewById(R.id.reg_surname);
        regEmail = findViewById(R.id.reg_email);
        regPassword = findViewById(R.id.reg_password);
        regConfirmPassword = findViewById(R.id.reg_confirmpassword);
        regBtn = findViewById(R.id.button_registration);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClientTask clientTask = new ClientTask(SERVER_IP, PORT, regName.getText().toString() + regSurname.getText().toString()+
                        regEmail.getText().toString()+regPassword.getText().toString()+regConfirmPassword.getText().toString());
                clientTask.execute();


            }
        });
    }

    public class ClientTask extends AsyncTask<String, Void, String> {
        String dstIP;
        int dstPort;

        Socket socket = null;
        OutputStream out = null;
        InputStream in = null;
        ObjectInputStream inputObj = null;
        ObjectOutputStream outputObj = null;

        public ClientTask(String IP, int port, String s) {
            this.dstIP = IP;
            this.dstPort = port;
        }

        @Override
        protected String doInBackground(String... params) {
            String serverMessage = "Server message";

            try {
                socket = new Socket(SERVER_IP, PORT);
                out = socket.getOutputStream();
                in = socket.getInputStream();
                outputObj = new ObjectOutputStream(out);
                inputObj = new ObjectInputStream(in);
                outputObj.writeObject("/reg" + " " + regName.getText().toString()+ " " + regSurname.getText().toString()+ " "+
                        regEmail.getText().toString()+ " "+regPassword.getText().toString());
                serverMessage = String.valueOf(inputObj.readObject());
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
            return serverMessage;
        }

        @Override
        protected void onPostExecute(String serverMessage) {
            super.onPostExecute(serverMessage);
            if(serverMessage.startsWith("/regok")){
                Intent intent = new Intent(Registration.this, Login.class );
                startActivity(intent);
            } else if(serverMessage.startsWith("/regfailed")){
                Toast.makeText(Registration.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
