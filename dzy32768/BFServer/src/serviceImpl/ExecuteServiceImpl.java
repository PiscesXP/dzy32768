//请不要修改本文件名
package serviceImpl;

import java.rmi.RemoteException;

import service.ExecuteService;
import service.UserService;

import executeImpl.*;

public class ExecuteServiceImpl implements ExecuteService {
	
	private OOKExecute ooke;
	private BFExecute bfe;
	private byte[] memBytes;
	
	
	
	/**
	 * 请实现该方法
	 */
	@Override
	public String execute(String code, String param) throws RemoteException {
		// TODO Auto-generated method stub
		String exeResult;
		if(code.contains("Ook")){
			ooke = new OOKExecute();
			exeResult=ooke.execute(code, param);
			memBytes=ooke.getMemBytes();			
			return exeResult;
		}
		else{
			bfe = new BFExecute();
			exeResult=bfe.execute(code, param);
			memBytes=bfe.getMemBytes();
			return exeResult;
		}
	}
	
	@Override
	public byte[] getMemBytes() throws RemoteException{
		return memBytes;		
	}

}
