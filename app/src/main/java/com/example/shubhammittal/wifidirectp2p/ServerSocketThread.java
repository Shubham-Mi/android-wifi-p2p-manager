package com.example.shubhammittal.wifidirectp2p;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketThread extends AsyncTask {
    private static final String TAG = "ServerSocketThread";
    ServerSocket serverSocket;
    String receivedData = "null";
    private boolean interrupted = false;
    OnUpdateListener listener;

    public interface OnUpdateListener {
        void onUpdate(String data);
    }

    public void setInterrupted(boolean interrupted) {
        this.interrupted = interrupted;
    }

    public void setUpdateListener(OnUpdateListener listener) {
        this.listener = listener;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            Log.d(TAG, "doInBackground: started");
            int port = 8888;
            serverSocket = new ServerSocket(port);

            while (!interrupted) {
                Socket client = serverSocket.accept();
                InputStream inputStream = client.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                bufferedReader.close();
                Log.d(TAG, "doInBackground: completed ReceivedDataTask");
                receivedData = stringBuilder.toString();

                if (listener != null) {
                    listener.onUpdate(receivedData);
                }
                Log.d(TAG, "doInBackground: =======================" + receivedData);
            }
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "doInBackground: IOException Occurred");
        }
        return null;
    }
}
