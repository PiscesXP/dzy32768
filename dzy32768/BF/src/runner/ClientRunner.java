package runner;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import rmi.RemoteHelper;
import ui.MainFrame;

public class ClientRunner {
	
	private RemoteHelper remoteHelper;
	
	private String account;
	private boolean isLogin=false;
	
	// history version
	private CodeVersionHistory cvh;
	
	private FileVersionController fvc;
	
// ------------------------------ init --------------------------------------------
	
	public static void main(String[] args){
		MainFrame mainFrame = new MainFrame();
	}
	
	public ClientRunner(String originCode){
		linkToServer();
		
		initCodeHistory(originCode);
		initFileController();
	}
	
	private void linkToServer() {
		try {
			remoteHelper = RemoteHelper.getInstance();
			remoteHelper.setRemote(Naming.lookup("rmi://127.0.0.1:8887/DataRemoteObject"));	
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(null, "Link establish failed.\r\nPlease try again.", "Link failed.", JOptionPane.WARNING_MESSAGE);
			System.exit(0);
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	
	
// --------------------------------------------------------------------------
	
	public String runProgram(String code,String input){
		try {
			return remoteHelper.getExecuteService().execute(code, input);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * output memory table as String[n][3]
	 * */
	public String[][] getMemTableList(){
		byte[] memBytes;
		String[][] list=null;
		try {
			memBytes = remoteHelper.getExecuteService().getMemBytes();
			list = new String[memBytes.length][3];
			for(int i=0;i<list.length;++i){
				list[i][0]=String.valueOf(i);
				list[i][1]=String.valueOf(memBytes[i]);
				
				// convert to ASCII char
				char c = (char) memBytes[i];
				if((c>='0' && c<='9') || (c>='a' && c<='z') || (c>='A' && c<='Z'))
					list[i][2]=String.valueOf(c);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	
// --------------------------  login logout ----------------------------------------
	/**
	 * return login account name.
	 * */
	public String login(){		
		if(isLogin){
			JOptionPane.showMessageDialog(null, "You are now login as " + account + ".", "Already login", JOptionPane.WARNING_MESSAGE);
		}
		String password;
		while(!isLogin){
			account=JOptionPane.showInputDialog(null,"Input your account:");
			password=JOptionPane.showInputDialog(null,"Input your password:");
			try {
				isLogin=remoteHelper.getUserService().login(account, password);
				if(isLogin){
					JOptionPane.showMessageDialog(null, "You are now login as " + account + ".", "Success", JOptionPane.WARNING_MESSAGE);
				}
				else{
					JOptionPane.showMessageDialog(null, "The account or password is incorrect.\r\nPlease try again.", "Login fail", JOptionPane.WARNING_MESSAGE);
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}		
		return account;
	}
	
	public void logout(){
		isLogin=false;
		JOptionPane.showMessageDialog(null, "Logout successful.", "Logout", JOptionPane.WARNING_MESSAGE);
	}
	
	public void initCodeHistory(String editorContent){		
		cvh = new CodeVersionHistory(editorContent);
	}
	
	public void codeChange(String editorContent){
		cvh.addNewVersion(editorContent);
	}
	
	public String undo(){
		return cvh.undo();
	}
	
	/**
	 * return null if not redoable.
	 * */
	public String redo(){
		if(cvh.isRedoable()){
			return cvh.redo();
		}
		return null;
	}
	
	
	
// ---------------------------- file version controller --------------------------------------

	// check all code of user
	// select a code file to open
	
	private void initFileController(){
		fvc = new FileVersionController(remoteHelper.getIOService());
	}
	
	// check all code files
	// select a file to open
	public String SelectCode(){
		if(!isLogin){
			JOptionPane.showMessageDialog(null, "Please login!", "", JOptionPane.WARNING_MESSAGE);
			return null;
		}
		
		ArrayList<FileVersion> fv= fvc.getAllFileVersion(account);
		
		StringBuffer sb = new StringBuffer();
		sb.append("Select a file: \r\n");
		for(int i=0;i<fv.size();++i){
			sb.append(i);
			sb.append(":  ");
			sb.append(fv.get(i).getName());
			sb.append("   ");
			sb.append(fv.get(i).getPostfix());
			sb.append(" File   ");
			sb.append(fv.get(i).getDate());
			sb.append("\r\n");
		}
		
		String input;
		int selectFile = -1;
		do{
			input=JOptionPane.showInputDialog(null,sb.toString());
			if(input.equals("") || input==null)
				continue;
			selectFile = Integer.parseInt(input);
		}while(selectFile<0 || selectFile>fv.size());
		
		return fv.get(selectFile).getContent(remoteHelper.getIOService());
	}

	
	public void saveCode(String codeToSave){
		if(!isLogin){
			JOptionPane.showMessageDialog(null, "Please login!", "", JOptionPane.WARNING_MESSAGE);
			return;
		}
		String fileName=JOptionPane.showInputDialog(null,"Input a file name: ");
		String code = codeToSave;
		String postfix;
		if(code.contains("Ook"))
			postfix="OOK";
		else
			postfix="BF";
		fvc.writeFile(account, fileName, code, postfix);
		JOptionPane.showMessageDialog(null, "File save successful.", "File saved", JOptionPane.INFORMATION_MESSAGE);
	}
	
// -----------------------------------------------------------------------------

}
