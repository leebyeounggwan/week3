package com.spring.week3.service;

import com.spring.week3.domain.Food;
import com.spring.week3.domain.Restaurant;
import com.spring.week3.dto.food.FoodRequestDto;
import com.spring.week3.repository.FoodRepository;
import com.spring.week3.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.spring.week3.exception.ExceptionMessages.*;

@Service
@RequiredArgsConstructor
public class FoodService {
    private final FoodRepository foodRepository;

    private final RestaurantRepository restaurantRepository;

    @Transactional
    public Long foodRegistration(Long restaurantId, List<FoodRequestDto> requestDtoList){
        Optional<Restaurant> resultId = restaurantRepository.findById(restaurantId);

        checkRestaurant(resultId);

        Restaurant restaurant = resultId.get();

        for(int i=0; i<requestDtoList.size(); i++) {
            String foodName = requestDtoList.get(i).getName();
            int foodPrice = requestDtoList.get(i).getPrice();

            checkDuplicateRestaurantFood(restaurant, foodName);
            checkFoodPrice(foodPrice);

            Food food = new Food();
            food.setName(foodName);
            food.setPrice(foodPrice);
            food.setRestaurant(restaurant);

            foodRepository.save(food);
        }

        return restaurant.getId();
    }

    private void checkRestaurant(Optional<Restaurant> restaurantId) {
        if (!restaurantId.isPresent())
            throw new IllegalArgumentException(RESTAURANT_IS_NULL);
    }

    private void checkDuplicateRestaurantFood(Restaurant restaurant, String foodName) {
        Optional<Food> found = foodRepository.findFoodByRestaurantAndName(restaurant, foodName);
        if(found.isPresent())
            throw new IllegalArgumentException(RESTAURANT_FOOD_DUPLICATE);
    }

    private void checkFoodPrice(int foodPrice) {
        if (foodPrice < 100)
            throw new IllegalArgumentException(FOOD_PRICES_TOO_LOW);

        if (foodPrice > 1_000_000)
            throw new IllegalArgumentException(FOOD_PRICES_TOO_HIGH);

        if (foodPrice % 100 > 0)
            throw new IllegalArgumentException(ILLEGAL_FOOD_PRICES_UNIT);
    }


    @javax.transaction.Transactional
    public List<Food> findAllRestaurantFoods(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(
                        () -> new NullPointerException(RESTAURANT_IS_NULL));

        return foodRepository.findFoodsByRestaurant(restaurant);
    }
}
