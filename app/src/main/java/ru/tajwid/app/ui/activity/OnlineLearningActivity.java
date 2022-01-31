package ru.tajwid.app.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ru.tajwid.app.R;


public class OnlineLearningActivity extends AppCompatActivity {

    RadioButton buttun1, buttun2, buttun3, radioButtun1, radioButtun2, radioButtun3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_learning);


        buttun1 = (RadioButton) findViewById(R.id.button1);
        buttun2 = (RadioButton) findViewById(R.id.button2);
        buttun3 = (RadioButton) findViewById(R.id.button3);

        radioButtun1 = (RadioButton) findViewById(R.id.button1);
        radioButtun2 = (RadioButton) findViewById(R.id.button2);
        radioButtun3 = (RadioButton) findViewById(R.id.button3);


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button1:
//                        radioButtun1.setBackground(getDrawable(R.drawable.costom_radiobutton2));
                        break;
                    case R.id.button2:
//                        radioButtun2.setBackground(getDrawable(R.drawable.costom_radiobutton2));
                        break;
                    case R.id.button3:
//                        radioButtun3.setBackground(getDrawable(R.drawable.costom_radiobutton2));
                        break;

                }


            }

        };

        buttun1.setOnClickListener(onClickListener);
        buttun2.setOnClickListener(onClickListener);
        buttun3.setOnClickListener(onClickListener);
    }

}
