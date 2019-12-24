package main;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

import spark.Request;
import spark.Response;
import spark.Route;

import java.io.File;
import java.io.IOException;

import com.google.gson.Gson;

import controllers.LoginController;
import services.UserService;
import controllers.UserController;


public class App {
	
	public static UserService userService;
	public static Gson g = new Gson();
	
	public static void main(String[] args) throws IOException {
		userService = new UserService();

		port(8080);		
		staticFiles.externalLocation(new File("./WebContent").getCanonicalPath()); 
		
		get("/homepage",LoginController.loadLoginPage);
		post("/login", LoginController.handleLogin);
		get("/getUsers", UserController.getUsers);
	}	
}
