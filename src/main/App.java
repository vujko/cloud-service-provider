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
import controllers.DriveController;
import controllers.LoginController;
import controllers.MachineController;
import controllers.OrganizationControlller;
import controllers.ReferenceController;
import controllers.UserController;
import services.CategoryService;
import services.DriveService;
import services.MachineService;
import services.OrganizationService;
import services.UserService;



public class App {
	public static OrganizationService orgService;
	public static UserService userService;
	public static CategoryService categoryService;
	public static MachineService machineService;
	public static DriveService driveService;
	public static Gson g = new Gson();
	
	public static void main(String[] args) throws IOException {
		orgService = new OrganizationService();
		userService = new UserService();
		categoryService = new CategoryService();
		machineService = new MachineService();
		driveService = new DriveService();
		
		ReferenceController.setUpReferences();
		(OrganizationService.getOrganization("Organizacija1")).addVirtaulMachine(MachineService.getMachine("Virtualna1"));
	
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


		get("/getDrives/:email", DriveController.getDrives);
		post("/addDrive", DriveController.addDrive);
		post("/deleteDrive",DriveController.deleteDrive);
		post("/updateDrive",DriveController.updateDrive);
		post("/searchDrives",DriveController.searchDrive);
		post("/driveFilterCapacity",DriveController.filterCapacity);
		get("/getAvilableDisks", DriveController.getAvilableDisks);

		get("/getOrganizations", OrganizationControlller.getOrganizations);
		post("/addOrganization", OrganizationControlller.addOrganization);
		post("/updateOrganization", OrganizationControlller.updateOrganization);
		post("/deleteOrganization", OrganizationControlller.deleteOrganization);
		get("/getOrgDrives/:orgName", OrganizationControlller.getSelectedDisks);
		get("/getDrivesWithoutVM/:orgName", OrganizationControlller.getDrivesWithoutVM);
		get("/getUsersDrivesWithoutVM", OrganizationControlller.getUsersDrivesWithoutVM);

		get("/getCategories", CategoryController.getCategories);
		post("/addCategory", CategoryController.addCategory);
		post("/updateCategory", CategoryController.updateCategory);
		post("/deleteCategory", CategoryController.deleteCategory);

		get("/getMachines/:email", MachineController.getMachines);
		get("/getAvilableMachines", MachineController.getAvilableMachines);
		post("/addVM", MachineController.addMachine);
		get("/getSelectedDisks/:machineName", MachineController.getSelectedDisks);
		post("/updateMachine", MachineController.updateMachine);

		
		get("/getSelectedMachines/:orgName", OrganizationControlller.getSelectedMachines);
		post("/searchVM", MachineController.search);
		post("/VMfilter",MachineController.filter);
	}	
}
