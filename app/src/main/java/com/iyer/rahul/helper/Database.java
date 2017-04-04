package com.iyer.rahul.helper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Database extends AppCompatActivity {

    TextView tvData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        tvData= (TextView) findViewById(R.id.tvData);

        final DatabaseHandler dbh=new DatabaseHandler(this);

        String data=dbh.getAll();
        tvData.setText(data);
    }
}
