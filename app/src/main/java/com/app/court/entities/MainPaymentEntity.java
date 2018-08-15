package com.app.court.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MainPaymentEntity {


    @SerializedName("due_payment")
    @Expose
    private ArrayList<PaymentEntity> duePayment;
    @SerializedName("paid_payment")
    @Expose
    private ArrayList<PaymentEntity> paidPayment;

    public ArrayList<PaymentEntity> getDuePayment() {
        return duePayment;
    }

    public void setDuePayment(ArrayList<PaymentEntity> duePayment) {
        this.duePayment = duePayment;
    }

    public ArrayList<PaymentEntity> getPaidPayment() {
        return paidPayment;
    }

    public void setPaidPayment(ArrayList<PaymentEntity> paidPayment) {
        this.paidPayment = paidPayment;
    }
}
