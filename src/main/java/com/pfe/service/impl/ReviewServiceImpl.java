package com.pfe.service.impl;

import com.pfe.entity.Gallery;
import com.pfe.entity.Restaurant;
import com.pfe.entity.ReviewRestaurant;
import com.pfe.models.PageRequest;
import com.pfe.models.RestaurantModel;
import com.pfe.repository.GalleryRepository;
import com.pfe.repository.RestaurantRepository;
import com.pfe.repository.ReviewRepository;
import com.pfe.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository restaurantReviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final GalleryRepository galleryRepository;
//    @Override
//    public List<List<Float>> getRestaurantAverageRating(Restaurant restaurant) {
//        return restaurantReviewRepository.avgRestaurantRating(restaurant);
//    }
    @Override
    public RestaurantModel getRestaurantAverageRating(Long id) {
        RestaurantModel restaurantModel ;
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        restaurantReviewRepository.avgRestaurantRating(restaurant.get());
        List<Float> rating = restaurantReviewRepository.avgRestaurantRating(restaurant.get()).get(0);
        List<Gallery> galleries = galleryRepository.findByRestaurant(restaurant.get());
        restaurantModel = new RestaurantModel(restaurant.get(),rating.get(1),rating.get(0),galleries);
        return restaurantModel;
    }
    @Override
    public List<RestaurantModel> getListRestaurantAverageRating() {
        int i=0;
        List<RestaurantModel> restaurantModels = new ArrayList<>();

        for(Object[] obj: restaurantReviewRepository.listAvgRestaurantRating()){
            if(i > 9) {
                break;
            }
            RestaurantModel model = new RestaurantModel((Restaurant)obj[0],Float.parseFloat(obj[1].toString()));
            restaurantModels.add(model);
            i++;
        }
        return restaurantModels;
    }

    @Override
    public Map<Integer, Integer> getRestaurantRatingDetail(Long id) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        Map<Integer, Integer> integerMap = new HashMap<>();
        List<ArrayList<Integer>> ratingDetail = restaurantReviewRepository.restaurantRatingDetail(restaurant.get());
        for(int i=0; i<ratingDetail.size(); i++){
            integerMap.put(ratingDetail.get(i).get(0),ratingDetail.get(i).get(1));
        }

        System.out.println(integerMap);
        return integerMap;
    }

    @Override
    public List<ReviewRestaurant> getRestaurantReviews(Long id) {
//        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
//        return restaurantReviewRepository.findByIdRestaurant(restaurant.get());
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        return restaurantReviewRepository.findByIdRestaurant(restaurant.get());
    }

    @Override
    public Page<ReviewRestaurant> getReviews(PageRequest pageRequest){

        Sort sort = Sort.by(pageRequest.getSortDirection(), pageRequest.getSortBy());
        Pageable pageable = org.springframework.data.domain.PageRequest.of(pageRequest.getPageNumber(),
                pageRequest.getPageSize(),sort);
        return restaurantReviewRepository.findAll(pageable);
    }

    @Override
    public Page<ReviewRestaurant> getReviews(PageRequest pageRequest, Restaurant restaurant){
        ReviewRestaurant reviewRestaurant = new ReviewRestaurant();
        reviewRestaurant.setIdRestaurant(restaurant);
        Sort sort = Sort.by(pageRequest.getSortDirection(), pageRequest.getSortBy());
        Pageable pageable = org.springframework.data.domain.PageRequest.of(pageRequest.getPageNumber(),
                pageRequest.getPageSize(),sort);
        // get not null element
        if(Integer.parseInt(pageRequest.getComment()) == 1){
            return restaurantReviewRepository.findAllByIdRestaurantAndCommentIsNotNull(restaurant,pageable);
        }
        // get null element
        return restaurantReviewRepository.findAllByIdRestaurant(restaurant,pageable);
    }
    @Override
    public Iterable<ReviewRestaurant> getAllReviews(PageRequest pageRequest, Restaurant restaurant){
        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withMatcher("titre",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        ReviewRestaurant reviewRestaurant = new ReviewRestaurant();
        reviewRestaurant.setIdRestaurant(restaurant);
        Sort sort = Sort.by(pageRequest.getSortDirection(), pageRequest.getSortBy());
        Pageable pageable = org.springframework.data.domain.PageRequest.of(pageRequest.getPageNumber(),
                pageRequest.getPageSize(),sort);
        return restaurantReviewRepository.findAll(Example.of(reviewRestaurant),pageable);
    }
}
