/*
* Created by Quinten Whitaker
* Assignment: Lab 9
* Date: 10/28/18
*/

package com.example.quinten.lab9;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button missed,outgoing,incoming;
    TextView textView;
    TableLayout t;
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
        t = (TableLayout)findViewById(R.id.callTable);

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
            case R.id.deleteButton:
                deleteCall(v);
                break;
        }
    }

    public void deleteCall(View view)
    {
        //Todo

    }
    public void displayMissed()
    {
        t.removeAllViews();
        selection = "CallLog.Calls.TYPE = 3";
        Cursor cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, myColumnProjection, selection, null, CallLog.Calls.DATE);
        if(cursor != null && cursor.getCount() > 0)
        {
            StringBuilder sb = new StringBuilder("");
            LayoutInflater li = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            int index = 0;
            while(cursor.moveToNext())
            {
                View row = li.inflate(R.layout.table_row_ui, null);
                TextView t1 = (TextView)row.findViewById(R.id.callTV);
                t1.setText(cursor.getString(0));
                TextView t2 = (TextView)row.findViewById(R.id.dateTV);
                t2.setText(epochToDate(cursor.getLong(1)));
                TextView t3 = (TextView)row.findViewById(R.id.callTypeTV);
                t3.setText(callType(cursor.getInt(2)));

                t.addView(row, index);
                index++;
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
