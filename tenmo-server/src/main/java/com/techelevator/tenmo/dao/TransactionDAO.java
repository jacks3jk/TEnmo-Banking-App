package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transaction;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;


public interface TransactionDAO {

    List<Transaction> list(Principal principal);

    Transaction getTransaction(int transactionId);

    void transfer(Transaction transaction) throws Exception;

    void requestTransfer(Transaction transaction);

    List<Transaction> listPending(Principal principal);



}
