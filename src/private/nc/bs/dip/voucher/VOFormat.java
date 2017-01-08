package nc.bs.dip.voucher;

import java.util.Hashtable;
import java.util.List;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.ILogAndTabMonitorSys;
import nc.itf.dip.pub.IQueryField;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.util.dip.sj.IContrastUtil;
import nc.vo.dip.contdata.DipContdataVO;
import nc.vo.dip.credence.CredenceBVO;
import nc.vo.dip.credence.CredenceHVO;
import nc.vo.dip.datachange.DipDatachangeBVO;
import nc.vo.dip.datachange.DipDatachangeHVO;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.dip.effectdef.CdSbodyVO;
import nc.vo.dip.filepath.FilepathVO;
import nc.vo.dip.voucher.ConfigVO;
import nc.vo.dip.voucher.TempletVO;

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
		
		getLogUtil().writToDataLog_RequiresNew(clue_id, IContrastUtil.YWLX_ZH, log);//.writToDataLog_RequiresNew(logVo);
	
	}
	/**
	 * 取得公共配置信息，包括数据转换主表信息，和其他一些配置信息<br>
	 * <li>取得数据转换主表信息
	 * <li>翻译部分字段为完全属性：
	 * @param clue_id
	 * @return
	 * @throws DAOException 
	 */
	@SuppressWarnings("unchecked")
	public ConfigVO getConfigVO(String clue_id) throws DAOException{
		if (config == null) {
			DipDatachangeHVO hvo = (DipDatachangeHVO) dmo.getDAO()
					.retrieveByPK(DipDatachangeHVO.class, clue_id);
			config = new ConfigVO();
			config.setParent(hvo);
			//转换表的主键 pk_def
			String pk_def = hvo.getBusidata();
			//找到转换表的主表 defhead
			DipDatadefinitHVO defhead = (DipDatadefinitHVO) dmo.getDAO()
					.retrieveByPK(DipDatadefinitHVO.class, pk_def);
			//找到转换表的附表List c
			List<DipDatadefinitBVO> c = (List<DipDatadefinitBVO>) dmo.getDAO().retrieveByClause(
					DipDatadefinitBVO.class,
					"pk_datadefinit_h = '" + pk_def + "'");
			if (c == null || c.size() == 0) {
				errLogOut("未找到数据转换对应的表的数据定义",clue_id);
				return null;
			}
			Hashtable<String, DipDatadefinitBVO> tab = new Hashtable<String, DipDatadefinitBVO>();
//			DipDatadefinitBVO[] bvos = (DipDatadefinitBVO[]) c
//					.toArray(new DipDatadefinitBVO[0]);
			//是否固定组织
			boolean isgdzz=false;
			if(hvo.getGuding()!=null&&hvo.getGuding().equals("Y")){
				isgdzz=true;
			}else{
				if(hvo.getOrg()==null||hvo.getOrg().length()<=0){
					errLogOut("没有定义组织字段",clue_id);
					return null;
				}
			}
			config.setIsgdzz(isgdzz);
			for (int i = 0; i < c.size(); i++) {
				DipDatadefinitBVO bvo = c.get(i);//[i];
				tab.put(bvo.getEname(), bvo);
				if(!isgdzz){
					if (bvo.getPrimaryKey().equals(hvo.getOrg())) {
						config.getParent().setOrg(bvo.getEname()); //翻译组织字段PK为实际字段名
					}
				}
				if(bvo.ispk != null && bvo.getIspk().booleanValue()){
					tab.put(DataChangeProcessor.PKFIELD_ATTRIBUTE, bvo);
				}
			}
			FilepathVO filepath = (FilepathVO)dmo.getDAO().retrieveByPK(FilepathVO.class, config.getParent().getOutpath());
			if(filepath != null && filepath.getDescriptions() != null){
				config.getParent().setOutpath(filepath.getDescriptions());
			}
			config.setDatadef(tab);
			//把数据转换的表头的vo的表名改为物理名称了
			config.getParent().setBusidata(defhead.getMemorytable());
			
			fillInfluenceConfig(clue_id);
			fillCombineConfig(clue_id);
			if(!config.isIsgdzz()){//如果不是固定组织
				getOrgMap(clue_id);
			}else{//如果是固定组织，那么给他new一个
//				config.setOrgMap(new Hashtable<String, String>());
			}
		}		
		return config;
	}
	
	@SuppressWarnings("unchecked")
	public Hashtable<String, TempletVO> getBillTemplet(String clue_id) throws DAOException{
		Hashtable<String, TempletVO> t_tab = new Hashtable<String, TempletVO>();
		//查询可用的表体VO
		List<DipDatachangeBVO> list = (List<DipDatachangeBVO>) dmo.getDAO().retrieveByClause(DipDatachangeBVO.class, "nvl(disable,'N') = 'N' and (length(tempexist) > 0 or def_str_1 is not null) and pk_datachange_h = '"+clue_id+"'");
		if(list == null || list.size() == 0){
//			没有有效的模板
			errLogOut("没有找到有效的模板",clue_id);
			return null;
		}
		boolean isgd=config.isIsgdzz();
		for(int i=0;i<list.size();i++){
			TempletVO t = new TempletVO();
			DipDatachangeBVO bvo = list.get(i);
			String key=bvo.getOrgcode().trim();
			String bkey=bvo.getPrimaryKey();
			if(bvo.getDef_str_1()!=null&&bvo.getDef_str_1().length()>0){
				bkey=bvo.getDef_str_1();
			}
			List<CredenceHVO> lch = (List<CredenceHVO>) dmo.getDAO().retrieveByClause(CredenceHVO.class, "pk_datadefinit_h = '"+bkey+"' and nvl(dr,0)=0");
			if(lch == null || lch.size() == 0){
				errLogOut("根据数据转换附表没有找到凭证模板定义主表"+key,clue_id);
		    	continue;
		    }
			t.setParent(((CredenceHVO[])lch.toArray(new CredenceHVO[0]))[0]);
			if(!key.contains(",")){ 
				t.getParent().setUnit(key);//TODO since 2011-07-02
			}else{
				t.getParent().setUnit(key.split(",")[0]);
			}
			
			List<CredenceBVO> lcb = (List<CredenceBVO>) dmo.getDAO().retrieveByClause(CredenceBVO.class, "pk_credence_h = '"+t.getParent().getPk_credence_h()+"' and nvl(dr,0)=0");
		    if(lcb == null || lcb.size() == 0){
				errLogOut("根据数据转换附表没有找到凭证模板定义附表"+key,clue_id);
		    	continue;
		    }
		    t.setChildren((CredenceBVO[])lcb.toArray(new CredenceBVO[0]));
		    if(bvo.getIssystmp()!=null&&bvo.getIssystmp().booleanValue()){
		    	t_tab.put(DataChangeProcessor.DEFAULT_TEMPLET, t);
		    }
		    if(isgd){
		    	t_tab.put(DataChangeProcessor.DEFAULT_TEMPLET, t);
		    }
		    if(!key.contains(",")){
		    	t_tab.put(key, t);
		    }else{
		    	String[] keys = key.split(",");
		    	for(int j=0;j<keys.length;j++){
		    		t_tab.put(keys[j].trim(), t);
		    	}
		    }
		}
	    return t_tab;
	}
	
	/**
	 * @param clue_id
	 * @return 键-业务数据字段，值-模板字段
	 * @throws DAOException 
	 */
	@SuppressWarnings("unchecked")
	public void fillInfluenceConfig(String clue_id) throws DAOException{
//		Collection c = dmo.getDAO().retrieveByClause(CdSbodyVO.class, "flag = "+INFLUENCE+" and dip_datachange_h = '"+clue_id+"'");
		List list = (List)dmo.getDAO().executeQuery("select effectname, (select ename from dip_datadefinit_b where pk_datadefinit_b=effectfiled) as effectfiled from dip_effectdef where flag = "+INFLUENCE+" and dip_datachange_h = '"+clue_id+"'", new BeanListProcessor(CdSbodyVO.class));
		if(list == null || list.size() == 0){
			return;
		}
		
		CdSbodyVO[] vos = (CdSbodyVO[])list.toArray(new CdSbodyVO[0]);
		Hashtable<String, String> tab = new Hashtable<String, String>();
		for(int i=0;i<vos.length;i++){
			if(vos[i].getEffectfiled() != null){
				tab.put(vos[i].getEffectname(), vos[i].getEffectfiled());
			}
		}
		config.setInfluence(tab);
	}
	
	@SuppressWarnings("unchecked")
	public void fillCombineConfig(String clue_id) throws DAOException{
		List list = (List)dmo.getDAO().executeQuery("select effectname, (select ename from dip_datadefinit_b where pk_datadefinit_b=effectfiled) as effectfiled from dip_effectdef where flag = "+COMBINE+" and dip_datachange_h = '"+clue_id+"'", new BeanListProcessor(CdSbodyVO.class));
		if(list == null || list.size() == 0){
			return;
		}
		
		CdSbodyVO[] vos = (CdSbodyVO[])list.toArray(new CdSbodyVO[0]);
		Hashtable<String, String> tab = new Hashtable<String, String>();
		for(int i=0;i<vos.length;i++){
			if(vos[i].getEffectfiled() != null){
				tab.put(vos[i].getEffectname(), vos[i].getEffectfiled());
			}
		}
//		config.setInfluence(tab);
		config.setCombine(tab);
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
		//这个orgPk是ename
		String orgPk = config.getParent().getOrg(); 
		//liyunzhe 2011-06-28 end可能是null，如果是空，就报空指针异常。
		if(orgPk==null || (config.getParent().getGuding() != null && config.getParent().getGuding().equals("Y"))){
			return;
		}
		//liyunzhe 2011-06-28 end
		//获取组织字段 字段VO的pk
		DipDatadefinitBVO orgField = config.getDatadef().get(orgPk);  //获取组织字段 字段VO
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
			tname=iqf.createVoucherPkTempTable(orgField,config.getParent().getBusidata(),  mapTab);
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
