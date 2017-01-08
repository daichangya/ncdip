package nc.ui.dip.contdatawh;

import java.util.Hashtable;


/**
 *
 *抽象的业务代理类
 *
 * @author author
 * @version tempProject version
 */
public abstract class AbstractMyDelegator extends nc.ui.trade.bsdelegate.BDBusinessDelegator {

	public Hashtable loadChildDataAry(String[] tableCodes,String key) 
	throws Exception{

		Hashtable dataHashTable = new Hashtable();
		nc.vo.dip.contdatawh.ContdatawhBVO[] bodyVOs1 = (nc.vo.dip.contdatawh.ContdatawhBVO[]) queryByCondition(nc.vo.dip.contdatawh.ContdatawhBVO.class, getBodyCondition(nc.vo.dip.contdatawh.ContdatawhBVO.class, key));
		if (bodyVOs1 != null && bodyVOs1.length > 0) {

			dataHashTable.put("pk_contdata_b", bodyVOs1);
		}

		return dataHashTable;

	}


	/**
	 *
	 *该方法用于获取查询条件，用户在缺省实现中可以对该方法进行修改。
	 *
	 */	
	public String getBodyCondition(Class bodyClass,String key){

		if (bodyClass == nc.vo.dip.contdatawh.ContdatawhBVO.class)
			return "pk_contdata_h = '" + key + "' and isnull(dr,0)=0 ";
		return null;
	} 

}
