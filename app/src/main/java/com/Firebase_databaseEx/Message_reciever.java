package com.Firebase_databaseEx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Message_reciever extends AppCompatActivity {
    EditText emailnumber;
    FirebaseFirestore db;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_reciever);
         db = FirebaseFirestore.getInstance();
         emailnumber = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        Button login = findViewById(R.id.logIn);
        preferences = getSharedPreferences("MYAPP",MODE_PRIVATE);//inatilize preference

        //login button set condition using patternmatches
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = emailnumber.getText().toString();
                if (Pattern.matches("[0-9]{1,}", number)) {
                    if (Pattern.matches("[7-9]{1}[0-9]{9}", number)) {
                        ProgressDialog dialog = new ProgressDialog(Message_reciever.this);
                        dialog.setCancelable(false);     //cancel the loading please wait dialoge
                        dialog.setMessage("Please wait");  // set dialoge
                        dialog.show();

                        db.collection("users").whereEqualTo("number",emailnumber.getText().toString()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
    //set condition of document size is less than one that means document size 0 the querysnapshots going to adduser method and creat new id
                                if (queryDocumentSnapshots.getDocuments().size()<1){
                                    addUser();
                                }
                                else {
                                    Intent intent = new Intent(Message_reciever.this, customerinfo.class);
                                    startActivity(intent);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });

                    } else {
                        Toast.makeText(Message_reciever.this, "Enter valid Number.", Toast.LENGTH_SHORT).show();
                    }
                }

                else if (Pattern.matches("[[a-z]+[A-Z]+[@#$%&*]+[0-9]+]{8,16}",number)) {

                    //using progressDialoge becz giving please wait message
                    ProgressDialog dialog = new ProgressDialog(Message_reciever.this);
                    dialog.setCancelable(false);
                    dialog.setMessage("Please wait");
                    dialog.show();

                    db.collection("ownerusers").whereEqualTo("email",emailnumber.getText().toString()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            dialog.dismiss();
                            if (queryDocumentSnapshots.getDocuments().size()<1){    //documents means one user
                                addowneruser();
                            }
                            else {
                                for (DocumentSnapshot snapshot:queryDocumentSnapshots.getDocuments()){
 //put hotelId and pass . getting hotelId by snapshot
                                    editor = preferences.edit();
                                    editor.putString("hotel_id", snapshot.getId());
                                    Log.e("tag",snapshot.getId());
                                    editor.commit();

                                }
                                Intent intent = new Intent(Message_reciever.this,ownerinfo.class);
                                startActivity(intent);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(Message_reciever.this, "check internet connection", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
                return;

            }
        });

    }

    //method of Customerusers
    private void addUser() {
        Map<String, Object> user = new HashMap<>();
        user.put("firstname", "rakhi1");    //put data with values and key
        user.put("lastname", "kadam");
        user.put("number",emailnumber.getText().toString() );
      //using successlistner
        db.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.e("TAG", "onSuccess:" + documentReference.getId());
                Intent intent = new Intent(Message_reciever.this, customerinfo.class);
                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Message_reciever.this, "Failedto add user.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //method of ownerusers
    private void addowneruser(){
        Map<String, Object> ownerusers = new HashMap<>();
        ownerusers.put("firstname", "rakhi1");    //put data with values and key
        ownerusers.put("number", "99999999");
        ownerusers.put("email", emailnumber.getText().toString());
       /* editor = preferences.edit();
        editor.putString("email",emailnumber.getText().toString());
        editor.commit();
       */ db.collection("ownerusers").add(ownerusers).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.e("TAG","OnSuccess:"+documentReference.getId());
                editor = preferences.edit();    //first this method called . this is inilize of editior
                editor.putString("hotel_id",documentReference.getId());
                editor.commit();

                Intent intent = new Intent(Message_reciever.this, UpdateOwner.class);
                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Message_reciever.this, "fail to add user", Toast.LENGTH_SHORT).show();
            }
        });

    }

}