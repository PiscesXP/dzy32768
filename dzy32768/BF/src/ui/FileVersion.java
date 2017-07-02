package ui;

import java.rmi.RemoteException;

import service.IOService;

//	FORMAT account_name_date.BF

public class FileVersion {
	
	private String account;
	private String name;	//file name
	private String date;
	private String postfix;
	private String fullFileName;
	/*
	FileVersion(String account,String name,String date){
		this.account=account;
		this.name=name;
		this.date=date;
	}
	*/
	FileVersion (String fullFileName){
		this.fullFileName=fullFileName;
		// auto parse
		this.postfix=fullFileName.substring(fullFileName.indexOf('.')+1);
		String names[] = fullFileName.substring(0, fullFileName.indexOf('.')).split("_");
		// account_name_date.BF/OOK
		this.account=names[0];
		this.name=names[1];
		this.date=names[2];
	}
	
	public String getAccount(){
		return account;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDate(){
		return date;
	}

	public String getPostfix() {
		return postfix;
	}
	
	public String getContent(IOService ios){
		try {
			return ios.readFile(account, name + "_" + date , postfix);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
