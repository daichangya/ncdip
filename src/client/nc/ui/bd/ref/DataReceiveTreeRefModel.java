package nc.ui.bd.ref;
/**
 * liyunzhe 2012-06-04 add ������ ����ͬ���ڵ㣬ҵ����ֶβ����ࡣ
 * @author Administrator
 *
 */
public class DataReceiveTreeRefModel extends AbstractRefGridTreeModel{


	
	public DataReceiveTreeRefModel(){
		setRefNodeName( );
	}
	public void setRefNodeName(){
		setRefTitle("���ݽ��ն���");
			setRootName("���ݽ��ն���");
			setClassFieldCode(new String[]{"syscode","recname","pk_datarec_h","fpk","pk_xt"});
			setClassTableName("v_dip_datarech");
			setClassWherePart("");
			setFatherField("fpk");
			setChildField("pk_datarec_h");
			setClassJoinField("pk_datarec_h");
			setClassDefaultFieldCount(2);
			
			setDocJoinField("fpk");
			setFieldCode(new String[]{"dip_datarec_h.syscode ","dip_datarec_h.recname","dip_datarec_h.ioflag","dip_datadefinit_h.syscode","dip_datadefinit_h.sysname","dip_datadefinit_h.memorytable"});
			setFieldName(new String[]{"ͬ���������","ͬ����������","���������־","���ݶ������","���ݱ���","�������"});
			setTableName("dip_datarec_h left join dip_datadefinit_h on dip_datarec_h.memorytable=dip_datadefinit_h.pk_datadefinit_h ");
			setHiddenFieldCode(new String[]{"dip_datarec_h.pk_datarec_h"});
			setPkFieldCode("dip_datarec_h.pk_datarec_h");
			setWherePart(" nvl(dip_datarec_h.dr,0)=0 and nvl(dip_datadefinit_h.dr,0)=0 and nvl(dip_datarec_h.isfolder,'N')='N'");
			setDefaultFieldCount(getFieldCode().length);
		}

}
