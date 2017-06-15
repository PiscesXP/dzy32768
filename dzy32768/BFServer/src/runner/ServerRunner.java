package runner;

import rmi.RemoteHelper1;

public class ServerRunner {
	
	public ServerRunner() {
		new RemoteHelper1();
	}
	
	public static void main(String[] args) {
		new ServerRunner();
	}
}
