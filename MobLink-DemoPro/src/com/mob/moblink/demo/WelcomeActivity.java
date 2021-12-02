package com.mob.moblink.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.mob.moblink.Scene;
import com.mob.moblink.SceneRestorable;
import com.mob.moblink.demo.splash.SplashActivity;

public class WelcomeActivity extends Activity implements SceneRestorable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    @Override
    public void onReturnSceneData(Scene scene) {
        if (scene != null && scene.params != null) {
            final String url = (String) scene.params.get("startPage");
            Intent intent_1 = new Intent(WelcomeActivity.this, SplashActivity.class);
            startActivity(intent_1);

            Intent intent = new Intent(WelcomeActivity.this, BrowserActivity.class);
            intent.putExtra("url", url);
            startActivity(intent);

            finish();
        }
    }
}
