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
import android.widget.Toast;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true)); // перевод в горизонтальное полжение представление

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
        database.child("TimeZoneLearning").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    TimeZoneLearning timeZoneLearning = snapshot.child("User1").getValue(TimeZoneLearning.class);
                    list.add(timeZoneLearning);

                }
                timeZoneAdapter.notifyDataSetChanged();

                TextView btn = (TextView) findViewById(R.id.bottonClass);
                btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String url = snapshot.child("User1").child("url").getValue(String.class);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });
//*********************************************************************
                button1 = (RadioButton) findViewById(R.id.radiobutton1);
                button2 = (RadioButton) findViewById(R.id.radiobutton2);
                button3 = (RadioButton) findViewById(R.id.radiobutton3);

                View.OnClickListener onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.radiobutton1:
                                Toast.makeText(getApplicationContext(), "Вы вошли в Мужскую группу", Toast.LENGTH_LONG).show();
                                list.clear(); // предварительно очищаем список
                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                    TimeZoneLearning timeZoneLearning = snapshot.child("User1").getValue(TimeZoneLearning.class);
                                    list.add(timeZoneLearning);

                                    TextView btn = (TextView) findViewById(R.id.bottonClass);
                                    btn.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            String url = snapshot.child("User1").child("url").getValue(String.class);
                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                            i.setData(Uri.parse(url));
                                            startActivity(i);
                                        }
                                    });
                                }
                                break;

                            case R.id.radiobutton2:
                                Toast.makeText(getApplicationContext(), "Вы вошли в Женскую группу", Toast.LENGTH_LONG).show();
                                list.clear(); // предварительно очищаем список
                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                    TimeZoneLearning timeZoneLearning = snapshot.child("User2").getValue(TimeZoneLearning.class);
                                    list.add(timeZoneLearning);

                                    TextView btn = (TextView) findViewById(R.id.bottonClass);
                                    btn.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            String url = snapshot.child("User2").child("url").getValue(String.class);
                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                            i.setData(Uri.parse(url));
                                            startActivity(i);
                                        }
                                    });
                                }
                                break;

                            case R.id.radiobutton3:
                                Toast.makeText(getApplicationContext(), "Вы вошли в Детскую группу", Toast.LENGTH_LONG).show();
                                list.clear(); // предварительно очищаем список
                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                    TimeZoneLearning timeZoneLearning = snapshot.child("User3").getValue(TimeZoneLearning.class);
                                    list.add(timeZoneLearning);

                                    TextView btn = (TextView) findViewById(R.id.bottonClass);
                                    btn.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            String url = snapshot.child("User3").child("url").getValue(String.class);
                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                            i.setData(Uri.parse(url));
                                            startActivity(i);
                                        }
                                    });
                                }
                                break;
                        }
                        timeZoneAdapter.notifyDataSetChanged();

                    }

                };
                button1.setOnClickListener(onClickListener);
                button2.setOnClickListener(onClickListener);
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