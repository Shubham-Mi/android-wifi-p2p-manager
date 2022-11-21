package com.example.shubhammittal.wifidirectp2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

public class WifiBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "WifiBroadcastReceiver";
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    MainActivity mActivity;

    public WifiBroadcastReceiver(WifiP2pManager mManager, WifiP2pManager.Channel mChannel, MainActivity mActivity) {
        super();
        this.mManager = mManager;
        this.mChannel = mChannel;
        this.mActivity = mActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                Log.d(TAG, "onReceive: Wifi P2P Enabled");
                mActivity.setStatusView(Constants.P2P_WIFI_ENABLED);
            } else {
                Log.d(TAG, "onReceive: Wifi P2P Disabled");
                mActivity.setStatusView(Constants.P2P_WIFI_DISABLED);
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            Log.d(TAG, "onReceive: WIFI_P2P_PEERS_CHANGED_ACTION");
            if (mManager != null) {
//                TODO: myPeerListener
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            if (mManager != null) {
                NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                if (networkInfo.isConnected()) {
                    mActivity.setStatusView(Constants.NETWORK_CONNECT);
                } else {
                    mActivity.setStatusView(Constants.NETWORK_DISCONNECT);
                }
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            Log.d(TAG, "onReceive: WIFI_P2P_THIS_DEVICE_CHANGED_ACTION");
        } else if (WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_DISCOVERY_STATE, 10000);
            if (state == WifiP2pManager.WIFI_P2P_DISCOVERY_STARTED) {
                mActivity.setStatusView(Constants.DISCOVERY_INITIATED);
            } else {
                mActivity.setStatusView(Constants.DISCOVERY_STOPPED);
            }
        }
    }
}
