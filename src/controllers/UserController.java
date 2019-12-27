package controllers;

import main.App;
import model.User;
import services.UserService;
import spark.Request;
import spark.Route;
import spark.Response;

public class UserController {
    public static boolean verify(String email, String password){
        //korisnik ne moze da prosledi null.Ovo pokriva 1. slucaj ucitavanja stranice
        // if(email == null){
        //     return true;
        // }
        User user = UserService.getUser(email, password);
        return user == null ? false : true;
    }
    public static Route getUsers = (Request req, Response res) ->{
    	 res.type("aplication/json");
    	 return App.g.toJson(App.userService.getUsers());
    };
}
