package Utills;

import model.User;
import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerator {
    public static User getRandom() {
        String email = RandomStringUtils.randomAlphanumeric(1, 50)+"@"+RandomStringUtils.randomAlphabetic(1, 10)+"."+RandomStringUtils.randomAlphabetic(2, 5);
        String password = RandomStringUtils.randomAlphanumeric(8, 20);
        String name = RandomStringUtils.randomAlphabetic(1, 50);
        return new User(email, password, name);
    }
}