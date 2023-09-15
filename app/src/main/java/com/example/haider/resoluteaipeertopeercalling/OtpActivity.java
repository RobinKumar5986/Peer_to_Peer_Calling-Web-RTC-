package com.example.haider.resoluteaipeertopeercalling;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.haider.resoluteaipeertopeercalling.databinding.ActivityOtpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {
    TextView txtNum;
    Intent intent;
    ActivityOtpBinding binding;
    String otp;
    String otp_auth;
    String num;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        txtNum = findViewById(R.id.getNum);
        intent = getIntent();
        //------Phone Number-------//
        num = intent.getStringExtra("number");
        txtNum.setText(txtNum.getText() + num);

        //----OTP From Firebase database-----//
        otp_auth = intent.getStringExtra("OTP_AUTH");

        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp = binding.op1.getText().toString() + binding.op2.getText().toString() +
                        binding.op3.getText().toString() + binding.op4.getText().toString() +
                        binding.op5.getText().toString() + binding.op6.getText().toString();
                if (otp.length() != 6) {
                    Toast.makeText(OtpActivity.this, "Enter Complete OTP", Toast.LENGTH_SHORT).show();
                } else {
                    binding.progressVerifyOtp.setVisibility(View.VISIBLE);
                    binding.btnVerify.setVisibility(View.INVISIBLE);
                    //-------Comparing The OTP-------//
                    PhoneAuthCredential phoneAuthCredential= PhoneAuthProvider.getCredential(
                            otp_auth,otp
                    );

                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    binding.progressVerifyOtp.setVisibility(View.GONE);
                                    binding.btnVerify.setVisibility(View.VISIBLE);

                                    if(task.isSuccessful()){
                                        Intent intent =new Intent(OtpActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        finishAffinity();
                                        Toast.makeText(OtpActivity.this, "Number Verified Successfully", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(OtpActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        numberOtpMove();
        binding.txtRequestAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + num, 40,
                        TimeUnit.SECONDS,
                        OtpActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String newOTP, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                //NOTE : -                   this 's' is the OPT
                                otp_auth=newOTP;
                                Toast.makeText(OtpActivity.this, "Requested Again", Toast.LENGTH_SHORT).show();
                            }
                        }
                );

            }
        });

        //---------back arrow button----//
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),NumberActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void numberOtpMove() {
        binding.op1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    binding.op2.requestFocus();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.op2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    binding.op3.requestFocus();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.op3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    binding.op4.requestFocus();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.op4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    binding.op5.requestFocus();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.op5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    binding.op6.requestFocus();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}