package com.reva.recyclerview;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Base64;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DBHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME="library_manager.db";
    private static final int DATABASE_VERSION=1;

    //Columns of Books Table
    private static String TABLE_BOOKS="my_library";
    private static String COLUMN_ID="id";
    private static String COLUMN_TITLE="book_title";
    private static String COLUMN_AUTHOR="book_author";
    private static String COLUMN_PAGES="book_pages";

    //Columns of Users Table;
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DOB = "dob";
    private static final String COLUMN_PASSWORD="password";
    //Columns of Images Table;
    private static final String TABLE_IMAGES="images";
    private static final String COLUMN_IMAGEID="imageid";
    private static final String COLUMN_IMAGE="image";
    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createBooksTableQuery="CREATE TABLE "+TABLE_BOOKS+" ("+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COLUMN_TITLE+" TEXT, "+COLUMN_AUTHOR+" TEXT, "+COLUMN_PAGES+" INTEGER);";
        db.execSQL(createBooksTableQuery);

        String createUsersTableQuery="CREATE TABLE "+TABLE_USERS+" ("+COLUMN_USERNAME+" TEXT PRIMARY KEY, "+COLUMN_NAME+" TEXT, "+COLUMN_DOB+" TEXT, "+COLUMN_PASSWORD+" TEXT);";
        db.execSQL(createUsersTableQuery);
        String createImagesTableQuery="CREATE TABLE "+TABLE_IMAGES+" ("+COLUMN_IMAGEID+" TEXT PRIMARY KEY, "+COLUMN_IMAGE+" BLOB, FOREIGN KEY("+COLUMN_IMAGEID+") REFERENCES "+TABLE_USERS+"("+COLUMN_USERNAME+") ON DELETE CASCADE);";
        db.execSQL(createImagesTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_BOOKS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_IMAGES);
        onCreate(db);

    }

    public void addBook(String bookTitle, String bookAuthor, int bookPages){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COLUMN_TITLE, bookTitle);
        contentValues.put(COLUMN_AUTHOR, bookAuthor);
        contentValues.put(COLUMN_PAGES, bookPages);
        long result=db.insert(TABLE_BOOKS,null,contentValues);
        if(result==-1){
            Toast.makeText(context, "Failed to Add Book!", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(context, "Book Added!", Toast.LENGTH_LONG).show();
        }
    }

    Cursor readAll(){
        String query="SELECT * FROM "+TABLE_BOOKS;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=null;
        if(db!=null){
            cursor=db.rawQuery(query,null);
        }
        return cursor;
    }

    public boolean insertUserData(String username, String name, String dob, String password){
        SQLiteDatabase myDB=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COLUMN_USERNAME, username);
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_DOB, dob);
        contentValues.put(COLUMN_PASSWORD, password);
        long result=myDB.insert(TABLE_USERS,null,contentValues);
        if(result==-1){
            return false;
        }
        else{
            return true;
        }
    }

    // Method to check if a username already exists
    public boolean checkUserName(String username) {
        SQLiteDatabase myDB = this.getReadableDatabase();
        Cursor cursor = myDB.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=?", new String[]{username});
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    // Method to check if a user with the given username and password exists
    public boolean checkUser(String username, String password) {
        SQLiteDatabase myDB = this.getReadableDatabase();
        Cursor cursor = myDB.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?", new String[]{username, password});
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }
    public String fetchpass(String username) {
        SQLiteDatabase myDB = this.getReadableDatabase();
        String[] projection = {COLUMN_PASSWORD};
        String selection = COLUMN_USERNAME + "=?";
        String[] selectionArgs = {username};
        Cursor cursor = myDB.query(TABLE_USERS, projection, selection, selectionArgs, null, null, null);
        String password = null;
        if (cursor != null && cursor.moveToFirst()) {
            password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
            cursor.close();
        }
        return password;
    }
    // Method to update the password for a given username
    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PASSWORD, newPassword);
        int result = myDB.update(TABLE_USERS, contentValues, COLUMN_USERNAME + "=?", new String[]{username});
        return result > 0;
    }

    // Method to update the name for a given username (assuming you have a name column in your users table)
    public boolean updateName(String username, String newName) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", newName); // Assuming 'name' is the column name for user's name
        int result = myDB.update(TABLE_USERS, contentValues, COLUMN_USERNAME + "=?", new String[]{username});
        return result > 0;
    }

    // Method to update the date of birth for a given username (assuming you have a dob column in your users table)
    public boolean updateDob(String username, String newDob) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("dob", newDob); // Assuming 'dob' is the column name for date of birth
        int result = myDB.update(TABLE_USERS, contentValues, COLUMN_USERNAME + "=?", new String[]{username});
        return result > 0;
    }

    // Method to delete user data for a given username
    public boolean deleteUserData(String username) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        int result = myDB.delete(TABLE_USERS, COLUMN_USERNAME + "=?", new String[]{username});
        return result > 0;
    }
    public boolean insertImageData(String username, Uri imageUri){
        SQLiteDatabase db=this.getWritableDatabase();
        try {
            InputStream inputStream=context.getContentResolver().openInputStream(imageUri);
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
            byte[] buffer=new byte[1024];
            int bytesRead;
            while((bytesRead=inputStream.read(buffer))!=-1){
                byteArrayOutputStream.write(buffer,0,bytesRead);
            }
            byte[] imageInBytes= byteArrayOutputStream.toByteArray();
            ContentValues contentValues=new ContentValues();
            contentValues.put(COLUMN_IMAGEID, username);
            contentValues.put(COLUMN_IMAGE, imageInBytes);
            long result=db.insert(TABLE_IMAGES,null,contentValues);
            return result>0;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }
    public byte[] getImageData(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        byte[] imageData = null;
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_IMAGE + " FROM " + TABLE_IMAGES + " WHERE " + COLUMN_IMAGEID + "=?", new String[]{username});
        if (cursor != null && cursor.moveToFirst()) {
            imageData = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));
            cursor.close();
        }
        return imageData;
    }
}
