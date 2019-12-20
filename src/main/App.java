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


public class App {
	
	
	
	public static void main(String[] args) throws IOException {
		port(8080);		
		staticFiles.externalLocation(new File("./WebContent").getCanonicalPath()); 
		
		get("/homepage",LoginController.loadLoginPage);
		post("/login", LoginController.handleLogin);

	}



	
}
