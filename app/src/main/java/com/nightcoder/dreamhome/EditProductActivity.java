package com.nightcoder.dreamhome;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.nightcoder.dreamhome.DataSupports.DBHelper;
import com.nightcoder.dreamhome.Models.Product;
import com.nightcoder.dreamhome.Supports.Constants;
import com.nightcoder.dreamhome.Supports.Prefs;
import com.nightcoder.dreamhome.Supports.RealPathUtil;
import com.nightcoder.dreamhome.databinding.ActivityEditProductBinding;
import com.squareup.picasso.Picasso;

import java.io.File;

public class EditProductActivity extends AppCompatActivity {

    private ActivityEditProductBinding binding;
    private DBHelper dbHelper;
    private String imageUri;

    public static Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_product);
        init();
    }

    private void init() {
        binding.addProduct.setOnClickListener(v -> validate());
        binding.image.setOnClickListener(v -> getImage());
        binding.unit.setOnClickListener(v -> units());
        dbHelper = new DBHelper(this);

        binding.title.setText(product.name);
        binding.description.setText(product.description);
        binding.logDescription.setText(product.longDescribe);
        binding.unit.setText(product.unit);
        binding.stock.setText(String.valueOf(product.stock));
        binding.price.setText(String.valueOf(product.price));
        binding.qTo.setText(String.valueOf(product.quantityTo));
        Picasso.get().load(new File(product.thumbnailUri)).into(binding.image);
        imageUri = product.thumbnailUri;
    }

    private void validate() {
        if (!binding.title.getText().toString().matches(Constants.TITLE_PATTERN)) {
            Toast.makeText(this, "Provide valid title", Toast.LENGTH_SHORT).show();
        } else if (binding.description.getText().toString().isEmpty()) {
            Toast.makeText(this, "Provide short description", Toast.LENGTH_SHORT).show();
        } else if (binding.logDescription.getText().toString().isEmpty()) {
            Toast.makeText(this, "Provide long description", Toast.LENGTH_SHORT).show();
        } else if (binding.price.getText().toString().isEmpty()) {
            Toast.makeText(this, "Provide unit price", Toast.LENGTH_SHORT).show();
        } else if (binding.unit.getText().toString().isEmpty()) {
            Toast.makeText(this, "Provide unit", Toast.LENGTH_SHORT).show();
        } else if (binding.stock.getText().toString().isEmpty()) {
            Toast.makeText(this, "Provide stock", Toast.LENGTH_SHORT).show();
        } else if (binding.qTo.getText().toString().isEmpty()) {
            Toast.makeText(this, "Provide maximum quantity", Toast.LENGTH_SHORT).show();
        } else if (imageUri == null) {
            Toast.makeText(this, "Provide an Image", Toast.LENGTH_SHORT).show();
        } else {
            addProduct();
        }
    }

    private void units() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle("Select Unit");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("KG");
        arrayAdapter.add("Ton");
        arrayAdapter.add("Unit");
        arrayAdapter.add("Load");
        arrayAdapter.add("Piece");
        arrayAdapter.add("Packet");
        builderSingle.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());
        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            binding.unit.setText(arrayAdapter.getItem(which));
            dialog.cancel();
        });
        builderSingle.show();
    }

    private void addProduct() {
        Product product = new Product();
        product.name = binding.title.getText().toString().trim();
        product.description = binding.description.getText().toString().trim();
        product.longDescribe = binding.logDescription.getText().toString().trim();
        product.unit = binding.unit.getText().toString().trim();
        product.thumbnailUri = imageUri;
        product.vendorId = Prefs.getString(this, Prefs.KEY_USERNAME, null);
        product.quantityTo = Integer.parseInt(binding.qTo.getText().toString());
        product.price = Integer.parseInt(binding.price.getText().toString());
        product.stock = Integer.parseInt(binding.stock.getText().toString());
        product.productId = EditProductActivity.product.productId;
        product.ref = EditProductActivity.product.ref;

        dbHelper.editProduct(product);
        Toast.makeText(this, "Product modified", Toast.LENGTH_SHORT).show();
        finish();
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
            Picasso.get().load(uri).into(binding.image);
            imageUri = RealPathUtil.getRealPath(this, uri);
        }
    }
}