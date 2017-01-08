package nc.ui.bd.ref;

public class DataLCTreeRefModel extends  AbstractRefGridTreeModel{
	public DataLCTreeRefModel(){
		setRefNodeName();
	}
	public void setRefNodeName(){
		setRefTitle("数据流程");
			setRootName("数据流程");
			setClassFieldCode(new String[]{"tcode","tname","typename","pk_sys","hpk"});
			setClassTableName("v_dip_sjlcfolder");
			setClassWherePart("nvl(dr,0)=0");
			setFatherField("pk_sys");
			setChildField("hpk");
			setClassJoinField("hpk");
			setClassDefaultFieldCount(3);
			setClassOrderPart("typename ");
			
			
			setDocJoinField("v_dip_sjlc.pk_sys");
			setFieldCode(new String[]{"v_dip_sjlc.tcode","v_dip_sjlc.tname","v_dip_sjlc.typename"});
			setFieldName(new String[]{"编码","名称","流程类型"});
			setTableName("v_dip_sjlc");
			setHiddenFieldCode(new String[]{"v_dip_sjlc.pk_sys","v_dip_sjlc.hpk"});
			setPkFieldCode("v_dip_sjlc.hpk");
			setWherePart("nvl(dr,0)=0" );
		}
}
