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
import com.nightcoder.dreamhome.Models.Vendor;
import com.nightcoder.dreamhome.Supports.Constants;
import com.nightcoder.dreamhome.Supports.RealPathUtil;
import com.nightcoder.dreamhome.databinding.ActivityEditVendorBinding;
import com.squareup.picasso.Picasso;

import java.io.File;

public class EditVendorActivity extends AppCompatActivity {

    private String bannerUri;
    private String logoUri;
    private ActivityEditVendorBinding binding;
    private DBHelper dbHelper;
    public static Vendor vendor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_vendor);
        init();
    }

    private void init() {
        binding.addVendor.setOnClickListener(v -> validate());
        binding.logoButton.setOnClickListener(v -> getImage(101));
        binding.bannerButton.setOnClickListener(v -> getImage(100));
        dbHelper = new DBHelper(this);

        binding.title.setText(vendor.title);
        binding.address.setText(vendor.address);
        binding.password.setText(vendor.password);
        binding.ConfirmPass.setText(vendor.password);
        binding.caption.setText(vendor.description);
        binding.pincode.setText(vendor.pincode);
        binding.number.setText(vendor.number);
        binding.email.setText(vendor.email);
        binding.website.setText(vendor.website);

        bannerUri = vendor.banner;
        logoUri = vendor.imageUri;

        Picasso.get().load(new File(logoUri)).into(binding.logo);
        Picasso.get().load(new File(bannerUri)).into(binding.banner);
    }

    private void validate() {
        if (logoUri == null) {
            Toast.makeText(this, "Choose a Logo", Toast.LENGTH_SHORT).show();
        } else if (bannerUri == null) {
            Toast.makeText(this, "Choose a banner", Toast.LENGTH_SHORT).show();
        } else if (!binding.title.getText().toString().matches(Constants.NAME_PATTERN)) {
            Toast.makeText(this, "Provide a valid title", Toast.LENGTH_SHORT).show();
        } else if (binding.caption.getText().toString().isEmpty()) {
            Toast.makeText(this, "Provide a caption", Toast.LENGTH_SHORT).show();
        } else if (binding.address.getText().toString().length() < 10) {
            Toast.makeText(this, "Provide a valid address", Toast.LENGTH_SHORT).show();
        } else if (binding.pincode.getText().toString().length() != 6) {
            Toast.makeText(this, "Provide valid pincode", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.PHONE.matcher(binding.number.getText().toString()).matches()) {
            Toast.makeText(this, "Provide valid phone number", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.getText().toString()).matches()) {
            Toast.makeText(this, "Provide valid email", Toast.LENGTH_SHORT).show();
        } else if (binding.password.getText().toString().length() < 8) {
            Toast.makeText(this, "Password must contains at least 8 character", Toast.LENGTH_SHORT).show();
        } else if (!binding.password.getText().toString().equals(binding.ConfirmPass.getText().toString())) {
            Toast.makeText(this, "Password not matching", Toast.LENGTH_SHORT).show();
        } else {
            createVendor();
        }
    }

    private void createVendor() {
        Vendor vendor = new Vendor();
        vendor.title = binding.title.getText().toString();
        vendor.description = binding.caption.getText().toString();
        vendor.address = binding.address.getText().toString();
        vendor.pincode = binding.pincode.getText().toString();
        vendor.number = binding.number.getText().toString();
        vendor.email = binding.email.getText().toString();
        vendor.password = binding.password.getText().toString();
        vendor.website = binding.website.getText().toString();
        vendor.imageUri = logoUri;
        vendor.banner = bannerUri;
        vendor.status = Constants.ACTIVE;
        User user = new User();
        user.userType = Constants.TYPE_VENDOR;
        user.password = binding.password.getText().toString();
        user.email = binding.email.getText().toString();
        dbHelper.insertUser(user);
        dbHelper.insertVendor(vendor);
        dbHelper.close();
        finish();
    }

    private void getImage(int code) {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            selectImage(code);
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, code);
        }
    }

    private void selectImage(int code) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            Picasso.get().load(uri).into(binding.logo);
            logoUri = RealPathUtil.getRealPath(this, uri);
        } else if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            Picasso.get().load(uri).into(binding.banner);
            bannerUri = RealPathUtil.getRealPath(this, uri);
        }
    }
}