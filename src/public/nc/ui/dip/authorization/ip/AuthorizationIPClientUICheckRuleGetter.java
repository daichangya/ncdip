package nc.ui.dip.authorization.ip;

import java.io.Serializable;

import nc.vo.trade.pub.IBDGetCheckClass2;

public class AuthorizationIPClientUICheckRuleGetter implements IBDGetCheckClass2,Serializable {

	public String getUICheckClass() {
		// TODO Auto-generated method stub
		return "nc.ui.dip.authorization.ip.AuthorizationIPClientUICheckRule";
	}

	public String getCheckClass() {
		// TODO Auto-generated method stub
		return null;
	}


}
