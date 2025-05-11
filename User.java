package com.example;

/**
 * User entity with an ID, name, phone number, and address.
 */
class User {
    private String id;
    private String name;
    private String phoneNumber;
    private String address;

    // Constructor
    /**
     * @param id .
     * @param name.
     * @param phoneNumber.
     * @param address.
     */
    public User(String id, String name, String phoneNumber, String address) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    // Getters
    public String getId() {return id;}
    public String getName() {return name;}
    public String getPhoneNumber() {return phoneNumber;}
    public String getAddress() {return address;}
}