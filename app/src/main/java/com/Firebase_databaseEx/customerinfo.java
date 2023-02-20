package com.Firebase_databaseEx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class customerinfo extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences preferences ;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customerinfo);
        RecyclerView recycler = findViewById(R.id.recyclerhotel);
        preferences = getSharedPreferences("MYAPP",MODE_PRIVATE);

       List<hotellist>list = new ArrayList<>();
        db.collection("ownerusers").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        Log.e("TAG", "onComplete: "+documentSnapshot.getData() );
                        documentSnapshot.getData();
                        hotellist hotel1 = new hotellist(documentSnapshot.getData().get("firstname").toString(),documentSnapshot.getData().get("offers").toString(),documentSnapshot.getData().getOrDefault("hotelimage","NA").toString(),documentSnapshot.getId());
                        list.add(hotel1);
                    }
                    recycler.setLayoutManager(new LinearLayoutManager(customerinfo.this));
                    HotelAdpter adpter = new HotelAdpter(list);
                    recycler.setAdapter(adpter);
                }
                else {
                    Log.e("TAG","Error getting documents:",task.getException());
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
          View view = LayoutInflater.from(customerinfo.this).inflate(R.layout.hotelslist,parent,false);
          CustomViewHolder holder = new CustomViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull HotelAdpter.CustomViewHolder holder, int position) {

            holder.name.setText(list.get(position).getName());
            holder.offers.setText(list.get(position).getOffer());
            Glide.with(customerinfo.this).load(list.get(position).getImage()).into(holder.image);
            holder.relative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction transaction = customerinfo.this.getSupportFragmentManager().beginTransaction();
                    Fragment fragment =new ReviewCustomer();
                    Bundle bundle = new Bundle();
                    bundle.putString("data",list.get(position).getHotel_id());
                    fragment.setArguments(bundle);
                    transaction.replace(R.id.frame,fragment);
                    transaction.commit();
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class CustomViewHolder extends RecyclerView.ViewHolder{
            TextView name,offers;
            ImageView image;
            RelativeLayout relative;

            public CustomViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.ftext2page10);
                offers = itemView.findViewById(R.id.ftext1page10);
                image = itemView.findViewById(R.id.fimage1page10);
                relative = itemView.findViewById(R.id.realtive);

            }
        }
    }
}