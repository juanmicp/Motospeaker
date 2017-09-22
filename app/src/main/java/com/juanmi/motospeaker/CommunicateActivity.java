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
            public void onClick(View view) { //Cuando se pulsa, se delega en el mánager para finalizar conversación.
                BluetoothManager.getInstance().endConnection();
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        connect();
    }

    private void connect(){ //Conectar con el dispositivo seleccionado.
        BluetoothManager btManager = BluetoothManager.getInstance();
        if (!btManager.connect()){ //Si no puede conectar con el dispositivo en cuestión.
            Toast.makeText(getBaseContext(), "Imposible conectar.", Toast.LENGTH_SHORT).show();
        }
    }
}

