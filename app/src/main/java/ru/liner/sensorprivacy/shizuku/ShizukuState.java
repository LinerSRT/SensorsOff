package ru.liner.sensorprivacy.shizuku;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 02.09.2023, 8:47
 * @noinspection JavadocLinkAsPlainText
 */
@IntDef({
        ShizukuState.NORMAL,
        ShizukuState.PERMISSION_DENIED,
        ShizukuState.PERMISSION_WAIT,
        ShizukuState.BINDER_DEAD,
        ShizukuState.UNKNOWN,
})
@Retention(RetentionPolicy.SOURCE)
public @interface ShizukuState {
    int NORMAL = 0;
    int PERMISSION_DENIED = 1;
    int PERMISSION_WAIT = 2;
    int BINDER_DEAD = 3;
    int UNKNOWN = -1;
}
