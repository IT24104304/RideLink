package com.ridelink.account_service.mapper;

import com.ridelink.account_service.dto.AccountResponse;
import com.ridelink.account_service.dto.RegisterRequest;
import com.ridelink.account_service.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public Account toEntity(RegisterRequest request) {
        if (request == null) {
            return null;
        }
        Account account = new Account();
        account.setFullName(request.getFullName());
        account.setEmail(request.getEmail());
        account.setPhoneNumber(request.getPhoneNumber());
        account.setPassword(request.getPassword());
        account.setRole(request.getRole());
        return account;
    }

    public AccountResponse toResponse(Account account) {
        if (account == null) {
            return null;
        }
        AccountResponse response = new AccountResponse();
        response.setId(account.getId());
        response.setFullName(account.getFullName());
        response.setEmail(account.getEmail());
        response.setPhoneNumber(account.getPhoneNumber());
        response.setRole(account.getRole());
        response.setStatus(account.getStatus());
        response.setCreatedAt(account.getCreatedAt());
        response.setUpdatedAt(account.getUpdatedAt());
        return response;
    }
}
