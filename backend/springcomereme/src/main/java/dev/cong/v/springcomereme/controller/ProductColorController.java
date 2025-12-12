package dev.cong.v.springcomereme.controller;

import dev.cong.v.springcomereme.entity.ProductColor;
import dev.cong.v.springcomereme.service.ProductColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/color")
@RequiredArgsConstructor
public class ProductColorController {

    private  final ProductColorService service;

    @GetMapping("/all")
    public ResponseEntity<?> getAll(){

        return  service.getAll();
    }

    @PostMapping
    public  ResponseEntity<?> createOne(@RequestBody ProductColor color){
        return  service.createOne(color);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<?> deleteOne(@PathVariable("id") Long id){
        return  service.deleteOne(id);
    }

}
