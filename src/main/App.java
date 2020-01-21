package main;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

import java.io.File;
import java.io.IOException;

import com.google.gson.Gson;

import controllers.DriveController;
import controllers.LoginController;
import controllers.MachineController;
import controllers.OrganizationControlller;
import controllers.UserController;
import services.DriveService;
import services.OrganizationService;
import services.UserService;



public class App {
	public static OrganizationService orgService;
	public static UserService userService;
	public static DriveService driveService;
	public static Gson g = new Gson();
	
	public static void main(String[] args) throws IOException {
		orgService = new OrganizationService();
		userService = new UserService();
		
		driveService = new DriveService();
		
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

		get("/getDrives", DriveController.getDrives);
		get("/getVirtual", MachineController.getMachines);

		get("/getOrganizations", OrganizationControlller.getOrganizations);
		post("/addOrganization", OrganizationControlller.addOrganization);
		post("/updateOrganization", OrganizationControlller.updateOrganization);
		post("/deleteOrganization", OrganizationControlller.deleteOrganization);
		
	}	
}
