package com.juanmi.motospeaker;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class WaitingAreaActivity extends AppCompatActivity {

    private static int TIME_OUT = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_area);
        TextView deviceTV = (TextView) findViewById(R.id.deviceNameTextView);
        deviceTV.setText("Dispositivo visible como: \"" + BluetoothManager.getInstance().getBtAdapter().getName() + "\"");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WaitingAreaActivity.this, CommunicateActivity.class);
                startActivity(intent);
            }
        }, TIME_OUT);
    }

    @Override
    public void onBackPressed() {
        //Cuando pulsa el botón atrás no vuelve a la activity principal para evitar situaciones de excepción.
    }
}
