package nc.ui.bd.ref;

/**
 * �ļ�����·��ע���Զ������
 * @author ����
 * 2011-4-14
 */
public class FilePathRefModel extends AbstractRefModel {

	//���ò���ʾ�ֶΣ��� ����
	public String[]getHiddenFieldCode(){
		return new String[]{"dip_filepath.pk_filepath"};		
	}
	
	public FilePathRefModel(){
		super();
	}
	
	//����select�Ӿ䣬��ʾ���롢�����ֶ�
	public String[] getFieldCode(){
		return new String[]{"dip_filepath.code","dip_filepath.name","dip_filepath.descriptions"};
	}
	
	//��ʾ�������ƣ���ͷ����Ŀ��
	public String[] getFieldName(){
		return new String[]{
				"�ļ�����·������","�ļ�����·������","�ļ�����·������"	
		};
	}
	
	//���մ������
	public String getRefTitle(){
		return "�ļ�����·��ע��";
	}
	
	//����������form�Ӿ�
	public String getTableName(){
		return "dip_filepath";
	}
	
	//�趨�����ֶΣ�������getHiddenFieldCode��setFieldCode���趨
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "dip_filepath.pk_filepath" ;
	}

	/**
	 * ��׼���Ʋ���ָ���������ļ�����·��ע��
	 * �趨where�Ӿ�
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
