package com.example.projetapplimetal;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;

public class ConcertAdapter extends ArrayAdapter<ConcertWindowData>  {

    private ArrayList<ConcertWindowData> listeConcerts;

    Context context;

    private static class ViewHolder {

        ImageView image;
        TextView titre;
        TextView date;
        TextView duree;
        TextView heure;
        Button voirConcert;
        Button deleteConcert;

    }

    public ConcertAdapter(ArrayList<ConcertWindowData> data, Context context){

        super(context , R.layout.row_concert , data);
        this.listeConcerts = data;
        this.context = context;



    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final ConcertWindowData concert = getItem(position);

        ViewHolder viewHolder;

        final View result;


        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_concert, parent, false);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.imageRow);
            viewHolder.titre = convertView.findViewById(R.id.titreRow);
            viewHolder.date = convertView.findViewById(R.id.dateRow);
            viewHolder.duree = convertView.findViewById(R.id.dureeRow);
            viewHolder.heure = convertView.findViewById(R.id.heureRow);
            viewHolder.voirConcert = convertView.findViewById(R.id.go);
            viewHolder.deleteConcert = convertView.findViewById(R.id.supprimer);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        lastPosition = position;

        viewHolder.image.setImageBitmap(concert.getImage().getBitmap());
        viewHolder.titre.setText(concert.getNom());
        viewHolder.date.setText(concert.getDate());
        viewHolder.heure.setText(concert.getHeure());
        viewHolder.duree.setText(concert.getDuree());

        viewHolder.voirConcert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context , MapsActivity.class);
                intent.setFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                context.startActivity(intent);

            }
        });
        // Return the completed view to render on screen
        return convertView;
    }



    }





