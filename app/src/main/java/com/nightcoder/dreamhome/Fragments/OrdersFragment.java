package com.nightcoder.dreamhome.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nightcoder.dreamhome.Adapters.OrderAdapter;
import com.nightcoder.dreamhome.DataSupports.DBHelper;
import com.nightcoder.dreamhome.ProductsActivity;
import com.nightcoder.dreamhome.R;
import com.nightcoder.dreamhome.Supports.Prefs;

public class OrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private TextView noItems;

    private Context context;
    private DBHelper dbHelper;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        refreshLayout = view.findViewById(R.id.refresh);
        noItems = view.findViewById(R.id.noItems);
        init();
        return view;
    }

    private void init() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        dbHelper = new DBHelper(context);
        setProducts();

        refreshLayout.setOnRefreshListener(() -> {
            setProducts();
            refreshLayout.setRefreshing(false);
        });
    }

    private void setProducts() {
        OrderAdapter adapter = new OrderAdapter(dbHelper.getOrders(ProductsActivity.vendor.email, Prefs.getString(context, Prefs.KEY_USERNAME, null)), context);
        recyclerView.setAdapter(adapter);
        if (adapter.getItemCount() != 0) {
            noItems.setVisibility(View.GONE);
        } else {
            noItems.setVisibility(View.VISIBLE);
        }
    }
}