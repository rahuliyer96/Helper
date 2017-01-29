package com.iyer.rahul.helper;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.Date;

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
                            sendSMS(phoneNumber,"Succesfully Rung");
                        }

                        if(messageBody.toLowerCase().contains("stop"))
                        {
                            mp.pause();
                            mp.stop();
                        }
                    }

                    //Contacts
                    if(messageBody.toLowerCase().contains("contact"))
                    {
                        contacts(phoneNumber,messageBody,password);
                    }

                    //CALL LOG
                    if(messageBody.toLowerCase().contains("calllog"))
                    {
                        Log.d("IYER","calllog string found");
                        calllog(phoneNumber,messageBody,password);
                    }

            }



    }



    public void locate(Context context, String phoneNumber){
        Toast.makeText(context, "Locating", Toast.LENGTH_SHORT).show();
        Log.d("IYER","Locating");
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

    public void contacts(String phoneNumber,String messageBody, String password)
    {


    }

    public void calllog(String phoneNumber, String messageBody, String password) {
        Log.d("IYER","retrieving calllogs");
        /*
        char[] msg=messageBody.toCharArray();
        int length=password.length()+9;
        char[] l={msg[length],msg[length+1]};
        String b = new String(l);
        int i=Integer.parseInt(b);
        */

        ContentResolver c=mcontext.getContentResolver();
        Cursor cursor=c.query(CallLog.Calls.CONTENT_URI,null,null,null,null);

        StringBuffer sb=new StringBuffer();
        i=0;
        Log.d("IYER","cursor");

        while(cursor.moveToNext())
        {
            String phNumber=cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            String callType=cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
            String callDate=cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));

            Date callDayTime = new Date(Long.valueOf(callDate));

            if(Integer.parseInt(callType)==CallLog.Calls.MISSED_TYPE)
            {
                sb.append("No-"+phNumber+" Date-"+callDayTime+"/Missed\n");
            }
            if(Integer.parseInt(callType)==CallLog.Calls.INCOMING_TYPE)
            {
                sb.append("No-"+phNumber+" Date-"+callDayTime+"/Incoming\n");
            }
            if(Integer.parseInt(callType)==CallLog.Calls.OUTGOING_TYPE)
            {
                sb.append("No-"+phNumber+" Date-"+callDayTime+"/Outgoing\n");
            }
            if(i==5)
            {
                break;
            }
            i++;
            Log.d("IYER","cursorloop1");
        }

        sendSMS(phoneNumber,sb.toString());
    }

    public void sendSMS(String phoneNumber, String data)
    {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, data, null, null);

    }



}

























