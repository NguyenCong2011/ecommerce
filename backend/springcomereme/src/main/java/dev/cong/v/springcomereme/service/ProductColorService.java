package dev.cong.v.springcomereme.service;

import dev.cong.v.springcomereme.dao.ProductColorRepository;
import dev.cong.v.springcomereme.dto.ProductColorDTO;
import dev.cong.v.springcomereme.entity.ProductColor;
import dev.cong.v.springcomereme.exception.DuplicateException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductColorService {
    private  final ProductColorRepository colorRepository;

    public ResponseEntity<?> getAll() {
        List<ProductColorDTO> dtos = colorRepository.findAll().stream()
                .map(ProductColorDTO::toDTO).toList();
        return  ResponseEntity.ok(dtos);
    }

    public ResponseEntity<?> createOne(ProductColor size) {
        if(colorRepository.findByValue(size.getValue()).isPresent()){
            throw new DuplicateException("Color " + size.getValue() +" already exits ");
        }

        colorRepository.save(size);

        return  ResponseEntity.status(HttpStatus.CREATED).body("Created");
    }

    public ResponseEntity<?> deleteOne(Long id) {

        var existing  = colorRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Not Found")
        );

        colorRepository.delete(existing);

        return  ResponseEntity.status(HttpStatus.ACCEPTED).body("Deleted");
    }
}
