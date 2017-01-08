package nc.bs.excel.pub;

import java.util.HashMap;
import java.util.Map;

import nc.vo.pub.SuperVO;

public class ExpExcelVO extends SuperVO{
	private Map map=new HashMap<String, Object>();
	@Override
	public Object getAttributeValue(String arg0) {
		return map.get(arg0);
	}
	@Override
	public void setAttributeValue(String arg0, Object arg1) {
		map.put(arg0, arg1);
	}
	private String line1;
	private String line2;
	private String line3;
	/*private String line4;
	private String line5;
	private String line6;
	private String line7;
	private String line8;
	private String line9;
	private String line10;
	private String line11;
	private String line12;
	private String line13;
	private String line14;
	private String line15;
	private String line16;
	private String line17;
	private String line18;
	private String line19;
	private String line20;
	private String line21;
	private String line22;
	private String line23;
	private String line24;
	private String line25;
	private String line26;
	private String line27;
	private String line28;
	private String line29;
	private String line30;
	private String line31;
	private String line32;
	private String line33;
	private String line34;
	private String line35;
	private String line36;
	private String line37;
	private String line38;
	private String line39;
	private String line40;
	private String line41;
	private String line42;
	private String line43;
	private String line44;
	private String line45;
	private String line46;
	private String line47;
	private String line48;
	private String line49;
	private String line50;
	private String line51;
	private String line52;
	private String line53;
	private String line54;
	private String line55;
	private String line56;
	private String line57;
	private String line58;
	private String line59;
	private String line60;
	private String line61;
	private String line62;
	private String line63;
	private String line64;
	private String line65;
	private String line66;
	private String line67;
	private String line68;
	private String line69;
	private String line70;*/
	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	

}
