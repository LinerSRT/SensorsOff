package ru.liner.sensorprivacy.receiver;

import static ru.liner.sensorprivacy.Application.preferences;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 14.09.2023, 10:06
 * @noinspection JavadocLinkAsPlainText
 */
public class SensorsOffReceiver extends BroadcastReceiver {
    private static final String ACTION_ENABLE_SENSORS = "ru.liner.sensorprivacy.ACTION_ENABLE_SENSORS";
    private static final String ACTION_DISABLE_SENSORS = "ru.liner.sensorprivacy.ACTION_DISABLE_SENSORS";
    private static final String ACTION_TOGGLE_SENSORS = "ru.liner.sensorprivacy.ACTION_TOGGLE_SENSORS";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction() == null)
            return;
        switch (intent.getAction()){
            case ACTION_ENABLE_SENSORS:
                preferences.put("privacy_state", false);
                break;
            case ACTION_DISABLE_SENSORS:
                preferences.put("privacy_state", true);
                break;
            case ACTION_TOGGLE_SENSORS:
                preferences.put("privacy_state", !preferences.get("privacy_state", false));
                break;
        }
    }
}
