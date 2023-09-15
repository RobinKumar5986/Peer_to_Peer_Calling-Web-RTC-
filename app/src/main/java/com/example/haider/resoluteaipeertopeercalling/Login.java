package com.example.haider.resoluteaipeertopeercalling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.haider.resoluteaipeertopeercalling.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Login extends AppCompatActivity {
    ActivityLoginBinding binding;
    String email,password;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        //---------Creating the Progress Dialog----------//
        ProgressDialog dialog=new ProgressDialog(this);
        dialog.setTitle("Loading...");
        dialog.setTitle("Please be patient Loading Your Account Details...");
        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=binding.txtEmail.getText().toString();
                password=binding.txtPassword.getText().toString();

                if(!email.isEmpty() && !password.isEmpty()){
                    dialog.show();
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            dialog.dismiss();
                            if(task.isSuccessful()){
                                Intent intent=new Intent(Login.this,NumberActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(Login.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }else {
                    Toast.makeText(Login.this, "Enter the Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //----------Checking if Already Login--------------//
        if(mAuth.getCurrentUser()!=null){
            Intent intent=new Intent(Login.this,NumberActivity.class);
            startActivity(intent);
            finish();
        }
        //-----------Click To SignUp----------------//
        binding.txtClickSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,SignUp.class);
                startActivity(intent);
            }
        });

    }
}