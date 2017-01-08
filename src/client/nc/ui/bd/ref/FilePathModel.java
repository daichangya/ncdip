package nc.ui.bd.ref;


public class FilePathModel extends AbstractRefModel {

	
	public FilePathModel(){
		super();

	}
	
	@Override
	public String[] getHiddenFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"dip_filepath.pk_filepath"};
	}

	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
		return "文件访问路径注册";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		
		return "dip_filepath";
	}

	@Override
	public String getWherePart() {
		// TODO Auto-generated method stub
		String where ="nvl(dr,0)=0";
		return where;
	}
	@Override
	public String[] getFieldCode() {
		// TODO Auto-generated method stub
		//2011-6-15 增加了"dip_filepath.descriptions"字段
		return new String[]{"dip_filepath.code","dip_filepath.name","dip_filepath.descriptions"};
	}
	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		//2011-6-15 增加了“文件属性”字段
		return new String[]{"文件访问路径编码","文件访问路径名称","文件属性"};
	}

	//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_filepath.pk_filepath";
	}
	@Override
	public void addWherePart(String newWherePart) {
		// TODO Auto-generated method stub
	
		super.addWherePart(newWherePart);
	}
	
	
	@Override
	public int getDefaultFieldCount() {
		// TODO Auto-generated method stub
		return getFieldCode().length;
	}
}
