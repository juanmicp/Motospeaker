package com.juanmi.motospeaker;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;
import android.util.Log;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.UUID;

/**
 * Created by Juanmi on 25/05/2017.
 */

public class Server extends Thread {

    private BluetoothSocket socket;
    private InputStream in;
    private BluetoothAdapter bluetoothAdapter;
    private String uuid;

    public Server (BluetoothAdapter bluetoothAdapter, String uuid) { //Constructor para el que ejerce de servidor en el momento de la conexión.
        this.bluetoothAdapter = bluetoothAdapter;
        this.uuid = uuid;

    }

    public Server(BluetoothSocket socket){ //Para el caso de que sea cliente a la hora de conectar.
        this.socket = socket;
    }

    public void run() {

        if (socket == null) {
            BluetoothServerSocket serverSocket = null;
            try {
                serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("Motospeaker", UUID.fromString(uuid));
            } catch (IOException e) {
                Log.d("Server", "Socket's listen() method failed: " + e.toString());
            }
            while (true) {
                try {
                    this.socket = serverSocket.accept();
                } catch (Exception e) {
                    Log.d("Server", "Socket's accept() method failed: " + e.toString());
                    break;
                }
                if (socket != null) {
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        Log.d("Server", "Could not close ServerSocket:" + e.toString());
                    }
                    break;
                }
            }
        }

        //int sampleRate = 48000;
        int sampleRate = 44100;
        int channelConfig = AudioFormat.CHANNEL_OUT_MONO;
        int encoding = AudioFormat.ENCODING_PCM_16BIT;
        int minBufferSize = AudioTrack.getMinBufferSize(sampleRate, channelConfig, encoding);

        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, channelConfig, encoding, minBufferSize, AudioTrack.MODE_STREAM);
        //AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, channelConfig, encoding, 48000, AudioTrack.MODE_STREAM);
        audioTrack.play(); //A partir de ahora reproducirá lo que dicte el método write().
        byte[] buffer = new byte[minBufferSize];
        while (true){ //Bucle de recepción.
            try {
                in = socket.getInputStream();
                in.read(buffer);
                audioTrack.write(buffer, 0, buffer.length);
                /*
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //Si la versión del SO Android es compatible se reproduce sin bloqueo...
                    audioTrack.write(buffer, 0, buffer.length, AudioTrack.WRITE_NON_BLOCKING); //En este momento reproduce lo que le llegó en el buffer.
                }
                else { //Si no es compatible se realiza del modo original.
                    audioTrack.write(buffer, 0, buffer.length);
                }*/
                audioTrack.flush();
                //buffer = null;

                //Thread.sleep(10);

                //audioTrack.stop();
                //audioTrack.release(); //Al no usar release() quizá se sature la memoria del dispositivo. Pero si

            } catch (Exception e) {
                Log.d("Server", "Could not read: " + e.toString());
                break;
            }
        }

        /*
        String oldText = "";
        while (true) {
            byte[] buffer = new byte[1024];
            int bytes;
            try {
                InputStream in = socket.getInputStream();
                bytes = in.read(buffer);
                final String text = new String(buffer, 0, bytes);
                String prueba = "test";
                if (!text.equals(oldText) && !text.equals("")){

                    CommunicateActivity.runOnUI(new Runnable() {
                        @Override
                        public void run() {
                            etToReceive.setText(text);
                        }
                    });
                }
                oldText = text;
            } catch (Exception e) {
                Log.d("Server", "Could not read: " + e.toString());
                break;
            }
        }
        */

    }

    public void cancel() {
        try {
            if (in != null)
                in.close();
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            Log.d("Server", "Could not close the connected socket: " + e.toString());
        }
    }

    public BluetoothSocket getSocket(){
        return socket;
    }

}

