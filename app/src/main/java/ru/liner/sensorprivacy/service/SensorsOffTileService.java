package ru.liner.sensorprivacy.service;

import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.ISensorPrivacyManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import rikka.shizuku.Shizuku;
import rikka.shizuku.ShizukuBinderWrapper;
import rikka.shizuku.SystemServiceHelper;
import ru.liner.sensorprivacy.Singleton;
import ru.liner.sensorprivacy.preference.Preferences;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 30.08.2023, 14:15
 * @noinspection JavadocLinkAsPlainText
 */
public class SensorsOffTileService extends TileService {
    private KeyguardManager keyguardManager;

    private Preferences preferences;
    private boolean privacyEnabled;

    private static final Singleton<ISensorPrivacyManager> sensorPrivacyManager = new Singleton<ISensorPrivacyManager>() {
        @Override
        protected ISensorPrivacyManager create() {
            return ISensorPrivacyManager.Stub.asInterface(new ShizukuBinderWrapper(SystemServiceHelper.getSystemService("sensor_privacy")));
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        Context context = getApplicationContext();
        keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        preferences = new Preferences(context);
    }

    @Override
    public void onStartListening() {
        privacyEnabled = preferences.get("privacy_enabled", false);
        refresh();
    }

    @Override
    public void onClick() {
        setPrivacyEnabled(!privacyEnabled);
    }

    private void refresh(){
        boolean shizukuReady = Shizuku.pingBinder() && Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED;
        Tile tile = getQsTile();
        if (tile == null)
            return;
        tile.setState(shizukuReady ? privacyEnabled ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE : Tile.STATE_UNAVAILABLE);
        tile.updateTile();
    }


    public void setPrivacyEnabled(boolean privacyEnabled) {
        if(keyguardManager.isKeyguardLocked())
            return;
        this.privacyEnabled = privacyEnabled;
        try {
            sensorPrivacyManager.get().setSensorPrivacy(privacyEnabled);
        } catch (RemoteException e) {
            e.printStackTrace();
            this.privacyEnabled = false;
        }
        preferences.put("privacy_enabled", privacyEnabled);
        refresh();
    }

    @Override
    public IBinder onBind(Intent intent) {
        TileService.requestListeningState(this, new ComponentName(this, SensorsOffTileService.class));
        return super.onBind(intent);
    }
}
