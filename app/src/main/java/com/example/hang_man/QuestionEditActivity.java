package com.example.hang_man;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hangman.R;

public class QuestionEditActivity extends Activity {
    Button backbtn,delbtn,applybtn;
    EditText qea_etid,qea_etword,qea_ethint;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_edit);

        backbtn = (Button)findViewById(R.id.qea_backbtn);
        delbtn = (Button)findViewById(R.id.qea_delbtn);
        applybtn = (Button)findViewById(R.id.qea_applybtn);

        qea_etid = (EditText)findViewById(R.id.qea_etid);
        qea_etword = (EditText)findViewById(R.id.qea_etword);
        qea_ethint = (EditText)findViewById(R.id.qea_ethint);

        qea_etid.setEnabled(false);
        dbHelper = new DBHelper(QuestionEditActivity.this);
        dbHelper.openDB();

        Intent intent = getIntent();

        qea_etword.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        String receivedID = intent.getStringExtra("id");
        String receivedWord = intent.getStringExtra("word");
        final String receivedHint = intent.getStringExtra("hint");

        qea_etid.setText(receivedID);
        qea_etword.setText(receivedWord);
        qea_ethint.setText(receivedHint);

        qea_etid.setHint(receivedID);
        qea_etword.setHint(receivedWord);
        qea_ethint.setHint(receivedHint);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent QEAtoQAintent = new Intent(QuestionEditActivity.this,QuestionActivity.class);
                startActivity(QEAtoQAintent);
            }
        });

        applybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long resultUpdate = dbHelper.UpdateQuestion(Integer.parseInt(qea_etid.getText().toString()),qea_etword.getText().toString(),qea_ethint.getText().toString());
                if (resultUpdate == 0) {
                    Toast.makeText(QuestionEditActivity.this,"ERROR",Toast.LENGTH_SHORT).show();
                }
                else if (resultUpdate == 1) {
                    Toast.makeText(QuestionEditActivity.this,"SUCCESS",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(QuestionEditActivity.this,"ERROR",Toast.LENGTH_SHORT).show();
                }
            }
        });

        delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long resultDelete = dbHelper.DeleteQuestion(Integer.parseInt(qea_etid.getText().toString()));
                if (resultDelete == 0) {
                    Toast.makeText(QuestionEditActivity.this,"ERROR",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(QuestionEditActivity.this,"DELETED SUCCESSFULLY",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
