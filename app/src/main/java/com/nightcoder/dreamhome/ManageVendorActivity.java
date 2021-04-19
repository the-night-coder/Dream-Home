package com.nightcoder.dreamhome;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.nightcoder.dreamhome.Models.Vendor;
import com.nightcoder.dreamhome.databinding.ActivityManageVendoBinding;
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
    }
}