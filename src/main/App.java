package main;

import static spark.Spark.*;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

import java.io.File;
import java.io.IOException;

import com.google.gson.Gson;

import controllers.CategoryController;
import controllers.LoginController;
import controllers.OrganizationControlller;
import services.*;
import controllers.UserController;



public class App {
	public static OrganizationService orgService;
	public static UserService userService;
	public static CategoryService categoryService;
	public static Gson g = new Gson();
	
	public static void main(String[] args) throws IOException {
		orgService = new OrganizationService();
		userService = new UserService();
		categoryService = new CategoryService();
		
		port(8080);		
		staticFiles.externalLocation(new File("./WebContent").getCanonicalPath()); 
		

		post("/login", LoginController.handleLogin);
		post("/verify", LoginController.verifyLogin);
		post("/logout", LoginController.handleLogout);
		get("/ensureLogin", LoginController.ensureLogin);
		get("/getRole", LoginController.getRole);
		post("/updateLoggedUser", LoginController.updateLoggedUser);

		get("/getUsers/SUPER_ADMIN", UserController.getUsersSuper);
		get("/getUsers/ADMIN", UserController.getUsersAdmin);
		get("/getUser/:email", UserController.getUser);
		post("/addUser",UserController.addUser);
		post("/updateUser", UserController.updateUser);
		post("/deleteUser", UserController.deleteUser);
		post("/checkEmail", UserController.checkEmail);

		get("/getOrganizations", OrganizationControlller.getOrganizations);
		post("/addOrganization", OrganizationControlller.addOrganization);
		post("/updateOrganization", OrganizationControlller.updateOrganization);
		post("/deleteOrganization", OrganizationControlller.deleteOrganization);

		get("/getCategories", CategoryController.getCategories);
		post("/addCategory", CategoryController.addCategory);
		post("/updateCategory", CategoryController.updateCategory);
		post("/deleteCategory", CategoryController.deleteCategory);
		
	}	
}
