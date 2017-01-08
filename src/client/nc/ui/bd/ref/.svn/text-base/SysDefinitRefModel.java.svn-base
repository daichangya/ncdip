package nc.ui.bd.ref;
/*
 * 系统注册主子表，数据定义主表的视图参照类
 * 2011-06-07
 * zlc*/
public class SysDefinitRefModel extends AbstractRefModel{

//	设置不显示字段：如 主键
	public String[]getHiddenFieldCode(){
		return new String[]{"v_dip_sysdefinit.pk_datadefinit_h"};		
	}
	
	@Override
	public int getDefaultFieldCount() {
		// TODO Auto-generated method stub
		return 6;
	}

	public SysDefinitRefModel(){
		super();
	}
	
	//设置select子句，显示编码、名称字段
	public String[] getFieldCode(){
		return new String[]{"v_dip_sysdefinit.syscode","v_dip_sysdefinit.sysname",
				"v_dip_sysdefinit.memorytable","v_dip_sysdefinit.memorytype",
				"v_dip_sysdefinit.sitecode","v_dip_sysdefinit.sitename"};
	}
	
	//显示中文名称：表头和栏目用
	public String[] getFieldName(){
		return new String[]{
				"编码","存储表名","存储表物理名","存储类型","分部站点编码","分部站点名称"
		};
	}
	
	//参照窗体标题
	public String getRefTitle(){
		return "系统定义类型";
	}
	
	//表名：设置form子句
	public String getTableName(){
		return "v_dip_sysdefinit";
	}
	
	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "v_dip_sysdefinit.pk_datadefinit_h" ;
	}

	/**
	 * 标准名称查找指定档案：数据来源类型
	 * 设定where子句
	 */
	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		return super.getWherePart();
	}	
}
