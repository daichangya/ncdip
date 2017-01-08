package nc.ui.dip.roleandtable;

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
	   if(tableCodes!=null&&tableCodes.length>0){
		   for(int i=0;i<tableCodes.length;i++){
			   if("dip_roleandtable_b".equals(tableCodes[i])){
				   nc.vo.dip.roleandtable.DipRoleAndTableBVO[] bodyVOs1 =
		               (nc.vo.dip.roleandtable.DipRoleAndTableBVO[])queryByCondition(nc.vo.dip.roleandtable.DipRoleAndTableBVO.class,
		                                            getBodyCondition(nc.vo.dip.roleandtable.DipRoleAndTableBVO.class,key));   
				    if(bodyVOs1 != null && bodyVOs1.length > 0){
				      dataHashTable.put("dip_roleandtable_b",bodyVOs1);
				    } 
			   }
			   if("dip_roleandfunction_c".equals(tableCodes[i])){
				   nc.vo.dip.roleandtable.DipRoleAndFunctionBVO[] bodyVOs1 =
		               (nc.vo.dip.roleandtable.DipRoleAndFunctionBVO[])queryByCondition(nc.vo.dip.roleandtable.DipRoleAndFunctionBVO.class,
		                                            getBodyCondition(nc.vo.dip.roleandtable.DipRoleAndFunctionBVO.class,key));   
				    if(bodyVOs1 != null && bodyVOs1.length > 0){
				      dataHashTable.put("dip_roleandfunction_c",bodyVOs1);
				    } 
			   }
		   }
		   
		   
	   }                      
	   
	         
	   	   return dataHashTable;
		
	}
	
	
       /**
         *
         *该方法用于获取查询条件，用户在缺省实现中可以对该方法进行修改。
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == nc.vo.dip.roleandtable.DipRoleAndTableBVO.class)
	   return "pk_roleandtable_h = '" + key + "' and isnull(dr,0)=0 ";
       	if(bodyClass == nc.vo.dip.roleandtable.DipRoleAndFunctionBVO.class)
     	   return "pk_roleandtable_h = '" + key + "' and isnull(dr,0)=0 ";	
	 return null;
       } 
	
}
