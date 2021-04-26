package com.nightcoder.dreamhome;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.nightcoder.dreamhome.Adapters.VendorAdapter;
import com.nightcoder.dreamhome.DataSupports.DBHelper;
import com.nightcoder.dreamhome.Models.User;
import com.nightcoder.dreamhome.Supports.Prefs;
import com.nightcoder.dreamhome.databinding.ActivityUserBinding;
import com.nightcoder.dreamhome.databinding.UserProfileBinding;
import com.squareup.picasso.Picasso;

import java.io.File;

public class UserActivity extends AppCompatActivity {

    private ActivityUserBinding binding;
    private DBHelper dbHelper;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user);
        init();
    }

    private void init() {
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        dbHelper = new DBHelper(this);
        user = dbHelper.getUserDetails(Prefs.getString(this, Prefs.KEY_USERNAME, null));
        Picasso.get().load(new File(user.photoUrl)).into(binding.profile);

        binding.profile.setOnClickListener(v -> openProfile());
    }

    private void openProfile() {
        BottomSheetDialog dialog = new BottomSheetDialog(UserActivity.this);
        UserProfileBinding binding = DataBindingUtil.inflate(LayoutInflater.from(UserActivity.this),
                R.layout.user_profile, null, false);
        dialog.setContentView(binding.getRoot());
        Picasso.get().load(new File(user.photoUrl)).into(binding.image);
        binding.name.setText(user.fullName);
        binding.email.setText(user.email);
        binding.logOut.setOnClickListener(v -> {
            dialog.cancel();
            logOut();
        });
        dialog.show();
    }

    private void logOut() {
        Prefs.putString(this, Prefs.KEY_USERNAME, null);
        startActivity(new Intent(this, LogInActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    private void setVendors() {
        VendorAdapter adapter = new VendorAdapter(dbHelper.getVendorsForUser(), this);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVendors();
    }
}