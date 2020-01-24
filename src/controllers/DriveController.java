package controllers;

import main.App;
import spark.Request;
import spark.Response;
import spark.Route;

public class DriveController {

	
	public static Route getDrives = (Request request, Response response) -> {
		response.type("application/json");
		return App.g.toJson(App.driveService.getDrives(request.params("email")));
	};
	
}