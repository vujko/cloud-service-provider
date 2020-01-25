package controllers;

import java.util.Set;


import main.App;
import model.Drive;
import model.Drive.DriveType;
import spark.Request;
import spark.Response;
import spark.Route;

public class DriveController {
	
	private class Adding{
		private String name;
		private DriveType type;
		private int capacity;
		private String vm;
	}
	public class Update{
		public String oldName;
		public String newName;
		public DriveType type;
		public int capacity;
		public String vm;
	}
	
	public static Route getDrives = (Request request, Response response) -> {
		response.type("application/json");
		return App.g.toJson(App.driveService.getDrives(request.params("email")));
	};
	
	public static Route addDrive = (Request request, Response response)->{
		response.type("application/json");
		Adding drive = App.g.fromJson(request.body(), Adding.class);
		String userEmail = request.session(false).attribute("email");
		Drive d = App.driveService.addDrive(userEmail,drive.name,drive.type,drive.capacity,drive.vm);
		if(d != null) {
			response.status(200);
			
			return App.g.toJson(d);
		}
		response.status(400);
		return App.g.toJson(null);
	};
	public static Route deleteDrive = (Request request, Response res)->{
		String name = App.g.fromJson(request.body(), String.class);
		res.type("application/json");
		if(App.driveService.deleteDrive(name)) {
			res.status(200);
			return true;
		}
		res.status(400);
		return false;
	};
	public static Route updateDrive = (Request req, Response res)->{
		res.type("application/json");
		Update update = App.g.fromJson(req.body(), Update.class);
		if(App.driveService.updateDrive(update)) {
			res.status(200);
			return true;
		}
		res.status(400);
		return false;
	};
	public static Route searchDrive = (Request req, Response res)->{
		res.type("application/json");
		String argument = App.g.fromJson(req.body(), String.class);
		Set<Drive> searched = App.driveService.search(argument);
		if(searched.size() != 0) {
			res.status(200);
			return App.g.toJson(searched);
		}
		res.status(400);
		return App.g.toJson(searched);
	};
	public static Route filterCapacity = (Request req, Response res)->{
		res.type("application/json");
		//ArrayList<Boolean> checked = (ArrayList<Boolean>)App.g.fromJson(req.body(),new TypeToken<ArrayList<Boolean>>(){}.getType());
		Boolean[] checked = App.g.fromJson(req.body(),Boolean[].class);
		Set<Drive> filtered = App.driveService.filterCapacity(checked);
		if(filtered.size() != 0) {
			res.status(200);
			return App.g.toJson(filtered);
		}
		res.status(400);
		return App.g.toJson(filtered);
	};
	
}

