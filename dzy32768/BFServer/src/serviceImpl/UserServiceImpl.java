package serviceImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;

import service.UserService;


public class UserServiceImpl implements UserService{
	
	public static final String PATH = "D:/BF/";
	
	@Override
	public boolean login(String username, String password) throws RemoteException {
		String line,text="";
		try {
			File file = new File(PATH + "Users.txt");
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			try {
				while( ( line=br.readLine() )!=null ){
					text=text.concat(line);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String[] userInfo = text.split(",");
		for(int i=0;i<userInfo.length;i+=2){
			if(userInfo[i].equals(username) && userInfo[i+1].equals(password))
				return true;
		}
		
		return false;
	}

	@Override
	public boolean logout(String username) throws RemoteException {
		return true;
	}

}
