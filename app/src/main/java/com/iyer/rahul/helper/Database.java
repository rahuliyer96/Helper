package com.iyer.rahul.helper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Database extends AppCompatActivity {

    TextView tvData;
    Button clearButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        tvData= (TextView) findViewById(R.id.tvData);
        clearButton=(Button)findViewById(R.id.clearButton);

        final DatabaseHandler dbh=new DatabaseHandler(this);

        String data=dbh.getAll();
        tvData.setText(data);

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbh.delete();
                tvData.setText("");
            }
        });
    }


}
