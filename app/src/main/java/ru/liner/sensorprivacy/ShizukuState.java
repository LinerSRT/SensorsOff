package ru.liner.sensorprivacy;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 31.08.2023, 9:56
 * @noinspection JavadocLinkAsPlainText
 */
@IntDef({
        ShizukuState.NORMAL,
        ShizukuState.INACTIVE,
        ShizukuState.UNAVAILABLE,
        ShizukuState.UNKNOWN
})
@Retention(RetentionPolicy.SOURCE)
public @interface ShizukuState {
    int NORMAL = 0;
    int INACTIVE = 1;
    int UNAVAILABLE = 2;
    int UNKNOWN = -1;
}
