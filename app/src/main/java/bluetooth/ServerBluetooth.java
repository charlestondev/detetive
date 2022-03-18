package bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by charleston on 10/07/14.
 */
public class ServerBluetooth {
    private BluetoothServerSocket mmServerSocket;
    BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket socket;
    private ReceiveBluetoothBytes bytesHandler;
    public static UUID DEFAULT_UUID;
    public static final String MSG_STOP_SERVER = "STOP";
    public static final String MSG_RESTART_SERVER = "RESTART";
    public static final String STATE_STARTED = "started", STATE_STOPPED = "stopped", STATE_WAITING_CONNECTION = "waiting";
    private String state = STATE_STOPPED;
    public boolean started = false;
    public ServerBluetooth(ReceiveBluetoothBytes bytesHandler)
    {
        this.bytesHandler = bytesHandler;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        DEFAULT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    }
    public void startServer()
    {
        BluetoothServerSocket tmp = null;
        try {
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("tabela", DEFAULT_UUID);
        } catch (IOException e) { }
        mmServerSocket = tmp;

        new Thread(new Runnable() {
            @Override
            public void run() {
                socket = null;
                // Keep listening until exception occurs or a socket is returned
                while (true) {
                    try {
                        state = STATE_WAITING_CONNECTION;
                        started = true;
                        Log.i("teste", "server waiting");
                        socket = mmServerSocket.accept();
                    } catch (IOException e) {
                        break;
                    }
                    // If a connection was accepted
                    if (socket != null) {
                        try {
                            mmServerSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        serverListening();
                        break;
                    }
                }
            }
        }).start();
    }
    private void serverListening()
    {
        final InputStream mInStream;
        final OutputStream mOutStream;
        InputStream tmpIn = null;
        //OutputStream tmpOut = null;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            //tmpOut = socket.getOutputStream();
        } catch (IOException e) { }

        mInStream = tmpIn;
        //mOutStream = tmpOut;
        state = STATE_STARTED;
        Log.i("teste", "server started");
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] buffer = new byte[1024];  // buffer store for the stream
                int bytes; // bytes returned from read()

                // Keep listening to the InputStream until an exception occurs
                while (true) {
                    try {
                        // Read from the InputStream
                        bytes = mInStream.read(buffer);
                        //Log.i("read", new String(buffer,0,bytes)+" aaaa");
                        bytesHandler.receiveBytes(buffer,bytes);
                        restartServer();

                    } catch (IOException e) {
                        Log.i("exception", "something went wrong on reading"+e.getMessage());
                        break;
                    }
                }
            }
        }).start();
    }
    public void stopServer()
    {
        if(!state.equals(STATE_WAITING_CONNECTION)) {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
        else
            try {
                mmServerSocket.close();
            } catch (IOException e) {
            }
        state = STATE_STOPPED;
        started = false;
        Log.i("server", "stopped");
    }
    public void restartServer(){
        stopServer();
        startServer();
    }

    public interface ReceiveBluetoothBytes{
        public void receiveBytes(byte[] buffer,int bytes);
    }
}
