package com.example.projetapplimetal;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


/**
 * Classe qui va adapter la liste des concerts à la ListView.
 * Elle prend la shema du fichier row_concert.xml pour représenter
 * une cellule.
 *
 * Chaque cellule contient toutes les informations du concert avec
 * en plus un bouton "Go" qui va nous emmener vers le concert sur l'api GoogleMap,
 * et un bouton pour supprimer l'élement.
 */
public class ConcertAdapter extends ArrayAdapter<ConcertWindowData> {

    private ArrayList<ConcertWindowData> listeConcerts;
    private double send_lat;
    private double send_long;


    Context context;
    AppCompatActivity liste_activity;

    private static class ViewHolder {

        ImageView image;
        TextView titre;
        TextView date;
        TextView duree;
        TextView heure;
        Button voirConcert;
        Button deleteConcert;

    }


    public ConcertAdapter(AppCompatActivity liste_activity,ArrayList<ConcertWindowData> data, Context context) {

        super(context, R.layout.row_concert, data);
        this.listeConcerts = data;
        this.context = context;
        this.liste_activity = liste_activity;

        this.send_long = -1;
        this.send_lat = -1;



    }

    private int lastPosition = -1;

    /**
     * Va adapter notre liste des concerts en vue pour la ListView, si on appuie sur "GO"
     * on modifie l'attribut send_long et send_lat avec la position du concert, sinon
     * ils ont une valeur par default égale à -1.
     *
     * Si on appuie sur le bouton "X" on supprime l'élément de la liste des concerts
     * et on actualise la ListView
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
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

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
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

                send_long=concert.getLng();
                send_lat=concert.getLat();
                liste_activity.finish();
            }
        });

        viewHolder.deleteConcert.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                listeConcerts.remove(concert);
                notifyDataSetChanged();

                Bundle listeBundle = new Bundle();
                listeBundle.putSerializable("liste_avec_supression" , listeConcerts);
                Intent listeChanged = new Intent(context , MapsActivity.class);
                listeChanged.putExtras(listeBundle);


            }


        });

        return convertView;
    }

    public ArrayList<ConcertWindowData> getListeConcerts() {
        return this.listeConcerts;
    }

    public double getSend_long() {
        return send_long;
    }

    public double getSend_lat() {
        return send_lat;
    }
}





