package com.example.collegeappadmin.Models;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeappadmin.Activities.EmptyFragmentActivity;
import com.example.collegeappadmin.Activities.bottom_ShowDetailActivity;
import com.example.collegeappadmin.R;
import com.example.collegeappadmin.databinding.LibraryRecyclerLayoutBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class libraryAdapter extends RecyclerView.Adapter<libraryAdapter.libraryViewModel> {

    Context context;
    ArrayList<itemModel> items;

    public libraryAdapter(Context context, ArrayList<itemModel> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public libraryViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.library_recycler_layout,parent,false);
        return new libraryViewModel(view);

    }

    @Override
    public void onBindViewHolder(@NonNull libraryViewModel holder, int position) {

        itemModel data = items.get(position);
        //Put Data here .....
        holder.binding.itemName.setText(data.getName());
        holder.binding.issueText.setText(data.getIssueDate());
        holder.binding.returnText.setText(data.getReturnDate());
        holder.binding.quantityText.setText(data.getQuantity());

        holder.binding.deleteDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
// Add the buttons
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseDatabase.getInstance().getReference().child(context.getString(R.string.key_student)).child(data.getUid())
                                .child(data.getType()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        int i=0;
                                        for(DataSnapshot dataSnapshot:snapshot.getChildren())
                                        {
                                            if(i==position)
                                            {
                                                FirebaseDatabase.getInstance().getReference().child(context.getString(R.string.key_student)).child(data.getUid())
                                                        .child(data.getType()).child(dataSnapshot.getKey()).removeValue();
                                                Intent intent = new Intent(context, bottom_ShowDetailActivity.class);
                                                context.startActivity(intent);
                                                return;
                                            }
                                            i++;
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                        // User clicked OK button
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        // User cancelled the dialog
                    }
                });

                builder.setTitle("Return");
                builder.setMessage("The item is returned by the student");
// Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class libraryViewModel extends RecyclerView.ViewHolder{

        LibraryRecyclerLayoutBinding binding;
        public libraryViewModel(@NonNull View itemView) {
            super(itemView);

            binding = LibraryRecyclerLayoutBinding.bind(itemView);

        }
    }
}
