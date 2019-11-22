package com.example.projetapplimetal;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Classe qui va réagir en fonction de si l'on rentre dans la zone du ProximityIntent relative
 * à chaque concert.
 *
 * Si l'on rentre ou ouvre une boite de dialogue disant qu'il y a un concert à proximité
 */
public class ProximityBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Resources res = context.getResources();
        String key = LocationManager.KEY_PROXIMITY_ENTERING;


        Boolean entering = intent.getBooleanExtra(key, false);

        String test = intent.getDataString();


        if (entering) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle(res.getString(R.string.broadcast_proxi));

            alertDialogBuilder.setMessage(res.getString(R.string.broadcast_leConcert)+" "+ res.getString(R.string.broadcast_proche));

            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {



                }
            });





            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
        else {


        }

    }
}
