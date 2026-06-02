package com.vzhukov.junior.project;import android.os.Bundle;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.materialswitch.MaterialSwitch;

public class SettingsActivity extends AppCompatActivity {

    private MaterialSwitch switchVibration;
    private RadioGroup rgGrounding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DynamicColors.applyToActivityIfAvailable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchVibration = findViewById(R.id.switchVibration);
        rgGrounding = findViewById(R.id.rgGrounding);

        
        boolean isVibrationEnabled = SettingsManager.getBoolean(SettingsManager.KEY_VIBRATION_ENABLED, true);
        String savedMethod = SettingsManager.getString(SettingsManager.KEY_FAVORITE_METHOD_STR, "breathe");

        
        switchVibration.setChecked(isVibrationEnabled);

        int idToSelect;
        switch (savedMethod) {
            case "pmr_full":
                idToSelect = R.id.rbPMRFull;
                break;
            case "pmr_express":
                idToSelect = R.id.rbPMRExpress;
                break;
            case "grounding":
                idToSelect = R.id.rb54321;
                break;
            case "describe":
                idToSelect = R.id.rbDescribe;
                break;
            default:
                idToSelect = R.id.rbBreathe;
                break;
        }
        rgGrounding.check(idToSelect);

        

        switchVibration.setOnCheckedChangeListener((view, isChecked) ->
                SettingsManager.setBoolean(SettingsManager.KEY_VIBRATION_ENABLED, isChecked)
        );

        rgGrounding.setOnCheckedChangeListener((group, checkedId) -> {
            String methodToSave;

            
            if (checkedId == R.id.rbPMRFull) {
                methodToSave = "pmr_full";
            } else if (checkedId == R.id.rbPMRExpress) {
                methodToSave = "pmr_express";
            } else if (checkedId == R.id.rb54321) {
                methodToSave = "grounding";
            } else if (checkedId == R.id.rbDescribe) {
                methodToSave = "describe";
            } else {
                methodToSave = "breathe";
            }

            SettingsManager.setString(SettingsManager.KEY_FAVORITE_METHOD_STR, methodToSave);
        });
    }
}