package com.scriptfloor.scanvix.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.scriptfloor.scanvix.model.AnswerModel;
import com.scriptfloor.scanvix.model.QuestionItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by LINCOLN on 3/30/2019.
 */

public class DBHandler extends SQLiteOpenHelper {

    private static final String TAG = DBHandler.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "scanvix_db";

    // Table names
    private static final String TABLE_TEMP = "temp";
    private static final String TABLE_ANS = "answers";

    //Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_QN = "question";
    private static final String KEY_ANS = "answer";
    private static final String KEY_CREATED_AT = "created_at";

    //answer table columns
    private static final String KEY_ANS_ID = "id";
    private static final String KEY_ANS_HIGHEST = "highest";
    private static final String KEY_ANS_HIGHER = "higher";
    private static final String KEY_ANS_HIGH = "high";
    private static final String KEY_ANS_LOWER = "lower";
    private static final String KEY_ANS_LOWEST = "lowest";
    private static final String KEY_ANS_CREATED_AT = "created_at";

    private String CREATE_TEMP_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TEMP + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_QN + " TEXT,"
            + KEY_ANS + " TEXT,"
            + KEY_CREATED_AT + " TEXT" + ")";

    private String CREATE_ANS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ANS + "("
            + KEY_ANS_ID + " INTEGER PRIMARY KEY,"
            + KEY_ANS_HIGHEST + " INTEGER,"
            + KEY_ANS_HIGHER + " INTEGER,"
            + KEY_ANS_HIGH + " INTEGER,"
            + KEY_ANS_LOWER + " INTEGER,"
            + KEY_ANS_LOWEST + " INTEGER,"
            + KEY_ANS_CREATED_AT + " DATE" + ")";


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TEMP_TABLE);
        db.execSQL(CREATE_ANS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_TEMP));
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_ANS));
        onCreate(db);
    }

    public void addQuestion(String qn, String ans, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_QN, qn); // Question
        values.put(KEY_ANS, ans); // answer
        values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        long id = db.insert(TABLE_TEMP, null, values);
        db.close(); // Closing database connection
    }

    public void addAnswer(int highest, int higher,int high,int lower,int lowest) {

        String created_at= new SimpleDateFormat("MMM,dd HH:mm").format(new Date());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ANS_HIGHEST, highest); // highest
        values.put(KEY_ANS_HIGHER, higher); // higher
        values.put(KEY_ANS_HIGH, high); // high
        values.put(KEY_ANS_LOWER, lower); // high
        values.put(KEY_ANS_LOWEST, lowest); // answer
        values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        long id = db.insert(TABLE_ANS, null, values);
        db.close(); // Closing database connection
    }

    public void createTempTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(CREATE_TEMP_TABLE);
        db.close();
    }

    public void dropTempTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_TEMP));
        onCreate(db);
        db.close();
    }

    public ArrayList<QuestionItem> getQuestionItems() {
        ArrayList<QuestionItem> QuestionItems = new ArrayList<>();
        try {
            String query = "select * from " + TABLE_TEMP;
            Cursor cursor;
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery(query, null);

            if (cursor != null) {
                //if cursor contains results
                if (cursor.moveToFirst()) {
                    do {
                        QuestionItem questionItem = new QuestionItem();
                        questionItem.setId(cursor.getString(0));
                        questionItem.setQuestion(cursor.getString(1));
                        questionItem.setAnswer(cursor.getString(2));
                        QuestionItems.add(questionItem);
                    } while (cursor.moveToNext());
                }
            }

            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return QuestionItems;
    }

    public ArrayList<AnswerModel> getAnswers() {
        ArrayList<AnswerModel> answerModels = new ArrayList<>();
        try {
            String query = "select * from " + TABLE_ANS;
            Cursor cursor;
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery(query, null);

            if (cursor != null) {
                //if cursor contains results
                if (cursor.moveToFirst()) {
                    do {
                        AnswerModel answerModel = new AnswerModel();
                        answerModel.setId(cursor.getInt(0));
                        answerModel.setHighest(cursor.getInt(1));
                        answerModel.setHigher(cursor.getInt(2));
                        answerModel.setHigh(cursor.getInt(3));
                        answerModel.setLower(cursor.getInt(4));
                        answerModel.setLowest(cursor.getInt(5));
                        answerModel.setDate_added(cursor.getString(6));
                        answerModels.add(answerModel);
                    } while (cursor.moveToNext());
                }
            }

            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return answerModels;
    }
}
