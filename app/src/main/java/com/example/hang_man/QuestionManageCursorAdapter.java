package com.example.hang_man;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.example.hangman.R;

public class QuestionManageCursorAdapter extends ResourceCursorAdapter {
    public QuestionManageCursorAdapter(Context context, int layout, Cursor c,int flags) {
        super(context,layout,c,flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvID = (TextView)view.findViewById(R.id.qe_tvid);
        tvID.setText(cursor.getString(cursor.getColumnIndex(DBHelper.getIdQuestion())));

        TextView tvWord = (TextView)view.findViewById(R.id.qe_tvword);
        tvWord.setText(cursor.getString(cursor.getColumnIndex(DBHelper.getWORD())));

        TextView tvHint = (TextView)view.findViewById(R.id.qe_tvhint);
        tvHint.setText(cursor.getString(cursor.getColumnIndex(DBHelper.getHINT())));
    }
}
