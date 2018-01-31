package com.project.avanikan_pc_003.ussd_reader;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {

    EditText editText_call , editText_send , editText_receiver_num ;
    TextView textView_send , textView_received  ;
    ScrollView scrollView_receiver ;
    Button btn_call , btn_send ;
    String USSD_code  , send_sms , address  ;
    ProgressDialog progressDialog ;
    ProgressBar progressBar ;
    public static final String TAG = "===>" ;
    private static MainActivity mainActivity ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this ;

        init();
        Check_Permission_call();




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if( grantResults.length > 0 &&   grantResults[0]  == PackageManager.PERMISSION_GRANTED){

            //resume tasks needing this permission
            Call_USSD_Code();

        }
    }

    public static MainActivity getInstance(){
        return mainActivity ;
    }

    private void Check_Permission_call (){

        //check for permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED )
               {

            //ask for permission
            if (Build.VERSION.SDK_INT >= 23) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 5);

            }




        }

        else {

            Call_USSD_Code();
            send();

        }
    }

    private void Check_Permission_sms (){

        if (  ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){

            //ask for permission
            if (Build.VERSION.SDK_INT >= 23 ){
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 10);
                Log.i(TAG, "Check_Permission_sms: ");
            }

        }else {


            send();

        }
    }

    public void init(){

        editText_call = (EditText) findViewById(R.id.phone_call);
        btn_call = (Button) findViewById(R.id.btn_call) ;
        textView_received = (TextView) findViewById(R.id.textView_receiver);
        textView_send  = (TextView) findViewById(R.id.send);
        btn_send = (Button) findViewById(R.id.btn_send);
        editText_send = (EditText) findViewById(R.id.edit_sms);
        editText_receiver_num = (EditText) findViewById(R.id.edit_receiver);

        scrollView_receiver = (ScrollView) findViewById(R.id.scroll_received);


        textView_send.setVisibility(View.INVISIBLE);
        textView_received.setVisibility(View.INVISIBLE);
        scrollView_receiver.setVisibility(View.INVISIBLE);


    }

    public void Call_USSD_Code () {

        Check_Permission_sms();

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editText_call != null){
                    USSD_code = "*" + editText_call.getText().toString() + Uri.encode("#");
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + USSD_code)));
                }


            }
        });


    }

    private void send(){

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken() , InputMethodManager.HIDE_NOT_ALWAYS);

                if (editText_receiver_num == null || editText_send == null){
                    Toast.makeText(MainActivity.this, "please complete address and message ... ", Toast.LENGTH_LONG).show();
                }else {

                    send_sms = editText_send.getText().toString();
                    textView_send.setText(send_sms);
                    textView_send.setVisibility(View.VISIBLE);
                    address = editText_receiver_num.getText().toString();


                    try{

                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(address , null , send_sms , null, null );
                        Log.i(TAG, "onClick:  sms send !!!1" );
                        showProgressDialog();

                    } catch (Exception e ){e.printStackTrace();}

                }

            }
        });
    }

    public void showProgressDialog(){

        progressDialog  = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Waiting For Sender ... ");
        progressDialog.setCancelable(false);
        progressDialog.show();


    }

    public void receive_sms (final String message){
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                Log.i(TAG, "run: receive ");
                textView_received.setText(message);
                scrollView_receiver.setVisibility(View.VISIBLE);
                textView_received.setVisibility(View.VISIBLE);


            }
        });



    }

}
