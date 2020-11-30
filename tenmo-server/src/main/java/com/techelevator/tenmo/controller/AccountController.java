package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.model.Account;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {

    private String path = "localhost:8080";
    private AccountDAO accountDAO;

    public AccountController(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    @GetMapping("/accounts")
    public List<Account> fetchAllAccounts() {
        return accountDAO.findAllAccounts();
    }

    @GetMapping("/accounts/user")
    public Account fetchAccount(Principal principal) {
        return accountDAO.findAccountByPrincipal(principal);
    }

    @GetMapping("/accounts/balance")
    public BigDecimal fetchBalance(Principal principal) {
        return accountDAO.getBalance(principal);
    }


}
