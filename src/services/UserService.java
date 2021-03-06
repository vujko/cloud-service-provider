package services;

import java.util.HashSet;
import java.util.Set;

import model.Organization;
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
    public Set<User> getUsersAdmin(String email){
    	Set<User> getusr = new HashSet<User>();
    	User user = getUser(email);
    	for(User u : user.getOrganization().getUsers()) {
    		if(u.getEmail().equals(email))
    			continue;
    		getusr.add(u);
    	}

        return getusr;
    }
    public Set<User> getUsers(){	
        return users;
    }
    public Set<User> getUsersSuper(String email){
    	Set<User> getusr = new HashSet<User>();
    	for(User u : users) {
    		if(u.getEmail().equals(email))
    			continue;
    		getusr.add(u);
    	}
    	return getusr;
    }

    public boolean addUser(User user,String email){
        if(userExsists(user.getEmail())){
            return false;
        }
        if(OrganizationService.organizationExsists(user.getOrganization().getName())) {
        	Organization o = OrganizationService.getOrganization(user.getOrganization().getName());
        	o.getUsers().add(user);
        	user.setOrganization(o);
            users.add(user);
            saveUsers(path);
            return true;
        }
        User u = getUser(email);
        if(u.getRole().name().equals("ADMIN")) {	
        	Organization or = OrganizationService.getOrganization(u.getOrganization().getName());
        	or.addUser(user);
        	user.setOrganization(or);
        	users.add(user);
        	saveUsers(path);
        	return true;
        }
        
        return false;
    }

    public boolean userExsists(String email){
        for (User user : users){
            if(user.getEmail().equalsIgnoreCase(email)){
                return true;
            }
        }
        return false;
    }
    public boolean updateUser(User newUser, String oldEmail) {
    	User user1 = getUser(oldEmail);
    	if(!oldEmail.equalsIgnoreCase(newUser.getEmail())){
            if(userExsists(newUser.getEmail())){
                return false;
            }
        }
        user1.setEmail(newUser.getEmail());
    	user1.setName(newUser.getName());
    	user1.setSurname(newUser.getSurname());
        user1.setPassword(newUser.getPassword());
        user1.setRole(newUser.getRole());
        saveUsers(path);
    	return true;
    }
    public boolean deleteUser(String email) {
    	if(userExsists(email)) {
    		User u = getUser(email);
    		Organization organization = OrganizationService.getOrganization(u.getOrganization().getName());
    		organization.getUsers().remove(u);
            users.remove(u);
            saveUsers(path);
    		return true;
    	}
    	return false;
    }
}
