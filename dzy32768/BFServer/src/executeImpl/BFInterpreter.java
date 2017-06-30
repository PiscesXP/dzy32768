package executeImpl;


/**
 * @author zhenxi
 *
 */
public class BFInterpreter {

	String code;
	
	int codePointer=0;
	// code run at
	int byteCount=1;
	// count byte need
	int inputRequest=0;
	int bracketCount=0;
	
	// allocate memory
	int memPointer=0;
	byte memBytes[];
	
	// get input
	int inputPointer=0;
	char programInput[];
	
	String output="";
	
	bracketPair bracketPairList[];
	
	
	/**
	 * @param sourceCode : The origin code, may include invalid commands.
	 * @param inputToProgram : Input stream.
	 * 
	 * */
	public BFInterpreter(String sourceCode,String inputToProgram){
		code=sourceCode;
		programInput=inputToProgram.toCharArray();
		
		allocateMemory();
		markBrackets();
	}
	
	
	private void allocateMemory(){
		for(int i=0;i<code.length();++i){
			if(code.charAt(i)=='>')
				++byteCount;
		}
		memBytes=new byte[byteCount];
		for(int i=0;i<memBytes.length;++i){
			memBytes[i]=0;
		}
	}
	
	/**
	 * Mark these pairs: [ ].</br>
	 * Store in bracketPairList[].
	 * */
	private void markBrackets(){
		bracketCount=0;
		for(int i=0;i<code.length();++i){
			if(code.charAt(i)=='[')
				++bracketCount;
		}
		
		bracketPairList=new bracketPair[bracketCount];
		for(int i=0;i<bracketCount;++i){
			bracketPairList[i]=new bracketPair();
		}

		
		// mark [
		int counter=0;
		char c;
		for(int i=0;i<code.length();++i){
			c=code.charAt(i);
			if(c=='['){
				bracketPairList[counter].begin=i;
				++counter;
			}
		}
		
		// mark ]
		// search backward
loop1:	for(int i=0;i<code.length();++i){
			c=code.charAt(i);
			if(c==']'){
				for(bracketPair bp : bracketPairList){
					if(bp.isValid() && bp.end==i){
						continue loop1;
					}
				}
				
				int j=i-1;
searchBack:		while(j>=0){
					if(code.charAt(j)=='['){
						for(bracketPair bp : bracketPairList){
							if(bp.begin==j && !bp.isValid()){
								bp.end=i;
								break searchBack;
							}
						}
					}
					--j;
				}
			}
		}
		
	}
	
	/**
	 * Get another bracket position of the pair.</br>
	 * If given begin return end.</br>
	 * If given end return begin.
	 * */
	private int getPairPosition(int position1){
		for(bracketPair bp:bracketPairList){
			if(!bp.isValid())
				System.out.println("Error!");
			if(bp.begin==position1)
				return bp.end;
			if(bp.end==position1)
				return bp.begin;
		}
		return -1;
	}
	
	/**
	 * Execute next instruction.
	 * @return TRUE: Success.</br> FALSE: Reach end of program.
	 * */
	public boolean nextIns(){
		if(codePointer>=code.length())
			return false;
		
		switch(code.charAt(codePointer++)){
		case '>':
			++memPointer;
			break;
		case '<':
			--memPointer;
			break;
		case '+':
			++memBytes[memPointer];
			break;
		case '-':
			--memBytes[memPointer];
			break;
		case ',':
			memBytes[memPointer]=(byte)programInput[inputPointer++];
			break;
		case '.':
			output=output+(char)(memBytes[memPointer]);
			break;
		case '[':
			if(memBytes[memPointer]==0){
				codePointer=getPairPosition(--codePointer);
			}
			break;
		case ']':
			if(memBytes[memPointer]!=0){
				codePointer=getPairPosition(--codePointer);
			}
			break;
		default:
			
		}
		return true;
	}
	
	public int getCodePointer(){
		return codePointer;
	}
	
	
	/**
	 * Execute the program without stop.
	 * */
	public void execute(){
		while(nextIns());
		/*
		while(codePointer<codeLength){
			nextIns();
		}*/
	}
	
	public String getOutput(){
		return output;
	}
	
	public int getMemByteCount(){
		return byteCount;
	}
	
	public byte[] getMemBytes(){
		return memBytes;
	}
}


/**
 * @author zhenxi
 * @version 17.5.13
 */
class bracketPair{
	int begin=-1;
	int end=-1;
	bracketPair(){
	}
	
	bracketPair(int beginAt,int endAt){
		begin=beginAt;
		end=endAt;
	}

	public boolean isValid(){
		if(begin!=-1 && end!=-1)
			return true;
		return false;
	}

}
