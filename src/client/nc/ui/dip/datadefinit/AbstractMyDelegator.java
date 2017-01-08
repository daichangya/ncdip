package nc.ui.dip.datadefinit;

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
	                         
           nc.vo.dip.datadefinit.DipDatadefinitBVO[] bodyVOs1 =
                       (nc.vo.dip.datadefinit.DipDatadefinitBVO[])queryByCondition(nc.vo.dip.datadefinit.DipDatadefinitBVO.class,
                                                    getBodyCondition(nc.vo.dip.datadefinit.DipDatadefinitBVO.class,key));   
            if(bodyVOs1 != null && bodyVOs1.length > 0){
                          
              dataHashTable.put("dip_datadefinit_b",bodyVOs1);
            } 
	         
	   	   return dataHashTable;
		
	}
	
	
       /**
         *
         *�÷������ڻ�ȡ��ѯ�������û���ȱʡʵ���п��ԶԸ÷��������޸ġ�
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == nc.vo.dip.datadefinit.DipDatadefinitBVO.class)
	   return "pk_datadefinit_h = '" + key + "' and isnull(dr,0)=0 ";
       		
	 return null;
       } 
	
}
