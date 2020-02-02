package controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import main.App;
import model.Organization;
import model.User;
import model.User.Role;
import services.UserService;
import spark.Request;
import spark.Response;
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

    
	public class BillDates{
		public String startDate;
		public String endDate;
	}


    private static boolean verifyUserRole(Request req, String forbiddenRole){
		if(req.session(false).attribute("user_role").equals(forbiddenRole)){
            return false;
		}
		return true;
	}

    public static Route getOrganizations = (Request req, Response res) -> {
        res.type("application/json");
        User user = UserService.getUser(req.session(false).attribute("email"));
        if(user.getRole() == Role.ADMIN){
            Set<Organization> result = new HashSet<Organization>();
            result.add(user.getOrganization());
            return App.g.toJson(result);
        }
        else{
            return App.g.toJson(App.orgService.getOrganizations());
        }
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
    	if(!verifyUserRole(req, "USER")){
            res.status(403);
            return "Forbidden";
        }

        OrganizationToAdd org;
        try {	
        	org = App.g.fromJson(req.body(), OrganizationToAdd.class);
        }catch(Exception e) {
        	res.status(400);
        	return "Los format zahteva";
        }
        if(org.name == null || org.name.equals("")) {
        	res.status(400);
        	return "Ime je obavezno polje";
        }
        
        res.type("application/json");

        if(App.orgService.addOrganization(org)){
            res.status(200);
            return true;
        }
        res.status(400);

        return false; 
    };
	public static Route getBills = (Request req, Response res) -> {
		BillDates bd = App.g.fromJson(req.body(), BillDates.class);
		User user = UserService.getUser(req.session(false).attribute("email"));
		return App.g.toJson(App.orgService.getBills(user.getOrganization(), bd));
	};
   public static Route updateOrganization = (Request req, Response res)->{
        if(!verifyUserRole(req, "USER")){
            res.status(403);
            return "Forbidden";
        }
	   OrganizationToUpdate updateOrg;
       try {
    	   updateOrg = App.g.fromJson(req.body(), OrganizationToUpdate.class);
       }catch(Exception e) {
    	   res.status(400);
    	   return "Nevalidan format zahteva";
       }
       if(updateOrg.newName == null || updateOrg.newName.equals("")) {
          	res.status(400);
          	return "Ime je obavezno polje";
          }
              
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
       if(!verifyUserRole(req, "USER")){
            res.status(403);
            return "Forbidden";
        }
	   OrganizationToDelete org;
	   try {
	   		org = App.g.fromJson(req.body(), OrganizationToDelete.class);
	   }catch(Exception e) {
		   res.status(400);
		   return "Nevalidan format zahteva";
	   }
	   if(org.name == null || org.name.equals("")) {
       	res.status(400);
       	return "Ime je obavezno polje";
       }
	   
	   res.type("application/json");
	   if(App.orgService.deleteOrganization(org.name)) {
		   res.status(200);
		   return true;
	   }
	   res.status(400);
	   return false;
   };
  
}
