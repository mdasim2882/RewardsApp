package com.creator.rewardsapp.Body.OfferWalls.Interfaces;

import androidx.annotation.Keep;

import com.creator.rewardsapp.Common.CreateOfferObject;

import java.util.List;

@Keep
public interface LoadNearbyEvents {

    void onNearbyLoadSuccess(List<CreateOfferObject> templates);

    void onNearbyLoadFailed(String message);
}