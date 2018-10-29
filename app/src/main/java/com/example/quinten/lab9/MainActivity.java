/*
* Created by Quinten Whitaker
* Assignment: Lab 9
* Date: 10/28/18
*/

package com.example.quinten.lab9;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button missed,outgoing,incoming;
    TextView textView;
    String[] myColumnProjection = new String[]{
            CallLog.Calls.NUMBER,
            CallLog.Calls.DATE,
            CallLog.Calls.TYPE
    };
    String selection = null;

    ContentResolver contentResolver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        missed = (Button)findViewById(R.id.missedButton);
        outgoing = (Button)findViewById(R.id.outgoingButton);
        incoming = (Button)findViewById(R.id.incomingButton);
        textView = (TextView)findViewById(R.id.callTV);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, 20 );
        contentResolver  = getContentResolver();

    }
    public static String callType(int type)
    {
        String call;
        switch(type)
        {
            case 1:
                call =  "Incoming";
                break;
            case 2:
                call =  "Outgoing";
                break;
            case 3:
                call =  "Missed";
                break;
            default:
                call = "Unknown Type";
                break;
        }

        return call;

    }
    public static String epochToDate(long time)
    {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        return format.format(new Date(time));
    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.missedButton:
                displayMissed();
                break;
            case R.id.outgoingButton:
                displayOutgoing();
                break;
            case R.id.incomingButton:
                displayIncoming();
                break;
        }
    }

    public void displayMissed()
    {
        selection = "CallLog.Calls.TYPE = 3";
        Cursor cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, myColumnProjection, selection, null, CallLog.Calls.DATE);
        if(cursor != null && cursor.getCount() > 0)
        {
            StringBuilder sb = new StringBuilder("");
            while(cursor.moveToNext())
            {
                sb.append(cursor.getString(0) + " , " + epochToDate(cursor.getLong(1))+ " , " + callType(cursor.getInt(2)) + "\n");
            }

            textView.setText(sb.toString());
        }
        else
        {
            textView.setText("No Calls");
        }
    }
    public void displayOutgoing()
    {
        selection = "CallLog.Calls.TYPE = 2";
        Cursor cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, myColumnProjection, selection, null, CallLog.Calls.DATE);
        if(cursor != null && cursor.getCount() > 0)
        {
            StringBuilder sb = new StringBuilder("");
            while(cursor.moveToNext())
            {
                sb.append(cursor.getString(0) + " , " + epochToDate(cursor.getLong(1))+ " , " + callType(cursor.getInt(2)) + "\n");
            }

            textView.setText(sb.toString());
        }
        else
        {
            textView.setText("No Calls");
        }
    }
    public void displayIncoming()
    {
        selection = "CallLog.Calls.TYPE = 1";
        Cursor cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, myColumnProjection, selection, null, CallLog.Calls.DATE);

        if(cursor != null && cursor.getCount() > 0)
        {
            StringBuilder sb = new StringBuilder("");
            while(cursor.moveToNext())
            {
                sb.append(cursor.getString(0) + " , " + epochToDate(cursor.getLong(1))+ " , " + callType(cursor.getInt(2)) + "\n");
            }

            textView.setText(sb.toString());
        }
        else
        {
            textView.setText("No Calls");
        }
    }
}
