package ru.tajwid.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.tajwid.app.R;
import ru.tajwid.app.ui.adapter.TimeZoneAdapter;
import ru.tajwid.app.ui.fragment.MainFragment;


public class OnlineLearningActivity extends AppCompatActivity {

    RadioButton buttun1, buttun2, buttun3, radioButtun1, radioButtun2, radioButtun3;

    RecyclerView recyclerView;
    DatabaseReference database;
    TimeZoneAdapter timeZoneAdapter;
    ArrayList<TimeZoneLearning> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_learning);
        recyclerView = findViewById(R.id.recycler_list);
        database = FirebaseDatabase.getInstance().getReference("TimeZoneLearning");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        timeZoneAdapter = new TimeZoneAdapter(this, list);
        recyclerView.setAdapter(timeZoneAdapter);

//        database.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    TimeZoneLearning timeZoneLearning = dataSnapshot.getValue(TimeZoneLearning.class);
//                    list.add(timeZoneLearning);
//                }
//                timeZoneAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

// навигация назад  в меню
        Toolbar toolbar = findViewById(R.id.learning_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_toolbar_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//*********************************************************************
        buttun1 = (RadioButton) findViewById(R.id.button1);
        buttun2 = (RadioButton) findViewById(R.id.button2);
        buttun3 = (RadioButton) findViewById(R.id.button3);

        database.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                View.OnClickListener onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.button1:
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    TimeZoneLearning timeZoneLearning = dataSnapshot.getValue(TimeZoneLearning.class);
                                    list.add(timeZoneLearning);
                                }
                                timeZoneAdapter.notifyDataSetChanged();
                                break;
                            case R.id.button2:
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    TimeZoneLearning timeZoneLearning = dataSnapshot.getValue(TimeZoneLearning.class);
                                    list.add(timeZoneLearning);
                                }
                                timeZoneAdapter.notifyDataSetChanged();
                                break;
                            case R.id.button3:
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    TimeZoneLearning timeZoneLearning = dataSnapshot.getValue(TimeZoneLearning.class);
                                    list.add(timeZoneLearning);
                                }
                                timeZoneAdapter.notifyDataSetChanged();
                                break;
                        }
                    }
                };

                buttun1.setOnClickListener(onClickListener);
                buttun2.setOnClickListener(onClickListener);
                buttun3.setOnClickListener(onClickListener);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
//*********************************************************************
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
