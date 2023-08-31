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
import android.widget.Toast;

import rikka.shizuku.Shizuku;
import rikka.shizuku.ShizukuBinderWrapper;
import rikka.shizuku.SystemServiceHelper;
import ru.liner.sensorprivacy.R;
import ru.liner.sensorprivacy.ShizukuState;
import ru.liner.sensorprivacy.Singleton;
import ru.liner.sensorprivacy.preference.Preferences;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 30.08.2023, 14:15
 *
 * @noinspection JavadocLinkAsPlainText
 */
public class SensorsOffTileService extends TileService {
    @ShizukuState
    private int shizukuState;

    private KeyguardManager keyguardManager;
    private Preferences preferences;
    private boolean privacyEnabled;

    private static final Singleton<ISensorPrivacyManager> sensorPrivacyManager = new Singleton<ISensorPrivacyManager>() {
        @Override
        protected ISensorPrivacyManager create() {
            return ISensorPrivacyManager.Stub.asInterface(new ShizukuBinderWrapper(SystemServiceHelper.getSystemService("sensor_privacy")));
        }
    };

    private Icon activeIcon;
    private Icon inactiveIcon;
    private Icon shizukuWarn;


    @Override
    public void onCreate() {
        super.onCreate();
        Context context = getApplicationContext();
        keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        preferences = new Preferences(context);
        activeIcon = Icon.createWithResource(context, R.drawable.tile_icon_sensorsoff_active);
        inactiveIcon = Icon.createWithResource(context, R.drawable.tile_icon_sensorsoff_inactive);
        shizukuWarn = Icon.createWithResource(context, R.drawable.tile_shizuku_warn);
        shizukuState = obtainShizukuState();
    }

    @Override
    public void onStartListening() {
        privacyEnabled = preferences.get("privacy_enabled", false);
        shizukuState = obtainShizukuState();
        updateUI();
    }


    @Override
    public void onClick() {
        shizukuState = obtainShizukuState();
        switch (shizukuState) {
            case ShizukuState.NORMAL:
                //Do nothing if device is on lockscreen
                if (keyguardManager.isKeyguardLocked())
                    break;
                try {
                    privacyEnabled = !privacyEnabled;
                    sensorPrivacyManager.get().setSensorPrivacy(privacyEnabled);
                } catch (RemoteException ignored) {
                    privacyEnabled = false;
                    shizukuState = obtainShizukuState();
                }
                preferences.put("privacy_enabled", privacyEnabled);
                break;
            case ShizukuState.INACTIVE:
                //TODO Inform user that our tile requires activation in Shizuku
                //TODO Toast now working :(
                Toast.makeText(this, "Enable SensorsOff in Shizuku", Toast.LENGTH_SHORT).show();
                break;
            case ShizukuState.UNAVAILABLE:
                //TODO Inform user that our tile required properly installed Shizuku
                //TODO Toast now working :(
                Toast.makeText(this, "Shizuku app require additional setup", Toast.LENGTH_SHORT).show();
                break;
            case ShizukuState.UNKNOWN:
                //TODO Inform user that our tile cant locate Shizuku app on device
                //TODO Toast now working :(
                Toast.makeText(this, "Shizuku app not found", Toast.LENGTH_SHORT).show();
                break;
        }
        updateUI();
    }

    private void updateUI() {
        Tile tile = getQsTile();
        if (tile == null)
            return;
        switch (shizukuState) {
            case ShizukuState.NORMAL:
                tile.setState(privacyEnabled ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
                tile.setIcon(privacyEnabled ? activeIcon : inactiveIcon);
                tile.setLabel(privacyEnabled ? getString(R.string.tile_sensors_disabled) : getString(R.string.tile_sensors_enabled));
                break;
            case ShizukuState.INACTIVE:
                tile.setState(Tile.STATE_INACTIVE);
                tile.setIcon(shizukuWarn);
                tile.setLabel(getString(R.string.tile_enable_in_shizuku_warn));
                break;
            case ShizukuState.UNAVAILABLE:
                tile.setState(Tile.STATE_INACTIVE);
                tile.setIcon(shizukuWarn);
                tile.setLabel(getString(R.string.tile_shizuku_not_configured_warn));
                break;
            case ShizukuState.UNKNOWN:
                tile.setState(Tile.STATE_INACTIVE);
                tile.setIcon(shizukuWarn);
                tile.setLabel(getString(R.string.tile_shizuku_not_found_warn));
                break;
        }
        tile.updateTile();
    }


    @Override
    public IBinder onBind(Intent intent) {
        TileService.requestListeningState(this, new ComponentName(this, SensorsOffTileService.class));
        return super.onBind(intent);
    }


    @ShizukuState
    private int obtainShizukuState() {
        return Shizuku.pingBinder() ?
                Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED ?
                        ShizukuState.NORMAL :
                        ShizukuState.INACTIVE :
                        ShizukuState.UNAVAILABLE;
    }

}
