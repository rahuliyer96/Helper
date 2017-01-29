package com.iyer.rahul.helper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePassword extends AppCompatActivity {
    EditText etNewPass,etOldPass,etConfirmNewPass;
    Button btConfirm;
    String pass,pass1,pass2,password;
    SharedPreferences sp1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        etOldPass=(EditText)findViewById(R.id.etOldPass);
        etNewPass=(EditText)findViewById(R.id.etNewPass);
        etConfirmNewPass=(EditText)findViewById(R.id.etConfirmNewPass);

        btConfirm=(Button)findViewById(R.id.btConfirm);

        sp1=getSharedPreferences("MyPass",MODE_PRIVATE);

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pass=etOldPass.getText().toString();
                pass1=etNewPass.getText().toString();
                pass2=etConfirmNewPass.getText().toString();
                password=sp1.getString("p","1");

                    if(!password.equals(pass))
                    {
                        etOldPass.setError("Incorrect Password entered");
                        etOldPass.requestFocus();

                    }
                    else
                    {

                        if(pass1==null)
                        {
                           etNewPass.setError("Password cannot be empty");
                            etNewPass.requestFocus();
                        }

                        else if(pass2==null)
                        {
                            etConfirmNewPass.setError("Password cannot be empty");
                            etConfirmNewPass.requestFocus();

                        }

                        else if(pass1.length()<4)
                        {
                            etNewPass.setError("Minimum length of 4 required");
                            etNewPass.requestFocus();

                        }

                        else if(!pass1.equals(pass2))
                        {

                            etConfirmNewPass.setError("Passwords dont match");
                        }

                        else
                        {
                            SharedPreferences.Editor editor=sp1.edit();
                            editor.putString("p",pass1);
                            editor.commit();

                            Toast.makeText(getApplicationContext(), "Password changed successfully",
                                    Toast.LENGTH_LONG).show();

                            Intent i = new Intent(getApplicationContext(),Home.class);
                            startActivity(i);
                            finish();

                        }

                    }

            }
        });


    }
}
