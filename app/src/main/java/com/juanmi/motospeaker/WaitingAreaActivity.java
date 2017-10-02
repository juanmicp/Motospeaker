package com.juanmi.motospeaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class WaitingAreaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_area);
        TextView deviceTV = (TextView) findViewById(R.id.deviceNameTextView);
        deviceTV.setText("Dispositivo visible como: \"" + BluetoothManager.getInstance().getBtAdapter().getName() + "\"");

        new Thread(new Runnable() { //Se genera un hilo encargado de la conexión para evitar que los bloqueos al conectar afecten a la UI.

            @Override
            public void run() {
                connect();
                whenConnected();
            }
        }).start();

    }

    @Override
    public void onBackPressed() {
        //Cuando pulsa el botón atrás no vuelve a la activity principal para evitar situaciones de excepción.
    }

    private void connect(){ //Conectar con el dispositivo seleccionado.
        BluetoothManager btManager = BluetoothManager.getInstance();
        if (!btManager.connect()){ //Si no puede conectar con el dispositivo en cuestión.
            Toast.makeText(getBaseContext(), "Imposible conectar.", Toast.LENGTH_SHORT).show();
        }
    }

    private void whenConnected(){ //Cuando se haya conectado al dispositivo que hace de cliente en la conexión, se pasará a la activity correspondiente al intercambio de audio en tiempo real.
        Intent intent = new Intent(WaitingAreaActivity.this, CommunicateActivity.class);
        startActivity(intent);
    }

}
