package com.example.haider.resoluteaipeertopeercalling.Models;

import android.webkit.JavascriptInterface;

import com.example.haider.resoluteaipeertopeercalling.CallConnectedActivity;

public class InterfaceJava {
    CallConnectedActivity callConnectedActivity;
    public InterfaceJava(CallConnectedActivity callConnectedActivity){
        this.callConnectedActivity=callConnectedActivity;
    }
    @JavascriptInterface
    public void onPeerConnected(){
        callConnectedActivity.onPeerConnected();
    }
}
