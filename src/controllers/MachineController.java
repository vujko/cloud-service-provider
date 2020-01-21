package controllers;

import main.App;
import services.MachineService;
import spark.Request;
import spark.Response;
import spark.Route;

public class MachineController {

	public static Route getMachines = (Request request, Response response) ->{
		response.type("application/json");
		return App.g.toJson(MachineService.getMachines());
	};
}
