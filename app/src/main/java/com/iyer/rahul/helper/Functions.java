package com.iyer.rahul.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by rishi on 29-01-2017.
 */

public class Functions implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    SharedPreferences sp1;
    int i;
    MediaPlayer mp;
    GoogleApiClient mLocationClient;
    Location mLastLocation;
    Context mcontext;


    public void check(Context context, String phoneNumber, String messageBody){
        Toast.makeText(context, "Text receved no:"+phoneNumber+" msg: " +messageBody, Toast.LENGTH_SHORT).show();
        Log.d("IYER","Text receved no:"+phoneNumber+" msg: " +messageBody);
        mcontext=context;

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
                Log.d("IYER","password accepted");


                // Locating------------------
                    if(messageBody.toLowerCase().contains("locate"))
                    {
                        locate(context,phoneNumber);

                    }

                //Ringing
                    if(messageBody.toLowerCase().contains("ring"))
                    {
                        mp=MediaPlayer.create(context,R.raw.ringsound);
                        if(!messageBody.toLowerCase().contains("stop"))
                        {

                            mp.start();
                        }

                        if(messageBody.toLowerCase().contains("stop"))
                        {
                            mp.pause();
                            mp.stop();
                        }
                    }

            }



    }

    public void locate(Context context, String phoneNumber){
        Toast.makeText(context, "Locating", Toast.LENGTH_SHORT).show();
        Log.d("XYZ","Locating");
        GoogleApiClient.Builder builder=new GoogleApiClient.Builder(context);
        builder.addApi(LocationServices.API);
        builder.addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this);
        builder.addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this);
        mLocationClient=builder.build();
            if(mLocationClient!=null)
            {
                mLocationClient.connect();
            }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLastLocation=LocationServices.FusedLocationApi.getLastLocation(mLocationClient);
            if(mLastLocation!=null)
            {
                double latitude=mLastLocation.getLatitude();
                double longitude=mLastLocation.getLongitude();
                Toast.makeText(mcontext, latitude+" "+longitude, Toast.LENGTH_SHORT).show();
            }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
























