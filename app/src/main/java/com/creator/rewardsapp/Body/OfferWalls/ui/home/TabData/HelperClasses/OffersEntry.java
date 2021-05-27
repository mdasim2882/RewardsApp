package com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.HelperClasses;

public class OffersEntry {
    private  String offerShopName;

    public OffersEntry() {
    }

    public OffersEntry(String offerShopName) {
        this.offerShopName = offerShopName;
    }

    public String getOfferShopName() {
        return offerShopName;
    }

    public void setOfferShopName(String offerShopName) {
        this.offerShopName = offerShopName;
    }

    @Override
    public String toString() {
        return "OffersEntry{" +
                "offerShopName='" + offerShopName + '\'' +
                '}';
    }
}
