package com.example.hang_man;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.*;
import android.database.Cursor;

import com.example.hangman.R;

public class AddQuestionActivity extends Activity {
    DBHelper dbHelper;
    Button addBtn,backBtn;
    EditText tvWord,tvHint,tvID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        addBtn = (Button)findViewById(R.id.aqa_addBtn);
        backBtn = (Button)findViewById(R.id.aqa_backBtn);
        tvWord = (EditText) findViewById(R.id.aqa_word);
        tvHint = (EditText) findViewById(R.id.aqa_hint);
        dbHelper = new DBHelper(AddQuestionActivity.this);
        dbHelper.openDB();

        tvWord.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long resultAdd = dbHelper.InsertQuestion(tvWord.getText().toString(),tvHint.getText().toString());
                if (resultAdd==-1){
                    Toast.makeText(AddQuestionActivity.this,"ERROR",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(AddQuestionActivity.this,"SUCCESS",Toast.LENGTH_SHORT).show();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backQA = new Intent(AddQuestionActivity.this,QuestionActivity.class);
                startActivity(backQA);
            }
        });
    }
}
