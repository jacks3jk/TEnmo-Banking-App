package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.TransactionDAO;
import com.techelevator.tenmo.model.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransactionController {

    private String path = "localhost:8080/accounts";
    private TransactionDAO transactionDAO;

    public TransactionController(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    @PostMapping("/transfer")
    @ResponseStatus(value = HttpStatus.ACCEPTED, reason="Approved!")
    public void transferTEBucks(@RequestBody Transaction transaction) throws Exception {
        transactionDAO.transfer(transaction);
    }

    @GetMapping("/list")
    public List<Transaction> fetchAllTransactions(Principal principal) {
        return transactionDAO.list(principal);
    }

    @GetMapping ("/list/{transactionId}")
    public Transaction fetchTransaction(@PathVariable int transactionId) {
        return transactionDAO.getTransaction(transactionId);
    }

    @PostMapping("/transfer/pending")
    public void postingRequestTransfer(@Valid @RequestBody Transaction transaction) {
        transactionDAO.requestTransfer(transaction);
    }

    @GetMapping("/transfer/pending")
    public List<Transaction> fetchMyPendingTransfers(Principal principal) {
        return transactionDAO.listPending(principal);
    }

}
