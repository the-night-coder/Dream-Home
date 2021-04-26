package com.nightcoder.dreamhome.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.nightcoder.dreamhome.DataSupports.DBHelper;
import com.nightcoder.dreamhome.EditProductActivity;
import com.nightcoder.dreamhome.Models.Product;
import com.nightcoder.dreamhome.OpenProductActivity;
import com.nightcoder.dreamhome.R;
import com.nightcoder.dreamhome.Supports.Tables;
import com.nightcoder.dreamhome.VendorActivity;
import com.nightcoder.dreamhome.databinding.ItemProductBinding;
import com.nightcoder.dreamhome.databinding.ManageProductBinding;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private final ArrayList<Product> products;
    private Context context;
    private DBHelper dbHelper;

    public ProductAdapter(Context context, ArrayList<Product> products) {
        this.products = products;
        this.context = context;
        this.dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);

        Picasso.get().load(new File(product.thumbnailUri)).into(holder.binding.image);
        holder.binding.description.setText(product.description);
        holder.binding.name.setText(product.name);
        holder.binding.price.setText("₹" + product.price);

        holder.binding.container.setOnClickListener(v -> {
            if (context instanceof VendorActivity) {
                BottomSheetDialog dialog = new BottomSheetDialog(context);
                ManageProductBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.manage_product, null, false);
                dialog.setContentView(binding.getRoot());
                binding.edit.setOnClickListener(v1 -> {
                    dialog.cancel();
                    EditProductActivity.product = product;
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity) context, holder.binding.image, "image");
                    context.startActivity(new Intent(context, EditProductActivity.class), options.toBundle());
                });
                binding.delete.setOnClickListener(v1 -> {
                    dialog.cancel();
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    db.execSQL("DELETE FROM " + Tables.PRODUCT + " WHERE _id=" + product.productId);
                    Toast.makeText(context, "Deleted please refresh", Toast.LENGTH_SHORT).show();
                });
                dialog.show();
            } else {
                OpenProductActivity.product = product;
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) context, holder.binding.image, "image");
                context.startActivity(new Intent(context, OpenProductActivity.class), options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return products == null ? 0 : products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
