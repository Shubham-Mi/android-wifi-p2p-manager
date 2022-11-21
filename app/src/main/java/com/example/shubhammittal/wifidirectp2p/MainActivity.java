package com.example.shubhammittal.wifidirectp2p;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, WifiP2pManager.ConnectionInfoListener {
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
    Button buttonConfig;
    EditText editTextInput;

    ListView listViewDevices;
    TextView textViewDiscoveryStatus;
    TextView textViewWifiP2PStatus;
    TextView textViewConnectionStatus;
    TextView textViewReceivedData;
    TextView textViewReceivedDataStatus;

    static boolean stateDiscovery = false;
    static boolean stateWifi = false;

    WifiP2pDevice[] deviceListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        TODO: serviceDiscovery
        setUpUI();
        mManager = (WifiP2pManager) getSystemService(WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WifiBroadcastReceiver(mManager, mChannel, this);
//        TODO: serverSocketThread
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
        buttonConfig = findViewById(R.id.main_activity_button_configure);
        editTextInput = findViewById(R.id.main_activity_input_text);
        listViewDevices = findViewById(R.id.main_activity_list_view_devices);
        textViewConnectionStatus = findViewById(R.id.main_activity_textView_connection_status);
        textViewDiscoveryStatus = findViewById(R.id.main_activity_textView_discovery_status);
        textViewWifiP2PStatus = findViewById(R.id.main_activity_textView_wifi_p2p_status);
        textViewReceivedData = findViewById(R.id.main_activity_data);
        textViewReceivedDataStatus = findViewById(R.id.main_activity_received_data);

        buttonServerStart.setOnClickListener(this);
        buttonServerStop.setOnClickListener(this);
        buttonClientStart.setOnClickListener(this);
        buttonClientStop.setOnClickListener(this);
        buttonConnect.setOnClickListener(this);
        buttonDiscoveryStart.setOnClickListener(this);
        buttonDiscoveryStop.setOnClickListener(this);
        buttonConfig.setOnClickListener(this);

        buttonClientStart.setVisibility(View.INVISIBLE);
        buttonClientStop.setVisibility(View.INVISIBLE);
        buttonServerStart.setVisibility(View.INVISIBLE);
        buttonServerStop.setVisibility(View.INVISIBLE);
        editTextInput.setVisibility(View.INVISIBLE);
        textViewReceivedData.setVisibility(View.INVISIBLE);
        textViewReceivedDataStatus.setVisibility(View.INVISIBLE);

        listViewDevices.setOnItemClickListener((adapterView, view, i, l) -> {
            device = deviceListItems[i];
            Toast.makeText(MainActivity.this, "Selected Device: " + device.deviceName, Toast.LENGTH_SHORT).show();
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.main_activity_button_discovery_start:
                if (!stateDiscovery) {
                    startPeersDiscover();
                }
                break;
            case R.id.main_activity_button_discovery_stop:
                if (stateDiscovery) {
                    stopPeersDiscover();
                }
                break;
            case R.id.main_activity_button_connect:
                if (device == null) {
                    Toast.makeText(MainActivity.this, "Please discover and select a device", Toast.LENGTH_SHORT).show();
                }
                connect(device);
                break;
            case R.id.main_activity_button_server_start:
//              TODO: serverSocketThread
                break;
            case R.id.main_activity_button_server_stop:
//              TODO: serverSocketThread not
                break;
            case R.id.main_activity_button_client_start:
//              TODO: clientSocket
                break;
            case R.id.main_activity_button_client_stop:
//              TODO: clientSocket not
                break;
            case R.id.main_activity_button_configure:
                mManager.requestConnectionInfo(mChannel, this);
            default:
                break;
        }
    }

    private void connect(final WifiP2pDevice device) {

    }

    private void stopPeersDiscover() {

    }

    private void startPeersDiscover() {

    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {

    }

    public void setStatusView(int status) {

    }
}