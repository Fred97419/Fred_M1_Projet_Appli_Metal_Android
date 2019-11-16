package com.example.projetapplimetal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

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

            ArrayList<ConcertWindowData> liste = (ArrayList<ConcertWindowData>) bundle.getSerializable("listeConcert");

            adapter = new ConcertAdapter( liste, getApplicationContext());

            liste_concertsView.setAdapter(adapter);


        }
    }
}
