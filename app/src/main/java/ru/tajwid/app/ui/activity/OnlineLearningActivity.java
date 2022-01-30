package ru.tajwid.app.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import ru.tajwid.app.R;
import ru.tajwid.app.ui.adapter.LessonsListAdapter;


public class OnlineLearningActivity extends AppCompatActivity implements View.OnClickListener {

    RadioButton buttun1, buttun2, buttun3, radioButtun1, radioButtun2, radioButtun3;


    public void initRadioButton() {
        radioButtun1 = (RadioButton) findViewById(R.id.button1);
        radioButtun2 = (RadioButton) findViewById(R.id.button2);
        radioButtun3 = (RadioButton) findViewById(R.id.button3);

        buttun1 = (RadioButton) findViewById(R.id.button1);
        buttun2 = (RadioButton) findViewById(R.id.button2);
        buttun3 = (RadioButton) findViewById(R.id.button3);

        radioButtun1.setOnClickListener(this);
        radioButtun2.setOnClickListener(this);
        radioButtun3.setOnClickListener(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_learning);

        initRadioButton();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                radioButtun1.setBackground(getDrawable(R.drawable.costom_radiobutton2));
                break;
            case R.id.button2:
                radioButtun2.setBackground(getDrawable(R.drawable.costom_radiobutton2));
                break;
            case R.id.button3:
                radioButtun3.setBackground(getDrawable(R.drawable.costom_radiobutton2));
                break;
        }
        // выше подправить на цикл меняющий цвет после смены кнопки
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
