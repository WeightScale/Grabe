package com.konst.scaleslibrary.module.wifi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.konst.scaleslibrary.R;
import com.konst.scaleslibrary.SearchDialog;

import java.util.ArrayList;
import java.util.List;

public class SearchDialogWiFi extends SearchDialog {
    private BaseReceiver broadcastReceiver;                   //приёмник намерений
    private List<ScanResult> scanResults;           //чужие устройства
    private ArrayAdapter<ScanResult> adapter;                   //адаптер имён
    private String message;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            message = getArguments().getString(ARG_MESSAGE);
        }
        //settings = new Settings(getActivity(), ScalesView.SETTINGS);
        //foundDevice = new ArrayList<>();
        broadcastReceiver = new BaseReceiver(getActivity());
        broadcastReceiver.register();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.search_device, null);
        imageViewButton = view.findViewById(R.id.buttonSearch);
        imageViewButton.setOnClickListener(this);
        imageViewButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_wifi));
        view.findViewById(R.id.buttonBack).setOnClickListener(this);
        textViewLog = view.findViewById(R.id.textLog);
        log(message);
        listView = view.findViewById(R.id.listViewDevices);  //список весов
        listView.setOnItemClickListener(onItemClickListener);

        WifiManager wifiManager =  (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        scanResults = wifiManager.getScanResults();

//        for (int i = 0; settings.contains(getActivity().getString(R.string.KEY_ADDRESS) + i); i++) { //заполнение списка
//            foundDevice.add(BluetoothAdapter.getDefaultAdapter().getRemoteDevice(settings.read(getActivity().getString(R.string.KEY_ADDRESS) + i, "")));
//        }
//
        adapter = new WiFiListAdapter(getActivity(), scanResults);
        listView.setAdapter(adapter);
//
//        if (foundDevice.isEmpty()) {
//            BluetoothAdapter.getDefaultAdapter().startDiscovery();
//        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        AlertDialog dialog = builder.create();
        setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonSearch) {
            searchDevice();
        } else {
            dismiss();
        }
    }

    public static SearchDialog newInstance(String text) {
        SearchDialogWiFi fragment = new SearchDialogWiFi();
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void searchDevice() {
        adapter.clear();
        adapter.notifyDataSetChanged();
        WifiManager wifiManager =  (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            scanResults = wifiManager.getScanResults();
            adapter = new WiFiListAdapter(getActivity(), scanResults);
            listView.setAdapter(adapter);
        }
    }

    private final AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            /*if (BluetoothAdapter.getDefaultAdapter().isDiscovering()) {
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
            }*/
            Intent intent = new Intent().putExtra(ARG_DEVICE,  scanResults.get(i).SSID);
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
            dismiss();
        }
    };

    class BaseReceiver extends BroadcastReceiver {
        final Context mContext;
        SpannableStringBuilder w;
        Rect bounds;
        ProgressDialog dialogSearch;
        final IntentFilter intentFilter;
        protected boolean isRegistered;

        BaseReceiver(Context context){
            mContext = context;
            intentFilter = new IntentFilter();

        }

        @Override
        public void onReceive(Context context, Intent intent) { //обработчик Bluetooth
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                }
            }
        }

        public void register() {
            isRegistered = true;
            mContext.registerReceiver(this, intentFilter);
        }

        public void unregister() {
            if (isRegistered) {
                mContext.unregisterReceiver(this);  // edited
                isRegistered = false;
            }
        }
    }
}
