package nc.ui.dip.returnmess;

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
		
	   Hashtable dataHashTable = new Hashtable();
	                         
           nc.vo.dip.returnmess.DipReturnmessBVO[] bodyVOs1 =
                       (nc.vo.dip.returnmess.DipReturnmessBVO[])queryByCondition(nc.vo.dip.returnmess.DipReturnmessBVO.class,
                                                    getBodyCondition(nc.vo.dip.returnmess.DipReturnmessBVO.class,key));   
            if(bodyVOs1 != null && bodyVOs1.length > 0){
                          
              dataHashTable.put("dip_returnmess_b",bodyVOs1);
            } 
	         
	   	   return dataHashTable;
		
	}
	
	
       /**
         *
         *�÷������ڻ�ȡ��ѯ�������û���ȱʡʵ���п��ԶԸ÷��������޸ġ�
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == nc.vo.dip.returnmess.DipReturnmessBVO.class)
	   return "pk_returnmess_h = '" + key + "' and isnull(dr,0)=0 ";
       		
	 return null;
       } 
	
}
