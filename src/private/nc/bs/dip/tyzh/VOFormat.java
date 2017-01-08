package nc.bs.dip.tyzh;

import java.util.Hashtable;
import java.util.List;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.ILogAndTabMonitorSys;
import nc.itf.dip.pub.IQueryField;
import nc.util.dip.sj.IContrastUtil;
import nc.vo.dip.contdata.DipContdataVO;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.dip.tyzhq.DipTYZHDatachangeHVO;
import nc.vo.dip.tyzhq.ConfigVO;

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
		
		getLogUtil().writToDataLog_RequiresNew(clue_id, IContrastUtil.YWLX_TYZH, log);//.writToDataLog_RequiresNew(logVo);
	
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
			DipTYZHDatachangeHVO hvo = (DipTYZHDatachangeHVO) dmo.getDAO()
					.retrieveByPK(DipTYZHDatachangeHVO.class, pk_tyzh);
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
					tab.put(TyzhProcessor.PKFIELD_ATTRIBUTE, bvo);
				}
			}
			config.setPk_sourtab(defhead.getPrimaryKey());
			config.setDatadef(tab);
			//把数据转换的表头的vo的表名改为物理名称了
			config.getParent().setSourcetab(defhead.getMemorytable());
			
			DipDatadefinitHVO thvo=(DipDatadefinitHVO) dmo.getDAO().retrieveByPK(DipDatadefinitHVO.class, hvo.getTargettab());
			List<DipDatadefinitBVO> c1 = (List<DipDatadefinitBVO>) dmo.getDAO().retrieveByClause(
					DipDatadefinitBVO.class,
					"pk_datadefinit_h = '" + hvo.getTargettab() + "' and ispk='Y'");
			if(c1!=null&&c1.size()>0){
				config.setTagpk(c1.get(0).getEname());
			}
//			if(hvo.getRepdatactl().equals("0001AA10000000018UZ1")){
			//liyunzhe modify 2012-6-13 修改重复数据控制基础档案信息判断
			if(hvo.getRepdatactl().equals(IContrastUtil.REPEATDATA_CONTROL_HULUE)){
				config.setIsfg(false);
			}else{
				config.setIsfg(true);
			}
			config.getParent().setTargettab(thvo.getMemorytable());
		}		
		return config;
	}
	
	
	
	/**
	 * 得到组织机构的对照表。
	 * @param clue_id
	 * @return <外系统组织机构表主键, PK_CORP>
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	public void getOrgMap(String clue_id) throws DAOException{
//		ConfigVO vo = getConfigVO(clue_id); 
		//获取组织字段 字段VO的pk
		DipDatadefinitBVO orgField = config.getDatadef().get("");  //获取组织字段 字段VO
		String orgTable = orgField.getQuotetable();  //引用的业务数据组织档案表 表名
//		String orgIDField = orgField.getQuotecolu();
		String NCOrgTab = "bd_corp".toUpperCase();
		List<DipContdataVO> c = (List<DipContdataVO>) dmo.getDAO().retrieveByClause(DipContdataVO.class, " extetabname = '"+NCOrgTab+"' and contcolcode = '"+orgTable+"'");
		if(c == null || c.size() <= 0){
			return;
		}
		DipContdataVO mapVo = c.get(0);
		config.setCdata(mapVo);
		String mapTab = mapVo.getMiddletab();
		IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
		String tname=null;
		try {
			tname=iqf.createVoucherPkTempTable(orgField,config.getParent().getSourcetab(),  mapTab);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(tname==null||tname.length()<=0){
			throw new DAOException("创建组织对照临时表出错");
		}
		config.setTemptablename(tname);
		/*String sql = "select contpk, extepk from " + mapTab + " where nvl(dr,0) = 0 and contpk is not null and extepk is not null";
		List list = (List)dmo.getDAO().executeQuery(sql, new MapListProcessor());
		if(list == null || list.size() == 0){
			//mapTab表没有维护对照关系
			return;
		}
		
		Hashtable<String, String> fieldMap = new Hashtable<String, String>();
		for(int i=0;i<list.size();i++){
			Map map = (Map)list.get(i);
			fieldMap.put(((String)map.get("contpk")).trim(), ((String)map.get("extepk")).trim());
		}
		config.setOrgMap(fieldMap);*/
	}
}
