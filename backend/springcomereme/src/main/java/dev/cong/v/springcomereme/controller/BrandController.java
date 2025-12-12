package dev.cong.v.springcomereme.controller;

import dev.cong.v.springcomereme.entity.Brand;
import dev.cong.v.springcomereme.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/brand")
@RestController
@RequiredArgsConstructor
public class BrandController {
    private  final BrandService brandService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        return  brandService.getAll();
    }
    @PostMapping
    public  ResponseEntity<?> createOne(@RequestBody Brand brand){
        return  brandService.create(brand);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?>getOne(@PathVariable("id") Long id){
        return  brandService.findOne(id);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOne(@PathVariable("id") Long id){
        return  brandService.deleteOne(id);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOne(@PathVariable("id") Long id ,@RequestBody Brand brand){
        return  brandService.updateOne(id,brand);
    }
}
