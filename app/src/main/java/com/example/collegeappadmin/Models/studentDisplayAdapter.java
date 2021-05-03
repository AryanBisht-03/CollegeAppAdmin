package com.example.collegeappadmin.Models;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeappadmin.Activities.EmptyFragmentActivity;
import com.example.collegeappadmin.R;
import com.example.collegeappadmin.databinding.StudentDisplayBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class studentDisplayAdapter extends RecyclerView.Adapter<studentDisplayAdapter.studentViewHolder> {

    Context context;
    ArrayList<studentDetialModel> students;

    public studentDisplayAdapter(Context context, ArrayList<studentDetialModel> students) {
        this.context = context;
        this.students = students;
    }

    @NonNull
    @Override
    public studentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_display,parent,false);
        return new studentDisplayAdapter.studentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull studentViewHolder holder, int position) {

        studentDetialModel data = students.get(position);
        Picasso.get().load(data.getImage()).placeholder(R.drawable.man).into(holder.binding.imageStudent);

        holder.binding.nameTextStudent.setText(data.getName());
        holder.binding.rollNumTextStudent.setText(data.getRollNum());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EmptyFragmentActivity.class);
                intent.putExtra("position",position);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return students.size();
    }


    public class studentViewHolder extends RecyclerView.ViewHolder{

        StudentDisplayBinding binding;
        public studentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = StudentDisplayBinding.bind(itemView);

        }
    }
}
