package com.spring.week3.controller;

import com.spring.week3.domain.Restaurant;
import com.spring.week3.dto.restaurant.RestaurantRequestDto;
import com.spring.week3.repository.RestaurantRepository;
import com.spring.week3.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantRepository repository;


    @PostMapping("/restaurant/register")
    public Restaurant addRestaurant(@RequestBody RestaurantRequestDto requestDto) {

        return restaurantService.restaurantRegistration(requestDto);
    }

    @GetMapping("/restaurants")
    public List<Restaurant> findAllRestaurant() {
        return repository.findAll();
    }
}
