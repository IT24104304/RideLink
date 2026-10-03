package com.ridelink.account_service.dto;

import com.ridelink.account_service.model.AccountRole;
import com.ridelink.account_service.model.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String tokenType;
    private String accountId;
    private String email;
    private AccountRole role;
    private AccountStatus status;
}
