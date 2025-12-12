package dev.cong.v.springcomereme.dto;

import dev.cong.v.springcomereme.entity.ShoppingCartItem;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemDTO {
    private  Long id;
    private Long quantity;
    private ProductItemDTO productItem;
    private String productName;
    private Double productPrice;
    private String avatar;
    private String color;

    public static  CartItemDTO toDTO(ShoppingCartItem shoppingCartItem){
        return  CartItemDTO.builder()
                .id(shoppingCartItem.getId())
                .color(shoppingCartItem.getProductItem().getProduct().getProductColor().getValue())
                .avatar(shoppingCartItem.getProductItem().getProduct().getAvatar())
                .quantity(shoppingCartItem.getQuantity())
                .productName(shoppingCartItem.getProductItem().getProduct().getName())
                .productPrice(shoppingCartItem.getProductItem().getProduct().getPrice())
                .productItem(ProductItemDTO.toDTO(shoppingCartItem.getProductItem()))
                .build();
    }
}
