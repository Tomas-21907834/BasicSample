package com.example.android.testing.espresso.BasicSample;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;


import android.util.Log;


public class BatteryDrainTestActivity extends AppCompatActivity {

    private WebView webViewYoutubePlayer;
    private BroadcastReceiver batteryLevelReceiver;

    @SuppressLint({"MissingInflatedId", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webViewYoutubePlayer = findViewById(R.id.webview_youtube_player);


        WebSettings webSettings = webViewYoutubePlayer.getSettings();
        webSettings.setJavaScriptEnabled(true);


        webViewYoutubePlayer.setWebChromeClient(new WebChromeClient());


        String frameVideo = "<html><body><iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/dQw4w9WgXcQ\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
        webViewYoutubePlayer.loadDataWithBaseURL(null, frameVideo, "text/html", "utf-8", null);



        batteryLevelReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                float batteryPct = level * 100 / (float)scale;

                // Log the battery level
                Log.d("BatteryTest", "Current battery level: " + batteryPct + "%");
            }
        };


        registerReceiver(batteryLevelReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(batteryLevelReceiver);
        webViewYoutubePlayer.destroy();
    }
}
