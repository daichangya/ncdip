package nc.ui.bd.ref;

public class PubDatadictTreeRefModel extends AbstractRefGridTreeModel{
	public PubDatadictTreeRefModel(){
		setRefNodeName( );
	}
	public void setRefNodeName(){
//		m_strRefNodeName=nodeName;
		
//		if(m_strRefNodeName.equals("ҵ���")){
		setRefTitle("�����ֵ�");
			setRootName("�����ֵ�");
			setClassFieldCode(new String[]{"id" ,"display"});
			setClassTableName("pub_datadict");
			setClassWherePart(" kind='Folder' and nvl(dr,0)=0 ");
			setFatherField("parentguid");
			setChildField("id");
			setClassJoinField("id");
			setClassDefaultFieldCount(2);
//			setClassOrderPart(" ");
			
			setDocJoinField("parentguid");
			setFieldCode(new String[]{"id" ,"display"});
			setFieldName(new String[]{"����","����"});
			setTableName("pub_datadict");
			setHiddenFieldCode(new String[]{""});
			setPkFieldCode("id");
			//setWherePart(" nvl(dr,0)=0  and pk_sysregister_h="+getPkValue());
			setWherePart(" nvl(dr,0)=0 ");
//			UIRefPane ref=new UIRefPane();
//			ref.setRefModel(model)
			
			//setRefQueryDlgClaseName("nc.ui.bd.psndoc.PsnQueryDlg");
		}
}
