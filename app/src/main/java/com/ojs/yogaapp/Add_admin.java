package com.ojs.yogaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Add_admin extends AppCompatActivity {
    EditText name, username, password;
    Button insert;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);
        name = (EditText) findViewById(R.id.name);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        insert = (Button) findViewById(R.id.confirm);
        DB = new DBHelper(this);

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fn = name.getText().toString();
                String uname = username.getText().toString();
                String pwd = password.getText().toString();

                Boolean checkinsertdata = DB.insertData(fn, uname, pwd);
                if (checkinsertdata == true)
                    Toast.makeText(Add_admin.this, "Details added", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Add_admin.this, "Details not added", Toast.LENGTH_SHORT).show();

            }
        });



    }
}