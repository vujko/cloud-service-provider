package services;

import java.io.FileReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import model.Drive;


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
	
	public Set<Drive> getDrives(){
		return drives;
	}
	
	
}
