package ru.liner.sensorprivacy.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;

import androidx.activity.result.ActivityResult;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.materialswitch.MaterialSwitch;

import java.util.List;

import ru.liner.sensorprivacy.Application;
import ru.liner.sensorprivacy.R;
import ru.liner.sensorprivacy.adapter.ApplicationAdapter;
import ru.liner.sensorprivacy.model.ApplicationModel;
import ru.liner.sensorprivacy.preference.Preferences;
import ru.liner.sensorprivacy.service.BlockingService;
import ru.liner.sensorprivacy.utils.ActivityResultLauncher;
import ru.liner.sensorprivacy.utils.ApplicationManager;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 17.09.2023, 11:28
 */
public class MainActivity extends AppCompatActivity {
    private final ActivityResultLauncher<Intent, ActivityResult> launcher = ActivityResultLauncher.registerActivityForResult(this);
    private ApplicationAdapter adapter;
    private Preferences preferences;
    private List<String> blockedApplicationList;
    private MaterialSwitch enableWindowService;

    @Override
    protected void onStart() {
        super.onStart();
        Window window = getWindow();
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);

        //TODO Add search filter to recyclerview
        //TODO Add visual status of service to enable switch
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = new Preferences(this);
        blockedApplicationList = preferences.getList("blocked_application_list", String.class);
        enableWindowService = findViewById(R.id.enableWindowService);
        RecyclerView recyclerView = findViewById(R.id.recycler_blocking_app);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new ApplicationAdapter(this, (packageName, block) -> {
            if (block && !blockedApplicationList.contains(packageName)) {
                blockedApplicationList.add(packageName);
            } else {
                blockedApplicationList.remove(packageName);
            }
            preferences.put("blocked_application_list", blockedApplicationList);
        });
        recyclerView.setAdapter(adapter);
        enableWindowService.setOnClickListener(view -> {
            if (enableWindowService.isChecked() && !Application.isAccessibilityServiceEnabled(MainActivity.this))
                launcher.launch(new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS), result -> {
                    if (Application.isAccessibilityServiceEnabled(MainActivity.this)) {
                        startService(new Intent(MainActivity.this, BlockingService.class));
                        enableWindowService.setChecked(true);
                    } else {
                        enableWindowService.setChecked(false);
                    }
                });
            if (!enableWindowService.isChecked())
                stopService(new Intent(MainActivity.this, BlockingService.class));
        });
        ApplicationManager.getAllApplications(this).forEach(resolveInfo -> {
            adapter.addApplication(
                    new ApplicationModel(
                            resolveInfo.activityInfo.packageName,
                            ApplicationManager.getApplicationName(this, resolveInfo),
                            ApplicationManager.getApplicationIcon(this, resolveInfo),
                            blockedApplicationList.contains(resolveInfo.activityInfo.packageName)
                    ));
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        preferences.put("blocked_application_list", blockedApplicationList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        blockedApplicationList = preferences.getList("blocked_application_list", String.class);
    }
}
