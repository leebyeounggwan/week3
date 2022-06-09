package com.spring.week3.dto.food;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FoodResponseDto {
    private String name;
    private int quantity;
    private int price;
}
