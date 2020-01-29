package controllers;

import spark.Request;
import spark.Response;

import java.util.ArrayList;

import main.App;
import model.Organization;
import spark.Route;

public class OrganizationControlller {

    public class OrganizationToAdd{
        public String name;
        public String description;
        public String logo;
        public ArrayList<String> disks;
    }
    public class OrganizationToUpdate{
        public String oldName;
        public String newName;
        public String description;
        public String logo;
        public ArrayList<String> disks;
    }
    public class OrganizationToDelete{
    	public String name;
    }

    public static Route getOrganizations = (Request req, Response res) -> {
        res.type("application/json");
        return App.g.toJson(App.orgService.getOrganizations());
    };

    public static Route getSelectedMachines = (Request req, Response res) -> {
        res.type("application/json");
        return App.g.toJson(App.orgService.getSelectedMachines(req.params("orgName")));
    };

    public static Route getSelectedDisks = (Request req, Response res) -> {
        res.type("application/json");
        return App.g.toJson(App.orgService.getSelectedDisks(req.params("orgName")));
    };

    public static Route getDrivesWithoutVM = (Request req, Response res) ->{
        res.type("application/json");
        return App.g.toJson(App.orgService.getDrivesWithoutVM(req.params("orgName")));
    };

    public static Route getUsersDrivesWithoutVM = (Request req, Response res) ->{
        res.type("application/json");
        return App.g.toJson(App.orgService.getUsersDrivesWithoutVM(req.session(false).attribute("email")));
    };

    public static Route addOrganization = (Request req, Response res) -> {
        OrganizationToAdd org = App.g.fromJson(req.body(), OrganizationToAdd.class);
        res.type("application/json");

        if(App.orgService.addOrganization(org)){
            res.status(200);
            return true;
        }
        res.status(400);

        return false; 
    };

   public static Route updateOrganization = (Request req, Response res)->{
       OrganizationToUpdate updateOrg = App.g.fromJson(req.body(), OrganizationToUpdate.class);
       res.type("application/json");
       Organization newOrg = new Organization();
       newOrg.setName(updateOrg.newName);
       newOrg.setDescription(updateOrg.description);
       newOrg.setLogo(updateOrg.logo);
       if(App.orgService.updateOrganization(updateOrg)){
           res.status(200);
           return true;
       }
       res.status(400);
       return false;


   };

   public static Route deleteOrganization = (Request req, Response res)->{
	   OrganizationToDelete org = App.g.fromJson(req.body(), OrganizationToDelete.class);
	   res.type("application/json");
	   if(App.orgService.deleteOrganization(org.name)) {
		   res.status(200);
		   return true;
	   }
	   res.status(400);
	   return false;
   };
}
