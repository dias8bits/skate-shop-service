package com.skateshop.service.product;

import com.skateshop.commons.ColorUtils;
import com.skateshop.domain.product.Color;
import com.skateshop.repository.product.ColorRepository;
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
class ColorServiceTest {

    @InjectMocks
    private ColorService colorService;

    @InjectMocks
    private ColorUtils colorUtils;

    @Mock
    private ColorRepository colorRepository;

    private List<Color> colorList;

    @BeforeEach
    void init() {
        colorList = colorUtils.getColorList();
    }

    @Test
    @DisplayName("findAll returns a list with all colors when param is null")
    @Order(1)
    void findAll_returnsAllCategories_whenSuccessful() {
        BDDMockito.when(colorRepository.findAll()).thenReturn(colorList);

        var colors = colorService.findAll(null);

        Assertions.assertThat(colors).isNotNull().hasSameElementsAs(colorList);
    }

    @Test
    @DisplayName("findAll returns a list with a single color when param is not null")
    @Order(2)
    void findAll_returnsSingleColor_whenIdIsNotNull() {
        var color = colorList.getFirst();

        BDDMockito.when(colorRepository.findColorByColorNameIgnoreCase(color.getColorName())).thenReturn(Optional.of(color));

        var colors = colorService.findAll(color.getColorName());

        Assertions.assertThat(colors).isNotNull().containsExactly(color);
    }

    @Test
    @DisplayName("findByName returns a colors when exists")
    @Order(3)
    void findByName_returnsAColor_whenSuccessful() {
        var colorToFound = colorList.getFirst();

        BDDMockito.when(colorRepository.findColorByColorNameIgnoreCase(colorToFound.getColorName())).thenReturn(Optional.of(colorToFound));

        var color = colorService.findColorByNameOrElseThrow(colorToFound.getColorName());

        Assertions.assertThat(colorToFound).isNotNull().isEqualTo(color);
    }

    @Test
    @DisplayName("findByName returns NotFound when color not exists")
    @Order(4)
    void findByName_returnsNotFound_whenColorNotExists() {
        var colorToFound = colorList.getFirst();

        BDDMockito.when(colorRepository.findColorByColorNameIgnoreCase(colorToFound.getColorName())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> colorService.findColorByNameOrElseThrow(colorToFound.getColorName()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("findById returns a colors when exists")
    @Order(5)
    void findById_returnsAColor_whenSuccessful() {
        var colorToFound = colorList.getFirst();

        BDDMockito.when(colorRepository.findById(colorToFound.getId())).thenReturn(Optional.of(colorToFound));

        var color = colorService.findColorByIdOrElseThrow(colorToFound.getId());

        Assertions.assertThat(colorToFound).isNotNull().isEqualTo(color);
    }

    @Test
    @DisplayName("findById returns NotFound when color not exists")
    @Order(6)
    void findById_returnsNotFound_whenColorNotExists() {
        var colorToFound = colorList.getFirst();

        BDDMockito.when(colorRepository.findById(colorToFound.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> colorService.findColorByIdOrElseThrow(colorToFound.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save create a color")
    @Order(6)
    void save_createAColor_whenSuccessful() {
        var color = colorUtils.newColor();

        BDDMockito.when(colorRepository.save(color)).thenReturn(color);

        var colorSaved = colorService.save(color);

        Assertions.assertThat(colorSaved).isEqualTo(color).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("save returns business Exception")
    @Order(7)
    void save_returnsBusinessException_whenColorAlreadyExists () {
        var colorToSave = colorUtils.newColor();
        var colorSaved = colorList.getFirst();
        colorToSave.setColorName(colorSaved.getColorName());

        BDDMockito.when(colorRepository.findColorByColorNameIgnoreCase(colorToSave.getColorName()))
                .thenReturn(Optional.of(colorSaved));

        Assertions.assertThatException()
                .isThrownBy(() -> colorService.save(colorToSave))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("delete delete a color")
    @Order(8)
    void delete_deleteAColor_whenSuccessful() {
        var color = colorList.getFirst();

        BDDMockito.when(colorRepository.findColorByColorNameIgnoreCase(color.getColorName()))
                .thenReturn(Optional.of(color));
        BDDMockito.doNothing().when(colorRepository).delete(color);

        Assertions.assertThatNoException().isThrownBy(() -> colorService.delete(color));
    }

    @Test
    @DisplayName("delete returns NotFound")
    @Order(9)
    void delete_returnsNotFound_whenColorNotExists() {
        var color = colorList.getFirst();

        BDDMockito.when(colorRepository.findColorByColorNameIgnoreCase(color.getColorName()))
                .thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> colorService.delete(color))
                .isInstanceOf(ResponseStatusException.class);
    }

}