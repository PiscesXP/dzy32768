//需要客户端的Stub
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
public interface IOService1 extends Remote{
	public boolean writeFile(String file, String userId, String fileName)throws RemoteException;
	
	public String readFile(String userId, String fileName)throws RemoteException;
	
	public String readFileList(String userId)throws RemoteException;
}
