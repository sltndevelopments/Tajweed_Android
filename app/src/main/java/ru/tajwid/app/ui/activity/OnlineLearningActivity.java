package ru.tajwid.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import ru.tajwid.app.R;
import ru.tajwid.app.ui.adapter.TimeZoneAdapter;


public class OnlineLearningActivity extends AppCompatActivity {

    RadioButton button1, button2, button3;

    RecyclerView recyclerView;
    DatabaseReference database;
    TimeZoneAdapter timeZoneAdapter;
    ArrayList<TimeZoneLearning> list;

    private FirebaseDatabase mdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_learning);
        recyclerView = findViewById(R.id.recycler_list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)); // перевод в горизонтальное полжение представление

        list = new ArrayList<>();
        timeZoneAdapter = new TimeZoneAdapter(this, list);
        recyclerView.setAdapter(timeZoneAdapter);

        mdatabase = FirebaseDatabase.getInstance();
        database = mdatabase.getReference();

// навигация назад  в меню
        Toolbar toolbar = findViewById(R.id.learning_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_toolbar_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//*********************************************************************
        button1 = (RadioButton) findViewById(R.id.radiobutton1);
        database.child("TimeZoneLearning").child("User1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    TimeZoneLearning timeZoneLearning = snapshot.getValue(TimeZoneLearning.class);
                    list.add(timeZoneLearning);

                }
                timeZoneAdapter.notifyDataSetChanged();

                TextView btn = (TextView) findViewById(R.id.bottonClass);
                btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String url = snapshot.child("url").getValue(String.class);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });
//*********************************************************************
                View.OnClickListener onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.radiobutton1:
                                list.clear(); // предварительно очищаем список
                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                    TimeZoneLearning timeZoneLearning = snapshot.getValue(TimeZoneLearning.class);
                                    list.add(timeZoneLearning);

                                }
                                timeZoneAdapter.notifyDataSetChanged();

                                TextView btn = (TextView) findViewById(R.id.bottonClass);
                                btn.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        String url = snapshot.child("url").getValue(String.class);
                                        Intent i = new Intent(Intent.ACTION_VIEW);
                                        i.setData(Uri.parse(url));
                                        startActivity(i);
                                    }
                                });
                                break;
                        }
                    }
                };
                button1.setOnClickListener(onClickListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
        button2 = (RadioButton) findViewById(R.id.radiobutton2);
        database.child("TimeZoneLearning").child("User2").addValueEventListener(new ValueEventListener() { // стучимся на второй элемент спсика
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                View.OnClickListener onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.radiobutton2:
                                list.clear();
                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                    TimeZoneLearning timeZoneLearning = snapshot.getValue(TimeZoneLearning.class);
                                    list.add(timeZoneLearning);
                                }
                                timeZoneAdapter.notifyDataSetChanged();

                                TextView btn = (TextView) findViewById(R.id.bottonClass);
                                btn.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        String url = snapshot.child("url").getValue(String.class);// получаем  ссылку на трейтий элемент в БД
                                        Intent i = new Intent(Intent.ACTION_VIEW);
                                        i.setData(Uri.parse(url));
                                        startActivity(i);
                                    }
                                });
                                break;
                        }
                    }
                };
                button2.setOnClickListener(onClickListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        button3 = (RadioButton) findViewById(R.id.radiobutton3);
        database.child("TimeZoneLearning").child("User3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                View.OnClickListener onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.radiobutton3:
                                list.clear();
                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                    TimeZoneLearning timeZoneLearning = snapshot.getValue(TimeZoneLearning.class);
                                    list.add(timeZoneLearning);
                                }
                                timeZoneAdapter.notifyDataSetChanged();
                                TextView btn = (TextView) findViewById(R.id.bottonClass);
                                btn.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        String url = snapshot.child("url").getValue(String.class);
                                        Intent i = new Intent(Intent.ACTION_VIEW);
                                        i.setData(Uri.parse(url));
                                        startActivity(i);
                                    }
                                });
                                break;
                        }
                    }
                };
                button3.setOnClickListener(onClickListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
//*********************************************************************

//  мгновенная блокировка recyclerView, нет прокрутке
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return true;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
    }
//*********************************************************************
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
