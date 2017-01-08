package nc.ui.bd.ref;

public class DataDefinitTableTreeRefModel extends AbstractRefGridTreeModel{


	
	public DataDefinitTableTreeRefModel(){
		setRefNodeName( );
	}
	public void setRefNodeName(){
		setRefTitle("数据定义表");
			setRootName("数据定义");
			setClassFieldCode(new String[]{"syscode","sysname","pk_datadefinit_h","pk_xt","pk_sysregister_h","iscreatetab","isfolder"});
			setClassTableName("v_dip_sysdefinittree");
			setClassWherePart(" isfolder='Y'");
			setFatherField("pk_sysregister_h");
			setChildField("pk_datadefinit_h");
			setClassJoinField("pk_datadefinit_h");
			setClassDefaultFieldCount(2);
			
			setDocJoinField("pk_sysregister_h");
			setFieldCode(new String[]{"syscode","sysname","memorytable","memorytype","tabsoucetype"});
			setFieldName(new String[]{"编码","名称","存储表名","存储类型","表来源类型"});
			setTableName("v_dip_sysdefinittree");
			setHiddenFieldCode(new String[]{"pk_datadefinit_h"});
			setPkFieldCode("pk_datadefinit_h");
			setWherePart(" iscreatetab='Y' ");
			setDefaultFieldCount(getFieldCode().length);
			
		}
//	}

}
