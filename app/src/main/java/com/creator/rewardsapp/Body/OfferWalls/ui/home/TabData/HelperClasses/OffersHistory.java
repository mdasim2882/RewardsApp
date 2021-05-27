package com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.HelperClasses;

public class OffersHistory {
    private  String offerShopName;

    public OffersHistory() {
    }

    public String getOfferShopName() {
        return offerShopName;
    }

    public void setOfferShopName(String offerShopName) {
        this.offerShopName = offerShopName;
    }

    public OffersHistory(String offerShopName) {
        this.offerShopName = offerShopName;
    }

    @Override
    public String toString() {
        return "OffersHistory{" +
                "offerShopName='" + offerShopName + '\'' +
                '}';
    }
}
