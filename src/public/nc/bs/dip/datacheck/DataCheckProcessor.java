package nc.bs.dip.datacheck;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.in.RowDataVO;

public class DataCheckProcessor {
	private RowDataVO[] rowVOs;
	private Hashtable<String, DipDatadefinitBVO> fieldKey = new Hashtable<String, DipDatadefinitBVO>();
	private String[] usableValidator;
	private List<RowDataVO> failList;

	public DataCheckProcessor(RowDataVO[] row, Hashtable<String, DipDatadefinitBVO> tab, String[] checkClsName){
		rowVOs = row;
		fieldKey = tab;	
		usableValidator = checkClsName;
	}

	public RowDataVO[] checkBasic(){
		BasicDataValidator valid = new BasicDataValidator();
		valid.setValidManager(this);
		
		ArrayList<RowDataVO> list = new ArrayList<RowDataVO>();
		for(int i=0;i<rowVOs.length;i++){
			String[] errmsg = valid.validate(rowVOs[i]);
			if(errmsg == null){
				list.add(rowVOs[i]);
			}else{
				rowVOs[i].setErrorType(errmsg[0]);
				rowVOs[i].setErrorMsg(errmsg[1]);
				failList.add(rowVOs[i]);
			}
		}
		return list.toArray(new RowDataVO[0]);
	}

	public RowDataVO[] check(){
		RowDataVO[] basic = checkBasic();
		if(basic == null || basic.length == 0){
			return null;
		}

		String[] classes = getUsableValidator();
		if(classes == null || classes.length == 0){
			return basic;
		}

		ArrayList<RowDataVO> rst = new ArrayList<RowDataVO>();

		for(String className:classes){
			try {
				Class newcls = Class.forName(className);
				Object o = newcls.newInstance();
				if(o instanceof AbstractDataValidator){
					if(rst.size() > 0){
						basic = rst.toArray(new RowDataVO[0]);
					}
					for (int i = 0; i < basic.length; i++) {
						AbstractDataValidator valid = (AbstractDataValidator)o;
						String[] msg = valid.validate(basic[i]);
						if(msg == null){
							rst.add(basic[i]);
						}else{
							basic[i].setErrorType(msg[0]);
							basic[i].setErrorMsg(msg[1]);
							failList.add(basic[i]);
						}
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch(Exception e){
				e.printStackTrace();
			}
		}

		return rst.toArray(new RowDataVO[0]);
	}

	private String[] getUsableValidator(){
		return usableValidator;
	}

	public Hashtable<String, DipDatadefinitBVO> getFieldKey() {
		return fieldKey;
	}
}
