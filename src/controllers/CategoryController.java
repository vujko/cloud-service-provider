package controllers;

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
    public static Route getCategories = (Request req, Response res) ->{
        res.type("application/json");
        return App.g.toJson(App.categoryService.getCategories());
    };

    public static Route addCategory = (Request req, Response res) ->{
        CategoryVM cat = App.g.fromJson(req.body(), CategoryVM.class);
        res.type("application/json");

        if(App.categoryService.addCategory(cat)){
            res.status(200);
            return true;
        }
        res.status(400);

        return false; 
    };

    public static Route updateCategory = (Request req, Response res)->{
        CategoryToUpdate updateCat = App.g.fromJson(req.body(), CategoryToUpdate.class);
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
        String catName = App.g.fromJson(req.body(), String.class);
        res.type("application/json");
        if(App.categoryService.deleteCategory(catName)){
            res.status(200);
            return true;
        }
        res.status(400);
        return false;
    };
    
}