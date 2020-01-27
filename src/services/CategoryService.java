package services;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import model.CategoryVM;

public class CategoryService {
	private static final String Path = "./data/category.json";
    private static Gson g = new Gson();
    private static Set<CategoryVM> categories = loadCategories(Path);


    public static Set<CategoryVM> loadCategories(String path){
        Set<CategoryVM> categories = new HashSet<CategoryVM>();
		try {
			Type drivesType = new TypeToken<Set<CategoryVM>>(){}.getType();
			FileReader fw = new FileReader(path);
			JsonReader reader = new JsonReader(fw);
			categories = g.fromJson(reader, drivesType);
			return categories;
		} catch (IOException e) {
			e.printStackTrace();
		}

        return categories;
    }
    public static void saveCategories(String path) {
    	try {
			FileWriter writer = new FileWriter(path);
			String json = g.toJson(categories);
			writer.write(json);
			writer.close();
		} catch (JsonIOException e) {
            e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public Set<CategoryVM> getCategories(){
        return categories;
    }

    public boolean addCategory(CategoryVM cat){
        if(categoryExsists(cat.getName())){
            return false;
        }
        categories.add(cat);
        saveCategories(Path);
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
        saveCategories(Path);
        return true;
    }

    public static CategoryVM getCategory(String name){
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
            saveCategories(Path);
            return true;
        }
        return false;
    }
}