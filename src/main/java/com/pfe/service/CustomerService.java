package com.pfe.service;

import com.pfe.entity.*;
import com.pfe.models.PageRequest;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface CustomerService {
    Customer saveCustomer(Customer customer);

    Customer getCustomer(User user);

    Customer getCustomerByUser(User user);

    void saveComment(ReviewRestaurant reviewRestaurant);

    void saveFavorite(Favorite favorite);

    Favorite isInFavorite(Customer customer, Long id);

    void deleteFavorite(Customer customer, Long id);

    Customer getCustomerByUserEmail(String email);


    Page<Favorite> getCustomerFavorites(PageRequest pageRequest, String email);

    Page<Reservation> getCustomerReservations(PageRequest pageRequest, String email);

    void addReservation(Customer customer, Restaurant restaurant, Map<String, String> requestParams) throws ParseException;

    void deleteReservation(Customer customer, Long id);

    List<Object[]> checkReservations(Restaurant restaurant, LocalDate date);
}
