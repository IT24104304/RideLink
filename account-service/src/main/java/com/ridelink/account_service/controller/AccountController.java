package com.ridelink.account_service.controller;

import com.ridelink.account_service.dto.AccountResponse;
import com.ridelink.account_service.dto.LoginRequest;
import com.ridelink.account_service.dto.LoginResponse;
import com.ridelink.account_service.dto.RegisterRequest;
import com.ridelink.account_service.dto.UpdateProfileRequest;
import com.ridelink.account_service.dto.UpdateRoleRequest;
import com.ridelink.account_service.dto.UpdateStatusRequest;
import com.ridelink.account_service.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@Tag(
    name = "Account Management",
    description = "APIs for account registration, login, profile management, role management and account status management"
)
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a passenger or driver account")
    @ApiResponse(responseCode = "201", description = "Created")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    public ResponseEntity<AccountResponse> registerAccount(@Valid @RequestBody RegisterRequest request) {
        return new ResponseEntity<>(accountService.registerAccount(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "Login and receive JWT token")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(accountService.login(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get account profile by ID")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "Not Found")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable String id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @GetMapping
    @Operation(summary = "Get all accounts")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @PutMapping("/{id}/profile")
    @Operation(summary = "Update account profile")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "404", description = "Not Found")
    public ResponseEntity<AccountResponse> updateProfile(
            @PathVariable String id,
            @Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(accountService.updateProfile(id, request));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update account status")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "404", description = "Not Found")
    public ResponseEntity<AccountResponse> updateAccountStatus(
            @PathVariable String id,
            @Valid @RequestBody UpdateStatusRequest request) {
        return ResponseEntity.ok(accountService.updateAccountStatus(id, request));
    }

    @PatchMapping("/{id}/role")
    @Operation(summary = "Update account role")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "404", description = "Not Found")
    public ResponseEntity<AccountResponse> updateAccountRole(
            @PathVariable String id,
            @Valid @RequestBody UpdateRoleRequest request) {
        return ResponseEntity.ok(accountService.updateAccountRole(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an account")
    @ApiResponse(responseCode = "204", description = "No Content")
    @ApiResponse(responseCode = "404", description = "Not Found")
    public ResponseEntity<Void> deleteAccount(@PathVariable String id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}
