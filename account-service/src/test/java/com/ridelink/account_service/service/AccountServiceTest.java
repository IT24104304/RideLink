package com.ridelink.account_service.service;

import com.ridelink.account_service.dto.*;
import com.ridelink.account_service.mapper.AccountMapper;
import com.ridelink.account_service.model.Account;
import com.ridelink.account_service.model.AccountRole;
import com.ridelink.account_service.model.AccountStatus;
import com.ridelink.account_service.repository.AccountRepository;
import com.ridelink.account_service.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AccountService accountService;

    private Account mockAccount;
    private RegisterRequest mockRegisterRequest;
    private LoginRequest mockLoginRequest;

    @BeforeEach
    void setUp() {
        mockAccount = new Account();
        mockAccount.setId("acc-123");
        mockAccount.setFullName("John Doe");
        mockAccount.setEmail("john@example.com");
        mockAccount.setPassword("hashed-password");
        mockAccount.setRole(AccountRole.PASSENGER);
        mockAccount.setStatus(AccountStatus.ACTIVE);
        mockAccount.setCreatedAt(LocalDateTime.now());
        mockAccount.setUpdatedAt(LocalDateTime.now());

        mockRegisterRequest = new RegisterRequest();
        mockRegisterRequest.setFullName("John Doe");
        mockRegisterRequest.setEmail("john@example.com");
        mockRegisterRequest.setPassword("raw-password");
        mockRegisterRequest.setPhoneNumber("1234567890");
        mockRegisterRequest.setRole(AccountRole.PASSENGER);

        mockLoginRequest = new LoginRequest();
        mockLoginRequest.setEmail("john@example.com");
        mockLoginRequest.setPassword("raw-password");
    }

    @Test
    void registerAccount_success() {
        when(accountRepository.existsByEmail(anyString())).thenReturn(false);
        when(accountMapper.toEntity(any(RegisterRequest.class))).thenReturn(mockAccount);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed-password");
        when(accountRepository.save(any(Account.class))).thenReturn(mockAccount);
        
        AccountResponse mockResponse = new AccountResponse();
        mockResponse.setId("acc-123");
        when(accountMapper.toResponse(any(Account.class))).thenReturn(mockResponse);

        AccountResponse response = accountService.registerAccount(mockRegisterRequest);

        assertNotNull(response);
        assertEquals("acc-123", response.getId());
        verify(accountRepository).save(any(Account.class));
        verify(passwordEncoder).encode("raw-password");
    }

    @Test
    void registerAccount_duplicateEmail() {
        when(accountRepository.existsByEmail(anyString())).thenReturn(true);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            accountService.registerAccount(mockRegisterRequest);
        });

        assertEquals("Email already registered", exception.getMessage());
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void registerAccount_adminRoleRejected() {
        mockRegisterRequest.setRole(AccountRole.ADMIN);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            accountService.registerAccount(mockRegisterRequest);
        });

        assertEquals("Invalid registration role", exception.getMessage());
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void login_success() {
        when(accountRepository.findByEmail(anyString())).thenReturn(Optional.of(mockAccount));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtService.generateToken(any(Account.class))).thenReturn("mock-jwt-token");

        LoginResponse response = accountService.login(mockLoginRequest);

        assertNotNull(response);
        assertEquals("mock-jwt-token", response.getToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals("acc-123", response.getAccountId());
        assertEquals("john@example.com", response.getEmail());
        assertEquals(AccountRole.PASSENGER, response.getRole());
        assertEquals(AccountStatus.ACTIVE, response.getStatus());
    }

    @Test
    void login_invalidPassword() {
        when(accountRepository.findByEmail(anyString())).thenReturn(Optional.of(mockAccount));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            accountService.login(mockLoginRequest);
        });

        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void login_accountNotFound() {
        when(accountRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            accountService.login(mockLoginRequest);
        });

        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void login_inactiveAccount() {
        mockAccount.setStatus(AccountStatus.INACTIVE);
        when(accountRepository.findByEmail(anyString())).thenReturn(Optional.of(mockAccount));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            accountService.login(mockLoginRequest);
        });

        assertEquals("Account is not active", exception.getMessage());
    }

    @Test
    void getAccountById_success() {
        when(accountRepository.findById("acc-123")).thenReturn(Optional.of(mockAccount));
        when(accountMapper.toResponse(mockAccount)).thenReturn(new AccountResponse());

        AccountResponse response = accountService.getAccountById("acc-123");

        assertNotNull(response);
    }

    @Test
    void getAccountById_notFound() {
        when(accountRepository.findById("acc-999")).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            accountService.getAccountById("acc-999");
        });

        assertEquals("Account not found", exception.getMessage());
    }

    @Test
    void updateProfile_success() {
        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setFullName("Jane Doe");
        request.setPhoneNumber("0987654321");

        when(accountRepository.findById("acc-123")).thenReturn(Optional.of(mockAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(mockAccount);
        when(accountMapper.toResponse(any(Account.class))).thenReturn(new AccountResponse());

        AccountResponse response = accountService.updateProfile("acc-123", request);

        assertNotNull(response);
        assertEquals("Jane Doe", mockAccount.getFullName());
        assertEquals("0987654321", mockAccount.getPhoneNumber());
        assertEquals("john@example.com", mockAccount.getEmail());
        assertEquals(AccountRole.PASSENGER, mockAccount.getRole());
        assertEquals(AccountStatus.ACTIVE, mockAccount.getStatus());
        verify(accountRepository).save(mockAccount);
    }

    @Test
    void updateAccountStatus_success() {
        UpdateStatusRequest request = new UpdateStatusRequest();
        request.setStatus(AccountStatus.SUSPENDED);

        when(accountRepository.findById("acc-123")).thenReturn(Optional.of(mockAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(mockAccount);
        when(accountMapper.toResponse(any(Account.class))).thenReturn(new AccountResponse());

        AccountResponse response = accountService.updateAccountStatus("acc-123", request);

        assertNotNull(response);
        assertEquals(AccountStatus.SUSPENDED, mockAccount.getStatus());
        verify(accountRepository).save(mockAccount);
    }

    @Test
    void updateAccountRole_success() {
        UpdateRoleRequest request = new UpdateRoleRequest();
        request.setRole(AccountRole.DRIVER);

        when(accountRepository.findById("acc-123")).thenReturn(Optional.of(mockAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(mockAccount);
        when(accountMapper.toResponse(any(Account.class))).thenReturn(new AccountResponse());

        AccountResponse response = accountService.updateAccountRole("acc-123", request);

        assertNotNull(response);
        assertEquals(AccountRole.DRIVER, mockAccount.getRole());
        verify(accountRepository).save(mockAccount);
    }

    @Test
    void deleteAccount_success() {
        when(accountRepository.existsById("acc-123")).thenReturn(true);

        assertDoesNotThrow(() -> {
            accountService.deleteAccount("acc-123");
        });

        verify(accountRepository).deleteById("acc-123");
    }

    @Test
    void deleteAccount_notFound() {
        when(accountRepository.existsById("acc-999")).thenReturn(false);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            accountService.deleteAccount("acc-999");
        });

        assertEquals("Account not found", exception.getMessage());
        verify(accountRepository, never()).deleteById(anyString());
    }
}
