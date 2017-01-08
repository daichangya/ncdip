package nc.ui.dip.messservtype;

import java.io.Serializable;

import nc.vo.trade.pub.IBDGetCheckClass2;

public class MessServTypeClientUICheckRuleGetter implements IBDGetCheckClass2,Serializable  {
	/**
	 * 前台校验类
	 */
	public String getUICheckClass() {
		return "nc.ui.dip.messservtype.MessServTypeClientUICheckRule";
	}

	/**
	 * 后台校验类
	 */
	public String getCheckClass() {
		return null;
	}
}
