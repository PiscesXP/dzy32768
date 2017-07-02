package runner;

import java.rmi.RemoteException;
import java.util.ArrayList;

import service.IOService;

public class FileVersionController {
	
	private IOService ios;
	private ArrayList<FileVersion> fvList;
	
	
	public FileVersionController(IOService ios){
		this.ios=ios;
	}
	
	public void setIOService(IOService ios){
		this.ios=ios;
	}
	
	// read file list 
	// add fileVersion to arrayList
	private void checkAllFiles(String account){
		fvList = new ArrayList<FileVersion>();
		String[] fullFileNames;
		try {
			fullFileNames = ios.readFileList(account).split(",");
			for(String fullFileName:fullFileNames){
				fvList.add(new FileVersion(fullFileName));
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}	
	
	public ArrayList<FileVersion> getAllFileVersion(String account){
		checkAllFiles(account);	
		return fvList;
		/*
		StringBuffer sb = new StringBuffer();
		sb.append("Select a file: \r\n");
		for(int i=0;i<fvList.size();++i){
			sb.append(i);
			sb.append(":  ");
			sb.append(fvList.get(i).getDate());
			sb.append("\r\n");
		}
		return sb.toString();	
		*/	
	}
	
	public void writeFile(String account,String name,String content,String postfix){
		try {
			ios.writeFile(content, account, name, postfix);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
