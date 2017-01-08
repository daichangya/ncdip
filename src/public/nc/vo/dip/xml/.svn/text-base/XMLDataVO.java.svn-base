package nc.vo.dip.xml;
import java.util.Hashtable;

import org.dom4j.Element;

import nc.vo.dip.in.PubDataVO;
import nc.vo.dip.in.RowDataVO;


public class XMLDataVO extends PubDataVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Hashtable<String, Integer> tab = new Hashtable<String, Integer>();
	
	public void readNode(int index, int row, Element e){
		if(getDataSize()<row+1){
			insertRow(row, new RowDataVO());
		}
		RowDataVO rowVo = getRowData(row);
		String key = e.getName();
		String value = e.getText();
		rowVo.setAttributeValue(key, value);
		if(!tab.containsKey(key)){
			pubKeyMap(key, index);
		}
	}
	
	public void pubKeyMap(String key, int index){
		tab.put(key, index);
	}
	
	public Integer getKeyIndex(String key){
		return tab.get(key);
	}
	
	public String[] getFieldKeys(){
		return tab.keySet().toArray(new String[0]);
	}
}
