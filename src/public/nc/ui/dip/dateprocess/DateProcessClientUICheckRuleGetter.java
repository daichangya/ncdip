package nc.ui.dip.dateprocess;

import java.io.Serializable;

import nc.vo.trade.pub.IBDGetCheckClass2;

public class DateProcessClientUICheckRuleGetter implements IBDGetCheckClass2,Serializable  {

	/**
	 * ǰ̨У����
	 */
	public String getUICheckClass() {
		return "nc.ui.dip.dateprocess.DateProcessClientUICheckRule";
	}

	/**
	 * ��̨У����
	 */
	public String getCheckClass() {
		return null;
	}
}
