package com.example.hang_man;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hangman.R;

public class YourScoreActivity extends Activity {
    TextView ysa_tvScore;
    EditText ysa_etName;
    Button ysa_submitBtn;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_score);

        dbHelper = new DBHelper(YourScoreActivity.this);
        dbHelper.openDB();

        Intent intent = getIntent();
        int receivedScore = intent.getIntExtra("score",0);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        ysa_tvScore = (TextView)findViewById(R.id.ysa_tvyourscore);
        ysa_etName = (EditText)findViewById(R.id.ysa_etname);
        ysa_submitBtn = (Button)findViewById(R.id.ysa_applyBtn);

        ysa_tvScore.setText(String.valueOf(receivedScore));

        ysa_submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long resultAdd = dbHelper.InsertScore(ysa_etName.getText().toString(),Integer.parseInt(ysa_tvScore.getText().toString()));
                if (resultAdd==-1){
                    Toast.makeText(YourScoreActivity.this,"ERROR",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent toBoardIntent = new Intent(YourScoreActivity.this,HighScoreActivity.class);
                    startActivity(toBoardIntent);
                }
            }
        });
    }
}
