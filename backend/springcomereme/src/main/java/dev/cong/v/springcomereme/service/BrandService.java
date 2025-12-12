package dev.cong.v.springcomereme.service;

import dev.cong.v.springcomereme.dao.BrandRepository;
import dev.cong.v.springcomereme.entity.Brand;
import dev.cong.v.springcomereme.exception.DuplicateException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {
    private  final BrandRepository brandRepository;


    public ResponseEntity<?> getAll() {
        List<Brand> brandList = brandRepository.findAll();

        return  ResponseEntity.ok(brandList);
    }

    public ResponseEntity<?> create(Brand brand) {
        if (brandRepository.findByName(brand.getName()).isPresent()) {
            throw new DuplicateException("Brand with name " + brand.getName() + " exists");
        }

        brandRepository.save(brand);

        return ResponseEntity.status(HttpStatus.CREATED).body("Created successfully");
    }

    public ResponseEntity<?> findOne(Long id) {
        var brand = brandRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Cant find brand")
        );
        return  ResponseEntity.ok(brand);
    }

    public ResponseEntity<?> deleteOne(Long id) {
        var brand = brandRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Cant find brand")
        );

        brandRepository.delete(brand);

        return  ResponseEntity.status(HttpStatus.ACCEPTED).body("Deleted");
    }

    public ResponseEntity<?> updateOne(Long id, Brand brand) {
        var existing = brandRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Cant find brand")
        );

        if(brandRepository.findByName(brand.getName()).isPresent() &&
            !existing.getName().equals(brand.getName())
        ){
            throw  new DuplicateException("Brand with name " + brand.getName()+" existed");
        }

        existing.setName(brand.getName());

        brandRepository.save(existing);

        return  ResponseEntity.ok("Updated");


    }
}
