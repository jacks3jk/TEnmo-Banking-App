package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransactionDAO implements TransactionDAO {


    private JdbcTemplate jdbcTemplate;

    public JdbcTransactionDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transaction> list(Principal principal) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT u.username, t.transfer_id, t.transfer_type_id, t.transfer_status_id, t.account_from, t.account_to, t.amount " +
                     "FROM users AS u " +
                     "JOIN accounts AS a ON u.user_id = a.user_id " +
                     "JOIN transfers AS t ON t.account_from = a.account_id " +
                     "WHERE u.username = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, principal.getName());
        while (rowSet.next()) {
            Transaction transaction = new Transaction();
            transaction.setUserNameFrom(rowSet.getString("username"));
//            transaction.setUsernameTo();
            transaction.setTransferId(rowSet.getInt("transfer_id"));
            transaction.setTransferTypeId(rowSet.getInt("transfer_type_id"));
            transaction.setTransferStatusId(rowSet.getInt("transfer_status_id"));
            transaction.setAmount(rowSet.getBigDecimal("amount"));
            transaction.setAccountFromId(rowSet.getInt("account_from"));
            transaction.setAccountToId(rowSet.getInt("account_to"));
            transactions.add(transaction);
        }
        return transactions;
    }

    @Override
    public Transaction getTransaction(int transactionId) {
        String sql = "SELECT u.username, t.transfer_id, t.transfer_type_id, t.transfer_status_id, t.account_from, t.account_to, t.amount " +
                     "FROM users AS u " +
                     "JOIN accounts AS a ON u.user_id = a.user_id " +
                     "JOIN transfers AS t ON t.account_from = a.account_id " +
                     "WHERE t.transfer_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, transactionId);
        Transaction transaction = new Transaction();
        while (rowSet.next()) {
            transaction.setUserNameFrom(rowSet.getString("username"));
            transaction.setTransferId(rowSet.getInt("transfer_id"));
            transaction.setTransferTypeId(rowSet.getInt("transfer_type_id"));
            transaction.setTransferStatusId(rowSet.getInt("transfer_status_id"));
            transaction.setAmount(rowSet.getBigDecimal("amount"));
            transaction.setAccountFromId(rowSet.getInt("account_from"));
            transaction.setAccountToId(rowSet.getInt("account_to"));
        }
        return transaction;
    }


    @Override
    public void transfer(Transaction transaction) throws Exception {
        //creating transaction
        int accountFromId = transaction.getAccountFromId();
        int accountToId = transaction.getAccountToId();
        BigDecimal amount = transaction.getAmount();
        int transferTypeId = transaction.getTransferTypeId();
        int transferStatusId = transaction.getTransferStatusId();

        //Withdraw
        if (withdraw(accountFromId,amount)) {
            transaction.setTransferTypeId(2);
            transaction.setTransferStatusId(2);
        } else {
            transaction.setTransferStatusId(3);
        }

        //Deposit
        deposit(accountToId, amount);

        //Insert Into Transfer Table
        logTransfer(transaction.getAccountFromId(), transaction.getAccountToId(), transaction.getAmount(),
                    transaction.getTransferTypeId(), transaction.getTransferStatusId());

    }

    @Override
    public void requestTransfer(Transaction transaction) {
        String sql = "INSERT INTO transfers (account_from, account_to, amount, transfer_type_id, transfer_status_id) " +
                "VALUES (?,?,?,?,?);";
        jdbcTemplate.update(sql, transaction.getAccountFromId(), transaction.getAccountToId(), transaction.getAmount(),
                                                transaction.getTransferTypeId(),transaction.getTransferStatusId());
    }

    @Override
    public List<Transaction> listPending(Principal principal) {
        List<Transaction> pendingTransactions = new ArrayList<>();
        String sql = "SELECT a.account_id, u.username, t.amount, t.transfer_id FROM users AS u JOIN accounts AS a ON u.user_id = a.user_id " +
                     "JOIN transfers AS t ON a.account_id = t.account_from " +
                     "WHERE transfer_status_id = 1 AND u.username = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, principal.getName());
        while (rowSet.next()) {
            Transaction transaction = new Transaction();
            transaction.setUserNameFrom(rowSet.getString("username"));
            transaction.setAccountFromId(rowSet.getInt("account_id"));
            transaction.setAmount(rowSet.getBigDecimal("amount"));
            transaction.setTransferId(rowSet.getInt("transfer_id"));
            pendingTransactions.add(transaction);
        }
        return pendingTransactions;
    }

    public boolean withdraw(int accountFromId, BigDecimal amount) throws Exception {
        boolean success = false;
        SqlRowSet rowSetUserOne = jdbcTemplate.queryForRowSet("SELECT balance FROM accounts WHERE account_id = ?;", accountFromId);
        BigDecimal accountFromBalance = new BigDecimal("0.00");
        while (rowSetUserOne.next()) {
            accountFromBalance = accountFromBalance.add(rowSetUserOne.getBigDecimal("balance"));
            if (accountFromBalance.compareTo(amount) > 0) {
                accountFromBalance = accountFromBalance.subtract(amount);
                success = true;
            } else {
                throw new Exception("You don't have enough TE Bucks to transfer.");
            }
            String sqlWithdraw = "UPDATE accounts SET balance = ? WHERE account_id = ?;";
            jdbcTemplate.update(sqlWithdraw, accountFromBalance, accountFromId);
        }
        return success;
    }

    public void deposit(int accountToId, BigDecimal amount) {
        BigDecimal accountToBalance = new BigDecimal("0.00");
        SqlRowSet rowSetUserTwo = jdbcTemplate.queryForRowSet("SELECT balance FROM accounts WHERE account_id = ?;", accountToId);
        while (rowSetUserTwo.next()) {
            accountToBalance = accountToBalance.add(rowSetUserTwo.getBigDecimal("balance")).add(amount);
        }
        String sqlDeposit = "UPDATE accounts SET balance = ? WHERE account_id = ?;";
        jdbcTemplate.update(sqlDeposit, accountToBalance, accountToId);
    }

    public void logTransfer(int accountFromId, int accountToId, BigDecimal amount, int transferTypeId, int transferStatusId) {

        String sql = "INSERT INTO transfers (account_from, account_to, amount, transfer_type_id, transfer_status_id) " +
                     "VALUES (?,?,?,?,?);";

        try {
            jdbcTemplate.update(sql, accountFromId, accountToId, amount, transferTypeId, transferStatusId);
        } catch (Exception e) {
            System.out.println("ERROR!!");
        }

    }

}






