package com.iyer.rahul.helper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.FileOutputStream;

public class Help extends AppCompatActivity {

    TextView tvHelp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        tvHelp=(TextView)findViewById(R.id.tvHelp);



      tvHelp.setText("List of Commands \n 1.To ring the phone - \n <password> ring \n");

    }
}
