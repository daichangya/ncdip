package nc.ui.dip.ftpsourceregister;

import java.io.Serializable;

import nc.vo.trade.pub.IBDGetCheckClass2;

public class FtpSourceRegisterClientUICheckRuleGetter  implements IBDGetCheckClass2,Serializable  {

	/**
	 * ǰ̨У����
	 */
	public String getUICheckClass() {
		return "nc.ui.dip.ftpsourceregister.FtpSourceRegisterClientUICheckRuleGetter";
	}

	/**
	 * ��̨У����
	 */
	public String getCheckClass() {
		return null;
	}
}
