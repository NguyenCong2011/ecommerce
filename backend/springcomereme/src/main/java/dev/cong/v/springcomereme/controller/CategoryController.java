package dev.cong.v.springcomereme.controller;
import dev.cong.v.springcomereme.entity.Category;
import dev.cong.v.springcomereme.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/category")
public class CategoryController {

        private  final CategoryService categoryService;

        @GetMapping("/all")
        public ResponseEntity<?> getAll(){
            return  categoryService.getAll();
        }

        @PostMapping
        public  ResponseEntity<?> createOne(@RequestBody Category category){
            return  categoryService.createOne(category);
        }

        @PutMapping("/{id}")
        public  ResponseEntity<?> updateOne(@PathVariable("id") Long id, @RequestBody Category category){
            return  categoryService.updateOne(id,category);
        }

        @GetMapping("/{id}")
        public  ResponseEntity<?> getOne(@PathVariable("id") Long id){
            return  categoryService.getOne(id);
        }

        @DeleteMapping("/{id}")
        public  ResponseEntity<?> deleteOne(@PathVariable("id") Long id){
            return  categoryService.deleteOne(id);
        }


}
