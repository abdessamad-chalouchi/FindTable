package com.pfe.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "review_restaurant")
@JsonSerialize
public class ReviewRestaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "number_star", nullable = false)
    private double numberStar;

    @Lob
    @Column(name = "comment", nullable = true)
    private String comment;

    @Column(name = "date", nullable = false)
    private Instant date = Instant.now();



    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_restauraut", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer"}) // to ignor Serializabl
    private Restaurant idRestaurant;




    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_customer", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer"}) // to ignor Serializabl
    private Customer idCustomer;

    public ReviewRestaurant(double numberStar, Restaurant idRestaurant, Customer idCustomer) {
        this.numberStar = numberStar;
        this.idRestaurant = idRestaurant;
        this.idCustomer = idCustomer;
    }

    public ReviewRestaurant(double numberStar, String comment, Restaurant idRestaurant, Customer idCustomer) {
        this.numberStar = numberStar;
        this.comment = comment;
        this.idRestaurant = idRestaurant;
        this.idCustomer = idCustomer;
    }
}