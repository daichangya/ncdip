package nc.itf.dip.pub.checkplugin;

import nc.util.dip.sj.CheckMessage;



public interface ICheckPlugin {
	/**
	 * @desc У����ӿ�
	 * @param Object... ob�������Ĳ���
	 * @return CheckMessage ������Ϣ�������Ƿ�ɹ����ɹ���¼����ʧ�ܼ�¼���������ʧ�ܣ���������ʧ��ԭ��
	 * */
	public CheckMessage doCheck(Object... ob);
}
