package controllers;
import model.User;
import services.UserService;

public class UserController {
    public static boolean verify(String email, String password){

        User user = UserService.getUser(email, password);
        return user == null ? false : true;
    }
}
