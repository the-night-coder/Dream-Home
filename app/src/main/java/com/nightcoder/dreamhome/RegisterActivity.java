package com.nightcoder.dreamhome;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.nightcoder.dreamhome.DataSupports.DBHelper;
import com.nightcoder.dreamhome.Models.User;
import com.nightcoder.dreamhome.Supports.Constants;
import com.nightcoder.dreamhome.Supports.RealPathUtil;
import com.nightcoder.dreamhome.databinding.ActivityRegisterBinding;
import com.squareup.picasso.Picasso;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private DBHelper dbHelper;
    private String imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        init();
    }

    private void init() {
        binding.join.setOnClickListener(v -> validate());
        binding.logo.setOnClickListener(v -> getImage());
        dbHelper = new DBHelper(this);
    }

    private void validate() {
        if (!binding.fullName.getText().toString().matches(Constants.NAME_PATTERN)) {
            Toast.makeText(this, "Provide valid name", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.getText().toString()).matches()) {
            Toast.makeText(this, "Provide valid email address", Toast.LENGTH_SHORT).show();
        } else if (binding.password.getText().toString().length() < 8) {
            Toast.makeText(this, "Password must contains 8 characters", Toast.LENGTH_SHORT).show();
        } else if (!binding.password.getText().toString().equals(binding.confirmPass.getText().toString())) {
            Toast.makeText(this, "Password doesn't match", Toast.LENGTH_SHORT).show();
        } else if (imageUri == null) {
            Toast.makeText(this, "Choose a profile photo", Toast.LENGTH_SHORT).show();
        } else join();
    }

    private void join() {
        if (dbHelper.getUser(binding.email.getText().toString()) == null) {
            User user = new User();
            user.email = binding.email.getText().toString();
            user.password = binding.password.getText().toString();
            user.fullName = binding.fullName.getText().toString();
            user.photoUrl = imageUri;
            user.userType = Constants.TYPE_USER;
            dbHelper.insertUser(user);
            dbHelper.insertUserDetails(user);
            Toast.makeText(this, "Registered", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show();
        }
    }

    private void getImage() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            Picasso.get().load(uri).into(binding.logo);
            imageUri = RealPathUtil.getRealPath(this, uri);
        }
    }
}