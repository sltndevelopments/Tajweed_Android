package ru.tajwid.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.tajwid.app.R;
import ru.tajwid.app.ui.activity.TimeZoneLearning;

public class TimeZoneAdapter extends RecyclerView.Adapter<TimeZoneAdapter.MyViewHolder> {

    Context context;

    ArrayList<TimeZoneLearning> list;

    public TimeZoneAdapter(Context context, ArrayList<TimeZoneLearning> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_timelearning, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TimeZoneLearning timeZoneLearning = list.get(position);
        holder.name.setText(timeZoneLearning.getName());
        holder.data.setText(timeZoneLearning.getData());

//        holder.url.setText(timeZoneLearning.getUrl());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, data, url;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.username);
            data = itemView.findViewById(R.id.userdata);

//            url=itemView.findViewById(R.id.url);
        }
    }


}
