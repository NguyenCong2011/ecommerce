package dev.cong.v.springcomereme.service;

import dev.cong.v.springcomereme.dao.ProductSizeRepository;
import dev.cong.v.springcomereme.dto.ProductSizeDTO;
import dev.cong.v.springcomereme.entity.ProductSize;
import dev.cong.v.springcomereme.exception.DuplicateException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSizeService {
    private  final ProductSizeRepository sizeRepository;

    public ResponseEntity<?> getAll() {
        List<ProductSize> productSizes = sizeRepository.findAll();
        List<ProductSizeDTO> sizeDTOS = productSizes.stream().map(ProductSizeDTO::toDTO).toList();

        return ResponseEntity.ok(sizeDTOS);
    }

    public ResponseEntity<?> createOne(ProductSize size) {
            if(sizeRepository.findByValue(size.getValue()).isPresent()){
                throw  new DuplicateException("Size " + size.getValue() + " already exits");
            }



            sizeRepository.save(size);

            return  ResponseEntity.status(HttpStatus.CREATED).body("Created");

    }

    public ResponseEntity<?> deleteOne(Long id) {
        var existing  = sizeRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Not Found")
        );

        sizeRepository.delete(existing);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Deleted");
    }
}
