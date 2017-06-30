//请不要修改本文件名
package serviceImpl;

import java.rmi.RemoteException;

import service.ExecuteService;
import service.UserService;

import executeImpl.*;

public class ExecuteServiceImpl implements ExecuteService {

	/**
	 * 请实现该方法
	 */
	@Override
	public String execute(String code, String param) throws RemoteException {
		// TODO Auto-generated method stub
		if(code.contains("Ook")){
			OOKExecute ooke = new OOKExecute();
			return ooke.execute(code, param);
		}
		else{
			BFExecute bfe = new BFExecute();
			return bfe.execute(code, param);
		}
	}
	/*
	@Override
	public byte[] getMemBytes() throws RemoteException{
		return null;
		
	}
*/
}
