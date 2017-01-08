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
 * @ƴװƾ֤ת����Ҫ��VO
 */
public class VOFormat {
	/**
	 * �ӱ��־ - Ӱ������
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
	 * ȡ�ù���������Ϣ����������ת��������Ϣ��������һЩ������Ϣ<br>
	 * <li>ȡ������ת��������Ϣ
	 * <li>���벿���ֶ�Ϊ��ȫ���ԣ�
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
			//ת��������� pk_def
			String pk_def = hvo.getSourcetab();
			//�ҵ�ת��������� defhead
			DipDatadefinitHVO defhead = (DipDatadefinitHVO) dmo.getDAO()
					.retrieveByPK(DipDatadefinitHVO.class, pk_def);
			//�ҵ�ת����ĸ���List c
			List<DipDatadefinitBVO> c = (List<DipDatadefinitBVO>) dmo.getDAO().retrieveByClause(
					DipDatadefinitBVO.class,
					"pk_datadefinit_h = '" + pk_def + "'");
			if (c == null || c.size() == 0) {
				errLogOut("δ�ҵ�����ת����Ӧ�ı�����ݶ���",pk_tyzh);
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
			//������ת���ı�ͷ��vo�ı�����Ϊ����������
			config.getParent().setSourcetab(defhead.getMemorytable());
			
			DipDatadefinitHVO thvo=(DipDatadefinitHVO) dmo.getDAO().retrieveByPK(DipDatadefinitHVO.class, hvo.getTargettab());
			List<DipDatadefinitBVO> c1 = (List<DipDatadefinitBVO>) dmo.getDAO().retrieveByClause(
					DipDatadefinitBVO.class,
					"pk_datadefinit_h = '" + hvo.getTargettab() + "' and ispk='Y'");
			if(c1!=null&&c1.size()>0){
				config.setTagpk(c1.get(0).getEname());
			}
//			if(hvo.getRepdatactl().equals("0001AA10000000018UZ1")){
			//liyunzhe modify 2012-6-13 �޸��ظ����ݿ��ƻ���������Ϣ�ж�
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
	 * �õ���֯�����Ķ��ձ�
	 * @param clue_id
	 * @return <��ϵͳ��֯����������, PK_CORP>
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	public void getOrgMap(String clue_id) throws DAOException{
//		ConfigVO vo = getConfigVO(clue_id); 
		//��ȡ��֯�ֶ� �ֶ�VO��pk
		DipDatadefinitBVO orgField = config.getDatadef().get("");  //��ȡ��֯�ֶ� �ֶ�VO
		String orgTable = orgField.getQuotetable();  //���õ�ҵ��������֯������ ����
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
			throw new DAOException("������֯������ʱ�����");
		}
		config.setTemptablename(tname);
		/*String sql = "select contpk, extepk from " + mapTab + " where nvl(dr,0) = 0 and contpk is not null and extepk is not null";
		List list = (List)dmo.getDAO().executeQuery(sql, new MapListProcessor());
		if(list == null || list.size() == 0){
			//mapTab��û��ά�����չ�ϵ
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
