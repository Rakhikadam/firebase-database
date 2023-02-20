package com.Firebase_databaseEx;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewOwner#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewOwner extends Fragment {
    FirebaseFirestore db;
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

    public ReviewOwner() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReviewOwner.
     */
    // TODO: Rename and change types and number of parameters
    public static ReviewOwner newInstance(String param1, String param2) {
        ReviewOwner fragment = new ReviewOwner();
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
        View view= inflater.inflate(R.layout.fragment_review_owner, container, false);
         recycler = view.findViewById(R.id.recyclerreiew);
         preferences = getActivity().getSharedPreferences("MYAPP", Context.MODE_PRIVATE);
        db = FirebaseFirestore.getInstance();

        getfacility();
        return view;
    }

    private void getfacility(){
        db.collection("Review").whereEqualTo("hotel_id",preferences.getString("hotel_id","")).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<review> list = new ArrayList<>();
                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                    String name = snapshot.get("name").toString();
                    String comment = snapshot.get("comment").toString();
                    String profile = snapshot.get("profile").toString();
                    review list1 = new review(name,comment,profile);
                    list.add(list1);

                }
                recycler.setLayoutManager(new LinearLayoutManager(getContext()));
                ReviewAdpter adpter = new ReviewAdpter(list);
                recycler.setAdapter(adpter);


            }
        });

    }

    class ReviewAdpter extends RecyclerView.Adapter<ReviewAdpter.CustomViewHolder>{
        List<review> list;

        public ReviewAdpter(List<review> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public ReviewAdpter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
          View view = LayoutInflater.from(getContext()).inflate(R.layout.reviewlist,parent,false);
          CustomViewHolder holder = new CustomViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ReviewAdpter.CustomViewHolder holder, int position) {
            holder.name.setText(list.get(position).getName());
            holder.comment.setText(list.get(position).getComment());
            Glide.with(getContext()).load(list.get(position).getProfile()).into(holder.profile);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class CustomViewHolder extends RecyclerView.ViewHolder{
            TextView name,comment;
            ImageView profile;
            public CustomViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.reviewname);
                comment = itemView.findViewById(R.id.comments);
                profile = itemView.findViewById(R.id.profile);

            }
        }
    }
}