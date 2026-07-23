package com.skateshop.controller.user;

import com.skateshop.commons.FileUtils;
import com.skateshop.config.IntegrationTestConfig;
import com.skateshop.config.RestAssuredConfig;
import com.skateshop.dto.response.CepGetResponse;
import com.skateshop.service.BrasilApiService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = RestAssuredConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AddressControllerIT extends IntegrationTestConfig {

    private static final String URL = "v1/address";

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    @Qualifier(value = "requestSpecification")
    private RequestSpecification requestSpecification;

    @MockitoBean
    private BrasilApiService brasilApiService;

    @BeforeEach
    void setUrl() {
        RestAssured.requestSpecification = requestSpecification;
    }

    @Test
    @DisplayName("GET v1/address returns a list with all address when param is null")
    @Sql(value = "/sql/address/init_one_address.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/address/clear_address.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(1)
    void findAll_returnsAllUsers_whenSuccessful() {
        var expectedResponse = fileUtils.readResourceFile("address/get-address-null-param-200.json");

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("GET v1/address?id= returns an address when param in not null and when address exist")
    @Sql(value = "/sql/address/init_one_address.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/address/clear_address.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(2)
    void findById_returnsAnUser_whenSuccessful() {
        RestAssured.requestSpecification = requestSpecification;

        var expectedResponse = fileUtils.readResourceFile("address/get-address-by-id-200.json");

        var id = UUID.fromString("9f1a7b0c-1234-4d89-9e01-abcdef123456");

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .queryParam("id", id)
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("GET v1/address?id= returns Not Found when address not exist")
    @Sql(value = "/sql/address/init_one_address.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/address/clear_address.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(3)
    void findById_returnsNotFound_whenUserNotExist() {
        RestAssured.requestSpecification = requestSpecification;

        var id = UUID.fromString("dd26aa46-10da-4dd5-afc8-5755d7a82063");

        var expectedResponse = fileUtils.readResourceFile("address/get-address-by-id-404.json");

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .queryParam("id", id)
                .get(URL)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("GET v1/address/user/userId returns an address when exists")
    @Sql(value = "/sql/address/init_one_address.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/address/clear_address.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(4)
    void findByCpf_returnsAnUser_whenSuccessful() {
        var expectedResponse = fileUtils.readResourceFile("address/get-address-by-userId-200.json");

        var userId = "11111111-1111-1111-1111-111111111111";

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .pathParam("userId", userId)
                .get(URL + "/user/{userId}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("POST v1/address create an address")
    @Sql(value = "/sql/user/init_one_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/address/clear_address.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(5)
    void save_createAnUser_whenSuccessful() {
        var request = fileUtils.readResourceFile("address/pots-request-200.json");
        var expectedResponse = fileUtils.readResourceFile("address/pots-response-201.json");

        var userId = "11111111-1111-1111-1111-111111111111";

        Mockito.when(brasilApiService.findCep("70800-000"))
                .thenReturn(new CepGetResponse("70800-000", "DF", "Brasília", "Centro", "Rua das Oliveiras"));

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .body(request)
                .pathParam("userId", userId)
                .post(URL + "/{userId}")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("id")
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("DELETE v1/address/id delete an address")
    @Sql(value = "/sql/address/init_one_address.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/address/clear_address.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(7)
    void delete_deleteAnUser_whenSuccessful() {

        var id = UUID.fromString("9f1a7b0c-1234-4d89-9e01-abcdef123456");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .pathParam("id", id)
                .delete(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();
    }

    @Test
    @DisplayName("DELETE v1/address/id returnNotFound when address not found")
    @Sql(value = "/sql/address/init_one_address.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/address/clear_address.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(8)
    void delete_returnsNotFound_whenUserNotExist() {

        var id = UUID.fromString("9f1a7b0c-1234-4d89-9e01-abcdef123451");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .pathParam("id", id)
                .delete(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .log().all();
    }
    @Test
    @DisplayName("PUT v1/address update an address")
    @Sql(value = "/sql/address/init_one_address.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/address/clear_address.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(9)
    void update_updatesAnAddress_whenSuccessful() {

        var request = fileUtils.readResourceFile("address/put-request-200.json");
        var expectedResponse = fileUtils.readResourceFile("address/put-response-201.json");

        var id = UUID.fromString("9f1a7b0c-1234-4d89-9e01-abcdef123456");

        Mockito.when(brasilApiService.findCep("70800-000"))
                .thenReturn(new CepGetResponse("70800-000", "DF", "Brasília", "Centro", "Rua das Oliveiras"));

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .body(request)
                .pathParam("id", id)
                .put(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("id")
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("PATCH v1/address update selected fields of an address")
    @Sql(value = "/sql/address/init_one_address.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/address/clear_address.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(10)
    void updateSelectedFields_updatesAnAddress_whenSuccessful() {

        var request = fileUtils.readResourceFile("address/patch-request-200.json");
        var expectedResponse = fileUtils.readResourceFile("address/patch-response-201.json");

        var id = UUID.fromString("9f1a7b0c-1234-4d89-9e01-abcdef123456");

        Mockito.when(brasilApiService.findCep("70800-000"))
                .thenReturn(new CepGetResponse("70800-000", "DF", "Brasília", "Centro", "Rua das Oliveiras"));

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .body(request)
                .pathParam("id", id)
                .patch(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("id")
                .isEqualTo(expectedResponse);
    }


}