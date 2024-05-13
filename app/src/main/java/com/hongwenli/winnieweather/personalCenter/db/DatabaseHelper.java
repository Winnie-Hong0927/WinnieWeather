package com.hongwenli.winnieweather.personalCenter.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hongwenli.winnieweather.personalCenter.bean.Person;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "user_db";
    private static final int DATABASE_VERSION = 1;

    // 表格名称和列名
    public static final String TABLE_NAME = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_DESC = "desc";
    private static DatabaseHelper databaseHelper;
    public static SQLiteDatabase mRDB;//读
    public static SQLiteDatabase mWDB;//写

    // 创建表格的 SQL 语句
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " VARCHAR NOT NULL UNIQUE, " +
                    COLUMN_PASSWORD + " VARCHAR NOT NULL," +
                    COLUMN_NAME + " VARCHAR NOT NULL,"+
                    COLUMN_AGE + " INTEGER NOT NULL,"+
                    COLUMN_DESC+" VARCHAR NOT NULL"+
                    ")";
    //这里出错简直是编译器抽疯了
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //    使用单例模式
    public static DatabaseHelper getInstance(Context context){
        if(databaseHelper==null){
            databaseHelper = new DatabaseHelper(context);
        }
        return databaseHelper;
    }

    public SQLiteDatabase openReadLink( ){
        if(mRDB==null||!mRDB.isOpen()){
            mRDB = databaseHelper.getReadableDatabase();
        }
        return mRDB;
    }

    public SQLiteDatabase openWriteLink(){
        if(mWDB==null||!mWDB.isOpen()){
            mWDB = databaseHelper.getWritableDatabase();
        }
        return mWDB;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        Log.d("TAG", "onCreate: 创建新表成功~~~");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 升级数据库版本时的操作，这里简单示例，直接删除旧表格并创建新表格
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // 查询指定用户名是否存在于数据库中
    public boolean checkUserExists(String username) {
        boolean exists = false;
        Cursor cursor = mRDB.query(TABLE_NAME,null,"username=?",new String[]{username},null,null,null);
        if(cursor.moveToNext()){
            exists = true;
        }
        return exists;
    }
    public boolean checkUserExists(String username,String password) {
        boolean exists = false;
        Cursor cursor = mRDB.query(TABLE_NAME,null,"username=? and password=?",
                new String[]{username,password},null,null,null);
        if(cursor.moveToNext()){
            exists = true;
        }
        return exists;
    }

    // 添加用户到数据库
    public long insert(Person person) {
        long id = -1;
        String username = person.getUsername();
        String password = person.getPassword();
        int age = person.getAge();
        String name = person.getName();
        String desc = person.getDesc();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME,username);
        values.put(COLUMN_PASSWORD,password);
        values.put(COLUMN_NAME,name);
        values.put(COLUMN_AGE,age);
        values.put(COLUMN_DESC,desc);
        mWDB.insert(TABLE_NAME,null,values);
        return id;
    }

    // 打印数据库中所有数据
    public void printAllUsers() {
        List<Person> list = new ArrayList<>();
        Cursor cursor = mRDB.query(TABLE_NAME,null,"1=1",null,null,null,null);
        while (cursor.moveToNext()){
            Person person;
            int usernameIndex = cursor.getColumnIndex(COLUMN_USERNAME);
            String username = cursor.getString(usernameIndex);
            int pswIndex = cursor.getColumnIndex(COLUMN_PASSWORD);
            String password = cursor.getString(pswIndex);
            int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
            String name = cursor.getString(nameIndex);
            int ageIndex = cursor.getColumnIndex(COLUMN_AGE);
            int age = cursor.getInt(ageIndex);
            int descIndex = cursor.getColumnIndex(COLUMN_DESC);
            String desc = cursor.getString(descIndex);
            person = new Person(username,name,age,desc,password);
            list.add(person);
        }
        for (Person person : list) {
            System.out.println(person);
        }
    }

    public void closeDB(){
        if(mRDB!=null&& mRDB.isOpen()) mRDB.close();
        if(mWDB!=null&& mWDB.isOpen()) mWDB.close();
        mRDB = null;
        mWDB = null;
    }

    public List<String> getColumnNames() {
        List<String> columnNames = new ArrayList<>();
        Cursor cursor = mRDB.rawQuery("PRAGMA table_info(" + TABLE_NAME + ")", null);
        if (cursor != null) {
            try {
                int nameIndex = cursor.getColumnIndex("name");
                while (cursor.moveToNext()) {
                    String columnName = cursor.getString(nameIndex);
                    columnNames.add(columnName);
                }
            } finally {
                cursor.close();
            }
        }
        return columnNames;
    }
    public void dropTable() {
        mRDB.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        System.out.println("删除表");
    }
}
