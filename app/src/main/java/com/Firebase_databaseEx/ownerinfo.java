package com.Firebase_databaseEx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

public class ownerinfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ownerinfo);
        Button menulist = findViewById(R.id.menu);
        Button photo = findViewById(R.id.photo);
        Button facilites = findViewById(R.id.facilites);
        Button review = findViewById(R.id.review);
        DrawerLayout drawer = findViewById(R.id.drawer);
        Toolbar tool = findViewById(R.id.tool);

        tool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        menulist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = ownerinfo.this.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.framemenu,new MenuOwner());
                transaction.commit();
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = ownerinfo.this.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.framemenu,new PhotoOwner());
                transaction.commit();

            }
        });
        facilites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = ownerinfo.this.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.framemenu,new FacilitesOwner());
                transaction.commit();

            }
        });
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = ownerinfo.this.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.framemenu,new ReviewOwner());
                transaction.commit();

            }
        });


    }
}