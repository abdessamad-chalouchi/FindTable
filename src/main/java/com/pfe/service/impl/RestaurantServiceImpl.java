package com.pfe.service.impl;

import com.pfe.entity.Category;
import com.pfe.entity.Dish;
import com.pfe.entity.Menu;
import com.pfe.entity.Restaurant;
import com.pfe.models.PageRequest;
import com.pfe.models.SearchRequest;
import com.pfe.repository.CategoriesRepository;
import com.pfe.repository.DishRepository;
import com.pfe.repository.MenuRepository;
import com.pfe.repository.RestaurantRepository;
import com.pfe.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final DishRepository dishRepository;
    private final CategoriesRepository categoriesRepository;
    public List<Restaurant> getAllRestaurant() {
        return restaurantRepository.findAll();
    }
    public Restaurant saveRestaurant(Restaurant restaurant){
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Optional<Restaurant> findById(Long id) {
        return restaurantRepository.findById(id);
    }

    @Override
    public Restaurant getRestaurant(Long id) {
        return restaurantRepository.getById(id);
    }

    @Override
    public Map<String, List<Dish>> getDishes(Long id) {
        Map<String, List<Dish>> menuDishes = new HashMap<>();
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        List<Menu> menus = menuRepository.findByRestaurant(restaurant.get());

        for(Menu menu : menus){
            menuDishes.put(menu.getName(), dishRepository.findByMenu(menu));
        }
        return menuDishes;
    }
    @Override
    public Page<Restaurant> getListSearchedRestaurant(SearchRequest searchRequest) {
        Sort sort = Sort.by(searchRequest.getSortDirection(), searchRequest.getSortBy());
        Pageable pageable = org.springframework.data.domain.PageRequest.of(searchRequest.getPageNumber(),
                searchRequest.getPageSize(),sort);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("categories",exact())
                .withMatcher("city",exact())
                .withMatcher("country",exact());
        Restaurant restaurant = new Restaurant();
        Category category = null;
        if(searchRequest.getIdCategory() != null && searchRequest.getIdCategory() != 0){
            category = categoriesRepository.findById(searchRequest.getIdCategory()).get();
        }

        restaurant.setCategories(category);
        restaurant.setOpen(null);
        restaurant.setName(searchRequest.getName());
        restaurant.setCity(Objects.equals(searchRequest.getCity(), "") ? null:searchRequest.getCity());
        restaurant.setCountry(Objects.equals(searchRequest.getCountry(), "") ? null:searchRequest.getCountry());
        Page<Restaurant> pageRestaurant = restaurantRepository.findAll(Example.of(restaurant,matcher),pageable);
        return pageRestaurant;
    }

}
