package nc.ui.bd.ref;
//<nc.ui.bd.ref.DataCopyRefModel>
/**
 * ���ݽ��ն����и�ʽ����ʹ�õĲ���
 * */
public class DataCopyRefModel extends AbstractRefModel{
	public String newWherePart="";
    public DataCopyRefModel(){
    	super();
    }

	@Override
	public String[] getFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"h.syscode","h.recname","h.dataname","h.ioflag"};
	}

	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		return new String[]{"����","����","ҵ�������","���������־"};
	}

	@Override
	public String[] getHiddenFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{"f.pk_dataformatdef_h"};
	}

	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "f.pk_dataformatdef_h";
	}

	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
		return "���ݽ��ո�ʽ����";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "dip_datarec_h h left join dip_dataformatdef_h f on h.pk_datarec_h = f.pk_datarec_h";
	}

//	@Override
//	public String getWherePart() {
//		// TODO Auto-generated method stub
//		return super.getWherePart() ;
//	}
//	@Override
//	public void addWherePart(String newWherePart) {
//		// TODO Auto-generated method stub
//	
//		super.addWherePart(newWherePart);
//	}
}
