package com.juanmi.motospeaker;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.UUID;

/**
 * Created by Juanmi on 25/05/2017.
 */

public class Client extends Thread{

    private BluetoothSocket socket;
    private OutputStream out;
    private BluetoothDevice device;
    private String uuid;

    public Client (BluetoothDevice device, String uuid) { //Constructor que usa el que haga de cliente al establecer la conexión.
        this.device = device;
        this.uuid = uuid;
    }

    public Client (BluetoothSocket socket) { //Para el que hace de servidor a la hora de conectar.
        this.socket = socket;
    }

    public void run() {

        //Preparando la conexión.
        if (socket == null) {
            BluetoothSocket tmp = null;
            try {
                tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid));
            } catch (IOException e) {
                Log.d("Client", "Could not create RFCOMM socket:" + e.toString());
                return;
            }
            this.socket = tmp;

            try {
                socket.connect();
            } catch (IOException e) {
                Log.d("Client", "Could not connect: " + e.toString());
                try {
                    socket.close();
                } catch (IOException e2) {
                    Log.d("Client", "Could not close connection:" + e.toString());
                    return;
                }
            }
        }
        try {
            this.out = this.socket.getOutputStream();
        }
        catch (IOException e){
            Log.d("Client","Could not create output stream: " + e.toString());
            try {
                socket.close();
            } catch (IOException e2) {
                Log.d("Client", "Could not close connection: " + e.toString());
                return;
            }
        }


        //Preparando el streaming.
        AudioRecord recorder;
        //int sampleRate = 8000;
        int sampleRate = 44100;
        //int sampleRate = 16000;
        //int sampleRate = 48000; //Con 16000 se escucha aunque también mucho ruido. Parece que el dispositivo más potente satura al otro con tanto envío de audio.
        int channelConfig = AudioFormat.CHANNEL_IN_MONO;
        int encoding = AudioFormat.ENCODING_PCM_16BIT;
        int minBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, encoding);
        byte[] buffer = new byte[minBufferSize];
        //recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,sampleRate,channelConfig,encoding,minBufferSize);
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,sampleRate,channelConfig,encoding,minBufferSize*10);
        recorder.startRecording();

        while (true){ //Bucle infinito de envío de audio.
            recorder.read(buffer, 0, buffer.length);
            try {
                out.write(buffer); //Se manda el buffer con el audio al servidor.
                out.flush();
            } catch (IOException e) {
                Log.d("Client", "Could not write: " + e.toString());
            }
            /*
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

        }
    }

    public void cancel() {
        try {
            if (out != null)
                out.close();
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            Log.d("Client", "Could not close the client connection:" + e.toString());
        }
    }

    public BluetoothSocket getSocket (){
        return socket;
    }

}

