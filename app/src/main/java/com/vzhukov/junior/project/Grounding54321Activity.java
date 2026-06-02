package com.vzhukov.junior.project;import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.color.DynamicColors;

public class Grounding54321Activity extends AppCompatActivity {

    private TextView tvTitle, tvHint, tvCounter;
    private EditText etInput;

    private MaterialCardView btnNext;

    private View flashView;
    private Vibrator vibrator;
    private long startTime;

    private int currentMainStep = 5;
    private int currentSubStep = 1;

    private final String[] titles = {"", "ВКУС", "ЗАПАХА", "ЗВУКА", "ВЕЩИ", "ПРЕДМЕТОВ"};
    private final String[] hints = {"",
            "Который вы чувствуете или помните",
            "Которые вы чувствуете прямо сейчас или можете представить",
            "Которые вы сейчас слышите",
            "Которые вы можете потрогать, ощутить кожей",
            "Которые вы видите прямо сейчас"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DynamicColors.applyToActivityIfAvailable(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grounding54321);

        startTime = System.currentTimeMillis();
        SessionManager.init(this);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        tvTitle = findViewById(R.id.tvStepTitle);
        tvHint = findViewById(R.id.tvStepHint);
        tvCounter = findViewById(R.id.tvCounter);
        etInput = findViewById(R.id.etInput);

        btnNext = findViewById(R.id.btnNext);

        flashView = findViewById(R.id.flashView);

        updateUI();

        btnNext.setOnClickListener(v -> handleNext());
    }

    private void handleNext() {
        String input = etInput.getText().toString().trim();
        if (input.isEmpty()) return;

        playTick();
        triggerFlash();

        etInput.setText("");

        if (currentSubStep < currentMainStep) {
            currentSubStep++;
        } else {
            currentMainStep--;
            currentSubStep = 1;
        }

        if (currentMainStep > 0) {
            updateUI();
        } else {
            finishSession();
        }
    }

    private void updateUI() {
        tvTitle.setText(currentMainStep + " " + titles[currentMainStep]);
        tvHint.setText(hints[currentMainStep]);
        tvCounter.setText(currentSubStep + " / " + currentMainStep);
        etInput.setHint("Запишите это...");
    }

    private void triggerFlash() {
        flashView.setVisibility(View.VISIBLE);
        flashView.setAlpha(0.12f);

        flashView.animate()
                .alpha(0f)
                .setDuration(800)
                .withEndAction(() -> flashView.setVisibility(View.GONE))
                .start();
    }

    private void finishSession() {
        tvTitle.setText("ГОТОВО");
        tvHint.setText("Вы сосредоточились на реальности.\nТревога отступает.");
        etInput.setVisibility(View.GONE);
        tvCounter.setVisibility(View.GONE);

        TextView btnText = (TextView) btnNext.getChildAt(0);
        btnText.setText("ЗАКОНЧИТЬ");

        btnNext.setOnClickListener(v -> finish());

        if (SettingsManager.getBoolean(SettingsManager.KEY_VIBRATION_ENABLED, true)) {
            if (vibrator != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK));
            }
        }
    }

    private void playTick() {
        boolean isVibEnabled = SettingsManager.getBoolean(SettingsManager.KEY_VIBRATION_ENABLED, true);

        if (isVibEnabled && vibrator != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK));
        }
    }

    @Override
    protected void onDestroy() {
        long duration = (System.currentTimeMillis() - startTime) / 1000;
        boolean isSos = getIntent().getBooleanExtra("SOS_MODE", false);
        SessionManager.addSession("Заземление 5-4-3-2-1", duration, isSos);
        super.onDestroy();
    }
}
