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

import controllers.DriveController.Update;
import main.App;
import model.Drive;
import model.Organization;
import model.Drive.DriveType;
import model.User;
import model.User.Role;
import model.VirtualMachine;


public class DriveService {
	private static final String Path = "./data/drives.json";
	private static Gson g = new Gson();
	private static Set<Drive> drives = loadDrives(Path);
		
	public static Set<Drive> loadDrives(String path){
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
			if(d.getOrganization().getName().equals("") || d.getOrganization() == null){
				result.add(d);
			}
		}
		return result;
	}
	
	public Drive addDrive(String email,String name,DriveType type,int capacity,String vmName) {
		if(!driveExists(name)){	
			Drive drive = new Drive(name,type,capacity);
			User user = UserService.getUser(email);
			if(MachineService.machineExsists(vmName)){
				VirtualMachine vm = MachineService.getMachine(vmName);
				drive.setVm(vm);
				drive.setOrganization(vm.getOrganization());
				vm.getDrives().add(drive);

				if(user.getRole() == User.Role.SUPER_ADMIN){
					vm.getOrganization().addDrive(drive);
				}
				else{
					user.getOrganization().addDrive(drive);
				}
			}
			else if(user.getRole() == User.Role.ADMIN){
				user.getOrganization().addDrive(drive);
				drive.setOrganization(user.getOrganization());
			}
			drives.add(drive);
			saveDrives(Path);
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
			saveDrives(Path);
			return true;
		}
		return false;
	}

	private void removeDriveFromOrganizations(String name){
		boolean removed = false;
		for (Organization org : App.orgService.getOrganizations()) {
			for(Drive d : org.getDrives()){
				if(d.getName().equals(name)){
					org.getDrives().remove(d);
					//d = null;
					removed = true;
				}
			}
			if(removed)
				return;
		}
	}

	private void removeDriveFromMachines(String name){
		boolean removed = false;
		for (VirtualMachine vm : App.machineService.getMachines()) {
			for(Drive d : vm.getDrives()){
				if(d.getName().equals(name)){
					d = null;
					removed = true;
				}
			}
			if(removed)
				return;
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
		drive.setVm(MachineService.getMachine(update.vm));
		saveDrives(Path);
		return true;
	}
	
	public Set<Drive> search(String argument){
		HashSet<Drive> searched = new HashSet<Drive>();
		if(argument == null) {
			return drives;
		}
		for(Drive drive : drives) {
			if(drive.getName().toLowerCase().contains(argument.toLowerCase()))
				searched.add(drive);
		}
		return searched;
	}
	
	public Set<Drive> filterCapacity(Boolean[] checked,String email){
		HashSet<Drive> filteredCap = new HashSet<Drive>();
		HashSet<Drive> filteredType = new HashSet<Drive>();
		Set<Drive> users_drives = new HashSet<Drive>();
		User user = UserService.getUser(email);
		
		if(user.getRole() == User.Role.SUPER_ADMIN)
			users_drives = this.drives;
		else if(user.getRole() == User.Role.ADMIN)
			users_drives = new HashSet<Drive> (user.getOrganization().getDrives());
		
		
		if(checked[0]&&checked[1]&&checked[2]&&checked[3]&&checked[4])
			return users_drives;
		if(!checked[0]&&!checked[1]&&!checked[2]&&!checked[3]&&!checked[4])
			return users_drives;
		
		for(Drive d : users_drives) {
			if(checked[0]) {
				if(d.getCapacity()>= 200 && d.getCapacity()<500)
					filteredCap.add(d);
			}if(checked[1]) {
				if(d.getCapacity()>=500 && d.getCapacity()<1000)
					filteredCap.add(d);
			}
			if(checked[2]) {
				if(d.getCapacity()>= 1000)
					filteredCap.add(d);
			}
		}
		if(!checked[0]&&!checked[1]&&!checked[2]) {
			for(Drive d : users_drives) {
				if(checked[3] && d.getType().equals(Drive.DriveType.HDD))
					filteredCap.add(d);
				if(checked[4] && d.getType().equals(Drive.DriveType.SSD))
					filteredCap.add(d);
			}
			return filteredCap;
		}
			
		
		for(Drive d : filteredCap) {
			if(checked[3] && checked[4])
				return filteredCap;
			if(checked[3] || checked[4]) {
				if(checked[3] && d.getType().equals(Drive.DriveType.HDD)) {
					filteredType.add(d);
				}
				if(checked[4] && d.getType().equals(Drive.DriveType.SSD))
					filteredType.add(d);
			}
		}
		if(!checked[3] && !checked[4])
			return filteredCap;
		return filteredType;
	}
	
	public static void saveDrives(String path) {
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
