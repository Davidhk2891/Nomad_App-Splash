package com.nomadapp.splash.utils.sysmsgs.questiondialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.nomadapp.splash.R;
import com.nomadapp.splash.ui.activity.standard.SignUpLogInActivity;

import com.parse.ParseUser;

/**
 * Created by David on 5/18/2018 for Splash.
 */
public class LogoutMessage {

    private Context context;

    public LogoutMessage(Context ctx){
        this.context = ctx;
    }
    public void logoutMessage(){
        AlertDialog.Builder pendingPaymentDialog = new AlertDialog.Builder(context);
        pendingPaymentDialog.setTitle(context.getResources().getString
                (R.string.logout_message_Logout));
        pendingPaymentDialog.setIcon(android.R.drawable.ic_dialog_alert);
        pendingPaymentDialog.setMessage(context.getResources()
                .getString(R.string.logout_message_areYouSureLogout));
        pendingPaymentDialog.setPositiveButton(context.getResources()
                .getString(R.string.logout_message_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent toSignorLogScreen = new Intent(context, SignUpLogInActivity.class);
                toSignorLogScreen.putExtra("fbLogout", "noRunFbParse");
                context.startActivity(toSignorLogScreen);
                ParseUser.logOut();
            }
        });
        pendingPaymentDialog.setNegativeButton(context.getResources().getString(R.string
                .logout_message_no), null);
        pendingPaymentDialog.setCancelable(false);
        pendingPaymentDialog.show();
    }
}
