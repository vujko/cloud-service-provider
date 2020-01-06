package services;

import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

import model.User;

public class UserService {

    private static Gson g = new Gson();
    private static final String path = "./data/users.json";
    private static Set<User> users = loadUsers(path);

    public static Set<User> loadUsers(String path) {

        
        try {
            Set<User> users = new HashSet<User>();
            Type usersType = new TypeToken<Set<User>>(){}.getType();
            JsonReader reader = new JsonReader(new FileReader(path));
            users = g.fromJson(reader, usersType);
            return users;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashSet<User>();
       
    }

    public static void saveUsers(String path) {
        try {
            FileWriter writer = new FileWriter(path);
            String json = g.toJson(users);
            writer.write(json);
            writer.close();
        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static User getUser(String email){
        for (User user : users) {
            if(user.getEmail().equalsIgnoreCase(email)){
                return user;
            }
        }
        return null;
    }

    public static User getUser(String email, String password){
        for (User user : users) {
            if(user.getEmail().equalsIgnoreCase(email) & user.getPassword().equalsIgnoreCase(password)){
                return user;
            }
        }
        return null;
    }
    public Set<User> getUsers(){
        return users;
    }

    public boolean addUser(User user){
        if(userExsists(user.getEmail())){
            return false;
        }
        user.setRole(User.Role.USER);
        users.add(user);
        saveUsers(path);
        return true;
    }

    public boolean userExsists(String email){
        for (User user : users){
            if(user.getEmail().equalsIgnoreCase(email)){
                return true;
            }
        }
        return false;
    }
    public boolean updateUser(User newUser) {
    	User user1 = getUser(newUser.getEmail());
    	
    	user1.setName(newUser.getName());
    	user1.setSurname(newUser.getSurname());
        user1.setPassword(newUser.getPassword());
        saveUsers(path);
    	return true;
    }
    public boolean deleteUser(String email) {
    	if(userExsists(email)) {
    		User u = getUser(email);
            users.remove(u);
            saveUsers(path);
    		return true;
    	}
    	return false;
    }
}
