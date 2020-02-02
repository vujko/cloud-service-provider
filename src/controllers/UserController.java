package controllers;

import main.App;
import model.User;
import services.UserService;
import spark.Request;
import spark.Route;
import spark.Response;

public class UserController {
	
	private class UserToUpdate{
		private String oldEmail;
		private String newEmail;
		private String surname;
		private String name;
		private String pass;
	}
	private class UserEmail{
		private String email;
	}
	
    public static boolean verify(String email, String password){
        User user = UserService.getUser(email, password);
        return user == null ? false : true;
	}
	
	private static boolean verifyUserRole(Request req, String forbiddenRole){
		if(req.session(false).attribute("user_role").equals(forbiddenRole)){
            return false;
		}
		return true;
	}
    public static Route getUsersSuper = (Request req, Response res) ->{
		res.type("aplication/json");
		if(!req.session(false).attribute("user_role").equals("SUPER_ADMIN")){
            res.status(403);
            return "Forbidden";
        }
		String email = req.session(false).attribute("email");
		return App.g.toJson(App.userService.getUsersSuper(email));
    };
    public static Route getUsersAdmin = (Request req, Response res) ->{
		res.type("aplication/json");
		if(!verifyUserRole(req, "USER")){
            res.status(403);
            return "Forbidden";
        }
		String email = req.session(false).attribute("email");
		return App.g.toJson(App.userService.getUsersAdmin(email));
    };
	
	public static Route getUser = (Request req, Response res) ->{
		res.type("application/json");
		return App.g.toJson(UserService.getUser(req.params("email")));
	};


    public static Route addUser = (Request req, Response res) ->{
    	if(!verifyUserRole(req, "USER")){
            res.status(403);
            return "Forbidden";
        }

		User user;
    	try {
    		user = App.g.fromJson(req.body(), User.class);
    	}
        catch(Exception e) {
        	res.status(400);
        	return "Los format zahteva";
        }
    	String validation = validate(user);
    	if(validation != null) {
    		res.status(400);
    		return validation;
    	}
    			
        res.type("aplication/json");
        String email = req.session(false).attribute("email");
        //User.Role role = (Role)req.session(false).attribute("user_role");
        if(App.userService.addUser(user,email)){
            res.status(200);
            return App.g.toJson(user);
        }
        res.status(400);
        return false;
    };
    public static Route checkEmail = (Request req, Response res) ->{
    	UserEmail us = App.g.fromJson(req.body(), UserEmail.class);
    	if(App.userService.userExsists(us.email)) {
    		res.status(200);
    		return false;
    	}
    	res.status(200);
    	return true;
    };
    public static Route updateUser = (Request req, Response res) ->{
    	UserToUpdate user;
    	try {
    		user = App.g.fromJson(req.body(),UserToUpdate.class);
    	}
    	catch(Exception e) {
    		res.status(400);
        	return "Los format zahteva";
    	}
    	
    	res.type("aplication/json");
    	User newUser = new User();
    	newUser.setEmail(user.newEmail);
    	newUser.setName(user.name);
    	newUser.setSurname(user.surname);
    	newUser.setPassword(user.pass);
    	
    	if(App.userService.updateUser(newUser, user.oldEmail)) {
    		res.status(200);
    		return true;
    	}
    	res.status(400);
        return false;
    };
    public static Route deleteUser = (Request req, Response res) ->{
    	if(!verifyUserRole(req, "USER")){
            res.status(403);
            return "Forbidden";
        }
		UserEmail user;
    	try {
    		user = App.g.fromJson(req.body(),UserEmail.class);
    	}
    	catch(Exception e) {	
    		res.status(400);
        	return "Los format zahteva";
    	}
		
    	res.type("aplication/json");
    	if(req.session(false).attribute("email").equals(user.email)) {
    		res.status(400);
        	return "Pogresan email";
    	}
    	if(App.userService.deleteUser(user.email)) {
    		res.status(200);
    		return true;
    	}
    	
    	return false;	
    };
    public static String validate(User user) {
    	
    	if(user.getEmail() == null)
    		return "Email je obavezno polje";
    	if(user.getEmail().equals("")) return "Email je obavezno polje";
    	
    	if(user.getName() == null)
    		return "Ime je obavezno polje";
    	if(user.getName().equals("")) return "Ime je obavezno polje";
    	
    	if(user.getSurname() == null)
    		return "Prezime je obavezno polje";
    	if(user.getSurname().equals("")) return "Prezime je obavezno polje";
    	
    	if(user.getOrganization() == null)
    		return "Organizacija je obavezno polje";

    	if(user.getPassword() == null) return "Lozinka je obavezno polje";
    	if(user.getPassword().equals("")) return "Lozinka je obavezno polje";
    	
    	if(user.getRole() == null) return "Uloga je obavezno polje";
    	if(!(user.getRole().equals(User.Role.ADMIN) || user.getRole().equals(User.Role.USER))) return "Pogresna uloga.";
    	
    	return null;
    }
}
