package nc.bs.dip.pub;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.impl.dip.pub.QueryFieldImpl;
import nc.itf.dip.pub.IQueryField;
import nc.jdbc.framework.exception.DbException;
import nc.util.dip.sj.IContrastUtil;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.dataformatdef.DataformatdefBVO;
import nc.vo.dip.dataformatdef.DataformatdefHVO;
import nc.vo.dip.datarec.DipDatarecHVO;
import nc.vo.dip.datarecD.ArgDataRecDVO;
import nc.vo.dip.datasynch.DipDatasynchVO;
import nc.vo.dip.messagelogo.MessagelogoVO;
import nc.vo.dip.sysregister.DipSysregisterHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;

public class ResvDataSrv {
	IQueryField query;

	public ResvDataSrv() {
		try {
			query = new QueryFieldImpl();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 合并list
	 * @param list1
	 * @param list2
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List mergeList(List list1, List list2) {
		List mergelist = new ArrayList();
		for (int i = 0; i < list1.size(); i++) {
			mergelist.add(list1.get(i));
		}
		for (int i = 0; i < list2.size(); i++) {
			mergelist.add(list2.get(i));
		}

		
		return mergelist;
	}
private BaseDAO bs;
	private BaseDAO getBasedao(){
		if(bs==null){
			bs=new BaseDAO(IContrastUtil.DESIGN_CON);
		}
		return bs;
	}

	/**
	 * 获取数据接收服务相关数据
	 * 
	 * @param syscode
	 *            系统编码
	 * @param sysname
	 *            系统名称
	 * @param busicode
	 *            服务标识
	 * @return
	 * @throws DbException
	 * @throws SQLException
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public Object getrRecAggVo(String syscode, String sysname, String busicode)
			throws BusinessException, SQLException, DbException {
		if ((null == syscode || "".equals(syscode))
				|| (null == sysname || "".equals(sysname))
				|| (null == busicode || "".equals(busicode))) {
			Logger.error("所传参数有为空的项");
			return null;
		}
		ArgDataRecDVO argvo = new ArgDataRecDVO();
		DipDatarecHVO Hvo = new DipDatarecHVO();
		String tableName = "";
		// 数据接收主表主键
		String pk_datarec_h = "";
		// 数据格式定义表主键
		String pk_def_h = "";
		//标志头<就一个字段>
		String head = "";
		//标志尾<就一个字段>
		String end = "";
		List list = new ArrayList();
		DataformatdefHVO bvo = new DataformatdefHVO();
		List list_def_h = new ArrayList();
		String sqlsyspk="select pk_sysregister_h from dip_sysregister_h where extcode='"+syscode+"' and nvl(dr,0)=0 and rownum<=1 ";
		//系统主键
		sqlsyspk=query.queryfield(sqlsyspk);
		if(sqlsyspk==null||sqlsyspk.length()<=0){
			return null;
		}
		DipSysregisterHVO hvo=(DipSysregisterHVO) getBasedao().retrieveByPK(DipSysregisterHVO.class, sqlsyspk);
		if(hvo==null){
			return null;
		}
		boolean isfb=hvo.getIsdeploy()==null?false:new UFBoolean(hvo.getIsdeploy()).booleanValue();
		//站点主键
		String pkzd="";
		//当前表主键
		String pktab="";
		if(isfb){
			String zdsq="select pk_sysregister_B from dip_sysregister_b where pk_sysregister_h='"+sqlsyspk+"' and sitecode='"+sysname+"' and nvl(dr,0)=0 and rownum<=1";
			pkzd=query.queryfield(zdsq);
			if(pkzd==null){
				pkzd="";
			}
			pktab="select pk_datadefinit_h from dip_datadefinit_h where  pk_sysregister_h='"+sqlsyspk+"' and nvl(dr,0)=0 and rownum<=1 and busicode='"+busicode+"' and tabsoucetype='自定义' and deploycode='"+pkzd+"'";
			
		}else{
			pktab="select pk_datadefinit_h from dip_datadefinit_h where  pk_sysregister_h='"+sqlsyspk+"' and nvl(dr,0)=0 and rownum<=1 and busicode='"+busicode+"' and tabsoucetype='自定义'";
		}
		pktab=query.queryfield(pktab);
		if(pktab==null){
			return null;
		}
		Map<String,String > deftype=new HashMap<String, String>();
		Map<String,Integer > deftypelen=new HashMap<String, Integer>();
		List<DipDatadefinitBVO> definbvos=(List<DipDatadefinitBVO>) getBasedao().retrieveByClause(DipDatadefinitBVO.class, "pk_datadefinit_h='"+pktab+"' and nvl(dr,0)=0");
		if(definbvos==null||definbvos.size()<=0){
			return null;
		}else{
			for(DipDatadefinitBVO bvodi:definbvos){
				deftype.put(bvodi.getEname(), bvodi.getType());
				deftypelen.put(bvodi.getEname(), bvodi.getLength());
			}
		}
		// 查询数据接收定义所有字段
		list = (List) getBasedao().retrieveByClause(DipDatarecHVO.class, "fpk='" +sqlsyspk+"' and memorytable='"+pktab+"' and nvl(dr,0)=0 and sourcetype='0001AA10000000013SVI'") ;
		if (null != list && list.size() > 0) {
			// 拿到的只是一条数据
			Hvo = (DipDatarecHVO) list.get(0);
			if (null != Hvo) {
				// 拿到储存表名
				tableName = Hvo.getDataname() == null ? "" : Hvo.getDataname();
				// 拿到数据接收主表的主键
				pk_datarec_h = Hvo.getPk_datarec_h()  == null ? "" : Hvo.getPk_datarec_h();
			}
			HashMap<Integer, String> mapb=new HashMap<Integer, String>();
			if (!"".equals(tableName) && !"".equals(pk_datarec_h)) {
				// 查询数据格式定义对应数据
				list_def_h = (List) getBasedao().retrieveByClause(DataformatdefHVO.class, "PK_DATAREC_H='"+ pk_datarec_h + "' and nvl(dr,0)=0");
				if (null != list_def_h && list_def_h.size() > 0) {
					// 数据来源类型为消息总线时，消息流格式默认为顺序对照
					int size = list_def_h.size();
					// 拿到DIP_DATAFORMATDEF_H数据格式定义主表（3个vo）
					for (int i = 0; i < size; i++) {
						// 得到数据接收子表vo
						bvo = (DataformatdefHVO) list_def_h.get(i);
						//获取标志头，这里表字段尚未定，定下来后必须改
						head = getBZ(bvo.getBeginsign());
						//获取标志尾，这里表字段尚未定，定下来后必须改
						end =getBZ( bvo.getEndsign());
						// 得到数据格式定义表主键
						pk_def_h = bvo.getPk_dataformatdef_h();
						List<DataformatdefBVO> listb=(List<DataformatdefBVO>) getBasedao().retrieveByClause(DataformatdefBVO.class, "PK_DATAFORMATDEF_H='"+ pk_def_h + "' AND  CODE>3 and datakind is not null");
						if(listb==null||listb.size()<=0){
							return null;
						}
						for(DataformatdefBVO bvoi:listb){
							mapb.put(bvoi.getCode(), bvoi.getDatakind());
						}
					}
				}
			}
			argvo.setCHVO(Hvo);
			argvo.setBmap(mapb);
			argvo.setTableName(tableName);
			argvo.setBeginFlag(head);
			argvo.setEndFlag(end);
			argvo.setPk_sys(sqlsyspk);
			argvo.setPk_zd(pkzd);
			argvo.setFbzd(isfb);
			argvo.setYw(busicode);
			argvo.setFieldLen(deftypelen);
			argvo.setFiledType(deftype);
			argvo.setJsl(head+","+syscode+","+sysname+","+busicode+","+end);

		} else {
			Logger.error("找不到相关数据");
			return null;
		}
		return argvo;
	}
	private String  getBZ(String pk_bz){
		if(pk_bz==null||pk_bz.length()<=0){
			return "";
		}
		try {
			MessagelogoVO vo=(MessagelogoVO) getBasedao().retrieveByPK(MessagelogoVO.class, pk_bz);
			if(vo==null){
				return "";
			}else{
				return vo.getCdata();
			}
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取数据同步服务相关数据
	 * 
	 * @param syscode
	 *            系统编码
	 * @param sysname
	 *            系统名称
	 * @param busicode
	 *            服务标识
	 * @return
	 */
	public DipDatasynchVO getSynAggVo(String syscode, String sysname,
			String busicode) {
		if ((null == syscode || "".equals(syscode))
				|| (null == sysname || "".equals(sysname))
				|| (null == busicode || "".equals(busicode))) {
			Logger.error("所传参数有为空的项");
			return null;
		}
		String sql = "";
		String tableName = "";
		String pk_datasynch = "";
		sql = "select pk_datasynch,datasite,dataname,phyname,datacon,constatus,endstatus,tasktype,ts,dr,def_str_1,def_str_2 from dip_datasynch ";
		DipDatasynchVO vo = new DipDatasynchVO();

		return vo;
	}

	/**
	 * 数据校验
	 * 
	 * @param syscode
	 *            系统编码
	 * @param sysname
	 *            系统名称
	 * @param busicode
	 *            服务标识
	 * @return
	 */
	public String datacheck(String syscode, String sysname, String busicode) {
		String str = null;

		return str;
	}

	/**
	 * 获取数据定义服务相关数据
	 * 
	 * @param syscode
	 *            系统编码
	 * @param sysname
	 *            系统名称
	 * @param busicode
	 *            服务标识
	 * @return
	 */
	public List getDataDef(String syscode, String sysname, String busicode) {
		String field = "cname,ename,type,length,deciplace,ispk,isonlyconst,isquote,quotetable,ts,dr,quotecolu,def_str_1,def_str_2";
		String sql = "";
		String pk = "";
		String tableName = "";
		List list = new ArrayList();
		List listField = new ArrayList();
		Map map = new HashMap();
		syscode = syscode == null ? "" : syscode;
		sysname = sysname == null ? "" : sysname;
		busicode = busicode == null ? "" : busicode;
		if (!"".equals(syscode) && !"".equals(sysname) && !"".equals(busicode)) {
			sql = "select memorytable,pk_datadefinit_h from dip_datadefinit_h where syscode='"
					+ syscode
					+ "' and sysname='"
					+ sysname
					+ "' and busicode='" + busicode + "' and nvl(dr,0)=0";
			try {
				list = query.queryListMapSingleSql(sql);
				if (null != list && list.size() > 0) {
					map = (Map) list.get(0);
					if (null != map) {
						pk = map.get("pk_datadefinit_h") == null ? "" : map
								.get("pk_datadefinit_h").toString();
						tableName = map.get("memorytable") == null ? "" : map
								.get("memorytable").toString();
					}
				}

				if (!"".equals(tableName) && !"".equals(pk)) {
					sql = "select " + field + " from " + tableName
							+ " where pk_datadefinit_h='" + pk
							+ "' and nvl(dr,0)=0";
					listField = query.queryListMapSingleSql(sql);

				}
			} catch (DAOException e) {
				e.printStackTrace();
			} catch (BusinessException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (DbException e) {
				e.printStackTrace();
			}

		}
		return listField;
	}

	

}
