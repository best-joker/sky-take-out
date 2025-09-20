package com.sky.service;

import java.util.List;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

public interface ShoppingCartService {

    void add(ShoppingCartDTO shoppingCartdDto);

    List<ShoppingCart> showShoppingCart();

}
