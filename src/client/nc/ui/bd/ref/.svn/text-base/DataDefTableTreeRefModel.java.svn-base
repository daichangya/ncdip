package nc.ui.bd.ref;

public class DataDefTableTreeRefModel extends AbstractRefGridTreeModel{
	public DataDefTableTreeRefModel(){
		setRefNodeName( );
	}
	public void setRefNodeName(){
//		m_strRefNodeName=nodeName;
		
//		if(m_strRefNodeName.equals("业务表")){
		setRefTitle("引用表");
			setRootName("引用表");
			setClassFieldCode(new String[]{"syscode" ,"sysname","pk_datadefinit_h","pkfath"});
			setClassTableName("v_dip_quotetable");
			setClassWherePart("  ");
			setFatherField("pkfath");
			setChildField("pk_datadefinit_h");
			setClassJoinField("pk_datadefinit_h");
			setClassDefaultFieldCount(2);
//			setClassOrderPart(" ");
			
			setDocJoinField("pk_datadefinit_h");
			setFieldCode(new String[]{"syscode","sysname","memorytable"});//,"cname" ,"ename"
			setFieldName(new String[]{"编码","名称","物理表名"});//,"中文字段","英文字段"
			setTableName("v_dip_jgyyzdtree");
			setHiddenFieldCode(new String[]{"pk_datadefinit_h1","pk_datadefinit_h"});
			setPkFieldCode("pk_datadefinit_h1");
			//setWherePart(" nvl(dr,0)=0  and pk_sysregister_h="+getPkValue());
			setWherePart(" ");
			setDefaultFieldCount(5);
			setRefCodeField("memorytable");
//			setRefShowNameField("memorytable");
//			String str[]=new String[]{"memorytable","memorytable"};
//			setBlurFields(str);
//			set
//			UIRefPane ref=new UIRefPane();
//			ref.setRefModel(model)
			
			//setRefQueryDlgClaseName("nc.ui.bd.psndoc.PsnQueryDlg");
		}
}
