package com.example.haider.resoluteaipeertopeercalling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.haider.resoluteaipeertopeercalling.Models.InterfaceJava;
import com.example.haider.resoluteaipeertopeercalling.databinding.ActivityCallConnectedBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class CallConnectedActivity extends AppCompatActivity {
    ActivityCallConnectedBinding binding;
    String uniqueId="";
    FirebaseAuth mAuth;

    String userName="";
    String senderUserName="";

    boolean isPeerConnected=false;

    DatabaseReference reference;

    boolean isAudio=true;
    boolean isVideo=true;
    String createdBy;
    boolean pageExit=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCallConnectedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //---initializing firebase reference----//
        mAuth=FirebaseAuth.getInstance();
        //---initializing database reference----//
        reference= FirebaseDatabase.getInstance().getReference().child("Rooms");

        userName=getIntent().getStringExtra("username");
        String incoming=getIntent().getStringExtra("incoming");
        createdBy=getIntent().getStringExtra("createdBy");


            senderUserName=incoming;
        setupWebView();
        //-------mic button toggle------//
        binding.imgMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAudio=!isAudio;
                callJavaScriptFunc("javascript:toggleAudio(\""+isAudio+"\")");
                if(isAudio){
                    binding.imgMic.setImageResource(R.drawable.mic_blue);
                }else{
                    binding.imgMic.setImageResource(R.drawable.mic_off);
                }

            }
        });

        binding.imgVideoCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVideo=!isVideo;
                callJavaScriptFunc("javascript:toggleVideo(\""+isVideo+"\")");
                if(isVideo){
                    binding.imgVideoCamera.setImageResource(R.drawable.vdeo_camb_lue);
                }else{
                    binding.imgVideoCamera.setImageResource(R.drawable.camera_off);
                }

            }
        });

        binding.imgCallEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    @SuppressLint("SetJavaScriptEnabled")
    void setupWebView(){
        binding.webCall.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    request.grant(request.getResources());
                }
            }
        });

        binding.webCall.getSettings().setJavaScriptEnabled(true);
        binding.webCall.getSettings().setMediaPlaybackRequiresUserGesture(false);
        binding.webCall.addJavascriptInterface(new InterfaceJava(this), "Android");

        //Loading the Video Call
        loadVideoCall();

    }

    private void loadVideoCall() {
        String filePath = "file:android_asset/call.html";
        binding.webCall.loadUrl(filePath);

        binding.webCall.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                //Initializing peer
                initialPeer();
            }
        });
    }

    private void initialPeer() {
        uniqueId=getUniqueId();
        callJavaScriptFunc("javascript:init(\"" + uniqueId + "\")");

        if(createdBy.equalsIgnoreCase(userName)){
//            if(pageExit)
//                return;
//
            reference.child(userName).child("connId").setValue(uniqueId);
            reference.child(userName).child("isAvailable").setValue(true);
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    senderUserName = createdBy;
                    FirebaseDatabase.getInstance().getReference()
                            .child("Rooms")
                            .child(senderUserName)
                            .child("connId")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.getValue()!=null){
                                        //Sending the call request
                                        sendCallReq();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
            },3000);
        }
    }
    public void onPeerConnected(){
        isPeerConnected=true;
    }
    void sendCallReq(){
        if(!isPeerConnected) {
            Toast.makeText(this, "Not Connected Please Check Internet..", Toast.LENGTH_SHORT).show();
            return;
        }
        //listeningConnection Id
        listenConnId();

    }
    void listenConnId(){
        reference.child(senderUserName).child("connId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()==null) return;

                String connId = snapshot.getValue(String.class);
                callJavaScriptFunc("javascript:startCall(\""+connId+"\")");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    void callJavaScriptFunc(String function){
        binding.webCall.post(new Runnable() {
            @Override
            public void run() {
                binding.webCall.evaluateJavascript(function,null);
            }
        });
    }
    String getUniqueId(){
        return UUID.randomUUID().toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pageExit = true;
        reference.child(createdBy).setValue(null);
        finish();
    }

}
