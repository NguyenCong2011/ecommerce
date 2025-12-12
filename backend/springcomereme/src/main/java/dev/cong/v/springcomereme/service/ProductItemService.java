package dev.cong.v.springcomereme.service;

import dev.cong.v.springcomereme.dao.ProductItemRepository;
import dev.cong.v.springcomereme.dao.ProductRepository;
import dev.cong.v.springcomereme.dao.ProductSizeRepository;
import dev.cong.v.springcomereme.dto.ProductItemDTO;
import dev.cong.v.springcomereme.entity.ProductItem;
import dev.cong.v.springcomereme.exception.DuplicateException;
import dev.cong.v.springcomereme.request.ProductItemRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductItemService {
    private  final ProductItemRepository repository;
    private  final ProductRepository productRepository;
    private  final ProductSizeRepository sizeRepository;


    public ResponseEntity<?> create(Long productId, ProductItemRequest productItemDTO) {
        var existing  = productRepository.findById(productId).orElseThrow(
                ()-> new EntityNotFoundException("Not Found")
         );

        List<ProductItem> productItemList = existing.getProductItems();

        for(ProductItem productItem :productItemList){
            if(Objects.equals(productItem.getProductSize().getId(), productItemDTO.getProductSizeId())){
                throw  new DuplicateException("This product already has this size");
            }
        }

        ProductItem productItem = new ProductItem();

        productItem.setQuantity(productItemDTO.getQuantity());

        var size = sizeRepository.findById(productItemDTO.getProductSizeId()).orElseThrow(
                ()-> new EntityNotFoundException("Not Found")
        );

        size.addProductItem(productItem);
        existing.addProductItem(productItem);

        repository.save(productItem);

        return ResponseEntity.status(HttpStatus.CREATED).body("Created");
    }

    public ResponseEntity<?> deleteOne(Long id) {
        var existing  = repository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Not Found")
        );

        repository.delete(existing);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Deleted");

    }

    public ResponseEntity<?> findOne(Long id) {
        var existing  = repository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Not Found")
        );

        return  ResponseEntity.ok(ProductItemDTO.toDTO(existing));
    }

    public ResponseEntity<?> updateOne(Long id, ProductItemRequest itemRequest) {
        var existing = repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Product item not found")
        );

        var product = existing.getProduct();

        // check for duplicates
        if (!Objects.equals(existing.getProductSize().getId(), itemRequest.getProductSizeId())) {
            List<ProductItem> productItemList = product.getProductItems();
            for (ProductItem productItem : productItemList) {
                if (Objects.equals(productItem.getProductSize().getId(), itemRequest.getProductSizeId())) {
                    throw new DuplicateException("This product already has this size");
                }
            }
        }

        existing.setQuantity(itemRequest.getQuantity());

        // If the size is updated
        if (!Objects.equals(existing.getProductSize().getId(), itemRequest.getProductSizeId())) {
            var size = sizeRepository.findById(itemRequest.getProductSizeId()).orElseThrow(
                    () -> new EntityNotFoundException("Product size not found")
            );
            existing.setProductSize(size);
        }

        repository.save(existing);

        return ResponseEntity.status(HttpStatus.OK).body("Product item updated");
    }

}
