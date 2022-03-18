package bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by charleston on 10/07/14.
 */
public class ClientBluetooth {
    private final BluetoothSocket mSocket;
    private final BluetoothDevice mDevice;
    public boolean connected = false;
    private byte[] dataToSend = null;
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    //private InputStream mInStream;
    private OutputStream mOutStream;
    public ClientBluetooth(BluetoothDevice device){
        BluetoothSocket tmp = null;
        mDevice = device;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device.createRfcommSocketToServiceRecord(ServerBluetooth.DEFAULT_UUID);
        } catch (IOException e) { }
        mSocket = tmp;
    }
    public void connectToServer()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mBluetoothAdapter.cancelDiscovery();
                try {
                    // Connect the device through the socket. This will block
                    // until it succeeds or throws an exception
                    mSocket.connect();
                    InputStream tmpIn = null;
                    OutputStream tmpOut = null;

                    // Get the input and output streams, using temp objects because
                    // member streams are final
                    try {
                        tmpIn = mSocket.getInputStream();
                        tmpOut = mSocket.getOutputStream();
                    } catch (IOException e) { }

                    //mInStream = tmpIn;
                    mOutStream = tmpOut;
                    connected = true;
                    connectedToServer();
                } catch (IOException connectException) {
                    // Unable to connect; close the socket and get out
                    try {
                        mSocket.close();
                    } catch (IOException closeException) { }
                    return;
                }
            }
        }).start();
    }
    public void connectedToServer(){
        if(dataToSend!=null)
            sendBytes(dataToSend);
    }
    public void sendBytes(byte[] bytes){
        if(!connected)
            dataToSend = bytes;
        else{
            try {
                mOutStream.write(bytes);
                dataToSend = null;
                Log.i("teste", "bytes send");
            } catch (IOException e) {

            }
        }
    }
    public void disconnectFromServer(){
        try {
            mSocket.close();
        } catch (IOException e) { }
    }
}
