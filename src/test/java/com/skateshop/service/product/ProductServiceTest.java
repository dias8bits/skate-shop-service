package com.skateshop.service.product;

import com.skateshop.commons.*;
import com.skateshop.domain.product.Product;
import com.skateshop.repository.product.ProductRepository;
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
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Spy
    private BrandUtils brandUtils;

    @Spy
    private CategoryUtils categoryUtils;

    @Spy
    private SizeUtils sizeUtils = new SizeUtils(new CategoryUtils());

    @Spy
    private ColorUtils colorUtils;

    @Spy
    private ProductUtils productUtils = new ProductUtils(new BrandUtils(), new CategoryUtils(), new SizeUtils(new CategoryUtils()), new ColorUtils());

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BrandService brandService;

    @Mock
    private ColorService colorService;

    @Mock
    private SizeService sizeService;

    @Mock
    private CategoryService categoryService;

    private List<Product> productList;

    @BeforeEach
    void init() {
        productList = productUtils.getProductList();
    }

    @Test
    @DisplayName("findAll returns a list with all products when param is null")
    @Order(1)
    void findAll_returnsAllProducts_whenSuccessful() {
        BDDMockito.when(productRepository.findAll()).thenReturn(productList);

        var products = productService.findAll(null);

        Assertions.assertThat(products).isNotNull().hasSameElementsAs(productList);
    }

    @Test
    @DisplayName("findAll returns a list with a single product when param is not null")
    @Order(2)
    void findAll_returnsSingleProduct_whenIdIsNotNull() {
        var product = productList.getFirst();

        BDDMockito.when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        var products = productService.findAll(product.getId());

        Assertions.assertThat(products).isNotNull().containsExactly(product);
    }

    @Test
    @DisplayName("findById returns a product if exists")
    @Order(3)
    void findById_returnsAProduct_whenExists() {
        var product = productList.getFirst();

        BDDMockito.when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        var productFound = productService.findProductByIdOrElseThrow(product.getId());

        Assertions.assertThat(productFound).isEqualTo(product);
    }

    @Test
    @DisplayName("findById returns exception if product is not found")
    @Order(4)
    void findById_returnsNotFound_whenProductNotFound() {
        var product = productList.getFirst();

        BDDMockito.when(productRepository.findById(product.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> productService.findProductByIdOrElseThrow(product.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save create a product")
    @Order(5)
    void save_createAProduct_whenSuccessful() {
        var product = productUtils.newProduct();
        var size = product.getSize();
        var brand = product.getBrand();
        var color = product.getColor();
        var category = product.getCategory();

        BDDMockito.when(sizeService.findSizeByIdOrElseThrow(size.getId())).thenReturn(size);
        BDDMockito.when(brandService.findBrandByIdOrElseThrow(brand.getId())).thenReturn(brand);
        BDDMockito.when(colorService.findColorByIdOrElseThrow(color.getId())).thenReturn(color);
        BDDMockito.when(categoryService.findCategoryByIdOrElseThrow(category.getId())).thenReturn(category);
        BDDMockito.when(productRepository.save(product)).thenReturn(product);

        var productSaved = productService.save(product, size.getId(), brand.getId(), color.getId(), category.getId());

        Assertions.assertThat(productSaved).isEqualTo(product).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("update updates a product when exists")
    @Order(6)
    void update_updatesAProduct_whenSuccessful() {
        var productToUpdate = productList.getFirst();
        var newData = productUtils.newProduct();
        var size = newData.getSize();
        var brand = newData.getBrand();
        var color = newData.getColor();
        var category = newData.getCategory();

        BDDMockito.when(productRepository.findById(productToUpdate.getId())).thenReturn(Optional.of(productToUpdate));
        BDDMockito.when(sizeService.findSizeByIdOrElseThrow(size.getId())).thenReturn(size);
        BDDMockito.when(brandService.findBrandByIdOrElseThrow(brand.getId())).thenReturn(brand);
        BDDMockito.when(colorService.findColorByIdOrElseThrow(color.getId())).thenReturn(color);
        BDDMockito.when(categoryService.findCategoryByIdOrElseThrow(category.getId())).thenReturn(category);
        BDDMockito.when(productRepository.save(productToUpdate)).thenReturn(productToUpdate);

        var productUpdated = productService.update(
                newData, productToUpdate.getId(), size.getId(), brand.getId(), color.getId(), category.getId());

        Assertions.assertThat(productUpdated).hasNoNullFieldsOrProperties();
        Assertions.assertThat(productUpdated.getName()).isEqualTo(newData.getName());
        Assertions.assertThat(productUpdated.getDescription()).isEqualTo(newData.getDescription());
        Assertions.assertThat(productUpdated.getPrice()).isEqualTo(newData.getPrice());
        Assertions.assertThat(productUpdated.getSize()).isEqualTo(size);
        Assertions.assertThat(productUpdated.getBrand()).isEqualTo(brand);
        Assertions.assertThat(productUpdated.getColor()).isEqualTo(color);
        Assertions.assertThat(productUpdated.getCategory()).isEqualTo(category);
    }

    @Test
    @DisplayName("updateSelectedFields updates provided fields when address exists")
    @Order(7)
    void updateSelectedFields_updatesProvidedFields_whenSuccessful() {
        var productToUpdate = productList.getFirst();
        var request = productUtils.newProduct();

        BDDMockito.when(productRepository.findById(productToUpdate.getId())).thenReturn(Optional.of(productToUpdate));
        BDDMockito.when(productRepository.save(productToUpdate)).thenReturn(productToUpdate);

        var productUpdated = productService.updateSelectedFields(request, productToUpdate.getId());

        Assertions.assertThat(productUpdated).hasNoNullFieldsOrProperties();
        Assertions.assertThat(productUpdated.getName()).isEqualTo(request.getName());
        Assertions.assertThat(productUpdated.getPrice()).isEqualTo(request.getPrice());
        Assertions.assertThat(productUpdated.getDescription()).isEqualTo(request.getDescription());
        Assertions.assertThat(productUpdated.getSize()).isEqualTo(request.getSize());
        Assertions.assertThat(productUpdated.getBrand()).isEqualTo(request.getBrand());
        Assertions.assertThat(productUpdated.getColor()).isEqualTo(request.getColor());
        Assertions.assertThat(productUpdated.getCategory()).isEqualTo(request.getCategory());
    }

    @Test
    @DisplayName("updateSelectedFields keeps original fields when request fields are null")
    @Order(8)
    void updateSelectedFields_keepsOriginalFields_whenRequestFieldsAreNull() {
        var productToUpdate = productList.getFirst();
        var originalName = productToUpdate.getName();
        var originalPrice = productToUpdate.getPrice();
        var originalDescription = productToUpdate.getDescription();
        var originalSize = productToUpdate.getSize();
        var originalBrand = productToUpdate.getBrand();
        var originalColor = productToUpdate.getColor();
        var originalCategory = productToUpdate.getCategory();

        var request = new Product();

        BDDMockito.when(productRepository.findById(productToUpdate.getId())).thenReturn(Optional.of(productToUpdate));
        BDDMockito.when(productRepository.save(productToUpdate)).thenReturn(productToUpdate);

        var productUpdated = productService.updateSelectedFields(request, productToUpdate.getId());

        Assertions.assertThat(productUpdated.getName()).isEqualTo(originalName);
        Assertions.assertThat(productUpdated.getPrice()).isEqualTo(originalPrice);
        Assertions.assertThat(productUpdated.getDescription()).isEqualTo(originalDescription);
        Assertions.assertThat(productUpdated.getSize()).isEqualTo(originalSize);
        Assertions.assertThat(productUpdated.getBrand()).isEqualTo(originalBrand);
        Assertions.assertThat(productUpdated.getColor()).isEqualTo(originalColor);
        Assertions.assertThat(productUpdated.getCategory()).isEqualTo(originalCategory);
    }

    @Test
    @DisplayName("delete delete a product when exists")
    @Order(9)
    void delete_deleteAProduct_whenExists() {
        var product = productList.getFirst();

        BDDMockito.when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        BDDMockito.doNothing().when(productRepository).delete(product);

        Assertions.assertThatNoException().isThrownBy(() -> productService.delete(product));
    }

    @Test
    @DisplayName("delete returns exception if product is not found")
    @Order(10)
    void delete_returnsNotFound_whenProductNotFound() {
        var product = productList.getFirst();

        BDDMockito.when(productRepository.findById(product.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> productService.delete(product))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("findByName returns a product if exists")
    @Order(11)
    void findByName_returnsAProduct_whenExists() {
        var product = productList.getFirst();

        BDDMockito.when(productRepository.findProductByNameIgnoreCase(product.getName())).thenReturn(Optional.of(product));

        var productFound = productService.findProductByNameOrElseThrow(product.getName());

        Assertions.assertThat(productFound).isEqualTo(product);
    }

    @Test
    @DisplayName("findByName returns exception if product is not found")
    @Order(12)
    void findByName_returnsNotFound_whenProductNotFound() {
        var product = productList.getFirst();

        BDDMockito.when(productRepository.findProductByNameIgnoreCase(product.getName())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> productService.findProductByNameOrElseThrow(product.getName()))
                .isInstanceOf(ResponseStatusException.class);
    }

}