package com.spring.week3.service;


import com.spring.week3.domain.Restaurant;
import com.spring.week3.dto.restaurant.RestaurantRequestDto;
import com.spring.week3.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.spring.week3.exception.ExceptionMessages.*;



@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Transactional
    public Restaurant restaurantRegistration(RestaurantRequestDto requestDto){
        int minOrderPrice = requestDto.getMinOrderPrice();
        int deliveryFee = requestDto.getDeliveryFee();

        checkMinOrderPrice(minOrderPrice);

        checkDeliveryFee(deliveryFee);
        Restaurant restaurant = Restaurant.builder()
                .name(requestDto.getName())
                .minOrderPrice(minOrderPrice)
                .deliveryFee(deliveryFee)
                .build();

        System.out.println("restaurantName = " + restaurant.getName());
        System.out.println("restaurantminPrice = " + restaurant.getMinOrderPrice());
        System.out.println("restaurantDeliveryFee = " + restaurant.getDeliveryFee());

        restaurantRepository.save(restaurant);

        return restaurant;
    }

    private void checkMinOrderPrice(int minOrderPrice) {
        if(!(1000 <= minOrderPrice && minOrderPrice <= 100000)) {
            throw new IllegalArgumentException(ILLEGAL_MIN_ORDER_PRICE_RANGE);
        }

        if(minOrderPrice % 100 > 0) {
            throw new IllegalArgumentException(ILLEGAL_MIN_ORDER_PRICE_UNIT);
        }
    }

    private void checkDeliveryFee(int deliveryFee) {
        if(0 > deliveryFee || deliveryFee > 10_000) {
            throw new IllegalArgumentException(ILLEGAL_DELIVERY_FEE_RANGE);
        }

        if(deliveryFee % 500 > 0) {
            throw new IllegalArgumentException(ILLEGAL_DELIVERY_FEE_UNIT);
        }
    }


}
