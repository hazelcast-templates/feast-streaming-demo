package com.example;

public class Transaction {
    private String acct_num;
    private Long unix_time;
    private int is_fraud;
    private double amt;

    public String getAcct_num() {
        return acct_num;
    }

    public void setAcct_num(String acct_num) {
        this.acct_num = acct_num;
    }

    public Long getUnix_time() {
        return unix_time;
    }

    public void setUnix_time(Long unix_time) {
        this.unix_time = unix_time;
    }

    public int getIs_fraud() {
        return is_fraud;
    }

    public void setIs_fraud(int is_fraud) {
        this.is_fraud = is_fraud;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }
}
