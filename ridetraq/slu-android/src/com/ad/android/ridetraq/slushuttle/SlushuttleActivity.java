package com.ad.android.ridetraq.slushuttle;

import org.apache.cordova.DroidGap;




import android.os.Bundle;
import android.webkit.WebSettings;

public class SlushuttleActivity extends DroidGap {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        super.setIntegerProperty("loadUrlTimeoutValue", 60000);
        super.setIntegerProperty("splashscreen", R.drawable.splash);        
        super.loadUrl("file:///android_asset/www/index.html", 5000);
        WebSettings ws = super.appView.getSettings();
        ws.setSupportZoom(true);
        ws.setBuiltInZoomControls(true);
        ws.setJavaScriptEnabled(true);
        
        
    }
}