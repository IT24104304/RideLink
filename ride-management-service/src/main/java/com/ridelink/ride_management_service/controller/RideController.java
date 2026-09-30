package com.ridelink.ride_management_service.controller;

import com.ridelink.ride_management_service.model.Ride;
import com.ridelink.ride_management_service.service.RideService;
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
public class RideController {

    private final RideService rideService;

    @PostMapping
    public ResponseEntity<Ride> createRide(@Valid @RequestBody Ride ride) {
        Ride createdRide = rideService.createRide(ride);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRide);
    }

    @GetMapping
    public ResponseEntity<List<Ride>> getAllRides() {
        return ResponseEntity.ok(rideService.getAllRides());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ride> getRideById(@PathVariable String id) {
        Optional<Ride> rideOptional = rideService.getRideById(id);
        return rideOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ride> updateRide(@PathVariable String id, @Valid @RequestBody Ride ride) {
        Ride updatedRide = rideService.updateRide(id, ride);
        if (updatedRide == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedRide);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRide(@PathVariable String id) {
        boolean deleted = rideService.deleteRide(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
