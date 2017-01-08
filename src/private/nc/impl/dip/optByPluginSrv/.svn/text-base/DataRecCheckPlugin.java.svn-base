package nc.impl.dip.optByPluginSrv;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import nc.bs.dip.txt.TxtDataVO;
import nc.bs.logging.Logger;
import nc.bs.pub.install.UFDate;
import nc.itf.dip.pub.checkplugin.ICheckPlugin;
import nc.util.dip.sj.CheckMessage;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.datarec.DipDatarecHVO;
import nc.vo.dip.in.PubDataVO;
import nc.vo.dip.in.RowDataVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class DataRecCheckPlugin implements ICheckPlugin{
	/**
	 * @desc 三个参数，数据同步的校验
	 * @param ob[0] TxtDataVO txtdvo
	 * @param ob[1] DipDatarecHVO hvo
	 * @param ob[2] Hashtable<String, DipDatadefinitBVO> datadefinb
	 * @param ob[3] String filepath 日志文件路径
	 * @param ob[4] boolean 是否跳过没有找到对照的列
	 * 
	 * */
	public CheckMessage doCheck(Object... ob) {
		CheckMessage cm=new CheckMessage();
		cm.setAllPrexCheckTile(true);
		boolean isAllPrexFlag=false;
		TxtDataVO txtdvo=(TxtDataVO) ob[0];
		DipDatarecHVO hvo=(DipDatarecHVO) ob[1];
		Hashtable<String, DipDatadefinitBVO> datadefinb=(Hashtable<String, DipDatadefinitBVO>) ob[2];
		String filepath=ob[3]==null?null:ob[3].toString();
		boolean isscrip=false;
		if(ob.length==5){
			isscrip=(Boolean) ob[4];
		}
		if(ob.length==6){
			isAllPrexFlag=(Boolean) ob[5];
		}
		HashMap mapVOs = (HashMap) hvo.getAttributeValue("map");// 对照表 行数从1开始，
		String pk = (datadefinb.get("#PKFIELD#")==null?"":datadefinb.get("#PKFIELD#").getEname());// 存储表的主键
		String pk_sys=(datadefinb.get("#SYSPKFIELD#")==null?"":datadefinb.get("#SYSPKFIELD#").getEname());// 存储表的预置主键
		String tabname = hvo.getTablename();// 存储表名
		int mapType = (Integer) hvo.getAttributeValue("datamaptype");// 对照方式
		List err=new ArrayList();
		List<RowDataVO> succData=new ArrayList<RowDataVO>();
		Map fieldmap = txtdvo.getTitlemap();// 文件里边读的头  从0开始
		for (int j = 0; j < txtdvo.getDataSize(); j++) {
			RowDataVO vo = new RowDataVO();
			vo.setTableName(tabname);
			if(pk!=null&&!pk.equals("")){
				vo.setPKField(pk);
			}
			
//			String[] fields = txtdvo.getFieldName();
			boolean issucc=true;
			int temp=0;
			if(mapType==1&&!fieldmap.containsKey("0")){
				temp=1;
			}
			
			if(isAllPrexFlag){
				if(fieldmap.size()!=mapVOs.size()){
					cm.setSuccessful(false);
					cm.setMessage("文件中的列数["+fieldmap.size()+"]和同步定义列数["+mapVOs.size()+"]不相等");
					cm.setAllPrexCheckTile(false);
					return cm;
				}
			}
			
			for(int k=0;k<mapVOs.size();k++){
				String colno = k+"";
				if(mapType==1){
					if(fieldmap.get((k+temp)+"")==null){
						if(isscrip){//webservice有关系。其他的都是false
							continue;
						}else{
							err.add("DataRecCheckPlugin 没有找到第"+fieldmap.get(k+"")+"列对应的字段");
							cm.setErrList(err);
							cm.setSuccessful(false);
							return cm;
						}
					}
					if(isAllPrexFlag){
						for(int w=0;w<fieldmap.size();w++){
							if(fieldmap.get(w+"")==null){
								cm.setMessage("DataRecCheckPlugin 文件中第1行第"+(w+1)+"列不能为空");
								cm.setSuccessful(false);
								cm.setAllPrexCheckTile(false);
								return cm;
							}
							if(!(fieldmap.get(w+"")!=null&&mapVOs.get(fieldmap.get(w+""))!=null)){
									cm.setMessage("DataRecCheckPlugin 对照定义中没有找到文件中("+fieldmap.get(w+"")+")对应的字段");
									cm.setSuccessful(false);
									cm.setAllPrexCheckTile(false);
									return cm;
							}
						}
					}
				}
					
				
				//String key = mapType == PubDataVO.FIELD_MATCH_KEY ? mapVOs.get(fieldmap.get(colno)).toString() : colno;
				String key =  colno;
				if(txtdvo.getRowData(j).getTab()!=null&&txtdvo.getRowData(j).getTab().size()==0){
					continue;
				}
				Object value = txtdvo.getRowData(j).getAttributeValue(colno);
				//String filedi=mapType==0?mapVOs.get(k+1+"").toString():key;
				String filedi="";
				if(mapType==0){
					filedi=mapVOs.get(k+1+"")==null?"":mapVOs.get(k+1+"").toString();
				}else{
					filedi=mapVOs.get(fieldmap.get((k+temp)+""))==null?"":mapVOs.get(fieldmap.get((k+temp)+"")).toString();
					if(!mapVOs.containsKey(fieldmap.get((k+temp)+""))){
						filedi=mapVOs.get((k+temp)+"")==null?"":mapVOs.get((k+temp)+"").toString();
					}
				}
				if(datadefinb.get(filedi)==null){
					continue;
				}
				String type= datadefinb.get(filedi).getType();
				Integer count=datadefinb.get(filedi).getDeciplace();
				Integer len=datadefinb.get(filedi).getLength();
				UFBoolean notnull=datadefinb.get(filedi).getIsimport();
				try{
					if(!(pk_sys!=null&&pk_sys.trim().length()>0&&pk_sys.equals(filedi))){
						Object value1=doTrance(value,filedi,pk,notnull,type,len,count);
						if(value1!=null){
							vo.setAttributeValue(filedi, value1);
						}
					}
				}catch(Exception e){
					Logger.debug("第"+(j+1)+"行数据，"+e.getMessage());
					err.add("DataRecCheckPlugin 第"+(j+1)+"行数据，"+e.getMessage());
					if(filepath!=null){
						SJUtil.PrintLog(filepath, "doCheck", "DataRecCheckPlugin 第"+(j+1)+"行数据的第"+(k+1)+"个字段校验失败，field["+filedi+"]value["+value+"],type["+type+"]"+e.getMessage());
					}
					issucc=false;
					
					if(isAllPrexFlag){
							cm.setSuccessful(false);
							cm.setMessage("DataRecCheckPlugin 第"+(j+1)+"行数据的第"+(k+1)+"个字段校验失败，field["+filedi+"]value["+value+"],type["+type+"]"+e.getMessage());
							cm.setAllPrexCheckTile(false);
							return cm;
					}
					
					
					break;
				}
			}
			if(issucc){
				succData.add(vo);
			}

		}
		boolean iserr=false;
		boolean issucc=false;
		if(err!=null&&err.size()>0){
			cm.setErrList(err);
			cm.setSuccessful(false);
			iserr=true;
		}
		if(succData!=null&&succData.size()>0){
			cm.setValidData(succData.toArray(new RowDataVO[succData.size()]));
			issucc=true;
		}
		String msg="";
		if(iserr&&issucc){
			msg="部分成功！";
		}else if(!iserr&&issucc){
			cm.setSuccessful(true);
			msg="全部成功！";
		}else if(iserr&&!issucc){
			msg="全部失败！";
		}
		cm.setMessage(msg);
		return cm;
	}

	private Object doTrance(Object value,String filed,String pk,UFBoolean notnull,String type,Integer len,Integer count) throws Exception{
		if(filed==pk||(notnull!=null&&notnull.booleanValue())){
			if(value==null||value.toString().trim().equals("")){
				throw new Exception("字段'"+filed+"'不能为空");
			}
		}
		if(value!=null){
			type=type.toLowerCase();
			if(type.contains("char")){
				String ret;
				if(value instanceof String) {
					ret=(String) value;
				}else{
					ret=value.toString();
				}
				if(ret!=null&&ret.length()>len){
					throw new Exception("字段'"+filed+"'长度大于定义的长度"+len);
				}
			}else if(type.equals("integer")||type.equals("smallint")){
				if(value instanceof Integer){
					return value;
				}else if(value.toString().matches("[0-9]*\\.*[0-9]*")){
					if(value.toString().equals("")){
						return null;
					}else{
						return Integer.parseInt(value.toString());
					}
				}else{
					throw new Exception("字段'"+filed+"'的类型不是数值型");
				}
			}else if(type.equals("number")||type.equals("long")||type.equals("decimal")){
				if(value instanceof UFDouble||value instanceof Integer||value instanceof BigDecimal){
					return value;
				}else if(value.toString().matches("[0-9]*\\.*[0-9]*")){
					if(value.toString().equals("")){
						return null;
					}else{
						return Double.parseDouble(value.toString());
					}
				}else{
					throw new Exception("字段'"+filed+"'的类型不是数值型");
				}
			}else if(type.equals("date")){
				if(value instanceof String){
					if(value.toString().matches("[0-9]{4}[-][0-9]{2}[-][0-9]{2}")){
						return value;
					}else{
						throw new Exception("字段'"+filed+"'的类型不是日期型");
					}
				}else if(value instanceof UFDate){
					if(value.toString().matches("[0-9]{4}[-][0-9]{2}[-][0-9]{2}")){
						return value;
					}else{
						throw new Exception("字段'"+filed+"'的类型不是日期型");
					}
				}else if(value instanceof UFDateTime){
					if(value.toString().matches("[0-9]{4}[-][0-9]{2}[-][0-9]{2}")){
						
					}else{
						throw new Exception("字段'"+filed+"'的类型不是日期型");
					}
				}else{
					throw new Exception("字段'"+filed+"'的类型不是日期类型");
				}
			}
		}
		return value;
	}
	public static void main(String[] args) {
//		System.out.println(new Integer("11.2"));
		String value="2001-05-13";
		System.out.println(value.toString().matches("[0-9]{4}[-][0-9]{2}[-][0-9]{2}"));
	}
}
