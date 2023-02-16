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
import android.widget.LinearLayout;
import android.widget.TextView;

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
 * Use the {@link FacilitesOwner#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FacilitesOwner extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences preferences ;
    SharedPreferences.Editor editor;
    RecyclerView recycler;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FacilitesOwner() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FacilitesOwner.
     */
    // TODO: Rename and change types and number of parameters
    public static FacilitesOwner newInstance(String param1, String param2) {
        FacilitesOwner fragment = new FacilitesOwner();
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
        View view = inflater.inflate(R.layout.fragment_facilites_owner, container, false);
        Button addbutton = view.findViewById(R.id.add_button);
         recycler  = view.findViewById(R.id.recyclerfacility);
        LinearLayout linear = view.findViewById(R.id.facilityinfo);
        Button save = view.findViewById(R.id.facilitysave);
        EditText name = view.findViewById(R.id.facilityname);
        preferences = getActivity().getSharedPreferences("MYAPP", Context.MODE_PRIVATE);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear.setVisibility(View.GONE);
                recycler.setVisibility(View.VISIBLE);
                addbutton.setVisibility(View.VISIBLE);
                Map<String,Object>facility = new HashMap<>();
                facility.put("text",name.getText().toString());
                facility.put("hotel_id",preferences.getString("hotel_id",""));

                db.collection("facility").add(facility).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.e("TAG","onSuccess:"+documentReference.getId());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG","onFailure:"+e.getMessage());
                    }
                });

            }
        });

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear.setVisibility(View.VISIBLE);
                addbutton.setVisibility(View.GONE);

            }
        });

        return view;
    }
    private void getFacility(){
        db.collection("facility").whereEqualTo("hotel_id",preferences.getString("hotel_id","")).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Facilities>list = new ArrayList<>();
                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                    String name = snapshot.get("name").toString();
                    String hotel_id = snapshot.get("hotel_id").toString();

                    Facilities facilities = new Facilities(name);
                    list.add(facilities);
                }
                recycler.setLayoutManager(new LinearLayoutManager(getContext()));
                FacilityAdpter adpter = new FacilityAdpter(list);
                recycler.setAdapter(adpter);

            }
        });
    }

    class FacilityAdpter extends RecyclerView.Adapter<FacilityAdpter.CustomViewHolder>{
        List<Facilities> list;
        public FacilityAdpter(List<Facilities> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public FacilityAdpter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.facilittylist,parent,false);
            CustomViewHolder holder = new CustomViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull FacilityAdpter.CustomViewHolder holder, int position) {
            holder.text.setText(list.get(position).getText());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class CustomViewHolder extends RecyclerView.ViewHolder {
            TextView text;
            public CustomViewHolder(@NonNull View itemView) {
                super(itemView);
                text = itemView.findViewById(R.id.text);
            }
        }
    }

}