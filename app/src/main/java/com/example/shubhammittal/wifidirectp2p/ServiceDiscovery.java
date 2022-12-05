package com.example.shubhammittal.wifidirectp2p;

import android.annotation.SuppressLint;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class ServiceDiscovery {
    private static final String TAG = "ServiceDiscovery";
    public static final String TXTRECORD_PROP_AVAILABLE = "available";
    public static final String SERVICE_INSTANCE = "_wifidemotest";
    public static final String SERVICE_REG_TYPE = "_presence._tcp";

    @SuppressLint("MissingPermission")
    public void discoverServices(WifiP2pManager manager, WifiP2pManager.Channel channel) {
        manager.setDnsSdResponseListeners(channel, (s, s1, wifiP2pDevice) -> {
//                A service has been discovered.
            Log.d(TAG, "onDnsSdServiceAvailable: ");
        }, (s, map, wifiP2pDevice) -> Log.d(TAG, "onDnsSdTxtRecordAvailable: " + wifiP2pDevice.deviceName + " is " + map.get(TXTRECORD_PROP_AVAILABLE)));

        WifiP2pDnsSdServiceRequest serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
        manager.addServiceRequest(channel, serviceRequest, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: Added Service discovery request");
            }

            @Override
            public void onFailure(int i) {
                Log.d(TAG, "onFailure: Failed adding service discovery request");
            }
        });

        manager.discoverServices(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: Service Discovery Initiated");
            }

            @Override
            public void onFailure(int i) {
                Log.d(TAG, "onFailure: Service discovery failed");
            }
        });
    }

    @SuppressLint("MissingPermission")
    public void startRegistrationAndDiscovery(WifiP2pManager manager, WifiP2pManager.Channel channel) {
        Map<String, String> record = new HashMap<>();
        record.put(TXTRECORD_PROP_AVAILABLE, "visible");

        WifiP2pDnsSdServiceInfo serviceInfo = WifiP2pDnsSdServiceInfo.newInstance(SERVICE_INSTANCE, SERVICE_REG_TYPE, record);
        manager.addLocalService(channel, serviceInfo, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: Added local Service");
            }

            @Override
            public void onFailure(int i) {
                Log.d(TAG, "onFailure: Failed to add local service");
            }
        });
    }
}
