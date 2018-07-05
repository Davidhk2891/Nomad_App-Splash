package com.nomadapp.splash.utils.sysmsgs.connectionlost;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.nomadapp.splash.R;

/**
 * Created by David on 12/3/2017.
 */

public class ConnectionLost {

    Context ctx;

    public ConnectionLost(Context ctx) {
        this.ctx = ctx;
    }

    public void connectionLostDialog(){
        String connectionLostTitle = ctx.getResources()
                .getString(R.string.connectionLost_java_connectionLost);
        String connectionLostMsg = ctx.getResources()
                .getString(R.string.connectionLost_java_checkYourInternet);
        AlertDialog.Builder connectionLostAlert;
        connectionLostAlert = new AlertDialog.Builder(ctx);
        connectionLostAlert.setTitle(connectionLostTitle);
        connectionLostAlert.setIcon(android.R.drawable.ic_dialog_alert);
        connectionLostAlert.setMessage(connectionLostMsg);
        connectionLostAlert.setPositiveButton("Ok",null);
        connectionLostAlert.setCancelable(false);
        connectionLostAlert.show();
    }

    public void connectivityStatus(Context ctx){
        ConnectivityManager cm =
                (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(!isConnected){
            connectionLostDialog();
        }
    }
}
