package ru.liner.sensorprivacy;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;

import ru.liner.sensorprivacy.utils.ActivityResultLauncher;

public class SettingsActivity extends AppCompatActivity {
    private final ActivityResultLauncher<Intent, ActivityResult> launcher = ActivityResultLauncher.registerActivityForResult(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        //TODO If service enabled, check WindowService running
        //TODO If SensorsService not running, start it here (never happens, but for sure)
        //TODO Fill UI

//        if (!Application.isAccessibilityServiceEnabled(this))
//            launcher.launch(new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS), result -> {
//                if (Application.isAccessibilityServiceEnabled(SettingsActivity.this)) {
//
//                }
//            });
    }
}