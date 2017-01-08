package nc.bs.dip.datacheck;

import nc.vo.dip.in.RowDataVO;

public abstract class AbstractDataValidator {
	
	private DataCheckProcessor validManager;

	public void setValidManager(DataCheckProcessor proc){
		validManager = proc;
	}
	
	public abstract String[] validate(RowDataVO rowVo);

	public DataCheckProcessor getValidManager() {
		return validManager;
	}
}
