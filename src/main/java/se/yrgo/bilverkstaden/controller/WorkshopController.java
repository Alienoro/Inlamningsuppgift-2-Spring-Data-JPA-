package se.yrgo.bilverkstaden.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import se.yrgo.bilverkstaden.Customer;
import se.yrgo.bilverkstaden.Vehicle;
import se.yrgo.bilverkstaden.dto.CustomerWithVehiclesDto;
import se.yrgo.bilverkstaden.dto.VehicleBrandDto;
import se.yrgo.bilverkstaden.dto.VehicleDto;
import se.yrgo.bilverkstaden.repository.CustomerRepository;
import se.yrgo.bilverkstaden.repository.VehicleRepository;

import java.util.List;

@RestController
public class WorkshopController {

    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;

    public WorkshopController(CustomerRepository customerRepository, VehicleRepository vehicleRepository) {
        this.customerRepository = customerRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @PostMapping("/customers")
    public ResponseEntity<CustomerWithVehiclesDto> createCustomer(
            @RequestParam String name,
            @RequestParam String phone) {
        Customer saved = customerRepository.save(new Customer(name, phone));
        return ResponseEntity.status(HttpStatus.CREATED).body(toCustomerDto(saved));
    }

    @GetMapping("/customer-id")
    public ResponseEntity<Long> getCustomerIdByName(@RequestParam String name) {
        return customerRepository.findByName(name)
                .map(c -> ResponseEntity.ok(c.getId()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customers")
    @Transactional(readOnly = true)
    public List<CustomerWithVehiclesDto> getAllCustomersWithVehicles() {
        return customerRepository.findAllCustomersWithVehicles().stream()
                .map(this::toCustomerDto)
                .toList();
    }

    @PostMapping("/vehicles")
    public ResponseEntity<VehicleDto> createVehicle(
            @RequestParam String registrationNumber,
            @RequestParam String brand,
            @RequestParam String model,
            @RequestParam int productionYear) {
        Vehicle saved = vehicleRepository.save(new Vehicle(registrationNumber, brand, model, productionYear));
        return ResponseEntity.status(HttpStatus.CREATED).body(toVehicleDto(saved));
    }

    @GetMapping("/vehicles")
    @Transactional(readOnly = true)
    public List<VehicleDto> getAllVehicles() {
        return vehicleRepository.findAll().stream()
                .map(this::toVehicleDto)
                .toList();
    }

    @GetMapping("/vehicles-by-brand")
    @Transactional(readOnly = true)
    public List<VehicleDto> getVehiclesByBrand(@RequestParam String brand) {
        return vehicleRepository.findByBrand(brand).stream()
                .map(this::toVehicleDto)
                .toList();
    }

    @PostMapping("/addvehicletocustomer")
    @Transactional
    public ResponseEntity<VehicleDto> addVehicleToCustomer(
            @RequestParam Long customerId,
            @RequestParam Long vehicleId) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElse(null);

        if (customer == null || vehicle == null) {
            return ResponseEntity.notFound().build();
        }

        customer.addVehicle(vehicle);
        Vehicle updated = vehicleRepository.save(vehicle);
        return ResponseEntity.ok(toVehicleDto(updated));
    }

    @PutMapping("/vehicles/{id}/brand")
    public ResponseEntity<String> updateVehicleBrand(
            @PathVariable Long id,
            @RequestParam String brand) {
        int updatedRows = vehicleRepository.updateBrandById(id, brand);
        if (updatedRows == 0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Uppdaterade bilmärket till " + brand);
    }

    @GetMapping("/vehicle-brand-info")
    public List<VehicleBrandDto> getVehicleBrandInfo() {
        return vehicleRepository.findAllVehicleBrands().stream()
                .map(proj -> new VehicleBrandDto(proj.getRegistrationNumber(), proj.getBrand()))
                .toList();
    }

    private VehicleDto toVehicleDto(Vehicle vehicle) {
        Long customerId = vehicle.getCustomer() != null ? vehicle.getCustomer().getId() : null;
        return new VehicleDto(
                vehicle.getId(),
                vehicle.getRegistrationNumber(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getProductionYear(),
                customerId
        );
    }

    private CustomerWithVehiclesDto toCustomerDto(Customer customer) {
        List<VehicleDto> vehicleDtos = customer.getVehicles().stream()
                .map(this::toVehicleDto)
                .toList();
        return new CustomerWithVehiclesDto(
                customer.getId(),
                customer.getName(),
                customer.getPhoneNumber(),
                vehicleDtos
        );
    }
}