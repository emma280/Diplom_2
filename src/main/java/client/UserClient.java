package client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.RefreshUserToken;
import model.User;
import model.UserCredentials;
import model.UserToken;

import static io.restassured.RestAssured.given;

public class UserClient extends StellarBurgersRestClient {
    private static final String REGISTRATION_PATH = "api/auth/register";
    private static final String LOGIN_PATH = "api/auth/login";
    private static final String LOGOUT_PATH = "api/auth/logout";
    private static final String USER_PATH = "api/auth/user";


    @Step("Создание пользователя")
    public ValidatableResponse createUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(REGISTRATION_PATH)
                .then().log().body();
    }

    @Step("Логин пользователя")
    public ValidatableResponse loginUser(UserCredentials userCredentials) {
        return given()
                .spec(getBaseSpec())
                .body(userCredentials)
                .when()
                .post(LOGIN_PATH)
                .then().log().body();

    }

    @Step("Изменение данных пользователя")
    public ValidatableResponse updateUser(UserToken userToken, User user) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", userToken.getToken())
                .body(user)
                .when()
                .patch(USER_PATH)
                .then();
    }


    @Step("Попытка изменения данных пользователя без авторизации")
    public ValidatableResponse updateUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .patch(USER_PATH)
                .then().log().body();
    }


    @Step("Выход пользователя из системы")
    public ValidatableResponse logoutUser(UserToken userToken, RefreshUserToken refreshUserToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", userToken.getToken())
                .body(refreshUserToken)
                .when()
                .post(LOGOUT_PATH)
                .then().log().body();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(UserToken userToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", userToken.getToken())
                .when()
                .delete(USER_PATH)
                .then().log().body();
    }

}
