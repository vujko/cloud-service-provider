package services;

import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;

import model.CategoryVM;

public class CategoryService {
    //TODO citanje i upis kategorija
    private static Gson g = new Gson();
    private static Set<CategoryVM> categories = loadCategories("123");


    public static Set<CategoryVM> loadCategories(String path){
        Set<CategoryVM> categories = new HashSet<CategoryVM>();
        categories.add(new CategoryVM("Kategorija1", 8, 8, 16));
        categories.add(new CategoryVM("Kategorija2", 4, 4, 8));


        return categories;
    }

    public Set<CategoryVM> getCategories(){
        return categories;
    }

    public boolean addCategory(CategoryVM cat){
        if(categoryExsists(cat.getName())){
            return false;
        }
        categories.add(cat);
        // saveOrganizations(path);
        return true;
    }

    private boolean categoryExsists(String name){
        for (CategoryVM cat : categories) {
            if(cat.getName().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }

    public boolean updateCategory(String oldName, CategoryVM newCat){
        CategoryVM cat = getCategory(oldName);
        if(!oldName.equalsIgnoreCase(newCat.getName())){
            if(categoryExsists(newCat.getName())){
                return false;
            }
        }
        cat.setName(newCat.getName());
        cat.setCores(newCat.getCores());
        cat.setGpus(newCat.getGpus());
        cat.setRam(newCat.getRam());
        // saveCategories();
        return true;
    }

    public CategoryVM getCategory(String name){
        for (CategoryVM cat : categories) {
            if(cat.getName().equalsIgnoreCase(name)){
                return cat;
            }
        }
        return null;
    }

    public boolean deleteCategory(String name){
        if(categoryExsists(name)){
            CategoryVM forDelete = getCategory(name);
            categories.remove(forDelete);
            // saveCategories();
            return true;
        }
        return false;
    }
}