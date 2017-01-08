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
	 * @desc ��������������ͬ����У��
	 * @param ob[0] TxtDataVO txtdvo
	 * @param ob[1] DipDatarecHVO hvo
	 * @param ob[2] Hashtable<String, DipDatadefinitBVO> datadefinb
	 * @param ob[3] String filepath ��־�ļ�·��
	 * @param ob[4] boolean �Ƿ�����û���ҵ����յ���
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
		HashMap mapVOs = (HashMap) hvo.getAttributeValue("map");// ���ձ� ������1��ʼ��
		String pk = (datadefinb.get("#PKFIELD#")==null?"":datadefinb.get("#PKFIELD#").getEname());// �洢�������
		String pk_sys=(datadefinb.get("#SYSPKFIELD#")==null?"":datadefinb.get("#SYSPKFIELD#").getEname());// �洢���Ԥ������
		String tabname = hvo.getTablename();// �洢����
		int mapType = (Integer) hvo.getAttributeValue("datamaptype");// ���շ�ʽ
		List err=new ArrayList();
		List<RowDataVO> succData=new ArrayList<RowDataVO>();
		Map fieldmap = txtdvo.getTitlemap();// �ļ���߶���ͷ  ��0��ʼ
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
					cm.setMessage("�ļ��е�����["+fieldmap.size()+"]��ͬ����������["+mapVOs.size()+"]�����");
					cm.setAllPrexCheckTile(false);
					return cm;
				}
			}
			
			for(int k=0;k<mapVOs.size();k++){
				String colno = k+"";
				if(mapType==1){
					if(fieldmap.get((k+temp)+"")==null){
						if(isscrip){//webservice�й�ϵ�������Ķ���false
							continue;
						}else{
							err.add("DataRecCheckPlugin û���ҵ���"+fieldmap.get(k+"")+"�ж�Ӧ���ֶ�");
							cm.setErrList(err);
							cm.setSuccessful(false);
							return cm;
						}
					}
					if(isAllPrexFlag){
						for(int w=0;w<fieldmap.size();w++){
							if(fieldmap.get(w+"")==null){
								cm.setMessage("DataRecCheckPlugin �ļ��е�1�е�"+(w+1)+"�в���Ϊ��");
								cm.setSuccessful(false);
								cm.setAllPrexCheckTile(false);
								return cm;
							}
							if(!(fieldmap.get(w+"")!=null&&mapVOs.get(fieldmap.get(w+""))!=null)){
									cm.setMessage("DataRecCheckPlugin ���ն�����û���ҵ��ļ���("+fieldmap.get(w+"")+")��Ӧ���ֶ�");
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
					Logger.debug("��"+(j+1)+"�����ݣ�"+e.getMessage());
					err.add("DataRecCheckPlugin ��"+(j+1)+"�����ݣ�"+e.getMessage());
					if(filepath!=null){
						SJUtil.PrintLog(filepath, "doCheck", "DataRecCheckPlugin ��"+(j+1)+"�����ݵĵ�"+(k+1)+"���ֶ�У��ʧ�ܣ�field["+filedi+"]value["+value+"],type["+type+"]"+e.getMessage());
					}
					issucc=false;
					
					if(isAllPrexFlag){
							cm.setSuccessful(false);
							cm.setMessage("DataRecCheckPlugin ��"+(j+1)+"�����ݵĵ�"+(k+1)+"���ֶ�У��ʧ�ܣ�field["+filedi+"]value["+value+"],type["+type+"]"+e.getMessage());
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
			msg="���ֳɹ���";
		}else if(!iserr&&issucc){
			cm.setSuccessful(true);
			msg="ȫ���ɹ���";
		}else if(iserr&&!issucc){
			msg="ȫ��ʧ�ܣ�";
		}
		cm.setMessage(msg);
		return cm;
	}

	private Object doTrance(Object value,String filed,String pk,UFBoolean notnull,String type,Integer len,Integer count) throws Exception{
		if(filed==pk||(notnull!=null&&notnull.booleanValue())){
			if(value==null||value.toString().trim().equals("")){
				throw new Exception("�ֶ�'"+filed+"'����Ϊ��");
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
					throw new Exception("�ֶ�'"+filed+"'���ȴ��ڶ���ĳ���"+len);
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
					throw new Exception("�ֶ�'"+filed+"'�����Ͳ�����ֵ��");
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
					throw new Exception("�ֶ�'"+filed+"'�����Ͳ�����ֵ��");
				}
			}else if(type.equals("date")){
				if(value instanceof String){
					if(value.toString().matches("[0-9]{4}[-][0-9]{2}[-][0-9]{2}")){
						return value;
					}else{
						throw new Exception("�ֶ�'"+filed+"'�����Ͳ���������");
					}
				}else if(value instanceof UFDate){
					if(value.toString().matches("[0-9]{4}[-][0-9]{2}[-][0-9]{2}")){
						return value;
					}else{
						throw new Exception("�ֶ�'"+filed+"'�����Ͳ���������");
					}
				}else if(value instanceof UFDateTime){
					if(value.toString().matches("[0-9]{4}[-][0-9]{2}[-][0-9]{2}")){
						
					}else{
						throw new Exception("�ֶ�'"+filed+"'�����Ͳ���������");
					}
				}else{
					throw new Exception("�ֶ�'"+filed+"'�����Ͳ�����������");
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
