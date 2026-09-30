package com.ridelink.ride_management_service.service;

import com.ridelink.ride_management_service.model.Ride;
import com.ridelink.ride_management_service.model.RideStatus;
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
        ride.setStatus(RideStatus.REQUESTED);
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
            existingRide.setFare(updatedRide.getFare());
            return rideRepository.save(existingRide);
        }
        return null;
    }

    public Ride updateRideStatus(String id, RideStatus newStatus) {
        Optional<Ride> existingRideOptional = rideRepository.findById(id);
        if (existingRideOptional.isEmpty()) {
            return null;
        }

        Ride existingRide = existingRideOptional.get();
        RideStatus currentStatus = existingRide.getStatus();

        if (!isValidTransition(currentStatus, newStatus)) {
            throw new IllegalStateException("Invalid status transition from " + currentStatus + " to " + newStatus);
        }

        existingRide.setStatus(newStatus);
        return rideRepository.save(existingRide);
    }

    private boolean isValidTransition(RideStatus currentStatus, RideStatus newStatus) {
        if (currentStatus == null || newStatus == null) {
            return false;
        }
        return switch (currentStatus) {
            case REQUESTED -> newStatus == RideStatus.ACCEPTED || newStatus == RideStatus.CANCELLED;
            case ACCEPTED -> newStatus == RideStatus.STARTED || newStatus == RideStatus.CANCELLED;
            case STARTED -> newStatus == RideStatus.COMPLETED;
            default -> false;
        };
    }

    public boolean deleteRide(String id) {
        if (rideRepository.existsById(id)) {
            rideRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
