package com.pfe.service;

import com.pfe.entity.*;
import com.pfe.models.PageRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ManagerService {
    void saveManager(Manager manager);

    void addDish(Dish dish, Long idMenu, User user);

    void addMenu(Menu menu, User user);

    void addTable(Tables tables);

    Page<Dish> DishList(PageRequest pageRequest, User user);
//    List<Dish> DishList(User user);


    List<Dish> DishList(User user);

    void deleteDish(Long[] idDish);

    void cancelReservation(Long[] idReservation);

    List<Menu> menuList(User user);

    Menu getMenu(Long id);

    void deleteMenu(Long[] idMenu);

    Dish getDish(Long id);

    Manager getManager(String email);

    List<Gallery> getGallery(Restaurant restaurant);

    void deleteImage(Long id);

    Page<Reservation> getRestaurantReservations(PageRequest pageRequest, String email);
}
