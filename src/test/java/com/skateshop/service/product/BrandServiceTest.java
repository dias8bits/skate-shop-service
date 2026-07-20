package com.skateshop.service.product;

import com.skateshop.commons.BrandUtils;
import com.skateshop.domain.product.Brand;
import com.skateshop.repository.product.BrandRepository;
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
class BrandServiceTest {

    @InjectMocks
    private BrandService brandService;

    @InjectMocks
    private BrandUtils brandUtils;

    @Mock
    private BrandRepository brandRepository;

    private List<Brand> brandList;

    @BeforeEach
    void init() {
        brandList = brandUtils.getBrandList();
    }

    @Test
    @DisplayName("findAll returns a list with all brands when param is null")
    @Order(1)
    void findAll_returnsAllBrands_whenSuccessful() {
        BDDMockito.when(brandRepository.findAll()).thenReturn(brandList);

        var brands = brandService.findAll(null);

        Assertions.assertThat(brands).isNotNull().hasSameElementsAs(brandList);
    }

    @Test
    @DisplayName("findAll returns a list with a single brand when param is not null")
    @Order(2)
    void findAll_returnsSingleBrand_whenIdIsNotNull() {
        var brand = brandList.getFirst();

        BDDMockito.when(brandRepository.findBrandByBrandNameIgnoreCase(brand.getBrandName())).thenReturn(Optional.of(brand));

        var brands = brandService.findAll(brand.getBrandName());

        Assertions.assertThat(brands).isNotNull().containsExactly(brand);
    }

    @Test
    @DisplayName("findByName returns a brands when exists")
    @Order(3)
    void findByName_returnsABrand_whenSuccessful() {
        var brandToFound = brandList.getFirst();

        BDDMockito.when(brandRepository.findBrandByBrandNameIgnoreCase(brandToFound.getBrandName())).thenReturn(Optional.of(brandToFound));

        var brand = brandService.findBrandByNameOrElseThrow(brandToFound.getBrandName());

        Assertions.assertThat(brandToFound).isNotNull().isEqualTo(brand);
    }

    @Test
    @DisplayName("findByName returns NotFound when brand not exists")
    @Order(4)
    void findByName_returnsNotFound_whenBrandNotExists() {
        var brandToFound = brandList.getFirst();

        BDDMockito.when(brandRepository.findBrandByBrandNameIgnoreCase(brandToFound.getBrandName())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> brandService.findBrandByNameOrElseThrow(brandToFound.getBrandName()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("findById returns a brands when exists")
    @Order(5)
    void findById_returnsABrand_whenSuccessful() {
        var brandToFound = brandList.getFirst();

        BDDMockito.when(brandRepository.findById(brandToFound.getId())).thenReturn(Optional.of(brandToFound));

        var brand = brandService.findBrandByIdOrElseThrow(brandToFound.getId());

        Assertions.assertThat(brandToFound).isNotNull().isEqualTo(brand);
    }

    @Test
    @DisplayName("findById returns NotFound when brand not exists")
    @Order(6)
    void findById_returnsNotFound_whenBrandNotExists() {
        var brandToFound = brandList.getFirst();

        BDDMockito.when(brandRepository.findById(brandToFound.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> brandService.findBrandByIdOrElseThrow(brandToFound.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save create a brand")
    @Order(6)
    void save_createABrand_whenSuccessful() {
        var brand = brandUtils.newBrand();

        BDDMockito.when(brandRepository.save(brand)).thenReturn(brand);

        var brandSaved = brandService.save(brand);

        Assertions.assertThat(brandSaved).isEqualTo(brand).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("save returns business Exception")
    @Order(7)
    void save_returnsBusinessException_whenBrandAlreadyExists () {
        var brandToSave = brandUtils.newBrand();
        var brandSaved = brandList.getFirst();
        brandToSave.setBrandName(brandSaved.getBrandName());

        BDDMockito.when(brandRepository.findBrandByBrandNameIgnoreCase(brandToSave.getBrandName()))
                .thenReturn(Optional.of(brandSaved));

        Assertions.assertThatException()
                .isThrownBy(() -> brandService.save(brandToSave))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("delete delete a brand")
    @Order(8)
    void delete_deleteABrand_whenSuccessful() {
        var brand = brandList.getFirst();

        BDDMockito.when(brandRepository.findBrandByBrandNameIgnoreCase(brand.getBrandName()))
                .thenReturn(Optional.of(brand));
        BDDMockito.doNothing().when(brandRepository).delete(brand);

        Assertions.assertThatNoException().isThrownBy(() -> brandService.delete(brand));
    }

    @Test
    @DisplayName("delete returns NotFound")
    @Order(9)
    void delete_returnsNotFound_whenBrandNotExists() {
        var brand = brandList.getFirst();

        BDDMockito.when(brandRepository.findBrandByBrandNameIgnoreCase(brand.getBrandName()))
                .thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> brandService.delete(brand))
                .isInstanceOf(ResponseStatusException.class);
    }

}