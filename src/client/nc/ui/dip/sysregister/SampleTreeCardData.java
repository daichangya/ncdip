package nc.ui.dip.sysregister;
import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.pub.IVOTreeDataByID;
import nc.vo.dip.sysregister.DipSysregisterHVO;

import nc.vo.pub.SuperVO;

public class SampleTreeCardData implements IVOTreeDataByID  {
	public String getIDFieldName() {
		return "pk_sysregister_h";
	}

	public String getParentIDFieldName() {
		return null;
	}

	public String getShowFieldName() {
		return "code+extname";
	}

	public SuperVO[] getTreeVO() {

		SuperVO[] treeVOs = null;
		BusinessDelegator business = new BDBusinessDelegator();

		try {
			treeVOs = business.queryByCondition(DipSysregisterHVO.class, "0=0 order by code");


		} catch (Exception e) {
			e.printStackTrace();
		}

		return treeVOs;
	}
}
