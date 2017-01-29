package com.iyer.rahul.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {
    static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    Functions f1=new Functions();

    @Override

    public void onReceive(Context context, Intent intent) {

        Log.d("XYZ","SMS Received");
        Toast.makeText(context, "SMS received", Toast.LENGTH_SHORT).show();

        if (intent.getAction() != null && (intent.getAction().equals(SMS_RECEIVED))) {

            // ---get the SMS message passed in---

            Bundle bundle = intent.getExtras();

            if (bundle != null) {

                // ---retrieve the SMS message received---

                Object[] pdus = (Object[]) bundle.get("pdus");


                for (int i = 0; i < pdus.length; i++) {


                    //Need to check diff functions of SmsMesage  like getmsgbody works.
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

                    String messageBody = currentMessage.getMessageBody();
                    String phoneNumber = currentMessage.getOriginatingAddress();
                    String displayMessageBody = currentMessage.getDisplayMessageBody();

                    f1.check(context,phoneNumber,messageBody);


                } // end for loop

            }

        }

    }
}
