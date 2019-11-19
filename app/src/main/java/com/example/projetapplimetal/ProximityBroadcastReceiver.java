package com.example.projetapplimetal;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

        String key = LocationManager.KEY_PROXIMITY_ENTERING;


        Boolean entering = intent.getBooleanExtra(key, false);
        if (entering) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle("Concert à proximité");

            alertDialogBuilder.setMessage("Le concert "+ " est proche de vous, voulez vous voir ?");

            alertDialogBuilder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {



                }
            });

            alertDialogBuilder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
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
