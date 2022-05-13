package com.example.collegeappadmin.Models;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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

    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String issueDate =  day+" "+month+" "+year;
        return issueDate;
    }

    private String calculateFine(String returnDate)
    {
        String today = getTodaysDate();
        returnDate = returnDate.substring(0,2)+" "+returnDate.substring(3,5)+" "+returnDate.substring(6);
        SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
        try {
            Date date1 = myFormat.parse(today);
            Date date2 = myFormat.parse(returnDate);
            long diff = (date1.getTime() - date2.getTime())*10;
            diff = diff<0 ? 0: diff;
            long daysBetween = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            return daysBetween+" ";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "0";
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
                        Log.d("Aryan","Inside  ");
                        dialog.cancel();
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        String fineAmount = calculateFine(data.getReturnDate());
                        builder1.setTitle("Pay Fine");
                        builder1.setMessage("Fine of Ruppe: - "+fineAmount);
                        builder1.setPositiveButton("Paid", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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
                            }
                        });
                        builder1.setNegativeButton("Not Paid", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface insideDialog, int which) {
                                insideDialog.cancel();
                            }
                        });

                        AlertDialog insidedialog = builder1.create();
                        insidedialog.show();
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
