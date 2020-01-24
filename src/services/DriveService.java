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
import model.User;
import model.User.Role;
import model.Drive.DriveType;
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
	
	public Drive addDrive(String name,DriveType type,int capacity,String vmName) {
		VirtualMachine vm = App.machineService.getMachine(vmName);
		if(!driveExists(name)){
			Drive drive = new Drive(name,type,capacity);
			drive.setVm(vm);
			drives.add(drive);
			return drive;
			//povezati sa virtuelnim
		}
		return null;
				
	}
	public boolean deleteDrive(String name) {
		if(driveExists(name)) {
			Drive drive = getDrive(name);
			drives.remove(drive);
			return true;
		}
		return false;
	}
	
	public boolean updateDrive(Update update) {
		if(driveExists(update.newName)) {
			return false;
		}
		Drive drive = getDrive(update.oldName);
		drive.setName(update.newName);
		drive.setType(update.type);
		drive.setCapacity(update.capacity);
		drive.setVm(App.machineService.getMachine(update.vm));
		return true;
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
	public Drive getDrive(String name) {
		for(Drive d:drives) {
			if(d.getName().equals(name))
				return d;
		}
		return null;
	}
}
