package dev.cong.v.springcomereme.dto;


import dev.cong.v.springcomereme.entity.Address;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

    private  Long id;


    private String fullName;

    private  String phoneNumber;

    private String district;

    private  String province;

    private  String ward;

    private  String specify;

    private  Boolean isDefault;

    public  static  AddressDTO toDTO(Address address){

        return  AddressDTO.builder()
                .id(address.getId())
                .fullName(address.getFullName())
                .district(address.getDistrict())
                .province(address.getProvince())
                .ward(address.getWard())
                .specify(address.getSpecify())
                .isDefault(address.getIsDefault())
                .phoneNumber(address.getPhoneNumber())
                .build();

    }
}
