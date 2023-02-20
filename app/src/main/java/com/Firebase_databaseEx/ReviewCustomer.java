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
 * Use the {@link ReviewCustomer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewCustomer extends Fragment {
    FirebaseFirestore db;
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

    public ReviewCustomer() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReviewCustomer.
     */
    // TODO: Rename and change types and number of parameters
    public static ReviewCustomer newInstance(String param1, String param2) {
        ReviewCustomer fragment = new ReviewCustomer();
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
            mParam1 = getArguments().getString("data");
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review_customer, container, false);
        Button add = view.findViewById(R.id.addreview);
        Button save = view.findViewById(R.id.savereview);
        LinearLayout linear = view.findViewById(R.id.linearreview);
         recycler = view.findViewById(R.id.reviewreycler);
        EditText name = view.findViewById(R.id.name);
        EditText profile = view.findViewById(R.id.profile);
        EditText comment = view.findViewById(R.id.comment);
        db = FirebaseFirestore.getInstance();
        preferences = getActivity().getSharedPreferences("MYAPP", Context.MODE_PRIVATE);



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycler.setVisibility(View.GONE);
                linear.setVisibility(View.VISIBLE);
                add.setVisibility(View.GONE);

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycler.setVisibility(View.VISIBLE);
                add.setVisibility(View.VISIBLE);
                linear.setVisibility(View.GONE);
                Map<String,Object>review = new HashMap<>();
                review.put("name",name.getText().toString());
                review.put("profile",profile.getText().toString());
                review.put("comment",comment.getText().toString());
                review.put("user_id",preferences.getString("user_id",""));
                review.put("hotel_id",mParam1);

                db.collection("Review").add(review).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.e("TAG", "onSuccess: "+documentReference.getId() );
                        getReview();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG","onFailure:"+e.getMessage());
                    }
                });

            }
        });

        getReview();
        return view;
    }
    private void getReview(){
        db.collection("Review").whereEqualTo("user_id",preferences.getString("user_id","")).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<custoreview>list = new ArrayList<>();
                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                    String name = snapshot.get("name").toString();
                    String comment = snapshot.get("comment").toString();
                    String profile = snapshot.get("profile").toString();
                    String user_id = snapshot.get("user_id").toString();
                    custoreview list1 = new custoreview(name,comment,profile,user_id);
                    list.add(list1);

                }
                recycler.setLayoutManager(new LinearLayoutManager(getContext()));
                ReviewAdpter adpter = new ReviewAdpter(list);
                recycler.setAdapter(adpter);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG", "onFailure: "+e.getMessage() );
            }
        });
    }

    class ReviewAdpter extends RecyclerView.Adapter<ReviewAdpter.CustomViewHolder>{
        List<custoreview> list;

        public ReviewAdpter(List<custoreview> list) {

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