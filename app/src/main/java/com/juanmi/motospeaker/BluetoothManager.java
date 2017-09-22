package com.juanmi.motospeaker;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static android.R.attr.action;

/**
 * Created by Juanmi on 22/05/2017.
 */

public class BluetoothManager {

    private BluetoothAdapter btAdapter;
    private List<BtDevice> btDevList = new ArrayList();
    private static BluetoothManager instance = null;
    private String uuid = "00001101-0000-1000-8000-00805f9b34fb";
    private Server btServer;
    private Client btClient;
    private BtDevice btDevToConnect = null;

    private BluetoothManager() {
    }

    public static BluetoothManager getInstance() {
        if (instance == null) {
            instance = new BluetoothManager();
        }
        return instance;
    }

    public BluetoothAdapter getBtAdapter() {
        return btAdapter;
    }

    public void createBtAdapter() {
        this.btAdapter = BluetoothAdapter.getDefaultAdapter();;
    }

    public boolean connect() { //Conexión.
        boolean conectado = false;
        BluetoothSocket btSocket = null;
        if (btDevToConnect == null){ //Conexión en la parte servidora.
            btServer = new Server(btAdapter, uuid);
            while (btSocket == null){
                btSocket = btServer.getSocket();
            }
            btClient = new Client(btSocket);
            btServer.start();
            btClient.start();
            conectado = true;
        }else{ //Conexión en la parte cliente.
            btClient = new Client(btDevToConnect.getDevice(), uuid);
            btSocket = btClient.getSocket();
            btServer = new Server(btSocket);
            btServer.start();
            btClient.start();
            conectado = true;

        }
        return conectado;
    }

    private List<BtDevice> getAvailableDevices() { //Devuelve los dispositivos asociados.
        List<BtDevice> availableDevices = new ArrayList();
        Set<BluetoothDevice> devs = btAdapter.getBondedDevices();
        for (BluetoothDevice dev : devs) {
            BtDevice device = new BtDevice(dev, false);
            availableDevices.add(device);
        }
        return availableDevices;
    }

    public void setBtDevToConnect(BtDevice btDevToConnect) {
        this.btDevToConnect = btDevToConnect;
    }

    public void endConnection() {
        btClient.cancel();
        btServer.cancel();
        btDevToConnect = null;
        btAdapter.disable();
    }
}
