package com.nightcoder.dreamhome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.nightcoder.dreamhome.Supports.Constants;
import com.nightcoder.dreamhome.Supports.Prefs;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Prefs.getString(this, Prefs.KEY_USERNAME, null) == null) {
            new Handler().postDelayed(launchSign, 1500);
        } else if (Prefs.getString(this, Prefs.KEY_USERNAME, null).equals(Constants.ADMIN)) {
            new Handler().postDelayed(launchAdmin, 1500);
        } else if (Prefs.getInt(this, Prefs.USER_TYPE, Constants.TYPE_USER) == Constants.TYPE_VENDOR){
            new Handler().postDelayed(launchVendor, 1500);
        } else {
            new Handler().postDelayed(launchUser, 1500);
        }
    }

    private final Runnable launchAdmin = () -> {
        startActivity(new Intent(this, AdminActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    };

    private final Runnable launchSign = () -> {
        startActivity(new Intent(this, LogInActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    };
    private final Runnable launchUser = () -> {
        startActivity(new Intent(this, UserActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    };
    private final Runnable launchVendor = () -> {
        startActivity(new Intent(this, VendorActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    };
}