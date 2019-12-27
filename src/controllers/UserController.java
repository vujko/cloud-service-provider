package controllers;
import model.User;
import services.UserService;

public class UserController {
    public static boolean verify(String email, String password){
        //korisnik ne moze da prosledi null.Ovo pokriva 1. slucaj ucitavanja stranice
        // if(email == null){
        //     return true;
        // }
        User user = UserService.getUser(email, password);
        return user == null ? false : true;
    }
}
