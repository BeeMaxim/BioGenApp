package com.example.biogenapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import static com.example.biogenapp.MainActivity.fields;

public class ExperimentListAdapter extends RecyclerView.Adapter<ExperimentListAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    Context OurContext;

    ExperimentListAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        OurContext = context;
    }

    @NonNull
    @Override
    public ExperimentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExperimentListAdapter.ViewHolder holder, int position) {
        ExperimentItem field = fields.get(position);
        holder.name.setText(field.getName());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        if (field.getStartTime() == 0) holder.dateTime.setText(R.string.not_started);
        else holder.dateTime.setText(format.format(field.getStartTime()));
        holder.root.setOnClickListener(v -> {
            Intent intent = new Intent(OurContext, ExperimentActivity.class);
            intent.putExtra("position", position);
            OurContext.startActivity(intent);
        });
        holder.removeButton.setOnClickListener(v -> {
            LayoutInflater inflater = LayoutInflater.from(OurContext);
            View window = inflater.inflate(R.layout.confirm_dialogue, null);
            AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(OurContext);
            DialogBuilder.setView(window);
            DialogBuilder.setPositiveButton("OK",
                    (dialog, id) -> {
                        fields.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, getItemCount());
                    });
            DialogBuilder.setNegativeButton("cancel",
                    (dialog, id) -> dialog.cancel());
            DialogBuilder.create().show();
        });
    }

    @Override
    public int getItemCount() {
        return fields.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        final TextView name;
        final TextView dateTime;
        final ImageButton removeButton;
        View root;
        ViewHolder(View view){
            super(view);
            root = view;
            name = view.findViewById(R.id.main_name_field);
            dateTime = view.findViewById(R.id.logo);
            removeButton = view.findViewById(R.id.remove_button);
        }
    }
}
