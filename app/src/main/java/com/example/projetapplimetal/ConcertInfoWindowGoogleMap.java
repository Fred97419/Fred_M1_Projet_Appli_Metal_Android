package com.example.projetapplimetal;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class ConcertInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {


    private Context context;

    public ConcertInfoWindowGoogleMap(Context context){

        this.context = context;
    }



    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        //récupération de la vue de concert_layout.xml

        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.concert_layout, null);

        TextView nom = view.findViewById(R.id.nom);
        ImageView image = view.findViewById(R.id.image);
        TextView date = view.findViewById(R.id.date);
        TextView duree = view.findViewById(R.id.duree);
        Button lien = view.findViewById(R.id.lien);

        lien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context , "LIEEEEEN" , Toast.LENGTH_LONG);
            }
        });



        //Description de la fenêtre


        ConcertWindowData concertWindowData = (ConcertWindowData) marker.getTag();
        int imageId = context.getResources().getIdentifier(concertWindowData.getImage().toLowerCase(),
                "drawable", context.getPackageName());

        nom.setText(concertWindowData.getNom());
        image.setImageResource(imageId);
        date.setText(concertWindowData.getDate());
        duree.setText(concertWindowData.getDuree()+" heure(s)");


        return view;



    }
}
