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

import controllers.MachineController.Filter;
import controllers.MachineController.MachineToAdd;
import controllers.MachineController.MachineToUpdate;

import model.Drive;
import model.User;
import model.User.Role;
import model.VirtualMachine;

public class MachineService {
	private static final String path = "./data/machines.json";
	private static Gson g = new Gson();
	private static Set<VirtualMachine> machines = loadMachines();
	
	private static HashSet<VirtualMachine> loadMachines(){
		HashSet<VirtualMachine> machiness = new HashSet<VirtualMachine>();
		
		try {
			Type drivesType = new TypeToken<Set<VirtualMachine>>(){}.getType();
			FileReader fw = new FileReader(path);
			JsonReader reader = new JsonReader(fw);
			machiness = g.fromJson(reader, drivesType);
			return machiness;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return machiness;
	}
	
	public static void saveMachines() {
		try {
			FileWriter writer = new FileWriter(path);
			String json = g.toJson(machines);
			writer.write(json);
			writer.close();
		} catch (JsonIOException e) {
            e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public Set<VirtualMachine> getMachines(){
		return machines;
	}
	public Set<VirtualMachine> getMachines(String email){
		User user = UserService.getUser(email);
		if(user.getRole() == Role.SUPER_ADMIN){
			return machines;
		}
		return new HashSet<VirtualMachine>(user.getOrganization().getVirtualMachines());
	}
	public Set<VirtualMachine> getAvilableMachines(){
		Set<VirtualMachine> result = new HashSet<VirtualMachine>();
		for (VirtualMachine vm : machines) {
			if(vm.getOrganization() == null){
				result.add(vm);
			}
		}
		return result;
	}

	public Set<Drive> getSelectedDisks(String machineName){
		for(VirtualMachine vm : machines){
			if(vm.getName().equals(machineName)){
				return new HashSet<Drive>(vm.getDrives());
			}
		}
		return new HashSet<Drive>();
	}

	public static VirtualMachine getMachine(String name) {
		for(VirtualMachine v : machines)
			if(v.getName().equals(name))
				return v;
		return null;
	}

	public static boolean addMachine(MachineToAdd mta){
		VirtualMachine vm = new VirtualMachine();
		if(machineExsists(mta.name)){
			return false;
		}
		vm.setName(mta.name);
		vm.setCategory(CategoryService.getCategory(mta.categoryName));
		for (String diskName : mta.disks) {
			Drive drive = DriveService.getDrive(diskName);
			vm.addDrive(drive);
			drive.setVm(vm);
			// drive.setOrganization(org); TODO
		}
		machines.add(vm);
		saveMachines();
		return true;
	}

	public static boolean updateMachine(MachineToUpdate mtu){
		VirtualMachine vm = getMachine(mtu.oldName);
		if(!mtu.oldName.equals(mtu.newName)){
			if(machineExsists(mtu.newName)){
				return false;
			}
		}
		vm.setName(mtu.newName);
		vm.setCategory(CategoryService.getCategory(mtu.categoryName));
		vm.clearDisks();
		for(String diskName : mtu.disks){
			Drive drive = DriveService.getDrive(diskName);
			vm.addDrive(drive);
			drive.setVm(vm);
			drive.setOrganization(vm.getOrganization());
		}
		saveMachines();
		return true;
	}

	public static Set<VirtualMachine> searchMachine(String arg){
		HashSet<VirtualMachine> searched = new HashSet<VirtualMachine>();
		if(arg == null)
			return machines;
		for(VirtualMachine vm : machines) {
			if(vm.getName().toLowerCase().contains(arg.toLowerCase()))
				searched.add(vm);
		}
		return searched;
	}
	public static Set<VirtualMachine> filterVM(Filter checked,String email){
		HashSet<VirtualMachine> filteredCore = new HashSet<VirtualMachine>();
		HashSet<VirtualMachine> filteredRam = new HashSet<VirtualMachine>();
		HashSet<VirtualMachine> filteredGpu = new HashSet<VirtualMachine>();
		User user = UserService.getUser(email);
		
		if(checked.core.size() == 3 && checked.ram.size() == 3 && checked.gpu.size() == 3) {
			if(user.getRole()==User.Role.SUPER_ADMIN)
				return machines;
			else {
				return new HashSet<VirtualMachine>(user.getOrganization().getVirtualMachines());
			}
		}	
		if(checked.core.size() == 0 && checked.ram.size() == 0 && checked.gpu.size() == 0) {
			if(user.getRole()==User.Role.SUPER_ADMIN)
				return machines;
			else {
				return new HashSet<VirtualMachine>(user.getOrganization().getVirtualMachines());
		}
		}
		for(String arg : checked.core) {
			if(arg.equals("4core")) {
				HashSet<VirtualMachine> vm = getMachineCore(4,user);
				if(vm.size() != 0) {
					for(VirtualMachine v : vm)
						filteredCore.add(v);
				}
			}
			else if(arg.equals("8core")) {
				HashSet<VirtualMachine> vm = getMachineCore(8,user);
				if(vm.size() != 0) {
					for(VirtualMachine v : vm)
						filteredCore.add(v);
				}
			}
			else if(arg.equals("16core")) {
				HashSet<VirtualMachine> vm = getMachineCore(16,user);
				if(vm.size() != 0) {
					for(VirtualMachine v : vm)
						filteredCore.add(v);
				}
			}
			else if(arg.equals("32core")) {
				HashSet<VirtualMachine> vm = getMachineCore(32,user);
				if(vm.size() != 0) {
					for(VirtualMachine v : vm)
						filteredCore.add(v);
				}
			}
		}
		for(String arg: checked.ram) {
			if(arg.equals("4gb")) {
				HashSet<VirtualMachine> vm = getMachineRam(4,user);
				if(vm.size() != 0) {
					for(VirtualMachine v : vm)
						filteredRam.add(v);
				}
					
			}
			else if(arg.equals("8gb")) {
				HashSet<VirtualMachine> vm = getMachineRam(8,user);
				if(vm.size() != 0) {
					for(VirtualMachine v : vm)
						filteredRam.add(v);
				}
			}
			else if(arg.equals("16gb")) {
				HashSet<VirtualMachine> vm = getMachineRam(16,user);
				if(vm.size() != 0) {
					for(VirtualMachine v : vm)
						filteredRam.add(v);
				}
			}
			else if(arg.equals("32gb")) {
				HashSet<VirtualMachine> vm = getMachineRam(32,user);
				if(vm.size() != 0) {
					for(VirtualMachine v : vm)
						filteredRam.add(v);
				}
			}
		}
		for(String arg : checked.gpu) {
			if(arg.equals("4gpu")) {
				HashSet<VirtualMachine> vm = getMachineGpu(4,user);
				if(vm.size() != 0) {
					for(VirtualMachine v : vm)
						filteredGpu.add(v);
				}
					
			}
			else if(arg.equals("8gpu")) {
				HashSet<VirtualMachine> vm = getMachineGpu(8,user);
				if(vm.size() != 0) {
					for(VirtualMachine v : vm)
						filteredGpu.add(v);
				}
			}
			else if(arg.equals("16gpu")) {
				HashSet<VirtualMachine> vm = getMachineGpu(16,user);
				if(vm.size() != 0) {
					for(VirtualMachine v : vm)
						filteredGpu.add(v);
				}
			}
			else if(arg.equals("32gpu")) {
				HashSet<VirtualMachine> vm = getMachineGpu(32,user);
				if(vm.size() != 0) {
					for(VirtualMachine v : vm)
						filteredGpu.add(v);
				}
			}
		}
		if(checked.core.size() != 0 && checked.ram.size() != 0 && checked.gpu.size()!=0) {
			filteredCore.retainAll(filteredRam);
			if(filteredCore.size()!=0)
				filteredCore.retainAll(filteredGpu);
			return filteredCore;
		}
		if(checked.core.size() != 0 && checked.ram.size() != 0) {
			filteredCore.retainAll(filteredRam);
			return filteredCore;
		}
		if(checked.core.size() != 0 && checked.gpu.size() != 0) {
			filteredCore.retainAll(filteredGpu);
			return filteredCore;
		}
		if(checked.ram.size() != 0 && checked.gpu.size() != 0) {
			filteredRam.retainAll(filteredGpu);
			return filteredRam;
		}
		
		if(filteredRam.size() != 0 && checked.core.size() == 0 && checked.gpu.size() == 0)
			return filteredRam;
		if(filteredGpu.size() != 0 && checked.core.size() == 0 && checked.ram.size() == 0)
			return filteredGpu;
		if(filteredCore.size() != 0 && checked.ram.size()==0 && checked.gpu.size() == 0)
			return filteredCore;
		
		return new HashSet<VirtualMachine>();
	}
	
	public static HashSet<VirtualMachine> getMachineCore(int cores,User user) {
		HashSet<VirtualMachine> vms = new HashSet<VirtualMachine>();
		Set<VirtualMachine> machiness = new HashSet<VirtualMachine>();
		if(user.getRole() == User.Role.SUPER_ADMIN) {
			machiness = machines;
		}else {
			machiness = new HashSet<VirtualMachine>(user.getOrganization().getVirtualMachines());
		}
		for(VirtualMachine vm : machiness) {
			if(vm.getCategory().getCores() == cores) {
				vms.add(vm);
			}
		}
		return vms;
	}
	public static HashSet<VirtualMachine> getMachineRam(int rams,User user) {
		HashSet<VirtualMachine> vms = new HashSet<VirtualMachine>();
		Set<VirtualMachine> machiness = new HashSet<VirtualMachine>();
		if(user.getRole() == User.Role.SUPER_ADMIN) {
			machiness = machines;
		}else {
			machiness = new HashSet<VirtualMachine>(user.getOrganization().getVirtualMachines());
		}
		for(VirtualMachine vm : machiness) {
			if(vm.getCategory().getRam() == rams) {
				vms.add(vm);
			}
		}
		return vms;
	}
	public static HashSet<VirtualMachine> getMachineGpu(int gpu,User user) {
		HashSet<VirtualMachine> vms = new HashSet<VirtualMachine>();
		Set<VirtualMachine> machiness = new HashSet<VirtualMachine>();
		if(user.getRole() == User.Role.SUPER_ADMIN) {
			machiness = machines;
		}else {
			machiness = new HashSet<VirtualMachine>(user.getOrganization().getVirtualMachines());
		}
		for(VirtualMachine vm : machiness) {
			if(vm.getCategory().getGpus() == gpu) {
				vms.add(vm);
			}
		}
		return vms;
	}
	
	public static boolean machineExsists(String name){
		for (VirtualMachine vm : machines) {
			if(vm.getName().equalsIgnoreCase(name)){
				return true;
			}
		}
		return false;
	}
	
}
