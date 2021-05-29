package com.creator.rewardsapp.Body.OfferWalls.ui.CreateRewardEvents;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.creator.rewardsapp.Body.OfferWalls.HomeActivity;
import com.creator.rewardsapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class CreateRewardEventForCustomers extends Fragment {
    private EditText cStartDate;
    private EditText cEndDate;
    private Button createOfferbtn;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,

                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.create_reward_events, container, false);
        initializeViews(root);
        findCalendarAndsetText();
        return root;
    }

    private void initializeViews(View root) {

        FloatingActionButton floatingActionButton = ((HomeActivity) getActivity()).getFloatingActionButton();
        if(floatingActionButton!=null)
            floatingActionButton.hide();

        createOfferbtn= root.findViewById(R.id.createOfferBtn);
        cStartDate = root.findViewById(R.id.cStartDate);
        cEndDate = root.findViewById(R.id.cEndDate);

        createOfferbtn.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Creating Offer...", Toast.LENGTH_SHORT).show();
        });
    }


    //******************DATE********************//
    private void findCalendarAndsetText() {
        Calendar startDateCalendar, endDateCalendar;
        startDateCalendar = Calendar.getInstance();
        endDateCalendar = Calendar.getInstance();


        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            startDateCalendar.set(Calendar.YEAR, year);
            startDateCalendar.set(Calendar.MONTH, monthOfYear);
            startDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(startDateCalendar, true, false);
        };

        cStartDate.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new DatePickerDialog(getActivity(), date, startDateCalendar
                    .get(Calendar.YEAR), startDateCalendar.get(Calendar.MONTH),
                    startDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        DatePickerDialog.OnDateSetListener endDate = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            endDateCalendar.set(Calendar.YEAR, year);
            endDateCalendar.set(Calendar.MONTH, monthOfYear);
            endDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(endDateCalendar, false, true);
        };

        cEndDate.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new DatePickerDialog(getActivity(), endDate, endDateCalendar
                    .get(Calendar.YEAR), endDateCalendar.get(Calendar.MONTH),
                    endDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }
    private void updateLabel(Calendar myCalendar, boolean start, boolean end) {
        String myFormat = "dd MMMM, yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if (start)
            cStartDate.setText(sdf.format(myCalendar.getTime()));
        if (end)
            cEndDate.setText(sdf.format(myCalendar.getTime()));
    }

}