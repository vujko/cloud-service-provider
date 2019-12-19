package model;

public class CategoryVM {
	private String name;
	private int cores;
	private int ram;
	private int gpus;
	public CategoryVM(String name, int cores, int ram, int gpus) {
		super();
		this.name = name;
		this.cores = cores;
		this.ram = ram;
		this.gpus = gpus;
	}
	public CategoryVM() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCores() {
		return cores;
	}
	public void setCores(int cores) {
		this.cores = cores;
	}
	public int getRam() {
		return ram;
	}
	public void setRam(int ram) {
		this.ram = ram;
	}
	public int getGpus() {
		return gpus;
	}
	public void setGpus(int gpus) {
		this.gpus = gpus;
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
		CategoryVM other = (CategoryVM) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}
