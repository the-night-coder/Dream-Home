package com.nightcoder.dreamhome;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nightcoder.dreamhome.Adapters.OrderAdapter;
import com.nightcoder.dreamhome.DataSupports.DBHelper;
import com.nightcoder.dreamhome.Supports.Prefs;
import com.nightcoder.dreamhome.databinding.ActivityUserOrderBinding;

public class OrderActivity extends AppCompatActivity {

    private ActivityUserOrderBinding binding;
    private DBHelper dbHelper;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_order);

        init();
    }

    private void init() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new DBHelper(this);
        setProducts();

        binding.refresh.setOnRefreshListener(() -> {
            setProducts();
            binding.refresh.setRefreshing(false);
        });

        binding.back.setOnClickListener(v -> onBackPressed());
    }

    private void setProducts() {
        OrderAdapter adapter = new OrderAdapter(dbHelper.getOrders(Prefs.getString(this, Prefs.KEY_USERNAME, null)), this);
        binding.recyclerView.setAdapter(adapter);
        if (adapter.getItemCount() != 0) {
            binding.noItems.setVisibility(View.GONE);
        } else {
            binding.noItems.setVisibility(View.VISIBLE);
        }
    }
}