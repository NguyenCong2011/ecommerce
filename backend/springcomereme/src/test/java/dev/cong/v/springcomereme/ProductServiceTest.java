package dev.cong.v.springcomereme;


import dev.cong.v.springcomereme.dao.BrandRepository;
import dev.cong.v.springcomereme.dao.CategoryRepository;
import dev.cong.v.springcomereme.dao.ProductColorRepository;
import dev.cong.v.springcomereme.dao.ProductRepository;
import dev.cong.v.springcomereme.entity.Brand;
import dev.cong.v.springcomereme.entity.Category;
import dev.cong.v.springcomereme.entity.Product;
import dev.cong.v.springcomereme.entity.ProductColor;
import dev.huynh.v.springcomereme.dao.*;
import dev.cong.v.springcomereme.dto.FilterCriteria;
import dev.cong.v.springcomereme.dto.ProductDTO;
import dev.huynh.v.springcomereme.entity.*;
import dev.cong.v.springcomereme.enums.SortType;
import dev.cong.v.springcomereme.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private ProductColorRepository colorRepository;

    @InjectMocks
    private ProductService productService;

    private Product existingProduct;
    private Category existingCategory;
    private Brand existingBrand;
    private ProductColor existingColor;

    @BeforeEach
    void setUp() {
        existingCategory = Category.builder()
                .id(1L)
                .name("Electronics")
                .products(new ArrayList<>())
                .build();

        existingBrand = Brand.builder()
                .id(1L)
                .name("Samsung")
                .products(new ArrayList<>())
                .build();

        existingColor = ProductColor.builder()
                .id(1L)
                .value("Black")
                .products(new ArrayList<>())
                .build();

        existingProduct = Product.builder()
                .id(1L)
                .name("Smartphone")
                .description("Latest smartphone with advanced features")
                .avatar("avatar.png")
                .images(Arrays.asList("img1.png", "img2.png"))
                .price(999.99)
                .category(existingCategory)
                .brand(existingBrand)
                .productColor(existingColor)
                .productItems(new ArrayList<>())
                .showHomepage(false)
                .build();

        existingCategory.getProducts().add(existingProduct);
        existingBrand.getProducts().add(existingProduct);
        existingColor.getProducts().add(existingProduct);
    }

    private ProductDTO createProductDTO(Long id, String name, String description, String avatar,
                                        List<String> images, Long categoryId, Long brandId, Double price,
                                        Boolean showHomepage) {
        ProductDTO dto = new ProductDTO();
        dto.setId(id);
        dto.setName(name);
        dto.setDescription(description);
        dto.setAvatar(avatar);
        dto.setImages(images);
        dto.setCategoryId(categoryId);
        dto.setBrandId(brandId);
        dto.setPrice(price);
        dto.setProductColorId(1L);
        dto.setShowHomepage(showHomepage);
        return dto;
    }

    @Test
    void createProduct_Success() {
        ProductDTO productRequest = createProductDTO(null, "Laptop", "High-performance laptop",
                "laptop.png", Arrays.asList("laptop1.png", "laptop2.png"), 1L, 1L, 1500.0, false);

        Product savedProduct = Product.builder()
                .id(2L)
                .name("Laptop")
                .description("High-performance laptop")
                .avatar("laptop.png")
                .images(Arrays.asList("laptop1.png", "laptop2.png"))
                .price(1500.0)
                .category(existingCategory)
                .brand(existingBrand)
                .productColor(existingColor)
                .productItems(new ArrayList<>())
                .showHomepage(false)
                .build();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(brandRepository.findById(1L)).thenReturn(Optional.of(existingBrand));
        when(colorRepository.findById(1L)).thenReturn(Optional.of(existingColor));
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // Act
        ResponseEntity<?> response = productService.create(productRequest);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isInstanceOf(ProductDTO.class);
        ProductDTO responseBody = (ProductDTO) response.getBody();
        assertThat(responseBody.getId()).isEqualTo(2L);
        assertThat(responseBody.getName()).isEqualTo("Laptop");
        verify(categoryRepository, times(1)).findById(1L);
        verify(brandRepository, times(1)).findById(1L);
        verify(colorRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void createProduct_CategoryNotFound_ThrowsException() {
        ProductDTO productRequest = createProductDTO(null, "Laptop", "High-performance laptop",
                "laptop.png", Arrays.asList("laptop1.png", "laptop2.png"), 99L, 1L, 1500.0, false);

        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.create(productRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Not found");

        verify(categoryRepository, times(1)).findById(99L);
        verify(brandRepository, never()).findById(anyLong());
        verify(colorRepository, never()).findById(anyLong());
        verify(productRepository, never()).save(any(Product.class));
    }


    @Test
    void filterProducts_Success() {

        FilterCriteria criteria = new FilterCriteria();
        criteria.setCategory("1");
        criteria.setBrand("1");
        criteria.setColor(Arrays.asList(1L, 2L));
        criteria.setKeyword("Smartphone");
        criteria.setMinPrice(500.0);
        criteria.setMaxPrice(1500.0);
        criteria.setSize(Arrays.asList("M", "L"));
        criteria.setSort(SortType.PriceASC);
        criteria.setPage(0);
        criteria.setLimit(10);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "price"));

        Product product2 = Product.builder()
                .id(2L)
                .name("Laptop")
                .description("High-performance laptop")
                .avatar("laptop.png")
                .images(Arrays.asList("laptop1.png", "laptop2.png"))
                .price(1200.0)
                .category(existingCategory)
                .brand(existingBrand)
                .productColor(existingColor)
                .productItems(new ArrayList<>())
                .showHomepage(false)
                .build();

        List<Product> filteredProducts = Arrays.asList(existingProduct, product2);
        Page<Product> productPage = new PageImpl<>(filteredProducts, pageable, filteredProducts.size());

        when(productRepository.findAll((Specification<Product>) any(Specification.class), eq(pageable))).thenReturn(productPage);

        ResponseEntity<?> response = productService.filter(criteria);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isInstanceOf(Page.class);

        Page<ProductDTO> responseBody = (Page<ProductDTO>) response.getBody();
        assert responseBody != null;
        assertThat(responseBody.getTotalElements()).isEqualTo(2);
        assertThat(responseBody.getContent()).extracting("name")
                .containsExactlyInAnyOrder("Smartphone", "Laptop");

        verify(productRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void getProductById_Success() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));

        // Act
        ResponseEntity<?> response = productService.getById(1L);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isInstanceOf(ProductDTO.class);
        ProductDTO responseBody = (ProductDTO) response.getBody();
        assertThat(responseBody.getId()).isEqualTo(1L);
        assertThat(responseBody.getName()).isEqualTo("Smartphone");
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void getProductById_NotFound_ThrowsException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Not Found");

        verify(productRepository, times(1)).findById(99L);
    }

    @Test
    void updateProduct_Success_NoChanges() {
        ProductDTO productRequest = createProductDTO(1L, "Smartphone Updated", "Updated Description",
                "new_avatar.png", Arrays.asList("new_img1.png"), 1L, 1L, 1099.99, true);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        ResponseEntity<?> response = productService.update(1L, productRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isInstanceOf(ProductDTO.class);
        ProductDTO responseBody = (ProductDTO) response.getBody();
        assert responseBody != null;
        assertThat(responseBody.getName()).isEqualTo("Smartphone Updated");
        assertThat(responseBody.getDescription()).isEqualTo("Updated Description");
        assertThat(responseBody.getAvatar()).isEqualTo("new_avatar.png");
        verify(categoryRepository, never()).findById(anyLong());
        verify(brandRepository, never()).findById(anyLong());
        verify(colorRepository, never()).findById(anyLong());
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    void updateProduct_Success_ChangeCategory() {
        Category newCategory = Category.builder()
                .id(2L)
                .name("Computers")
                .products(new ArrayList<>())
                .build();

        ProductDTO productRequest = createProductDTO(1L, "Smartphone", "Updated Description",
                "avatar.png", List.of("img1.png"), 2L, 1L, 999.99, false);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(newCategory));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        ResponseEntity<?> response = productService.update(1L, productRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ProductDTO responseBody = (ProductDTO) response.getBody();
        assertThat(responseBody.getDescription()).isEqualTo("Updated Description");
        assertThat(responseBody.getCategoryId()).isEqualTo(2L);
        verify(categoryRepository, times(1)).findById(2L);
        verify(productRepository, times(1)).save(existingProduct);
    }






    @Test
    void showHomePageChange_Success() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setShowHomepage(true);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        ResponseEntity<?> response = productService.showHomePageChange(1L, productDTO);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Updated");
        assertThat(existingProduct.getShowHomepage()).isTrue();
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(existingProduct);
    }



    // Test for deleting a Product successfully
    @Test
    void deleteProduct_Success() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        doNothing().when(productRepository).delete(existingProduct);

        // Act
        ResponseEntity<?> response = productService.delete(1L);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(response.getBody()).isEqualTo("Deleted");
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).delete(existingProduct);
    }




}
