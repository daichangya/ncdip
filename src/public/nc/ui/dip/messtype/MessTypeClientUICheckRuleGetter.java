package nc.ui.dip.messtype;

import java.io.Serializable;

import nc.vo.trade.pub.IBDGetCheckClass2;

public class MessTypeClientUICheckRuleGetter implements IBDGetCheckClass2,Serializable {

	/**
	 * ǰ̨У����
	 */
	public String getUICheckClass() {
		return "nc.ui.dip.messtype.MessTypeClientUICheckRule";
	}

	/**
	 * ��̨У����
	 */
	public String getCheckClass() {
		return null;
	}
}
