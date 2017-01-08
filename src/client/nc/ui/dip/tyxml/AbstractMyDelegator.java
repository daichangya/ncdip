package nc.ui.dip.tyxml;

import java.util.Hashtable;

import nc.vo.dip.tyxml.DipTYXMLDatachangeBVO;




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
	                         
	   DipTYXMLDatachangeBVO[] bodyVOs1 =
                       (DipTYXMLDatachangeBVO[])queryByCondition(DipTYXMLDatachangeBVO.class,
                                                    getBodyCondition(DipTYXMLDatachangeBVO.class,key));   
            if(bodyVOs1 != null && bodyVOs1.length > 0){
                          
              dataHashTable.put("dip_tyxml_b",bodyVOs1);
            } 
	         
	   	   return dataHashTable;
		
	}
	
	
       /**
         *
         *�÷������ڻ�ȡ��ѯ�������û���ȱʡʵ���п��ԶԸ÷��������޸ġ�
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == DipTYXMLDatachangeBVO.class)
	   return " pk_tyxml_h = '" + key + "' and isnull(dr,0)=0 ";
       		
	 return null;
       } 
	
}
