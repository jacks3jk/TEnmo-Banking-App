package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transaction {

    private String userNameFrom;
    private String usernameTo;
    private int accountFromId;
    private int accountToId;
    private BigDecimal amount;
    private int transferTypeId;
    private int transferStatusId;
    private int transferId;


    public Transaction(){}

    public Transaction(int accountFromId, int accountToId, BigDecimal amount) {
        this.accountFromId = accountFromId;
        this.accountToId = accountToId;
    }

    public int getAccountFromId() {
        return accountFromId;
    }

    public void setAccountFromId(int accountFromId) {
        this.accountFromId = accountFromId;
    }

    public int getAccountToId() {
        return accountToId;
    }

    public void setAccountToId(int accountToId) {
        this.accountToId = accountToId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public String getUserNameFrom() {
        return userNameFrom;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setUserNameFrom(String userNameFrom) {
        this.userNameFrom = userNameFrom;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public String getUsernameTo() {
        return usernameTo;
    }
    public void setUsernameTo(String usernameTo) {
        this.usernameTo = usernameTo;
    }
}
