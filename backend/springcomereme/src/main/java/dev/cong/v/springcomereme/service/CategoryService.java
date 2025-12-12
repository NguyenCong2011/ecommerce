package dev.cong.v.springcomereme.service;

import dev.cong.v.springcomereme.dao.CategoryRepository;
import dev.cong.v.springcomereme.dto.CategoryDTO;
import dev.cong.v.springcomereme.entity.Category;
import dev.cong.v.springcomereme.exception.DuplicateException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private  final CategoryRepository categoryRepository;


    public ResponseEntity<?> getAll() {


        return  ResponseEntity.ok(categoryRepository.findAll());

    }

    public ResponseEntity<?> createOne(Category category) {

       var duplicate = categoryRepository.findByName(category.getName());

       if(duplicate.isPresent()){
           throw  new DuplicateException("Category with name " + category.getName() + " already exists.");
       }

       categoryRepository.save(category);

       return  ResponseEntity.status(HttpStatus.CREATED).body("Ok");

    }

    public ResponseEntity<?> updateOne(Long id, Category category) {
        var existing = categoryRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Not Found category with id " + id)
        );

       if(categoryRepository.findByName(category.getName()).isPresent()
               && !existing.getName().equals(category.getName())  ){
           throw  new DuplicateException("Category with name " + category.getName() + " already exists.");
       }

        BeanUtils.copyProperties(category,existing,"id");

        categoryRepository.save(existing);

        return  ResponseEntity.ok("updated");
    }

    public ResponseEntity<?> deleteOne(Long id) {
        var existing = categoryRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Not Found category with id " + id)
        );

        categoryRepository.delete(existing);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("deleted");

    }

    public ResponseEntity<?> getOne(Long id) {
        var existing = categoryRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Not Found category with id " + id)
        );

        CategoryDTO categoryDTO = CategoryDTO.builder()
                .id(existing.getId())
                .name(existing.getName())
                .image(existing.getImage())
                .build();

        return  ResponseEntity.ok(categoryDTO);
    }
}
