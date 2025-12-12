package dev.cong.v.springcomereme.controller;

import dev.cong.v.springcomereme.request.CartRequest;
import dev.cong.v.springcomereme.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class ShoppingCartController {

    private  final  ShoppingCartService shoppingCartService;


    @PostMapping
    public ResponseEntity<?> create(@RequestBody CartRequest cartRequest, @AuthenticationPrincipal UserDetails userDetails){


        return  shoppingCartService.create(cartRequest,userDetails.getUsername());

    }

    @GetMapping("/auth")
    public  ResponseEntity<?> getAuth(@AuthenticationPrincipal UserDetails userDetails){
        return  shoppingCartService.getAuth(userDetails.getUsername());
    }

    @PutMapping("/quantity/{id}")
    public ResponseEntity<?> updateQuantity(@PathVariable("id") Long id,  @RequestBody CartRequest cartRequest){
        return  shoppingCartService.updateQuantity(id,cartRequest);

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        return  shoppingCartService.delete(id);
    }



}
