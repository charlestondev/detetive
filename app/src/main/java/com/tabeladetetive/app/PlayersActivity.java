package com.tabeladetetive.app;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tabeladetetive.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PlayersActivity extends ActionBarActivity {
    private ListView playersListView;
    public PlayersAdapter pairedDevicesAdapter;
    public BluetoothAdapter mBluetoothAdapter;
    public String type;
    public int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);
        playersListView = (ListView) findViewById(R.id.players_list);
        //pairedDevicesAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        List<BluetoothDevice> listPlayers = new ArrayList<BluetoothDevice>();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                listPlayers.add(device);
            }
            pairedDevicesAdapter = new PlayersAdapter(this, listPlayers);
            playersListView.setAdapter(pairedDevicesAdapter);
        }
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        position = intent.getIntExtra("position",0);
    }
}
