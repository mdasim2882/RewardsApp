package com.creator.rewardsapp.Common;


public class ParticipateOfferObject
{
    private  String fullname;
    private  String contactno;
    private  String billvalue;
    private  String receiptUrl;

    public ParticipateOfferObject() {
    }

    public ParticipateOfferObject(String fullname, String contactno, String billvalue, String receiptUrl) {
        this.fullname = fullname;
        this.contactno = contactno;
        this.billvalue = billvalue;
        this.receiptUrl = receiptUrl;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public String getBillvalue() {
        return billvalue;
    }

    public void setBillvalue(String billvalue) {
        this.billvalue = billvalue;
    }

    public String getReceiptUrl() {
        return receiptUrl;
    }

    public void setReceiptUrl(String receiptUrl) {
        this.receiptUrl = receiptUrl;
    }

    @Override
    public String toString() {
        return "ParticipateOfferObject{" +
                "fullname='" + fullname + '\'' +
                ", contactno='" + contactno + '\'' +
                ", billvalue='" + billvalue + '\'' +
                ", receiptUrl='" + receiptUrl + '\'' +
                '}';
    }
}