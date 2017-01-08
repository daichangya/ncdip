package nc.impl.dip.pub;

import sj.alter.threadpool.factory.ThreadpoolFactoryAtOnce;
import nc.itf.dip.pub.IDipWarsetAlert;
import nc.vo.dip.warningset.MyBillVO;

public class DipWarsetAlert implements IDipWarsetAlert {

	/**
	 * @author wyd
	 * @desc 在前台街面上点击预警启动，执行预警
	 * @param hpk 预警主表主键
	 * @return 返回执行是否成功
	 * */
	public boolean doTastAtOnce(String hpk){
		Alert alert=new Alert(hpk);
		return ThreadpoolFactoryAtOnce.doTaskAtOnce(alert);
		
	}
	/**
	 * @author wyd
	 * @desc 在前台街面上点击预警停止，执行停止预警
	 * @param hpk 预警主表主键
	 * @return 返回执行是否成功
	 * */
	public boolean doStopTaskAtOnce(String hpk){
		Alert alert=new Alert(hpk);
		return ThreadpoolFactoryAtOnce.doStopTaskAtOnce(alert);
	}
	/**
	 * @author wyd
	 * @desc 在前台街面上点击预警启动，执行预警
	 * @param hpk 预警主表主键
	 * @return 返回执行是否成功
	 * */
	public boolean doTastAtOnce(MyBillVO mbvo){
		Alert alert=new Alert(mbvo);
		return ThreadpoolFactoryAtOnce.doTaskAtOnce(alert);
		
	}
	/**
	 * @author wyd
	 * @desc 在前台街面上点击预警停止，执行停止预警
	 * @param hpk 预警主表主键
	 * @return 返回执行是否成功
	 * */
	public boolean doStopTaskAtOnce(MyBillVO mbvo){
		Alert alert=new Alert(mbvo);
		return ThreadpoolFactoryAtOnce.doStopTaskAtOnce(alert);
	}
}
