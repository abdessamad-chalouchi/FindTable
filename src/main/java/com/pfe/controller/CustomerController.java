package com.pfe.controller;

import com.pfe.entity.*;
import com.pfe.models.PageRequest;
import com.pfe.service.CustomerService;
import com.pfe.service.RestaurantService;
import com.pfe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    private final UserService userService;
    private final CustomerService customerService;
    private final RestaurantService restaurantService;

    @PostMapping("/comment/add")
    public ResponseEntity<?> addComment(@RequestParam Map<String,String> requestParams,
                                        @RequestHeader("Authorization") String authorization) throws ParseException {
        String token = authorization.split(" ")[1];
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));
        Object object =  new JSONParser(payload).parse();

        String email = String.valueOf(object).split(",")[0].split("=")[1];
        User user = userService.getUser(email);

        Customer customer = customerService.getCustomerByUser(user);
        Optional<Restaurant> restaurant = restaurantService.findById(Long.parseLong(requestParams.get("id")));
        ReviewRestaurant reviewRestaurant;
        if(requestParams.get("comment").equals("")){
            reviewRestaurant = new ReviewRestaurant(Double.parseDouble(requestParams.get("rating")),
                    restaurant.get(),customer);
        }
        else {
            reviewRestaurant = new ReviewRestaurant(Double.parseDouble(requestParams.get("rating")),
                    requestParams.get("comment"),restaurant.get(),customer);
        }

        customerService.saveComment(reviewRestaurant);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/reservation/add")
    public ResponseEntity<?> addReservation(@RequestParam Map<String,String> requestParams,
                                        @RequestHeader("Authorization") String authorization) throws ParseException, java.text.ParseException {
        String token = authorization.split(" ")[1];
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));
        Object object =  new JSONParser(payload).parse();

        String email = String.valueOf(object).split(",")[0].split("=")[1];
        User user = userService.getUser(email);
        Customer customer = customerService.getCustomerByUser(user);
        Optional<Restaurant> restaurant = restaurantService.findById(Long.parseLong(requestParams.get("id")));
//        System.out.println(requestParams);
        Reservation reservation = new Reservation(LocalDate.parse(requestParams.get("date")),
                Integer.parseInt(requestParams.get("seats")),
                LocalTime.parse(requestParams.get("time")),
                customer,
                requestParams.get("note"));
        System.out.println(reservation.toString());
        customerService.addReservation(customer,restaurant.get(),requestParams);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/reservation")
    public ResponseEntity<Iterable<Reservation>> getReservation(@RequestHeader("Authorization") String authorization,
                                                           PageRequest pageRequest) throws ParseException {
        String token = authorization.split(" ")[1];
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));
        Object object =  new JSONParser(payload).parse();

        String email = String.valueOf(object).split(",")[0].split("=")[1];
        Customer customer = customerService.getCustomerByUserEmail(email);

        return ResponseEntity.ok(customerService.getCustomerReservations(pageRequest,email));
    }

    @GetMapping("/favorite")
    public ResponseEntity<Iterable<Favorite>> getFavorites(@RequestHeader("Authorization") String authorization,
                                                 PageRequest pageRequest) throws ParseException {
        String token = authorization.split(" ")[1];
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));
        Object object =  new JSONParser(payload).parse();

        String email = String.valueOf(object).split(",")[0].split("=")[1];
        Customer customer = customerService.getCustomerByUserEmail(email);

        return ResponseEntity.ok(customerService.getCustomerFavorites(pageRequest,email));
    }
    @DeleteMapping("/reservation/remove/{id}")
    public ResponseEntity<?> removeReservation(@RequestHeader("Authorization") String authorization,
                                            @PathVariable("id") Long id) throws ParseException {
        String token = authorization.split(" ")[1];
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));
        Object object =  new JSONParser(payload).parse();

        String email = String.valueOf(object).split(",")[0].split("=")[1];
        User user = userService.getUser(email);

        Customer customer = customerService.getCustomerByUser(user);

        customerService.deleteReservation(customer, id);

        return ResponseEntity.ok().build();
    }
    @PostMapping("/favorite/add/{id}")
    public ResponseEntity<?> addFavorite(@RequestHeader("Authorization") String authorization,
                                        @PathVariable("id") Long id) throws ParseException {
        String token = authorization.split(" ")[1];
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));
        Object object =  new JSONParser(payload).parse();

        String email = String.valueOf(object).split(",")[0].split("=")[1];
        User user = userService.getUser(email);

        Customer customer = customerService.getCustomerByUser(user);
        Optional<Restaurant> restaurant = restaurantService.findById(id);

        Favorite favorite =  new Favorite(customer,restaurant.get());

        customerService.saveFavorite(favorite);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/favorite/{id}")
    public ResponseEntity<Favorite> getFavorite(@RequestHeader("Authorization") String authorization,
                                         @PathVariable("id") Long id) throws ParseException {
        String token = authorization.split(" ")[1];
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));
        Object object =  new JSONParser(payload).parse();

        String email = String.valueOf(object).split(",")[0].split("=")[1];
        User user = userService.getUser(email);

        Customer customer = customerService.getCustomerByUser(user);


        return ResponseEntity.ok(customerService.isInFavorite(customer,id));
    }
    @DeleteMapping("/favorite/remove/{id}")
    public ResponseEntity<?> removeFavorite(@RequestHeader("Authorization") String authorization,
                                         @PathVariable("id") Long id) throws ParseException {
        String token = authorization.split(" ")[1];
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));
        Object object =  new JSONParser(payload).parse();

        String email = String.valueOf(object).split(",")[0].split("=")[1];
        User user = userService.getUser(email);

        Customer customer = customerService.getCustomerByUser(user);

        customerService.deleteFavorite(customer, id);

        return ResponseEntity.ok().build();
    }
}
