package main;
import static spark.Spark.*;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;


import java.io.File;
import java.io.IOException;

import com.google.gson.Gson;

import controllers.LoginController;
import controllers.OrganizationControlller;
import services.*;
import controllers.UserController;



public class App {
	
	public static UserService userService;
	public static OrganizationService orgService;
	public static Gson g = new Gson();
	
	public static void main(String[] args) throws IOException {
		userService = new UserService();
		orgService = new OrganizationService();
		port(8080);		
		staticFiles.externalLocation(new File("./WebContent").getCanonicalPath()); 
		

		get("/login", LoginController.loadLoginPage);
		post("/login", LoginController.handleLogin);
		post("/verify", LoginController.verifyLogin);
		get("/success", (req, res) -> {
			res.redirect("/");
			return null;
		});

		get("/getUsers", UserController.getUsers);
		get("/getOrganizations", OrganizationControlller.getOrganizations);
		post("/logout", LoginController.handleLogout);
		post("/addOrganization", OrganizationControlller.addOrganization);
		post("/addUser",UserController.addUser);
		post("/updateOrganization", OrganizationControlller.updateOrganization);
		get("/ensureLogin", LoginController.ensureLogin);
		post("/updateUser", UserController.updateUser);
		post("/deleteUser", UserController.deleteUser);
	}	
}
