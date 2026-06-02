package com.vzhukov.junior.project;import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.color.MaterialColors;
import java.util.ArrayList;
import java.util.List;

public class PMRActivity extends AppCompatActivity {

    private TextView statusText, hintText, timerText;
    private List<PmrStep> steps = new ArrayList<>();
    private int currentStepIndex = 0;
    private CountDownTimer currentTimer;
    private CountDownTimer preparationTimer;
    private Vibrator vibrator;
    private long startTime;


    private int materialAccentColor;

    private static class PmrStep {
        int[] muscleIds;
        String title;
        String instruction;
        int durationSeconds;

        PmrStep(int[] ids, String title, String instruction, int durationSeconds) {
            this.muscleIds = ids;
            this.title = title;
            this.instruction = instruction;
            this.durationSeconds = durationSeconds;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        DynamicColors.applyToActivityIfAvailable(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pmr);


        materialAccentColor = MaterialColors.getColor(
                this,
                androidx.appcompat.R.attr.colorPrimary,
                android.graphics.Color.BLUE
        );
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        startTime = System.currentTimeMillis();
        SessionManager.init(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        statusText = findViewById(R.id.statusText);
        hintText = findViewById(R.id.hint);
        timerText = findViewById(R.id.timerText);



        String mode = getIntent().getStringExtra("MODE");
        if (mode == null) mode = "FULL";

        setupSteps(mode);
        startPreparationCountDown();
    }

    private void startPreparationCountDown() {
        if (isFinishing()) return;

        statusText.setText("ПРИГОТОВЬТЕСЬ");
        hintText.setText("Тут будут подсказки");

        preparationTimer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millis) {
                int secondsLeft = (int) (millis / 1000);
                if (secondsLeft > 0) {
                    timerText.setText(String.valueOf(secondsLeft));
                }
            }

            @Override
            public void onFinish() {
                if (!isFinishing() && !isDestroyed()) {
                    startStep(0);
                }
            }
        }.start();
    }

