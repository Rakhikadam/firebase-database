package com.Firebase_databaseEx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recycler = findViewById(R.id.recycler);
        FirebaseFirestore db =FirebaseFirestore.getInstance(); //declare and initalize firebase

        //using type of Map in coolection
        //putting data ..putting data applied and data put is owner
        //1st example put


        Map<String,Object>user = new HashMap<>();
        user.put("firstname","rakhi1");    //put data with values and key
        user.put("lastname","kadam");
        user.put("number","9887737321");

        //map has key and value
        //2nd example put
        Map<String,Object>hotel = new HashMap<>();
        hotel.put("name","star hotel");
        hotel.put("offer","20%OFF");
        hotel.put("image","https://www.itchotels.com/content/dam/itchotels/in/umbrella/images/headmast-desktop/welcomhotel-bhubaneswar.jpg");

        //using addOnSuccessListener becz of put data add into the in this listner
        //1st example
        db.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.e("TAG","onSuccess:"+documentReference.getId());

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG", "onFailure: "+ e.getMessage());

            }
        });


        //using addOnSuccessListener becz of put data add into the in this listner
        //2nd example
        db.collection("hotel").add(hotel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.e("TAG","onSuccess:"+documentReference.getId());

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "onFailure: "+ e.getMessage());

                    }
                });




      //get data .. getting data method applied to te user or customer
        //1st example
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Log.e("TAG", document.getId() + " => " + document.getData());
                       String number =  document.getData().get("number").toString();
                    }
                } else {
                    Log.e("TAG", "Error getting documents.", task.getException());
                }
            }
        });


     //example for loop
        //2nd example
      /* for (hotellist obj: list){

       }
      */
        List<hotellist>list = new ArrayList<>();
        db.collection("hotel").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                  // List<hotellist>list = new ArrayList<>();

                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                       // List<hotellist>list = new ArrayList<>();
                        // String name = documentSnapshot.getData().get("name").toString();
                        hotellist hotel = new hotellist(documentSnapshot.getData().get("name").toString(),documentSnapshot.getData().get("offer").toString(),documentSnapshot.getData().get("image").toString());
                        list.add(hotel);

                    }

                    recycler.setLayoutManager(new LinearLayoutManager(MainActivity.this,RecyclerView.HORIZONTAL,false));
                    HotelAdpter adpter = new HotelAdpter(list);
                    recycler.setAdapter(adpter);

                }
                else {
                    Log.e("TAG", "Error getting documents.", task.getException());


                }
            }
        });



    }
    class HotelAdpter extends RecyclerView.Adapter<HotelAdpter.CustomViewHolder>{
        List<hotellist> list;

        public HotelAdpter(List<hotellist> list) {
            this.list = list;

        }

        @NonNull
        @Override
        public HotelAdpter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
           View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.hotelslist,parent,false);
           CustomViewHolder holder = new CustomViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull HotelAdpter.CustomViewHolder holder, int position) {
            holder.name.setText(list.get(position).getName());
            holder.offers.setText(list.get(position).getOffer());
            Glide.with(MainActivity.this).load(list.get(position).getImage()).into(holder.image);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class CustomViewHolder extends RecyclerView.ViewHolder{
            TextView name,offers;
            ImageView image;

            public CustomViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.ftext2page10);
                offers = itemView.findViewById(R.id.ftext1page10);
                image = itemView.findViewById(R.id.fimage1page10);

            }
        }
    }
}