package model;

public class Drive {
	public enum DriveType{SSD, HDD;}
	private String name;
	private DriveType type;
	private int capacity;
	private VirtualMachine vm;
	
	public Drive(String name, DriveType type, int capacity, VirtualMachine vm) {
		super();
		this.name = name;
		this.type = type;
		this.capacity = capacity;
		this.vm = vm;
	}
	public Drive() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public DriveType getType() {
		return type;
	}
	public void setType(DriveType type) {
		this.type = type;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public VirtualMachine getVm() {
		return vm;
	}
	public void setVm(VirtualMachine vm) {
		this.vm = vm;
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
		Drive other = (Drive) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
	
	
}
