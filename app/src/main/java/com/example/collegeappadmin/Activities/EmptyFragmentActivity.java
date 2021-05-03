package com.example.collegeappadmin.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.collegeappadmin.Fragements.fillDetialFragment;
import com.example.collegeappadmin.Fragements.profileFragment;
import com.example.collegeappadmin.R;
import com.example.collegeappadmin.databinding.ActivityEmptyFragmentBinding;

public class EmptyFragmentActivity extends AppCompatActivity {

    ActivityEmptyFragmentBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmptyFragmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fillDetialFragment fragment = new fillDetialFragment();

        Intent intent = getIntent();
        if(intent.hasExtra("name"))
        {
            profileFragment profileFragment = new profileFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,profileFragment).commit();
        }
        else
        {
            fillDetialFragment detialFragment = new fillDetialFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,detialFragment).commit();
        }
    }
}