package com.vzhukov.junior.project;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.color.DynamicColors;

public class BoxBreathingActivity extends AppCompatActivity {
    private MaterialCardView startBut;
    private View movDot, statRect, prepareLayout;
    private TextView phaseText, brtCounter;
    private Vibrator vibrator;

    private final String[] phases = {"ВДОХ", "ЗАДЕРЖКА", "ВЫДОХ", "ЗАДЕРЖКА"};
    private int currentPhaseIndex = 0;
    private Handler breathingHandler = new Handler(Looper.getMainLooper());
    private boolean isDestroyed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DynamicColors.applyToActivityIfAvailable(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_box_breathing);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        startBut = findViewById(R.id.startBut);
        movDot = findViewById(R.id.movDot);
        statRect = findViewById(R.id.statSquare);
        brtCounter = findViewById(R.id.brtCounter);
        phaseText = findViewById(R.id.phaseText);
        prepareLayout = findViewById(R.id.prepareLayout);

        boolean isSos = getIntent().getBooleanExtra("SOS_MODE", false);
        if (isSos) {
            startBreathingImmediately();
        } else {
            startBut.setOnClickListener(v -> startBreathingImmediately());
        }
    }

    private void startBreathingImmediately() {
        movDot.setVisibility(View.VISIBLE);
        statRect.setVisibility(View.VISIBLE);
        phaseText.setVisibility(View.VISIBLE);
        brtCounter.setVisibility(View.VISIBLE);

        prepareLayout.animate().alpha(0f).setDuration(500).withEndAction(() -> {
            prepareLayout.setVisibility(View.GONE);
            startBreathingStep();
        }).start();
    }

    private void startBreathingStep() {
        if (isDestroyed || isFinishing()) return;
        triggerCornerVibration();

        phaseText.animate().alpha(0f).setDuration(300).withEndAction(() -> {
            if (!isDestroyed) {
                phaseText.setText(phases[currentPhaseIndex]);
                phaseText.animate().alpha(1f).setDuration(300).start();
            }
        }).start();

        float side = statRect.getWidth();
        float offset = 12 * getResources().getDisplayMetrics().density;
        int speed = 4000;

        switch (currentPhaseIndex) {
            case 0:
                movDot.animate()
                        .translationX(side - offset)
                        .translationY(-offset)
                        .setInterpolator(new LinearInterpolator())
                        .setDuration(speed).start();
                break;
            case 1:
                movDot.animate()
                        .translationX(side - offset)
                        .translationY(side - offset)
                        .setInterpolator(new LinearInterpolator())
                        .setDuration(speed).start();
                break;
            case 2:
                movDot.animate()
                        .translationX(-offset)
                        .translationY(side - offset)
                        .setInterpolator(new LinearInterpolator())
                        .setDuration(speed).start();
                break;
            case 3:
                movDot.animate()
                        .translationX(-offset)
                        .translationY(-offset)
                        .setInterpolator(new LinearInterpolator())
                        .setDuration(speed).start();
                break;
        }

        runCountdown(1);

        breathingHandler.postDelayed(() -> {
            if (!isDestroyed) {
                currentPhaseIndex = (currentPhaseIndex + 1) % 4;
                startBreathingStep();
            }
        }, speed);
    }

    private void triggerCornerVibration() {
        boolean isVibEnabled = SettingsManager.getBoolean(SettingsManager.KEY_VIBRATION_ENABLED, true);

        if (isVibEnabled && vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK));
            } else {
                vibrator.vibrate(20);
            }
        }
    }

    private void runCountdown(int second) {
        if (isDestroyed) return;
        brtCounter.setText(String.valueOf(second));
        if (second < 4) {
            breathingHandler.postDelayed(() -> runCountdown(second + 1), 1000);
        }
    }

    @Override
    protected void onDestroy() {
        isDestroyed = true;
        breathingHandler.removeCallbacksAndMessages(null);
        if (vibrator != null) {
            vibrator.cancel();
        }
        super.onDestroy();
    }
}