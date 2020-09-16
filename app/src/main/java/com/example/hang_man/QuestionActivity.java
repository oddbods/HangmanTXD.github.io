package com.example.hang_man;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hangman.R;

public class QuestionActivity extends Activity {

    int listIndex = 0;
    ListView lvQuestion;
    Button addBtn,backBtn;
    DBHelper dbHelper;

    private String getValue(EditText editText) {
        return editText.getText().toString().trim();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        setTitle("Question Manager");
        addBtn = (Button)findViewById(R.id.qa_addbtn);
        lvQuestion = (ListView)findViewById(R.id.qa_listview);
        backBtn = (Button)findViewById(R.id.qa_backBtn);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        dbHelper = new DBHelper(QuestionActivity.this);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(QuestionActivity.this, AddQuestionActivity.class);
                startActivity(addIntent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backIntent = new Intent(QuestionActivity.this,MainActivity.class);
                startActivity(backIntent);
            }
        });

        lvQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView idHolder = (TextView)view.findViewById(R.id.qe_tvid);
                TextView wordHolder = (TextView)view.findViewById(R.id.qe_tvword);
                TextView hintHolder = (TextView)view.findViewById(R.id.qe_tvhint);
                Intent qeIntent = new Intent(view.getContext(), QuestionEditActivity.class);
                qeIntent.putExtra("id",idHolder.getText().toString());
                qeIntent.putExtra("word",wordHolder.getText().toString());
                qeIntent.putExtra("hint",hintHolder.getText().toString());
                startActivityForResult(qeIntent,0);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        dbHelper.openDB();
        showWord();
        dbHelper.closeDB();
    }

    public void showWord(){
        Cursor cursor = dbHelper.getAllQuestion();

        QuestionManageCursorAdapter adapter = new QuestionManageCursorAdapter(QuestionActivity.this,R.layout.activity_question_layout,cursor,0);
        lvQuestion.setAdapter(adapter);
    }
}
