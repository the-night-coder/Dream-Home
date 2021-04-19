package com.nightcoder.dreamhome;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nightcoder.dreamhome.DataSupports.DBHelper;
import com.nightcoder.dreamhome.Models.User;
import com.nightcoder.dreamhome.Supports.Constants;
import com.nightcoder.dreamhome.Supports.Prefs;
import com.nightcoder.dreamhome.databinding.ActivityLogInBinding;

public class LogInActivity extends AppCompatActivity {

    private ActivityLogInBinding binding;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        binding.signIn.setOnClickListener(v -> validate());
        dbHelper = new DBHelper(this);
    }

    private void validate() {
        if (!binding.email.getText().toString().contains("@")) {
            Toast.makeText(this, "Provide valid email", Toast.LENGTH_SHORT).show();
        } else if (binding.password.getText().toString().isEmpty()) {
            Toast.makeText(this, "Provide password", Toast.LENGTH_SHORT).show();
        } else {
            signIn();
        }
    }

    private void signIn() {
        if (binding.email.getText().toString().equals(Constants.ADMIN)) {
            if (binding.password.getText().toString().equals(Constants.ADMIN_PASS)) {
                startActivity(new Intent(this, AdminActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                Prefs.putString(this, Prefs.KEY_USERNAME, Constants.ADMIN);
                finish();
            } else {
                Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
            }
        } else if (dbHelper.getUser(binding.email.getText().toString()) == null) {
            Toast.makeText(this, "User doesn't exist !", Toast.LENGTH_SHORT).show();
        } else {
            User user = dbHelper.getUser(binding.email.getText().toString());
            if (user.password.equals(binding.password.getText().toString())) {
                if (user.userType == Constants.TYPE_USER) {
                    startActivity(new Intent(this, UserActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    startActivity(new Intent(this, VendorActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                }
                Prefs.putString(this, Prefs.KEY_USERNAME, user.email);
                Prefs.putInt(this, Prefs.USER_TYPE, user.userType);
                finish();
            }
        }
    }
}