    private void playHapticClick(int effectId) {
        boolean isVibEnabled = SettingsManager.getBoolean(SettingsManager.KEY_VIBRATION_ENABLED, true);
        if (isVibEnabled && vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                vibrator.vibrate(VibrationEffect.createPredefined(effectId));
            } else {
                vibrator.vibrate(20);
            }
        }
    }

    private void setupSteps(String mode) {
        if ("EXPRESS".equals(mode)) {
            steps.add(new PmrStep(new int[]{R.id.hForehead, R.id.hEyes, R.id.hMouth, R.id.hNose},
                    "ЛИЦО", "Зажмурьтесь и напрягите все мышцы лица", 10));
            steps.add(new PmrStep(null, "РАССЛАБЛЕНИЕ", "Почувствуйте, как лицо становится мягким", 20));

            steps.add(new PmrStep(new int[]{R.id.bNeck, R.id.bShoulders, R.id.aUpper, R.id.aHands, R.id.tAbdomen, R.id.tChest},
                    "КОРПУС И РУКИ", "Сожмите кулаки, напрягите пресс и плечи", 10));
            steps.add(new PmrStep(null, "РАССЛАБЛЕНИЕ", "Дышите глубоко, отпускайте напряжение", 20));

            steps.add(new PmrStep(new int[]{R.id.lButt, R.id.lThighs, R.id.lCalves, R.id.lFeet},
                    "НОГИ", "Сильно напрягите бедра, икры и стопы", 10));
            steps.add(new PmrStep(null, "ГОТОВО", "Ваше тело полностью расслаблено", 20));
        } else {
            addFullStep(R.id.hForehead, "ЛОБ", "Поднимите брови как можно выше", 7, 15);
            addFullStep(R.id.hEyes, "ГЛАЗА", "Очень сильно зажмурьтесь", 7, 15);
            addFullStep(R.id.hMouth, "ЧЕЛЮСТЬ", "Плотно сожмите губы и зубы", 7, 15);
            addFullStep(R.id.bNeck, "ШЕЯ", "Прижмите подбородок к груди", 7, 15);
            addFullStep(R.id.bShoulders, "ПЛЕЧИ", "Подтяните плечи к самым ушам", 7, 15);
            addFullStep(R.id.aUpper, "РУКИ", "Напрягите бицепсы", 7, 15);
            addFullStep(R.id.aHands, "КИСТИ", "Крепко сожмите кулаки", 7, 15);
            addFullStep(R.id.tChest, "ГРУДЬ", "Глубокий вдох и задержка дыхания", 7, 15);
            addFullStep(R.id.tAbdomen, "ЖИВОТ", "Сильно втяните и напрягите пресс", 7, 15);
            addFullStep(R.id.lButt, "ЯГОДИЦЫ", "Напрягите мышцы таза", 7, 15);
            addFullStep(R.id.lThighs, "БЕДРА", "Напрягите переднюю часть ног", 7, 15);
            addFullStep(R.id.lCalves, "ИКРЫ", "Тяните носки сильно на себя", 7, 15);
            addFullStep(R.id.lFeet, "СТОПЫ", "Подожмите пальцы ног внутрь", 7, 15);
        }
    }

    private void addFullStep(int muscleId, String title, String instruction, int work, int rest) {
        steps.add(new PmrStep(new int[]{muscleId}, title, instruction, work));
        steps.add(new PmrStep(null, "ОТДЫХ", "Расслабьтесь и выдохните...", rest));
    }

    private void startStep(int index) {
        if (isFinishing() || isDestroyed()) return;

        if (index >= steps.size()) {
            updateTextSmoothly(statusText, "ЗАВЕРШЕНО");
            updateTextSmoothly(hintText, "Вы отлично поработали над собой!");
            timerText.animate().alpha(0f).setDuration(500);
            hideAllMusclesSmoothly();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                playHapticClick(VibrationEffect.EFFECT_HEAVY_CLICK);
            }
            return;
        }

        PmrStep step = steps.get(index);


        if (step.muscleIds != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                playHapticClick(VibrationEffect.EFFECT_DOUBLE_CLICK);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                playHapticClick(VibrationEffect.EFFECT_TICK);
            }
        }

        updateTextSmoothly(statusText, step.title);
        updateTextSmoothly(hintText, step.instruction);

        hideAllMusclesSmoothly();
        if (step.muscleIds != null) {
            for (int id : step.muscleIds) {
                View muscle = findViewById(id);
                if (muscle instanceof ImageView) {
                    ImageView img = (ImageView) muscle;
                    img.setImageTintList(ColorStateList.valueOf(materialAccentColor));
                    img.setVisibility(View.VISIBLE);
                    img.setAlpha(0f);
                    img.animate().alpha(1f).setDuration(800).start();
                }
            }
        }

        if (currentTimer != null) currentTimer.cancel();
        currentTimer = new CountDownTimer(step.durationSeconds * 1000L, 1000) {
            @Override
            public void onTick(long millis) {
                timerText.setText(String.valueOf(millis / 1000 + 1));
            }
            @Override
            public void onFinish() {
                if (!isFinishing() && !isDestroyed()) {
                    currentStepIndex++;
                    startStep(currentStepIndex);
                }
            }
        }.start();
    }

    private void updateTextSmoothly(TextView textView, String newText) {
        if (textView == null) return;
        textView.animate().alpha(0f).setDuration(300).withEndAction(() -> {
            textView.setText(newText);
            textView.animate().alpha(1f).setDuration(300).start();
        }).start();
    }

    private void hideAllMusclesSmoothly() {
        int[] allIds = {R.id.hForehead, R.id.hEyes, R.id.hMouth, R.id.hNose, R.id.bNeck, R.id.bShoulders,
                R.id.aUpper, R.id.aHands, R.id.tAbdomen, R.id.tChest, R.id.lButt, R.id.lCalves,
                R.id.lFeet, R.id.lThighs};
        for (int id : allIds) {
            View v = findViewById(id);
            if (v != null && v.getVisibility() == View.VISIBLE) {
                v.animate().alpha(0f).setDuration(800).withEndAction(() -> v.setVisibility(View.INVISIBLE)).start();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (currentTimer != null) currentTimer.cancel();
        if (preparationTimer != null) preparationTimer.cancel();
        if (vibrator != null) vibrator.cancel();
        
        long duration = (System.currentTimeMillis() - startTime) / 1000;
        boolean isSos = getIntent().getBooleanExtra("SOS_MODE", false);
        SessionManager.addSession("Релаксация ПМР", duration, isSos);

        super.onDestroy();
    }
}
