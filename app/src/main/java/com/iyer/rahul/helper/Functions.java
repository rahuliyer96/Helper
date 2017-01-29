package com.iyer.rahul.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by rishi on 29-01-2017.
 */

public class Functions {
    SharedPreferences sp1;
    int i;
    MediaPlayer mp;

    public void check(Context context, String phoneNumber, String messageBody){
        Toast.makeText(context, "Checking", Toast.LENGTH_SHORT).show();
        Log.d("XYZ","Checking");

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
                Toast.makeText(context, "password accepted", Toast.LENGTH_SHORT).show();
                Log.d("XYZ","password accepted");


                // Locating------------------
                    if(messageBody.toLowerCase().contains("locate"))
                    {
                        locate(context,phoneNumber);

                    }

                //Ringing
                    if(messageBody.toLowerCase().contains("ring"))
                    {
                        mp=MediaPlayer.create(context,R.raw.ringsound);
                        mp.start();
                        if(messageBody.toLowerCase().contains("stop"))
                        {
                            if(mp.isPlaying())
                            {
                                mp.stop();
                            }

                        }
                    }

            }



    }

    public void locate(Context context, String phoneNumber){
        Toast.makeText(context, "Locating", Toast.LENGTH_SHORT).show();
        Log.d("XYZ","Locating");

    }

    public void ring(Context context)
    {

    }
}
























