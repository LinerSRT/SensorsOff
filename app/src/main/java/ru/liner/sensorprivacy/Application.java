package ru.liner.sensorprivacy;

import android.content.Context;

import org.lsposed.hiddenapibypass.HiddenApiBypass;

import ru.liner.sensorprivacy.preference.Preferences;


/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 30.08.2023, 12:13
 *
 * @noinspection JavadocLinkAsPlainText
 */
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Preferences.create(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        HiddenApiBypass.addHiddenApiExemptions("L");
    }
}
