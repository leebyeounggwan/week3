package com.spring.week3.service;

import com.spring.week3.domain.Food;
import com.spring.week3.domain.Order;
import com.spring.week3.domain.OrderDetails;
import com.spring.week3.domain.Restaurant;
import com.spring.week3.dto.food.FoodResponseDto;
import com.spring.week3.dto.order.OrderFoodResponseDto;
import com.spring.week3.dto.order.OrderRequestDto;
import com.spring.week3.dto.order.OrderResponseDto;
import com.spring.week3.repository.FoodRepository;
import com.spring.week3.repository.OrderDetailsRepository;
import com.spring.week3.repository.OrderRepository;
import com.spring.week3.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.spring.week3.exception.ExceptionMessages.*;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final RestaurantRepository restaurantRepository;
    private final FoodRepository foodRepository;
    private final OrderRepository orderRepository;
//    private final OrderRequestDto requestDto;
    private final OrderDetailsRepository orderDetailsRepository;


    // 주문데이터 받아서 order, orderlist 저장
    @Transactional
    public OrderResponseDto saveOrder(OrderRequestDto requestDto) {
        Restaurant restaurant = restaurantRepository.findById(requestDto.getRestaurantId()).orElseThrow(
                () -> new NullPointerException(RESTAURANT_IS_NULL));

        OrderResponseDto orderResponseDto = new OrderResponseDto();
        List<OrderFoodResponseDto> orderFoodResponseDtoList = new ArrayList<>();

        Order order = new Order();
        int totalPrice = 0;
        List<OrderDetails> setOrderDetails = new ArrayList<>();

        for (int i = 0; i < requestDto.getFoods().size(); i++) {
            OrderFoodResponseDto orderFoodResponseDto = new OrderFoodResponseDto();
            int count = requestDto.getFoods().get(i).getQuantity();
//            System.out.println("count = " + count);

            if (count < 1 || count > 100) {
                throw new IllegalArgumentException(ILLEGAL_FOOD_ORDER_QUANTITY);
            }

            Food foodIs = foodRepository.findById(requestDto.getFoods().get(i).getId()).orElseThrow(
                    () -> new NullPointerException(CANT_FIND_FOOD));
            System.out.println("foodIs = " + foodIs.getName());

            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setFood(foodIs);
            orderDetails.setPrice(foodIs.getPrice() * count);
            orderDetails.setQuantity(requestDto.getFoods().get(i).getQuantity());
            orderDetails.setOrder(order);
            orderDetailsRepository.save(orderDetails);

            setOrderDetails.add(orderDetails);

            orderFoodResponseDto.setName(foodIs.getName());
            orderFoodResponseDto.setQuantity(count);
            orderFoodResponseDto.setPrice(foodIs.getPrice()*count);

            orderFoodResponseDtoList.add(orderFoodResponseDto);

            totalPrice += foodIs.getPrice() * count;
            }

        if(totalPrice < restaurant.getMinOrderPrice()) {
            throw new IllegalStateException(ILLEGAL_TOTAL_PRICE);
        }
        int deliveryFee = restaurant.getDeliveryFee();

        totalPrice += deliveryFee;


        order.setRestaurant(restaurant);
        order.setTotalPrice(totalPrice);
        order.setOrderDetails(setOrderDetails);

        orderRepository.save(order);

        orderResponseDto.setRestaurantName(restaurant.getName());
        orderResponseDto.setFoods(orderFoodResponseDtoList);
        System.out.println("orderResponseDto = " + orderResponseDto.getFoods().get(0).getName());
        System.out.println("orderResponseDto = " + orderResponseDto.getFoods().get(1).getName());
        System.out.println("orderResponseDto = " + orderResponseDto.getFoods().get(2).getName());
        orderResponseDto.setDeliveryFee(deliveryFee);
        orderResponseDto.setTotalPrice(totalPrice);

        return orderResponseDto;
    }

//    //전체 주문내역
    @Transactional
    public List<OrderResponseDto> findAllOrder() {
        List<OrderResponseDto> orderListDto = new ArrayList<>();

        List<OrderFoodResponseDto> foodResponseDtoList = new ArrayList<>();

        List<Order> allOrders = orderRepository.findAll();

        for (int i=0; i<allOrders.size(); i++) {
            OrderResponseDto orderResponseDto = new OrderResponseDto();

            List<OrderDetails> allOrderDetails = orderDetailsRepository.findOrderDetailsByOrder(allOrders.get(i));
            System.out.println(allOrderDetails);
            int deliveryFee = restaurantRepository
                    .findById(allOrders.get(i).getRestaurant().getId()).get().getDeliveryFee();

            for (int j = 0; j < allOrderDetails.size(); j++) {
                OrderFoodResponseDto foodResponseDto = new OrderFoodResponseDto();
                foodResponseDto.setName(allOrderDetails.get(j).getFood().getName());
                foodResponseDto.setQuantity(allOrderDetails.get(j).getQuantity());
                foodResponseDto.setPrice(allOrderDetails.get(j).getPrice());

                foodResponseDtoList.add(foodResponseDto);
            }
            System.out.println(foodResponseDtoList);
            orderResponseDto.setRestaurantName(allOrders.get(i).getRestaurant().getName());
            orderResponseDto.setDeliveryFee(deliveryFee);
            orderResponseDto.setTotalPrice(allOrders.get(i).getTotalPrice());
            orderResponseDto.setFoods(foodResponseDtoList);

            orderListDto.add(orderResponseDto);
        }

        return orderListDto;
    }

}


