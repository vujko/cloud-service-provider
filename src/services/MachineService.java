package services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import controllers.MachineController.MachineToAdd;
import main.App;
import model.CategoryVM;
import model.Drive;
import model.User;
import model.User.Role;
import model.VirtualMachine;

public class MachineService {
	private static Set<VirtualMachine> machines = loadMachines();
	
	
	private static HashSet<VirtualMachine> loadMachines(){
		HashSet<VirtualMachine> machines = new HashSet<VirtualMachine>();
		VirtualMachine vm1 = new VirtualMachine("Virtualna1", new CategoryVM("Kategorija1", 8, 8, 16), new HashSet<Drive>(Arrays.asList(DriveService.getDrive("WD BLUE"))), App.orgService.getOrganization("Organizacija1"));
		VirtualMachine vm2 = new VirtualMachine("Virtualna2", new CategoryVM("Kategorija2", 4, 4, 8), new HashSet<Drive>(Arrays.asList(DriveService.getDrive("TOSHIBA"))), App.orgService.getOrganization("Organizacija2"));
		machines.add(vm1);
		machines.add(vm2);
		App.orgService.getOrganization("Organizacija1").addMachine(vm1);
		App.orgService.getOrganization("Organizacija2").addMachine(vm2);
		return machines;
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
			vm.addDrive(DriveService.getDrive(diskName));
		}
		machines.add(vm);
		// saveMachines(path);
		return true;
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
