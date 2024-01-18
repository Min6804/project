package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHandler {

    private SQLiteDatabase database;

    public DataHandler(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public void open() throws SQLException {

    }

    public void close() {
        // dbHelper.close();
    }


    // 데이터 수정
    public int updateData(String originalColumn1Value, String newColumn1Value, String newColumn2Value) {
        ContentValues values = new ContentValues();
        values.put("column1", newColumn1Value);
        values.put("column2", newColumn2Value);
        return database.update("babtable", values, "column1 = ?", new String[]{originalColumn1Value});
    }

    // 데이터 조회
    public Cursor getAllData() {
        return database.query("babtable", null, null, null, null, null, null);
    }

    private static class DBHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "babtable";
        private static final int DATABASE_VERSION = 1;

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            createTable(db);
            insertInitialData(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS babtable");
            onCreate(db);
        }

        private void createTable(SQLiteDatabase db) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS babtable ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "column1 TEXT,"
                    + "column2 TEXT);";

            db.execSQL(createTableQuery);
        }

        private void insertInitialData(SQLiteDatabase db) {
            String[] initialData = {
                    "INSERT INTO babtable (column1, column2) VALUES ('A', '0800');",
                    "INSERT INTO babtable (column1, column2) VALUES ('B', '1230');",
                    "INSERT INTO babtable (column1, column2) VALUES ('C', '1830');"
            };

            for (String query : initialData) {
                db.execSQL(query);
            }
        }
    }
}
