/*
* Created by Quinten Whitaker
* Assignment: Lab 9
* Date: 10/28/18
*/

package com.example.quinten.lab9;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.lang.Long;

import org.w3c.dom.Text;

import java.text.ParsePosition;
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
    EditText searchNumber;
    String where;
    String selection = null;
    static SimpleDateFormat format;

    ContentResolver contentResolver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        format = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        missed = (Button)findViewById(R.id.missedButton);
        outgoing = (Button)findViewById(R.id.outgoingButton);
        incoming = (Button)findViewById(R.id.incomingButton);
        textView = (TextView)findViewById(R.id.callTV);
        t = (TableLayout)findViewById(R.id.callTable);
        searchNumber = (EditText)findViewById(R.id.searchBarET);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, 20 );
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALL_LOG}, 20 );

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
            case R.id.displayAllButton:
                displayAll();
                break;
            case R.id.searchButton:
                displaySearch();
                break;
        }
    }

    public void deleteCall(View view)
    {
        //Todo
        TableRow row = (TableRow)view.getParent();

        TextView t2 = (TextView)row.findViewById(R.id.dateTV);
        Date date = format.parse(t2.getText().toString(), new ParsePosition(0));
        long time = date.getTime();
        String timeString = Long.toString(time);

        where = CallLog.Calls.DATE+ " = " + timeString;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Call");
        builder.setMessage("Are you sure you want to delete this call from your history?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                contentResolver.delete(CallLog.Calls.CONTENT_URI, where, null);
                displayAll();
            }
        });

        builder.setNegativeButton("Cancel", null);
        AlertDialog del = builder.create();
        del.show();

    }

    public void displaySearch()
    {
        selection = "CallLog.Calls.NUMBER = "+ searchNumber.getText().toString();
        Cursor cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, myColumnProjection, selection, null, CallLog.Calls.DATE);
        updateDisplay(cursor);
    }
    public void displayMissed()
    {
        selection = "CallLog.Calls.TYPE = 3";
        Cursor cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, myColumnProjection, selection, null, CallLog.Calls.DATE);
        updateDisplay(cursor);
    }
    public void displayAll()
    {
        Cursor cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, myColumnProjection, null, null, CallLog.Calls.DATE);
        updateDisplay(cursor);
    }
    public void updateDisplay(Cursor cursor)
    {
        t.removeAllViews();

        if(cursor != null && cursor.getCount() > 0)
        {
            LayoutInflater li = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            int index = 0;
            while(cursor.moveToNext())
            {
                View row = li.inflate(R.layout.table_row_ui, null);
                TextView t1 = (TextView)row.findViewById(R.id.phoneNumTV);
                t1.setText(cursor.getString(0));
                TextView t2 = (TextView)row.findViewById(R.id.dateTV);
                t2.setText(epochToDate(cursor.getLong(1)));
                TextView t3 = (TextView)row.findViewById(R.id.callTypeTV);
                t3.setText(callType(cursor.getInt(2)));
                Button btn = (Button)row.findViewById(R.id.deleteButton);

                t.addView(row, index);
                index++;
                textView.setText("");
            }
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
        updateDisplay(cursor);
    }
    public void displayIncoming()
    {
        selection = "CallLog.Calls.TYPE = 1";
        Cursor cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, myColumnProjection, selection, null, CallLog.Calls.DATE);
        updateDisplay(cursor);
    }
}
