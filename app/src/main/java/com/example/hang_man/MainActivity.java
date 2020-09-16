package com.example.hang_man;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.hangman.R;

/**
 * This is demo code to accompany the Mobiletuts+ tutorial:
 * - Android SDK: Create a Hangman Game
 * 
 * Sue Smith - January 2014
 */

public class MainActivity extends Activity  implements OnClickListener {
	DBHelper dbHelper;
	final Context context = this;

	@Override
	protected void onStart() {
		super.onStart();
		dbHelper.openDB();
	}


	@Override
	protected void onStop() {
		super.onStop();
		dbHelper.closeDB();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button playBtn = (Button)findViewById(R.id.playBtn);
		Button QuestionManBtn = (Button)findViewById(R.id.QuestionManBtn);
		Button highscoreBtn = (Button)findViewById(R.id.ma_highscore);
		playBtn.setOnClickListener(this);
		QuestionManBtn.setOnClickListener(this);
		highscoreBtn.setOnClickListener(this);
		dbHelper = new DBHelper(MainActivity.this);
		dbHelper.openDB();

	}

	@Override
	public void onClick(View view) {
		//handle clicks
		if(view.getId()==R.id.playBtn){
			Intent playIntent = new Intent(this, GameActivity.class);
			this.startActivity(playIntent);
		}
		if(view.getId()==R.id.QuestionManBtn){
			showDialog();
		}
		if(view.getId()==R.id.ma_highscore){
			Intent highScoreIntent = new Intent(this,HighScoreActivity.class);
			this.startActivity(highScoreIntent);
		}
	}

	public void showDialog()
	{
		LayoutInflater li = LayoutInflater.from(context);
		View promptsView = li.inflate(R.layout.pass_promt, null);
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setView(promptsView);

		final EditText userInput = (EditText) promptsView
				.findViewById(R.id.editTextDialogUserInput);


		// set dialog message
		alertDialogBuilder
				.setCancelable(false)
				.setNegativeButton("Go",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								/** DO THE METHOD HERE WHEN PROCEED IS CLICKED*/
								String user_text = (userInput.getText()).toString();

								/** CHECK FOR USER'S INPUT **/
								if (user_text.equals(getString(R.string.cheat_code)))
								{
									Intent qaIntent = new Intent(MainActivity.this,QuestionActivity.class);
									startActivity(qaIntent);
								}
								else{
									Log.d(user_text,"string is empty");
									String message = "The password you have entered is incorrect." + " \n \n" + "Please try again!";
									AlertDialog.Builder builder = new AlertDialog.Builder(context);
									builder.setTitle("Error");
									builder.setMessage(message);
									builder.setPositiveButton("Cancel", null);
									builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int id) {
											showDialog();
										}
									});
									builder.create().show();

								}
							}
						})
				.setPositiveButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								dialog.dismiss();
							}

						}

				);

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}
}
