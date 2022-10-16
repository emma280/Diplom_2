import Utills.UserGenerator;
import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.User;
import model.UserCredentials;
import org.junit.Before;
import org.junit.Test;
import Utills.UserGenerator;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest {
    UserClient userClient;
    User user;
    UserCredentials userCredentials;

    int statusCode;
    String messageText;


    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandom();
        userCredentials = new UserCredentials(user.getEmail(), user.getPassword());
    }

    @Test
    @DisplayName("Создание уникального пользователя с заполнением всех полей верными значениями")
    public void userCreationWithValidCredentials() {
        ValidatableResponse createResponse = userClient.createUser(user);
        statusCode = createResponse.extract().statusCode();
        assertThat("User isn't created", statusCode, equalTo(SC_OK));
    }

    @Test
    @DisplayName("Создание пользователя с существующими значениями")
    public void userCreationWithExistedData() {
        userClient.createUser(user);
        User existedUser = new User(user.getEmail(), user.getPassword(), user.getName());
        ValidatableResponse createResponse = userClient.createUser(existedUser);
        statusCode = createResponse.extract().statusCode();
        messageText = createResponse.extract().path("message");
        assertThat("User isn't created", messageText, equalTo("User already exists"));
        assertThat("User isn't created", statusCode, equalTo(SC_FORBIDDEN));

    }


    @Test
    @DisplayName("Создание пользователя с пустым значением поля \"email\"")
    public void userCreationWithEmptyEmailField() {
        user.setEmail("");
        ValidatableResponse createResponse = userClient.createUser(user);
        statusCode = createResponse.extract().statusCode();
        messageText = createResponse.extract().path("message");
        assertThat("User isn't created", messageText, equalTo("Email, password and name are required fields"));
        assertThat("User isn't created", statusCode, equalTo(SC_FORBIDDEN));
    }


    @Test
    @DisplayName("Создание пользователя с пустым значением поля \"password\"")
    public void userCreationWithEmptyPasswordField() {
        user.setPassword("");
        ValidatableResponse createResponse = userClient.createUser(user);
        statusCode = createResponse.extract().statusCode();
        messageText = createResponse.extract().path("message");
        assertThat("User isn't created", messageText, equalTo("Email, password and name are required fields"));
        assertThat("User isn't created", statusCode, equalTo(SC_FORBIDDEN));
    }

}