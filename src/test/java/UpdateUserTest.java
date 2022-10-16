import Utills.UserGenerator;
import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.User;
import model.UserCredentials;
import model.UserToken;
import org.junit.Before;
import org.junit.Test;
import Utills.UserGenerator;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class UpdateUserTest {
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
    @DisplayName("Изменение данных пользователя с авторизацией")
    public void userDataUpdatingWithAuthorization() {
        userClient.createUser(user);
        ValidatableResponse loginResponse = userClient.loginUser(userCredentials);
        String token = loginResponse.extract().path("accessToken");
        UserToken userToken = new UserToken(token);
        User updatedUser = UserGenerator.getRandom();
        ValidatableResponse updateResponse = userClient.updateUser(userToken, updatedUser);
        statusCode = updateResponse.extract().statusCode();
        assertThat(statusCode, equalTo(SC_OK));
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void userDataUpdatingWithoutAuthorization() {
        User updatedUser = UserGenerator.getRandom();
        ValidatableResponse updateResponse = userClient.updateUser(updatedUser);
        statusCode = updateResponse.extract().statusCode();
        assertThat(statusCode, equalTo(SC_UNAUTHORIZED));
    }
}