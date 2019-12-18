package com.muviteam.muviapp.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.muviteam.muviapp.R;

public class SplashActivity extends AppCompatActivity {

    private VideoView myVideoViewVideoSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

       new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }, 2000);

        myVideoViewVideoSplash = findViewById(R.id.SplashActivity_VideoView_VideoSplash);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.muvi;
        Uri uri = Uri.parse(videoPath);
        myVideoViewVideoSplash.setVideoURI(uri);
        myVideoViewVideoSplash.requestFocus();
        myVideoViewVideoSplash.start();
    }
}
