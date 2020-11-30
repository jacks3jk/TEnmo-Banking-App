package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transaction;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class AccountService {


    public HttpEntity<Transaction> getEntity(AuthenticatedUser currentUser, Transaction transaction) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity<Transaction> entity =  new HttpEntity<>(transaction, headers);
        return entity;

    }

    public HttpEntity<?> getEntity(AuthenticatedUser currentUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(headers);
    }

    public String handleSendTEBucksResponse() {

        return "";
    }


}
