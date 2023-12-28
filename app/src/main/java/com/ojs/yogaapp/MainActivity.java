package com.ojs.yogaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText username, password;
    Button login;
    DBHelper DB;
    String correct_username = "123";
    String correctpassword = "123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        DB = new DBHelper(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                if (user.equals("")||pass.equals(""))
                    Toast.makeText(MainActivity.this,"Please enter your credentials",Toast.LENGTH_SHORT).show();
                else {
                    Boolean checkuserpass = DB.checkusernamepassword(user, pass);
                    if (checkuserpass == true){
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else if (username.getText().toString().equals(correct_username) || password.getText().toString().equals(correctpassword)) {
                        Intent intent = new Intent(MainActivity.this, HomePage.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this,"Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }



}