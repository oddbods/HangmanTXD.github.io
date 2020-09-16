package com.example.hang_man;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DBName = "qm.db";
    private static final int VERSION = 1;

    private static final String QUESTION_TABLE = "QuestMan";
    private static final String ID_QUESTION = "_id";
    private static final String WORD = "word";
    private static final String HINT = "hint";

    private static final String SCORE_TABLE = "HighScore";
    private static final String ID_SCORE = "_id";
    private static final String NAME = "Name";
    private static final String SCORE = "Score";

    private SQLiteDatabase QM;

    public DBHelper(Context context){
        super(context,DBName,null,VERSION);
    }

    public static String getIdQuestion() {
        return ID_QUESTION;
    }

    public static String getWORD() {
        return WORD;
    }

    public static String getHINT() {
        return HINT;
    }

    public static String getIdScore() { return ID_SCORE; }

    public static String getNAME() { return NAME; }

    public static String getSCORE() { return SCORE; }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createQuestionTableQuery
                = "CREATE TABLE IF NOT EXISTS " + QUESTION_TABLE + "( " + ID_QUESTION + " INTEGER PRIMARY KEY AUTOINCREMENT, " + WORD + " TEXT NOT NULL, " + HINT + " TEXT NOT NULL" + ")";
        sqLiteDatabase.execSQL(createQuestionTableQuery);
        initQuestionData(sqLiteDatabase);
        String createScoreTableQuery
                = "CREATE TABLE IF NOT EXISTS " + SCORE_TABLE + "( " + ID_SCORE + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT NOT NULL, " + SCORE + " INTEGER NOT NULL" + ")";
        sqLiteDatabase.execSQL(createScoreTableQuery);
    }

    public void initQuestionData(SQLiteDatabase sqLiteDatabase) {
        String createQuestion = "INSERT INTO " + QUESTION_TABLE + "(word, hint) VALUES('SCHOOL', 'Trường học')," +
                "('UNIVERSITY', '... of transport and communication')," +
                "('ELEVEN', 'Nine, Ten, ..., Twelve')," +
                "('RHYTHM', 'Giai điệu')," +
                "('FOURTH', 'First, Second, Third, ...')," +
                "('ANDROID', '... is an open source operating system')," +
                "('LINUX', '... is an open source operating system')," +
                "('WINDOWS', 'Most popular operating system')," +
                "('BEAUTIFUL', 'She looks ...'),"+
                "('AMAZING', 'the ... world of Gumball')";
        sqLiteDatabase.execSQL(createQuestion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + QUESTION_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SCORE_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void openDB() {
        QM = getWritableDatabase();
    }

    public void closeDB() {
        if (QM!=null && QM.isOpen()) {
            QM.close();
        }
    }

    public long InsertQuestion(String word,String hint) {
        ContentValues values = new ContentValues();
        values.put(WORD,word);
        values.put(HINT,hint);
        return QM.insert(QUESTION_TABLE,null,values);
    }

    public long InsertScore(String name,int score) {
        ContentValues values = new ContentValues();
        values.put(NAME,name);
        values.put(SCORE,score);
        return QM.insert(SCORE_TABLE,null,values);
    }

    public long UpdateQuestion(int id,String word, String hint) {
        ContentValues values = new ContentValues();
        values.put(ID_QUESTION,id);
        values.put(WORD,word);
        values.put(HINT,hint);
        String where = ID_QUESTION + " = " + id;
        return QM.update(QUESTION_TABLE,values,where,null);
    }

    public long DeleteQuestion(int id) {
        String where = ID_QUESTION + " = " + id;
        return QM.delete(QUESTION_TABLE,where,null);
    }

    public Cursor getAllQuestion() {
        String query = "SELECT * FROM " + QUESTION_TABLE;
        return QM.rawQuery(query,null);
    }

    public Cursor getAllScore() {
        String query = "SELECT * FROM " + SCORE_TABLE + " ORDER BY " + SCORE + " DESC ";
        return QM.rawQuery(query,null);
    }

}
