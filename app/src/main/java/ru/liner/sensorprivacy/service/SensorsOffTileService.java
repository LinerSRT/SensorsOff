package ru.liner.sensorprivacy.service;

import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Icon;
import android.hardware.ISensorPrivacyManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import androidx.annotation.NonNull;

import rikka.shizuku.Shizuku;
import rikka.shizuku.ShizukuBinderWrapper;
import rikka.shizuku.SystemServiceHelper;
import ru.liner.sensorprivacy.R;
import ru.liner.sensorprivacy.preference.Preferences;
import ru.liner.sensorprivacy.shizuku.ShizukuState;
import ru.liner.sensorprivacy.utils.Broadcast;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 30.08.2023, 14:15
 *
 * @noinspection JavadocLinkAsPlainText
 */
public class SensorsOffTileService extends TileService implements Shizuku.OnRequestPermissionResultListener, Shizuku.OnBinderReceivedListener, Shizuku.OnBinderDeadListener {
    private static final int REQUEST_CODE_SHIZUKU = 9988;
    private static final String ACTION_ENABLE_SENSORS = "ru.liner.sensorprivacy.ACTION_ENABLE_SENSORS";
    private static final String ACTION_DISABLE_SENSORS = "ru.liner.sensorprivacy.ACTION_DISABLE_SENSORS";
    private static final String ACTION_TOGGLE_SENSORS = "ru.liner.sensorprivacy.ACTION_TOGGLE_SENSORS";
    private ISensorPrivacyManager sensorPrivacyManager;
    private KeyguardManager keyguardManager;
    private Preferences preferences;
    private Broadcast broadcast;
    private boolean privacyEnabled;
    private Icon activeIcon;
    private Icon inactiveIcon;
    private Icon warningIcon;
    private Icon stopIcon;
    @ShizukuState
    private int shizukuState;

    @Override
    public void onCreate() {
        super.onCreate();
        Context context = getApplicationContext();
        sensorPrivacyManager = ISensorPrivacyManager.Stub.asInterface(new ShizukuBinderWrapper(SystemServiceHelper.getSystemService("sensor_privacy")));
        keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        preferences = new Preferences(context);
        broadcast = new Broadcast(context, ACTION_ENABLE_SENSORS, ACTION_DISABLE_SENSORS, ACTION_TOGGLE_SENSORS) {
            @Override
            public void handle(@NonNull Context context, @NonNull Intent intent) {
                if(intent.getAction() != null)
                    switch (intent.getAction()){
                        case ACTION_ENABLE_SENSORS:
                            setPrivacyEnabled(false);
                            break;
                        case ACTION_DISABLE_SENSORS:
                            setPrivacyEnabled(true);
                            break;
                        case ACTION_TOGGLE_SENSORS:
                            setPrivacyEnabled(!privacyEnabled);
                            break;
                    }

            }
        };
        activeIcon = Icon.createWithResource(context, R.drawable.tile_icon_sensorsoff_active);
        inactiveIcon = Icon.createWithResource(context, R.drawable.tile_icon_sensorsoff_inactive);
        warningIcon = Icon.createWithResource(context, R.drawable.tile_icon_warning);
        stopIcon = Icon.createWithResource(context, R.drawable.tile_icon_stop);
    }

    @Override
    public void onStartListening() {
        privacyEnabled = preferences.get("privacy_enabled", false);
        shizukuState = checkShizukuState();
        broadcast.register();
        updateUI();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
        broadcast.unregister();
    }

    @Override
    public void onClick() {
        setPrivacyEnabled(!privacyEnabled);
    }

    private void updateUI() {
        Tile tile = getQsTile();
        if (tile == null)
            return;
        switch (shizukuState) {
            case ShizukuState.NORMAL:
                tile.setState(privacyEnabled ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
                tile.setIcon(privacyEnabled ? activeIcon : inactiveIcon);
                tile.setLabel(getString(privacyEnabled ? R.string.tile_sensors_disabled : R.string.tile_sensors_enabled));
                break;
            case ShizukuState.PERMISSION_WAIT:
            case ShizukuState.PERMISSION_DENIED:
                tile.setState(Tile.STATE_UNAVAILABLE);
                tile.setIcon(warningIcon);
                tile.setLabel(getString(R.string.tile_permission_required));
                break;
            case ShizukuState.BINDER_DEAD:
                tile.setState(Tile.STATE_UNAVAILABLE);
                tile.setIcon(warningIcon);
                tile.setLabel(getString(R.string.tile_shizuku_not_working));
                break;
            case ShizukuState.UNKNOWN:
                tile.setState(Tile.STATE_UNAVAILABLE);
                tile.setIcon(stopIcon);
                tile.setLabel(getString(R.string.tile_shizuku_error));
                break;
        }
        tile.updateTile();
    }

    @Override
    public IBinder onBind(Intent intent) {
        TileService.requestListeningState(this, new ComponentName(this, SensorsOffTileService.class));
        return super.onBind(intent);
    }

    private void setPrivacyEnabled(boolean enabled){
        shizukuState = checkShizukuState();
        if (shizukuState == ShizukuState.NORMAL) {
            if (!keyguardManager.isKeyguardLocked()) {
                try {
                    privacyEnabled = enabled;
                    sensorPrivacyManager.setSensorPrivacy(privacyEnabled);
                } catch (RemoteException | SecurityException ignored) {
                    privacyEnabled = false;
                    shizukuState = checkShizukuState();
                }
                preferences.put("privacy_enabled", privacyEnabled);
            }
        }
        updateUI();
    }

    private boolean checkShizukuPermission() {
        if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED)
            return true;
        if (Shizuku.isPreV11())
            return false;
        if (Shizuku.shouldShowRequestPermissionRationale())
            return false;
        Shizuku.requestPermission(REQUEST_CODE_SHIZUKU);
        return false;
    }

    private boolean isShizukuAlive() {
        return Shizuku.pingBinder();
    }

    @ShizukuState
    private int checkShizukuState() {
        if (isShizukuAlive()) {
            Shizuku.addRequestPermissionResultListener(this);
            if (checkShizukuPermission()) {
                return ShizukuState.NORMAL;
            } else {
                return ShizukuState.PERMISSION_WAIT;
            }
        } else {
            Shizuku.addBinderReceivedListener(this);
            return ShizukuState.BINDER_DEAD;
        }
    }

    @Override
    public void onRequestPermissionResult(int requestCode, int grantResult) {
        if (requestCode == REQUEST_CODE_SHIZUKU && grantResult == PackageManager.PERMISSION_GRANTED) {
            shizukuState = ShizukuState.NORMAL;
        } else {
            shizukuState = ShizukuState.PERMISSION_DENIED;
        }
        Shizuku.removeRequestPermissionResultListener(this);
        updateUI();
    }

    @Override
    public void onBinderReceived() {
        shizukuState = checkShizukuState();
        broadcast.register();
        Shizuku.removeBinderReceivedListener(this);
        updateUI();
        broadcast.register();
    }

    @Override
    public void onBinderDead() {
        shizukuState = ShizukuState.BINDER_DEAD;
        broadcast.unregister();
        Shizuku.addBinderReceivedListener(this);
        updateUI();
    }
}
