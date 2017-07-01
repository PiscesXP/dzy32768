package ui;

public class CodeVersion{
	private String code;
	private int version;
	private int preVersion;
	private int nextVersion=-1;
	
	CodeVersion(String code,int version,int preVersion){
		this.code=code;
		this.version=version;
		this.preVersion=preVersion;		
	}
	
	CodeVersion(String code,int version,int preVersion,int nextVersion){
		this(code,version,preVersion);
		this.nextVersion=nextVersion;
	}
	
	public String getCode(){
		return code;
	}
	
	public int getVersion(){
		return version;
	}
	
	public int getPreVersion(){
		return preVersion;
	}
	
	public int getNextVersion(){
		return nextVersion;
	}
	
	public void setNextVersion(int nextVersion){
		this.nextVersion=nextVersion;
	}
}
