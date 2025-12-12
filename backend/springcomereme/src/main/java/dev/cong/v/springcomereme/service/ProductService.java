package dev.cong.v.springcomereme.service;

import dev.cong.v.springcomereme.dao.BrandRepository;
import dev.cong.v.springcomereme.dao.CategoryRepository;
import dev.cong.v.springcomereme.dao.ProductColorRepository;
import dev.cong.v.springcomereme.dao.ProductRepository;
import dev.cong.v.springcomereme.dao.*;
import dev.cong.v.springcomereme.dto.FilterCriteria;
import dev.cong.v.springcomereme.dto.ProductDTO;
import dev.cong.v.springcomereme.dto.ProductItemDTO;
import dev.cong.v.springcomereme.entity.Brand;
import dev.cong.v.springcomereme.entity.Category;
import dev.cong.v.springcomereme.entity.Product;
import dev.cong.v.springcomereme.entity.ProductColor;
import dev.cong.v.springcomereme.enums.SortType;
import dev.cong.v.springcomereme.specification.ProductSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private  final ProductRepository productRepository;
    private  final CategoryRepository categoryRepository;
    private  final BrandRepository brandRepository;
    private  final ProductColorRepository colorRepository;



    public ResponseEntity<?> create(ProductDTO productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setAvatar(productRequest.getAvatar());
        product.setImages(productRequest.getImages());
        product.setPrice(productRequest.getPrice());

        Category category = categoryRepository.findById(productRequest.getCategoryId()).orElseThrow(
                ()-> new EntityNotFoundException("Not found")
        );
        category.addProduct(product);

        Brand brand = brandRepository.findById(productRequest.getBrandId()).orElseThrow(
                ()-> new EntityNotFoundException("Not found")
        );
        brand.addProduct(product);

        ProductColor productColor = colorRepository.findById(productRequest.getProductColorId()).orElseThrow(
                ()-> new EntityNotFoundException("Not found")
        );
        productColor.addProduct(product);

        ProductDTO productDTO =    ProductDTO.toDTO( productRepository.save(product));

        return  ResponseEntity.status(HttpStatus.CREATED).body(productDTO);

    }

    public ResponseEntity<?> getAll() {

        List<ProductDTO> products = productRepository.findAll()
                .stream().map(ProductDTO::toDTO).toList();

        return  ResponseEntity.ok(products);
    }


    public ResponseEntity<?> getById(Long id) {
        var existing = productRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Not Found")
        );

        ProductDTO productDTO = ProductDTO.toDTOWithItems(existing);

        return ResponseEntity.ok(productDTO);
    }

    public ResponseEntity<?> getConfigsProduct(Long id) {

        var existing = productRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Not Found")
        );

        List<ProductItemDTO> productItemDTOS = existing.getProductItems().stream().
                map(ProductItemDTO::toDTO).toList();

        return  ResponseEntity.ok(productItemDTOS);


    }

    public ResponseEntity<?> update(Long id, ProductDTO productRequest) {

        var product = productRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Product not found")
        );


        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setAvatar(productRequest.getAvatar());
        product.setImages(productRequest.getImages());
        product.setPrice(productRequest.getPrice());

        // Update Category if changed
        if (!product.getCategory().getId().equals(productRequest.getCategoryId())) {
            Category category = categoryRepository.findById(productRequest.getCategoryId()).orElseThrow(
                    () -> new EntityNotFoundException("Category not found")
            );
            category.addProduct(product);  // new category
        }

        // Update Brand if changed
        if (!product.getBrand().getId().equals(productRequest.getBrandId())) {
            Brand brand = brandRepository.findById(productRequest.getBrandId()).orElseThrow(
                    () -> new EntityNotFoundException("Brand not found")
            );
            brand.addProduct(product);  // new brand
        }

        // Update ProductColor if changed
        if (!product.getProductColor().getId().equals(productRequest.getProductColorId())) {
            ProductColor productColor = colorRepository.findById(productRequest.getProductColorId()).orElseThrow(
                    () -> new EntityNotFoundException("ProductColor not found")
            );
            productColor.addProduct(product);  // new color
        }

        productRepository.save(product);

        ProductDTO updatedProductDTO = ProductDTO.toDTOWithItems(product);

        return ResponseEntity.ok(updatedProductDTO);
    }

    public ResponseEntity<?> getRandom(Long id) {
        List<ProductDTO> productDTOS = productRepository.findRandomProducts(id).stream()
                .map(ProductDTO::toDTODisplayScreen).toList();

        return  ResponseEntity.ok(productDTOS);
    }


    public ResponseEntity<?> filter(FilterCriteria criteria) {


        Pageable pageable = createPageable(criteria.getSort(), criteria.getPage(), criteria.getLimit());


        Specification<Product> spec = getProductSpecification(criteria);
        System.out.println("Specification: " + criteria.toString());

        Page<Product> products = productRepository.findAll(spec, pageable);




        return ResponseEntity.ok(products.map(ProductDTO::toDTODisplayScreen));
    }


    public Specification<Product> getProductSpecification(FilterCriteria criteria) {
        return Specification
                .where(ProductSpecification.hasCategory(criteria.getCategory()))
                .and(ProductSpecification.hasBrand(criteria.getBrand()))
                .and(ProductSpecification.hasColor(criteria.getColor()))
                .and(ProductSpecification.hasKeyword(criteria.getKeyword()))
                .and(ProductSpecification.hasPriceRange(criteria.getMinPrice(),criteria.getMaxPrice()))
                .and(ProductSpecification.hasSizes(criteria.getSize()));
    }

    private Pageable createPageable(SortType sortType, int page, int sizePerPage) {
        Sort sort = (sortType != null) ? switch (sortType) {
            case Newest -> Sort.by(Sort.Direction.DESC, "createdAt");
            case PriceDESC -> Sort.by(Sort.Direction.DESC, "price");
            case PriceASC -> Sort.by(Sort.Direction.ASC, "price");
        } : Sort.unsorted();

        int offSet = page*sizePerPage;



        return PageRequest.of(page, sizePerPage,sort);
    }



    public ResponseEntity<?> showHomePageChange(Long id, ProductDTO productDTO) {
        var product = productRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Product not found")
        );
        product.setShowHomepage(productDTO.getShowHomepage());

        productRepository.save(product);


        return  ResponseEntity.ok("Updated");



    }

    public ResponseEntity<?> getShowHomepage() {
        List<Product> products = productRepository.findByShowHomepageTrue();

        List<ProductDTO> productDTOS = products.stream().map(ProductDTO::toDTODisplayScreen).toList();

        return  ResponseEntity.ok(productDTOS);
    }

    public ResponseEntity<?> delete(Long id) {

        var existing = productRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Not Found")
        );
        productRepository.delete(existing);

        return  ResponseEntity.status(HttpStatus.ACCEPTED).body("Deleted");
    }

    public ResponseEntity<?> getBySearch(String name) {
        Specification<Product> spec = ProductSpecification.hasKeyword(name);

        List<ProductDTO> products = productRepository.findAll(spec)
                .stream().map(ProductDTO::toDTO).toList();


        return  ResponseEntity.ok(products);


    }
}
