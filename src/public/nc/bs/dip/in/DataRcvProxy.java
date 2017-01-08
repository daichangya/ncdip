package nc.bs.dip.in;

import java.io.File;
import java.util.Hashtable;

import nc.bs.dip.excel.ExcelProcessor;
import nc.bs.dip.xml.XMLProcessor;
import nc.servlet.dip.pub.SynDataUtil;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.pub.AggregatedValueObject;

public class DataRcvProxy {
	private String sysflag ;
	private String pointflag ;
	private String billtype ;
	
	private AggregatedValueObject rcvConfig;
	private Hashtable<String, DipDatadefinitBVO> dataDef;
	private String[] dataChecker;
	/**
	 * @param arg0 - ϵͳ��ʶ
	 * @param arg1 - վ���ʶ
	 * @param arg2 - ҵ���ʶ
	 */
	public DataRcvProxy(String arg0, String arg1, String arg2){
		sysflag = arg0;
		pointflag = arg1;
		billtype = arg2;
	}
	
	/**
	 * ִ��������
	 */
	public void execute(){
		int tranType = (Integer)getDataRcv().getParentVO().getAttributeValue("tranType");//��������
		if(tranType == 1){  //��Ϣ
			doRcvMessage();
		}else if(tranType == 2){ //�ļ�
			doReadFile();
		}else if(tranType == 3){ //����ץȡ
			doSyncBasdoc();
		}else{
			doOtherProcess(tranType);
		}
	
	}
	
	protected void doOtherProcess(int tranType) {
		
	}

	private void doSyncBasdoc() {
	    String url = (String)getDataRcv().getParentVO().getAttributeValue("url");
		new SynDataUtil().execute(url);
	}

	private void doRcvMessage(){
		
	}
	
	private void doReadFile(){
		String filePath = (String)getDataRcv().getParentVO().getAttributeValue("filePath");
		File dir = new File(filePath);
		File[] file;
		if(dir.isDirectory()){
			file = dir.listFiles();
		}else{
			file = new File[]{dir};
		}
		
		AbstractDataProcessor proc;
		if(file[0].getName().endsWith(".xls")){
			proc = new ExcelProcessor(file, getDataRcv(), getDataDef(), getDataChecker());
		}else{
			proc = new XMLProcessor(file, getDataRcv(), getDataDef(), getDataChecker());
		}
		proc.execute();
	}
	
	public AggregatedValueObject getDataRcv(){
		if(rcvConfig == null){
			
		}
		return rcvConfig;
	}
	
	public Hashtable<String, DipDatadefinitBVO> getDataDef(){
		if(dataDef == null){
			
		}
		return dataDef;
	}
	
	public String[] getDataChecker(){
		if(dataChecker == null){
			
		}
		return dataChecker;
	}

	public String getBilltype() {
		return billtype;
	}

	public String getPointflag() {
		return pointflag;
	}

	public String getSysflag() {
		return sysflag;
	}
}
