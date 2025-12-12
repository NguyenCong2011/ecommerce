package dev.cong.v.springcomereme.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.cong.v.springcomereme.entity.ProductItem;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductItemDTO {

    private  Long  id;
    private  Long  quantity;
    private ProductSizeDTO productSize;
    private String productName;
    private Double productPrice;
    private String avatar;
    private String color;
    public  static  ProductItemDTO toDTO(ProductItem productItem){
        return  ProductItemDTO.builder()
                .id(productItem.getId())
                .quantity(productItem.getQuantity())
                .productSize(ProductSizeDTO.toDTO(productItem.getProductSize()))
                .build();
    }

    public static  ProductItemDTO toDTOWithParentsInfos(ProductItem productItem){
        return  ProductItemDTO.builder()
                .id(productItem.getId())
                .quantity(productItem.getQuantity())
                .productSize(ProductSizeDTO.toDTO(productItem.getProductSize()))
                .productName(productItem.getProduct().getName())
                .productPrice(productItem.getProduct().getPrice())
                .avatar(productItem.getProduct().getAvatar())
                .color(productItem.getProduct().getProductColor().getValue())
                .build();
    }




}
