package ru.liner.sensorprivacy.service;

import android.content.BroadcastReceiver;
import android.content.Intent;

import androidx.annotation.NonNull;

import ru.liner.sensorprivacy.R;
import ru.liner.sensorprivacy.preference.PreferenceListener;
import ru.liner.sensorprivacy.preference.Preferences;
import ru.liner.sensorprivacy.receiver.RestartReceiver;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 02.09.2023, 23:12
 *
 * @noinspection JavadocLinkAsPlainText
 */
public class SensorService extends ImmortalService implements PreferenceListener<String> {
    public static boolean isRunning;
    private Preferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        isRunning = true;
        preferences = new Preferences(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        preferences.register(this);
        return START_NOT_STICKY;
    }

    private void onApplicationChanged(@NonNull String packageName) {
        //TODO Check package name here, if its should be blocked - disable sensors until package will not changed
        //TODO May be also check if package has background services, if its true we also need await before enabling sensors again
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        preferences.unregister(this);
        isRunning = false;
    }

    @NonNull
    @Override
    public String getChannelID() {
        return SensorService.class.getSimpleName();
    }

    @NonNull
    @Override
    public String getChannelDescription() {
        return "Check running applications for automatic sensors toggling";
    }

    @Override
    public int getNotificationIcon() {
        return R.drawable.tile_icon_sensorsoff_active;
    }

    @NonNull
    @Override
    public Class<? extends BroadcastReceiver> getRestartReceiverClass() {
        return RestartReceiver.class;
    }

    @NonNull
    @Override
    public String key() {
        return "active_package";
    }

    @Override
    public void onChanged(@NonNull String newValue) {
        onApplicationChanged(newValue);
    }

    @NonNull
    @Override
    public String defaultValue() {
        return "";
    }
}
