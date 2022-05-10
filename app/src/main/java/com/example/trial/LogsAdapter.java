package com.example.trial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.ViewHolder> {

    private ArrayList<UserHelperClass> localDataSet;
    private TextView date, time, type, name, key;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView date, time, type, name, key;

        public TextView getDate() {
            return date;
        }

        public TextView getTime() {
            return time;
        }

        public TextView getType() {
            return type;
        }

        public TextView getName() {
            return name;
        }

        public TextView getKey() {
            return key;
        }

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            date = view.findViewById(R.id.date);
            time = view.findViewById(R.id.time);
            type = view.findViewById(R.id.type);
            name = view.findViewById(R.id.name);
            key = view.findViewById(R.id.key);
        }


    }

    public LogsAdapter(ArrayList<UserHelperClass> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.logs_layout, viewGroup, false);
        date = view.findViewById(R.id.date);
        time = view.findViewById(R.id.time);
        type = view.findViewById(R.id.type);
        name = view.findViewById(R.id.name);
        key = view.findViewById(R.id.key);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.setIsRecyclable(false);
        date.setText(localDataSet.get(position).getDate());
        time.setText(localDataSet.get(position).getTime());
        type.setText(localDataSet.get(position).getType());
        name.setText(localDataSet.get(position).getFile_name());
        key.setText(localDataSet.get(position).getKey());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
