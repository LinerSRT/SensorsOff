package ru.liner.sensorprivacy.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.materialswitch.MaterialSwitch;

import java.util.ArrayList;
import java.util.List;

import ru.liner.sensorprivacy.R;
import ru.liner.sensorprivacy.model.ApplicationModel;
import ru.liner.sensorprivacy.utils.Spans;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 17.09.2023, 11:37
 *
 * @noinspection JavadocLinkAsPlainText
 */
public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ViewHolder> {
    private final Context context;
    private final Callback callback;
    private final List<ApplicationModel> applicationList;

    public ApplicationAdapter(Context context, Callback callback) {
        this.context = context;
        this.applicationList = new ArrayList<>();
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_application, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ApplicationModel model = applicationList.get(position);
        holder.icon.setImageDrawable(model.icon);
        holder.packageHolder.setText(
                new Spans()
                        .append(model.applicationName, new AbsoluteSizeSpan(context.getResources().getDimensionPixelSize(R.dimen.application_name_size)))
                        .append("\n")
                        .append(model.packageName, new StyleSpan(Typeface.ITALIC), new AbsoluteSizeSpan(context.getResources().getDimensionPixelSize(R.dimen.application_package_size)))
                        .get()
        );
        holder.packageHolder.setOnCheckedChangeListener((compoundButton, b) -> callback.onChanged(model.packageName, b));

        holder.packageHolder.setChecked(model.isBlocked);
    }


    public void addApplication(ApplicationModel applicationModel) {
        this.applicationList.add(applicationModel);
        notifyItemInserted(applicationList.size() - 1);
    }

    @Override
    public int getItemCount() {
        return applicationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView icon;
        private final MaterialSwitch packageHolder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.applicationIcon);
            packageHolder = itemView.findViewById(R.id.packageHolder);
        }
    }

    public interface Callback {
        void onChanged(String packageName, boolean block);
    }
}
