package ui;

import java.util.ArrayList;

public class CodeVersionHistory {
	
	private ArrayList<CodeVersion> historyCode = new ArrayList<CodeVersion>();
	private CodeVersion currentVersion;
	private int topVersionNumber=0;
	
	
	CodeVersionHistory(String firstVersion){
		CodeVersion cv = new CodeVersion(firstVersion,0,0);
		historyCode.add(0,cv);
		currentVersion=cv;
	}
	
	public void addNewVersion(String text){
		++topVersionNumber;
		CodeVersion cv = new CodeVersion(text,topVersionNumber,currentVersion.getVersion());
		currentVersion.setNextVersion(topVersionNumber);
		historyCode.add(topVersionNumber,cv);
		currentVersion=cv;
	}
	
	public String undo(){
		CodeVersion currentCodeVersion = currentVersion;
		CodeVersion preCodeVersion = historyCode.get(currentCodeVersion.getPreVersion());
		currentVersion=preCodeVersion;	
		return preCodeVersion.getCode();
	}
	
	// check whether there is next version
	public String redo(){
		CodeVersion currentCodeVersion = currentVersion;
		CodeVersion nextCodeVersion = historyCode.get(currentCodeVersion.getNextVersion());
		currentVersion=nextCodeVersion;
		return nextCodeVersion.getCode();

	}
	
	public boolean isRedoable(){
		if(currentVersion.getNextVersion()==-1)
			return false;
		return true;
	}
	
	public boolean isUndoable(){
		if(currentVersion.getPreVersion()==0)
			return false;
		return true;
	}
	
}
