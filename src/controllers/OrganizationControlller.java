package controllers;

import spark.Request;
import spark.Response;
import main.App;
import spark.Route;

public class OrganizationControlller {

    public static Route getOrganizations = (Request req, Response res) -> {
        res.type("application/json");
        return App.g.toJson(App.orgService.getOrganizations());
    };

}
