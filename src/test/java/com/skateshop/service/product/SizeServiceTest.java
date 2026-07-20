package com.skateshop.service.product;

import com.skateshop.commons.CategoryUtils;
import com.skateshop.commons.SizeUtils;
import com.skateshop.domain.product.Size;
import com.skateshop.exception.BusinessException;
import com.skateshop.repository.product.SizeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SizeServiceTest {

    @InjectMocks
    private SizeService sizeService;

    @InjectMocks
    private SizeUtils sizeUtils;

    @Mock
    private CategoryService categoryService;

    @Mock
    private SizeRepository sizeRepository;

    @Spy
    private CategoryUtils categoryUtils;

    private List<Size> sizeList;

    @BeforeEach
    void init() {
        sizeList = sizeUtils.getSizeList();
    }

    @Test
    @DisplayName("findAll returns a list with all sizes when param is null")
    @Order(1)
    void findAll_returnsAllCategories_whenSuccessful() {
        BDDMockito.when(sizeRepository.findAll()).thenReturn(sizeList);

        var sizes = sizeService.findAll(null);

        Assertions.assertThat(sizes).isNotNull().hasSameElementsAs(sizeList);
    }

    @Test
    @DisplayName("findAll returns a list with sizes by category when param is not null")
    @Order(2)
    void findAll_returnsSingleSize_whenIdIsNotNull() {
        var size = sizeList.getFirst();
        var category = categoryUtils.getCategoryList().getFirst();

        BDDMockito.when(sizeRepository.findAllByCategory_Id(category.getId())).thenReturn(List.of(size));

        var sizes = sizeService.findAll(category.getId());

        Assertions.assertThat(sizes).isNotNull().containsExactly(size);
    }

    @Test
    @DisplayName("findByName returns a sizes when exists")
    @Order(3)
    void findByName_returnsASize_whenSuccessful() {
        var sizeToFound = sizeList.getFirst();

        BDDMockito.when(sizeRepository.findSizeBySizeNameIgnoreCase(sizeToFound.getSizeName())).thenReturn(Optional.of(sizeToFound));

        var size = sizeService.findSizeByNameOrElseThrow(sizeToFound.getSizeName());

        Assertions.assertThat(sizeToFound).isNotNull().isEqualTo(size);
    }

    @Test
    @DisplayName("findByName returns NotFound when size not exists")
    @Order(4)
    void findByName_returnsNotFound_whenSizeNotExists() {
        var sizeToFound = sizeList.getFirst();

        BDDMockito.when(sizeRepository.findSizeBySizeNameIgnoreCase(sizeToFound.getSizeName())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> sizeService.findSizeByNameOrElseThrow(sizeToFound.getSizeName()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("findById returns a sizes when exists")
    @Order(5)
    void findById_returnsASize_whenSuccessful() {
        var sizeToFound = sizeList.getFirst();

        BDDMockito.when(sizeRepository.findById(sizeToFound.getId())).thenReturn(Optional.of(sizeToFound));

        var size = sizeService.findSizeByIdOrElseThrow(sizeToFound.getId());

        Assertions.assertThat(sizeToFound).isNotNull().isEqualTo(size);
    }

    @Test
    @DisplayName("findById returns NotFound when size not exists")
    @Order(6)
    void findById_returnsNotFound_whenSizeNotExists() {
        var sizeToFound = sizeList.getFirst();

        BDDMockito.when(sizeRepository.findById(sizeToFound.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> sizeService.findSizeByIdOrElseThrow(sizeToFound.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save create a size")
    @Order(6)
    void save_createASize_whenSuccessful() {
        var size = sizeUtils.newSize();
        var category = categoryUtils.getCategoryList().getFirst();

        BDDMockito.when(categoryService.findCategoryByNameOrElseThrow(category.getCategoryName())).thenReturn(category);
        BDDMockito.when(sizeRepository.save(size)).thenReturn(size);

        var sizeSaved = sizeService.save(size, category.getCategoryName());

        Assertions.assertThat(sizeSaved).isEqualTo(size).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("save returns business Exception")
    @Order(7)
    void save_returnsBusinessException_whenSizeAlreadyExists() {
        var sizeToSave = sizeUtils.newSize();
        var sizeSaved = sizeList.getFirst();
        var category = categoryUtils.getCategoryList().getFirst();
        sizeToSave.setSizeName(sizeSaved.getSizeName());

        BDDMockito.when(categoryService.findCategoryByNameOrElseThrow(category.getCategoryName())).thenReturn(category);
        BDDMockito.when(sizeRepository.existsByCategory_IdAndSizeNameIgnoreCase(category.getId(), sizeToSave.getSizeName()))
                .thenReturn(true);

        Assertions.assertThatException()
                .isThrownBy(() -> sizeService.save(sizeToSave, category.getCategoryName()))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("delete delete a size")
    @Order(8)
    void delete_deleteASize_whenSuccessful() {
        var size = sizeList.getFirst();

        BDDMockito.when(sizeRepository.findSizeBySizeNameIgnoreCase(size.getSizeName()))
                .thenReturn(Optional.of(size));
        BDDMockito.doNothing().when(sizeRepository).delete(size);

        Assertions.assertThatNoException().isThrownBy(() -> sizeService.delete(size));
    }

    @Test
    @DisplayName("delete returns NotFound")
    @Order(9)
    void delete_returnsNotFound_whenSizeNotExists() {
        var size = sizeList.getFirst();

        BDDMockito.when(sizeRepository.findSizeBySizeNameIgnoreCase(size.getSizeName()))
                .thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> sizeService.delete(size))
                .isInstanceOf(ResponseStatusException.class);
    }

}