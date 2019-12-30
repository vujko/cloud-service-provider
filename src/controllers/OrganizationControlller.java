package controllers;

import spark.Request;
import spark.Response;
import main.App;
import model.Organization;
import spark.Route;

public class OrganizationControlller {

    public static Route getOrganizations = (Request req, Response res) -> {
        res.type("application/json");
        return App.g.toJson(App.orgService.getOrganizations());
    };

    public static Route addOrganization = (Request req, Response res) -> {
        Organization org = App.g.fromJson(req.body(), Organization.class);
        res.type("application/json");

        if(App.orgService.addOrganization(org)){
            res.status(200);
            return "OK";
        }
        res.status(400);

        return "Organization with that name already exsists"; 
    };

}
