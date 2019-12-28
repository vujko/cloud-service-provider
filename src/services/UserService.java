package services;

import java.util.Set;
import java.util.HashSet;

import model.User;
import model.User.Role;

public class UserService {
    private static Set<User> users = loadUsers("users.json");
    //TODO required ne radi kako treba
    //TODO ucitavanje iz fajla
    public static Set<User> loadUsers(String path){

        Set<User> users = new HashSet<User>();
        users.add(new User("vukasin@gmail.com", "Vukasin", "Jokic", null, Role.SUPER_ADMIN, "123" ));
        users.add(new User("nikola@gmail.com", "Nikola", "Stojanovic", null, Role.ADMIN, "123"));
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
}
