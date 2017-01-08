package nc.bs.dip.tyzhxml;

import java.util.Hashtable;
import java.util.List;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.ILogAndTabMonitorSys;
import nc.util.dip.sj.IContrastUtil;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.dip.filepath.FilepathVO;
import nc.vo.dip.tyxml.ConfigVO;
import nc.vo.dip.tyxml.DipTYXMLDatachangeHVO;

/**
 * @author Administrator
 * @拼装凭证转换需要的VO
 */
public class VOFormat {
	/**
	 * 子表标志 - 影响因素
	 */
	public static final int INFLUENCE = 1;
	public static final int COMBINE = 2;
	
	private VoucherDMO dmo = new VoucherDMO();
	private ConfigVO config;
	private ILogAndTabMonitorSys logUtil;
	public ILogAndTabMonitorSys getLogUtil(){
		if(logUtil == null){
			logUtil = (ILogAndTabMonitorSys)NCLocator.getInstance().lookup(ILogAndTabMonitorSys.class.getName());
		}
		return logUtil;
	}
	public void errLogOut(String log,String clue_id){
		
//		getLogUtil().writToDataLog_RequiresNew(logVo);
		
		getLogUtil().writToDataLog_RequiresNew(clue_id, IContrastUtil.YWLX_TYZHXML, log);//.writToDataLog_RequiresNew(logVo);
	
	}
	/**
	 * 取得公共配置信息，包括数据转换主表信息，和其他一些配置信息<br>
	 * <li>取得数据转换主表信息
	 * <li>翻译部分字段为完全属性：
	 * @param pk_tyzh
	 * @return
	 * @throws DAOException 
	 */
	@SuppressWarnings("unchecked")
	public ConfigVO getConfigVO(String pk_tyzh) throws DAOException{
		if (config == null) {
			DipTYXMLDatachangeHVO hvo = (DipTYXMLDatachangeHVO) dmo.getDAO()
					.retrieveByPK(DipTYXMLDatachangeHVO.class, pk_tyzh);
			config = new ConfigVO();
			config.setParent(hvo);
			//转换表的主键 pk_def
			String pk_def = hvo.getSourcetab();
			//找到转换表的主表 defhead
			DipDatadefinitHVO defhead = (DipDatadefinitHVO) dmo.getDAO()
					.retrieveByPK(DipDatadefinitHVO.class, pk_def);
			//找到转换表的附表List c
			List<DipDatadefinitBVO> c = (List<DipDatadefinitBVO>) dmo.getDAO().retrieveByClause(
					DipDatadefinitBVO.class,
					"pk_datadefinit_h = '" + pk_def + "'");
			if (c == null || c.size() == 0) {
				errLogOut("未找到数据转换对应的表的数据定义",pk_tyzh);
				return null;
			}
			Hashtable<String, DipDatadefinitBVO> tab = new Hashtable<String, DipDatadefinitBVO>();
			for (int i = 0; i < c.size(); i++) {
				DipDatadefinitBVO bvo = c.get(i);//[i];
				tab.put(bvo.getEname(), bvo);
				if(bvo.ispk != null && bvo.getIspk().booleanValue()){
					tab.put(TyzhXMLProcessor.PKFIELD_ATTRIBUTE, bvo);
				}
			}
			config.setPk_sourtab(defhead.getPrimaryKey());
			config.setDatadef(tab);
			//把数据转换的表头的vo的表名改为物理名称了
			config.getParent().setSourcetab(defhead.getMemorytable());
			FilepathVO filvo=(FilepathVO) dmo.getDAO().retrieveByPK(FilepathVO.class, hvo.getPkfilepath());
			if(filvo!=null){
				config.setFilepath(filvo.getDescriptions());
			}else{
				errLogOut("没有找到文件生成路径", pk_tyzh);
				return null;
			}
//			if(hvo.getRepdatactl().equals("0001AA10000000018UZ1")){
				if(hvo.getRepdatactl().equals(IContrastUtil.REPEATDATA_CONTROL_HULUE)){
				config.setIsfg(false);
			}else{
				config.setIsfg(true);
			}
		}		
		return config;
	}
}
