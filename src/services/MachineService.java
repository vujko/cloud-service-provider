package services;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import controllers.MachineController.Act;
import controllers.MachineController.FilterVM;
import controllers.MachineController.MachineToAdd;
import controllers.MachineController.MachineToUpdate;
import controllers.OrganizationControlller.BillDates;
import model.DateActivity;
import model.Drive;
import model.Organization;
import model.User;
import model.User.Role;
import model.VirtualMachine;

public class MachineService {
	private static final String path = "./data/machines.json";
	private static Gson g = new Gson();
	private static Set<VirtualMachine> machines = loadMachines();
	

	public class MachineBill{
		public String machineName;
		public String price;
	}

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

	public Set<MachineBill> getBills(Set<VirtualMachine> machines, BillDates bd){
		Set<MachineBill> result = new HashSet<MachineBill>();
		for (VirtualMachine vm : machines) {
			MachineBill bill = new MachineBill();
			bill.machineName = vm.getName();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				DecimalFormat f = new DecimalFormat("##.00");
				bill.price = f.format(vm.getBill(sdf.parse(bd.startDate), sdf.parse(bd.endDate)));
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
			result.add(bill);
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

	public static boolean addMachine(String email, MachineToAdd mta){
		VirtualMachine vm = new VirtualMachine();
		if(machineExsists(mta.name)){
			return false;
		}
		vm.setName(mta.name);
		vm.setCategory(CategoryService.getCategory(mta.categoryName));
		if(OrganizationService.organizationExsists(mta.orgName)){
			Organization org = OrganizationService.getOrganization(mta.orgName);
			vm.setOrganization(org);
			org.addMachine(vm);	
		}
		else{
			User admin = UserService.getUser(email);
			Organization org = admin.getOrganization();
			vm.setOrganization(org);
			org.addMachine(vm);
		}

		for (String diskName : mta.disks) {
			Drive drive = DriveService.getDrive(diskName);
			vm.addDrive(drive);
			drive.setVm(vm);
		}

		machines.add(vm);
		saveMachines();
		return true;
	}
	public static VirtualMachine changeActivity(Act vm) {
		VirtualMachine virtual_machine = getMachine(vm.name);
		if(vm.activity != virtual_machine.isActivity()) {
			virtual_machine.setActivity(vm.activity);
			if(vm.activity) {
				DateActivity dact = new DateActivity();
				dact.setStartActivity(new Date());   //current time
				virtual_machine.getListOfActivities().add(dact);
			}else {
				DateActivity dact = virtual_machine.getListOfActivities().get(virtual_machine.getListOfActivities().size() - 1);
				dact.setEndActivity(new Date());
			}
		}
		saveMachines();
		return virtual_machine;
	}
	
	public static boolean updateMachine(MachineToUpdate mtu) throws ParseException{
		VirtualMachine vm = getMachine(mtu.oldName);
		if(!mtu.oldName.equals(mtu.newName)){
			if(machineExsists(mtu.newName)){
				return false;
			}
		}
		vm.setName(mtu.newName);
		//aktivnost
		
		SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy hh:mm:ss a");
		if(mtu.deletedItems.size() != 0) {
			for(String del : mtu.deletedItems) {
				Date date = sdf.parse(del);
				vm.getListOfActivities().removeIf(dact -> (
					sdf.format(dact.getStartActivity()).equals(sdf.format(date))
				));							
				
			}
		}
			
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

	public boolean deleteMachine(String name){
		if(machineExsists(name)){
			VirtualMachine vm = getMachine(name);
			vm.clearDisks();
			DriveService.saveDrives();
			vm.getOrganization().getVirtualMachines().remove(vm);
			OrganizationService.saveOrganizations();
			machines.remove(vm);
			saveMachines();
			return true;
		}
		return false;
	}

	public static void searchMachine(String arg,HashSet<VirtualMachine> machines){
		machines.removeIf(filter -> !filter.getName().toLowerCase().contains(arg.toLowerCase()));
	}
	public static Set<VirtualMachine> filter(FilterVM filter,String email){
		HashSet<VirtualMachine> forFilter;
		User user= UserService.getUser(email);
		if(user.getRole()==User.Role.SUPER_ADMIN)
			forFilter = new HashSet<VirtualMachine>(machines);
		else 
			forFilter = new HashSet<VirtualMachine>(user.getOrganization().getVirtualMachines());
		
		if(filter.searchArg != null)
			searchMachine(filter.searchArg, forFilter);
		if(filter.coreFrom != 0)
			filterCoreFrom(filter.coreFrom, forFilter);
		if(filter.coreTo != 0)
			filterCoreTo(filter.coreTo, forFilter);
		if(filter.ramFrom != 0)
			filterRamFrom(filter.ramFrom, forFilter);
		if(filter.ramTo != 0)
			filterRamTo(filter.ramTo, forFilter);
		if(filter.gpuFrom != 0)
			filterGpuFrom(filter.gpuFrom, forFilter);
		if(filter.gpuTo != 0)
			filterGpuTo(filter.gpuTo, forFilter);
		
		return forFilter;
	}
	public static void filterCoreFrom(int from, HashSet<VirtualMachine> machines) {
		machines.removeIf(filter -> from >= filter.getCategory().getCores());
	}
	
	public static void filterCoreTo(int to, HashSet<VirtualMachine> machines) {
		machines.removeIf(filter ->filter.getCategory().getCores() > to);
	}
	
	public static void filterRamFrom(int from, HashSet<VirtualMachine> machines) {
		machines.removeIf(filter -> from >= filter.getCategory().getRam());
	}
	
	public static void filterRamTo(int to, HashSet<VirtualMachine> machines) {
		machines.removeIf(filter -> filter.getCategory().getRam() > to);
	}
	
	public static void filterGpuFrom(int from, HashSet<VirtualMachine> machines) {
		machines.removeIf(filter -> from >= filter.getCategory().getGpus());
	}
	
	public static void filterGpuTo(int to, HashSet<VirtualMachine> machines) {
		machines.removeIf(filter -> filter.getCategory().getGpus() > to);
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
