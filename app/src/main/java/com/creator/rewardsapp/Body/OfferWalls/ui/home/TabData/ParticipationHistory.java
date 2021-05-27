package com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.creator.rewardsapp.R;

public class ParticipationHistory extends Fragment {
    static ParticipationHistory instance;

    public static final String ARG_OBJECT = "object";

    public static ParticipationHistory getInstance() {
        if (instance == null)
            instance = new ParticipationHistory();
        return instance;
    }

    public ParticipationHistory() {

    }
    @Nullable

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.participation_history, container, false);

        return root;
    }



}
