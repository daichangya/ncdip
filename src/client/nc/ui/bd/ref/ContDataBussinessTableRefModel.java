package nc.ui.bd.ref;

/**
 * liyunzhe ���ݶ��������β��գ�����ѡ���Ľṹ�Ĳ��ն��������������
 * @author Administrator
 *
 */
public class ContDataBussinessTableRefModel extends AbstractRefGridTreeModel{

	
	public ContDataBussinessTableRefModel(){
		setRefNodeName( );
	}
	public void setRefNodeName(){
		setRefTitle("���ݶ����");
			setRootName("���ݶ���");
			setClassFieldCode(new String[]{"syscode","sysname","pk_datadefinit_h","pk_xt","pk_sysregister_h","iscreatetab","isfolder"});
			setClassTableName("dip_datadefinit_h");
			setClassWherePart("  nvl(dr,0)=0 and (isfolder='Y') )"+
							  "  union all "+/**/
							  "  select tt.syscode,tt.sysname,tt.pk,tt.pk,'',  '','Y' from v_dip_datadefinit tt  where nvl(dr,0)=0  AND ( 1=1 ");
			setFatherField("pk_sysregister_h");
			setChildField("pk_datadefinit_h");
			setClassJoinField("pk_datadefinit_h");
			setClassDefaultFieldCount(2);
			
			setDocJoinField("pk_sysregister_h");
			setFieldCode(new String[]{"syscode","sysname","memorytable","memorytype","pk_datadefinit_h","pk_sysregister_h"});
			setFieldName(new String[]{"����","����","�洢����","�洢����"});
			setTableName("dip_datadefinit_h");
			setHiddenFieldCode(new String[]{"pk_datadefinit_h"});
			setPkFieldCode("pk_datadefinit_h");
			setWherePart(" nvl(dr,0)=0  and iscreatetab='Y'  and tabsoucetype='�Զ���' ");
			
		}
//	}
}
