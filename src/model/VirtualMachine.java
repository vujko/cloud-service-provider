package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;


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

	public double getBill(Date startDate, Date endDate){
		long validTimeMilli = 0;
		for(DateActivity d : listOfActivities){
			
			if(d.getEndActivity() == null){
				if(d.getStartActivity().after(startDate)){			
					validTimeMilli += Math.abs(new Date().getTime() - d.getStartActivity().getTime());
				}
			}
			else if(d.getStartActivity().before(startDate) && d.getEndActivity().before(endDate) && d.getEndActivity().after(startDate)){
				validTimeMilli += Math.abs(d.getEndActivity().getTime() - startDate.getTime());
			}

			else if(d.getStartActivity().after(startDate) && d.getEndActivity().after(endDate) && d.getStartActivity().before(endDate)){
				validTimeMilli += Math.abs(new Date().getTime() - d.getStartActivity().getTime());
			}

			else if(d.getStartActivity().after(startDate) && d.getEndActivity().before(endDate)){
				validTimeMilli += Math.abs(d.getEndActivity().getTime() - d.getStartActivity().getTime());
			}
		}
		long hours = TimeUnit.HOURS.convert(validTimeMilli, TimeUnit.MILLISECONDS);
		double result = hours * getMonthlyBill() /( 30 * 24 );
		return result;
	}

	private double getMonthlyBill(){
		double result = 0.0;
		result += category.getCores() * 25;
		result += category.getRam() * 15;
		result += category.getGpus();

		// for(Drive d : drives){
		// 	if(d.getType() == DriveType.HDD){
		// 		result += 0.1 * d.getCapacity();
		// 	}
		// 	else{
		// 		result += 0.3 * d.getCapacity();
		// 	}
		// }
		return result;
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
