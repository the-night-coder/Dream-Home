package com.nightcoder.dreamhome;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.nightcoder.dreamhome.DataSupports.DBHelper;
import com.nightcoder.dreamhome.Models.Order;
import com.nightcoder.dreamhome.Models.Product;
import com.nightcoder.dreamhome.Models.User;
import com.nightcoder.dreamhome.Supports.Constants;
import com.nightcoder.dreamhome.Supports.Prefs;
import com.nightcoder.dreamhome.Supports.Tables;
import com.nightcoder.dreamhome.databinding.ActivityPurchaseBinding;
import com.squareup.picasso.Picasso;

import java.io.File;

public class PurchaseActivity extends AppCompatActivity {

    public static Product product;
    public static int quantity;
    private ActivityPurchaseBinding binding;
    private DBHelper dbHelper;
    private User user;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_purchase);
        dbHelper = new DBHelper(this);
        user = dbHelper.getUserDetails(Prefs.getString(this, Prefs.KEY_USERNAME, null));

        binding.name.setText(product.name);
        binding.description.setText(product.description);
        binding.price.setText("₹" + (product.price * quantity));
        binding.unit.setText(quantity + product.unit);
        binding.fullName.setText(user.fullName);
        binding.email.setText(user.email);

        Picasso.get().load(new File(product.thumbnailUri)).into(binding.image);
        init();
    }

    private void init() {
        binding.buy.setOnClickListener(v -> validate());
    }

    private void validate() {
        if (!binding.fullName.getText().toString().matches(Constants.TITLE_PATTERN)) {
            Toast.makeText(this, "Provide valid name", Toast.LENGTH_SHORT).show();
        } else if (binding.address.getText().toString().isEmpty()) {
            Toast.makeText(this, "Provide address", Toast.LENGTH_SHORT).show();
        } else if (binding.pincode.getText().toString().length() != 6) {
            Toast.makeText(this, "Provide valid pincode", Toast.LENGTH_SHORT).show();
        } else if (binding.landmark.getText().toString().isEmpty()) {
            Toast.makeText(this, "Provide a land mark", Toast.LENGTH_SHORT).show();
        } else if (binding.number.getText().toString().length() < 10) {
            Toast.makeText(this, "Provide valid phone number", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.getText().toString()).matches()) {
            Toast.makeText(this, "Provide valid email", Toast.LENGTH_SHORT).show();
        } else {
            order();
        }
    }

    private void order() {
        Order order = new Order();
        order.vendor = product.vendorId;
        order.name = binding.fullName.getText().toString();
        order.email = binding.email.getText().toString();
        order.landmark = binding.landmark.getText().toString();
        order.number = binding.number.getText().toString();
        order.productId = product.productId;
        order.quantity = quantity;
        order.status = Constants.PENDING;
        order.key = product.ref;
        order.address = binding.address.getText().toString() + ", " + binding.pincode.getText().toString();
        order.timestamp = System.currentTimeMillis();
        dbHelper.addOrder(order);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int stock = product.stock - quantity;
        db.execSQL("UPDATE " + Tables.PRODUCT + " SET stock=" + stock + " WHERE _id=" + product.productId);
        Toast.makeText(this, "Order informed", Toast.LENGTH_SHORT).show();
        finish();
    }
}