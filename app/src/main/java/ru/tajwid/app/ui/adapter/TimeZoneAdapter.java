package ru.tajwid.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
        holder.hour1.setText(timeZoneLearning.getHour1());
        holder.hour2.setText(timeZoneLearning.getHour2());
        holder.day1.setText(timeZoneLearning.getDay1());
        holder.day2.setText(timeZoneLearning.getDay2());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
//
//    public void clear() {
//        list.clear();
////        notifyDataSetChanged();
//    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, hour1, hour2, day1, day2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.username);
            hour1 = itemView.findViewById(R.id.userhour1);
            hour2 = itemView.findViewById(R.id.userhour2);
            day1 = itemView.findViewById(R.id.userday1);
            day2 = itemView.findViewById(R.id.userday2);
        }
    }


}
