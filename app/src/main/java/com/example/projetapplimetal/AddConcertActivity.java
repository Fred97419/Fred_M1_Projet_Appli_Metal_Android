package com.example.projetapplimetal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class AddConcertActivity extends AppCompatActivity {

    EditText nom;
    EditText date;
    EditText duree;
    EditText lng;
    EditText lat;

    public final int VALIDATE_ADD = 43;
    public final int VALIDATE_CANCELED = 44;

    boolean validate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_concert);

        nom = findViewById(R.id.nom_concert);
        date = findViewById(R.id.edit_DATE);
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


    @Override
    public void finish(){

        Intent intent = new Intent();

        if(!validate){

            setResult(RESULT_CANCELED);
            super.finish();

        }

        if(validate){

            intent.putExtra("titre " , nom.getText().toString());
            Log.println(Log.ASSERT , "valeur_titre" , nom.getText().toString());
            intent.putExtra("date" , date.getText().toString());
            intent.putExtra("duree" , duree.getText().toString());
            intent.putExtra("longi" , Double.valueOf(lng.getText().toString()));
            intent.putExtra("lati" , Double.valueOf(lat.getText().toString()));
            setResult(RESULT_OK , intent);
            super.finish();


        }




    }




}
