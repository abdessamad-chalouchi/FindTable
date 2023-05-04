package com.pfe.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "manager")
public class Manager {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer"}) // to ignor Serializabl
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer"}) // to ignor Serializabl
    private Restaurant restaurant;

    public Manager(User user) {
        this.user = user;
    }

    public Manager(User user, Restaurant restaurant) {
        this.user = user;
        this.restaurant = restaurant;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User idUser) {
        this.user = idUser;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

}