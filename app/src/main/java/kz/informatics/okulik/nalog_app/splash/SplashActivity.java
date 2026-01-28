package kz.informatics.okulik.nalog_app.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import kz.informatics.okulik.R;
import kz.informatics.okulik.nalog_app.onboarding.Onboarding;

public class SplashActivity extends AppCompatActivity {

    Animation animation;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        relativeLayout = findViewById(R.id.relativeLayout);

        animation = AnimationUtils.loadAnimation(this, R.anim.splash_anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, Onboarding.class));
                finish();
            }
        }, 2500);
    }
}