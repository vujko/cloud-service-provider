package model;

import java.util.Collection;

public class VirtualMachine {
	private String name;
	private CategoryVM category;
	private Collection<Drive> drives;
	
	public VirtualMachine(String name, CategoryVM category, Collection<Drive> drives) {
		super();
		this.name = name;
		this.category = category;
		this.drives = drives;
	}

	public VirtualMachine() {
		super();
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
	
	
	
	
}
