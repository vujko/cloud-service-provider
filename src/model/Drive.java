package model;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Drive {
	public enum DriveType{SSD, HDD;}
	private String name;
	private DriveType type;
	private int capacity;
	private VirtualMachine vm;
	private Organization organization;
	
	public Drive(String name, DriveType type, int capacity, VirtualMachine vm, Organization org) {
		super();
		this.name = name;
		this.type = type;
		this.capacity = capacity;
		this.vm = vm;
		this.organization = org;
	}
	public Drive(String name,DriveType type, int capacity) {
		this.name = name;
		this.type = type;
		this.capacity = capacity;
		this.vm = new VirtualMachine();
		this.organization = new Organization();
	}
	public Drive() {
		
		this.vm = new VirtualMachine();
		this.organization = new Organization();
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

	public Organization getOrganization(){
		return organization;
	}
	public void setOrganization(Organization org){
		this.organization = org;
	}

	public double getBill(Date startDate, Date endDate){
		long hours = TimeUnit.HOURS.convert(endDate.getTime()- startDate.getTime(), TimeUnit.MILLISECONDS);
		
		double result = 0.0;
		if(type == DriveType.HDD){
			result += 0.1 * capacity;
		}
		else{
			result += 0.3 * capacity;
		}

		return hours * result /( 30 * 24 );
	

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
