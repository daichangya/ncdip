package nc.ui.dip.dataformatdef;

import java.util.Hashtable;


/**
 *
 *�����ҵ�������
 *
 * @author author
 * @version tempProject version
 */
public abstract class AbstractMyDelegator extends nc.ui.trade.bsdelegate.BDBusinessDelegator {

//	public Hashtable loadChildDataAry(String[] tableCodes,String key) 
//	                                                 throws Exception{
//		
//	   Hashtable dataHashTable = new Hashtable();
//	                         
//           nc.vo.jyprj.returnmess.DipReturnmessBVO[] bodyVOs1 =
//                       (nc.vo.jyprj.returnmess.DipReturnmessBVO[])queryByCondition(nc.vo.jyprj.returnmess.DipReturnmessBVO.class,
//                                                    getBodyCondition(nc.vo.jyprj.returnmess.DipReturnmessBVO.class,key));   
//            if(bodyVOs1 != null && bodyVOs1.length > 0){
//                          
//              dataHashTable.put("dip_returnmess_b",bodyVOs1);
//            } 
//	         
//	   	   return dataHashTable;
//		
//	}
	
	public Hashtable loadChildDataAry(String[] tableCodes,String key) 
    throws Exception{

        Hashtable dataHashTable = new Hashtable();

         nc.vo.dip.dataformatdef.DataformatdefBVO[] bodyVOs1 =
         (nc.vo.dip.dataformatdef.DataformatdefBVO[])queryByCondition(nc.vo.dip.dataformatdef.DataformatdefBVO.class,
          getBodyCondition(nc.vo.dip.dataformatdef.DataformatdefBVO.class,key));   
if(bodyVOs1 != null && bodyVOs1.length > 0){

dataHashTable.put("dip_dataformatdef_b",bodyVOs1);
} 

return dataHashTable;

}
	
       /**
         *
         *�÷������ڻ�ȡ��ѯ�������û���ȱʡʵ���п��ԶԸ÷��������޸ġ�
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == nc.vo.dip.dataformatdef.DataformatdefBVO.class)
	   return "pk_datafornatdef_h = '" + key + "' and isnull(dr,0)=0 ";
       		
	 return null;
       } 

	
}
