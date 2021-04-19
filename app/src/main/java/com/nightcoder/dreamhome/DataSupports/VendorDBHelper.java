package com.nightcoder.dreamhome.DataSupports;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.nightcoder.dreamhome.Models.Vendor;
import com.nightcoder.dreamhome.Supports.Constants;
import com.nightcoder.dreamhome.Supports.Tables;


public class VendorDBHelper extends SQLiteOpenHelper {

    public VendorDBHelper(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.VENDOR_DETAILS
                + " (email TEXT PRIMARY KEY UNIQUE, " +
                "password TEXT, title TEXT, description TEXT, " +
                "address TEXT, pincode TEXT, number TEXT, website TEXT, photoUrl TEXT, banner TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tables.VENDOR_DETAILS);
        onCreate(db);
    }


    public void insertVendor(Vendor vendor) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("INSERT OR REPLACE INTO " + Tables.VENDOR_DETAILS + " VALUES ('" + vendor.email + "','" + vendor.password + "','"
                + vendor.title + "','" + vendor.description + "','" + vendor.address + "','" + vendor.pincode + "','" +
                vendor.number + "','" + vendor.website + "','" + vendor.imageUri + "','" + vendor.banner + "')");
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
                vendor.banner = cursor.getString(cursor.getColumnIndex("banner"));
                cursor.close();
                return vendor;
            } else return null;
        } catch (SQLiteException e) {
            return null;
        }
    }
}
