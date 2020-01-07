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

import model.Organization;


public class OrganizationService {

    private static Gson g = new Gson();
    private static final String path = "./data/organizations.json";
    private static Set<Organization> organizations = loadOrganizations(path);

    public static Set<Organization> loadOrganizations(String path){

        try{
            Set<Organization> organizations = new HashSet<Organization>();
            Type orgsType = new TypeToken<Set<Organization>>(){}.getType();
            JsonReader reader = new JsonReader(new FileReader(path));
            organizations = g.fromJson(reader, orgsType);
            return organizations;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashSet<Organization>();
    }

    public static void saveOrganizations(String path) {
        try {
            FileWriter writer = new FileWriter(path);
            String json = g.toJson(organizations);
            writer.write(json);
            writer.close();
        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Set<Organization> getOrganizations(){
        return organizations;
    }

    public boolean addOrganization(Organization org){
        if(organizationExsists(org.getName())){
            return false;
        }
        organizations.add(org);
        saveOrganizations(path);
        return true;
    }

    public boolean updateOrganization(String oldName, Organization newOrg){
        Organization org = getOrganization(oldName);
        if(!oldName.equalsIgnoreCase(newOrg.getName())){
            if(organizationExsists(newOrg.getName())){
                return false;
            }
        }
        org.setName(newOrg.getName());
        org.setDescription(newOrg.getDescription());
        org.setLogo(newOrg.getLogo());
        saveOrganizations(path);
        return true;
    }
    
    public boolean deleteOrganization(String name) { 	
    	if(organizationExsists(name)) {
    		Organization forDelete = getOrganization(name);
            organizations.remove(forDelete);
            saveOrganizations(path);
        	return true;
    	}
    	return false;
    }

    public boolean organizationExsists(String name){
        for (Organization org : organizations) {
            if(org.getName().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }

    public Organization getOrganization(String name){
        for (Organization org : organizations) {
            if(org.getName().equalsIgnoreCase(name)){
                return org;
            }
        }
        return null;
    }
}