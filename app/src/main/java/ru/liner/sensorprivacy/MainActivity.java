package ru.liner.sensorprivacy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.SensorPrivacyManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;

import rikka.shizuku.SystemServiceHelper;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TAGTAG";
    private SwitchMaterial sensorPrivacyToggle;
    private SensorPrivacyManager sensorPrivacyManager;
    private static final String MANAGE_SENSOR_PRIVACY = "android.permission.MANAGE_SENSOR_PRIVACY";
    private static final String OBSERVE_SENSOR_PRIVACY = "android.permission.OBSERVE_SENSOR_PRIVACY";

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorPrivacyToggle = findViewById(R.id.sensorPrivacyToggle);
        sensorPrivacyToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Toast.makeText(MainActivity.this, "Sensors "+(b ? "Disabled" : "Enabled"), Toast.LENGTH_SHORT).show();

                try {
                    SystemServerApi.getSensorPrivacyManager().setSensorPrivacy(b);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }

}