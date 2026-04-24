package com.vzhukov.junior.project;

import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EMDRActivity extends AppCompatActivity {

    private View emdrDot;
    private int speed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_emdractivity);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emdrDot = findViewById(R.id.emdrDot);


        speed = getIntent().getIntExtra("SPEED", 1000);

        emdrDot.post(this::startEmdrAnimation);
    }

    private void startEmdrAnimation() {
        float screenWidth = getResources().getDisplayMetrics().widthPixels;

        float moveDistance = screenWidth - emdrDot.getWidth();

        emdrDot.animate()
                .translationX(moveDistance)
                .setDuration(speed)
                .setInterpolator(new LinearInterpolator())
                .withEndAction(() -> {
                    emdrDot.animate()
                            .translationX(0)
                            .setDuration(speed)
                            .setInterpolator(new LinearInterpolator())
                            .withEndAction(this::startEmdrAnimation)
                            .start();
                })
                .start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (emdrDot != null) {
            emdrDot.animate().cancel();
        }
    }
}