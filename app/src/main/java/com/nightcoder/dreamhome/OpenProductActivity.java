package com.nightcoder.dreamhome;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.nightcoder.dreamhome.DataSupports.DBHelper;
import com.nightcoder.dreamhome.Models.Product;
import com.nightcoder.dreamhome.Models.Vendor;
import com.nightcoder.dreamhome.Models.Wishlist;
import com.nightcoder.dreamhome.Supports.Prefs;
import com.nightcoder.dreamhome.databinding.ActivityOpenProductBinding;
import com.squareup.picasso.Picasso;

import java.io.File;

public class OpenProductActivity extends AppCompatActivity {

    public static Product product;
    public static Vendor vendor;
    private ActivityOpenProductBinding binding;
    private int quantity = 1;
    private int total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_open_product);
        init();
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        Picasso.get().load(new File(product.thumbnailUri)).into(binding.image);
        binding.name.setText(product.name);
        binding.description.setText(product.description);
        binding.logDescription.setText(product.longDescribe);
        binding.unit.setText(" per " + product.unit);
        binding.price.setText("₹" + product.price);
        binding.quantity.setText(quantity + " " + product.unit);
        total = product.price;
        DBHelper dbHelper = new DBHelper(this);
        vendor = dbHelper.getVendor(product.vendorId);
        Picasso.get().load(new File(vendor.imageUri)).into(binding.logo);
        binding.shoName.setText(vendor.title);
        binding.add.setOnClickListener(v -> {
            if (quantity < product.quantityTo) {
                quantity++;
                update();
            }
        });

        binding.minus.setOnClickListener(v -> {
            if (quantity != 1) {
                quantity--;
                update();
            }
        });

        binding.buy.setOnClickListener(v -> {
            if (quantity <= product.stock) {
                PurchaseActivity.product = product;
                PurchaseActivity.quantity = quantity;
                startActivity(new Intent(OpenProductActivity.this, PurchaseActivity.class));
            } else Toast.makeText(this, "Out of stock", Toast.LENGTH_SHORT).show();
        });

        binding.wish.setEnabled(!dbHelper.checkWishlist(Prefs.getString(
                OpenProductActivity.this, Prefs.KEY_USERNAME, null), product.ref));

        binding.wish.setOnClickListener(v -> {
            Wishlist wishlist = new Wishlist();
            wishlist.user = Prefs.getString(OpenProductActivity.this, Prefs.KEY_USERNAME, null);
            wishlist.ref = product.ref;
            wishlist.proId = product.productId;
            dbHelper.addWishlist(wishlist);
            Toast.makeText(this, "Added to wishlist", Toast.LENGTH_SHORT).show();
            binding.wish.setEnabled(false);
        });
    }

    @SuppressLint("SetTextI18n")
    private void update() {
        binding.quantity.setText(quantity + " " + product.unit);
        total = quantity * product.price;
        binding.total.setText("₹" + total);
    }
}