package dev.cong.v.springcomereme.controller;


import dev.cong.v.springcomereme.entity.ProductSize;
import dev.cong.v.springcomereme.service.ProductSizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/size")
@RequiredArgsConstructor
public class ProductSizeController {
    private  final ProductSizeService service;

    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        return  service.getAll();
    }
    @PostMapping
    public  ResponseEntity<?> createOne(@RequestBody ProductSize size){
        return  service.createOne(size);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<?> deleteOne(@PathVariable("id") Long id){
        return  service.deleteOne(id);
    }
}
