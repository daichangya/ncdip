package nc.ui.dip.sourceregist;

import java.io.Serializable;

import nc.vo.trade.pub.IBDGetCheckClass2;

public class SourceRegistClientUICheckRuleGetter implements IBDGetCheckClass2,Serializable  {

	/**
	 * ǰ̨У����
	 */
	public String getUICheckClass() {
		return "nc.ui.dip.sourceregist.SourceRegistClientUICheckRule";
	}

	/**
	 * ��̨У����
	 */
	public String getCheckClass() {
		return null;
	}
}
