package com.nightcoder.dreamhome.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.nightcoder.dreamhome.DataSupports.DBHelper;
import com.nightcoder.dreamhome.Models.Order;
import com.nightcoder.dreamhome.Models.Product;
import com.nightcoder.dreamhome.OrderActivity;
import com.nightcoder.dreamhome.R;
import com.nightcoder.dreamhome.Supports.Animation;
import com.nightcoder.dreamhome.Supports.Constants;
import com.nightcoder.dreamhome.Supports.Time;
import com.nightcoder.dreamhome.databinding.ItemOrderBinding;
import com.nightcoder.dreamhome.databinding.ManageOrderBinding;
import com.squareup.picasso.Picasso;

import java.io.File;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private Cursor cursor;
    private Context context;

    private DBHelper dbHelper;

    public OrderAdapter(Cursor cursor, Context context) {
        this.cursor = cursor;
        this.context = context;
        this.dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        Order order = new Order();
        order.timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"));
        order.status = cursor.getInt(cursor.getColumnIndex("status"));
        order.quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
        order.number = cursor.getString(cursor.getColumnIndex("number"));
        order.productId = cursor.getInt(cursor.getColumnIndex("productId"));
        order._id = cursor.getInt(cursor.getColumnIndex("_id"));
        order.address = cursor.getString(cursor.getColumnIndex("address"));
        order.landmark = cursor.getString(cursor.getColumnIndex("landmark"));
        order.email = cursor.getString(cursor.getColumnIndex("email"));
        order.vendor = cursor.getString(cursor.getColumnIndex("vendor"));
        order.key = cursor.getString(cursor.getColumnIndex("ref"));
        order.name = cursor.getString(cursor.getColumnIndex("name"));

        Product product = dbHelper.getProduct(order.productId);

        assert holder.binding != null;
        holder.binding.description.setText(product.description);
        holder.binding.name.setText(product.name);
        holder.binding.price.setText("₹" + (product.price * order.quantity));
        holder.binding.unit.setText(order.quantity + product.unit);
        Picasso.get().load(new File(product.thumbnailUri)).into(holder.binding.image);

        holder.binding.fullName.setText(order.name);
        holder.binding.address.setText(order.address);
        holder.binding.email.setText(order.email);
        holder.binding.landmark.setText(order.landmark);
        holder.binding.number.setText(order.number);

        if (order.status == Constants.PENDING) {
            holder.binding.status.setText(Html.fromHtml("<b>Status:</b> Pending"));
            holder.binding.status.setTextColor(context.getResources().getColor(R.color.yellow, context.getTheme()));
        } else if (order.status == Constants.ACCEPT) {
            holder.binding.status.setText(Html.fromHtml("<b>Status:</b> Accepted"));
            holder.binding.status.setTextColor(context.getResources().getColor(R.color.green, context.getTheme()));
        } else {
            holder.binding.status.setText(Html.fromHtml("<b>Status:</b> Rejected"));
            holder.binding.status.setTextColor(context.getResources().getColor(R.color.red, context.getTheme()));
        }
        holder.binding.time.setText(Time.getTimeFullText(order.timestamp));

        holder.binding.expand.setOnClickListener(v -> {
            if (holder.binding.expand.getTag().equals("EXPAND")) {
                Animation.expand(holder.binding.details);
                holder.binding.expand.setTag("COLLAPSE");
                holder.binding.expand.setImageResource(R.drawable.ic_baseline_expand_less_24);
            } else {
                holder.binding.expand.setImageResource(R.drawable.ic_baseline_expand_more_24);
                Animation.collapse(holder.binding.details);
                holder.binding.expand.setTag("EXPAND");
            }

        });

        holder.binding.container.setOnClickListener(v -> {
            if (context instanceof OrderActivity) {
                BottomSheetDialog dialog = new BottomSheetDialog(context);
                ManageOrderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.manage_order, null, false);
                dialog.setContentView(binding.getRoot());
                binding.accept.setOnClickListener(v1 -> {
                    dbHelper.updateOrderStatus(Constants.ACCEPT, order._id);
                    Toast.makeText(context, "Order Accepted", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                });

                binding.reject.setOnClickListener(v1 -> {
                    dbHelper.updateOrderStatus(Constants.REJECT, order._id);
                    Toast.makeText(context, "Order Rejected", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemOrderBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
