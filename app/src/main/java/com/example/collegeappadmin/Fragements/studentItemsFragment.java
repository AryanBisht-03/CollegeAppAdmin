package com.example.collegeappadmin.Fragements;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.collegeappadmin.Activities.EmptyFragmentActivity;
import com.example.collegeappadmin.Models.itemModel;
import com.example.collegeappadmin.Models.libraryAdapter;
import com.example.collegeappadmin.R;
import com.example.collegeappadmin.databinding.FragmentStudentItemsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class studentItemsFragment extends Fragment {


    public studentItemsFragment() {
        // Required empty public constructor
    }

    FragmentStudentItemsBinding binding;
    Integer position = EmptyFragmentActivity.position;
    libraryAdapter adapter;
    ArrayList<itemModel> items;
    FirebaseDatabase database;
    DatabaseReference reference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStudentItemsBinding.inflate(getLayoutInflater(),container,false);

        database = FirebaseDatabase.getInstance();

        reference = database.getReference().child(getContext().getString(R.string.key_student));
        items = new ArrayList<>();

        Log.d("Aryan","position is  = "+position.toString());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0;
                items.clear();
                for(DataSnapshot data:snapshot.getChildren())
                {
                    if(i==position) {
                        if(data.child("library").exists())
                        {

                            for(DataSnapshot store: data.child(getContext().getString(R.string.key_library)).getChildren())
                            {
                                itemModel val = store.getValue(itemModel.class);
                                val.setType(getContext().getString(R.string.key_library));
                                val.setUid(data.getKey());
                                items.add(val);
                            }
                        }
                        if(data.child(getContext().getString(R.string.key_sport)).exists())
                        {

                            for(DataSnapshot store: data.child(getContext().getString(R.string.key_sport)).getChildren())
                            {
                                itemModel val = store.getValue(itemModel.class);
                                val.setType(getContext().getString(R.string.key_sport));
                                val.setUid(data.getKey());
                                items.add(val);
                            }
                        }
                    }
                    i++;
                }
                adapter.notifyDataSetChanged();
                checkSizeOfItems();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new libraryAdapter(getContext(),items);
        binding.studentItemsRecyclerView.setAdapter(adapter);


        binding.addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),EmptyFragmentActivity.class);
                intent.putExtra("addNew","Adding New Data");
                startActivity(intent);
                getActivity().finish();
            }
        });

        return binding.getRoot();
    }

    private void checkSizeOfItems() {
        if(items.size()==0)
        {
            binding.textView4.setVisibility(View.VISIBLE);
            binding.animationView.setVisibility(View.VISIBLE);
            binding.studentItemsRecyclerView.setVisibility(View.GONE);
        }
        else
        {
            binding.textView4.setVisibility(View.GONE);
            binding.animationView.setVisibility(View.GONE);
            binding.studentItemsRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}