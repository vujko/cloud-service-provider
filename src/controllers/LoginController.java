package controllers;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import main.App;
import model.User;


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
			
        if (req.session().attribute("user_role") == null){
            res.redirect("/");
            return null;
        }
        res.redirect("/success");
        return null;
    };

    public static Route handleLogin = (Request req, Response res) ->{
        Input in = getInputFromReq(req);
        if(UserController.verify(in.email, in.password)){
            User.Role role = getUserRole(in.email);
            req.session().attribute("user_role", role.name());
            req.session().attribute("email", in.email);
            res.redirect("/success");
            return null;
        }
        res.redirect("/");        
        return "Invalid email or password";

    };

    public static Route ensureLogin = (Request req, Response res) ->{
        Session s = req.session(false);
        if(s == null){
            return false;
        }
        if(req.session(false).attribute("user_role") == null){
            res.status(200);
            return false;
        }
        res.status(200);
        return true;
    };

    public static Route handleLogout = (Request req, Response res) ->{
        req.session().attribute("user_role", null);
        req.session().attribute("email", null);
        return "200 OK";
        
    };

    private static Input getInputFromReq(Request req){
        String payload = req.body();
        return App.g.fromJson(payload, Input.class);

    }

    private static User.Role getUserRole(String email){
        for (User u : App.userService.getUsers()) {
            if(u.getEmail().equalsIgnoreCase(email)){
                return u.getRole();
            }
        }
        return null;
    }
}
