package dev.cong.v.springcomereme.controller;

import dev.cong.v.springcomereme.dto.AddressDTO;
import dev.cong.v.springcomereme.entity.Address;
import dev.cong.v.springcomereme.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {


        private  final AddressService addressService;

        @PostMapping
        public ResponseEntity<?> addAddress(@RequestBody Address address, @AuthenticationPrincipal UserDetails userDetails){



            return  addressService.addAddress(address,userDetails.getUsername());


        }

        @GetMapping("/auth")
        public  ResponseEntity<?> getAddressUser( @AuthenticationPrincipal UserDetails userDetails){

            return  addressService.getAddressUser(userDetails.getUsername());
        }

        @DeleteMapping("/{id}")
        public  ResponseEntity<?> deleteAddress(@PathVariable("id") Long id){

            return  addressService.deleteAddress(id);
        }

        @PutMapping("/{id}")
        public  ResponseEntity<?> updateAddress(@PathVariable("id") Long id, @RequestBody AddressDTO addressDTO){

            return  addressService.updateAddress(id,addressDTO);
        }

        @GetMapping("/{id}")
        public  ResponseEntity<?> getSpecific(@PathVariable("id") Long id){

            return  addressService.getSpecific(id);
        }

        @PutMapping("/set-default")
        public  ResponseEntity<?> setDefault( @AuthenticationPrincipal UserDetails userDetails ,  @RequestBody AddressDTO addressDTO){


            return  addressService.setAddressDefault(userDetails.getUsername(),addressDTO);
        }







}
