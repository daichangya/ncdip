package nc.ui.bd.ref;
//<nc.ui.bd.ref.ReturnBMsgRefModel>
public class ReturnBMsgRefModel  extends AbstractRefModel {

	//	���ò���ʾ�ֶΣ��� ����
	public String[]getHiddenFieldCode(){
//		return new String[]{"dip_dataorigin.pk_dataorigin"};
		return new String[]{"pk_returnmess_b"};
	}
	public ReturnBMsgRefModel(){
		super();
	}
	//����select�Ӿ䣬��ʾ���롢��ʾ���ơ������ֶ�
	public String[] getFieldCode(){
//		return new String[]{"dip_dataorigin.code","dip_dataorigin.name"};
		return new String[]{"code","def_str_3","content","def_str_1","def_str_2"};
	}
	//��ʾ�������ƣ���ͷ����Ŀ��
	public String[] getFieldName(){
		return new String[]{
				"����","����","��־ͷ"	,"��־��","��־β"	
		};
		}
		//���մ������
		public String getRefTitle(){
			return "��ִ���������Ϣ";
		}

		//����������form�Ӿ�
		public String getTableName(){
			return "dip_returnmess_b";
		}

	
	//�趨�����ֶΣ�������getHiddenFieldCode��setFieldCode���趨
	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "pk_returnmess_b" ;
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
