package executeImpl;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * ����ʵ����ExecuteService�ӿڣ��ṩ�����Գ�����á�
 * �ڲ��Թ����У����Գ������ñ����execute��������������ַ����������ַ������������õ�BF��������н����
 * ����Ĳ������ݿ��Բο�ExecutionTest�ࡣ
 * 
 * ���޸ı����execute���������ݣ�����ʵ���е������Լ��Ľ��������룬�����ض�Ӧ�����
 * ���������Ҫ����Ľ��������븴�ƽ�����Ŀ��
 * 
 * ע�⣬����Ҫ�޸ı���������Ͱ�������ᵼ�²��Գ����޷��ҵ��������в��ԡ�
 */
public class BFExecute {
	
	private BFInterpreter bfi;
	
	public String execute(String code, String param) throws RemoteException {
		bfi = new BFInterpreter(code,param);
		bfi.execute();
		return bfi.getOutput();
	}
	
	public byte[] getMemBytes(){
		return bfi.getMemBytes();
	}
}