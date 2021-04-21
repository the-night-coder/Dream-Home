package com.nightcoder.dreamhome;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.nightcoder.dreamhome.Models.Product;

public class ManageProductActivity extends AppCompatActivity {

    public static Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_product);
    }
}