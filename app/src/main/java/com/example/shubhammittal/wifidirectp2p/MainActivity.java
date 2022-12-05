package com.example.shubhammittal.wifidirectp2p;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

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

//    TODO: serviceDiscovery and SocketDiscoveryThread declaration

    static boolean stateDiscovery = false;
    static boolean stateWifi = false;
    public static boolean stateConnection = false;
    public static String IP = null;
    public static boolean IS_OWNER = false;

    WifiP2pDevice[] deviceListItems;
    ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        TODO: serviceDiscovery and serverSocketThread
        setUpUI();
        mManager = (WifiP2pManager) getSystemService(WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WifiBroadcastReceiver(mManager, mChannel, this);
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

    public void setDeviceList(ArrayList<WifiP2pDevice> deviceDetails) {
        deviceListItems = new WifiP2pDevice[deviceDetails.size()];
        String[] deviceNames = new String[deviceDetails.size()];

        for (int i = 0; i < deviceDetails.size(); i++) {
            deviceListItems[i] = deviceDetails.get(i);
            deviceNames[i] = deviceDetails.get(i).deviceName;
        }
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, deviceNames);
        listViewDevices.setAdapter(mAdapter);
    }

    public void setStatusView(int status) {
        switch (status) {
            case Constants.DISCOVERY_INITIATED:
                stateDiscovery = true;
                textViewDiscoveryStatus.setText(R.string.discovery_initiated);
                break;
            case Constants.DISCOVERY_STOPPED:
                stateDiscovery = false;
                textViewDiscoveryStatus.setText(R.string.discovery_stopped);
                break;
            case Constants.P2P_WIFI_ENABLED:
                stateWifi = true;
                textViewWifiP2PStatus.setText(R.string.p2p_wifi_enabled);
                buttonDiscoveryStart.setEnabled(true);
                buttonDiscoveryStop.setEnabled(true);
                break;
            case Constants.P2P_WIFI_DISABLED:
                stateWifi = false;
                textViewWifiP2PStatus.setText(R.string.p2p_wifi_disabled);
                buttonDiscoveryStart.setEnabled(false);
                buttonDiscoveryStop.setEnabled(false);
                break;
            case Constants.NETWORK_CONNECT:
                stateConnection = true;
                textViewConnectionStatus.setText(R.string.connected);
                break;
            case Constants.NETWORK_DISCONNECT:
                stateConnection = true;
                textViewConnectionStatus.setText(R.string.disconnected);
                break;
            default:
                Log.d(TAG, "setStatusView: Unknown Status");
                break;
        }
    }

    @SuppressLint("MissingPermission")
    private void connect(final WifiP2pDevice device) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        config.wps.setup = WpsInfo.PBC;
        Log.d(TAG, "Trying to connect: " + device.deviceName);
        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: Connected to: " + device.deviceName);
                Toast.makeText(MainActivity.this, "Connection Successful with " + device.deviceName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reason) {
                if (reason == WifiP2pManager.P2P_UNSUPPORTED) {
                    Log.d(TAG, "P2P unsupported");
                    Toast.makeText(MainActivity.this, "Failed to Make connection: P2P Unsupported", Toast.LENGTH_SHORT).show();
                } else if (reason == WifiP2pManager.ERROR) {
                    Log.d(TAG, "Error");
                    Toast.makeText(MainActivity.this, "Failed to Make connection: Error", Toast.LENGTH_SHORT).show();
                } else if (reason == WifiP2pManager.BUSY) {
                    Log.d(TAG, "Busy");
                    Toast.makeText(MainActivity.this, "Failed to Make connection: Busy", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void startPeersDiscover() {
        setDeviceList(new ArrayList<>());
        Log.d(TAG, "startPeersDiscover()");
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                stateDiscovery = true;
                Log.d(TAG, "startPeersDiscover: starting peer discovery");
                Toast.makeText(MainActivity.this, "Peer Discovery Started", Toast.LENGTH_SHORT).show();
                MyPeerListener myPeerListener = new MyPeerListener(MainActivity.this);
                mManager.requestPeers(mChannel, myPeerListener);
            }

            @Override
            public void onFailure(int reason) {
                stateDiscovery = false;
                if (reason == WifiP2pManager.P2P_UNSUPPORTED) {
                    Log.d(TAG, "P2P unsupported");
                    Toast.makeText(MainActivity.this, "peer discovery failed : P2P Unsupported", Toast.LENGTH_SHORT).show();
                } else if (reason == WifiP2pManager.ERROR) {
                    Log.d(TAG, "Error");
                    Toast.makeText(MainActivity.this, "peer discovery failed : Error", Toast.LENGTH_SHORT).show();
                } else if (reason == WifiP2pManager.BUSY) {
                    Log.d(TAG, "Busy");
                    Toast.makeText(MainActivity.this, "peer discovery failed : Busy", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void stopPeersDiscover() {
        mManager.stopPeerDiscovery(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                stateDiscovery = false;
                Log.d(TAG, "onSuccess: Peer Discovery Stopped");
                Toast.makeText(MainActivity.this, "Peer Discovery Stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i) {
                Log.d(TAG, "onFailure: Stopping Peer Discovery Failed");
                Toast.makeText(MainActivity.this, "Stopping Peer Discovery Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
        Log.d(TAG, "wifiP2pInfo.groupOwnerAddress.getHostAddress() " + wifiP2pInfo.groupOwnerAddress.getHostAddress());
        IP = wifiP2pInfo.groupOwnerAddress.getHostAddress();
        IS_OWNER = wifiP2pInfo.isGroupOwner;

        if (IS_OWNER) {
            buttonClientStop.setVisibility(View.GONE);
            buttonClientStart.setVisibility(View.GONE);
            editTextInput.setVisibility(View.GONE);

            buttonServerStop.setVisibility(View.VISIBLE);
            buttonServerStart.setVisibility(View.VISIBLE);

            textViewReceivedData.setVisibility(View.VISIBLE);
            textViewReceivedDataStatus.setVisibility(View.VISIBLE);
        } else {
            buttonClientStart.setVisibility(View.VISIBLE);
            editTextInput.setVisibility(View.VISIBLE);
            buttonServerStop.setVisibility(View.GONE);
            buttonServerStart.setVisibility(View.GONE);
            textViewReceivedData.setVisibility(View.GONE);
            textViewReceivedDataStatus.setVisibility(View.GONE);
        }

        Toast.makeText(MainActivity.this, "Configuration Completed", Toast.LENGTH_SHORT).show();
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
                String dataToSend = editTextInput.getText().toString();
                ClientSocket clientSocket = new ClientSocket(dataToSend);
                clientSocket.execute();
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
}