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

import controllers.OrganizationControlller.OrganizationToAdd;
import controllers.OrganizationControlller.OrganizationToUpdate;
import model.Drive;
import model.Organization;
import model.VirtualMachine;


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

    public static void saveOrganizations() {
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

    public Set<VirtualMachine> getSelectedMachines(String orgName){
        for (Organization org : organizations) {
            if(org.getName().equals(orgName)){
                return  new HashSet<VirtualMachine>(org.getVirtualMachines());
            }
        }
        return new HashSet<VirtualMachine>();
    }

    public Set<Drive> getSelectedDisks(String orgName){
        for(Organization org : organizations){
            if(org.getName().equals(orgName)){
                return new HashSet<Drive>(org.getDrives());
            }
        }
        return new HashSet<Drive>();
    }

    public Set<Drive> getDrivesWithoutVM(String orgName){
        Set<Drive> result = new HashSet<Drive>();
        for(Organization org : organizations){
            if(org.getName().equals(orgName)){
                for(Drive d : org.getDrives()){
                    if(d.getVm() == null || d.getVm().getName() == null || d.getVm().getName() == ""){
                        result.add(d);
                    }
                }
                return result;
            }
        }
        return result;
    }

    public boolean addOrganization(OrganizationToAdd ota){
        Organization org = new Organization();
        if(organizationExsists(ota.name)){
            return false;
        }
        org.setName(ota.name);
        org.setDescription(ota.description);
        org.setLogo(ota.logo);
        for (String driveName : ota.disks) {
            Drive d = DriveService.getDrive(driveName);
            org.addDrive(d);
            d.setOrganization(org);
        }
        organizations.add(org);
        saveOrganizations();
        return true;
    }

    public boolean updateOrganization(OrganizationToUpdate newOrg){
        Organization org = getOrganization(newOrg.oldName);
        if(!newOrg.oldName.equalsIgnoreCase(newOrg.newName)){
            if(organizationExsists(newOrg.newName)){
                return false;
            }
        }
        org.setName(newOrg.newName);
        org.setDescription(newOrg.description);
        org.setLogo(newOrg.logo);
        org.clearDrives();
        for(String driveName : newOrg.disks){
            Drive d = DriveService.getDrive(driveName);
            org.addDrive(d);
            d.setOrganization(org);
        }
        saveOrganizations();
        return true;
    }
    
    public boolean deleteOrganization(String name) { 	
    	if(organizationExsists(name)) {
            Organization forDelete = getOrganization(name);
            forDelete.getVirtualMachines().forEach((vm) -> vm.setOrganization(null));
            organizations.remove(forDelete);
            saveOrganizations();
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