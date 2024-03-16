/*
Designing a Car Rental System
In this article, we will explore the object-oriented design and implementation of a Car Rental System (CRS) using Java.

This system will handle the renting of cars to customers, managing car inventory, and tracking rentals.

System Requirements
The CRS should support:

Car Inventory Management: Keep track of available cars for rent.
Rental Process Management: Handle the process of renting a car to a customer.
Rental Tracking: Track ongoing and past rentals.
Customer Management: Manage customer information.
Core Use Cases
Renting a Car: Customers can rent available cars.
Returning a Car: Handle the return process of rented cars.
Tracking Rentals: View current and past rental records.
Managing Car Inventory: Add, update, and remove cars from the inventory.
Key Classes:
CarRentalSystem: Manages the overall operations of the car rental system.
Car: Represents a car in the system.
Rental: Manages details about a car rental.
Customer: Stores information about customers.

 */

import java.time.LocalDate;

public class Car {
    private String licensePlate;
    private String make;
    private boolean isAvailable;
    private int rate;
    

    public Car(String licensePlate, String make, int rate) {
        this.licensePlate = licensePlate;
        this.make = make;
        this.isAvailable = true;
        this.rate = rate;
    }

    public void rentOut() {
        isAvailable = false;
    }

    public void returnCar() {
        isAvailable = true;

    }

    public double calculateRent(LocalDate returnDate, LocalDate rentalDate)
    {
        return (returnDate - rentalDate) * this.rate;
    }

    // Getters and setters...
}

public class Customer {
    private String customerId;
    private String name;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    // Getters and setters...
}

//import java.time.LocalDate;

public class Rental {
    private Car car;
    private Customer customer;
    private LocalDate rentalDate;
    private LocalDate returnDate;


    public Rental(Car car, Customer customer, LocalDate rentalDate) {
        this.car = car;
        this.customer = customer;
        this.rentalDate = rentalDate;
        this.car.rentOut();
    }

    public double completeRental(LocalDate returnDate) {
        this.returnDate = returnDate;
        this.car.returnCar();
        return this.car.calculateRent(returnDate, returnDate);
    }

    // Getters and setters...
}



public class CarRentalSystem {
    private List<Car> cars;
    private List<Rental> rentals;

    public CarRentalSystem() {
        cars = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public Rental rentCar(String licensePlate, Customer customer, LocalDate rentalDate) {
        Car car = findAvailableCar(licensePlate);
        if (car != null) {
            Rental rental = new Rental(car, customer, rentalDate);
            rentals.add(rental);
            return rental;
        }
        return null;
    }

    private Car findAvailableCar(String licensePlate) {
        return cars.stream()
                   .filter(c -> c.getLicensePlate().equals(licensePlate) && c.isAvailable())
                   .findFirst().orElse(null);
    }

    public closeRental(Rental rentalDetail, LocalDate returnDate)
    {
        rentalDetail.completeRental(returnDate);
    }


    // Other necessary methods...
}