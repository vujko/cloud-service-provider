package services;

import java.util.HashSet;
import java.util.Set;

import model.CategoryVM;
import model.User;
import model.VirtualMachine;
import model.User.Role;

public class MachineService {
	private static Set<VirtualMachine> machines = loadMachines();
	
	
	private static HashSet<VirtualMachine> loadMachines(){
		HashSet<VirtualMachine> machines = new HashSet<VirtualMachine>();
		VirtualMachine vm1 = new VirtualMachine("Virtualna1", new CategoryVM("K1", 4, 4, 8), null);
		VirtualMachine vm2 = new VirtualMachine("Virtualna2", new CategoryVM("K2", 8, 8, 16), null);
		machines.add(vm1);
		machines.add(vm2);
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

		// return machines;
	}
	public VirtualMachine getMachine(String name) {
		for(VirtualMachine v : machines)
			if(v.getName().equals(name))
				return v;
		return null;
	}
}
