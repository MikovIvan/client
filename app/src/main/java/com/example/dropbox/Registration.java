package com.example.dropbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Registration extends AppCompatActivity {

    private EditText regName;
    private EditText regSurname;
    private EditText regEmail;
    private EditText regPassword;
    private EditText regConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        regName = findViewById(R.id.reg_name);
        regSurname = findViewById(R.id.reg_surname);
        regEmail = findViewById(R.id.reg_email);
        regPassword = findViewById(R.id.reg_password);
        regConfirmPassword = findViewById(R.id.reg_confirmpassword);
    }

    public void onClick(View view) {
        Intent intent = new Intent(Registration.this, Login.class );
        startActivity(intent);
    }
}
