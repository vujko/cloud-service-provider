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

import controllers.DriveController.DriveToAdd;
import controllers.DriveController.Filter;
import controllers.DriveController.Update;
import main.App;
import model.Drive;
import model.Organization;
import model.User;
import model.User.Role;
import model.VirtualMachine;


public class DriveService {
	private static final String path = "./data/drives.json";
	private static Gson g = new Gson();
	private static Set<Drive> drives = loadDrives();
		
	public static Set<Drive> loadDrives(){
		Set<Drive> drivess = new HashSet<Drive>();
		
		try {
			Type drivesType = new TypeToken<Set<Drive>>(){}.getType();
			FileReader fw = new FileReader(path);
			JsonReader reader = new JsonReader(fw);
			drivess = g.fromJson(reader, drivesType);
			return drivess;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return drivess;	
	}

	public Set<Drive> getDrives(){
		return drives;
	}

	public Set<Drive> getAvilableDrives(){
		Set<Drive> result = new HashSet<Drive>();
		for(Drive d : drives){
			if(d.getOrganization() == null || d.getOrganization().getName().equals("")){
				result.add(d);
			}
		}
		return result;
	}
	
	public Drive addDrive(String email,DriveToAdd dta) {
		if(!driveExists(dta.name)){	
			Drive drive = new Drive(dta.name,dta.type,dta.capacity);
			
			//super admin mora postaviti postojecu organizaciju
			if(OrganizationService.organizationExsists(dta.organization)){
				Organization org = OrganizationService.getOrganization(dta.organization);
				drive.setOrganization(org);
				org.addDrive(drive);
			}
			//admin kao org salje prazno ime
			else{
				User user = UserService.getUser(email);
				Organization org = user.getOrganization();
				org.addDrive(drive);
				drive.setOrganization(org);

			}

			if(MachineService.machineExsists(dta.vm)){
				VirtualMachine vm = MachineService.getMachine(dta.vm);
				drive.setVm(vm);
				vm.getDrives().add(drive);


			}
			drives.add(drive);
			saveDrives();
			return drive;
		}
		return null;
	}
	public boolean deleteDrive(String name) {
		if(driveExists(name)) {
			Drive drive = getDrive(name);
			drives.remove(drive);
			removeDriveFromOrganizations(name);
			removeDriveFromMachines(name);
			OrganizationService.saveOrganizations();
			MachineService.saveMachines();		
			saveDrives();
			return true;
		}
		return false;
	}

	private void removeDriveFromOrganizations(String name){

		for (Organization org : App.orgService.getOrganizations()) {
			for(Drive d : org.getDrives()){
				if(d.getName().equals(name)){
					org.getDrives().remove(d);
					return;
				}
			}
		}
	}

	private void removeDriveFromMachines(String name){

		for (VirtualMachine vm : App.machineService.getMachines()) {
			for(Drive d : vm.getDrives()){
				if(d.getName().equals(name)){
					vm.getDrives().remove(d);
					return;
				}
			}
		}
	}
	
	public boolean updateDrive(Update update) {
		if(!update.newName.equals(update.oldName)) {
			if(driveExists(update.newName)) 
				return false;
		}
		Drive drive = getDrive(update.oldName);
		drive.setName(update.newName);
		drive.setType(update.type);
		drive.setCapacity(update.capacity);
		VirtualMachine vm = MachineService.getMachine(update.vm);
		drive.setVm(vm);
		vm.addDrive(drive);
		saveDrives();
		return true;
	}
	
	public static void search(String argument,HashSet<Drive> diskovi){
		diskovi.removeIf(filter -> !filter.getName().toLowerCase().contains(argument.toLowerCase()));
	}
	
	public static Set<Drive> filterCapacity(Filter filter,String email){
		HashSet<Drive> users_drives = new HashSet<Drive>();
		User user = UserService.getUser(email);
		
		if(user.getRole() == User.Role.SUPER_ADMIN)
			users_drives = new HashSet<Drive>(drives);
		else
			users_drives = new HashSet<Drive> (user.getOrganization().getDrives());
		
		if(filter.searchArg != null)
			search(filter.searchArg,users_drives);
		if(filter.capFrom != 0)
			capFrom(filter.capFrom,users_drives);
		if(filter.capTo != 0)
			capTo(filter.capTo,users_drives);
		if(filter.type.size() == 1)
			diskType(filter.type.get(0),users_drives);

		return users_drives;
	}
	
	public static void capFrom(int from, HashSet<Drive> diskovi) {
		diskovi.removeIf(filter -> filter.getCapacity() <= from);
	}
	
	public static void capTo(int to, HashSet<Drive> diskovi) {
		diskovi.removeIf(filter -> filter.getCapacity() > to);
	}
	
	public static void diskType(String type, HashSet<Drive> diskovi) {
		diskovi.removeIf(filter -> !filter.getType().name().equals(type));
	}
	
	public static void saveDrives() {
		try {
			FileWriter writer = new FileWriter(path);
			String json = g.toJson(drives);
			writer.write(json);
			writer.close();
		} catch (JsonIOException e) {
            e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Set<Drive> getDrives(String email){
		User user = UserService.getUser(email);
		if(user.getRole() == Role.SUPER_ADMIN){
			return drives;
		}
		return new HashSet<Drive>(user.getOrganization().getDrives());
	}
	
	public boolean driveExists(String name) {
		for(Drive d:drives) {
			if(d.getName().equals(name))
				return true;
		}
		return false;
	}
	public static Drive getDrive(String name) {
		for(Drive d:drives) {
			if(d.getName().equals(name))
				return d;
		}
		return null;
	}
}
