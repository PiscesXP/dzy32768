package runner;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import rmi.RemoteHelper;
import service.IOService;
import ui.MainFrame;

public class ClientRunner {
	/*
	private RemoteHelper remoteHelper;
	
	public ClientRunner() {
		linkToServer();
		//initGUI();
	}
	
	private void linkToServer() {
		try {
			remoteHelper = RemoteHelper.getInstance();
			remoteHelper.setRemote(Naming.lookup("rmi://127.0.0.1:8887/DataRemoteObject"));			
			System.out.println("linked");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	private void initGUI() {
		MainFrame mainFrame = new MainFrame();
	}
	
	
	// IO service
	public boolean writeFile(String file, String userId, String fileName)throws RemoteException {
		return remoteHelper.getIOService().writeFile(file, userId, fileName);
	}
	
	public String readFile(String userId, String fileName)throws RemoteException {
		return remoteHelper.getIOService().readFile(userId, fileName);
	}
	
	public String readFileList(String userId)throws RemoteException {
		return remoteHelper.getIOService().readFileList(userId);
	}
	
	
	// User service	
	public boolean login(String username, String password) throws RemoteException{
		return remoteHelper.getUserService().login(username, password);
	}

	public boolean logout(String username) throws RemoteException {
		return remoteHelper.getUserService().logout(username);
	}
	
	
	// execute service
	public String execute(String code, String param) throws RemoteException {
		return remoteHelper.getExecuteService().execute(code, param);
	}
	
	public void test(){
		try {
			System.out.println(remoteHelper.getUserService().login("admin", "123456a"));
			System.out.println(remoteHelper.getIOService().writeFile("2", "admin", "testFile"));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
*/
	public static void main(String[] args){
		MainFrame mainFrame = new MainFrame();
		//ClientRunner cr = new ClientRunner();
		//cr.test();
	}
}
