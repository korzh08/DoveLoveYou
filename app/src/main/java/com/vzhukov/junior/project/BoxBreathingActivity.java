package com.vzhukov.junior.project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BoxBreathingActivity extends AppCompatActivity {
    private Button startBrt;
    private View movDot;

    private View statRect;
private void startDotMove(int speed){
    float dist = getResources().getDimension(R.dimen.move_dot_distance);
    movDot.animate().translationX(dist).setDuration(speed).withEndAction(() -> {
        movDot.animate().translationY(dist).setDuration(speed).withEndAction(() -> {
            movDot.animate().translationX(0).setDuration(speed).withEndAction(() ->{
                movDot.animate().translationY(0).setDuration(speed).withEndAction(() -> startDotMove(speed)).start();
                    }).start();
                }).start();
            }).start();
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_box_breathing);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        startBrt = findViewById(R.id.startBut);
        movDot = findViewById(R.id.movDot);
        statRect = findViewById(R.id.statSquare);

        startBrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movDot.setVisibility(View.VISIBLE);
                movDot.setAlpha(0f);
                movDot.animate()
                        .alpha(1f)
                        .setDuration(500)
                        .start();
                statRect.setVisibility(View.VISIBLE);
                statRect.setAlpha(0f);
                statRect.animate()
                        .alpha(1f)
                        .setDuration(500)
                        .start();
                startBrt.animate()
                        .alpha(0f)
                        .setDuration(500)
                        .withEndAction(() -> startBrt.setVisibility(View.GONE))
                        .start();
                startDotMove(4000);
            }
        });
    }
}