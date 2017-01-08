package nc.itf.dip.pub;

import nc.vo.dip.warningset.MyBillVO;
//�����ϵ���
public interface IDipWarsetAlert {
	/**
	 * @author wyd
	 * @desc ��ǰ̨�����ϵ��Ԥ��������ִ��Ԥ��
	 * @param hpk Ԥ����������
	 * @return ����ִ���Ƿ�ɹ�
	 * */
	public boolean doTastAtOnce(String hpk);
	/**
	 * @author wyd
	 * @desc ��ǰ̨�����ϵ��Ԥ��ֹͣ��ִ��ֹͣԤ��
	 * @param hpk Ԥ����������
	 * @return ����ִ���Ƿ�ɹ�
	 * */
	public boolean doStopTaskAtOnce(String hpk);
	/**
	 * @author wyd
	 * @desc ��ǰ̨�����ϵ��Ԥ��������ִ��Ԥ��
	 * @param hpk Ԥ����������
	 * @return ����ִ���Ƿ�ɹ�
	 * */
	public boolean doTastAtOnce(MyBillVO mbvo);
	/**
	 * @author wyd
	 * @desc ��ǰ̨�����ϵ��Ԥ��ֹͣ��ִ��ֹͣԤ��
	 * @param hpk Ԥ����������
	 * @return ����ִ���Ƿ�ɹ�
	 * */
	public boolean doStopTaskAtOnce(MyBillVO mbvo);
}
