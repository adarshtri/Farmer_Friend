package com.thenewboston.kirusa_test.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.thenewboston.kirusa_test.Fragments.Officer;
import com.thenewboston.kirusa_test.PojoClasses.message_object;
import com.thenewboston.kirusa_test.PojoClasses.officers_group;
import java.util.ArrayList;

/**
 * Created by adarsh on 21/9/16.
 */

public class Officer_Group_DbOperation extends SQLiteOpenHelper {

    Context ctx;
    Group_Const group_const = new Group_Const();
    Group_Const_Message message_table = new Group_Const_Message();
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "groups";
    private String create_group_table = "CREATE TABLE "+group_const.getTable_name()+" ("+
            group_const.getGroup_id()+" VARCHAR(10),"+
            group_const.getGroup_name()+" VARCHAR(100));";
    private String create_group_message_table = "CREATE TABLE "+message_table.getTable_name()+" ("+
            message_table.getGroup_id()+" VARCHAR(10),"+
            message_table.getMessage()+" VARCHAR(1000));";

    public Officer_Group_DbOperation(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.ctx=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(create_group_table);
        sqLiteDatabase.execSQL(create_group_message_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void insert_group(Officer_Group_DbOperation dop, officers_group og){
        Log.e("ADARSH",og.getGroup_id()+" "+og.getGroup_name());
        SQLiteDatabase sqLiteDatabase = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(group_const.getGroup_id(),og.getGroup_id());
        cv.put(group_const.getGroup_name(),og.getGroup_name());
        sqLiteDatabase.insert(group_const.getTable_name(),null,cv);
    }
    public void insert_message(Officer_Group_DbOperation dop, message_object m){
        Log.e("ADARSH",m.getGroup_id()+" "+m.getMessage());
        SQLiteDatabase sqLiteDatabase = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(message_table.getGroup_id(),m.getGroup_id());
        cv.put(message_table.getMessage(),m.getMessage());
        sqLiteDatabase.insert(message_table.getTable_name(),null,cv);
    }
    public ArrayList<message_object> read_by_group_id(Officer_Group_DbOperation dop,String group_id){

        ArrayList<message_object> message_objects = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = dop.getReadableDatabase();
        String query = "select * from "+message_table.getTable_name()+" where "+message_table.getGroup_id()+" = "+group_id+" ;";
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        if(cursor!=null&&cursor.moveToFirst()){
            do{
                message_object  mo = new message_object();
                mo.setGroup_id(cursor.getString(cursor.getColumnIndex(message_table.getGroup_id())));
                mo.setMessage(cursor.getString(cursor.getColumnIndex(message_table.getMessage())));
                message_objects.add(mo);
            }while (cursor.moveToNext());
        }
        return message_objects;
    }
    public ArrayList<message_object> read_message(Officer_Group_DbOperation dop){
        SQLiteDatabase sqLiteDatabase =dop.getReadableDatabase();
        ArrayList<message_object> messages = new ArrayList<>();
        String col[] ={
                message_table.getGroup_id(),
                message_table.getMessage()
        };
        Cursor cursor = sqLiteDatabase.query(message_table.getTable_name(),col,null,null,null,null,null);
        if(cursor!=null&&cursor.moveToNext()){
            do {
                message_object mo = new message_object();
                mo.setGroup_id(cursor.getString(cursor.getColumnIndex(message_table.getGroup_id())));
                mo.setMessage(cursor.getString(cursor.getColumnIndex(message_table.getMessage())));
                messages.add(mo);
            }while (cursor.moveToNext());
        }
        return messages;
    }

    public ArrayList<officers_group> read_group(Officer_Group_DbOperation dop){
        SQLiteDatabase sqLiteDatabase = dop.getReadableDatabase();
        ArrayList<officers_group> group = new ArrayList<>();
        String col[] = {
                group_const.getGroup_id(),
                group_const.getGroup_name()
        };
        Cursor cursor = sqLiteDatabase.query(group_const.getTable_name(), col, null, null, null, null, null);
        cursor.moveToFirst();
        if(cursor!=null&&cursor.moveToFirst()){
            do{
                officers_group og = new officers_group();
                og.setGroup_id(cursor.getString(cursor.getColumnIndex(group_const.getGroup_id())));
                og.setGroup_name(cursor.getString(cursor.getColumnIndex(group_const.getGroup_name())));
                group.add(og);
            }while(cursor.moveToNext());
        }
        return group;
    }

    public class Group_Const{
        String group_id = "group_id";
        String group_name = "group_name";
        String table_name = "group_table";
        public Group_Const(){

        }
        public String getGroup_id() {
            return group_id;
        }
        public String getGroup_name() {
            return group_name;
        }
        public String getTable_name() {
            return table_name;
        }
    }
    public class Group_Const_Message{
        String message = "message";
        String group_id = "group_id";
        String table_name = "group_message";

        public Group_Const_Message(){

        }
        public String getMessage() {
            return message;
        }

        public String getGroup_id() {
            return group_id;
        }

        public String getTable_name() {
            return table_name;
        }
    }
}
