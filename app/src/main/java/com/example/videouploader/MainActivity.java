package com.example.videouploader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    //UI views
    FloatingActionButton addVideoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Videos");

        //init Ui Views
        addVideoBtn = findViewById(R.id.addVideoBtn);
    }

    //handle click
    public void addVideoBtn(View view){
        startActivity(new Intent(MainActivity.this, AddVideoActivity.class));

    }
}