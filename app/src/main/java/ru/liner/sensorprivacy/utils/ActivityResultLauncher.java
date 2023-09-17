package ru.liner.sensorprivacy.utils;

import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 02.09.2023, 23:54
 *
 * @noinspection JavadocLinkAsPlainText, unused
 */
public class ActivityResultLauncher<Input, Result> {
    @NonNull
    public static <Input, Result> ActivityResultLauncher<Input, Result> registerForActivityResult(
            @NonNull ActivityResultCaller caller,
            @NonNull ActivityResultContract<Input, Result> contract,
            @Nullable OnActivityResult<Result> onActivityResult) {
        return new ActivityResultLauncher<>(caller, contract, onActivityResult);
    }

    @NonNull
    public static <Input, Result> ActivityResultLauncher<Input, Result> registerForActivityResult(
            @NonNull ActivityResultCaller caller,
            @NonNull ActivityResultContract<Input, Result> contract) {
        return registerForActivityResult(caller, contract, null);
    }

    @NonNull
    public static ActivityResultLauncher<Intent, ActivityResult> registerActivityForResult(@NonNull ActivityResultCaller caller) {
        return registerForActivityResult(caller, new ActivityResultContracts.StartActivityForResult());
    }

    public interface OnActivityResult<O> {
        void onActivityResult(O result);
    }

    private final androidx.activity.result.ActivityResultLauncher<Input> launcher;
    @Nullable
    private OnActivityResult<Result> onActivityResult;

    private ActivityResultLauncher(@NonNull ActivityResultCaller caller,
                                   @NonNull ActivityResultContract<Input, Result> contract,
                                   @Nullable OnActivityResult<Result> onActivityResult) {
        this.onActivityResult = onActivityResult;
        this.launcher = caller.registerForActivityResult(contract, this::callOnActivityResult);
    }

    public void setOnActivityResult(@Nullable OnActivityResult<Result> onActivityResult) {
        this.onActivityResult = onActivityResult;
    }

    public void launch(Input input, @Nullable OnActivityResult<Result> onActivityResult) {
        if (onActivityResult != null) {
            this.onActivityResult = onActivityResult;
        }
        launcher.launch(input);
    }

    public void launch(Input input) {
        launch(input, this.onActivityResult);
    }

    private void callOnActivityResult(Result result) {
        if (onActivityResult != null) onActivityResult.onActivityResult(result);
    }
}