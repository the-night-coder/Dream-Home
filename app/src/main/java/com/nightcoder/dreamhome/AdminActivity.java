package com.nightcoder.dreamhome;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.nightcoder.dreamhome.Adapters.VendorAdapter;
import com.nightcoder.dreamhome.DataSupports.DBHelper;
import com.nightcoder.dreamhome.Supports.Prefs;
import com.nightcoder.dreamhome.databinding.ActivityAdminBinding;

public class AdminActivity extends AppCompatActivity {

    private ActivityAdminBinding binding;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin);
        init();
    }

    private void init() {
        binding.add.setOnClickListener(v -> addVendor());
        dbHelper = new DBHelper(this);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.logOut.setOnClickListener(v -> logOut());
    }

    private void addVendor() {
        startActivity(new Intent(this, AddVendorActivity.class));
    }

    private void setVendors() {
        VendorAdapter adapter = new VendorAdapter(dbHelper.getVendors(), this);
        binding.recyclerView.setAdapter(adapter);
    }

    private void logOut() {
        Prefs.putString(this, Prefs.KEY_USERNAME, null);
        startActivity(new Intent(this, LogInActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVendors();
    }
}