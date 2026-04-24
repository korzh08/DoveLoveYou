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

    private MaterialCardView goEMDR, goGrounding, goPMR, goBreathe, btnPriorityAction;
    private ImageButton btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DynamicColors.applyToActivityIfAvailable(this);

        super.onCreate(savedInstanceState);


        SettingsManager.init(this);
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
        goEMDR = findViewById(R.id.goEMDR);
        goGrounding = findViewById(R.id.goGrounding);
        goPMR = findViewById(R.id.goPMR);
        goBreathe = findViewById(R.id.goBreathe);
        btnPriorityAction = findViewById(R.id.btnPriorityAction);


        btnSettings.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        });

        btnPriorityAction.setOnClickListener(v -> {

            String favoriteMethod = SettingsManager.getString(SettingsManager.KEY_FAVORITE_METHOD_STR, "breathe");

            Intent intent;


            if (favoriteMethod.equals("pmr")) {
                intent = new Intent(this, PMRStartActivity.class);
            } else if (favoriteMethod.equals("grounding")) {
                intent = new Intent(this, Grounding54321Activity.class);
            } else {

                intent = new Intent(this, BoxBreathingActivity.class);

                intent.putExtra("SOS_MODE", true);
            }

            startActivity(intent);


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


        goEMDR.setOnClickListener(v ->
                startActivity(new Intent(this, EMDRStartActivity.class)));

        goGrounding.setOnClickListener(v ->
                startActivity(new Intent(this, Grounding54321Activity.class)));

        goPMR.setOnClickListener(v ->
                startActivity(new Intent(this, PMRStartActivity.class)));

        goBreathe.setOnClickListener(v ->
                startActivity(new Intent(this, BoxBreathingActivity.class)));
    }
}