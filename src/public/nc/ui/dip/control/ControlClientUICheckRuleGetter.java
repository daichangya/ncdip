package nc.ui.dip.control;

import java.io.Serializable;

import nc.vo.trade.pub.IBDGetCheckClass2;

public class ControlClientUICheckRuleGetter implements IBDGetCheckClass2,Serializable {
	/**
	 * 前台校验类
	 */
	public String getUICheckClass() {
		return "nc.ui.dip.control.ControlClientUICheckRule";
	}

	/**
	 * 后台校验类
	 */
	public String getCheckClass() {
		return null;
	}

}
