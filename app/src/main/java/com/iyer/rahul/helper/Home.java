package com.iyer.rahul.helper;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {
    Button btDatabase,btHelp,btChangePassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btDatabase=(Button)findViewById(R.id.btDatabase);
        btChangePassword=(Button)findViewById(R.id.btChangePassword);
        btHelp=(Button)findViewById(R.id.btHelp);



        btChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),ChangePassword.class);
                startActivity(i);
            }
        });

        btDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Database.class);
                startActivity(i);
            }
        });

        btHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Help.class);
                startActivity(i);
            }
        });


    }
}
