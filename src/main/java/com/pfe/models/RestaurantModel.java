package com.pfe.models;


import com.pfe.entity.Category;
import com.pfe.entity.Gallery;
import com.pfe.entity.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Lob;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
public class RestaurantModel {
    private Long id;
    @Lob
    private byte[] image;
    private String name;
    private String city;
    private String description;
    private final Float rating;
    private final Category category;
    private Restaurant restaurant;
    private final Float nbRating;
    private final List<Gallery> galleries;
    public RestaurantModel(Restaurant restaurant, Float rating) {
        this.restaurant = restaurant;
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.city = restaurant.getCity();
        this.description = restaurant.getDescription();
        this.image = restaurant.getImage();
        this.rating = rating;
        this.category = restaurant.getCategories();
        nbRating = null;
        galleries = null;
    }
    public RestaurantModel(Restaurant restaurant, Float rating, Float nbRating, List<Gallery> galleries) {
        this.restaurant = restaurant;
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.city = restaurant.getCity();
        this.description = restaurant.getDescription();
        this.image = restaurant.getImage();
        this.rating = rating;
        this.category = restaurant.getCategories();
        this.nbRating = nbRating;
        this.galleries = galleries;
    }
}
