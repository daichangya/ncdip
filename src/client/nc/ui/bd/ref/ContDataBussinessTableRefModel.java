package nc.ui.bd.ref;

/**
 * liyunzhe 数据定义表的树形参照，所有选择表的结构的参照都可以用这个参照
 * @author Administrator
 *
 */
public class ContDataBussinessTableRefModel extends AbstractRefGridTreeModel{

	
	public ContDataBussinessTableRefModel(){
		setRefNodeName( );
	}
	public void setRefNodeName(){
		setRefTitle("数据定义表");
			setRootName("数据定义");
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
			setFieldName(new String[]{"编码","名称","存储表名","存储类型"});
			setTableName("dip_datadefinit_h");
			setHiddenFieldCode(new String[]{"pk_datadefinit_h"});
			setPkFieldCode("pk_datadefinit_h");
			setWherePart(" nvl(dr,0)=0  and iscreatetab='Y'  and tabsoucetype='自定义' ");
			
		}
//	}
}
