package nc.itf.dip.pub.checkplugin;

import nc.util.dip.sj.CheckMessage;



public interface ICheckPlugin {
	/**
	 * @desc 校验类接口
	 * @param Object... ob传过来的参数
	 * @return CheckMessage 返回消息，包括是否成功，成功记录数，失败记录数，如果有失败，具体哪行失败原因
	 * */
	public CheckMessage doCheck(Object... ob);
}
