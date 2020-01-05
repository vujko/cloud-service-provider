package services;

import java.util.HashSet;
import java.util.Set;

import model.Organization;
import model.User;
import model.User.Role;

public class UserService {
    private static Set<User> users = loadUsers("users.json");
    //TODO required ne radi kako treba
    //TODO ucitavanje iz fajla
    public static Set<User> loadUsers(String path){

        Set<User> users = new HashSet<User>();
        users.add(new User("vukasin@gmail.com", "Vukasin", "Jokic", new Organization(), Role.SUPER_ADMIN, "123" ));
        users.add(new User("nikola@gmail.com", "Nikola", "Stojanovic", new Organization(), Role.ADMIN, "123"));
        return users;
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
    	return true;
    }
    public boolean deleteUser(String email) {
    	if(userExsists(email)) {
    		User u = getUser(email);
    		users.remove(u);
    		return true;
    	}
    	return false;
    }
}
