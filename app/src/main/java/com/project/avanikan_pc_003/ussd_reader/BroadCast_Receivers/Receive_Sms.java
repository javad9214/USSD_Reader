package com.project.avanikan_pc_003.ussd_reader.BroadCast_Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.project.avanikan_pc_003.ussd_reader.MainActivity;


public class Receive_Sms extends BroadcastReceiver {

    public static final String TAG = "===>" ;
    LayoutInflater inflater ;



    @Override
    public void onReceive(Context context, final Intent intent) {




        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String messageReceived = "";


        if (bundle != null) {
            Log.i(TAG, "onReceive: bundle not null  ");
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                Log.i(TAG, "onReceive:  for " + i);
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                messageReceived += msgs[i].getMessageBody().toString();
                messageReceived += "\n";
            }


            String senderPhoneNumber = msgs[0].getOriginatingAddress();
            Log.i(TAG, "onReceive: " + messageReceived + "    :    " + senderPhoneNumber );
            Log.i(TAG, "onReceive: sender phone nomber : "  + senderPhoneNumber);
            if ( senderPhoneNumber.equals("+982000111" ) ) {


                try{
                    Log.i(TAG, "onReceive:  if ... ");
                    MainActivity.getInstance().receive_sms(messageReceived);
                }catch (Exception e){
                    e.printStackTrace();
                }


            }


        }

        }


    }
