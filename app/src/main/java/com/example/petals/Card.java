package com.example.petals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class Card extends AppCompatActivity {


CardView bell,corn,grape,potato,rice,wheat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_card);

        bell=findViewById(R.id.bellpepper);

        corn=findViewById(R.id.corn);
        grape=findViewById(R.id.grape);
        potato=findViewById(R.id.potato);
        rice=findViewById(R.id.rice);
        wheat=findViewById(R.id.wheat);

        bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Card.this, Bellpepper.class);
                startActivity(i);


            }
        });




        corn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent i = new Intent(Card.this, CornActivity.class);
                startActivity(i);


            }
        });
        grape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent ii = new Intent(Card.this, GrapeActivity.class);
               startActivity(ii);


            }
        });
        potato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i = new Intent(Card.this, PotatoActivity.class);
                startActivity(i);


            }
        });
        rice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i = new Intent(Card.this, RiceActivity.class);
                startActivity(i);


            }
        });
        wheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Card.this, WheatActivity.class);
               startActivity(i);


            }
        });








    }




    }
