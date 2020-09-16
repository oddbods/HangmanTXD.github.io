package com.example.hang_man;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.example.hangman.R;

public class HighScoreAdapter extends ResourceCursorAdapter {
    public HighScoreAdapter(Context context, int layout, Cursor c, int flags) {
        super(context,layout,c,flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvScore = (TextView)view.findViewById(R.id.hsl_score);
        tvScore.setText(cursor.getString(cursor.getColumnIndex(DBHelper.getSCORE())));

        TextView tvName = (TextView)view.findViewById(R.id.hsl_name);
        tvName.setText(cursor.getString(cursor.getColumnIndex(DBHelper.getNAME())));
    }
}
