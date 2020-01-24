package controllers;

import main.App;
import spark.Request;
import spark.Response;
import spark.Route;

public class MachineController {

	public static Route getAllMachines = (Request request, Response response) ->{
		response.type("application/json");
		return App.g.toJson(App.machineService.getMachines());
	};

	public static Route getMachines = (Request req, Response res) ->{
		res.type("application/json");
		return App.g.toJson(App.machineService.getMachines(req.params("email")));
	};
}
