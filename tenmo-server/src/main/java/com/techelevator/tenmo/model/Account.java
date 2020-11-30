package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {

    private String userName;
    private int accountId;
    private int userId;
    private BigDecimal balance = new BigDecimal("0.00");


    public Account() {}

    public Account(String userName, int accountId, int userId, BigDecimal balance) {
        this.userName = userName;
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    }


    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "Account {" +
                accountId + ", " + userId + ", " + balance + "}";
    }
}
