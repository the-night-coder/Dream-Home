package com.nightcoder.dreamhome.DataSupports;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.nightcoder.dreamhome.Models.User;
import com.nightcoder.dreamhome.Models.Vendor;
import com.nightcoder.dreamhome.Supports.Constants;
import com.nightcoder.dreamhome.Supports.Tables;


public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.USERS
                + "(email TEXT PRIMARY KEY UNIQUE, password TEXT, userType INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.USER_DETAILS
                + "(email TEXT PRIMARY KEY UNIQUE, password TEXT, fullName TEXT, photoUrl TEXT, userType INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.VENDOR_DETAILS
                + " (email TEXT PRIMARY KEY UNIQUE, " +
                "password TEXT, title TEXT, description TEXT, " +
                "address TEXT, pincode TEXT, number TEXT, website TEXT, photoUrl TEXT, banner TEXT, status INTEGER, extra TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tables.USERS);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.USER_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.VENDOR_DETAILS);
        onCreate(db);
    }

    public User getUserDetails(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + Tables.USER_DETAILS + " WHERE email='" + email + "'", null);
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                User user = new User();
                user.email = cursor.getString(cursor.getColumnIndex("email"));
                user.fullName = cursor.getString(cursor.getColumnIndex("fullName"));
                user.photoUrl = cursor.getString(cursor.getColumnIndex("photoUrl"));
                user.password = cursor.getString(cursor.getColumnIndex("password"));
                user.userType = cursor.getInt(cursor.getColumnIndex("userType"));
                cursor.close();
                return user;
            } else {
                return null;
            }

        } catch (SQLiteException e) {
            return null;
        }
    }

    public void insertUser(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("INSERT OR REPLACE INTO " + Tables.USERS + " VALUES('" + user.email
                + "','" + user.password + "'," + user.userType + ")");
    }

    public void insertUserDetails(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("INSERT OR REPLACE INTO " + Tables.USER_DETAILS
                + " VALUES('" + user.email + "','" + user.password + "','" + user.fullName
                + "','" + user.photoUrl + "'," + user.userType + ")");
    }


    public User getUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + Tables.USERS + " WHERE email='" + email + "'", null);
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                User user = new User();
                user.email = cursor.getString(cursor.getColumnIndex("email"));
                user.password = cursor.getString(cursor.getColumnIndex("password"));
                user.userType = cursor.getInt(cursor.getColumnIndex("userType"));
                cursor.close();
                return user;
            } else {
                return null;
            }
        } catch (SQLiteException e) {
            return null;
        }
    }

    public void insertVendor(Vendor vendor) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("INSERT OR REPLACE INTO " + Tables.VENDOR_DETAILS
                + " VALUES ('" + vendor.email + "','" + vendor.password + "','" + vendor.title
                + "','" + vendor.description + "','" + vendor.address + "','" + vendor.pincode
                + "','" + vendor.number + "','" + vendor.website + "','" + vendor.imageUri + "','"
                + vendor.banner + "'," + vendor.status + ",'" + vendor.extra + "')");
    }

    public Cursor getVendors() {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            return db.rawQuery("SELECT * FROM " + Tables.VENDOR_DETAILS, null);
        } catch (SQLiteException e) {
            return null;
        }
    }

    public Vendor getVendor(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + Tables.VENDOR_DETAILS + " WHERE email='" + email + "'", null);
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                Vendor vendor = new Vendor();
                vendor.imageUri = cursor.getString(cursor.getColumnIndex("photoUrl"));
                vendor.title = cursor.getString(cursor.getColumnIndex("title"));
                vendor.description = cursor.getString(cursor.getColumnIndex("description"));
                vendor.website = cursor.getString(cursor.getColumnIndex("website"));
                vendor.pincode = cursor.getString(cursor.getColumnIndex("pincode"));
                vendor.email = cursor.getString(cursor.getColumnIndex("email"));
                vendor.number = cursor.getString(cursor.getColumnIndex("number"));
                vendor.address = cursor.getString(cursor.getColumnIndex("address"));
                vendor.password = cursor.getString(cursor.getColumnIndex("password"));
                vendor.extra = cursor.getString(cursor.getColumnIndex("extra"));
                vendor.status = cursor.getInt(cursor.getColumnIndex("status"));
                vendor.banner = cursor.getString(cursor.getColumnIndex("banner"));
                cursor.close();
                return vendor;
            } else return null;
        } catch (SQLiteException e) {
            return null;
        }
    }
}
