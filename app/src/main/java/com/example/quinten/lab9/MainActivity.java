package com.example.quinten.lab9;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button missed,outgoing,incoming;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        missed = (Button)findViewById(R.id.missedButton);
        outgoing = (Button)findViewById(R.id.outgoingButton);
        incoming = (Button)findViewById(R.id.incomingButton);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, 20 );


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

    }
    public void displayOutgoing()
    {

    }
    public void displayIncoming()
    {

    }
}
