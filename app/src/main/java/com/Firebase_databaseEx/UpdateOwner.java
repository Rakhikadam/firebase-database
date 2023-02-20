package com.Firebase_databaseEx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class UpdateOwner extends AppCompatActivity {
    Button save;
    FirebaseFirestore db;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_owner);
        EditText name = findViewById(R.id.name);
        EditText hotelimage = findViewById(R.id.image);
        EditText offers = findViewById(R.id.offer);
        EditText address = findViewById(R.id.address);
        EditText about = findViewById(R.id.about);
        EditText phone = findViewById(R.id.phone);
        EditText averagecost = findViewById(R.id.cost);

        db = FirebaseFirestore.getInstance();
        preferences = getSharedPreferences("MYAPP", MODE_PRIVATE);

        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("ownerusers").document( preferences.getString("hotel_id", "")).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String,Object> data  = documentSnapshot.getData();
                        data.put("firstname",name.getText().toString());
                        data.put("hotelimage",hotelimage.getText().toString());
                        data.put("offers",offers.getText().toString());
                        data.put("address",address.getText().toString());
                        data.put("about",about.getText().toString());
                        data.put("phone",phone.getText().toString());
                        data.put("averagecost",averagecost.getText().toString());
                        Log.e("TAG", "onSuccess: "+data.toString());
                        db.collection("ownerusers").document(preferences.getString("hotel_id","")).set(data);
                    }
                });
            }
        });
    }
}