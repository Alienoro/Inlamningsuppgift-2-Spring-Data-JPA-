package se.yrgo.bilverkstaden.dto;

import java.util.List;

public record VehicleDto(
        Long id,
        String registrationNumber,
        String brand,
        String model,
        int productionYear,
        Long customerId
) {
}