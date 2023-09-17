package ru.liner.sensorprivacy.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 17.09.2023, 11:45
 *
 * @noinspection JavadocLinkAsPlainText
 */
public class ApplicationManager {
    /**
     * Find associated with package name application and loads their name
     *
     * @param context     application context
     * @param packageName searched package
     * @return name of application if found, package name if not
     */
    public static String getApplicationName(@NonNull Context context, @NonNull String packageName) {
        return Consumer.of(context)
                .next(input -> {
                    PackageManager packageManager = input.getPackageManager();
                    Optional<ResolveInfo> packageInfo = packageManager.queryIntentActivities(
                                    new Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER),
                                    0
                            )
                            .stream()
                            .filter(resolveInfo -> resolveInfo.activityInfo.packageName.equals(packageName))
                            .findFirst();
                    return packageInfo.map(resolveInfo -> resolveInfo.loadLabel(packageManager).toString()).orElse(packageName);
                }).get();
    }

    public static String getApplicationName(@NonNull Context context, @NonNull ResolveInfo resolveInfo) {
        return resolveInfo.loadLabel(context.getPackageManager()).toString();
    }

    public static Drawable getApplicationIcon(@NonNull Context context, @NonNull ResolveInfo resolveInfo) {
        return resolveInfo.loadIcon(context.getPackageManager());
    }

    public static List<ResolveInfo> getAllApplications(@NonNull Context context) {
        List<ResolveInfo> list = Consumer.of(context)
                .next((Consumer.FunctionB<Context, List<ResolveInfo>>) input -> input.getPackageManager().queryIntentActivities(new Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER), 0))
                .next(input -> input.stream().filter(resolveInfo -> !resolveInfo.activityInfo.packageName.equals(context.getPackageName())).collect(Collectors.toList()))
                .orElse(new ArrayList<>());
        Collator collator = Collator.getInstance(context.getResources().getConfiguration().getLocales().get(0));
        list.sort((a1, a2) -> collator.compare(getApplicationName(context, a1), getApplicationName(context, a2)));
        return list;
    }

    private static boolean isSystemPackage(@NonNull ResolveInfo resolveInfo) {
        return (resolveInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }
}
