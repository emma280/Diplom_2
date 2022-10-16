import Utills.UserGenerator;
import client.UserClient;
import client.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import Utills.UserGenerator;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CreateOrderTest {
    OrderClient orderClient;
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
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void orderCreationWithUserAuthorization() {
        userClient.createUser(user);
        ValidatableResponse loginResponse = userClient.loginUser(userCredentials);
        String token = loginResponse.extract().path("accessToken");
        UserToken userToken = new UserToken(token);
        List<String> ingredients = List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa70");
        Order order = new Order(ingredients);
        ValidatableResponse createResponse = orderClient.createOrder(userToken, order);
        statusCode = createResponse.extract().statusCode();
        assertThat("Order isn't created", statusCode, equalTo(SC_OK));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверяется отсутствие номера пустого заказа в списке созданных заказов")
    public void orderCreationWithoutUserAuthorization() {
        List<String> ingredients = List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa70");
        Order order = new Order(ingredients);
        ValidatableResponse createResponse = orderClient.createOrder(order);
        List<Integer> orderNumbers = orderClient.getOrders().extract().response().path("orders.number");
        Integer orderNumber = createResponse.extract().response().path("order.number");
        Assert.assertFalse(orderNumbers.contains(orderNumber));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void orderCreationWithoutIngredients() {
        userClient.createUser(user);
        ValidatableResponse loginResponse = userClient.loginUser(userCredentials);
        String token = loginResponse.extract().path("accessToken");
        UserToken userToken = new UserToken(token);
        ValidatableResponse createResponse = orderClient.createOrder(userToken);
        statusCode = createResponse.extract().statusCode();
        messageText = createResponse.extract().path("message");
        assertThat(statusCode, equalTo(SC_BAD_REQUEST));
        assertThat(messageText, equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хэшем ингредиентов")
    public void orderCreationWithInvalidIngredientsHash() {
        userClient.createUser(user);
        ValidatableResponse loginResponse = userClient.loginUser(userCredentials);
        String token = loginResponse.extract().path("accessToken");
        UserToken userToken = new UserToken(token);
        List<String> ingredientHashes = List.of("4321", "1234");
        Order order = new Order(ingredientHashes);
        ValidatableResponse createResponse = orderClient.createOrder(userToken, order);
        statusCode = createResponse.extract().statusCode();
        assertThat(statusCode, equalTo(SC_INTERNAL_SERVER_ERROR));
    }
}

