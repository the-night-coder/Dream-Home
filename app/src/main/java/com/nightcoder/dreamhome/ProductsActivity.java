package com.nightcoder.dreamhome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.nightcoder.dreamhome.Fragments.OrdersFragment;
import com.nightcoder.dreamhome.Fragments.ProductsFragment;
import com.nightcoder.dreamhome.Fragments.WishlistFragment;
import com.nightcoder.dreamhome.Models.Vendor;
import com.nightcoder.dreamhome.databinding.ActivityProductsBinding;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class ProductsActivity extends AppCompatActivity {

    private ActivityProductsBinding binding;
    public static Vendor vendor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_products);
        init();
    }

    private void init() {
        Picasso.get().load(new File(vendor.imageUri)).into(binding.profile);
        binding.title.setText(vendor.title);
        binding.back.setOnClickListener(v -> onBackPressed());
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addPage(new ProductsFragment());
        adapter.addPage(new WishlistFragment());
        adapter.addPage(new OrdersFragment());
        binding.viewPager.setAdapter(adapter);

        binding.bottomNav.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                binding.viewPager.setCurrentItem(0);
            } else if (item.getItemId() == R.id.nav_cart) {
                binding.viewPager.setCurrentItem(1);
            } else if (item.getItemId() == R.id.nav_orders) {
                binding.viewPager.setCurrentItem(2);
            }
            return true;
        });

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    binding.bottomNav.setSelectedItemId(R.id.nav_home);
                } else if (position == 1) {
                    binding.bottomNav.setSelectedItemId(R.id.nav_cart);
                } else if (position == 2) {
                    binding.bottomNav.setSelectedItemId(R.id.nav_orders);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        binding.profile.setOnClickListener(v -> {
            ManageVendorActivity.vendor = vendor;
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation((Activity) this, binding.profile, "Logo");
            startActivity(new Intent(this, ManageVendorActivity.class), options.toBundle());
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {

        private final ArrayList<Fragment> fragments;

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        public void addPage(Fragment fragment) {
            fragments.add(fragment);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}