package com.codepolitan.grammar.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.codepolitan.grammar.entity.Item;

import java.util.ArrayList;
import java.util.List;

public class MyDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "todo_db";
    private static final String TABLE_NAME = "todo_table";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TODO_NAME = "name";
    private static final String COLUMN_TODO_PRIORITY = "priority";


    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TODO_TABLE = "CREATE TABLE " +
                TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_TODO_NAME +
                " TEXT," +
                COLUMN_TODO_PRIORITY +
                " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addItem(Item item) {
        ContentValues values = new ContentValues();
        //put data to values object
        values.put(COLUMN_TODO_NAME, item.getName());
        values.put(COLUMN_TODO_PRIORITY, item.getPriority());
        //create sqlitedatanase object name "db" using getwritedatabase (if want to update or delete)
        SQLiteDatabase db = this.getWritableDatabase();
        // call method insert
        db.insert(TABLE_NAME, null, values);
    }

    public List<Item> getAll() {
        //create string variabel to store sql query
        String sql = "SELECT * FROM " + TABLE_NAME;
        //create SQL Database object name "db" using getReadable (if just wnat to readdata)
        SQLiteDatabase db = this.getReadableDatabase();
        List<Item> itemList = new ArrayList<>();
        //
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor.moveToFirst()){ //if there is data inside a database table
            do {
                int id = Integer.valueOf(cursor.getString(0));
                String name = cursor.getString(1);
                String priority = cursor.getString(2);
                Item item = new Item(id,name, priority);
                itemList.add(item);
                //kalau data setelah baris masih ada, maka akan melakukan proses perulangan ke data selanjutnya
                //
            }while (cursor.moveToNext());
        }
        cursor.close();
        return itemList;
    }
    public void updateItem(Item item) {
        //create contentsvalues object name values
        ContentValues values = new ContentValues();
        //put data into values object
        values.put(COLUMN_TODO_NAME,item.getName());
        values.put(COLUMN_TODO_PRIORITY,item.getPriority());

        //create SQLiteDatabase object
        SQLiteDatabase db = this.getWritableDatabase();
        //call update methor from db object wih these paramas (table_name, id=?, Array of strings)
        db.update(TABLE_NAME,values,COLUMN_ID + "=?",new String[] {
                        String.valueOf(item.getId())
                }
        );
    }

    public void deleteItem(Item item) {

        //create SQLiteDatabase object
        SQLiteDatabase db = this.getWritableDatabase();
        //call deleted method drom SQLiteDatabase class or db object
        db.delete(TABLE_NAME,COLUMN_ID+"=?",new String[]{String.valueOf(item.getId())});
    }
}
