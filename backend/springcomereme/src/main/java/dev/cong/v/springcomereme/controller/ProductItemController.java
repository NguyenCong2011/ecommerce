package dev.cong.v.springcomereme.controller;



import dev.cong.v.springcomereme.request.ProductItemRequest;
import dev.cong.v.springcomereme.service.ProductItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product-item")
public class ProductItemController {

    private  final ProductItemService service;

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOne(@PathVariable("id") Long id, @RequestBody ProductItemRequest productItem){
        return  service.updateOne(id,productItem);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findOne(@PathVariable("id") Long id){
        return  service.findOne(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?>delete(@PathVariable("id") Long id){
        return  service.deleteOne(id);
    }

    @PostMapping("/{productId}")
    public ResponseEntity<?> create(@PathVariable("productId") Long productId , @RequestBody
    ProductItemRequest productItemDTO){
        return  service.create(productId,productItemDTO);
    }





}
