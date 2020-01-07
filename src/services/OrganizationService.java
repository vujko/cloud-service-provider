package services;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import model.Organization;
import model.User;

public class OrganizationService {

    private static Set<Organization> organizations = loadOrganizations("organizations.json");

    public static Set<Organization> loadOrganizations(String path){
        Set<Organization> org = new HashSet<Organization>();
        org.add(new Organization("Organizacija1", "Jako dobra organizacija", "Logo1", new HashSet<User>() , null));
        org.add(new Organization("Organizacija2", "Jos bolja organizacija", "Logo2", new HashSet<User>(), null));
        return org;
    }

    public Set<Organization> getOrganizations(){
        return organizations;
    }

    public boolean addOrganization(Organization org){
        if(organizationExsists(org.getName())){
            return false;
        }
        organizations.add(org);
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
        return true;
    }
    
    public boolean deleteOrganization(String name) { 	
    	if(organizationExsists(name)) {
    		Organization forDelete = getOrganization(name);
    		organizations.remove(forDelete);
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