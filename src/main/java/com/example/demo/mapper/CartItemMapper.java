package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.cartitem.CartItemResponseDto;
import com.example.demo.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemResponseDto toCartItemResponseDto(CartItem cartItem);
}
