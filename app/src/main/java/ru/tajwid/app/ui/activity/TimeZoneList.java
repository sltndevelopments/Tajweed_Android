package ru.tajwid.app.ui.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ru.tajwid.app.R;
import ru.tajwid.app.ui.adapter.TimeZoneAdapter;

public class TimeZoneList extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference database;
    TimeZoneAdapter timeZoneAdapter;
    ArrayList<TimeZoneLearning> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timezone_list);

        recyclerView = findViewById(R.id.recycler_list);
        database = FirebaseDatabase.getInstance().getReference("TimeZoneLearning");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        timeZoneAdapter = new TimeZoneAdapter(this, list);
        recyclerView.setAdapter(timeZoneAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TimeZoneLearning timeZoneLearning = dataSnapshot.getValue(TimeZoneLearning.class);
                    list.add(timeZoneLearning);
                }
                timeZoneAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
