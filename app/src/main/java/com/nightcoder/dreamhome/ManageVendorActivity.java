package com.nightcoder.dreamhome;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.nightcoder.dreamhome.DataSupports.DBHelper;
import com.nightcoder.dreamhome.Models.Vendor;
import com.nightcoder.dreamhome.Supports.Constants;
import com.nightcoder.dreamhome.Supports.Prefs;
import com.nightcoder.dreamhome.databinding.ActivityManageVendoBinding;
import com.nightcoder.dreamhome.databinding.ManageVendorBinding;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ManageVendorActivity extends AppCompatActivity {

    private ActivityManageVendoBinding binding;
    public static Vendor vendor;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_vendo);
        init();
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        binding.title.setText(vendor.title);
        binding.caption.setText(vendor.description);
        binding.website.setText(vendor.website);
        binding.address.setText(vendor.address + ", " + vendor.pincode);
        binding.number.setText(vendor.number);
        binding.email.setText(vendor.email);

        dbHelper = new DBHelper(this);

        Picasso.get().load(new File(vendor.imageUri)).into(binding.logo);
        Picasso.get().load(new File(vendor.banner)).into(binding.banner);

        binding.manage.setOnClickListener(v -> manage());
        binding.edit.setOnClickListener(v -> {
            EditVendorActivity.vendor = vendor;
            startActivity(new Intent(ManageVendorActivity.this, EditVendorActivity.class));
        });

        if (Prefs.getString(this, Prefs.KEY_USERNAME, null).equals(Constants.ADMIN)) {
            binding.manage.setVisibility(View.VISIBLE);
            binding.edit.setVisibility(View.VISIBLE);
        } else {
            binding.manage.setVisibility(View.GONE);
            binding.edit.setVisibility(View.GONE);
        }

        if (Prefs.getInt(this, Prefs.USER_TYPE, Constants.TYPE_USER) == Constants.TYPE_VENDOR) {
            binding.edit.setVisibility(View.VISIBLE);
            binding.logOut.setVisibility(View.VISIBLE);
        }

        binding.logOut.setOnClickListener(v -> logOut());
    }

    private void logOut() {
        Prefs.putString(this, Prefs.KEY_USERNAME, null);
        startActivity(new Intent(this, LogInActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    private void manage() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        ManageVendorBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.manage_vendor, null, false);
        dialog.setContentView(binding.getRoot());
        binding.cancel.setOnClickListener(v -> dialog.cancel());
        binding.group.check(R.id.active);
        binding.group.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.active) {
                binding.reason.setVisibility(View.GONE);
            } else {
                binding.reason.setVisibility(View.VISIBLE);
            }
        });
        binding.save.setOnClickListener(v -> {
            if (binding.group.getCheckedRadioButtonId() == R.id.active) {
                dbHelper.updateVendorStatus(vendor.email, Constants.DEACTIVATE, null);
                Toast.makeText(this, "Vendor visible for users", Toast.LENGTH_SHORT).show();
            } else if (!binding.reason.getText().toString().isEmpty()) {
                dbHelper.updateVendorStatus(vendor.email, Constants.DEACTIVATE, binding.reason.getText().toString());
                Toast.makeText(this, "Vendor invisible for users", Toast.LENGTH_SHORT).show();
            }
            dialog.cancel();
        });

        dialog.show();
    }
}