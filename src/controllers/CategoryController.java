package controllers;

import java.util.Locale.Category;

import main.App;
import model.CategoryVM;
import spark.Request;
import spark.Response;
import spark.Route;

public class CategoryController{

    public class CategoryToUpdate{
        public String oldName;
        public String newName;
        public int cores;
        public int ram;
        public int gpus;
    }
    public class Delete{
    	public String name;
    }
    public static Route getCategories = (Request req, Response res) ->{
        res.type("application/json");
        return App.g.toJson(App.categoryService.getCategories());
    };

    public static Route addCategory = (Request req, Response res) ->{
    	CategoryVM cat;
    	try {
    		cat = App.g.fromJson(req.body(), CategoryVM.class);
    	}catch(Exception e) {
    		res.status(400);
    		return "Nevalidan zahtev";
    	}
        res.type("application/json");

        String validation = validate(cat);
        if(validation != null)
        {
        	res.status(200);
        	return validation;
        }
        
        if(App.categoryService.addCategory(cat)){
            res.status(200);
            return true;
        }
        res.status(400);

        return false; 
    };

    public static Route updateCategory = (Request req, Response res)->{
    	CategoryToUpdate updateCat;
    	try {
    		updateCat = App.g.fromJson(req.body(), CategoryToUpdate.class);
    	}catch(Exception e) {
    		res.status(400);
    		return "Nevalidan zahtev";
    	}
        
    	String validation = validateUpdate(updateCat);
        if(validation != null)
        {
        	res.status(400);
        	return validation;
        }
    	
        res.type("application/json");
        CategoryVM newCat = new CategoryVM(updateCat.newName,updateCat.cores,updateCat.ram,updateCat.gpus);
        if(App.categoryService.updateCategory(updateCat.oldName, newCat)){
            res.status(200);
            return true;
        }
        res.status(400);
        return false;

    };

    public static Route deleteCategory = (Request req, Response res) ->{
        Delete catName = App.g.fromJson(req.body(), Delete.class);
        res.type("application/json");
        if(App.categoryService.deleteCategory(catName.name)){
            res.status(200);
            return true;
        }
        res.status(400);
        return false;
    };
    public static String validate(CategoryVM cat) {
    	
    	if(cat.getName() == null) return "Ime je obavezno polje";
    	if(cat.getName().equals("")) return "Ime je obavezno polje";
    	
    	if(cat.getCores() == 0) return "Jezgra su obavezno polje";
    	
    	if(cat.getRam() == 0) return "Ram je obavezno polje";
    	
    	return null;
    }
    public static String validateUpdate(CategoryToUpdate cat) {
    	
    	if(cat.newName == null) return "Ime je obavezno polje";
    	if(cat.newName.equals("")) return "Ime je obavezno polje";
    	
    	if(cat.cores == 0) return "Jezgra su obavezno polje";
    	
    	if(cat.ram == 0) return "Ram je obavezno polje";
    	
    	return null;
    }
    
}