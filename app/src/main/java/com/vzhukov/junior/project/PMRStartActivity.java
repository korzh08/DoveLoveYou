package com.vzhukov.junior.project;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.color.DynamicColors;

public class PMRStartActivity extends AppCompatActivity {
    MaterialCardView btnExpress, btnFull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DynamicColors.applyToActivityIfAvailable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmr_start);

        btnExpress = findViewById(R.id.btnExpress);
        btnFull = findViewById(R.id.btnFull);

        btnExpress.setOnClickListener(v -> {
            Intent intent = new Intent(PMRStartActivity.this, PMRActivity.class);
            intent.putExtra("MODE", "EXPRESS");
            startActivity(intent);
        });

        btnFull.setOnClickListener(v -> {
            Intent intent = new Intent(PMRStartActivity.this, PMRActivity.class);
            intent.putExtra("MODE", "FULL");
            startActivity(intent);
        });
    }
}