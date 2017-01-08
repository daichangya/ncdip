package nc.impl.dip.pub;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.ILogAndTabMonitorSys;
import nc.itf.dip.pub.IQueryField;
import nc.jdbc.framework.exception.DbException;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.control.ControlVO;
import nc.vo.dip.dateprocess.DateprocessVO;
import nc.vo.dip.esbmhccb.DipEsbMhccbVO;
import nc.vo.dip.message.MsrVO;
import nc.vo.dip.runsys.DipRunsysBVO;
import nc.vo.dip.statusregist.StatusregistVO;
import nc.vo.dip.sysregister.DipSysregisterHVO;
import nc.vo.dip.tabstatus.DipTabstatusVO;
import nc.vo.dip.tabstatus.TabstatusBVO;
import nc.vo.dip.view.VDipTabAFileVO;
import nc.vo.dip.warningset.DipWarningsetVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;

/**
 * 
 * @desc 日志系统和表状态监控
 * @data 2011-05-23
 * */
public class LogAndTabMonitorSysImpl implements ILogAndTabMonitorSys {
	BaseDAO bd;
	private BaseDAO getBaseDAO(){
		if(bd==null){
			bd=new BaseDAO(IContrastUtil.DESIGN_CON);
		}
		return bd;
	}
	IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	/**
	 * @desc 往模糊存储表里插入消息
	 * 2012-2-20 新增的方法
	 * */
	public void writToMhccb_RequiresNew(String msg,MsrVO msrvo){

		try {
			List<DipRunsysBVO> runbvo=(List<DipRunsysBVO>) getBaseDAO().retrieveByClause(DipRunsysBVO.class, " syscode='DIP-0000012' and nvl(dr,0)=0 ");
			DateprocessVO vo=new DateprocessVO();
			vo.setActivetype("数据接收");
			vo.setActive("ESB消息接收");
			vo.setSdate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			
			if(runbvo==null||runbvo.size()<=0){
				vo.setContent("没有找到【运行参数】里“ESB消息模糊匹配存储表”的参数值，因此往存储表里写入模糊消息失败【"+msg+"】");
				vo.setDef_str_1(IContrastUtil.DATAPROCESS_HINT);
				writToDataLog_RequiresNew(vo);
			}else{
				String value=runbvo.get(0).getSysvalue();
				if(value!=null&&value.length()>0){
					String sql="select table_name from user_tables where table_name='"+value.trim().toUpperCase()+"'";
					String isexit = null;
					try {
						isexit = iqf.queryfield(sql);
					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (DbException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(isexit==null||isexit.length()<=0){
						String cratesql="create table "+value.trim()+"  ( "
							+" pk_esb_mhccb       VARCHAR2(20)                    not null, "
							+" mcode               VARCHAR2(20), "
							+" mname               VARCHAR2(100), "
							+" pk_msr             CHAR(20), "
							+" sdate              char(19), "
							+" msg                CLOB, "
							+" ts		      char(19), "
							+" dr                 smallint, "
							+" constraint PK_"+value.trim().toUpperCase()+" primary key (pk_esb_mhccb) "
							+" )";
						try {
							iqf.exesql(cratesql);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (DbException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					DipEsbMhccbVO emvo=new DipEsbMhccbVO(value.trim());
					emvo.setMcode(msrvo.getCode());
					emvo.setMname(msrvo.getName());
					emvo.setPk_msr(msrvo.getPrimaryKey());
					emvo.setMsg(msg);
					emvo.setSdate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					getBaseDAO().insertVO(emvo);
				}
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/**往表状态监控里写日志，如果表状态监控主表vo不是空，那么插入，如果是空，那么插入子表*/
	public boolean writToTabMointor_RequiresNew(DipTabstatusVO hvo,TabstatusBVO bvo,int state) {
		
		if(hvo!=null){
			String tabname=hvo.getTablecode();
			List<DipTabstatusVO> list=null;
			DipTabstatusVO hvot=null;
			try {
				list=(List<DipTabstatusVO>) getBaseDAO().retrieveByClause(DipTabstatusVO.class, "tablecode ='"+tabname+"' and nvl(dr,0)=0");
				if(list!=null&&list.size()>0){
					hvot=list.get(0);
				}
				hvo.setTabdate(sdf.format(new Date()));
				bvo.setMydate(sdf.format(new Date()));
				if(hvot!=null){
					hvo.setPrimaryKey(hvot.getPrimaryKey());
					if(state==MSTATE_ZT){
						getBaseDAO().updateVO(hvo);
					}
					bvo.setPk_tabstatus_h(hvo.getPrimaryKey());
					getBaseDAO().insertVO(bvo);
				}else{
						String pk=getBaseDAO().insertVO(hvo);
						bvo.setPk_tabstatus_h(pk);
						getBaseDAO().insertVO(bvo);
				}
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	/**
	 * @desc 往表状态监控里写监控状态
	 * @author wyd
	 * @param pk_bus 对应的业务表的主表主键
	 * @param type 业务类别
	 * @param status 要写入的状态 IContrastUtil.TABMONSTA_ON 或者IContrastUtil.TABMONSTA_FIN
	 */
	public boolean writeToTabMonitor_RequiresNew(String pk_bus,String type,String status,Integer s, Integer f){
		boolean ret =true;
		try {
			if(pk_bus!=null&&pk_bus.length()>0){
				List<ControlVO> vos=(List<ControlVO>) getBaseDAO().retrieveByClause(ControlVO.class, "pk_bus='"+pk_bus+"' and kzjszt is not null and kzclzt is not null");
				if(vos!=null&&vos.size()>0){
					List<DipTabstatusVO> listh=new ArrayList<DipTabstatusVO>();
					List<TabstatusBVO> listb=new ArrayList<TabstatusBVO>();
					List<String> errlist=new ArrayList<String>();
					for(int i=0;i<vos.size();i++){
						ControlVO convo=vos.get(i);
						String pktab=convo.getTabcname();
						TabstatusBVO bvoo=null;
						DipTabstatusVO vo=null;
						String sta=status==IContrastUtil.TABMONSTA_ON?convo.getKzclzt():convo.getKzjszt();
						StatusregistVO sr=null;
						try {
							sr=(StatusregistVO) getBaseDAO().retrieveByPK(StatusregistVO.class, sta);
						} catch (DAOException e1) {
							e1.printStackTrace();
						}
						if(sr==null){
							return false;
						}
						

						VDipTabAFileVO dfhvo=null;
							try {
								dfhvo=(VDipTabAFileVO) getBaseDAO().retrieveByPK(VDipTabAFileVO.class, pktab);
							} catch (DAOException e) {
								e.printStackTrace();
							}
							if(dfhvo==null){
								return ret;
							}
							String pk_sys=dfhvo.getPksys();
							vo=new DipTabstatusVO();
							vo.setSyscode(pk_sys);
							vo.setSysname(dfhvo.getSname()==null?"":dfhvo.getSname());
							vo.setTablecode(pktab);
							vo.setTabname(dfhvo.getName());
							vo.setStabname(dfhvo.getDes());
							vo.setDef_str_1(sr.getPrimaryKey());
							vo.setIsstack(sr.getIsstack());
							vo.setTabstatus(sr.getName());
							
							bvoo=new TabstatusBVO();
							bvoo.setActive(SJUtil.getYWnameByLX(type)/*IContrastUtil.getTabName().get(type).toString()*/);
							bvoo.setState(sr.getName());
							if(status==IContrastUtil.TABMONSTA_FIN){
								bvoo.setAccessdata(s);
								bvoo.setFaildata(f);
							}
							bvoo.setDef_str_1(sr.getPrimaryKey());
							if(sr.getIsstack()!=null&&sr.getIsstack().booleanValue()){
								List<DipTabstatusVO> list=(List<DipTabstatusVO>) getBaseDAO().retrieveByClause(DipTabstatusVO.class, "tablecode ='"+pktab+"' and isstack='Y' and nvl(dr,0)=0");
								if(list!=null&&list.size()>0){
									errlist.add("【"+dfhvo.getName()+"】"+"状态已经是【"+list.get(0).getTabstatus()+"】,根据独占状态规则不能更改为【"+vo.getTabstatus()+"】");
									ret=false;
								}
							}
								listh.add(vo);
								listb.add(bvoo);
					
					}
					if(!ret){
						writToDataLog_RequiresNew(pk_bus, type, errlist);
						return ret;
					}
					if(listh!=null&&listh.size()>0){
						for(int i=0;i<listh.size();i++){
							writToTabMointor_RequiresNew(listh.get(i), listb.get(i),MSTATE_ZT);
						}
					}
				}
			}
		} catch (DAOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return ret;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ret;
	}

	public boolean writeToTabMonitor_RequiresNew(String pk_bus, String yw, int su, int fa,String classname) {
		boolean ret =false;
		try {
			if(pk_bus!=null&&pk_bus.length()>0){
				List<ControlVO> vos=(List<ControlVO>) getBaseDAO().retrieveByClause(ControlVO.class, "pk_bus='"+pk_bus+"'");
				if(vos!=null&&vos.size()>0){
					for(int i=0;i<vos.size();i++){
						ControlVO convo=vos.get(i);
						String pktab=convo.getTabcname();
						DipTabstatusVO vo =new DipTabstatusVO();
						vo.setTablecode(pktab);
						
						TabstatusBVO bvoo=new TabstatusBVO();
						bvoo.setActive(SJUtil.getYWnameByLX(yw)/*IContrastUtil.getTabName().get(yw).toString()*/);
						bvoo.setAccessdata(su);
						bvoo.setFaildata(fa);
						bvoo.setOper(classname);
						
						ret=writToTabMointor_RequiresNew(vo, bvoo,MSTATE_JY);
					}
				}
			}
		}catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		return ret;
	}
	/**
	 * @desc 往表状态监控里写监控状态 写校验的相关信息
	 * @author wyd
	 * @param pktab 监控表的主（pk_datadefin_h）
	 * @param pk_bus 对应的业务表的主表主键
	 * @param yw 业务类别 IContrastUtil.YWLX_DY...
	 * @param su 成功的数量
	 * @param fa 失败的数量
	 * @param classname 校验类
	 */
	public boolean writeToTabMonitor_RequiresNew(String pktab, String pk_bus, String yw,
			int su,int fa,String classname) {
		boolean ret=false;
		try{
		//数据同步，数据加工，数据转换，数据发送需要，其它不需要
		if(!(yw.equals(IContrastUtil.YWLX_FS)||yw.equals(IContrastUtil.YWLX_JG)||yw.equals(IContrastUtil.YWLX_TB)||yw.equals(IContrastUtil.YWLX_ZH))){
			return ret;
		}
		//1、取得当前状态当前表的控制状态
		String tablname=(String) IContrastUtil.getTabNameMap().get(yw);
		List<DipTabstatusVO> vos=null;
		TabstatusBVO bvoo=null;
		try {
			vos= (List<DipTabstatusVO>) getBaseDAO().retrieveByClause(DipTabstatusVO.class, "tablecode in ('"+pktab.replaceAll(",", "','")+"') and nvl(dr,0)=0");
		} catch (DAOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DipTabstatusVO vo=null;
		if(vos==null||vos.size()<=0){

		}else{
			UFDateTime uft=new UFDateTime(new Date());
			List<TabstatusBVO> il=new ArrayList<TabstatusBVO>();
			for(int i=0;i<vos.size();i++){
				vo=vos.get(0);
				String pkh=vo.getPrimaryKey();
				bvoo=new TabstatusBVO();
				bvoo.setActive(SJUtil.getYWnameByLX(yw)/*IContrastUtil.getTabName().get(yw).toString()*/);
				bvoo.setMydate(uft.toString());
				bvoo.setPk_tabstatus_h(pkh);
				bvoo.setAccessdata(su);
				bvoo.setFaildata(fa);
				bvoo.setOper(classname);
				il.add(bvoo);
			}
			try {
				getBaseDAO().insertVOList(il);
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		}catch(Exception e){
			e.printStackTrace();
		}

		return true;
	}
	/**
	 * @desc 往数据处理日志里写日志
	 * @param pk_bus 业务主表主键
	 * @param yw 业务类型  IContrastUtil.YWLX_DY...
	 * @param des 工作内容
	 * */

	public boolean writToDataLog_RequiresNew(String pk_bus,String yw,String des){
		List<String> errlist=new ArrayList<String>();
		errlist.add(des);
		return writToDataLog_RequiresNew(pk_bus, yw, errlist);
	}
	public boolean writToDataLog_RequiresNew(String pk_bus,String yw,List<String> errlist){
		boolean ret=false;
		if(SJUtil.isNull(pk_bus)||SJUtil.isNull(yw)||SJUtil.isNull(errlist)){
			return ret;
		}
		String field=IContrastUtil.getFpkMap().get(yw).toString();
		String fieldyw=IContrastUtil.getYwName().get(yw).toString();
		String fieldbm=IContrastUtil.getYwbmName().get(yw).toString();
		String table=IContrastUtil.getTabNameMap().get(yw).toString();
		String where=IContrastUtil.getPkMap().get(yw).toString();
		field="pk_xt";
		String sql="select "+field+","+fieldyw+","+fieldbm+" from "+table+" where "+where +"='"+pk_bus+"' and nvl(dr,0)=0";
//		String sqlyw="select "+fieldyw+" from "+table+" where "+where +"='"+pk_bus+"' and nvl(dr,0)=0";
//		String sqlywbm="select "+fieldbm+" from "+table+" where "+where +"='"+pk_bus+"' and nvl(dr,0)=0";
		IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
		List list=null;
		String ywname="";
		String ywcode="";
		try {
			list=iqf.queryListMapSingleSql(sql);
		} catch (BusinessException e1) {
			 // TODO Auto-generated catch block
			 e1.printStackTrace();
		 } catch (SQLException e1) {
			 // TODO Auto-generated catch block
			 e1.printStackTrace();
		 } catch (DbException e1) {
			 // TODO Auto-generated catch block
			 e1.printStackTrace();
		 }catch(Exception e){
			 e.printStackTrace();
		 }
			 if(list==null||list.size()<=0){
				 return ret;
			 }
			 Map map=(Map) list.get(0);
			 String pk_sys=map.get(field.toUpperCase())==null?"":map.get(field.toUpperCase()).toString();
			 ywname=map.get(fieldyw.toUpperCase())==null?"":map.get(fieldyw.toUpperCase()).toString();
			 ywcode=map.get(fieldbm.toUpperCase())==null?"":map.get(fieldbm.toUpperCase()).toString();
			 DipSysregisterHVO hvo=SJUtil.getSysVOByPK(pk_sys);
			 if(hvo==null){
				 return ret;
			 }
//		系统名称，活动类型，活动编码，活动，内容，时间
//		sysname,activetype,activecode,active,content,sdate
			 for(int i=0;i<errlist.size();i++){
			 DateprocessVO vo=new DateprocessVO();
			 vo.setDef_str_1(IContrastUtil.DATAPROCESS_ERR);
			 vo.setSysname(hvo.getExtname());
			 vo.setActivetype(SJUtil.getYWnameByLX(yw)/*IContrastUtil.getTabName().get(yw).toString()*/);
			 vo.setActivecode(ywcode);
			 vo.setActive(ywname);
			 vo.setContent(errlist.get(i));
			 if(errlist.get(i)!=null&&errlist.get(i).length()>0){
				 if(errlist.get(i).endsWith("--"+IContrastUtil.DATAPROCESS_HINT)){
					 vo.setDef_str_1(IContrastUtil.DATAPROCESS_HINT);
					 vo.setContent(errlist.get(i).substring(0, errlist.get(i).lastIndexOf("--")));
				 }
			 }
			 
			 vo.setSyscode(hvo.getCode());
			 vo.setPk_bus(pk_bus);
			 
			 UFDateTime now = new UFDateTime(new Date());
			 String nowDate = now.toString();
			 vo.setSdate(nowDate);
			 
			 if(vo.getActivetype()!=null&&vo.getActivetype().equals("数据同步")&&pk_bus!=null&&!pk_bus.trim().equals("")&&ywname!=null&&!ywname.trim().equals("")){
					String sql1="select hh.ioflag from dip_datarec_h hh where hh.pk_datarec_h in (select ch.dataname from dip_datasynch ch where ch.pk_datasynch='"+pk_bus+"')";
					String value="";
					try {
						value=iqf.queryfield(sql1);
					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (DbException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(value!=null&&!value.trim().equals("")){
						vo.setActive(ywname+"-"+value);
					}
				}
			 
			 writToDataLog_RequiresNew(vo);
			 }
			 return true;
	}
	public boolean writToDataLog_RequiresNew(DateprocessVO vo) {
		vo.setSdate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		WriteLog.getOrAddList(WriteLog.LX_ADD, vo, null);
		return true;
	}
	
	
	
	/**
	 * @desc 往数据处理日志里写日志
	 * @param pk_warn 预警的主键
	 * @desc 工作内容
	 * */
	public boolean writToDataLog_RequiresNew(String pk_warn,String des){
		if(pk_warn==null){
			return false;
		}
		DipWarningsetVO hvo=null;
		try {
			hvo = (DipWarningsetVO) getBaseDAO().retrieveByPK(DipWarningsetVO.class, pk_warn);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		if(hvo==null){
			return false;
		}
		return writToDataLog_RequiresNew(hvo.getPk_bustab(), SJUtil.getYWbyYWLX(hvo.getTasktype()), /*"不符合预警条件"+*/des);

	}
	
	
	/**
	 * @desc 往表状态监控里写监控状态
	 * @author lyz
	 * @param pk_bus 对应的业务表的主表主键
	 * @param type 业务类别
	 * @param status 要写入的状态 IContrastUtil.TABMONSTA_ON 或者IContrastUtil.TABMONSTA_FIN
	 * @param tablestate 需要写入的状态
	 * @param tablekey表在数据定义的主键
	 */
	public boolean writeToTabMonitor_RequiresNew_StateManage(String pk_bus,String type,String status,Integer s, Integer f,String tablestate,String tablekey){
		boolean ret =true;
		try {
//			if(pk_bus!=null&&pk_bus.length()>0){
//				List<ControlVO> vos=(List<ControlVO>) getBaseDAO().retrieveByClause(ControlVO.class, "pk_bus='"+pk_bus+"'");
//				if(vos!=null&&vos.size()>0){
					List<DipTabstatusVO> listh=new ArrayList<DipTabstatusVO>();
					List<TabstatusBVO> listb=new ArrayList<TabstatusBVO>();
					List<String> errlist=new ArrayList<String>();
//					for(int i=0;i<vos.size();i++){
//						ControlVO convo=vos.get(i);
//						String pktab=convo.getTabcname();//table主键
						TabstatusBVO bvoo=null;
						DipTabstatusVO vo=null;
						//String sta=status==IContrastUtil.TABMONSTA_ON?convo.getKzclzt():convo.getKzjszt();
						StatusregistVO sr=null;
						try {
							sr=(StatusregistVO) getBaseDAO().retrieveByPK(StatusregistVO.class, tablestate);
						} catch (DAOException e1) {
							e1.printStackTrace();
						}
						if(sr==null){
							return false;
						}
						

						VDipTabAFileVO dfhvo=null;
							try {
								dfhvo=(VDipTabAFileVO) getBaseDAO().retrieveByPK(VDipTabAFileVO.class, tablekey);
							} catch (DAOException e) {
								e.printStackTrace();
							}
							if(dfhvo==null){
								return false;
							}
							String pk_sys=dfhvo.getPksys();
							vo=new DipTabstatusVO();
							vo.setSyscode(pk_sys);
							vo.setSysname(dfhvo.getSname()==null?"":dfhvo.getSname());
							vo.setTablecode(tablekey);
							vo.setTabname(dfhvo.getName());
							vo.setStabname(dfhvo.getDes());
							vo.setDef_str_1(sr.getPrimaryKey());
							vo.setIsstack(sr.getIsstack());
							vo.setTabstatus(sr.getName());
							
							bvoo=new TabstatusBVO();
							bvoo.setActive(SJUtil.getYWnameByLX(type)/*IContrastUtil.getTabName().get(type).toString()*/);
							bvoo.setState(sr.getName());
							if(status==IContrastUtil.TABMONSTA_FIN){
								bvoo.setAccessdata(s);
								bvoo.setFaildata(f);
							}
							bvoo.setDef_str_1(sr.getPrimaryKey());
							if(sr.getIsstack()!=null&&sr.getIsstack().booleanValue()){
								List<DipTabstatusVO> list=(List<DipTabstatusVO>) getBaseDAO().retrieveByClause(DipTabstatusVO.class, "tablecode ='"+tablekey+"' and isstack='Y' and nvl(dr,0)=0");
								if(list!=null&&list.size()>0){
									errlist.add("【"+dfhvo.getName()+"】"+"状态已经是【"+list.get(0).getTabstatus()+"】,根据独占状态规则不能更改为【"+vo.getTabstatus()+"】");
									ret=false;
								}
							}
								listh.add(vo);
								listb.add(bvoo);
					
//					}
					if(!ret){
						writToDataLog_RequiresNew(pk_bus, type, errlist);
						return ret;
					}
					if(listh!=null&&listh.size()>0){
						for(int i=0;i<listh.size();i++){
							writToTabMointor_RequiresNew(listh.get(i), listb.get(i),MSTATE_ZT);
						}
					}
//				}
//			}
		} catch (DAOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return ret;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ret;
	}
	
	
}
