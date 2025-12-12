package dev.cong.v.springcomereme.controller;

import dev.cong.v.springcomereme.request.OrderRequest;
import dev.cong.v.springcomereme.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private  final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> create (@RequestBody OrderRequest request,
                                     @AuthenticationPrincipal UserDetails userDetails){

        return orderService.create(request,userDetails.getUsername());
    }



    @GetMapping("/auth")
    public ResponseEntity<?> getAuth(@AuthenticationPrincipal UserDetails userDetails){
        return  orderService.getByAuth(userDetails.getUsername());
    }

    @GetMapping("/all")
    public  ResponseEntity<?> getAll(){
        return  orderService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id){
        return  orderService.getById(id);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<?> deleteOne(@PathVariable("id")Long id){
        return  orderService.delete(id);
    }


    @PutMapping("/{id}")
    public  ResponseEntity<?> changeStatus(@PathVariable("id") Long id,@RequestBody OrderRequest orderDTO){
        return  orderService.changeStatus(id,orderDTO);
    }
}
