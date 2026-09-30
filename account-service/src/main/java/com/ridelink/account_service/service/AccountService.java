package com.ridelink.account_service.service;

import com.ridelink.account_service.dto.AccountResponse;
import com.ridelink.account_service.dto.LoginRequest;
import com.ridelink.account_service.dto.LoginResponse;
import com.ridelink.account_service.dto.RegisterRequest;
import com.ridelink.account_service.dto.UpdateProfileRequest;
import com.ridelink.account_service.dto.UpdateRoleRequest;
import com.ridelink.account_service.dto.UpdateStatusRequest;
import com.ridelink.account_service.mapper.AccountMapper;
import com.ridelink.account_service.model.Account;
import com.ridelink.account_service.model.AccountRole;
import com.ridelink.account_service.model.AccountStatus;
import com.ridelink.account_service.repository.AccountRepository;
import com.ridelink.account_service.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AccountService(AccountRepository accountRepository, AccountMapper accountMapper, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AccountResponse registerAccount(RegisterRequest request) {
        if (request.getRole() != AccountRole.PASSENGER && request.getRole() != AccountRole.DRIVER) {
            throw new IllegalStateException("Invalid registration role");
        }

        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Email already registered");
        }
        
        Account account = accountMapper.toEntity(request);
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setStatus(AccountStatus.ACTIVE);
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());
        
        Account savedAccount = accountRepository.save(account);
        return accountMapper.toResponse(savedAccount);
    }

    public LoginResponse login(LoginRequest request) {
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalStateException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new IllegalStateException("Invalid email or password");
        }

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Account is not active");
        }

        String token = jwtService.generateToken(account);
        return new LoginResponse(
                token,
                "Bearer",
                account.getId(),
                account.getEmail(),
                account.getRole(),
                account.getStatus()
        );
    }

    public AccountResponse getAccountById(String id) {
        return accountMapper.toResponse(findAccountEntityById(id));
    }

    public AccountResponse getAccountByEmail(String email) {
        Account account = accountRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalStateException("Account not found"));
        return accountMapper.toResponse(account);
    }

    public List<AccountResponse> getAllAccounts() {
        return accountRepository.findAll().stream()
            .map(accountMapper::toResponse)
            .collect(Collectors.toList());
    }

    public AccountResponse updateProfile(String id, UpdateProfileRequest request) {
        Account existing = findAccountEntityById(id);
        existing.setFullName(request.getFullName());
        existing.setPhoneNumber(request.getPhoneNumber());
        existing.setUpdatedAt(LocalDateTime.now());
        
        Account updatedAccount = accountRepository.save(existing);
        return accountMapper.toResponse(updatedAccount);
    }

    public AccountResponse updateAccountStatus(String id, UpdateStatusRequest request) {
        Account existing = findAccountEntityById(id);
        existing.setStatus(request.getStatus());
        existing.setUpdatedAt(LocalDateTime.now());
        
        Account updatedAccount = accountRepository.save(existing);
        return accountMapper.toResponse(updatedAccount);
    }

    public AccountResponse updateAccountRole(String id, UpdateRoleRequest request) {
        Account existing = findAccountEntityById(id);
        existing.setRole(request.getRole());
        existing.setUpdatedAt(LocalDateTime.now());
        
        Account updatedAccount = accountRepository.save(existing);
        return accountMapper.toResponse(updatedAccount);
    }

    public void deleteAccount(String id) {
        if (!accountRepository.existsById(id)) {
            throw new IllegalStateException("Account not found");
        }
        accountRepository.deleteById(id);
    }

    private Account findAccountEntityById(String id) {
        return accountRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("Account not found"));
    }
}
