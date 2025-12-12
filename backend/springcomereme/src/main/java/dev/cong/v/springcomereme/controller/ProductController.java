package dev.cong.v.springcomereme.controller;

import dev.cong.v.springcomereme.dto.FilterCriteria;
import dev.cong.v.springcomereme.dto.ProductDTO;
import dev.cong.v.springcomereme.enums.SortType;
import dev.cong.v.springcomereme.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/product")
@RestController
@RequiredArgsConstructor
public class ProductController {
    private  final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long id){

        return  productService.getById(id);
    }
    @GetMapping("/random")
    public  ResponseEntity<?> getRandom(@RequestParam("id") Long id){
        return  productService.getRandom(id);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filter(@RequestParam(value = "category", required = false)  String  category,
                                    @RequestParam(value = "brand", required = false)  String  brand,
                                    @RequestParam(value = "color", required = false)  List<Long>  color,
                                    @RequestParam(value = "size", required = false) List<String> size,
                                    @RequestParam(value = "keyword", required = false) String keyword,
                                    @RequestParam(value = "minPrice",required = false) Double minPrice,
                                    @RequestParam(value = "maxPrice",required = false) Double maxPrice,
                                    @RequestParam(value = "sort",required = false) SortType sortType,
                                    @RequestParam(value = "page", defaultValue = "0") int page,
                                    @RequestParam(value = "limit", defaultValue = "6") int sizePerPage){

            FilterCriteria filterCriteria = FilterCriteria.builder()
                    .category(category)
                    .size(size)
                    .page(page)
                    .limit(sizePerPage)
                    .brand(brand)
                    .color(color)
                    .keyword(keyword)
                    .minPrice(minPrice)
                    .maxPrice(maxPrice)
                    .sort(sortType).build();

        return productService.filter(filterCriteria);
    }





    @GetMapping("/search")
    public  ResponseEntity<?> getSearch(@RequestParam(value = "name") String name  ){

        return  productService.getBySearch(name);
    }



    @PutMapping("/{id}")
    public  ResponseEntity<?> updateProduct(@PathVariable("id") Long id,@RequestBody ProductDTO productDTO){
        return  productService.update(id,productDTO);
    }

    @PutMapping("/show/{id}")
    public  ResponseEntity<?> showHomePageChange(@PathVariable("id") Long id ,@RequestBody ProductDTO productDTO){
        return  productService.showHomePageChange(id,productDTO);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<?> deleteProduct(@PathVariable("id") Long id){
        return  productService.delete(id);
    }

    @PostMapping
    public  ResponseEntity<?> createProduct(@RequestBody ProductDTO productDTO){
        return  productService.create(productDTO);
    }


    @GetMapping("/all")
    public  ResponseEntity<?> getAllProduct(){
        return  productService.getAll();
    }
  @GetMapping("/show-on-homepage")
  public  ResponseEntity<?> getShowHomepage(){
        return  productService.getShowHomepage();
  }


    @GetMapping("/{id}/config")
    public  ResponseEntity<?> getConfigsProduct(@PathVariable("id") Long id){
        return  productService.getConfigsProduct(id);
    }

}
