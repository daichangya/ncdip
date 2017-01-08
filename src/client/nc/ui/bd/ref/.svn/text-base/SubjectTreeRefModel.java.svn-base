package nc.ui.bd.ref;

import nc.ui.bd.ref.AbstractRefTreeModel;
//<nc.ui.bd.ref.SubjectTreeRefModel>
public class SubjectTreeRefModel extends AbstractRefTreeModel{
	
	public SubjectTreeRefModel(){
		super();
//		formatRefByID(Constant.BUDGET_ITEMCLASS_PROJECT);
	}
	
	public String[] getFieldCode() {
        // TODO Auto-generated method stub
        return new String[]{
                "bd_accsubj.subjcode", "bd_accsubj.subjname"
        };
    }

    @Override
	public String[] getHiddenFieldCode() {
		// TODO Auto-generated method stub
    	return new String[]{
//                "bd_accsubj.pk_accsubj", 
                "bd_accsubj.pk_accsubj",
                "case when length(substr(bd_accsubj.subjcode,0,length(bd_accsubj.subjcode)-2))=2 then '' else  substr(bd_accsubj.subjcode,0,length(bd_accsubj.subjcode)-2) end fcode"
        };
	}

	public String[] getFieldName() {
        // TODO Auto-generated method stub
        return new String[]{
                "科目编码","科目名称"
        };
    }

    public String getPkFieldCode() {
        // TODO Auto-generated method stub
        return "bd_accsubj.subjcode";
    }

    public String getRefTitle() {
        // TODO Auto-generated method stub
        return this.getTitle();
    }

    private String getTitle() {
		// TODO Auto-generated method stub
		return "科目";
	}

	public String getTableName() {
        // TODO Auto-generated method stub
        return "bd_accsubj";
    }

	@Override
	public String getChildField() {
		// TODO Auto-generated method stub
		return "bd_accsubj.subjcode";
	}

	/**
	 * 职能部门和事业部的权限控制
	 *//*
	@Override
	public String getWherePart() {
		return " bd_accsubj.pk_glorgbook='0001S21000000000068N'";
	}*/

	@Override
	public boolean isNotLeafSelectedEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String getFatherField() {
		// TODO Auto-generated method stub
		return "case when length(substr(bd_accsubj.subjcode,0,length(bd_accsubj.subjcode)-2))=2 then '' else  substr(bd_accsubj.subjcode,0,length(bd_accsubj.subjcode)-2) end fcode";
	}	
}
