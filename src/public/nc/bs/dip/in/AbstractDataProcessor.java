package nc.bs.dip.in;

import java.util.Hashtable;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.dip.datacheck.DataCheckProcessor;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.in.RowDataVO;
import nc.vo.pub.AggregatedValueObject;

public abstract class AbstractDataProcessor {
	private AggregatedValueObject rcvConfig;
	private String[] checkClsName;
	private Hashtable<String, DipDatadefinitBVO> fieldKey = new Hashtable<String, DipDatadefinitBVO>();
	private Object odata;
	
	public AbstractDataProcessor(Object data, AggregatedValueObject datarcv, Hashtable<String, DipDatadefinitBVO> datadef, String[] checkers){
		odata = data;
		rcvConfig = datarcv;
		checkClsName = checkers;
		fieldKey = datadef;
	}
	
	public Object getDataObject(){
		return odata;
	}
	
	public abstract RowDataVO[] rexecute() throws Exception;
	public abstract void execute();
	
	public RowDataVO[] checkData(RowDataVO[] rowVOs){
		DataCheckProcessor checkproc = new DataCheckProcessor(rowVOs,
				getFieldKey(), getCheckClsName());
		return checkproc.check();
	}
	
	public void savData(RowDataVO[] rowVOs) throws DAOException{
		if(rowVOs == null || rowVOs.length == 0){
			return;
		}
		
	}

	public String[] getCheckClsName() {
		return checkClsName;
	}

	public Hashtable<String, DipDatadefinitBVO> getFieldKey() {
		return fieldKey;
	}

	public AggregatedValueObject getRcvConfig() {
		return rcvConfig;
	}
	
	
}
