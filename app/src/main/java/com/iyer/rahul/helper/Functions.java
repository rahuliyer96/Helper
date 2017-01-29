package com.iyer.rahul.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by rishi on 29-01-2017.
 */

public class Functions {
    SharedPreferences sp1;
    int i;

    public void check(Context context, String phoneNumber, String messageBody){
        Toast.makeText(context, "Checking", Toast.LENGTH_SHORT).show();

        sp1=context.getSharedPreferences("MyPass",MODE_PRIVATE);
        String password=sp1.getString("p","1");
        char[] pass=password.toCharArray();

        char[] msg=messageBody.toCharArray();

        for(i=0;i<password.length();i++)
        {
            if(pass[i]==msg[i])
            {
                continue;
            }
            break;
        }
            if(i==password.length())
            {
                    if(messageBody.toLowerCase().contains("locate"))
                    {
                        locate(context,phoneNumber);
                    }

            }



    }

    public void locate(Context context, String phoneNumber){
        Toast.makeText(context, "Locating", Toast.LENGTH_SHORT).show();

    }
}
























