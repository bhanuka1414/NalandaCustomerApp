package com.bp.nalandacustomerapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {
    private TextView title;
    private TextView sub;
    private static int welcomeTimeOut = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        title=(TextView)findViewById(R.id.tit);
        sub = (TextView)findViewById(R.id.subtopic);

        Animation welcomeanim = AnimationUtils.loadAnimation(this,R.anim.mytra);
        title.startAnimation(welcomeanim);
        sub.startAnimation(welcomeanim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeavt = new Intent(WelcomeActivity.this,HomeActivity.class);
                startActivity(homeavt);
                finish();
            }
        },welcomeTimeOut);
    }
}
