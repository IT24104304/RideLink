package com.ridelink.ride_management_service.service;

import com.ridelink.ride_management_service.model.Ride;
import com.ridelink.ride_management_service.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RideService {

    private final RideRepository rideRepository;

    public Ride createRide(Ride ride) {
        if (ride.getStatus() == null || ride.getStatus().isBlank()) {
            ride.setStatus("REQUESTED");
        }
        ride.setRequestedAt(LocalDateTime.now());
        return rideRepository.save(ride);
    }

    public List<Ride> getAllRides() {
        return rideRepository.findAll();
    }

    public Optional<Ride> getRideById(String id) {
        return rideRepository.findById(id);
    }

    public Ride updateRide(String id, Ride updatedRide) {
        Optional<Ride> existingRideOptional = rideRepository.findById(id);
        if (existingRideOptional.isPresent()) {
            Ride existingRide = existingRideOptional.get();
            existingRide.setPassengerId(updatedRide.getPassengerId());
            existingRide.setDriverId(updatedRide.getDriverId());
            existingRide.setPickupLocation(updatedRide.getPickupLocation());
            existingRide.setDropoffLocation(updatedRide.getDropoffLocation());
            existingRide.setStatus(updatedRide.getStatus());
            existingRide.setFare(updatedRide.getFare());
            return rideRepository.save(existingRide);
        }
        return null;
    }

    public boolean deleteRide(String id) {
        if (rideRepository.existsById(id)) {
            rideRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
