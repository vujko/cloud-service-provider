package controllers;

import spark.Route;
import spark.Request;
import spark.Response;

public class LoginController {
	
	public static Route loadLoginPage = (Request req, Response res) ->{
			
        if (req.session().attribute("user") == null){
            res.redirect("login.html");
            return null;
        }
        res.redirect("/homepage");
        return null;
    };

    public static Route handleLogin = (Request req, Response res) ->{
        if(UserController.verify(req.queryParams("email"), req.queryParams("password"))){
            req.session().attribute("user", req.queryParams("email"));
            res.redirect("/");
        }
        
        return "Invalid email or password";

    };
}
