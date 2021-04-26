package com.nightcoder.dreamhome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nightcoder.dreamhome.Adapters.ProductAdapter;
import com.nightcoder.dreamhome.DataSupports.DBHelper;
import com.nightcoder.dreamhome.Models.Vendor;
import com.nightcoder.dreamhome.Supports.Prefs;
import com.nightcoder.dreamhome.databinding.ActivityVendorBinding;
import com.squareup.picasso.Picasso;

import java.io.File;

public class VendorActivity extends AppCompatActivity {

    private ActivityVendorBinding binding;
    private Vendor vendor;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vendor);
        init();
    }

    private void init() {
        binding.add.setOnClickListener(v -> addProduct());
        dbHelper = new DBHelper(this);
        vendor = dbHelper.getVendor(Prefs.getString(this, Prefs.KEY_USERNAME, null));
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Picasso.get().load(new File(vendor.imageUri)).into(binding.profile);

        binding.profile.setOnClickListener(v -> {
            ManageVendorActivity.vendor = vendor;
            startActivity(new Intent(this, ManageVendorActivity.class));
        });
        binding.list.setOnClickListener(v -> startActivity(new Intent(this, OrderActivity.class)));
    }

    private void addProduct() {
        startActivity(new Intent(this, AddProductActivity.class));
    }

    private void setProducts() {
        ProductAdapter adapter = new ProductAdapter(this, dbHelper.getProducts(Prefs.getString(
                this, Prefs.KEY_USERNAME, null)));
        binding.recyclerView.setAdapter(adapter);
        if (adapter.getItemCount() != 0) {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.noProducts.setVisibility(View.GONE);
        } else {
            binding.recyclerView.setVisibility(View.GONE);
            binding.noProducts.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setProducts();
    }
}