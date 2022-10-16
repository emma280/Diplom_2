import Utills.UserGenerator;
import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.User;
import model.UserCredentials;
import org.junit.Before;
import org.junit.Test;
import Utills.UserGenerator;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class LoginUserTest {
    UserClient userClient;
    User user;
    UserCredentials userCredentials;

    int statusCode;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandom();
        userCredentials = new UserCredentials(user.getEmail(), user.getPassword());
    }

    @Test
    @DisplayName("Авторизация пользователя")
    public void userAuthorization() {
        userClient.createUser(user);
        ValidatableResponse createResponse = userClient.loginUser(userCredentials);
        statusCode = createResponse.extract().statusCode();
        assertThat(statusCode, equalTo(SC_OK));
    }

    @Test
    @DisplayName("Авторизация пользователя с несуществующими данными")
    public void userAuthorizationWithInvalidData() {
        String nonExistedEmail = "qw6767e@lk.ru";
        String nonExistedPassword = "123ррр45";
        UserCredentials userCredentials = new UserCredentials(nonExistedEmail, nonExistedPassword);
        ValidatableResponse loginResponse = userClient.loginUser(userCredentials);
        statusCode = loginResponse.extract().statusCode();
        assertThat(statusCode, equalTo(SC_UNAUTHORIZED));
    }
}
