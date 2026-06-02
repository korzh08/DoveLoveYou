package com.vzhukov.junior.project;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EMDRActivity extends AppCompatActivity {

    private View emdrDot;
    private int speed;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_emdractivity);

        startTime = System.currentTimeMillis();
        SessionManager.init(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emdrDot = findViewById(R.id.emdrDot);
        findViewById(R.id.btnClose).setOnClickListener(v -> finish());


        speed = getIntent().getIntExtra("SPEED", 1000);

        emdrDot.post(this::startEmdrAnimation);
    }

    private void startEmdrAnimation() {
        float screenWidth = getResources().getDisplayMetrics().widthPixels;

        float moveDistance = screenWidth - emdrDot.getWidth();

        emdrDot.animate()
                .translationX(moveDistance)
                .setDuration(speed)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .withEndAction(() -> {
                    emdrDot.animate()
                            .translationX(0)
                            .setDuration(speed)
                            .setInterpolator(new AccelerateDecelerateInterpolator())
                            .withEndAction(this::startEmdrAnimation)
                            .start();
                })
                .start();
    }

    @Override
    protected void onDestroy() {
        long duration = (System.currentTimeMillis() - startTime) / 1000;
        boolean isSos = getIntent().getBooleanExtra("SOS_MODE", false);
        SessionManager.addSession("ДПДГ (EMDR)", duration, isSos);

        if (emdrDot != null) {
            emdrDot.animate().cancel();
        }
        super.onDestroy();
    }
}
