package com.nightcoder.dreamhome.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nightcoder.dreamhome.Adapters.ProductAdapter;
import com.nightcoder.dreamhome.DataSupports.DBHelper;
import com.nightcoder.dreamhome.ProductsActivity;
import com.nightcoder.dreamhome.R;

public class ProductsFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;

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
        ProductAdapter adapter = new ProductAdapter(context, dbHelper.getProducts(ProductsActivity.vendor.email));
        recyclerView.setAdapter(adapter);
    }
}