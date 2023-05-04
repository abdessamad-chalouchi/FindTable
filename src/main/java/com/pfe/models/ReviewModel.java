package com.pfe.models;

import com.pfe.entity.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewModel {
    private final Restaurant restaurant;
    private final Double rating;


}
