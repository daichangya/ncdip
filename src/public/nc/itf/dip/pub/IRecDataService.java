package nc.itf.dip.pub;

import nc.vo.pub.BusinessException;
/**
 * @desc 数据发送的时候使用呢
 * */
public interface IRecDataService {

	public String queDataRec(String flag) throws BusinessException;
	
	public String dataToCbur(String dwbm) throws BusinessException;
	
	public String dataToCbured(String dwbm)throws BusinessException;
	
	public String cbuToXML(String dwbm) throws BusinessException;
	
	public String cbuToXMLed(String dwbm) throws BusinessException;
	
	public String xmlToVou(String dwbm) throws BusinessException;
	
	public String xmlToVoued(String dwbm) throws BusinessException;
}
