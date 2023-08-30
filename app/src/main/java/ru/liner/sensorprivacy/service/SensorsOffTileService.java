package ru.liner.sensorprivacy.service;

import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import rikka.shizuku.Shizuku;
import ru.liner.sensorprivacy.SystemServerApi;
import ru.liner.sensorprivacy.preference.Preferences;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 30.08.2023, 14:15
 */
public class SensorsOffTileService extends TileService {
    private static final String TAG = "TAGTAG";
    private Context context;
    private KeyguardManager keyguardManager;

    private Preferences preferences;
    private boolean shizukuReady;
    private boolean privacyEnabled;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
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
        shizukuReady = Shizuku.pingBinder() && Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED;
        Tile tile = getQsTile();
        if (tile == null)
            return;
        tile.setState(shizukuReady ? privacyEnabled ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE : Tile.STATE_UNAVAILABLE);
        tile.updateTile();
    }


    public void setPrivacyEnabled(boolean privacyEnabled) {
        try {
            this.privacyEnabled = privacyEnabled;
            preferences.put("privacy_enabled", privacyEnabled);
            SystemServerApi.getSensorPrivacyManager().setSensorPrivacy(privacyEnabled);
        } catch (RemoteException e) {
            e.printStackTrace();
            this.privacyEnabled = false;
            preferences.put("privacy_enabled", privacyEnabled);
        }
        refresh();
    }

    @Override
    public IBinder onBind(Intent intent) {
        TileService.requestListeningState(this, new ComponentName(this, SensorsOffTileService.class));
        return super.onBind(intent);
    }
}
