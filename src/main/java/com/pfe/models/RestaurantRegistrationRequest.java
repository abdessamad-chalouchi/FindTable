package com.pfe.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class RestaurantRegistrationRequest {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final String phone;
    private final String city;
    private final String country;
    private final String latitude;
    private final String longitude;
    private final Integer numberTables;
    private final String restaurantName;
    private final String address;
    private final String description;
    private final Long category;
}
