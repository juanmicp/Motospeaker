package com.juanmi.motospeaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CommunicateActivity extends AppCompatActivity {

    Button endCallButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communicate);
        endCallButton = (Button) findViewById(R.id.endCallButton);
        endCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //Cuando se pulsa, se delega en el mánager para finalizar conversación y seguidamente se cierra la aplicación.
                BluetoothManager.getInstance().endConnection();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }

    @Override
    public void onBackPressed() { }

}

