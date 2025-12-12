package dev.cong.v.springcomereme.dto;

import dev.cong.v.springcomereme.entity.Brand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandDTO {

    private  Long id;

    private String name;

    public  static  BrandDTO toDTO(Brand brand){
        return  BrandDTO.builder().id(brand.getId())
                .name(brand.getName())
                .build();
    }


}
