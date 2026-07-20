package com.skateshop.service.product;

import com.skateshop.commons.CategoryUtils;
import com.skateshop.domain.product.Category;
import com.skateshop.repository.product.CategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @InjectMocks
    private CategoryUtils categoryUtils;

    @Mock
    private CategoryRepository categoryRepository;

    private List<Category> categoryList;

    @BeforeEach
    void init() {
        categoryList = categoryUtils.getCategoryList();
    }

    @Test
    @DisplayName("findAll returns a list with all categories when param is null")
    @Order(1)
    void findAll_returnsAllCategories_whenSuccessful() {
        BDDMockito.when(categoryRepository.findAll()).thenReturn(categoryList);

        var categories = categoryService.findAll(null);

        Assertions.assertThat(categories).isNotNull().hasSameElementsAs(categoryList);
    }

    @Test
    @DisplayName("findAll returns a list with a single category when param is not null")
    @Order(2)
    void findAll_returnsSingleCategory_whenIdIsNotNull() {
        var category = categoryList.getFirst();

        BDDMockito.when(categoryRepository.findCategoryByCategoryNameIgnoreCase(category.getCategoryName())).thenReturn(Optional.of(category));

        var categories = categoryService.findAll(category.getCategoryName());

        Assertions.assertThat(categories).isNotNull().containsExactly(category);
    }

    @Test
    @DisplayName("findByName returns a categories when exists")
    @Order(3)
    void findByName_returnsACategory_whenSuccessful() {
        var categoryToFound = categoryList.getFirst();

        BDDMockito.when(categoryRepository.findCategoryByCategoryNameIgnoreCase(categoryToFound.getCategoryName())).thenReturn(Optional.of(categoryToFound));

        var category = categoryService.findCategoryByNameOrElseThrow(categoryToFound.getCategoryName());

        Assertions.assertThat(categoryToFound).isNotNull().isEqualTo(category);
    }

    @Test
    @DisplayName("findByName returns NotFound when category not exists")
    @Order(4)
    void findByName_returnsNotFound_whenCategoryNotExists() {
        var categoryToFound = categoryList.getFirst();

        BDDMockito.when(categoryRepository.findCategoryByCategoryNameIgnoreCase(categoryToFound.getCategoryName())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> categoryService.findCategoryByNameOrElseThrow(categoryToFound.getCategoryName()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("findById returns a categories when exists")
    @Order(5)
    void findById_returnsACategory_whenSuccessful() {
        var categoryToFound = categoryList.getFirst();

        BDDMockito.when(categoryRepository.findById(categoryToFound.getId())).thenReturn(Optional.of(categoryToFound));

        var category = categoryService.findCategoryByIdOrElseThrow(categoryToFound.getId());

        Assertions.assertThat(categoryToFound).isNotNull().isEqualTo(category);
    }

    @Test
    @DisplayName("findById returns NotFound when category not exists")
    @Order(6)
    void findById_returnsNotFound_whenCategoryNotExists() {
        var categoryToFound = categoryList.getFirst();

        BDDMockito.when(categoryRepository.findById(categoryToFound.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> categoryService.findCategoryByIdOrElseThrow(categoryToFound.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save create a category")
    @Order(6)
    void save_createACategory_whenSuccessful() {
        var category = categoryUtils.newCategory();

        BDDMockito.when(categoryRepository.save(category)).thenReturn(category);

        var categorySaved = categoryService.save(category);

        Assertions.assertThat(categorySaved).isEqualTo(category).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("save returns business Exception")
    @Order(7)
    void save_returnsBusinessException_whenCategoryAlreadyExists () {
        var categoryToSave = categoryUtils.newCategory();
        var categorySaved = categoryList.getFirst();
        categoryToSave.setCategoryName(categorySaved.getCategoryName());

        BDDMockito.when(categoryRepository.findCategoryByCategoryNameIgnoreCase(categoryToSave.getCategoryName()))
                .thenReturn(Optional.of(categorySaved));

        Assertions.assertThatException()
                .isThrownBy(() -> categoryService.save(categoryToSave))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("delete delete a category")
    @Order(8)
    void delete_deleteACategory_whenSuccessful() {
        var category = categoryList.getFirst();

        BDDMockito.when(categoryRepository.findCategoryByCategoryNameIgnoreCase(category.getCategoryName()))
                .thenReturn(Optional.of(category));
        BDDMockito.doNothing().when(categoryRepository).delete(category);

        Assertions.assertThatNoException().isThrownBy(() -> categoryService.delete(category));
    }

    @Test
    @DisplayName("delete returns NotFound")
    @Order(9)
    void delete_returnsNotFound_whenCategoryNotExists() {
        var category = categoryList.getFirst();

        BDDMockito.when(categoryRepository.findCategoryByCategoryNameIgnoreCase(category.getCategoryName()))
                .thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> categoryService.delete(category))
                .isInstanceOf(ResponseStatusException.class);
    }

}