package com.example.haider.resoluteaipeertopeercalling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.haider.resoluteaipeertopeercalling.databinding.ActivityNumberBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class NumberActivity extends AppCompatActivity {
    ActivityNumberBinding binding;
    String uID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //----------getting the User Id--------//
        uID=getIntent().getStringExtra("uName");

        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String n = binding.txtNumber.getText() + "";

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                if (n.length() < 10) {
                    Toast.makeText(NumberActivity.this, "Enter a Valid Number", Toast.LENGTH_SHORT).show();
                } else {
                    binding.progressSendingOtp.setVisibility(View.VISIBLE);
                    binding.btnContinue.setVisibility(View.INVISIBLE);

                    String phoneNumber = "+91" + n;
                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(mAuth)
                                    .setPhoneNumber(phoneNumber)
                                    .setTimeout(60L, TimeUnit.SECONDS)
                                    .setActivity(NumberActivity.this)
                                    .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                        @Override
                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                                            binding.progressSendingOtp.setVisibility(View.GONE);
                                            binding.btnContinue.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onVerificationFailed(@NonNull FirebaseException e) {
                                            binding.progressSendingOtp.setVisibility(View.GONE);
                                            binding.btnContinue.setVisibility(View.VISIBLE);
                                            Toast.makeText(NumberActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            Log.d("My err",e.getMessage());

                                        }

                                        @Override
                                        public void onCodeSent(@NonNull String OTP,
                                                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                            binding.progressSendingOtp.setVisibility(View.GONE);
                                            binding.btnContinue.setVisibility(View.VISIBLE);
                                            Intent intent = new Intent(NumberActivity.this, OtpActivity.class);
                                            intent.putExtra("FirstUser",uID);//user id using the email.
                                            intent.putExtra("number", n);
                                            intent.putExtra("OTP_AUTH",OTP);
                                            startActivity(intent);
                                            finish();
                                        }
                                    })
                                    .build();

                    PhoneAuthProvider.verifyPhoneNumber(options);
                }
            }
        });
    }
}