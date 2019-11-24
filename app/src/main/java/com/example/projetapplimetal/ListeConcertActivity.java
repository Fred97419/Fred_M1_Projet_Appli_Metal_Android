package com.example.projetapplimetal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Activité qui va afficher la ListView avec nos concerts.
 * Et mettre à jour la liste si changements.
 *
 */
public class ListeConcertActivity extends AppCompatActivity {

    ListView liste_concertsView;
    ConcertAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_concert);
        liste_concertsView = findViewById(R.id.listeConcerts);

        Intent list_from_map = getIntent();

        if (list_from_map != null) {

            Bundle bundle = list_from_map.getExtras();

            /** Récupère la liste des concerts passé via Intent puis l'affiche via la ListView **/
            ArrayList<ConcertWindowData> liste = new ArrayList<ConcertWindowData>();
            int taille = bundle.getInt("longueur");

            for (int i=0 ; i< taille ; i++){

                liste.add( (ConcertWindowData) bundle.getSerializable("concert"+i));
            }

            adapter = new ConcertAdapter(this , liste, getApplicationContext());

            liste_concertsView.setAdapter(adapter);


        }
    }
    public void OK(View v){


        Log.println(Log.ASSERT , "listeView value " , adapter.getListeConcerts().toString());
        finish();


    }

    /**
     * Envoie la liste des concerts mis à jour,
     * et va envoyer la longitude et latitude :
     *
     * (-1,-1) si l'on a pas appuyer sur le bouton "OK"
     *
     * sinon la longitude et latitude du concert ou l'on veut aller
     * @see ConcertAdapter
     */
    @Override
    public void finish(){

        Bundle extras = new Bundle();

        extras.putInt("listeViewLong" , adapter.getListeConcerts().size());

        for (int i = 0 ; i< adapter.getListeConcerts().size() ; i++){

            extras.putSerializable("listeView"+i , adapter.getListeConcerts().get(i));
        }
        extras.putDouble("go_long" , adapter.getSend_long());
        extras.putDouble("go_lat" , adapter.getSend_lat());


        Intent forMapActivity = new Intent(this, MapsActivity.class);

        forMapActivity.putExtras(extras);

        setResult(RESULT_OK , forMapActivity);

        super.finish();


    }


}
