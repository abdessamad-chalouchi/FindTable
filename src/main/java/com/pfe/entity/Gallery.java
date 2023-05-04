package com.pfe.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "gallery")
public class Gallery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;


    @Column(name = "image", nullable = false)
    private byte[] image;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer"}) // to ignor Serializabl
    private Restaurant restaurant;

    public Gallery(byte[] image, Restaurant restaurant) {
        this.image = image;
        this.restaurant = restaurant;
    }
}