package model;

import java.util.Collection;
import java.util.HashSet;

public class Organization {
	private String name;
	private String description;
	private String logo;
	private transient Collection<User> users;
	private transient Collection<VirtualMachine> virtualMachines;
	private Collection<Drive> drives;

	
	
	
	public Organization(String name, String description, String logo, Collection<User> users,
			Collection<VirtualMachine> virtualMachines, Collection<Drive> drives) {
		super();
		this.name = name;
		this.description = description;
		this.logo = logo;
		this.users = users;
		this.virtualMachines = virtualMachines;
		this.drives = drives;
	}


	public Organization() {
		super();
		this.name = "";
		this.users = new HashSet<User>();
		this.virtualMachines = new HashSet<VirtualMachine>();
		this.drives = new HashSet<Drive>();
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getLogo() {
		return logo;
	}


	public void setLogo(String logo) {
		this.logo = logo;
	}


	public Collection<User> getUsers() {
		return users;
	}


	public void setUsers(Collection<User> users) {
		this.users = users;
	}


	public Collection<VirtualMachine> getVirtualMachines() {
		return virtualMachines;
	}


	public void setVirtualMachines(Collection<VirtualMachine> virtualMachines) {
		this.virtualMachines = virtualMachines;
	}

	public void addMachine(VirtualMachine vm){
		this.virtualMachines.add(vm);
	}

	public void clearVirtualMachines(){
		this.virtualMachines = new HashSet<VirtualMachine>();
	}

	public Collection<Drive> getDrives() {
		return drives;
	}


	public void setDrives(Collection<Drive> drives) {
		this.drives = drives;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Organization other = (Organization) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "name : " +this.name;
	}

	public void addUser(User u){
		this.users.add(u);
	}


	public void addVirtaulMachine(VirtualMachine machine) {
		virtualMachines.add(machine);
		
	}
}
