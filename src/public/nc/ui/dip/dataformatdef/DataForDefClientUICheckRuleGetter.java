package nc.ui.dip.dataformatdef;

import java.io.Serializable;

import nc.vo.trade.pub.IBDGetCheckClass2;

public class DataForDefClientUICheckRuleGetter implements IBDGetCheckClass2,Serializable {
	/**
	 * ǰ̨У����
	 */
	public String getUICheckClass() {
		return "nc.ui.dip.dataformatdef.DataForDefClientUICheckRule";
	}

	/**
	 * ��̨У����
	 */
	public String getCheckClass() {
		return null;
	}

}
