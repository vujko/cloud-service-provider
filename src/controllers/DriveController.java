package controllers;

import java.util.ArrayList;
import java.util.Set;

import main.App;
import model.Drive;
import model.Drive.DriveType;
import services.DriveService;
import spark.Request;
import spark.Response;
import spark.Route;

public class DriveController {
	
	public class DriveToAdd{
		public String name;
		public DriveType type;
		public int capacity;
		public String vm;
		public String organization;

	}
	public class Update{
		public String oldName;
		public String newName;
		public DriveType type;
		public int capacity;
		public String vm;
	}
	public class Delete{
		public String name;
	}
	public static Route getDrives = (Request request, Response response) -> {
		response.type("application/json");
		return App.g.toJson(App.driveService.getDrives(request.params("email")));
	};
	
	public static Route addDrive = (Request request, Response response)->{
		response.type("application/json");
		DriveToAdd drive;
		try {
			drive = App.g.fromJson(request.body(), DriveToAdd.class);
		}catch(Exception e) {
			response.status(400);
			return "Nevalidan zahtev";
		}
		String validation = validate(drive);
		if(validation != null) {
			response.status(400);
			return validation;
		}
		
		String userEmail = request.session(false).attribute("email");
		Drive d = App.driveService.addDrive(userEmail,drive);
		if(d != null) {
			response.status(200);
			
			return App.g.toJson(d);
		}
		response.status(400);
		return App.g.toJson(null);
	};
	public static Route deleteDrive = (Request request, Response res)->{
		Delete name;
		try {
			name = App.g.fromJson(request.body(), Delete.class);
		}catch(Exception e) {
			res.status(400);
			return "Nevalidan zahtev";
		}
		res.type("application/json");
		if(App.driveService.deleteDrive(name.name)) {
			res.status(200);
			return true;
		}
		res.status(400);
		return false;
	};
	public static Route getAvilableDisks = (Request req, Response res) -> {
		res.type("application/json");
		return App.g.toJson(App.driveService.getAvilableDrives());
	};
	public static Route updateDrive = (Request req, Response res)->{
		res.type("application/json");
		Update update;
		try {
			update = App.g.fromJson(req.body(), Update.class);
		}catch(Exception e) {
			res.status(400);
			return "Nevalidan zahtev";
		}
		String validation = validateUpdate(update);
		if(validation != null) {
			res.status(400);
			return validation;
		}
		
		if(App.driveService.updateDrive(update)) {
			res.status(200);
			return true;
		}
		res.status(400);
		return false;
	};
	
	public static Route filterCapacity = (Request req, Response res)->{
		res.type("application/json");
		//ArrayList<Boolean> checked = (ArrayList<Boolean>)App.g.fromJson(req.body(),new TypeToken<ArrayList<Boolean>>(){}.getType());
		String email = req.session(false).attribute("email");
		Filter checked = App.g.fromJson(req.body(),Filter.class);
		Set<Drive> filtered = DriveService.filterCapacity(checked,email);
		if(filtered.size() != 0) {
			res.status(200);
			return App.g.toJson(filtered);
		}
		res.status(400);
		return App.g.toJson(filtered);
	};
	
	public class Filter{
		public String searchArg;
		public int capFrom;
		public int capTo;
		public ArrayList<String> type;
	}
	public static String validate(DriveToAdd drive) {
		
		if(drive.name == null) return "Ime je obavezno polje";
		if(drive.name.equals("")) return "Ime je obavezno polje";
		
		if(drive.type == null) return "Tip je obavezno polje";
		if(drive.type.equals("")) return "Tip je obavezno polje";
		
		if(drive.capacity == 0) return "Kapacitet je obavezno polje";
		
		if(drive.organization == null) return "Organizacija je obavezno polje";
		if(drive.organization.equals("")) return "Organizacije je obaveznno polje";
		
		return null;
	}
	public static String validateUpdate(Update update) {
		
		if(update.newName == null) return "Ime je obavezno polje";
		if(update.newName.equals("")) return "Ime je obavezno polje";
		
		if(update.type == null) return "Tip je obavezno polje";
		if(update.type.equals("")) return "Tip je obavezno polje";
		
		if(update.capacity == 0) return "Kapacitet je obavezno polje";
		
		return null;
		
	}
}

