package com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.creator.rewardsapp.Body.OfferWalls.HomeActivity;
import com.creator.rewardsapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

public class ParticipationHistory extends Fragment {
    static ParticipationHistory instance;


    public static ParticipationHistory getInstance() {
        if (instance == null)
            instance = new ParticipationHistory();
        return instance;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(false);
        super.onCreate(savedInstanceState);
    }

    public ParticipationHistory() {

    }

    @Nullable

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.participation_history, container, false);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        FloatingActionButton floatingActionButton = ((HomeActivity) getActivity()).getFloatingActionButton();
        if (floatingActionButton != null) {
            floatingActionButton.hide();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(null, null);
    }
}
