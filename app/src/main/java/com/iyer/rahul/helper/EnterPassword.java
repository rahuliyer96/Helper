package com.iyer.rahul.helper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class EnterPassword extends AppCompatActivity {
    EditText etPassword,etConfirmPassword;
    Button btConfirm;
    TextView tvDetails;
    String pass1,pass2;
    SharedPreferences sp1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);

        etPassword=(EditText)findViewById(R.id.etPassword);
        etConfirmPassword=(EditText)findViewById(R.id.etConfirmPassword);
        btConfirm=(Button)findViewById(R.id.btConfirm);
        tvDetails=(TextView)findViewById(R.id.tvDetails);

        sp1=getSharedPreferences("MyPass",MODE_PRIVATE);

        if(!sp1.getString("p","1").equals("1"))
        {
            Intent i = new Intent(getApplicationContext(),Home.class);
            startActivity(i);
            finish();

        }





        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pass1=etPassword.getText().toString();
                pass2=etConfirmPassword.getText().toString();

                if(pass1==null || pass2==null )
                {
                    tvDetails.setText("Password cannot be empty");
                    etPassword.requestFocus();
                }

                else if(pass1.length()<4)
                {
                    tvDetails.setText("Minimum 4 characters required");

                }

                else if(!pass1.equals(pass2))
                {

                    tvDetails.setText("Passswords dont match");
                }

                else
                {
                    SharedPreferences.Editor editor=sp1.edit();
                    editor.putString("p",pass1);
                    editor.commit();

                    Intent i = new Intent(getApplicationContext(),Home.class);
                    startActivity(i);
                    finish();

                }

            }
        });
    }
}
