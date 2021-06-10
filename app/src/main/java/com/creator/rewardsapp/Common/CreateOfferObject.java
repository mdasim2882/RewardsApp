package com.creator.rewardsapp.Common;

import java.util.List;

public class CreateOfferObject {
    private boolean winnerDeclared;
    private String startDate;
    private String endDate;
    private String shopname;
    private String contactno;
    private Long maxParticipants;
    private String offerPrice;
    private String offerProduct;
    private String firstOffer;
    private String secondOffer;
    private List<String> winnerList;
    private String offerId;
    private String creatorId;

    public CreateOfferObject() {
    }

    public CreateOfferObject(boolean winnerDeclared, String startDate, String endDate, String shopname, String contactno, Long maxParticipants, String offerPrice, String offerProduct, String firstOffer, String secondOffer, List<String> winnerList, String offerId, String creatorId) {
        this.winnerDeclared = winnerDeclared;
        this.startDate = startDate;
        this.endDate = endDate;
        this.shopname = shopname;
        this.contactno = contactno;
        this.maxParticipants = maxParticipants;
        this.offerPrice = offerPrice;
        this.offerProduct = offerProduct;
        this.firstOffer = firstOffer;
        this.secondOffer = secondOffer;
        this.winnerList = winnerList;
        this.offerId = offerId;
        this.creatorId = creatorId;
    }

    public boolean isWinnerDeclared() {
        return winnerDeclared;
    }

    public void setWinnerDeclared(boolean winnerDeclared) {
        this.winnerDeclared = winnerDeclared;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public Long getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Long maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getOfferProduct() {
        return offerProduct;
    }

    public void setOfferProduct(String offerProduct) {
        this.offerProduct = offerProduct;
    }

    public String getFirstOffer() {
        return firstOffer;
    }

    public void setFirstOffer(String firstOffer) {
        this.firstOffer = firstOffer;
    }

    public String getSecondOffer() {
        return secondOffer;
    }

    public void setSecondOffer(String secondOffer) {
        this.secondOffer = secondOffer;
    }

    public List<String> getWinnerList() {
        return winnerList;
    }

    public void setWinnerList(List<String> winnerList) {
        this.winnerList = winnerList;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public String toString() {
        return "CreateOfferObject{" +
                "winnerDeclared=" + winnerDeclared +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", shopname='" + shopname + '\'' +
                ", contactno='" + contactno + '\'' +
                ", maxParticipants=" + maxParticipants +
                ", offerPrice='" + offerPrice + '\'' +
                ", offerProduct='" + offerProduct + '\'' +
                ", firstOffer='" + firstOffer + '\'' +
                ", secondOffer='" + secondOffer + '\'' +
                ", winnerList=" + winnerList +
                ", offerId='" + offerId + '\'' +
                ", creatorId='" + creatorId + '\'' +
                '}';
    }
}