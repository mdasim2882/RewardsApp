package com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.creator.rewardsapp.R;

public class NearbyRewardEvents extends Fragment {
    static NearbyRewardEvents instance;

    public static final String ARG_OBJECT = "object";

    public NearbyRewardEvents() {
    }

    public static NearbyRewardEvents getInstance() {
        if (instance == null)
            instance = new NearbyRewardEvents();
        return instance;
    }

    @Nullable

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.nearby_reward_events, container, false);

        return root;
    }




}
