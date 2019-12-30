package services;

import java.util.HashSet;
import java.util.Set;

import model.Organization;

public class OrganizationService {

    private static Set<Organization> organizations = loadOrganizations("organizations.json");

    public static Set<Organization> loadOrganizations(String path){
        Set<Organization> org = new HashSet<Organization>();
        org.add(new Organization("Organizacija1", "Jako dobra organizacija", "Logo1", null, null));
        org.add(new Organization("Organizacija2", "Jos bolja organizacija", "Logo2", null, null));
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

    public boolean organizationExsists(String name){
        for (Organization org : organizations) {
            if(org.getName().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }
}