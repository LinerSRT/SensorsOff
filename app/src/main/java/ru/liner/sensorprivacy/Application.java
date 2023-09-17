package ru.liner.sensorprivacy;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.view.accessibility.AccessibilityManager;

import org.lsposed.hiddenapibypass.HiddenApiBypass;

import java.util.List;

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
    public static Preferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = new Preferences(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        HiddenApiBypass.addHiddenApiExemptions("L");
    }


    public static boolean isAccessibilityServiceEnabled(Context context) {
        AccessibilityManager accessibilityManager = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> accessibilityServices = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);
        for (AccessibilityServiceInfo info : accessibilityServices)
            if (info.getId().contains(context.getPackageName()))
                return true;
        return false;
    }
}
