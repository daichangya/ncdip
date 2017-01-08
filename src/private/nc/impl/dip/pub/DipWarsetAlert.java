package nc.impl.dip.pub;

import sj.alter.threadpool.factory.ThreadpoolFactoryAtOnce;
import nc.itf.dip.pub.IDipWarsetAlert;
import nc.vo.dip.warningset.MyBillVO;

public class DipWarsetAlert implements IDipWarsetAlert {

	/**
	 * @author wyd
	 * @desc ��ǰ̨�����ϵ��Ԥ��������ִ��Ԥ��
	 * @param hpk Ԥ����������
	 * @return ����ִ���Ƿ�ɹ�
	 * */
	public boolean doTastAtOnce(String hpk){
		Alert alert=new Alert(hpk);
		return ThreadpoolFactoryAtOnce.doTaskAtOnce(alert);
		
	}
	/**
	 * @author wyd
	 * @desc ��ǰ̨�����ϵ��Ԥ��ֹͣ��ִ��ֹͣԤ��
	 * @param hpk Ԥ����������
	 * @return ����ִ���Ƿ�ɹ�
	 * */
	public boolean doStopTaskAtOnce(String hpk){
		Alert alert=new Alert(hpk);
		return ThreadpoolFactoryAtOnce.doStopTaskAtOnce(alert);
	}
	/**
	 * @author wyd
	 * @desc ��ǰ̨�����ϵ��Ԥ��������ִ��Ԥ��
	 * @param hpk Ԥ����������
	 * @return ����ִ���Ƿ�ɹ�
	 * */
	public boolean doTastAtOnce(MyBillVO mbvo){
		Alert alert=new Alert(mbvo);
		return ThreadpoolFactoryAtOnce.doTaskAtOnce(alert);
		
	}
	/**
	 * @author wyd
	 * @desc ��ǰ̨�����ϵ��Ԥ��ֹͣ��ִ��ֹͣԤ��
	 * @param hpk Ԥ����������
	 * @return ����ִ���Ƿ�ɹ�
	 * */
	public boolean doStopTaskAtOnce(MyBillVO mbvo){
		Alert alert=new Alert(mbvo);
		return ThreadpoolFactoryAtOnce.doStopTaskAtOnce(alert);
	}
}
