package com.pfe.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class RegistrationRequest {
    private  String firstName;
    private  String lastName;
    private  String email;
    private  String password;
    private  String phone;
    private String city;

    public RegistrationRequest(String firstName, String lastName, String email, String password, String phone, String city) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.city = city;
    }
}
