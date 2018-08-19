package com.konst.scaleslibrary.module;

import android.content.Context;

import com.konst.scaleslibrary.module.wifi.ClientWiFi;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Client {
    protected Context mContext;
    protected BufferedReader bufferedReader;
    protected PrintWriter printWriter;
    protected ObjectCommand response;
    private AtomicBoolean working;
    private InetSocketAddress inetSocketAddress;
    protected String address;
    private int port;
    private static final int TIME_OUT_CONNECT = 2000; /** Время в милисекундах. */
    protected static final String TAG = Client.class.getName();

    public abstract void killWorkingThread();
    public abstract void restartWorkingThread();
    public abstract void write(String data);
    public abstract ObjectCommand sendCommand(Commands cmd);
    protected abstract boolean writeByte(byte ch);
    protected abstract int getByte();

    protected Client(Context context, String add, int port){
        mContext = context;
        address = "ws://"+add+"/ws";
        this.port = port;
    }

    protected Client(Context context, InetSocketAddress address){
        mContext = context;
        inetSocketAddress = address;
        this.address = "ws://"+inetSocketAddress.getHostString()+"/ws";
        port = inetSocketAddress.getPort();
    }
}
