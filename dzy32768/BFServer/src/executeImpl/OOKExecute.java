package executeImpl;

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
public class OOKExecute {
	
	private BFExecute bfe;
	
	public String execute(String code, String param) throws RemoteException {
		bfe = new BFExecute();
		Ook2BF o2b = new Ook2BF();
		return bfe.execute(o2b.CodeInBF(code), param);
	}
	
	public byte[] getMemBytes(){
		return bfe.getMemBytes();
	}
}