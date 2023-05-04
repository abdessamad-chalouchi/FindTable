package com.pfe.service;

import com.pfe.entity.Restaurant;
import com.pfe.entity.ReviewRestaurant;
import com.pfe.models.PageRequest;
import com.pfe.models.RestaurantModel;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ReviewService {
    //    @Override
    //    public List<List<Float>> getRestaurantAverageRating(Restaurant restaurant) {
    //        return restaurantReviewRepository.avgRestaurantRating(restaurant);
    //    }

    //    @Override
    //    public List<List<Float>> getRestaurantAverageRating(Restaurant restaurant) {
    //        return restaurantReviewRepository.avgRestaurantRating(restaurant);
    //    }
    RestaurantModel getRestaurantAverageRating(Long id);

    //    List<List<Float>> getRestaurantAverageRating(Restaurant restaurant);
    List<RestaurantModel> getListRestaurantAverageRating();

    Map<Integer, Integer> getRestaurantRatingDetail(Long id);

    List<ReviewRestaurant> getRestaurantReviews(Long id);

    Page<ReviewRestaurant> getReviews(PageRequest pageRequest);

    Page<ReviewRestaurant> getReviews(PageRequest pageRequest, Restaurant restaurant);

    Iterable<ReviewRestaurant> getAllReviews(PageRequest pageRequest, Restaurant restaurant);
}
