package services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import controllers.MachineController.MachineToAdd;
import controllers.MachineController.MachineToUpdate;
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
			Drive drive = DriveService.getDrive(diskName);
			vm.addDrive(drive);
			drive.setVm(vm);
			// drive.setOrganization(org);
		}
		machines.add(vm);
		// saveMachines(path);
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
		// saveMachines(path);
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
	public static Set<VirtualMachine> filterVM(String[] checked){
		HashSet<VirtualMachine> filteredCore = new HashSet<VirtualMachine>();
		HashSet<VirtualMachine> filteredRam = new HashSet<VirtualMachine>();
		HashSet<VirtualMachine> filteredGpu = new HashSet<VirtualMachine>();
		if(checked.length == 12)
			return machines;
		if(checked[0].equals("prazna"))
			return machines;
		
		for(String arg : checked) {
			if(arg.equals("4core")) {
				VirtualMachine vm = getMachineCore(4);
				if(vm != null)
					filteredCore.add(vm);
			}
			else if(arg.equals("8core")) {
				VirtualMachine vm = getMachineCore(8);
				if(vm != null)
					filteredCore.add(vm);
			}
			else if(arg.equals("16core")) {
				VirtualMachine vm = getMachineCore(16);
				if(vm != null)
					filteredCore.add(vm);
			}
			else if(arg.equals("32core")) {
				VirtualMachine vm = getMachineCore(32);
				if(vm != null)
					filteredCore.add(vm);
			}
		}
		for(String arg: checked) {
			if(arg.equals("4gb")) {
				VirtualMachine vm = getMachineRam(4);
				if(vm != null)
					filteredRam.add(vm);
			}
			else if(arg.equals("8gb")) {
				VirtualMachine vm = getMachineRam(8);
				if(vm != null)
					filteredRam.add(vm);
			}
			else if(arg.equals("16gb")) {
				VirtualMachine vm = getMachineRam(16);
				if(vm != null)
					filteredRam.add(vm);
			}
			else if(arg.equals("32gb")) {
				VirtualMachine vm = getMachineRam(32);
				if(vm != null)
					filteredRam.add(vm);
			}
		}
		for(String arg : checked) {
			if(arg.equals("4gpu")) {
				VirtualMachine vm = getMachineGpu(4);
				if(vm != null)
					filteredGpu.add(vm);
			}
			else if(arg.equals("8gpu")) {
				VirtualMachine vm = getMachineGpu(8);
				if(vm != null)
					filteredGpu.add(vm);
			}
			else if(arg.equals("16gpu")) {
				VirtualMachine vm = getMachineGpu(16);
				if(vm != null)
					filteredGpu.add(vm);
			}
			else if(arg.equals("32gpu")) {
				VirtualMachine vm = getMachineGpu(32);
				if(vm != null)
					filteredGpu.add(vm);
			}
		}
		if(filteredCore.size() != 0 && filteredRam.size() != 0 && filteredGpu.size()!=0) {
			filteredCore.retainAll(filteredRam);
			if(filteredCore.size()!=0)
				filteredCore.retainAll(filteredGpu);
			return filteredCore;
		}
		if(filteredCore.size() != 0 && filteredRam.size() != 0) {
			filteredCore.retainAll(filteredRam);
			return filteredCore;
		}
		if(filteredCore.size() != 0 && filteredGpu.size() != 0) {
			filteredCore.retainAll(filteredGpu);
			return filteredCore;
		}
		if(filteredRam.size() != 0 && filteredGpu.size() != 0) {
			filteredRam.retainAll(filteredGpu);
			return filteredRam;
		}
		if(filteredRam.size() != 0)
			return filteredRam;
		if(filteredGpu.size() != 0)
			return filteredGpu;
		
		return filteredCore;
	}
	
	public static VirtualMachine getMachineCore(int cores) {
		for(VirtualMachine vm : machines) {
			if(vm.getCategory().getCores() == cores) {
				return vm;
			}
		}
		return null;
	}
	public static VirtualMachine getMachineRam(int rams) {
		for(VirtualMachine vm : machines) {
			if(vm.getCategory().getRam() == rams) {
				return vm;
			}
		}
		return null;
	}
	public static VirtualMachine getMachineGpu(int gpu) {
		for(VirtualMachine vm : machines) {
			if(vm.getCategory().getGpus() == gpu) {
				return vm;
			}
		}
		return null;
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
