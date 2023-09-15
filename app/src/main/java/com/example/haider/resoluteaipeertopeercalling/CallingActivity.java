package com.example.haider.resoluteaipeertopeercalling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.haider.resoluteaipeertopeercalling.databinding.ActivityCallingBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class CallingActivity extends AppCompatActivity {
    ActivityCallingBinding binding;
    FirebaseDatabase database;
    String user;
    FirebaseAuth mAuth;
    static boolean isOK=false;
    DatabaseReference reference;
    String createdBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCallingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //------Authentication Reference-------//
        mAuth=FirebaseAuth.getInstance();
        //getting the current use ID
        user = mAuth.getUid();

        //------DataBase Reference-------//
        reference= FirebaseDatabase.getInstance().getReference().child("Rooms");
        createdBy=getIntent().getStringExtra("createdBy");

        database=FirebaseDatabase.getInstance();
        database.getReference().child("Rooms")
                .orderByChild("status")
                .equalTo(0).limitToFirst(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getChildrenCount() > 0){
                            isOK=true;
                            //Room is Available
                            Toast.makeText(CallingActivity.this, "Available Room..", Toast.LENGTH_SHORT).show();
                            for(DataSnapshot childSnap: snapshot.getChildren()){
                                database.getReference()
                                        .child("Rooms")
                                        .child(Objects.requireNonNull(childSnap.getKey()))
                                        .child("incoming")
                                        .setValue(user);
                                database.getReference()
                                        .child("Rooms")
                                        .child(childSnap.getKey())
                                        .child("status")
                                        .setValue(1);

                                Intent intent=new Intent(CallingActivity.this,CallConnectedActivity.class);
                                intent.putExtra("username",user);
                                intent.putExtra("incoming",childSnap.child("incoming").getValue(String.class));
                                intent.putExtra("createdBy",childSnap.child("createdBy").getValue(String.class));
                                intent.putExtra("isAvailable",childSnap.child("isAvailable").getValue(Boolean.class));
                                startActivity(intent);

                            }

                        }
                        else{
                            //Room is not Available
                            HashMap<String,Object> room=new HashMap<>();
                            room.put("incoming",user);
                            room.put("createdBy",user);
                            room.put("isAvailable",true);
                            room.put("status",0);

                            database.getReference()
                                    .child("Rooms")
                                    .child(user)
                                    .setValue(room).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            database.getReference()
                                                    .child("Rooms")
                                                    .child(user).addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if(snapshot.child("status").exists())
                                                            {
                                                                if(snapshot.child("status").getValue(Integer.class)== 1 ){
                                                                    if(isOK)return;

                                                                    isOK=true;
                                                                    Intent intent=new Intent(CallingActivity.this,CallConnectedActivity.class);
                                                                    intent.putExtra("username",user);
                                                                    intent.putExtra("incoming",snapshot.child("incoming").getValue(String.class));
                                                                    intent.putExtra("createdBy",snapshot.child("createdBy").getValue(String.class));
                                                                    intent.putExtra("isAvailable",snapshot.child("isAvailable").getValue(Boolean.class));
                                                                    startActivity(intent);
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });

                                        }
                                    });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    @Override
    protected void onDestroy() {

        super.onDestroy();
        reference.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).setValue(null);
        finish();
    }
}