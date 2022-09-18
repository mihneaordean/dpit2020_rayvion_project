package com.example.dpit2020navem.Bluetooth;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.dpit2020navem.HomePage.MainActivity;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class BluetoothService extends Service{
    BluetoothAdapter myBluetooth = null;
    public BluetoothSocket btSocket = null;
    //ProgressDialog progress;
    boolean isBtConnected = false;
    String address;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    InputStream inputStream;
    //private int NOTIFICATION = R.string.local_service_started;

    private final IBinder binder = new LocalBinder();
    public class LocalBinder extends Binder {
        public BluetoothService getBluetoothService() {
            return BluetoothService.this;
        }
    }

    public BluetoothSocket getBtSocket() {
        return btSocket;
    }

    public void setBtSocket(BluetoothSocket btSocket) {
        this.btSocket = btSocket;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        //mNM.cancel(NOTIFICATION);

        // Tell the user we stopped.
        //Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate() {
        // Display a notification about us starting.  We put an icon in the status bar.
        //showNotification();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private final IBinder mBinder = new LocalBinder();

    public void bluetoothConnection(Activity activity){
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        if(myBluetooth == null)
        {
            //Show a mensag. that the device has no bluetooth adapter
            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();

        }
        else if(!myBluetooth.isEnabled())
        {
            //Ask to the user turn the bluetooth on
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(turnBTon,1);
        }

    }

    private String getDeviceMacAddress(){
        //address = "00:19:08:35:F7:17";  //BIG box address
        address = "98:D3:71:FD:77:F2";  //SMALL box address
        //address = "98:D3:61:FD:6E:16";
        return  address;
    }

    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }


    public class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true;//if it's here, it's almost connected


        @Override
        protected void onPreExecute()
        {
            //progress = ProgressDialog.show(context, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(getDeviceMacAddress());//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                    inputStream = btSocket.getInputStream();
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            //progress.dismiss();
        }
    }

    public void connectionBT(Context context){
        new ConnectBT().execute();
    }

    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");}
        }

    }

    public void writeBluetooth(String s){
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write(s.getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    public String readBluetooth() {
        String s = "";

        int bytes = 0; // Number of bytes.
        byte ch;


        try {
            if (btSocket!=null  && inputStream != null && inputStream.available()>0 )
            {
                try
                {
                    Log.i(null, String.valueOf(inputStream.available()));

                    while((ch = (byte) inputStream.read())!='x') {
                        bytes++;
                    }

                    while((ch = (byte) inputStream.read())!='x') {
                        bytes++;
                        s += Character.toString((char) ch);
                    }



                    try{
                        int ascii = s.charAt(0);
                        if(ascii == 1){
                            s = s.substring(1);
                        }
                    }catch (Exception e){

                        //msg("Error");
                    }



                    while(inputStream.available() > 0){
                        inputStream.read();
                    }

                }
                catch (IOException e)
                {
                    msg("Error");
                    connectionBT(null);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return s;
    }

    public void closeShandrama(){
        try {
            btSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
