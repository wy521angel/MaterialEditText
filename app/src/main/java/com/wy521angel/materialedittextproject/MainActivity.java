package com.wy521angel.materialedittextproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    MaterialEditText materialEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        materialEditText = findViewById(R.id.edit);
//        materialEditText.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                materialEditText.setUseFloatingLabel(false);
//            }
//        }, 3000);
    }
}
