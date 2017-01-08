package nc.ui.dip.datarec;

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
	                         
           nc.vo.dip.datarec.DipDatarecBVO[] bodyVOs1 =
                       (nc.vo.dip.datarec.DipDatarecBVO[])queryByCondition(nc.vo.dip.datarec.DipDatarecBVO.class,
                                                    getBodyCondition(nc.vo.dip.datarec.DipDatarecBVO.class,key));   
            if(bodyVOs1 != null && bodyVOs1.length > 0){
                          
              dataHashTable.put("dip_datarec_b",bodyVOs1);
            } 
	         
	   	   return dataHashTable;
		
	}
	
	
       /**
         *
         *�÷������ڻ�ȡ��ѯ�������û���ȱʡʵ���п��ԶԸ÷��������޸ġ�
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == nc.vo.dip.datarec.DipDatarecBVO.class)
	   return "pk_datarec_h = '" + key + "' and isnull(dr,0)=0 ";
       		
	 return null;
       } 
	
}
