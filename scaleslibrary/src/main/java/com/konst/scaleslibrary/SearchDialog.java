package com.konst.scaleslibrary;

import android.app.DialogFragment;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public abstract class SearchDialog extends DialogFragment implements View.OnClickListener {
    Context mContext;
    protected TextView textViewLog;                             //лог событий
    protected ImageView imageViewButton;
    protected ListView listView;                                  //список весов
    protected static final String ARG_MESSAGE = SearchDialog.class.getSimpleName()+"MESSAGE";
    public static final String ARG_DEVICE = SearchDialog.class.getSimpleName()+"DEVICE";

    protected abstract void searchDevice();

    //==================================================================================================================
    void log(int resource) { //для ресурсов
        textViewLog.setText(getActivity().getString(resource) + '\n' + textViewLog.getText());
    }

    //==================================================================================================================
    public void log(String string) { //для текста
        textViewLog.setText(string + '\n' + textViewLog.getText());
    }

    //==================================================================================================================
    void log(int resource, boolean toast) { //для текста
        textViewLog.setText(getActivity().getString(resource) + '\n' + textViewLog.getText());
        if (toast) {
            Toast.makeText(getActivity(), resource, Toast.LENGTH_SHORT).show();
        }
    }

    //==================================================================================================================
    void log(int resource, String str) { //для ресурсов с текстовым дополнением
        textViewLog.setText(getActivity().getString(resource) + ' ' + str + '\n' + textViewLog.getText());
    }
}
