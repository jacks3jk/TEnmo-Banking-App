package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class Transaction {

    private String userName;
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



    public Transaction(Integer accountFromId, int accountToId, BigDecimal amount, int transferTypeId, int transferStatusId) {
        this.accountFromId = accountFromId;
        this.accountToId = accountToId;
        this.amount = amount;
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
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

    public String getUserName() {
        return userName;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    @Override
    public String toString() {
        return "Transfers {" +
                accountFromId + ", " + accountToId + ", " + amount + "}";
    }
}
