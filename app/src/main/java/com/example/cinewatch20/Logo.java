package com.example.cinewatch20;

import static android.os.Build.VERSION_CODES.LOLLIPOP;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Logo extends AppCompatActivity {

    //variables
    Animation topAnim, bottomAnim;
    View image;
    View title;
    TextView slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.open_logo);


        //Animations
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //hooks
        image = findViewById(R.id.camera);
        title= findViewById(R.id.opening_title);
        slogan= findViewById(R.id.subtitle);

        image.setAnimation(topAnim);
        title.setAnimation(bottomAnim);
        slogan.setAnimation(bottomAnim);

        int SPLASH_SCREEN = 5000;
        new Handler().postDelayed(this::run, SPLASH_SCREEN);
    } //end onCreate

    private void run() {
        Intent intent = new Intent(Logo.this, LoginScreen.class);


        Pair[] pairs = new Pair[2];
        pairs[0] = new Pair<View, String>(image, "logo_image");
        pairs[1] = new Pair<View, String>(title, "logo_text");

        if (Build.VERSION.SDK_INT >= LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Logo.this, pairs);
            startActivity(intent, options.toBundle());
        }

    }
}
