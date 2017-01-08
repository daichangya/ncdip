package nc.ui.dip.dataverify;

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

         nc.vo.dip.dataverify.DataverifyBVO[] bodyVOs1 =
         (nc.vo.dip.dataverify.DataverifyBVO[])queryByCondition(nc.vo.dip.dataverify.DataverifyBVO.class,
          getBodyCondition(nc.vo.dip.dataverify.DataverifyBVO.class,key));   
if(bodyVOs1 != null && bodyVOs1.length > 0){

dataHashTable.put("dip_dataverify_b",bodyVOs1);
} 

return dataHashTable;

}
	
       /**
         *
         *该方法用于获取查询条件，用户在缺省实现中可以对该方法进行修改。
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == nc.vo.dip.dataverify.DataverifyBVO.class)
	   return "pk_dataverify_h = '" + key + "' and isnull(dr,0)=0 ";
       		
	 return null;
       } 

	
}
