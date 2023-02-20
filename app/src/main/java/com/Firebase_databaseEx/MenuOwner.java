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
import android.widget.TextView;

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
 * Use the {@link MenuOwner#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuOwner extends Fragment {
    SharedPreferences preferences;
    List<Menulist> list;
    RecyclerView recycler;
    SharedPreferences.Editor editor;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MenuOwner() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuOwner.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuOwner newInstance(String param1, String param2) {
        MenuOwner fragment = new MenuOwner();
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
        View view = inflater.inflate(R.layout.fragment_menu_owner, container, false);
        Button addmenu = view.findViewById(R.id.add_button);
        Button savebutton = view.findViewById(R.id.savebuttin);
        recycler = view.findViewById(R.id.menurecycler);
        EditText menuname = view.findViewById(R.id.menuname);
        EditText menuprice = view.findViewById(R.id.menuprice);
        EditText menuimage = view.findViewById(R.id.menuimmage);
        LinearLayout liner = view.findViewById(R.id.menuinfo);
        long timestamp = System.currentTimeMillis();
        //Initalize sharedprefernce
        preferences = getActivity().getSharedPreferences("MYAPP", Context.MODE_PRIVATE);

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycler.setVisibility(View.VISIBLE);
                addmenu.setVisibility(View.VISIBLE);
                liner.setVisibility(View.GONE);
                //put data in the savebutton using HashMap
                Map<String, Object> Menu = new HashMap<>();
                Menu.put("name", menuname.getText().toString());    //put data with values and key
                Menu.put("price", menuprice.getText().toString());
                Menu.put("image", menuimage.getText().toString());
                Menu.put("hotel_id", preferences.getString("hotel_id", "")); //getting hotel_id by sharedprefernce
                Menu.put("time",timestamp);
                //add data in this addOnSuccessListener
                db.collection("Menu").add(Menu).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.e("TAG", "onSuccess:" + documentReference.getId());
                        getMenu(); //call method

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "onFailure: " + e.getMessage());
                    }
                });

            }
        });

        addmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycler.setVisibility(View.GONE);
                liner.setVisibility(View.VISIBLE);


            }
        });


       getMenu();   //call getMenu method


        return view;
    }

    //create getMenu method becz this method called more time
    //called addOnsuccessclicklistner method
    private void getMenu() {
        db.collection("Menu").whereEqualTo("hotel_id", preferences.getString("hotel_id", "")).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                list = new ArrayList<>();
                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                    String name = snapshot.get("name").toString();
                    String image = snapshot.get("image").toString();
                    String price = snapshot.get("price").toString();
                    String hotel_id = snapshot.get("hotel_id").toString();
                    Menulist menulist = new Menulist(name,price,image);
                    list.add(menulist);
                }
                recycler.setLayoutManager(new LinearLayoutManager(getContext()));
                MenuAdpter adpter = new MenuAdpter(list);
                recycler.setAdapter(adpter);
            }
        });
    }

    class MenuAdpter extends RecyclerView.Adapter<MenuAdpter.CustomViewHolder> {
        List<Menulist> list;

        public MenuAdpter(List<Menulist> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public MenuAdpter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.menulist, parent, false);
            CustomViewHolder holder = new CustomViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MenuAdpter.CustomViewHolder holder, int position) {

            holder.name.setText(list.get(position).getName());
            holder.price.setText(list.get(position).getPrice());
            Glide.with(getContext()).load(list.get(position).getImage()).into(holder.image);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class CustomViewHolder extends RecyclerView.ViewHolder {
            ImageView image;
            TextView name, price;

            public CustomViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.imagemenu);
                name = itemView.findViewById(R.id.menuname);
                price = itemView.findViewById(R.id.price);


            }
        }
    }
}