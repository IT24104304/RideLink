package com.ridelink.account_service.service;

import com.ridelink.account_service.model.Account;
import com.ridelink.account_service.model.AccountRole;
import com.ridelink.account_service.model.AccountStatus;
import com.ridelink.account_service.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account registerAccount(Account account) {
        if (accountRepository.existsByEmail(account.getEmail())) {
            throw new IllegalStateException("Email already registered");
        }
        account.setStatus(AccountStatus.ACTIVE);
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());
        return accountRepository.save(account);
    }

    public Account getAccountById(String id) {
        return accountRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("Account not found"));
    }

    public Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalStateException("Account not found"));
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account updateProfile(String id, Account updatedAccount) {
        Account existing = getAccountById(id);
        existing.setFullName(updatedAccount.getFullName());
        existing.setPhoneNumber(updatedAccount.getPhoneNumber());
        existing.setUpdatedAt(LocalDateTime.now());
        return accountRepository.save(existing);
    }

    public Account updateAccountStatus(String id, AccountStatus status) {
        Account existing = getAccountById(id);
        existing.setStatus(status);
        existing.setUpdatedAt(LocalDateTime.now());
        return accountRepository.save(existing);
    }

    public Account updateAccountRole(String id, AccountRole role) {
        Account existing = getAccountById(id);
        existing.setRole(role);
        existing.setUpdatedAt(LocalDateTime.now());
        return accountRepository.save(existing);
    }

    public void deleteAccount(String id) {
        if (!accountRepository.existsById(id)) {
            throw new IllegalStateException("Account not found");
        }
        accountRepository.deleteById(id);
    }
}
