package com.konst.scaleslibrary.module.wifi;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.konst.scaleslibrary.module.Client;
import com.konst.scaleslibrary.module.Commands;
import com.konst.scaleslibrary.module.Module;
import com.konst.scaleslibrary.module.ObjectCommand;
import com.konst.scaleslibrary.module.wifi.websoket.WebSocketClient;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;


import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ClientWebSoket extends Client {
    private WebSocketClient mConnection;
    private WorkerThread workerThread;

    public ClientWebSoket(Context context, String address, int port){
        super(context, address, port);
    }

    ClientWebSoket(Context context, InetSocketAddress address){
        super(context,address);
    }

    @Override
    public void killWorkingThread() {
        if(workerThread != null) {
            workerThread.stopWorkingThread();
            workerThread = null;
        }
    }

    @Override
    public void restartWorkingThread() {
        if(workerThread == null) {
            workerThread = new WorkerThread();
            workerThread.start();
        }
    }

    @Override
    public void write(String data) {
        //s.append('\r').append('\n');
        mConnection.send(data);
    }

    @Override
    public ObjectCommand sendCommand(Commands cmd) {
        write(cmd.toString());
        response = new ObjectCommand(cmd, "");
        for (int i = 0; i < cmd.getTimeOut(); i++) {
            try { TimeUnit.MILLISECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
            try {
                if (response.isResponse()) {
                    return response;
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return null;
    }

    @Override
    protected boolean writeByte(byte ch) {
        return false;
    }

    @Override
    protected int getByte() {
        return 0;
    }

    private class WorkerThread extends Thread {

        WorkerThread(){
            List<BasicNameValuePair> extraHeaders = Collections.singletonList(new BasicNameValuePair("Cookie", "session=abcd"));
            mConnection = new WebSocketClient(URI.create("ws://scl/ws"), webSocketHandler,extraHeaders);
        }

        @Override
        public void run() {
            mConnection.connect();
            mContext.sendBroadcast(new Intent(Module.CONNECT));
        }

        void stopWorkingThread() {
            mConnection.disconnect();
            Thread.currentThread().interrupt();
        }

        WebSocketClient.Listener webSocketHandler = new WebSocketClient.Listener() {
            @Override
            public void onConnect() {

            }

            @Override
            public void onMessage(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String weight = jsonObject.getString("w");
                    int charge = jsonObject.getInt("c");
                    boolean stable = jsonObject.getBoolean("s");
                    //Commands cmd = Commands.valueOf(s.substring(0, 3));


                } catch (Exception e) {
                    Log.i(TAG, e.getMessage());
                }
            }

            @Override
            public void onMessage(byte[] data) {

            }

            @Override
            public void onDisconnect(int code, String reason) {

            }

            @Override
            public void onError(Exception error) {

            }
        };


    }



    /*WebSocketConnection mConnection;
    private WorkerThread workerThread;

    public ClientWebSoket(Context context, String address, int port){
        super(context, address, port);
    }

    public ClientWebSoket(Context context, InetSocketAddress address){
        super(context,address);
    }

    @Override
    public void killWorkingThread() {
        if(workerThread != null) {
            workerThread.stopWorkingThread();
            workerThread = null;
        }
    }

    @Override
    public void restartWorkingThread() {
        if(workerThread == null) {
            workerThread = new WorkerThread();
            workerThread.start();
        }
    }

    @Override
    public void write(String data) {
        StringBuilder s = new StringBuilder(data);
        //s.append('\r').append('\n');
        mConnection.sendMessage(s.toString());
    }

    @Override
    public synchronized ObjectCommand sendCommand(Commands cmd) {
        write(cmd.toString());
        response = new ObjectCommand(cmd, "");
        for (int i = 0; i < cmd.getTimeOut(); i++) {
            try { TimeUnit.MILLISECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
            try {
                if (response.isResponse()) {
                    return response;
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return null;
    }

    @Override
    protected boolean writeByte(byte ch) {
        return false;
    }

    @Override
    protected int getByte() {
        return 0;
    }

    private class WorkerThread extends Thread {
        private AtomicBoolean working;

        WorkerThread(){
            mConnection = new WebSocketConnection();
        }

        @Override
        public void run() {
            try {
                mConnection.connect(address, webSocketHandler);
                mContext.sendBroadcast(new Intent(Module.CONNECT));

            } catch (WebSocketException e) {
                e.printStackTrace();
            }
        }

        public void stopWorkingThread() {
            mConnection.sendClose();
            Thread.currentThread().interrupt();
        }

        IWebSocketConnectionHandler webSocketHandler = new IWebSocketConnectionHandler() {
            @Override
            public void onConnect(ConnectionResponse connectionResponse) {
                Log.i(TAG, connectionResponse.toString());
            }

            @Override
            public void onOpen() {
                Log.e(TAG, "Open Connection");
            }

            @Override
            public void onClose(int i, String s) {
                Log.e(TAG, s);
            }

            @Override
            public void onMessage(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String weight = jsonObject.getString("w");
                    int charge = jsonObject.getInt("c");
                    boolean stable = jsonObject.getBoolean("s");
                    //Commands cmd = Commands.valueOf(s.substring(0, 3));

                    *//*try {
                        if (cmd == response.getCommand()){
                            response.setValue(s.replace(cmd.name(),""));
                            response.setResponse(true);
                        }else {
                            //ObjectCommand obj = new ObjectCommand(cmd, substring.replace(cmd.name(),""));
                            throw new Exception(" ");
                        }
                    }catch (Exception e){
                        ObjectCommand obj = new ObjectCommand(cmd, s.replace(cmd.name(),""));
                        mContext.sendBroadcast(new Intent(Module.COMMAND).putExtra(InterfaceModule.EXTRA_SCALES, obj));
                    }   *//*
                } catch (Exception e) {
                    Log.i(TAG, e.getMessage());
                }
            }

            @Override
            public void onMessage(byte[] bytes, boolean b) {
                Log.e(TAG, new String(bytes));
            }

            @Override
            public void onPing() {
                Log.e(TAG, "Ping");
            }

            @Override
            public void onPing(byte[] bytes) {
                Log.e(TAG, "Ping");
            }

            @Override
            public void onPong() {
                Log.e(TAG, "Pong");
            }

            @Override
            public void onPong(byte[] bytes) {
                Log.e(TAG, "Pong");
            }

            @Override
            public void setConnection(WebSocketConnection webSocketConnection) {
                Log.e(TAG, webSocketConnection.toString());
            }
        };
    }*/

}
