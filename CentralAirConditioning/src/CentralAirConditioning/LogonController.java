package CentralAirConditioning;

public class LogonController {
	private Manager manager;
	
	public LogonController() {
		// TODO Auto-generated constructor stub
	}
	
	boolean logon(String name, String password) {
		manager = (Manager) DBFacade.getInstance().get(name, Manager.class);
		if (manager.getPassword().equals(password))
			return true;
		else 
			return false;
	}
	
	
}
