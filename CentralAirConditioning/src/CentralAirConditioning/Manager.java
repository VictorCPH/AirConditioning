package CentralAirConditioning;

public class Manager {
	private String name;
	private String password;
	
	public Manager(String name, String password) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Manager [name=" + name + ", password=" + password + "]";
	}
	
	
}
