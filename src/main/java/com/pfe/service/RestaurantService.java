package com.pfe.service;

import com.pfe.entity.Dish;
import com.pfe.entity.Restaurant;
import com.pfe.models.SearchRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RestaurantService {
    List<Restaurant> getAllRestaurant();
    Restaurant saveRestaurant(Restaurant restaurant);

    Optional<Restaurant> findById(Long id);

    Restaurant getRestaurant(Long id);


    Map<String, List<Dish>> getDishes(Long id);

    Page<Restaurant> getListSearchedRestaurant(SearchRequest searchRequest);
}
