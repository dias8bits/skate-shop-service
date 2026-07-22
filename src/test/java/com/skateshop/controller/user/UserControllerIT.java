package com.skateshop.controller.user;

import com.skateshop.commons.FileUtils;
import com.skateshop.commons.UserUtils;
import com.skateshop.config.IntegrationTestConfig;
import com.skateshop.config.RestAssuredConfig;
import com.skateshop.repository.user.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = RestAssuredConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerIT extends IntegrationTestConfig {

    private static final String URL = "v1/users";

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    @Qualifier(value = "requestSpecification")
    private RequestSpecification requestSpecification;

    @BeforeEach
    void setUrl() {
        RestAssured.requestSpecification = requestSpecification;
    }

    @Test
    @DisplayName("GET v1/users returns a list with all users when param is null")
    @Sql(value = "/sql/user/init_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clear_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(1)
    void findAll_returnsAllUsers_whenSuccessful() {
        var expectedResponse = fileUtils.readResourceFile("user/get-user-null-param-200.json");

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("[*].createdAt")
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("GET v1/users?id= returns an user when param in not null and when user exist")
    @Sql(value = "/sql/user/init_one_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clear_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(2)
    void findById_returnsAnUser_whenSuccessful() {
        RestAssured.requestSpecification = requestSpecification;

        var expectedResponse = fileUtils.readResourceFile("user/get-user-by-id-200.json");

        var id = UUID.fromString("11111111-1111-1111-1111-111111111111");

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
                .whenIgnoringPaths("[*].createdAt")
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("GET v1/users?id= returns Not Found when user not exist")
    @Sql(value = "/sql/user/init_one_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clear_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(3)
    void findById_returnsNotFound_whenUserNotExist() {
        RestAssured.requestSpecification = requestSpecification;

        var id = UUID.fromString("dd26aa46-10da-4dd5-afc8-5755d7a82063");

        var expectedResponse = fileUtils.readResourceFile("user/get-user-by-id-404.json");

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
                .whenIgnoringPaths("[*].createdAt")
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("GET v1/users/cpf returns an user when exists")
    @Sql(value = "/sql/user/init_one_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clear_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(4)
    void findByCpf_returnsAnUser_whenSuccessful() {
        var expectedResponse = fileUtils.readResourceFile("user/get-user-by-cpf-200.json");

        var cpf = "11144477735";

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .pathParam("cpf", cpf)
                .get(URL + "/{cpf}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("[*].createdAt")
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("GET v1/users/cpf returns not found when user not exist")
    @Sql(value = "/sql/user/init_one_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clear_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(5)
    void findByCpf_returnsNotFound_whenUserNotExist() {
        var expectedResponse = fileUtils.readResourceFile("user/get-user-by-cpf-404.json");

        var cpf = "11144477736";

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .pathParam("cpf", cpf)
                .get(URL + "/{cpf}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("[*].createdAt")
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("POST v1/users create an user")
    @Sql(value = "/sql/user/clear_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(6)
    void save_createAnUser_whenSuccessful() {
        var request = fileUtils.readResourceFile("user/pots-request-200.json");
        var expectedResponse = fileUtils.readResourceFile("user/pots-response-201.json");

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .body(request)
                .post(URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("createdAt", "id")
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("DELETE v1/users/id delete an user")
    @Sql(value = "/sql/user/init_one_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clear_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(7)
    void delete_deleteAnUser_whenSuccessful() {

        var id = UUID.fromString("11111111-1111-1111-1111-111111111111");

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
    @DisplayName("DELETE v1/users/id returnNotFound when user not found")
    @Sql(value = "/sql/user/init_one_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clear_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(8)
    void delete_returnsNotFound_whenUserNotExist() {

        var id = UUID.fromString("11111111-1111-1111-1111-11111111110");

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
    @DisplayName("PUT v1/users upload an user")
    @Sql(value = "/sql/user/init_one_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clear_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(9)
    void update_returnsAnUser_whenUserNotExist() {

        var request = fileUtils.readResourceFile("user/put-request-200.json");
        var expectedResponse = fileUtils.readResourceFile("user/put-response-201.json");

        var id = UUID.fromString("11111111-1111-1111-1111-111111111111");

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
                .whenIgnoringPaths("createdAt", "id")
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("PATCH v1/users upload an user")
    @Sql(value = "/sql/user/init_one_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clear_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(10)
    void updateSelectedFields_returnsAnUser_whenUserNotExist() {

        var request = fileUtils.readResourceFile("user/patch-request-200.json");
        var expectedResponse = fileUtils.readResourceFile("user/patch-response-201.json");

        var id = UUID.fromString("11111111-1111-1111-1111-111111111111");

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
                .whenIgnoringPaths("createdAt", "id")
                .isEqualTo(expectedResponse);
    }


}