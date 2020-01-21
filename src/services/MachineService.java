package services;

import java.util.HashSet;
import java.util.Set;

import model.VirtualMachine;

public class MachineService {
	private static Set<VirtualMachine> machines = loadMachines();
	
	
	private static HashSet<VirtualMachine> loadMachines(){
		HashSet<VirtualMachine> ma = new HashSet<VirtualMachine>();
		VirtualMachine vm = new VirtualMachine();
		vm.setName("Virtualna1");
		vm.setCategory(null);
		vm.setDrives(null);
		ma.add(vm);
		return ma;
	}
	
	public static Set<VirtualMachine> getMachines(){
		return machines;
	}
}
