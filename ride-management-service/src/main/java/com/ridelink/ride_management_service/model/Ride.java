package com.ridelink.ride_management_service.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "rides")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ride {

    @Id
    private String id;

    @NotBlank(message = "Passenger ID must not be blank")
    private String passengerId;

    private String driverId;

    @NotBlank(message = "Pickup location must not be blank")
    private String pickupLocation;

    @NotBlank(message = "Dropoff location must not be blank")
    private String dropoffLocation;

    private String status;

    @NotNull(message = "Fare must not be null")
    private Double fare;

    private LocalDateTime requestedAt;
}
