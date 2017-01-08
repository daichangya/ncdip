package nc.ui.dip.processstyle;

import java.io.Serializable;

import nc.vo.trade.pub.IBDGetCheckClass2;

public class ProcessStyleClientUICheckRuleGetter implements IBDGetCheckClass2,Serializable {

	/**
	 * 前台校验类
	 */
	public String getUICheckClass() {
		// TODO Auto-generated method stub
		return "nc.ui.dip.processstyle.ProcessStyleClientUICheckRule";
	}

	/**
	 * 后台校验类
	 */
	public String getCheckClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
