package nc.ui.dip.statemanage;

import java.util.Hashtable;




/**
 *
 *�����ҵ�������
 *
 * @author author
 * @version tempProject version
 */
public abstract class AbstractMyDelegator extends nc.ui.trade.bsdelegate.BDBusinessDelegator {

	public Hashtable loadChildDataAry(String[] tableCodes,String key) 
	                                                 throws Exception{
//		
//	   Hashtable dataHashTable = new Hashtable();
//	                         
//           nc.vo.dip.dataproce.DipDataproceBVO[] bodyVOs1 =
//                       (nc.vo.dip.dataproce.DipDataproceBVO[])queryByCondition(nc.vo.dip.dataproce.DipDataproceBVO.class,
//                                                    getBodyCondition(nc.vo.dip.dataproce.DipDataproceBVO.class,key));   
//            if(bodyVOs1 != null && bodyVOs1.length > 0){
//                          
//              dataHashTable.put("dip_dataproce_b",bodyVOs1);
//            } 
	         
	   	   return super.loadChildDataAry(tableCodes, key);
		
	}
	
	
       /**
         *
         *�÷������ڻ�ȡ��ѯ�������û���ȱʡʵ���п��ԶԸ÷��������޸ġ�
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == nc.vo.dip.statemanage.DipStateManageHVO.class)
	   return "pk_statemanage_h = '" + key + "' and isnull(dr,0)=0 ";
       		
	 return null;
       } 
	
}
