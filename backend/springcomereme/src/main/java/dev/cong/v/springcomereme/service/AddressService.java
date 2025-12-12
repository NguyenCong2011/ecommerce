package dev.cong.v.springcomereme.service;

import dev.cong.v.springcomereme.dao.AddressRepository;
import dev.cong.v.springcomereme.dao.UserRepository;
import dev.cong.v.springcomereme.dto.AddressDTO;
import dev.cong.v.springcomereme.entity.Address;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private  final UserRepository userRepository;


    public ResponseEntity<?> addAddress(Address address, String email) {
        var user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Not Found")
        );


        if(user.getAddresses().isEmpty()){
            address.setIsDefault(true);
        }

        user.addAddress(address);


        addressRepository.save(address);

        return ResponseEntity.status(HttpStatus.CREATED).body("Ok");

    }

    public ResponseEntity<?> getAddressUser(String email) {
        var user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Not Found")
        );

        List<AddressDTO> addressList = user.getAddresses().stream().map(AddressDTO::toDTO).toList();

        return  ResponseEntity.ok(addressList);

    }

    public ResponseEntity<?> deleteAddress(Long id) {
        var address= addressRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not Found")
        );

        addressRepository.delete(address);

        return  ResponseEntity.accepted().body("Ok");
    }

    public ResponseEntity<?> updateAddress(Long id, AddressDTO addressDTO) {
        var address= addressRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not Found")
        );

        BeanUtils.copyProperties(addressDTO,address,"id","isDefault");

        addressRepository.save(address);

        return  ResponseEntity.ok("Updated");

    }

    public ResponseEntity<?> getSpecific(Long id) {
        var address= addressRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not Found")
        );

        AddressDTO addressDTO = AddressDTO.toDTO(address);

        return  ResponseEntity.ok(addressDTO);
    }

    public ResponseEntity<?> setAddressDefault(String email, AddressDTO addressDTO) {

        var user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Not Found")
        );

        List<Address> userAddresses =user.getAddresses();

        userAddresses.forEach(address -> address.setIsDefault(false));


        var address= addressRepository.findById(addressDTO.getId()).orElseThrow(
                () -> new EntityNotFoundException("Not Found")
        );



        address.setIsDefault(true);

        addressRepository.save(address);
        return  ResponseEntity.ok(addressDTO);
    }



}
