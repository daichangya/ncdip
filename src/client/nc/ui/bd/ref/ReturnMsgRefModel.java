package nc.ui.bd.ref;
//<nc.ui.bd.ref.ReturnMsgRefModel>
public class ReturnMsgRefModel  extends AbstractRefModel {

	//	���ò���ʾ�ֶΣ��� ����
	public String[]getHiddenFieldCode(){
//		return new String[]{"dip_dataorigin.pk_dataorigin"};
		return new String[]{"pk_returnmess_h"};
	}
	public ReturnMsgRefModel(){
		super();
	}
	//����select�Ӿ䣬��ʾ���롢��ʾ���ơ������ֶ�
	public String[] getFieldCode(){
//		return new String[]{"dip_dataorigin.code","dip_dataorigin.name"};
		return new String[]{"code","name","propty"};
	}
	//��ʾ�������ƣ���ͷ����Ŀ��
	public String[] getFieldName(){
		return new String[]{
				"����","����"	,"����"	
		};
		}
		//���մ������
		public String getRefTitle(){
			return "��ִ�������";
		}

		//����������form�Ӿ�
		public String getTableName(){
			return "dip_returnmess_h";
		}

	
	//�趨�����ֶΣ�������getHiddenFieldCode��setFieldCode���趨
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "pk_returnmess_h" ;
	}
	/**
	 * ��׼���Ʋ���ָ��������������Դ����
	 * �趨where�Ӿ�
	 * ����������д����÷�����myeven�����оͲ���Ϊ������������
	 */
	public  String getWherePart(){
		String where=" nvl(dr,0)=0";
		return where;
	}

		
	
	

}
