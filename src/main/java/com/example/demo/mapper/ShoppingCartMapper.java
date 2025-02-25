package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.demo.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "user.id", target = "userId")
    ShoppingCartResponseDto toShoppingCartResponseDto(ShoppingCart shoppingCart);
}
