package com.pfe.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalTime;

@JsonIgnoreProperties({"hibernateLazyInitializer"}) // to ignor Serializabl
public class CustomReservation {
    LocalTime time;
    Long total;

    public CustomReservation(LocalTime time, Long total) {
        this.time = time;
        this.total = total;
    }
}
