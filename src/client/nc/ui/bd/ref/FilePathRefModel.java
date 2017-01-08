package nc.ui.bd.ref;

/**
 * 文件访问路径注册自定义参照
 * @author 刘洋
 * 2011-4-14
 */
public class FilePathRefModel extends AbstractRefModel {

	//设置不显示字段：如 主键
	public String[]getHiddenFieldCode(){
		return new String[]{"dip_filepath.pk_filepath"};		
	}
	
	public FilePathRefModel(){
		super();
	}
	
	//设置select子句，显示编码、名称字段
	public String[] getFieldCode(){
		return new String[]{"dip_filepath.code","dip_filepath.name","dip_filepath.descriptions"};
	}
	
	//显示中文名称：表头和栏目用
	public String[] getFieldName(){
		return new String[]{
				"文件访问路径编码","文件访问路径名称","文件访问路径属性"	
		};
	}
	
	//参照窗体标题
	public String getRefTitle(){
		return "文件访问路径注册";
	}
	
	//表名：设置form子句
	public String getTableName(){
		return "dip_filepath";
	}
	
	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_filepath.pk_filepath" ;
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
		return getFieldCode().length;
	}
}
