package se.yrgo.bilverkstaden.dto;

import java.util.List;

public record CustomerWithVehiclesDto(
        Long id,
        String name,
        String phoneNumber,
        List<VehicleDto> vehicles
) {
}