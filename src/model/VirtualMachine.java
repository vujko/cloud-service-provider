package model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class VirtualMachine {
	private String name;
	private CategoryVM category;
	private transient Collection<Drive> drives;
	private Organization organization;
	private boolean activity;
	private ArrayList<DateActivity> listOfActivities;
	
	public VirtualMachine(String name, CategoryVM category, Collection<Drive> drives, Organization organization) {
		super();
		this.name = name;
		this.category = category;
		this.drives = drives;
		this.organization = organization;
		this.activity = false;
	}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
	public boolean isActivity() {
		return activity;
	}

	public void setActivity(boolean activity) {
		this.activity = activity;
	}

	
	public VirtualMachine() {
		this.drives = new HashSet<Drive>();
		this.listOfActivities = new ArrayList<DateActivity>();
		this.activity = false;
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
	public ArrayList<DateActivity> getListOfActivities() {
		return listOfActivities;
	}

	public void setListOfActivities(ArrayList<DateActivity> listOfActivities) {
		this.listOfActivities = listOfActivities;
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
	}

	public void clearDisks(){
		for (Drive drive : drives) {
			drive.setVm(null);
			drive.setOrganization(null); //da li treba?
		}
		this.drives = new HashSet<Drive>();
	}
	
	
	
	
}
