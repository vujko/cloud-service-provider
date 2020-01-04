package controllers;

import spark.Request;
import spark.Response;
import main.App;
import model.Organization;
import spark.Route;

public class OrganizationControlller {

    public class OrganizationToUpdate{
        public String oldName;
        public String newName;
        public String description;
        public String logo;
    }
    public class OrganizationToDelete{
    	public String name;
    }

    public static Route getOrganizations = (Request req, Response res) -> {
        res.type("application/json");
        return App.g.toJson(App.orgService.getOrganizations());
    };

    public static Route addOrganization = (Request req, Response res) -> {
        Organization org = App.g.fromJson(req.body(), Organization.class);
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
       if(App.orgService.updateOrganization(updateOrg.oldName, newOrg)){
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
