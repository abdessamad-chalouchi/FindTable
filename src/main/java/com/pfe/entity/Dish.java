package com.pfe.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "dish")
@AllArgsConstructor
@NoArgsConstructor
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Float price;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "image", nullable = true)
    private byte[] image;

    @JsonFormat(timezone = "Africa/Casablanca")
    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @JsonFormat(timezone = "Africa/Casablanca")
    @Column(name = "expiration_date", nullable = false)
    private Date expirationDate;

    @Column(name = "percentage", nullable = false)
    private Float percentage;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "menu", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer"}) // to ignor Serializabl
    private Menu menu;




    public Dish(Long id,
                String name,
                Float price,
                String description,
                Date startDate,
                Date expirationDate,
                Float percentage) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.startDate = startDate;
        this.expirationDate = expirationDate;
        this.percentage = percentage;
    }
    public Dish(String name,
                Float price,
                String description,
                byte[] image,
                Date startDate,
                Date expirationDate,
                Float percentage) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
        this.startDate = startDate;
        this.expirationDate = expirationDate;
        this.percentage = percentage;
    }
    public Dish(String name,
                Float price,
                String description,
                Date startDate,
                Date expirationDate,
                Float percentage) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.startDate = startDate;
        this.expirationDate = expirationDate;
        this.percentage = percentage;
    }


}