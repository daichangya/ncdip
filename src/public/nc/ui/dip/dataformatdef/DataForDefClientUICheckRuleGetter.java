package nc.ui.dip.dataformatdef;

import java.io.Serializable;

import nc.vo.trade.pub.IBDGetCheckClass2;

public class DataForDefClientUICheckRuleGetter implements IBDGetCheckClass2,Serializable {
	/**
	 * 前台校验类
	 */
	public String getUICheckClass() {
		return "nc.ui.dip.dataformatdef.DataForDefClientUICheckRule";
	}

	/**
	 * 后台校验类
	 */
	public String getCheckClass() {
		return null;
	}

}
