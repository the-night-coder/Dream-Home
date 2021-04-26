package com.nightcoder.dreamhome.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.nightcoder.dreamhome.DataSupports.DBHelper;
import com.nightcoder.dreamhome.EditProductActivity;
import com.nightcoder.dreamhome.Fragments.WishlistFragment;
import com.nightcoder.dreamhome.Interface.OnRefresh;
import com.nightcoder.dreamhome.Models.Product;
import com.nightcoder.dreamhome.Models.Wishlist;
import com.nightcoder.dreamhome.OpenProductActivity;
import com.nightcoder.dreamhome.R;
import com.nightcoder.dreamhome.Supports.Tables;
import com.nightcoder.dreamhome.VendorActivity;
import com.nightcoder.dreamhome.databinding.ItemWishlistBinding;
import com.nightcoder.dreamhome.databinding.ManageProductBinding;
import com.squareup.picasso.Picasso;

import java.io.File;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    private final Cursor cursor;
    private final Context context;
    private final DBHelper dbHelper;
    private OnRefresh onRefresh;

    public WishlistAdapter(WishlistFragment fragment, Context context, Cursor cursor) {
        this.cursor = cursor;
        this.context = context;
        this.dbHelper = new DBHelper(context);
        this.onRefresh = (OnRefresh) fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wishlist, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        Wishlist wishlist = new Wishlist();
        wishlist.proId = cursor.getInt(cursor.getColumnIndex("proId"));
        wishlist._id = cursor.getInt(cursor.getColumnIndex("_id"));

        Product product = dbHelper.getProduct(wishlist.proId);

        assert holder.binding != null;
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
                dialog.show();
            } else {
                OpenProductActivity.product = product;
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) context, holder.binding.image, "image");
                context.startActivity(new Intent(context, OpenProductActivity.class), options.toBundle());
            }
        });

        holder.binding.delete.setOnClickListener(v -> {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            db.execSQL("DELETE FROM " + Tables.WISHLIST + " WHERE _id=" + wishlist._id);
            onRefresh.onRefresh();
        });
    }

    @Override
    public int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemWishlistBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
