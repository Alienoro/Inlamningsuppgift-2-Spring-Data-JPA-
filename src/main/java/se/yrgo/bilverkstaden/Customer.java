package se.yrgo.bilverkstaden;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Vehicle> vehicles = new ArrayList<>();

    public Customer() {
    }

    public Customer(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        vehicle.setCustomer(this);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
}