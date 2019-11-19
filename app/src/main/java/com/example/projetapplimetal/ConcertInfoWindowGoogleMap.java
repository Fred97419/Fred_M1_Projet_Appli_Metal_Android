package com.example.projetapplimetal;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Adapte la fenêtre d'info de googleMap
 *
 * Par default elle est représenté par un titre uniquement,
 * ici on l'adapte pour afficher toutes les informations de notre concert.
 *
 * @see ConcertWindowData
 */
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

        Resources res = context.getResources();

        //récupération de la vue de concert_layout.xml

        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.concert_layout, null);

        TextView nom = view.findViewById(R.id.titreRow);
        ImageView image = view.findViewById(R.id.image);
        TextView date = view.findViewById(R.id.date);
        TextView duree = view.findViewById(R.id.duree);
        TextView heure = view.findViewById(R.id.heure);





        //Description de la fenêtre


        ConcertWindowData concertWindowData = (ConcertWindowData) marker.getTag();

        nom.setText(concertWindowData.getNom());
        image.setImageBitmap(concertWindowData.getImage().getBitmap());
        date.setText(concertWindowData.getDate());
        duree.setText(concertWindowData.getDuree()+" "+res.getString(R.string.info_adapter_heure));
        heure.setText(concertWindowData.getHeure());


        return view;



    }
}
