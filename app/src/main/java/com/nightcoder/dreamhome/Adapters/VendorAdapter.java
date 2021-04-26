package com.nightcoder.dreamhome.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nightcoder.dreamhome.AdminActivity;
import com.nightcoder.dreamhome.ManageVendorActivity;
import com.nightcoder.dreamhome.Models.Vendor;
import com.nightcoder.dreamhome.ProductsActivity;
import com.nightcoder.dreamhome.R;
import com.nightcoder.dreamhome.databinding.ItemVendorBinding;
import com.squareup.picasso.Picasso;

import java.io.File;

public class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.ViewHolder> {

    private final Cursor cursor;
    private final Context context;

    public VendorAdapter(Cursor cursor, Context context) {
        this.cursor = cursor;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_vendor, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        Vendor vendor = new Vendor();
        vendor.imageUri = cursor.getString(cursor.getColumnIndex("photoUrl"));
        vendor.title = cursor.getString(cursor.getColumnIndex("title"));
        vendor.description = cursor.getString(cursor.getColumnIndex("description"));
        vendor.website = cursor.getString(cursor.getColumnIndex("website"));
        vendor.pincode = cursor.getString(cursor.getColumnIndex("pincode"));
        vendor.email = cursor.getString(cursor.getColumnIndex("email"));
        vendor.number = cursor.getString(cursor.getColumnIndex("number"));
        vendor.address = cursor.getString(cursor.getColumnIndex("address"));
        vendor.password = cursor.getString(cursor.getColumnIndex("password"));
        vendor.extra = cursor.getString(cursor.getColumnIndex("extra"));
        vendor.status = cursor.getInt(cursor.getColumnIndex("status"));
        vendor.banner = cursor.getString(cursor.getColumnIndex("banner"));

        assert holder.binding != null;
        Picasso.get().load(new File(vendor.imageUri)).into(holder.binding.logo);
        Picasso.get().load(new File(vendor.banner)).into(holder.binding.banner);
        holder.binding.title.setText(vendor.title);
        holder.binding.website.setText(vendor.website);

        holder.binding.container.setOnClickListener(v -> {
            if (context instanceof AdminActivity) {
                ManageVendorActivity.vendor = vendor;
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) context, holder.binding.logo, "Logo");
                context.startActivity(new Intent(context, ManageVendorActivity.class), options.toBundle());
            } else {
                ProductsActivity.vendor = vendor;
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) context, holder.binding.logo, "Logo");
                context.startActivity(new Intent(context, ProductsActivity.class), options.toBundle());
            }
        });

    }

    @Override
    public int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemVendorBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
