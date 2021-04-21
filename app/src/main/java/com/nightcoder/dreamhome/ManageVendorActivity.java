package com.nightcoder.dreamhome;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.nightcoder.dreamhome.Models.Vendor;
import com.nightcoder.dreamhome.databinding.ActivityManageVendoBinding;
import com.nightcoder.dreamhome.databinding.ManageVendorBinding;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ManageVendorActivity extends AppCompatActivity {

    private ActivityManageVendoBinding binding;
    public static Vendor vendor;

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

        Picasso.get().load(new File(vendor.imageUri)).into(binding.logo);
        Picasso.get().load(new File(vendor.banner)).into(binding.banner);

        binding.manage.setOnClickListener(v -> manage());
        binding.edit.setOnClickListener(v -> {
            EditVendorActivity.vendor = vendor;
            startActivity(new Intent(ManageVendorActivity.this, EditVendorActivity.class));
        });
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

        });

        dialog.show();
    }
}