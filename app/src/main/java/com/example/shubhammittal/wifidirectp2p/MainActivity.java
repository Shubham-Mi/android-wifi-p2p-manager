package com.example.shubhammittal.wifidirectp2p;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    WifiBroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;
    WifiP2pDevice device;

    Button buttonDiscoveryStart;
    Button buttonDiscoveryStop;
    Button buttonConnect;
    Button buttonServerStart;
    Button buttonServerStop;
    Button buttonClientStart;
    Button buttonClientStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpIntentFilter();
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    private void setUpIntentFilter() {
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    private void setUpUI() {
        buttonDiscoveryStart = findViewById(R.id.main_activity_button_discovery_start);
        buttonDiscoveryStop = findViewById(R.id.main_activity_button_discovery_stop);
        buttonConnect = findViewById(R.id.main_activity_button_connect);
        buttonServerStart = findViewById(R.id.main_activity_button_server_start);
        buttonServerStop = findViewById(R.id.main_activity_button_server_stop);
        buttonClientStart = findViewById(R.id.main_activity_button_client_start);
        buttonClientStop = findViewById(R.id.main_activity_button_client_stop);
    }
}