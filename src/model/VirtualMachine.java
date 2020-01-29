package model;

import java.util.Collection;
import java.util.HashSet;

public class VirtualMachine {
	private String name;
	private CategoryVM category;
	private transient Collection<Drive> drives;
	private Organization organization;
	
	public VirtualMachine(String name, CategoryVM category, Collection<Drive> drives, Organization organization) {
		super();
		this.name = name;
		this.category = category;
		this.drives = drives;
		this.organization = organization;
	}

	public VirtualMachine() {
		this.drives = new HashSet<Drive>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CategoryVM getCategory() {
		return category;
	}

	public void setCategory(CategoryVM category) {
		this.category = category;
	}

	public Collection<Drive> getDrives() {
		return drives;
	}

	public void setDrives(Collection<Drive> drives) {
		this.drives = drives;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public Organization getOrganization(){
		return organization;
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
		VirtualMachine other = (VirtualMachine) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public void addDrive(Drive drive) {
		this.drives.add(drive);
		drive.setOrganization(this.organization);
		this.organization.addDrive(drive);
	}

	public void clearDisks(){
		for (Drive drive : drives) {
			drive.setVm(null);
		}
		this.drives = new HashSet<Drive>();
	}

	public void deleteDrive(Drive d){
		drives.forEach(driveToDelete -> {
			if(driveToDelete.getName().equals(d.getName())){
				driveToDelete = null;
				
			}
		});
	}
	
	
	
	
}
