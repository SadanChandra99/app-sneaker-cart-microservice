package com.app.sneakers.cart.mapper;

import com.app.sneakers.cart.dto.CartDto;
import com.app.sneakers.cart.entities.CartEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {

    private static ModelMapper modelMapper;

    public CartMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    public static CartEntity mapCartDtoToEntity(CartDto cartDto){
        return modelMapper.map(cartDto, CartEntity.class);
    }

    public static CartDto mapCartEntityToDto(CartEntity cartEntity){
        return modelMapper.map(cartEntity, CartDto.class);
    }
}
