package com.example.haider.resoluteaipeertopeercalling;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.haider.resoluteaipeertopeercalling.databinding.ActivitySignUpBinding;
import com.example.haider.resoluteaipeertopeercalling.userNameHOlder.userName;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUp extends AppCompatActivity {
    ActivitySignUpBinding binding;
    protected FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //-------Initializing the Firebase database-------//
        firebaseDatabase=FirebaseDatabase.getInstance();

        //---------Creating user Account using Google Firebase-------//
        mAuth=FirebaseAuth.getInstance();
        //-----------Checking if the user is Already login-----------//
        if(mAuth.getCurrentUser()!=null) {
            Intent intent = new Intent(SignUp.this, NumberActivity.class);
            startActivity(intent);
            finish();
        }
        //-----------Else Creating new User Account------------//
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user=binding.txtUsername.getText().toString();
                String email=binding.txtEmail.getText().toString();
                String password=binding.txtPassword.getText().toString();
                //-----------Setting up the Progress dialog-------//
                ProgressDialog dialog=new ProgressDialog(SignUp.this);
                dialog.setTitle("Creating Account...");
                dialog.setMessage("Please be patient we are creating your account ");

                if(!binding.txtEmail.getText().toString().replaceAll("\\s","").isEmpty()
                        && !binding.txtPassword.getText().toString().replaceAll("\\s","").isEmpty()
                        && !binding.txtUsername.getText().toString().replaceAll("\\s","").isEmpty())
                {
                    dialog.show();
                    mAuth.createUserWithEmailAndPassword(email, password).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            dialog.dismiss();
                            if(task.isSuccessful()){
                                Intent intent=new Intent(SignUp.this,NumberActivity.class);
                                //------SAVING The User Under The User ID-------//
                                FirebaseUser uId = mAuth.getCurrentUser();
                                userName nameObj=new userName(user,email,password);

                                firebaseDatabase.getReference().child("Users").child(uId.getUid()).setValue(nameObj);
                                
                                Toast.makeText(SignUp.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(SignUp.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else{
                    Toast.makeText(SignUp.this, "Enter correct values", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //----------Already Have Account-------------//
        binding.txtAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUp.this,Long.class);
                startActivity(intent);;
                finish();
            }
        });

    }
}