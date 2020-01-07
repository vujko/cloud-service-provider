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

    static class LoggInfo{
        public boolean isLoggedIn = false;
        public String role = "";
        public String email = "";
    }
    public static Route verifyLogin = (Request req, Response res) ->{
        res.type("application/json");
        Input in = getInputFromReq(req);
        if(UserController.verify(in.email, in.password)){
            
            return App.g.toJson(true);
        }
        return App.g.toJson(false);
    };

    public static Route handleLogin = (Request req, Response res) ->{
        Input in = getInputFromReq(req);
        if(UserController.verify(in.email, in.password)){
            User.Role role = getUserRole(in.email);
            req.session().attribute("user_role", role.name());
            req.session().attribute("email", in.email);
            res.status(200);
            return "OK";
        }

        res.status(400);
        return "Invalid email or password";

    };

    public static Route updateLoggedUser = (Request req, Response res) ->{
        req.session().attribute("email", req.body());
        res.status(200);
        return "OK";
    };

    public static Route ensureLogin = (Request req, Response res) ->{
        Session s = req.session(false);
        LoggInfo li = new LoggInfo();
        if(s == null){
            return App.g.toJson(li);
        }
        if(req.session(false).attribute("user_role") == null){
            res.status(200);
            return  App.g.toJson(li);
        }
        res.status(200);
        li.isLoggedIn = true;
        li.role = req.session(false).attribute("user_role");
        li.email = req.session(false).attribute("email");
        return App.g.toJson(li);
    };

    public static Route handleLogout = (Request req, Response res) ->{
        req.session().attribute("user_role", null);
        req.session().attribute("email", null);
        return "200 OK";
        
    };

    public static Route getRole = (Request req, Response res) ->{
        res.type("application/json");
        return req.session(false).attribute("user_role");
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
