package com.vzhukov.junior.project;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.color.DynamicColors;
import android.widget.RadioGroup;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DynamicColors.applyToActivityIfAvailable(this);
        super.onCreate(savedInstanceState);

        SettingsManager.init(this);

        setContentView(R.layout.activity_welcome);

        RadioGroup rgMethods = findViewById(R.id.rgSosMethods);
        MaterialCardView btnNext = findViewById(R.id.btnNext);

        btnNext.setOnClickListener(v -> {
            int selectedId = rgMethods.getCheckedRadioButtonId();

            String methodToSave = "breathe";

            if (selectedId == R.id.rbPmr) {
                methodToSave = "pmr";
            } else if (selectedId == R.id.rbGrounding) {
                methodToSave = "grounding";
            } else if (selectedId == R.id.rbBreathe) {
                methodToSave = "breathe";
            }

            SettingsManager.setString("fav_method_string", methodToSave);

            SettingsManager.setBoolean(SettingsManager.KEY_FIRST_LAUNCH, false);

            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        });
    }
}