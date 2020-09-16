package com.example.hang_man;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.NavUtils;

import com.example.hangman.R;

	/**
	 * This is demo code to accompany the Mobiletuts+ tutorial:
	 * - Android SDK: Create a Hangman Game
	 *
	 * Sue Smith - January 2014
	 */

	public class GameActivity extends Activity {

	//the words
	private ArrayList<Question> questions;
	//random for word selection
	private Random rand;
	//store the current word
	private String currWord;
	//the layout holding the answer
	private LinearLayout wordLayout;
	//text views for each letter in the answer
	private TextView[] charViews;
	//letter button grid
	private GridView letters;
	//letter button adapter
	private LetterAdapter ltrAdapt;
	//body part images
	private ImageView[] bodyParts;

	private TextView scoreTv;
	private TextView hintTv;
	//total parts
	private int numParts=6;
	//current part
	private int currPart;
	//num chars in word
	private int numChars;
	//num correct so far
	private int numCorr;
	//help
	private AlertDialog helpAlert;
	//score
	public static int score;

	private MediaPlayer mpClap;
	private MediaPlayer mpFail;
	DBHelper dbHelper;

	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		scoreTv = (TextView)findViewById(R.id.ag_score);
		hintTv = (TextView)findViewById(R.id.ag_hint);

		mpClap = MediaPlayer.create(GameActivity.this, R.raw.clap);
		mpFail = MediaPlayer.create(GameActivity.this, R.raw.fail);

		// init questions data
		dbHelper = new DBHelper(GameActivity.this);
		initData();

		//init variables
		rand = new Random();
		currWord = "";
		score = 0;

		wordLayout = (LinearLayout)findViewById(R.id.word);
		letters = (GridView)findViewById(R.id.letters);

		bodyParts = new ImageView[numParts];
		bodyParts[0] = (ImageView)findViewById(R.id.head);
		bodyParts[1] = (ImageView)findViewById(R.id.body);
		bodyParts[2] = (ImageView)findViewById(R.id.leg1);
		bodyParts[3] = (ImageView)findViewById(R.id.leg2);
		bodyParts[4] = (ImageView)findViewById(R.id.arm2);
		bodyParts[5] = (ImageView)findViewById(R.id.arm1);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		playGame();

	}

	private void initData() {
		dbHelper.openDB();

		Cursor cursor = dbHelper.getAllQuestion();
		questions = new ArrayList<Question>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Question question = new Question();
			question.setId(cursor.getColumnIndex(DBHelper.getIdQuestion()));
			question.setHint(cursor.getString(cursor.getColumnIndex(DBHelper.getHINT())));
			question.setWord(cursor.getString(cursor.getColumnIndex(DBHelper.getWORD())));
			questions.add(question);
			cursor.moveToNext();
		}

		dbHelper.closeDB();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_help:
			showHelp();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	//play a new game
	private void playGame(){
		scoreTv.setText(Integer.toString(score));
		//choose a question
		int randomNumber = rand.nextInt(questions.size());
		Question question = questions.get(randomNumber);

		//update current word
		currWord = question.getWord();
		questions.remove(randomNumber);
		hintTv.setText(question.getHint());
		//create new array for character text views
		charViews = new TextView[currWord.length()];

		//remove any existing letters
		wordLayout.removeAllViews();

		//loop through characters
		for(int c=0; c<currWord.length(); c++){
			charViews[c] = new TextView(this);
			//set the current letter
			charViews[c].setText(""+currWord.charAt(c));
			//set layout
			charViews[c].setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, 
					LayoutParams.WRAP_CONTENT));
			charViews[c].setGravity(Gravity.CENTER);
			charViews[c].setTextColor(Color.WHITE);
			charViews[c].setBackgroundResource(R.drawable.letter_bg);
			//add to display
			wordLayout.addView(charViews[c]);
		}

		//reset adapter
		ltrAdapt=new LetterAdapter(this);
		letters.setAdapter(ltrAdapt);

		//start part at zero
		currPart=0;
		//set word length and correct choices
		numChars=currWord.length();
		numCorr=0;

		//hide all parts
		for(int p=0; p<numParts; p++){
			bodyParts[p].setVisibility(View.INVISIBLE);
		}
	}

	//letter pressed method
	public void letterPressed(View view){
		//find out which letter was pressed
		String ltr=((TextView)view).getText().toString();
		char letterChar = ltr.charAt(0);
		//disable view
		view.setEnabled(false);
		view.setBackgroundResource(R.drawable.letter_down);
		//check if correct
		boolean correct=false;
		for(int k=0; k<currWord.length(); k++){ 
			if(currWord.charAt(k)==letterChar){
				correct=true;
				numCorr++;
				charViews[k].setTextColor(Color.BLACK);
			}
		}
		//check in case won
		if(correct){
			if(numCorr==numChars){
				mpClap.start();
				score++;
				//disable all buttons
				disableBtns();
				//let user know they have won, ask if they want to play again
				AlertDialog.Builder winBuild = new AlertDialog.Builder(this);
				winBuild.setTitle("YAY");
				winBuild.setMessage("You answered correctly!\n\nThe answer was:\n\n"+currWord);
				if (questions.size() != 0) {
					winBuild.setPositiveButton("Next question",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									GameActivity.this.playGame();
								}});
				} else {
					winBuild.setPositiveButton("To score board",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									mpRelease();
									GameActivity.this.finish();
									Intent yourScoreActivity = new Intent(GameActivity.this,YourScoreActivity.class);
									yourScoreActivity.putExtra("score",score);
									startActivity(yourScoreActivity);
								}});
				}
				winBuild.setNegativeButton("Exit", 
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						mpRelease();
						GameActivity.this.finish();
					}});
				winBuild.show();
			}
		}
		//check if user still has guesses
		else if(currPart<numParts){
			//show next part
			bodyParts[currPart].setVisibility(View.VISIBLE);
			currPart++;
		}
		else{
			mpFail.start();
			//user has lost
			disableBtns();
			//let the user know they lost, ask if they want to play again
			AlertDialog.Builder loseBuild = new AlertDialog.Builder(this);
			loseBuild.setTitle("OOPS");
			loseBuild.setMessage("You lose!\n\nThe answer was:\n\n"+currWord);
			loseBuild.setPositiveButton("Play Again", 
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					initData();
					GameActivity.this.playGame();
				}});
			loseBuild.setNegativeButton("Exit", 
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					mpRelease();
					GameActivity.this.finish();
				}});
			if (score > 0) {
				loseBuild.setNeutralButton("To score board", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						mpRelease();
						GameActivity.this.finish();
						Intent yourScoreActivity = new Intent(GameActivity.this,YourScoreActivity.class);
						yourScoreActivity.putExtra("score",score);
						startActivity(yourScoreActivity);
					}});
			}
			loseBuild.show();
		}
	}

	//disable letter buttons
	public void disableBtns(){	
		int numLetters = letters.getChildCount();
		for(int l=0; l<numLetters; l++){
			letters.getChildAt(l).setEnabled(false);
		}
	}

	private void mpRelease() {
		mpClap.release();
		mpFail.release();
	}

	//show help information
	public void showHelp(){
		AlertDialog.Builder helpBuild = new AlertDialog.Builder(this);
		helpBuild.setTitle("Help");
		helpBuild.setMessage("Guess the word by selecting the letters.\n\n"
				+ "You only have 6 wrong selections then it's game over!");
		helpBuild.setPositiveButton("OK", 
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				helpAlert.dismiss();
			}});
		helpAlert = helpBuild.create();
		helpBuild.show();
	}
}
