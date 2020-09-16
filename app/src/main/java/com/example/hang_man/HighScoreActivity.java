package com.example.hang_man;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.hangman.R;

public class HighScoreActivity extends Activity {
    ListView listview;
    Button homeBtn,retryBtn;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        listview = (ListView)findViewById(R.id.hsa_listview);
        homeBtn = (Button)findViewById(R.id.hsa_homeBtn);
        retryBtn = (Button)findViewById(R.id.hsa_retryBtn);

        dbHelper = new DBHelper(HighScoreActivity.this);
        dbHelper.openDB();

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backhome = new Intent(HighScoreActivity.this,MainActivity.class);
                startActivity(backhome);
            }
        });

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent retry = new Intent(HighScoreActivity.this,GameActivity.class);
                startActivity(retry);
            }
        });

        showScore();
    }

    public void showScore() {
        Cursor cursor = dbHelper.getAllScore();

        HighScoreAdapter adapter = new HighScoreAdapter(HighScoreActivity.this,R.layout.high_score_layout,cursor,0);
        listview.setAdapter(adapter);
    }
}
