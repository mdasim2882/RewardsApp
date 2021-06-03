package com.creator.rewardsapp.Body.OfferWalls.Interfaces;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public interface LoadMyCreatedEvents {

    void onLoadMyCreatedEventsSuccess(List<String> myevents);

    void onLoadMyCreatedEventsFailed(String message);
}