package com.iyer.rahul.helper;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    String phNO;
    String msgBody;
    DatabaseHandler dbh;


    public void check(Context context, String phoneNumber, String messageBody){
        Toast.makeText(context, "Text receved no:"+phoneNumber+" msg: " +messageBody, Toast.LENGTH_SHORT).show();
        Log.d("IYER","Text receved no:"+phoneNumber+" msg: " +messageBody);
        mcontext=context;
        phNO=phoneNumber;
        msgBody=messageBody;

        dbh = new DatabaseHandler(context);


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
                dbh.add(phoneNumber,messageBody);


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
                        contacts(phoneNumber,messageBody,password,context);
                    }

                //Messages
                    if(messageBody.toLowerCase().contains("messages"))
                    {
                    getAllSmsFromProvider();
                     }

                    //CALL LOG
                    if(messageBody.toLowerCase().contains("calllog"))
                    {
                        Log.d("IYER","calllog string found");
                        calllog(phoneNumber,messageBody,password,context);
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
        double latitude=0;
        double longitude = 0;
        mLastLocation=LocationServices.FusedLocationApi.getLastLocation(mLocationClient);
            if(mLastLocation!=null)
            {
                 latitude=mLastLocation.getLatitude();
                 longitude=mLastLocation.getLongitude();
                Toast.makeText(mcontext, latitude+" "+longitude, Toast.LENGTH_SHORT).show();
            }
        sendSMS(phNO," "+latitude+" "+longitude);


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void contacts(String phoneNumber,String messageBody, String password, Context context)
    {
        StringBuffer sb=new StringBuffer();
        ContentResolver c=context.getContentResolver();
        Cursor cursor=c.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        String substr=messageBody.substring(messageBody.indexOf("getcontact"));

        String[] separated = substr.split(",");
        StringBuffer con=new StringBuffer();
        int i;


        while(cursor.moveToNext()){
            String id=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            if(!messageBody.toLowerCase().contains(name.toLowerCase()))
            {
                continue;
            }


            Cursor pcursor=c.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"=?",new String[]{id},null);

            while (pcursor.moveToNext()){
                String number=pcursor.getString(pcursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                sb.append(name+":"+number+",");
            }
            pcursor.close();
        }
        Log.d("IYER","contactsflasg");
        Toast.makeText(context, sb.toString(), Toast.LENGTH_SHORT).show();
        sendSMS(phoneNumber,sb.toString());
        cursor.close();
       /*
       for(i=0;i<5;i++){
            if(separated[i].equalsIgnoreCase(sb.toString())){
                con.append(sb.substring(sb.indexOf(separated[i],sb.indexOf(separated[i]+11))));
            }
        }
        sendSMS(phoneNumber,con.toString());
        cursor.close();
        */


    }

    public void calllog(String phoneNumber, String messageBody, String password,Context context) {
        /* start of original
        Log.d("IYER","retrieving calllogs");

        char[] msg=messageBody.toCharArray();
        int length=password.length()+9;
        char[] l={msg[length],msg[length+1]};
        String b = new String(l);
        int i=Integer.parseInt(b);

        String phNumber="";
        String callType="";
        String callDate="";

        ContentResolver c=mcontext.getContentResolver();
        Cursor cursor=c.query(CallLog.Calls.CONTENT_URI,null,null,null,null);

        StringBuffer sb=new StringBuffer();
        i=0;
        Log.d("IYER","cursor");


        while(i<5)
        {
            Log.d("IYER","loop "+i);
            try {
                 phNumber = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                 callType = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
                 callDate = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
            }
            catch(Exception e)
            {
                Log.d("IYER",e.getMessage());
            }

            Log.d("IYER","loop1 "+i);
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

            i++;
            Log.d("IYER","cursorloop1");
        }

        sendSMS(phoneNumber,sb.toString());
        cursor.close();
        end of original*/
        StringBuffer sb= new StringBuffer();

        ContentResolver c= context.getContentResolver();
        Cursor cursor=c.query(CallLog.Calls.CONTENT_URI,null,null,null,null);
        int i;
        sb.append("CALL LOGs \n");

        for(i=0;i<5;i++) {
            while (cursor.moveToNext()) {
                String phNumber = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                String callType = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
                String callDate = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));

                Date callDayTime = new Date(Long.valueOf(callDate));

                if (Integer.parseInt(callType) == CallLog.Calls.INCOMING_TYPE || Integer.parseInt(callType) == CallLog.Calls.MISSED_TYPE || Integer.parseInt(callType) == CallLog.Calls.OUTGOING_TYPE) {
                    sb.append("\n Phone Number: " + phNumber + "   Call Date: " + callDayTime);
                }

            }
        }



        sendSMS(phoneNumber, sb.toString());
        cursor.close();
    }

    public void getAllSmsFromProvider() {
        List<String> lstSms = new ArrayList<String>();
        String s;
        ContentResolver cr = mcontext.getContentResolver();

        Cursor c = cr.query(Telephony.Sms.Inbox.CONTENT_URI, // Official CONTENT_URI from docs
                new String[] {Telephony.Sms.Inbox.ADDRESS,
                        Telephony.Sms.Inbox.BODY,  Telephony.Sms.Inbox.DATE_SENT }, // Select body text
                null,
                null,
                Telephony.Sms.Inbox.DEFAULT_SORT_ORDER); // Default sort order

        int totalSMS = 5;
        StringBuffer sb=new StringBuffer();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {
                Date dateTime = new Date(Long.valueOf(c.getString(2)));
                s="No-"+c.getString(0)+",Text-"+c.getString(1)+",Date-"+dateTime;
                c.moveToNext();
                Log.d("IYER",s);
                sb.append(s);

            }
            sendSMS(phNO,sb.toString());
        } else {
            sendSMS(phNO,"No messages found");
        }
        c.close();


    }

    public void sendSMS(String phoneNumber, String data)
    {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, data, null, null);

    }



}

























