package com.autosoftug.emasks.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.autosoftug.emasks.database.model.Note;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "emask_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(Note.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Note.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertNote(String note) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Note.COLUMN_AMOUNT, note);

        // insert row
        long id = db.insert(Note.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public void insertTxs(JSONArray jsonArray) throws JSONException {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        int length = jsonArray.length();

        for (int i = 0; i < length; i++) {
            JSONObject session_object = jsonArray.getJSONObject(i);

            String txid = session_object.getString("id");
            String time = session_object.getString("time");
            String method = session_object.getString("method");
            String recipient = session_object.getString("receipient");
            String amount = session_object.getString("amount");
            String curreny = session_object.getString("curency");

            ContentValues newValues = new ContentValues();
            newValues.put(Note.COLUMN_ID, txid);
            newValues.put(Note.COLUMN_TIMESTAMP, time);
            newValues.put(Note.COLUMN_METHOD, method);
            newValues.put(Note.COLUMN_RECEIPIENT, recipient);
            newValues.put(Note.COLUMN_AMOUNT, amount);
            newValues.put(Note.COLUMN_CURRENCY, curreny);

            db.insertWithOnConflict(Note.TABLE_NAME, null, newValues, SQLiteDatabase.CONFLICT_REPLACE);
//            db.close();
        }


    }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + Note.TABLE_NAME + " ORDER BY " +
                Note.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)));
                note.setTimestamp(cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP)));
                note.setMethod(cursor.getString(cursor.getColumnIndex(Note.COLUMN_METHOD)));
                note.setReceipient(cursor.getString(cursor.getColumnIndex(Note.COLUMN_RECEIPIENT)));
                note.setAmount(cursor.getString(cursor.getColumnIndex(Note.COLUMN_AMOUNT)));
                note.setCurrency(cursor.getString(cursor.getColumnIndex(Note.COLUMN_CURRENCY)));
                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }

    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + Note.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Note.COLUMN_AMOUNT, note.getAmount());

        // updating row
        return db.update(Note.TABLE_NAME, values, Note.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    public void deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Note.TABLE_NAME, Note.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
//        db.close();
    }
}
