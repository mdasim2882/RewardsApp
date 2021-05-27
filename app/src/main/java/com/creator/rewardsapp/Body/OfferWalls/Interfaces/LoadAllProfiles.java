package com.creator.rewardsapp.Body.OfferWalls.Interfaces;

import androidx.annotation.Keep;


import com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.HelperClasses.OffersEntry;

import java.util.List;

@Keep
public interface LoadAllProfiles {

    void onProfilesLoadSuccess(List<OffersEntry> templates);

    void onProfilesLoadFailure(String message);
}