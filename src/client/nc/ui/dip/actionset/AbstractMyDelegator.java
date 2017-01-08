package nc.ui.dip.actionset;

import java.util.Hashtable;

import nc.vo.dip.actionset.ActionSetBVO;

/**
 * 
 * 抽象的业务代理类
 * 
 * @author author
 * @version tempProject version
 */
public abstract class AbstractMyDelegator extends
		nc.ui.trade.bsdelegate.BDBusinessDelegator {

	public Hashtable loadChildDataAry(String[] tableCodes, String key)
			throws Exception {

		Hashtable dataHashTable = new Hashtable();

		ActionSetBVO[] bodyVOs1 = (ActionSetBVO[]) queryByCondition(
				ActionSetBVO.class, getBodyCondition(ActionSetBVO.class, key));
		if (bodyVOs1 != null && bodyVOs1.length > 0) {

			dataHashTable.put("dip_actionset_b", bodyVOs1);
		}

		return dataHashTable;

	}

	/**
	 * 
	 * 该方法用于获取查询条件，用户在缺省实现中可以对该方法进行修改。
	 * 
	 */
	public String getBodyCondition(Class bodyClass, String key) {

		if (bodyClass == ActionSetBVO.class)
			return "pk_actionset_h = '" + key + "' and isnull(dr,0)=0 ";

		return null;
	}

}
