package ru.liner.sensorprivacy;


import android.hardware.ISensorPrivacyManager;

import rikka.shizuku.ShizukuBinderWrapper;
import rikka.shizuku.SystemServiceHelper;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 30.08.2023, 13:35
 */
public class SystemServerApi {
    private static final Singleton<ISensorPrivacyManager> SENSOR_PRIVACY_MANAGER = new Singleton<ISensorPrivacyManager>() {
        @Override
        protected ISensorPrivacyManager create() {
            return ISensorPrivacyManager.Stub.asInterface(new ShizukuBinderWrapper(SystemServiceHelper.getSystemService("sensor_privacy")));
        }
    };

    public static ISensorPrivacyManager getSensorPrivacyManager(){
        return SENSOR_PRIVACY_MANAGER.get();
    }
}
