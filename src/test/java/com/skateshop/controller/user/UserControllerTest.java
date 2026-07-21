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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = RestAssuredConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest extends IntegrationTestConfig {

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
        RestAssured.requestSpecification = requestSpecification;

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
                .whenIgnoringPaths("[*].createAt")
                .isEqualTo(expectedResponse);
    }

}