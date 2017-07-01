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
		if(code.contains("Ook")){
			ooke = new OOKExecute();
			memBytes=ooke.getMemBytes();
			return ooke.execute(code, param);
		}
		else{
			bfe = new BFExecute();
			memBytes=bfe.getMemBytes();
			return bfe.execute(code, param);
		}
	}
	
	@Override
	public byte[] getMemBytes() throws RemoteException{
		return memBytes;		
	}

}
