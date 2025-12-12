package dev.cong.v.springcomereme.dto;

import dev.cong.v.springcomereme.entity.ProductSize;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductSizeDTO {
    private  Long id;
    private String value;

    public  static  ProductSizeDTO toDTO(ProductSize productSize){
        return  ProductSizeDTO.builder()
                .id(productSize.getId())
                .value(productSize.getValue())
                .build();
    }
}
