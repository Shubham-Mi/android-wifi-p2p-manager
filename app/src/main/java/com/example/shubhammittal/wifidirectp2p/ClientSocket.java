package com.example.shubhammittal.wifidirectp2p;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientSocket extends AsyncTask {
    private static final String TAG = "ClientSocket";
    private final String data;

    public ClientSocket(String data) {
        if (data != null)
            this.data = data;
        else
            this.data = "null data";
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        sendData();
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Log.d(TAG, "onPostExecute: Send Data Task Completed");
    }

    public void sendData() {
        String host = MainActivity.IP;
        int port = 8888;
        int len;
        Socket socket = new Socket();
        byte[] buf = new byte[1024];

        try {
            socket.bind(null);
            Log.d(TAG, "sendData: Trying to connect...");
            socket.connect(new InetSocketAddress(host, port), 500);
            Log.d(TAG, "sendData: Connected...");

            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = new ByteArrayInputStream(data.getBytes());
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            Log.d(TAG, "sendData: " + e.toString());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
