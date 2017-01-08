package nc.ui.dip.processflow;

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

		nc.vo.dip.processflow.DipProcessflowBVO[] bodyVOs1 =
			(nc.vo.dip.processflow.DipProcessflowBVO[])queryByCondition(nc.vo.dip.processflow.DipProcessflowBVO.class,
					getBodyCondition(nc.vo.dip.processflow.DipProcessflowBVO.class,key));   
		if(bodyVOs1 != null && bodyVOs1.length > 0){

			dataHashTable.put("dip_processflow_b",bodyVOs1);
		} 

		return dataHashTable;

	}


	/**
	 *
	 *该方法用于获取查询条件，用户在缺省实现中可以对该方法进行修改。
	 *
	 */	
	 public String getBodyCondition(Class bodyClass,String key){

		 if(bodyClass == nc.vo.dip.processflow.DipProcessflowBVO.class)
			 return " pk_processflow_h= '" + key + "' and isnull(dr,0)=0 ";

		 return null;
	 } 

}
