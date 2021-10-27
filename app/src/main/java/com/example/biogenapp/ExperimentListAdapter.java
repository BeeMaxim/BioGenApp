package com.example.biogenapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.biogenapp.MainActivity.fields;

public class ExperimentListAdapter extends RecyclerView.Adapter<ExperimentListAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    //private final List<ExperimentItem> fields;
    Context OurContext;

    ExperimentListAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        OurContext = context;
    }

    @Override
    public ExperimentListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExperimentListAdapter.ViewHolder holder, int position) {
        ExperimentItem field = fields.get(position);
        holder.name.setText(field.getName());
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        if (field.getStartTime() == 0) holder.dateTime.setText("Не начат");
        else holder.dateTime.setText(format.format(field.getStartTime()));
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(OurContext, ExperimentActivity.class);
                intent.putExtra("position", position);
                OurContext.startActivity(intent);
            }
        });
        holder.removeButton.setOnClickListener(new View.OnClickListener(){
                @Override public void onClick(View v){
                    LayoutInflater inflater = LayoutInflater.from(OurContext);
                    View window = inflater.inflate(R.layout.confirm_dialogue, null);
                    AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(OurContext);
                    DialogBuilder.setView(window);
                    DialogBuilder.setPositiveButton("ОК",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    fields.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, getItemCount());
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
            name = (TextView) view.findViewById(R.id.main_name_field);
            dateTime = (TextView) view.findViewById(R.id.logo);
            removeButton = (ImageButton) view.findViewById(R.id.remove_button);
        }
    }
}
