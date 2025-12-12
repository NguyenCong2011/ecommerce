package dev.cong.v.springcomereme;

import dev.cong.v.springcomereme.dao.ProductItemRepository;
import dev.cong.v.springcomereme.dao.ProductRepository;
import dev.cong.v.springcomereme.dao.ProductSizeRepository;
import dev.cong.v.springcomereme.dto.ProductItemDTO;
import dev.cong.v.springcomereme.entity.Product;
import dev.cong.v.springcomereme.entity.ProductItem;
import dev.cong.v.springcomereme.entity.ProductSize;
import dev.cong.v.springcomereme.exception.DuplicateException;
import dev.cong.v.springcomereme.request.ProductItemRequest;
import dev.cong.v.springcomereme.service.ProductItemService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;


import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductItemServiceTest {

    @Mock
    private ProductItemRepository productItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductSizeRepository productSizeRepository;

    @InjectMocks
    private ProductItemService productItemService;

    private Product existingProduct;
    private ProductSize existingSize;
    private ProductItem existingProductItem;

    @BeforeEach
    void setUp() {
        existingSize = ProductSize.builder()
                .id(1L)
                .value("M")
                .build();

        existingProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .description("Test Description")
                .price(100.0)
                .productItems(new ArrayList<>())
                .build();

        existingProductItem = ProductItem.builder()
                .id(1L)
                .quantity(10L)
                .product(existingProduct)
                .productSize(existingSize)
                .build();

        existingProduct.getProductItems().add(existingProductItem);
    }

    @Test
    void createProductItem_Success() {
        Long productId = existingProduct.getId();
        ProductItemRequest request = ProductItemRequest.builder()
                .quantity(20L)
                .productSizeId(2L)
                .build();

        ProductSize newSize = ProductSize.builder()
                .id(2L)
                .value("L")
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productSizeRepository.findById(2L)).thenReturn(Optional.of(newSize));
        when(productItemRepository.save(any(ProductItem.class))).thenAnswer(invocation -> {
            ProductItem item = invocation.getArgument(0);
            item.setId(2L);
            return item;
        });

        ResponseEntity<?> response = productItemService.create(productId, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo("Created");
        verify(productRepository, times(1)).findById(productId);
        verify(productSizeRepository, times(1)).findById(2L);
        verify(productItemRepository, times(1)).save(any(ProductItem.class));
        assertThat(existingProduct.getProductItems()).hasSize(2);
    }

    @Test
    void createProductItem_DuplicateSize_ThrowsException() {
        Long productId = existingProduct.getId();
        ProductItemRequest request = ProductItemRequest.builder()
                .quantity(15L)
                .productSizeId(1L)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        assertThatThrownBy(() -> productItemService.create(productId, request))
                .isInstanceOf(DuplicateException.class)
                .hasMessage("This product already has this size");

        verify(productRepository, times(1)).findById(productId);
        verify(productSizeRepository, never()).findById(anyLong());
        verify(productItemRepository, never()).save(any(ProductItem.class));
    }

    // Test for creating a ProductItem when Product not found
    @Test
    void createProductItem_ProductNotFound_ThrowsException() {
        Long productId = 99L;
        ProductItemRequest request = ProductItemRequest.builder()
                .quantity(10L)
                .productSizeId(2L)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productItemService.create(productId, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Not Found");

        verify(productRepository, times(1)).findById(productId);
        verify(productSizeRepository, never()).findById(anyLong());
        verify(productItemRepository, never()).save(any(ProductItem.class));
    }

    @Test
    void deleteOne_Success() {
        Long itemId = existingProductItem.getId();

        when(productItemRepository.findById(itemId)).thenReturn(Optional.of(existingProductItem));

        ResponseEntity<?> response = productItemService.deleteOne(itemId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(response.getBody()).isEqualTo("Deleted");
        verify(productItemRepository, times(1)).findById(itemId);
        verify(productItemRepository, times(1)).delete(existingProductItem);
    }



    @Test
    void findOne_Success() {
        Long itemId = existingProductItem.getId();

        when(productItemRepository.findById(itemId)).thenReturn(Optional.of(existingProductItem));

        ResponseEntity<?> response = productItemService.findOne(itemId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isInstanceOf(ProductItemDTO.class);
        ProductItemDTO dto = (ProductItemDTO) response.getBody();
        assert dto != null;
        assertThat(dto.getId()).isEqualTo(itemId);
        assertThat(dto.getQuantity()).isEqualTo(existingProductItem.getQuantity());
        verify(productItemRepository, times(1)).findById(itemId);
    }

    @Test
    void findOne_NotFound_ThrowsException() {
        Long itemId = 99L;

        when(productItemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productItemService.findOne(itemId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Not Found");

        verify(productItemRepository, times(1)).findById(itemId);
    }

    // Test for updating a ProductItem successfully without changing size
    @Test
    void updateOne_Success_WithoutChangingSize() {
        Long itemId = existingProductItem.getId();
        ProductItemRequest request = ProductItemRequest.builder()
                .quantity(30L)
                .productSizeId(1L) // Same size
                .build();

        when(productItemRepository.findById(itemId)).thenReturn(Optional.of(existingProductItem));

        ResponseEntity<?> response = productItemService.updateOne(itemId, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Product item updated");
        verify(productItemRepository, times(1)).findById(itemId);
        verify(productSizeRepository, never()).findById(anyLong());
        verify(productItemRepository, times(1)).save(existingProductItem);
        assertThat(existingProductItem.getQuantity()).isEqualTo(30L);
    }

    // Test for updating a ProductItem successfully with changing size
    @Test
    void updateOne_Success_WithChangingSize() {
        Long itemId = existingProductItem.getId();
        ProductItemRequest request = ProductItemRequest.builder()
                .quantity(25L)
                .productSizeId(2L)
                .build();

        ProductSize newSize = ProductSize.builder()
                .id(2L)
                .value("L")
                .build();

        when(productItemRepository.findById(itemId)).thenReturn(Optional.of(existingProductItem));
        when(productSizeRepository.findById(2L)).thenReturn(Optional.of(newSize));

        ResponseEntity<?> response = productItemService.updateOne(itemId, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Product item updated");
        verify(productItemRepository, times(1)).findById(itemId);
        verify(productSizeRepository, times(1)).findById(2L);
        verify(productItemRepository, times(1)).save(existingProductItem);
        assertThat(existingProductItem.getQuantity()).isEqualTo(25L);
        assertThat(existingProductItem.getProductSize()).isEqualTo(newSize);
    }

    // Test for updating a ProductItem with duplicate size
    @Test
    void updateOne_DuplicateSize_ThrowsException() {
        Long itemId = existingProductItem.getId();

        ProductItemRequest duplicateRequest = ProductItemRequest.builder()
                .quantity(20L)
                .productSizeId(2L) // Size ID that already exists
                .build();

        //simulate duplicate
        ProductSize size2 = ProductSize.builder()
                .id(2L)
                .value("L")
                .build();

        ProductItem anotherItem = ProductItem.builder()
                .id(2L)
                .quantity(5L)
                .product(existingProduct)
                .productSize(size2)
                .build();

        existingProduct.getProductItems().add(anotherItem);

        when(productItemRepository.findById(itemId)).thenReturn(Optional.of(existingProductItem));

        assertThatThrownBy(() -> productItemService.updateOne(itemId, duplicateRequest))
                .isInstanceOf(DuplicateException.class)
                .hasMessage("This product already has this size");

        verify(productItemRepository, times(1)).findById(itemId);

        verify(productSizeRepository, never()).findById(anyLong());

        verify(productItemRepository, never()).save(any(ProductItem.class));
    }

    // Test for updating a ProductItem when not found
    @Test
    void updateOne_NotFound_ThrowsException() {
        Long itemId = 99L;
        ProductItemRequest request = ProductItemRequest.builder()
                .quantity(20L)
                .productSizeId(2L)
                .build();

        when(productItemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productItemService.updateOne(itemId, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Product item not found");

        verify(productItemRepository, times(1)).findById(itemId);
        verify(productSizeRepository, never()).findById(anyLong());
        verify(productItemRepository, never()).save(any(ProductItem.class));
    }

    // Additional test: updating with non-existing size
    @Test
    void updateOne_SizeNotFound_ThrowsException() {
        Long itemId = existingProductItem.getId();
        ProductItemRequest request = ProductItemRequest.builder()
                .quantity(20L)
                .productSizeId(99L)
                .build();

        when(productItemRepository.findById(itemId)).thenReturn(Optional.of(existingProductItem));
        when(productSizeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productItemService.updateOne(itemId, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Product size not found");

        verify(productItemRepository, times(1)).findById(itemId);
        verify(productSizeRepository, times(1)).findById(99L);
        verify(productItemRepository, never()).save(any(ProductItem.class));
    }

}
