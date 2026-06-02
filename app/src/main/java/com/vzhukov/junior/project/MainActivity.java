package com.vzhukov.junior.project;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.color.DynamicColors;

public class MainActivity extends AppCompatActivity {

    private MaterialCardView goEMDR, goGrounding, goPMR, goBreathe, btnPriorityAction, goDescribe;
    private ImageButton btnSettings, btnHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DynamicColors.applyToActivityIfAvailable(this);

        super.onCreate(savedInstanceState);


        SettingsManager.init(this);
        SessionManager.init(this);
        boolean isFirstLaunch = SettingsManager.getBoolean(SettingsManager.KEY_FIRST_LAUNCH, true);

        if (isFirstLaunch) {

            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);

            finish();
            return;
        }

        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnSettings = findViewById(R.id.btnSettings);
        btnHistory = findViewById(R.id.btnHistory);
        goEMDR = findViewById(R.id.goEMDR);
        goGrounding = findViewById(R.id.goGrounding);
        goPMR = findViewById(R.id.goPMR);
        goBreathe = findViewById(R.id.goBreathe);
        goDescribe = findViewById(R.id.goDescribe);
        btnPriorityAction = findViewById(R.id.btnPriorityAction);


        btnSettings.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            applyFadeTransition();
        });

        btnHistory.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            applyFadeTransition();
        });

        btnPriorityAction.setOnClickListener(v -> {

            String favoriteMethod = SettingsManager.getString(SettingsManager.KEY_FAVORITE_METHOD_STR, "breathe");

            Intent intent;


            if (favoriteMethod.equals("pmr_full")) {
                intent = new Intent(this, PMRActivity.class);
                intent.putExtra("MODE", "FULL");
                intent.putExtra("SOS_MODE", true);
            } else if (favoriteMethod.equals("pmr_express")) {
                intent = new Intent(this, PMRActivity.class);
                intent.putExtra("MODE", "EXPRESS");
                intent.putExtra("SOS_MODE", true);
            } else if (favoriteMethod.equals("grounding")) {
                intent = new Intent(this, Grounding54321Activity.class);
                intent.putExtra("SOS_MODE", true);
            } else if (favoriteMethod.equals("describe")) {
                intent = new Intent(this, DescribeObjectStartActivity.class);
                intent.putExtra("SOS_MODE", true);
            } else {

                intent = new Intent(this, BoxBreathingActivity.class);

                intent.putExtra("SOS_MODE", true);
            }

            startActivity(intent);
            applyFadeTransition();


            if (SettingsManager.getBoolean(SettingsManager.KEY_VIBRATION_ENABLED, true)) {
                Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (vib != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        vib.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK));
                    } else {
                        vib.vibrate(100);
                    }
                }
            }
        });


        goEMDR.setOnClickListener(v -> {
            startActivity(new Intent(this, EMDRStartActivity.class));
            applyFadeTransition();
        });

        goGrounding.setOnClickListener(v -> {
            startActivity(new Intent(this, Grounding54321Activity.class));
            applyFadeTransition();
        });

        goPMR.setOnClickListener(v -> {
            startActivity(new Intent(this, PMRStartActivity.class));
            applyFadeTransition();
        });

        goBreathe.setOnClickListener(v -> {
            startActivity(new Intent(this, BoxBreathingActivity.class));
            applyFadeTransition();
        });

        goDescribe.setOnClickListener(v -> {
            startActivity(new Intent(this, DescribeObjectStartActivity.class));
            applyFadeTransition();
        });

        animateEntrance();
    }

    private void applyFadeTransition() {
        if (Build.VERSION.SDK_INT >= 34) {
            overrideActivityTransition(OVERRIDE_TRANSITION_OPEN, R.anim.activity_fade_in, R.anim.activity_fade_out);
        } else {
            overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
        }
    }

    private void animateEntrance() {
        android.view.View[] views = {goEMDR, goDescribe, goGrounding, goPMR, goBreathe, btnPriorityAction};
        for (int i = 0; i < views.length; i++) {
            android.view.View v = views[i];
            if (v == null) continue;
            v.setAlpha(0f);
            v.animate()
                    .alpha(1f)
                    .setDuration(700)
                    .setStartDelay(100 + i * 100L)
                    .setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator())
                    .start();
        }
    }
}