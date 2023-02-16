package com.Firebase_databaseEx;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotoOwner#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoOwner extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    RecyclerView recycler;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PhotoOwner() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhotoOwner.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotoOwner newInstance(String param1, String param2) {
        PhotoOwner fragment = new PhotoOwner();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo_owner, container, false);
        Button addphoto = view.findViewById(R.id.photobutton);
         recycler = view.findViewById(R.id.recyclerphoto);
        LinearLayout linear = view.findViewById(R.id.linear);
        EditText text = view.findViewById(R.id.edit);
        Button save = view.findViewById(R.id.photosubmit);
        preferences = getActivity().getSharedPreferences("MYAPP", Context.MODE_PRIVATE);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycler.setVisibility(View.VISIBLE);
                addphoto.setVisibility(View.VISIBLE);
                linear.setVisibility(View.GONE);

                Map<String,Object> photo = new HashMap<>();
                photo.put("image",text.getText().toString());    //put data with values and key
                photo.put("hote_id",preferences.getString("hotel_id",""));
                db.collection("photo").add(photo).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.e("TAG","onSuccess:"+documentReference.getId());
                        getMenu();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "onFailure: "+ e.getMessage());

                    }
                });

            }
        });

        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear.setVisibility(View.VISIBLE);
                recycler.setVisibility(View.GONE);
            }
        });
        getMenu();
        return view;
    }
    public void getMenu(){
        db.collection("photo").whereEqualTo("hotel_id",preferences.getString("hotel_id","")).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Photolist>list = new ArrayList<>();
                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                    String image = snapshot.get("image").toString();
                    Photolist photo = new Photolist(image);
                    list.add(photo);
                }
                recycler.setLayoutManager(new LinearLayoutManager(getContext()));
                PhotoAdpter adpter = new PhotoAdpter(list);
                recycler.setAdapter(adpter);

            }
        });
    }
    class PhotoAdpter extends RecyclerView.Adapter<PhotoAdpter.CustomViewHolder>{
        List<Photolist> list;

        public PhotoAdpter(List<Photolist> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public PhotoAdpter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
          View view = LayoutInflater.from(getContext()).inflate(R.layout.photolist,parent,false);
          CustomViewHolder holder = new CustomViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull PhotoAdpter.CustomViewHolder holder, int position) {
            Glide.with(getContext()).load(list.get(position).getImage()).into(holder.image);

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class CustomViewHolder extends RecyclerView.ViewHolder{
            ImageView image;
            public CustomViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.imagephoto);
            }
        }
    }
}