package com.juanmi.motospeaker;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CommunicateActivity extends AppCompatActivity {

    Button sendButton;
    EditText etToReceive;
    public static Handler UIHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communicate);
        sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //Cuando se pulsa, se delega en el mánager para enviar lo que se haya escrito.
                EditText etToSend = (EditText)findViewById(R.id.meEditText);
                String text = etToSend.getText().toString();
                BluetoothManager.getInstance().sendText(text);
            }
        });
        etToReceive = (EditText)findViewById(R.id.heSheEditText);
        UIHandler = new Handler(Looper.getMainLooper());
        connect();
    }

    private void connect(){ //Conectar con el dispositivo seleccionado.
        BluetoothManager btManager = BluetoothManager.getInstance();
        if (btManager.connect()){ //Si conecta con el dispositivo en cuestión.
            btManager.setEditText(etToReceive);
            sendButton.setEnabled(true); //En ese caso se activa el botón de envío.
        }
        else{
            Toast.makeText(getBaseContext(), "Imposible conectar.", Toast.LENGTH_SHORT).show();
        }
    }
    public static void runOnUI(Runnable runnable) {
        UIHandler.post(runnable);
    }
}

