package com.example.biogenapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

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

    @Override
    public ParameterListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
        holder.deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(OurContext);
                View window = inflater.inflate(R.layout.confirm_dialogue, null);
                AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(OurContext);
                DialogBuilder.setView(window);
                DialogBuilder.setPositiveButton("ОК",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                fields.get(pos).remove(position);
                                notifyDataSetChanged();
                            }
                        });
                DialogBuilder.setNegativeButton("отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                DialogBuilder.create().show();

            }
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
            key = (TextView) view.findViewById(R.id.key_text);
            value = (TextView) view.findViewById(R.id.value_text);
            date = (TextView) view.findViewById(R.id.date_text);
            time = (TextView) view.findViewById(R.id.time_text);
            deleteButton = (ImageButton) view.findViewById(R.id.delete_button);
        }
    }
}
