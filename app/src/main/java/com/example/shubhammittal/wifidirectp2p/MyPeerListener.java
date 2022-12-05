package com.example.shubhammittal.wifidirectp2p;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import java.util.ArrayList;

public class MyPeerListener implements WifiP2pManager.PeerListListener {
    private static final String TAG = "MyPeerListener";
    public MainActivity mainActivity;

    public MyPeerListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        Log.d(TAG, "MyPeerListener: Object Created");
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
        ArrayList<WifiP2pDevice> deviceDetails = new ArrayList<>();

        if (wifiP2pDeviceList != null) {
            if (wifiP2pDeviceList.getDeviceList().size() == 0) {
                Log.d(TAG, "onPeersAvailable: WifiP2PDeviceList size = 0");
                return;
            }
            for (WifiP2pDevice device: wifiP2pDeviceList.getDeviceList()) {
                deviceDetails.add(device);
                Log.d(TAG, "onPeersAvailable: Found Device: " + device.deviceName + " " + device.deviceAddress);
            }
            if (mainActivity != null) {
                mainActivity.setDeviceList(deviceDetails);
            }
        } else {
            Log.d(TAG, "onPeersAvailable: WifiP2PDeviceList is null");
        }
    }
}