package com.juanmi.motospeaker;

import android.bluetooth.BluetoothDevice;

import java.io.Serializable;

/**
 * Created by Juanmi on 22/05/2017.
 */

@SuppressWarnings("serial")
class BtDevice implements Serializable { //Implementa serializable para poder pasar un objeto de tipo BtDevice como parámetro.

    private BluetoothDevice device;
    private boolean connected;

    public BtDevice(BluetoothDevice device, boolean connected) {
        this.device = device;
        this.connected = connected;
    }

    public String getName() {
        return device.getName();
    }

    public String getAddress() {
        return device.getAddress();
    }

    public BluetoothDevice getDevice(){
        return device;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    @Override
    public String toString() {
        return device.getName();
    }
}

