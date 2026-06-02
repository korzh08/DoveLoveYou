package com.vzhukov.junior.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.google.android.material.card.MaterialCardView;
import com.google.android.material.color.DynamicColors;

public class EMDRStartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        DynamicColors.applyToActivityIfAvailable(this);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_emdrstart);

        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        MaterialCardView btnSlow = findViewById(R.id.btnSlow);
        MaterialCardView btnMedium = findViewById(R.id.btnMedium);
        MaterialCardView btnFast = findViewById(R.id.btnFast);

        final int SLOW = 3500;
        final int MEDIUM = 2200;
        final int FAST = 1200;

        if (btnSlow != null) {
            btnSlow.setOnClickListener(v -> startTraining(SLOW));
        }
        if (btnMedium != null) {
            btnMedium.setOnClickListener(v -> startTraining(MEDIUM));
        }
        if (btnFast != null) {
            btnFast.setOnClickListener(v -> startTraining(FAST));
        }
    }

    private void startTraining(int speed) {
        Intent intent = new Intent(this, EMDRActivity.class);
        intent.putExtra("SPEED", speed);
        intent.putExtra("SOS_MODE", getIntent().getBooleanExtra("SOS_MODE", false));
        startActivity(intent);
    }
}