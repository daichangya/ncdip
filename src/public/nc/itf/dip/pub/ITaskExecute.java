package nc.itf.dip.pub;

import java.util.List;
import java.util.Map;

import nc.util.dip.sj.RetMessage;
import nc.vo.dip.message.MsrVO;

public interface ITaskExecute {
	/**
	 * @desc ����ִ��--����Ԥ������
	 * @author wyd
	 * @param Ԥ������������
	 * */
	public RetMessage doTaskAtOnce(String pk_warning);
	/**
	 * @desc ǰ̨ͬ��
	 * @param hpk
	 * @param filename
	 * */
	public RetMessage doTbTaskFrom(String hpk,String filename);
	
	
	/**
	 * @desc ����ת��
	 * @author wyd
	 * @param hpk ����ת������������
	 * */
	public RetMessage doDataChangeTask(String hpk);
	/**
	 * @desc ����ͬ��
	 * @author wyd
	 * @param hpk ����ת������������
	 * */
	public RetMessage doTBTask(String hpk);
	/**
	 * @desc ���ݼӹ�
	 * @author wyd
	 * @param hpk ����ת������������
	 * */
	public RetMessage doJGTask(String hpk);
	/**
	 * @desc ���ݷ���
	 * @author wyd
	 * @param hpk ����ת������������
	 * */
	public RetMessage doFSTask(String hpk);
	/**
	 * @desc �Զ���������
	 * @author wyd
	 * @param hpk ���ݶ��ն������������
	 * */
	public RetMessage doAutoContData(String hpk);
	/**
	 * @desc ��������
	 * @author wyd
	 * @param hpk ����ת������������
	 * */
	public RetMessage doLCTask(String hpk);
	/**
	 * @desc ESB�ļ�����
	 * @author wyd
	 * @param hpk ESB�ļ����͵���������
	 * */
	public RetMessage doESBSendTask(String hpk);
	/**
	 * @desc ͨ������ת��
	 * @author wyd
	 * @param hpk ͨ������ת������������
	 * */
	public RetMessage doTYZHTask(String hpk);
	/**
	 * @desc ͨ��XMLת��
	 * @author wyd
	 * @param hpk ͨ��xmlת������������
	 * */
	public RetMessage doTYXMLTask(String hpk);
	/**
	 * @desc �����ļ�·�������path�����ڣ�����·����������ڣ�������
	 * @author wyd
	 * @param path �ļ�·��
	 * */
	public boolean createFilePath(String path);
	
	/**
	 * @desc �ж��Ƿ����Ԥ��������������ϣ�����true,���򷵻�false
	 * @param String pk_warn Ԥ�������� 
	 * */
	public boolean isFitWarnCondition(String pk_warn);
	
	
	public String dosend(MsrVO vo);
	
	public String dosendTopic(MsrVO vo);
	public String dorec(MsrVO vo );
	/**
	 * @desc ����ִ���õ�
	 * */
	public String dosend(MsrVO vo,String msg);
/**
 * liyunzhe add ���� control ��pk_bus,����������Ϊtrue��count��pagecount��������־��д����ʼ�ͽ�����־��
 * @param pk_msr  
 * @param msg
 * @param control �������
 * @param pk_bus  ҵ������
 * @param count  ����,����������Ƚϴ��ʱ����ҳ����һ����0���ڷ���ʱ��д��ʼ��־ʱ��Ҫ�õ���
 * @param pagecount  ��ҳ��,����������Ƚϴ��ʱ����ҳ������������ٲ�����ҳʱ��pagecount=1��
 * @param delayedmap  ��ʱʱ�� b,d,e��
 * @return
 */
	public RetMessage dosendListmsg(String pk_msr,List<String> msg,boolean control,String pk_bus,int count,int pagecount,Map<String,Integer> delayedmap);
	/**
	 * 
	 * @desc ״̬����
	 * 
	 */
	public RetMessage doStateChange(String pk);

}
