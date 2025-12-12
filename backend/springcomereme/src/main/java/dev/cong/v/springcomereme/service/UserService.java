package dev.cong.v.springcomereme.service;

import dev.cong.v.springcomereme.dao.UserRepository;
import dev.cong.v.springcomereme.dto.UserDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private  final UserRepository repository;

    public ResponseEntity<?> getInformation(String email) {

        var user = repository.findByEmail(email).orElseThrow(
                ()-> new EntityNotFoundException("Cannot find user")
        );

        return  ResponseEntity.ok(UserDTO.toDTO(user));


    }

    public ResponseEntity<?> updateInformation(String email,UserDTO userDTO) {
        var user = repository.findByEmail(email).orElseThrow(
                ()-> new EntityNotFoundException("Cannot find user")
        );

        BeanUtils.copyProperties(userDTO,user,"id","photoURL","addresses");

        repository.save(user);

        return  ResponseEntity.ok("updated");

    }
}
