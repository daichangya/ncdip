package nc.ui.bd.ref;

public class DataFormateCopyTreeRefModel extends AbstractRefGridTreeModel{



//	public ContDataBussinessTableRefModel(String nodeName){
//		setRefNodeName( nodeName);
//	}
	
	public DataFormateCopyTreeRefModel(){
		setRefNodeName( );
	}
	public void setRefNodeName(){
//		m_strRefNodeName=nodeName;
		
//		if(m_strRefNodeName.equals("业务表")){
		setRefTitle("数据接收格式定义");
			setRootName("格式定义");
			setClassFieldCode(new String[]{"vcode","vname","pk","fpk"});
			setClassTableName("v_dip_copydataformat");
			setClassWherePart("  nvl(dr,0)=0 ");
			setFatherField("fpk");
			setChildField("pk");
			setClassJoinField("pk");
			setClassDefaultFieldCount(2);
			
//			setClassOrderPart(" ");
			
			setDocJoinField("fpk");
			setFieldCode(new String[]{"dip_datarec_h.syscode","dip_datarec_h.recname","dip_datarec_h.dataname","dip_datarec_h.ioflag","dip_dataorigin.name"," dip_messtype.name"});
			setFieldName(new String[]{"编码","名称","业务物理表","输入输出标志","数据来源类型","消息类型"});
			setTableName(" dip_datarec_h  left join dip_dataformatdef_h  on dip_datarec_h.pk_datarec_h = dip_dataformatdef_h.pk_datarec_h"+
					     " left join dip_dataorigin  on dip_dataorigin.pk_dataorigin=dip_datarec_h.sourcetype "+
					     " left join dip_messtype on dip_messtype.pk_messtype=dip_dataformatdef_h.messflowtype ");
			setHiddenFieldCode(new String[]{"dip_datarec_h.pk_datarec_h","dip_dataformatdef_h.pk_dataformatdef_h"});
			setPkFieldCode("dip_dataformatdef_h.pk_dataformatdef_h");
			//setWherePart(" nvl(dr,0)=0  and pk_sysregister_h="+getPkValue());
//			setWherePart(" nvl(dr,0)=0  and pk_datarec_h='"+getPkValue()+"'");
			setWherePart(" nvl(dip_datarec_h.dr,0)=0  and nvl( dip_dataformatdef_h.dr,0)=0 and nvl(dip_dataorigin.dr,0)=0  and nvl(dip_messtype.dr,0)=0"  );
//			UIRefPane ref=new UIRefPane();
//			ref.setRefModel(model)
			
			//setRefQueryDlgClaseName("nc.ui.bd.psndoc.PsnQueryDlg");
		}
//	}

}

