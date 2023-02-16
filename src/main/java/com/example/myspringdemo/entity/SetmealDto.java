package com.example.myspringdemo.entity;

import lombok.Data;

import java.util.List;

public class SetmealDto extends SetMeal {

    private List<SetMealDish> setmealDishes;

    private String categoryName;

    public List<SetMealDish> getSetmealDishes() {
        return setmealDishes;
    }

    public void setSetmealDishes(List<SetMealDish> setmealDishes) {
        this.setmealDishes = setmealDishes;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
