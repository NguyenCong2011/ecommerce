package dev.cong.v.springcomereme;


import dev.cong.v.springcomereme.dao.CategoryRepository;
import dev.cong.v.springcomereme.dto.CategoryDTO;
import dev.cong.v.springcomereme.entity.Category;
import dev.cong.v.springcomereme.exception.DuplicateException;

import dev.cong.v.springcomereme.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");
        category.setImage("image_url");
    }

    @Test
    void getAllCategories_Success() {
        categoryService.getAll();
        verify(categoryRepository).findAll();
    }

    @Test
    void createCategory_Success() {
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.empty());
        ResponseEntity<?> response = categoryService.createOne(category);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void createCategory_DuplicateName_ThrowsException() {
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(category));
        assertThatThrownBy(() -> categoryService.createOne(category))
                .isInstanceOf(DuplicateException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void updateCategory_Success() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.empty());
        ResponseEntity<?> response = categoryService.updateOne(1L, category);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void deleteCategory_Success() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        ResponseEntity<?> response = categoryService.deleteOne(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        verify(categoryRepository).delete(category);
    }

    @Test
    void getCategoryById_Success() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        ResponseEntity<?> response = categoryService.getOne(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isInstanceOf(CategoryDTO.class);
    }

    @Test
    void getCategoryById_NotFound_ThrowsEntityNotFoundException() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> categoryService.getOne(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Not Found category with id");
    }
}
