package com.example.collegeappadmin.Fragements;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.collegeappadmin.Models.studentDetialModel;
import com.example.collegeappadmin.Models.studentDisplayAdapter;
import com.example.collegeappadmin.R;
import com.example.collegeappadmin.databinding.FragmentStudentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class studentFragment extends Fragment {


    public studentFragment() {
        // Required empty public constructor
    }

    FragmentStudentBinding binding;
    studentDisplayAdapter adapter;
    ArrayList<studentDetialModel> values;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStudentBinding.inflate(getLayoutInflater(),container,false);

        values = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child(getString(R.string.key_student));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                values.clear();
                for(DataSnapshot data: snapshot.getChildren())
                {
                    studentDetialModel info = data.getValue(studentDetialModel.class);
                    values.add(info);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new studentDisplayAdapter(getContext(),values);

        binding.recyclerViewStudents.setAdapter(adapter);
        return binding.getRoot();
    }
}