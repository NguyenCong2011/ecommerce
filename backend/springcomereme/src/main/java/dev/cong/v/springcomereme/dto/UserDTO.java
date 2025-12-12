package dev.cong.v.springcomereme.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.cong.v.springcomereme.entity.User;
import dev.cong.v.springcomereme.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private  long id;

    private  String email;

    private  String firstName;

    private  String lastName;

    private  String photoURL;

    private Gender gender;

    private String dob;

    public  static UserDTO toDTO(User user){
        return  UserDTO.builder().
                id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .photoURL(user.getPhotoURL())
                .gender(user.getGender())
                .dob(user.getDob())
                .build();
    }
}
