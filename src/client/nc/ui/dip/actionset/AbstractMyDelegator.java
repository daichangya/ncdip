package nc.ui.dip.actionset;

import java.util.Hashtable;

import nc.vo.dip.actionset.ActionSetBVO;

/**
 * 
 * �����ҵ�������
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
	 * �÷������ڻ�ȡ��ѯ�������û���ȱʡʵ���п��ԶԸ÷��������޸ġ�
	 * 
	 */
	public String getBodyCondition(Class bodyClass, String key) {

		if (bodyClass == ActionSetBVO.class)
			return "pk_actionset_h = '" + key + "' and isnull(dr,0)=0 ";

		return null;
	}

}
