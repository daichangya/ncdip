package nc.ui.bd.ref;

public class FtpSourceRegisterRefModel extends AbstractRefModel {

	//设置不显示字段：如 主键
	public String[]getHiddenFieldCode(){
		return new String[]{"dip_ftpsourceregister.pk_ftpsourceregister"};		
	}
	
	public FtpSourceRegisterRefModel(){
		super();
	}
	
	//设置select子句，显示编码、名称字段
	public String[] getFieldCode(){
		return new String[]{"dip_ftpsourceregister.code","dip_ftpsourceregister.name","dip_ftpsourceregister.address","dip_ftpsourceregister.username"};
	}
	
	//显示中文名称：表头和栏目用
	public String[] getFieldName(){
		return new String[]{
				"编码","名称","地址","用户名"	
		};
	}
	
	//参照窗体标题
	public String getRefTitle(){
		return "ftp数据源注册";
	}
	
	//表名：设置form子句
	public String getTableName(){
		return "dip_ftpsourceregister";
	}
	
	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_ftpsourceregister.pk_ftpsourceregister" ;
	}

	/**
	 * 标准名称查找指定档案：文件访问路径注册
	 * 设定where子句
	 */
	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		String where ="nvl(dr,0)=0";
		return where;
	}	
	
	@Override
	public int getDefaultFieldCount() {
		// TODO Auto-generated method stub
		return 4;
	}
}
