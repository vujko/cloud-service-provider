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
}