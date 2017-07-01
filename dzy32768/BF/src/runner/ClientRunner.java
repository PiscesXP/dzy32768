package runner;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import rmi.RemoteHelper;
import service.IOService;
import ui.MainFrame;

public class ClientRunner {

	public static void main(String[] args){
		MainFrame mainFrame = new MainFrame();
		//ClientRunner cr = new ClientRunner();
		//cr.test();
	}
}
