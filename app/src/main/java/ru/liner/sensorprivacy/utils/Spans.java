package ru.liner.sensorprivacy.utils;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.CharacterStyle;

import androidx.annotation.NonNull;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 17.09.2023, 13:43
 *
 * @noinspection JavadocLinkAsPlainText
 */
public class Spans {
    private final SpannableStringBuilder stringBuilder;

    public Spans() {
        this.stringBuilder = new SpannableStringBuilder();
    }

    public Spans append(@NonNull String text) {
        this.stringBuilder.append(text);
        return this;
    }


    @SafeVarargs
    public final <Span extends CharacterStyle> Spans append(@NonNull String text, @NonNull Span... spans) {
        int spanStart = stringBuilder.length();
        int spanEnd = spanStart + text.length();
        this.stringBuilder.append(text);
        for (Span span : spans)
            this.stringBuilder.setSpan(span, spanStart, spanEnd, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return this;
    }

    public SpannableStringBuilder get() {
        return stringBuilder;
    }
}
