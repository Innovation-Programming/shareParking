package com.example.parking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.skt.Tmap.TMapTapi;
import com.skt.Tmap.TMapView;

import java.util.HashMap;

public class Navi extends AppCompatActivity {




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Log.d("navi","hello");

        final TMapTapi tMapTapi = new TMapTapi(this);
        tMapTapi.setSKTMapAuthentication("l7xx822806fc23c04ea6b159ede271a93d10");

        SharedPreferences sharedPreferences = getSharedPreferences("sFile",MODE_PRIVATE);
//
        final String rGoY = sharedPreferences.getString("latitude", "user");
        final String rGoX = sharedPreferences.getString("longitude", "user");
        final String rGoName = sharedPreferences.getString("parkingName", "user");


        tMapTapi.setOnAuthenticationListener(new TMapTapi.OnAuthenticationListenerCallback() {
            @Override
            public void SKTMapApikeySucceed() {
                Log.d("navi","성공");
                HashMap pathInfo = new HashMap();
                pathInfo.put("rGoName", rGoName);
                pathInfo.put("rGoX", rGoX);
                pathInfo.put("rGoY", rGoY);

                tMapTapi.invokeRoute(pathInfo);

            }

            @Override
            public void SKTMapApikeyFailed(String s) {
                Log.d("navi","실패");
                Log.d("navi", s);
            }

        });

    }
}

