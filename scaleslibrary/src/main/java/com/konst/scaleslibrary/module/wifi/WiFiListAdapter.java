package com.konst.scaleslibrary.module.wifi;

/*  */
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.konst.scaleslibrary.R;

import java.util.List;

class WiFiListAdapter extends ArrayAdapter<ScanResult> {
    private final List<ScanResult> items;

    public WiFiListAdapter(Context context, List<ScanResult> items) {
        super(context, R.layout.list_item_bluetooth, items);
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_item_bluetooth, parent, false);
        }

        if (v == null) {
            return null;
        }
        ScanResult o = items.get(position);
        if (o != null) {
            TextView tt = (TextView) v.findViewById(R.id.topText);
            TextView bt = (TextView) v.findViewById(R.id.bottomText);
            //LinearLayout linearLayout = (LinearLayout)v.findViewById(R.id.layout_item_bluetooth);
            /*if(position == 0)
                linearLayout.setBackgroundResource(R.color.background_light);*/
            if (tt != null) {
                tt.setText(o.SSID);
            }
            if (bt != null) {
                String address = o.BSSID;
                if (tt != null) {
                    if ("-".equalsIgnoreCase(address)) {
                        tt.setTextColor(0xFFFF5050);
                    } else
                    //tt.setTextColor(getContext().getResources().getColor(R.color.tt_item_bt));
                    {
                        tt.setTextColor(getContext().getResources().getColor(R.color.background2));
                    }
                }
                bt.setText(address);
            }
        }
        return v;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public void add(ScanResult object) {
        super.add(object);
    }
}
