package com.example.projetapplimetal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;

public class AddConcertActivity extends AppCompatActivity {

    EditText nom;
    EditText date;
    EditText duree;
    EditText heure;
    EditText lng;
    EditText lat;


    Bitmap image_to_send;


    static final int DEMANDER_IMAGE= 1;





    boolean validate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_concert);

        nom = findViewById(R.id.nom_concert);
        date = findViewById(R.id.edit_DATE);
        heure = findViewById(R.id.edit_heure);
        duree = findViewById(R.id.edit_duree);
        lng = findViewById(R.id.lng_edit);
        lat = findViewById(R.id.lat_edit);

        Intent result = getIntent();

        lng.setText(Double.toString(result.getDoubleExtra("long" , 0)));
        lat.setText(Double.toString(result.getDoubleExtra("lat" , 0)));
    }


    public void valider(View v){

        validate = true;
        finish();


    }

    public void annuler(View v){

        validate = false;
        finish();

    }

    public void prendrePhoto(View v){

        Intent prendrePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (prendrePhoto.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(prendrePhoto, DEMANDER_IMAGE);
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

            Log.println(Log.ASSERT , "BLA" , "TESSSSST");


            if (requestCode == DEMANDER_IMAGE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                image_to_send = (Bitmap) extras.get("data");

            }






    }

    @Override
    public void finish(){

        Intent intent = new Intent(this, MapsActivity.class);

        if(!validate){

            setResult(RESULT_CANCELED);
            super.finish();

        }

        if(validate){

            intent.putExtra("titre" , nom.getText().toString());
            Log.println(Log.ASSERT , "valeur_titre" , nom.getText().toString());
            intent.putExtra("date" , date.getText().toString());
            intent.putExtra("heure" , heure.getText().toString());
            intent.putExtra("duree" , duree.getText().toString());
            intent.putExtra("longi" , Double.valueOf(lng.getText().toString()));
            intent.putExtra("lati" , Double.valueOf(lat.getText().toString()));

            if (image_to_send!=null) intent.putExtra("image" , image_to_send);

            setResult(RESULT_OK , intent);
            super.finish();


        }




    }




}
