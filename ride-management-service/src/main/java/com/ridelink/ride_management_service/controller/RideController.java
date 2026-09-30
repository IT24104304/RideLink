package com.ridelink.ride_management_service.controller;

import com.ridelink.ride_management_service.model.Ride;
import com.ridelink.ride_management_service.model.RideStatus;
import com.ridelink.ride_management_service.service.RideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rides")
@RequiredArgsConstructor
@Tag(name = "Ride Management", description = "APIs for managing rides and ride status")
public class RideController {

    private final RideService rideService;

    @PostMapping
    @Operation(summary = "Create a new ride")
    @ApiResponse(responseCode = "201", description = "Successful operation")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    public ResponseEntity<Ride> createRide(@Valid @RequestBody Ride ride) {
        Ride createdRide = rideService.createRide(ride);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRide);
    }

    @GetMapping
    @Operation(summary = "Get all rides")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    public ResponseEntity<List<Ride>> getAllRides() {
        return ResponseEntity.ok(rideService.getAllRides());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ride by ID")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Ride not found")
    public ResponseEntity<Ride> getRideById(@PathVariable String id) {
        Optional<Ride> rideOptional = rideService.getRideById(id);
        return rideOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update ride details")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "Ride not found")
    public ResponseEntity<Ride> updateRide(@PathVariable String id, @Valid @RequestBody Ride ride) {
        Ride updatedRide = rideService.updateRide(id, ride);
        if (updatedRide == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedRide);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update ride status")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "Ride not found")
    public ResponseEntity<Ride> updateRideStatus(@PathVariable String id, @RequestParam RideStatus status) {
        Ride updatedRide = rideService.updateRideStatus(id, status);
        if (updatedRide == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedRide);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a ride")
    @ApiResponse(responseCode = "204", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Ride not found")
    public ResponseEntity<Void> deleteRide(@PathVariable String id) {
        boolean deleted = rideService.deleteRide(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
