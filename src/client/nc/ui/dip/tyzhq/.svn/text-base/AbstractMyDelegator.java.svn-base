package nc.ui.dip.tyzhq;

import java.util.Hashtable;

import nc.vo.dip.tyzhq.DipTYZHDatachangeBVO;




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
	                         
           DipTYZHDatachangeBVO[] bodyVOs1 =
                       (DipTYZHDatachangeBVO[])queryByCondition(DipTYZHDatachangeBVO.class,
                                                    getBodyCondition(DipTYZHDatachangeBVO.class,key));   
            if(bodyVOs1 != null && bodyVOs1.length > 0){
                          
              dataHashTable.put("dip_tyzhq_b",bodyVOs1);
            } 
	         
	   	   return dataHashTable;
		
	}
	
	
       /**
         *
         *该方法用于获取查询条件，用户在缺省实现中可以对该方法进行修改。
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == DipTYZHDatachangeBVO.class)
	   return " pk_tyzhq_h = '" + key + "' and isnull(dr,0)=0 ";
       		
	 return null;
       } 
	
}
