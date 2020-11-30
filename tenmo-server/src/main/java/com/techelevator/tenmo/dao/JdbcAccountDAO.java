package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDAO implements AccountDAO {

    private static final BigDecimal STARTING_BALANCE = new BigDecimal("1000.00");
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Account> findAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT u.username, u.user_id, a.account_id, balance FROM users AS u JOIN accounts AS a ON u.user_id = a.user_id;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        while (rowSet.next()) {
          Account account = new Account();
          account.setUserName(rowSet.getString("username"));
          account.setUserId(rowSet.getInt("user_id"));
          account.setAccountId(rowSet.getInt("account_id"));
          account.setBalance(rowSet.getBigDecimal("balance"));
          accounts.add(account);
        }
        return accounts;
    }

    @Override
    public BigDecimal getBalance(Principal principal) {
        BigDecimal balance = new BigDecimal("0.00");
        String sql = "SELECT balance FROM accounts AS a JOIN users AS u ON a.user_id = u.user_id " +
                "WHERE username = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, principal.getName());
        if (rowSet.next()) {
            balance = rowSet.getBigDecimal("balance");
        }
        return balance;
    }

    @Override
    public Account findAccountById(int accountId) {
        Account account = new Account();
        String sql = "SELECT account_id, user_id, balance FROM accounts " +
                "WHERE account_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, accountId);
        if (rowSet.next()) {
            account = mapRowToAccount(rowSet);
        }
        return account;
    }

    @Override
    public Account findAccountByPrincipal(Principal principal) {
        Account account = new Account();
        String sql = "SELECT a.account_id, u.user_id, a.balance " +
                "FROM users AS u " +
                "JOIN accounts AS a ON u.user_id = a.user_id " +
                "WHERE u.username = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, principal.getName());
        if (rowSet.next()) {
            account = mapRowToAccount(rowSet);
        }
        return account;
    }


    private Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();
        account.setAccountId(rowSet.getInt("account_id"));
        account.setUserId(rowSet.getInt("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        return account;
    }
}
