package com.pfe.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "restaurant")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = true)
    private String email;

    @Column(name = "phone", nullable = true)
    private String phone;
    @Column(name = "number_tables", nullable = false)
    private Integer numberTables;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "city", nullable = false)
    private String city;
    @Column(name = "country", nullable = true)
    private String country;
    @Column(name = "latitude", nullable = true)
    private String latitude;
    @Column(name = "longitude", nullable = true)
    private String longitude;

    @Column(name = "image", nullable = true)
    @Lob
    private byte[] image;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;


    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "categories", nullable = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer"}) // to ignor Serializabl
    private Category categories ;

    @Column(name = "start_working_time")
    private LocalTime startWorkingTime;

    @Column(name = "end_working_time")
    private LocalTime endWorkingTime;

    @Column(name = "open", nullable = false)
    private Boolean open = false;

    public Restaurant(String name,
                      String email,
                      String phone,
                      Integer numberTables,
                      String address, String city,String country,
                      String latitude,String longitude,
                      String description,Category category,
                      String startWorkingTime,
                      String endWorkingTime) {
        this.name = name;
        this.email=email;
        this.phone=phone;
        this.numberTables = numberTables;
        this.address = address;
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.categories = category;
        this.startWorkingTime = LocalTime.parse(startWorkingTime);
        this.endWorkingTime = LocalTime.parse(endWorkingTime);
    }
}