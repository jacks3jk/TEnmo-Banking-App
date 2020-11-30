package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public interface AccountDAO {

    List<Account> findAllAccounts();

    BigDecimal getBalance(Principal principal);

    Account findAccountById(int accountId);

    Account findAccountByPrincipal(Principal principal);

}
