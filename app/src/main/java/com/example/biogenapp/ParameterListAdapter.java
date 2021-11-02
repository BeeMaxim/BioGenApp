package com.example.biogenapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.biogenapp.MainActivity.fields;

public class ParameterListAdapter extends RecyclerView.Adapter<ParameterListAdapter.ViewHolder>{
    private final LayoutInflater inflater;
    private Context OurContext;
    private int pos;

    ParameterListAdapter(Context context, int Pos) {
        this.inflater = LayoutInflater.from(context);
        OurContext = context;
        pos = Pos;
    }

    @NonNull
    @Override
    public ParameterListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.parameter_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ParameterListAdapter.ViewHolder holder, int position) {
        ParameterItem field = fields.get(pos).getParameterList().get(position);
        holder.key.setText(field.getKey());
        String i = String.valueOf(field.getValue());
        holder.value.setText(i);
        holder.date.setText(field.getDate());
        holder.time.setText(field.getTime());
        holder.deleteButton.setOnClickListener(v -> {
            LayoutInflater inflater = LayoutInflater.from(OurContext);
            View window = inflater.inflate(R.layout.confirm_dialogue, null);
            AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(OurContext);
            DialogBuilder.setView(window);
            DialogBuilder.setPositiveButton("OK",
                    (dialog, id) -> {
                        fields.get(pos).remove(position);
                        notifyDataSetChanged();
                    });
            DialogBuilder.setNegativeButton("cancel",
                    (dialog, id) -> dialog.cancel());
            DialogBuilder.create().show();

        });
    }

    @Override
    public int getItemCount() {
        return fields.get(pos).getParameterList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView key, value, date, time;
        final ImageButton deleteButton;
        ViewHolder(View view) {
            super(view);
            key = view.findViewById(R.id.key_text);
            value = view.findViewById(R.id.value_text);
            date = view.findViewById(R.id.date_text);
            time = view.findViewById(R.id.time_text);
            deleteButton = view.findViewById(R.id.delete_button);
        }
    }
}
