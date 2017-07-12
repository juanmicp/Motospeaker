package com.juanmi.motospeaker;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
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

public class Client {

    private BluetoothSocket socket;
    private OutputStream out;

    public Client (BluetoothDevice device, String uuid) { //Constructor que usa el que haga de cliente al establecer la conexión.
        BluetoothSocket tmp = null;
        try {
            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid));
        } catch (IOException e) {
            Log.d("Client","Could not create RFCOMM socket:" + e.toString());
            return;
        }
        socket = tmp;

        try {
            socket.connect();
            this.out=socket.getOutputStream();
        }
        catch (IOException e) {
            Log.d("Client","Could not connect: " + e.toString());
            try {
                socket.close();
            } catch (IOException e2) {
                Log.d("Client", "Could not close connection:" + e.toString());
                return;
            }
        }
    }

    public Client (BluetoothSocket socket) { //Para el que hace de cliente a la hora de conectar.
        this.socket = socket;
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
    }

    public void send(String toSend) {
        try {
            out.write(toSend.getBytes());
        } catch (IOException e) {
            Log.d("Client", "Could not write: " + e.toString());
        }

        /*
        try {
            socket.close();
        } catch (IOException e) {
            Log.d("Client", "In run: Could not close connection:" + e.toString());
        }
        */
    }

    public void cancel() {
        try {
            socket.close();
        } catch (IOException e) {
            Log.d("Client", "Could not close the client connection:" + e.toString());
        }
    }

    public BluetoothSocket getSocket (){
        return socket;
    }

}

