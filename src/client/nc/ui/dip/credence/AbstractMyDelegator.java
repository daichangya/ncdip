package nc.ui.dip.credence;

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
	                         
           nc.vo.dip.credence.CredenceBVO[] bodyVOs1 =
                       (nc.vo.dip.credence.CredenceBVO[])queryByCondition(nc.vo.dip.credence.CredenceBVO.class,
                                                    getBodyCondition(nc.vo.dip.credence.CredenceBVO.class,key));   
            if(bodyVOs1 != null && bodyVOs1.length > 0){
                          
              dataHashTable.put("pk_credence_b",bodyVOs1);
            } 
	         
	   	   return dataHashTable;
		
	}
	
	
       /**
         *
         *该方法用于获取查询条件，用户在缺省实现中可以对该方法进行修改。
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == nc.vo.dip.runsys.DipRunsysBVO.class)
	   return "pk_credence_h = '" + key + "' and isnull(dr,0)=0 ";
       		
	 return null;
       } 
	
}
