package controllers;

import spark.Request;
import spark.Response;
import spark.Route;
import main.App;


public class LoginController {

    private class Input{
        public String email;
        public String password;
    }

    public static Route verifyLogin = (Request req, Response res) ->{
        res.type("application/json");
        Input in = getInputFromReq(req);
        if(UserController.verify(in.email, in.password)){
            
            return App.g.toJson(true);
        }
        return App.g.toJson(false);
    };

	
	public static Route loadLoginPage = (Request req, Response res) ->{
			
        if (req.session().attribute("user") == null){
            res.redirect("/");
            return null;
        }
        res.redirect("/success");
        return null;
    };

    public static Route handleLogin = (Request req, Response res) ->{
        Input in = getInputFromReq(req);
        if(UserController.verify(in.email, in.password)){
            req.session().attribute("user", in.email);
            res.redirect("/success");
            return null;
        }
        res.redirect("/");
        //displayMessage("invalid email or password.Please try again");
        
        return "Invalid email or password";

    };

    private static Input getInputFromReq(Request req){
        String payload = req.body();
        return App.g.fromJson(payload, Input.class);

    }
}
