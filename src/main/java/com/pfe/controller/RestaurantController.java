package com.pfe.controller;

import com.pfe.entity.Dish;
import com.pfe.entity.Restaurant;
import com.pfe.entity.ReviewRestaurant;
import com.pfe.models.PageRequest;
import com.pfe.models.RestaurantModel;
import com.pfe.models.SearchRequest;
import com.pfe.service.CustomerService;
import com.pfe.service.ManagerService;
import com.pfe.service.RestaurantService;
import com.pfe.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final ReviewService reviewService;
    private final ManagerService managerService;
    private final CustomerService customerService;

    @GetMapping("/public/restaurant")
    public ResponseEntity<List<RestaurantModel>> getAllRestaurant(){
        return ResponseEntity.ok(reviewService.getListRestaurantAverageRating());
    }
    @GetMapping("/public/CheckReservation")
    public ResponseEntity<List<Object[]>> checkReservation(@RequestParam Map<String,String> requestParams) {
        Optional<Restaurant> restaurant = restaurantService.findById(Long.parseLong(requestParams.get("id")));
        return ResponseEntity.ok(customerService.checkReservations(restaurant.get(), LocalDate.parse(requestParams.get("date"))));
    }
    @GetMapping("/public/restaurant/{id}")
    public ResponseEntity<RestaurantModel> getRestaurant(@PathVariable("id") Long id){
        return ResponseEntity.ok(reviewService.getRestaurantAverageRating(id));
    }
    @GetMapping("/public/restaurant/comment/{id}")
    public ResponseEntity<Map<Integer, Integer>> getRestaurantRating(@PathVariable("id") Long id){
        return ResponseEntity.ok(reviewService.getRestaurantRatingDetail(id));
    }
    @GetMapping("/public/restaurant/comment/all/{id}")
    public ResponseEntity<Iterable<ReviewRestaurant>> getRestaurantReviews(PageRequest pageRequest, @PathVariable String id){
        Optional<Restaurant> restaurant = restaurantService.findById(Long.parseLong(id));
        return ResponseEntity.ok(reviewService.getReviews(pageRequest,restaurant.get()));
    }
    @GetMapping("/restaurant/comment/all")
    public ResponseEntity<Iterable<ReviewRestaurant>> getAllRestaurantReviews(PageRequest pageRequest, @RequestHeader("Authorization") String authorization) throws ParseException {
        String token = authorization.split(" ")[1];
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chunks[1]));
        Object object =  new JSONParser(payload).parse();

        String email = String.valueOf(object).split(",")[0].split("=")[1];

        Restaurant restaurant = managerService.getManager(email).getRestaurant();
        return ResponseEntity.ok(reviewService.getReviews(pageRequest,restaurant));
    }

    @GetMapping("/public/restaurant/search")
    public ResponseEntity<Iterable<Restaurant>> searchRestaurant(SearchRequest searchRequest) {

        return ResponseEntity.ok(restaurantService.getListSearchedRestaurant(searchRequest));
    }
    @GetMapping("/public/restaurant/dish/{id}")
    public ResponseEntity<Map<String, List<Dish>>> getDishes(@PathVariable("id") Long id){
        return ResponseEntity.ok(restaurantService.getDishes(id));
    }

    @GetMapping("/public/test")
    public List<Float> test(@RequestParam Long id){
//        return reviewService.getRestaurantAverageRating(restaurantService.findById(id).get()).get(0);
        return null;
    }

}
