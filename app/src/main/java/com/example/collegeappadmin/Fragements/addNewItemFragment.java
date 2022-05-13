package com.example.collegeappadmin.Fragements;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.collegeappadmin.Activities.EmptyFragmentActivity;
import com.example.collegeappadmin.Activities.bottom_ShowDetailActivity;
import com.example.collegeappadmin.Models.itemModel;
import com.example.collegeappadmin.R;
import com.example.collegeappadmin.databinding.FragmentAddNewItemBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class addNewItemFragment extends Fragment {


    public addNewItemFragment() {
        // Required empty public constructor
    }

    FragmentAddNewItemBinding binding;
    private DatePickerDialog datePickerDialog;
    String itemString;
    boolean checked = false;
    Integer position = EmptyFragmentActivity.position;
    String returnDate,issueDate,itemName,quantity;
    FirebaseDatabase database;
    DatabaseReference reference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddNewItemBinding.inflate(getLayoutInflater(),container,false);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child(getString(R.string.key_student));

        initDatePicker();
        binding.datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        binding.radioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton selected = (RadioButton)binding.getRoot().findViewById(checkedId);
                checked = true;
                Log.d("Aryan","text is + "+selected.getText().toString());
                if(selected.getText().toString().equals("Books"))
                {
                    itemString = getString(R.string.key_library);
                }
                else
                {
                    itemString = getString(R.string.key_sport);
                }
            }
        });

        binding.sureButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTodaysDate();
                if(!binding.itemName.getText().toString().trim().isEmpty() && !binding.qunatityText.getText().toString().trim().isEmpty()
                                && checked && !returnDate.isEmpty() && !issueDate.isEmpty())
                {
                    Log.d("Aryan","Going for the adding");
                    putDatainFireData(binding.itemName.getText().toString().trim(),binding.qunatityText.getText().toString().trim()
                                ,returnDate,issueDate);
                }
                else{
                    Toast.makeText(getContext(), "Please fill all the details First", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return binding.getRoot();
    }

    private void putDatainFireData(String name, String quantity, String returnDate,String issueDate) {

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0;
                for(DataSnapshot data:snapshot.getChildren())
                {
                    if(i==position)
                    {
                        Log.d("Aryan","position matched");
                        Map<String,Object> values = new HashMap<>();

                        values.put("name",name);
                        values.put("returnDate",returnDate);
                        values.put("issueDate",issueDate);
                        values.put("quantity",quantity);

                        data.getRef().child(itemString).push().updateChildren(values).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Intent intent = new Intent(getActivity(), bottom_ShowDetailActivity.class);

                                startActivity(intent);
                                getActivity().finish();
                                return;
                            }
                        });
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

    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        issueDate =  day+"/"+month+"/"+year;
        return makeDateString(day, month, year);
    }

    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }
    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                if(month<10)
                    returnDate = day+"/0"+month+"/"+year;
                else
                    returnDate = day+"/"+month+"/"+year;
                String date = makeDateString(day, month, year);

                binding.datePickerButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.BUTTON_NEUTRAL;

        datePickerDialog = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }
}