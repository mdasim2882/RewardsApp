package com.creator.rewardsapp.Body.OfferWalls.Interfaces;

import androidx.annotation.Keep;

import com.creator.rewardsapp.Common.ParticipateOfferObject;

import java.util.List;

@Keep
public interface LoadWinners {

    void onLoadWinnersSuccess(List<ParticipateOfferObject> templates);

    void onLoadWinnersFailed(String message);
}