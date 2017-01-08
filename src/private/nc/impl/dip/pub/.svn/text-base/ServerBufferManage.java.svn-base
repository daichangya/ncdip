package nc.impl.dip.pub;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.bs.pub.pa.PaConstant;
import nc.itf.dip.pub.IServerBufferManage;
import nc.util.dip.sj.IContrastUtil;
import nc.vo.dip.managerserver.DipManagerserverVO;
import nc.vo.dip.messservtype.MessservtypeVO;
import nc.vo.pub.pa.TimingSettingVO;
import nc.vo.uap.scheduler.TimeConfigVO;

public class ServerBufferManage implements IServerBufferManage {

	private static Map<String,DipManagerserverVO> serverBufManageVOMap=null;
	private static List<DipManagerserverVO> messServerBuufer=new ArrayList<DipManagerserverVO>();
	/**
	 * @author wyd
	 * @desc 更新服务管理，根据线程id跟新服务管理的状态
	 * @param serverThreadName 服务器以及线程的唯一id
	 * @param dipMVO 要更新的服务管理vo
	 * @return boolean 返回是否更新服务管理成功
	 * */

	public static byte lock[] = new byte[0]; 
	public boolean setServerBufferManage(String serverThreadName,DipManagerserverVO dipMVO){
		boolean ret=true;
		synchronized (lock){
        	Logger.debug("写入服务========"+serverThreadName+"===========");
			if(serverBufManageVOMap==null){
				serverBufManageVOMap=new HashMap<String, DipManagerserverVO>();
			}
			if(serverBufManageVOMap.containsKey(serverThreadName)){
				serverBufManageVOMap.remove(serverThreadName);
			}
			if(dipMVO==null){
				dipMVO=new DipManagerserverVO();
	//			dipMVO.set
			}
			dipMVO.setIsmsg(false);
			dipMVO.setStartandstorp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			serverBufManageVOMap.put(serverThreadName, dipMVO);
		}
		return ret;
		/*boolean ret=true;
		String lock="getRunMesVO";
		Boolean flag=PKLock.getInstance().acquireLock(lock, null,null); 
		try{
	        if(flag){
	        	Logger.debug("写入服务========"+serverThreadName+"===========");
				if(serverBufManageVOMap==null){
					serverBufManageVOMap=new HashMap<String, DipManagerserverVO>();
				}
				if(serverBufManageVOMap.containsKey(serverThreadName)){
					serverBufManageVOMap.remove(serverThreadName);
				}
				if(dipMVO==null){
					dipMVO=new DipManagerserverVO();
		//			dipMVO.set
				}
				dipMVO.setIsmsg(false);
				dipMVO.setStartandstorp(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(new Date()));
				serverBufManageVOMap.put(serverThreadName, dipMVO);
	        }
		}finally{
			PKLock.getInstance().releaseLock(lock, null, null);
		}
		return ret;*/
	}
	
	/**
	 * @author wyd
	 * @desc 返回缓存中，所有服务管理状态
	 * @param 
	 * @return DipManagerserverVO[] 返回缓存中，所有服务管理VO
	 * */
	public DipManagerserverVO[] getServerBufferManage(){
		synchronized (lock){
			List<DipManagerserverVO> retlist=getWarnBufVO();
			List<DipManagerserverVO> list2=getMsgBufVO();
			if(retlist==null){
				retlist=list2;
			}else{
				if(list2!=null&&list2.size()>0){
					retlist.addAll(list2);
				}
			}
			if(retlist.size()>0){
				return (DipManagerserverVO[]) retlist.toArray(new DipManagerserverVO[retlist.size()]);
			}else{
				return null;
			}
		}
		
	}
	/**
	 * @author wyd 
	 * @desc 得到预警类型的所有服务管理
	 * */
	public List<DipManagerserverVO> getWarnBufVO(){
		if(serverBufManageVOMap==null||serverBufManageVOMap.size()==0){
			return null;
		}else{
			List<DipManagerserverVO> retlist=new ArrayList<DipManagerserverVO>();
			Iterator it=serverBufManageVOMap.keySet().iterator();
			while(it.hasNext()){
				
				String key=(String) it.next();
				retlist.add(serverBufManageVOMap.get(key));
			}
			return retlist;
		}
	}
	/**
	 * @author wyd 
	 * @desc 得到消息服务类型的所有服务管理
	 * */
	public List<DipManagerserverVO> getMsgBufVO(){
//TODO wyd 把时间段的那种加进来，读时间，定时去跑
		synchronized (lock){
			List<MessservtypeVO> mtvo=null;
			try {
				mtvo = (List<MessservtypeVO>) new BaseDAO(IContrastUtil.DESIGN_CON).retrieveByClause(MessservtypeVO.class,"nvl(dr,0)=0");
			} catch (DAOException e) {
				e.printStackTrace();
			}
			//如果当前的缓存里的服务管理是空的那么把所有的都查询出来
			if(messServerBuufer==null||messServerBuufer.size()<=0){
				if(mtvo!=null&&mtvo.size()>0){
					messServerBuufer=new ArrayList<DipManagerserverVO>();
					for(MessservtypeVO tvo:mtvo){
						if(tvo.dr!=null&&tvo.dr==1){
							continue;
						}
						DipManagerserverVO vo=new DipManagerserverVO();
						vo.setMesvo(tvo);
						vo.setPk_managerserver(tvo.getPrimaryKey());
						vo.setIsmsg(true);
						vo.setSysname(tvo.getName());
						messServerBuufer.add(vo);
//						getOrSetUpdateBufList(null, false, vo);
					}
				}
			}else{//如果当前缓存里的服务管理不是空的，那么，再查询出来所有的，然后与当前的比对，如果当前的没有包含查询出来的，那么给他添加进去
				if(mtvo!=null&&mtvo.size()>0){
					for(MessservtypeVO tvo:mtvo){
						if(tvo.dr!=null&&tvo.dr==1){
							continue;
						}
						DipManagerserverVO vo=new DipManagerserverVO();
						vo.setPk_managerserver(tvo.getPrimaryKey());
						vo.setIsmsg(true);
						vo.setSysname(tvo.getName());
						boolean iscontain=false;
						for(DipManagerserverVO msb:messServerBuufer){
							if(msb.getPrimaryKey().equals(tvo.getPrimaryKey())){
								iscontain=true;
								break;
							}
						}
						if(!iscontain){
							messServerBuufer.add(vo);
							getOrSetUpdateBufList(null, false, vo);
						}
					}
				}
			}
			return messServerBuufer;
		}
	}
	/**
	 * @author wyd
	 * @desc 根据线程名称，返回缓存中的服务VO:预警任务类型的
	 * @param threadName 当前线程名称
	 * */
	public DipManagerserverVO getServerVOByThreadname(String threadName){
		if(serverBufManageVOMap==null||serverBufManageVOMap.size()==0){
			setServerBufferManage(threadName,null);
		}
		return serverBufManageVOMap.get(threadName);
	}

	public DipManagerserverVO getRunMesVO(String threadName) {

		synchronized (lock){
			List<DipManagerserverVO> list=getMsgBufVO();
			if(list==null||list.size()<=0){
				return null;
			}else{
				DipManagerserverVO retvo=null;
				for(DipManagerserverVO vo:list){
					if(vo!=null&&(vo.getMesvo()!=null)){
                        //2012-05-31 modify liyunzhe 把table名称改成线程名称，个人认为这里写错了，这个和表名没有关系，表名是vo中必须有的。
						//						if((vo.getThreadname()==null||vo.getTableName().length()<=0)
						if((vo.getThreadname()==null||vo.getThreadname().length()<=0)
								&&((vo.getMstatus()!=null&&vo.getMstatus().equals("运行"))
								||(vo.getMstatus()!=null&&vo.getMstatus().equals("运行启动中"))||
								(vo.getMstatus()==null||vo.getMstatus().length()==0))){
//							2012-05-31 liyunzhe modify end	
							MessservtypeVO mesvo=vo.getMesvo();
							if(threadName.startsWith(mesvo.getVdef2())){
								if(mesvo.getType().equals("即时启动")){
									retvo=vo;
									break;
								}else{
		
									SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									try {
										Date d1=sd.parse(mesvo.getVdef4());
										Date d2=sd.parse(mesvo.getVdef5());
										Date d3=new Date();
										if(d1.getTime()<=d3.getTime()&&d2.getTime()>=d3.getTime()){
											retvo=vo;
											break;
										}
										
									} catch (ParseException e) {
										e.printStackTrace();
									}
								}
							}
						}else if(vo.getMstatus()!=null&&vo.getMstatus().equals("停止中")){
							vo.setThreadname(null);
							vo.setMstatus("停止");
							vo.setStartandstorp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
							vo.setRunservice(vo.getMesvo().getVdef2());
							vo.setWorkcont(null);
						}
					}
				}
				if(retvo!=null){
					retvo.setStartandstorp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					retvo.setRunservice(retvo.getMesvo().getVdef2());
					retvo.setThreadname(threadName);
					retvo.setWorkcont(threadName);
					retvo.setMstatus("运行");
				}
				return retvo;
			}
		}    	
    }
//	private static Map BJmap=new HashMap<String, Map>();
//	public Map getBJmap(){
//		return BJmap;
//	}
	/**
	 * @desc 停止或启动消息服务
	 * @param String pk_server 消息服务主键
	 * @param boolean isStart 启动是true;停用是false;
	 * */
	public boolean startOrStopMsgServer(String pk_server,boolean isStart){
		boolean issus=false;
		if(pk_server==null){
			return issus;
		}
		synchronized (lock){
			List<DipManagerserverVO> dmvos=getMsgBufVO();
			if(dmvos==null||dmvos.size()<=0){
				return false;
			}
			for(DipManagerserverVO vo:dmvos){
				if(vo.getPrimaryKey().equals(pk_server)){
					if(isStart&&(vo.getMstatus()!=null&&!vo.getMstatus().equals("运行"))){
						getOrSetUpdateBufList(vo.getRunservice(),false, vo);
						vo.setMstatus("运行启动中");
						vo.setRunservice(vo.getMesvo().getVdef2());
						vo.setStartandstorp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					}else if(!isStart&&vo.getMstatus()!=null&&!vo.getMstatus().equals("停止")){
						if(vo.getRunservice()==null||vo.getRunservice().length()==0){
							vo.setMstatus("停止");
							vo.setRunservice(vo.getMesvo().getVdef2());
						}else{
							vo.setMstatus("停止中");
							vo.setRunservice(vo.getMesvo().getVdef2());
							vo.setThreadname(null);
							vo.setWorkcont(null);
							getOrSetUpdateBufList(vo.getRunservice(),false, vo);
						}
					}
					return true;
				}
			}
			return issus;
			
		}
		
	}
//	BaseDAO bd=null;
//	private BaseDAO getBaseDAO(){
//		if(bd==null){
//			bd=new BaseDAO(IContrastUtil.DESIGN_CON);
//		}
//		return bd;
//	}
//	public void innitialBj(DipManagerserverVO dvo){
//		MessservtypeVO mtvo=dvo.getMesvo();
//		if(mtvo==null){
//			Logger.debug("没有找到对应的消息格式的连接定义");
//			return;
//		}
//		//得到消息服务器的注册信息
//		String ippk=mtvo.getVdef1();
//		if(ippk==null||ippk.length()<=0){
//			Logger.debug("没有找到消息服务器的主键");
//			return;
//		}
//		try{
//			MsrVO mvo=(MsrVO) getBaseDAO().retrieveByPK(MsrVO.class, ippk);
//			if(mvo==null){
//				Logger.debug("没有查询到消息服务器配置"+ippk);
//				return;
//			}
//			//查询分割标记的主键
//			String gs=mvo.getFgbj();
//			if(gs==null||gs.length()<=0){
//				Logger.debug("消息服务器配置没有定义分割标记，或者没有保存分割标记的主键");
//				return;
//			}
//			//1、查到数据接收、系统、数据定义的联合vo
//			List<VDipDsreceiveVO> vdiprvos=(List<VDipDsreceiveVO>) getBaseDAO().retrieveByClause(VDipDsreceiveVO.class, " bjpk='"+gs+"' and nvl(dr,0)=0 and sourcetype='0001AA10000000013SVI' and bj is not null");
//			if(vdiprvos==null||vdiprvos.size()<=0){
//				Logger.debug("没有找到分割标记主键【"+gs+"】对应的系统或者接收类型为消息总线的接收定义");
//				return;
//			}
//			//2、接收格式的找到的key
//			Map<String,String> jamap=new HashMap<String,String>();//接收主键，格式key
//			String pkmemtab="";//数据定义主表主键
//			Map<String,String> dymap=new HashMap<String, String>();//key 数据定义主表主键，Value 数据接收定义的主表主键
//			String bj=vdiprvos.get(0).getBj();
//			String jspk="";//数据接收的主键
//			Map<String ,VDipDsreceiveVO> dsmap=new HashMap<String, VDipDsreceiveVO>();//接收主键，联合vo
//			for(VDipDsreceiveVO jsvoi:vdiprvos){
//				jspk=jspk+"'"+jsvoi.getPk_datarec_h()+"',";
//				pkmemtab=pkmemtab+"'"+jsvoi.getPk_datadefinit_h()+"',";
//				dsmap.put(jsvoi.getPk_datarec_h(), jsvoi);
//				dymap.put(jsvoi.getPk_datadefinit_h(), jsvoi.getPk_datarec_h());
//				jamap.put(jsvoi.getPk_datarec_h(), bj+jsvoi.getExtcode()+bj+(jsvoi.getSitecode()==null||jsvoi.getSitecode().length()<=0?(jsvoi.getDef_str_1()==null?"000000":jsvoi.getDef_str_1()):jsvoi.getSitecode())+bj+jsvoi.getBusicode()+bj);
//			}
//			//3、数据接收对应的数据定义的格式,map，key数据接收主键，value表格式
//			Map<String,List<DipDatadefinitBVO>> ddbmap=new HashMap<String, List<DipDatadefinitBVO>>();//key 数据接收定义的主表主键，value 数据格式
//			List<DipDatadefinitBVO> ddfbvos=(List<DipDatadefinitBVO>) getBaseDAO().retrieveByClause(DipDatadefinitBVO.class, "pk_datadefinit_h in ("+pkmemtab.substring(0,pkmemtab.length()-1)+") and nvl(dr,0)=0");
//			if(ddfbvos==null||ddfbvos.size()<=0){
//				Logger.debug("没有找到数据定义附表，主表主键为："+pkmemtab);
//				return;
//			}else{
//				for(DipDatadefinitBVO bvod:ddfbvos){
//					String key=dymap.get(bvod.getPk_datadefinit_h());
//					List l;
//					if(ddbmap.containsKey(key)){
//						l=ddbmap.get(key);
//					}else{
//						l=new ArrayList<DipDatadefinitBVO>();
//					}
//					l.add(bvod);
//					ddbmap.put(key, l);
//				}
//			}
//			//数据校验的方法
//			List<DataverifyHVO> vhvos=(List<DataverifyHVO>) getBaseDAO().retrieveByClause(DataverifyHVO.class, "pk_datachange_h in ("+jspk.substring(0,jspk.length()-1)+") and nvl ( dr, 0 ) = 0");
//			String vhvostr="";
//			Map<String ,String> vmap=new HashMap<String, String>();//数据校验的主表的map,key是数据校验的主表主键，value是数据接收定义的主表主键
//			Map<String ,List> verfiemap=new HashMap<String,List>();//数据校验的map，key是数据接收的主表主键，值是校验的list
//			if(vhvos!=null&&vhvos.size()>0){
//				for(DataverifyHVO vhvo:vhvos){
//					if(!vmap.containsKey(vhvo.getPrimaryKey())){
//						vmap.put(vhvo.getPrimaryKey(), vhvo.getPk_datachange_h());
//					}
//					vhvostr=vhvostr+"'"+vhvo.getPrimaryKey()+"',";
//				}
//				vhvostr=vhvostr.substring(0,vhvostr.length()-1);
//				List<DataverifyBVO> vbvos=(List<DataverifyBVO>) getBaseDAO().retrieveByClause(DataverifyBVO.class, "pk_dataverify_h in ("+vhvostr+") and nvl(dr,0)=0");
//				if(vbvos!=null&&vbvos.size()>0){
//					for(DataverifyBVO bvoi:vbvos){
//						String key=vmap.get(bvoi.getPk_dataverify_h());
//						List fa;
//						if(verfiemap.containsKey(key)){
//							fa=verfiemap.get(key);
//						}else{
//							fa=new ArrayList<String>();
//						}
//						fa.add(bvoi.getName());
//						verfiemap.put(key, fa);
//					}
//				}
//			}
//			//接收的格式
//			List<DataformatdefHVO> gsh=(List<DataformatdefHVO>) getBaseDAO().retrieveByClause(DataformatdefHVO.class, "pk_datarec_h in ("+jspk.substring(0,jspk.length()-1)+") and nvl(dr,0)=0");
//			if(gsh==null||gsh.size()<=0){
//				return;
//			}
//			Map gszb=new HashMap<String, String>();//格式主表主键与流类型的Map
//			Map<String, List> jsygs=new HashMap<String, List>();//接收与格式的Map
//			String gszbpk="";
//			for(DataformatdefHVO hvos:gsh){
//				gszb.put(hvos.getPrimaryKey(),hvos.getMessflowtype());
//				gszbpk=gszbpk+"'"+hvos.getPrimaryKey()+"',";
//				List list;
//				if(jsygs.containsKey(hvos.getPk_datarec_h())){
//					list=jsygs.get(hvos.getPk_datarec_h());
//				}else{
//					list=new ArrayList<String>();
//				}
//				list.add(hvos.getPrimaryKey());
//				jsygs.put(hvos.getPk_datarec_h(), list);
//			}
//			List<DataformatdefBVO> fmb=(List<DataformatdefBVO>) getBaseDAO().retrieveByClause(DataformatdefBVO.class, "pk_dataformatdef_h in ("+gszbpk.substring(0,gszbpk.length()-1)+")","code");
//			if(fmb==null||fmb.size()<=0){
//				return;
//			}
//			HashMap<String, ArrayList> blm=new HashMap<String, ArrayList>();//主表主键和vo的映射
//			for(DataformatdefBVO bvo:fmb){
//				String pkh=bvo.getPk_dataformatdef_h();
//				ArrayList<DataformatdefBVO> l;
//				if(blm.containsKey(pkh)){
//					l=blm.get(pkh);
//				}else{
//					l=new ArrayList<DataformatdefBVO>();
//				}
//				l.add(bvo);
//				blm.put(pkh, l);
//			}
//			Iterator it=jamap.keySet().iterator();
//			String keys="";
//			Map gsmap=new HashMap<String, EsbMapUtilVO>();
//			while(it.hasNext()){
//				//3、接收主键，格式key,jamap
//				//1、根据接收与格式的关系，找格式gszb
//				//2、根据格式的主表主键，找到流和主表主键的关系 blm 主表主键和vo的映射
//				String key=(String) it.next();//数据接收的主表主键
//				if(jsygs.containsKey(key)){
//					List<String> l=(List<String>) jsygs.get(key);//格式主表主键与流类型的Map
//					if(l!=null&&l.size()>0){
////						Map<String,Object> map=new HashMap<String, Object>();
////						String ikey="";
//						for(String s:l){//得到的是格式定义的主表主键
//							if(blm.containsKey(s)){
//								List<DataformatdefBVO> dal=blm.get(s);
//								String data="";
//								String ltype=(String) gszb.get(s);//得到是流类型；
//								if(dal!=null&&dal.size()>0){
//									data=bj+dal.get(0).getDatakind()+jamap.get(key);
//									String mapbj="";
//									if(ltype.equals("0001AA100000000142YQ")){//开始
//										mapbj="b";
//									}else if(ltype.equals("0001AA100000000142YS")){//数据
//										mapbj="d";
//									}else if(ltype.equals("0001ZZ10000000018K7M")){//结束
//										mapbj="e";
//									}
//									EsbMapUtilVO vo=new EsbMapUtilVO();
//									if(verfiemap!=null&&verfiemap.containsKey(key)){
//										vo.setCheckname(verfiemap.get(key));//数据校验
//									}
//									if(ddbmap!=null&&ddbmap.containsKey(key)){
//										vo.setTypeddb(ddbmap.get(key));//数据定义
//									}
//									vo.setTablename(dsmap.get(key).getMemorytable());//存储表名
//									vo.setSysvo(dsmap.get(key));//联合vo
//									vo.setBj(bj);//标记
//									vo.setType(mapbj);//类型
//									vo.setData(dal);//数据项，估计就是格式
//									vo.setKey(jamap.get(key));//key串
//									gsmap.put(data.toUpperCase(), vo);
//									keys=keys+"【"+data.toUpperCase()+"】,";
//								}
//							}
//						}
//					}
//				}
//				
//			}
//			BJmap.put(gs,gsmap);
//			Logger.debug("查询到的格式有："+(keys.length()>0?keys.substring(0,keys.length()-1):""));
//			DateprocessVO vop=new DateprocessVO();
//			vop.setActivetype("数据接收");
//			vop.setActive("ESB消息接收");
//			vop.setSdate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//			vop.setContent("查询到的格式有："+(keys.length()>0?keys.substring(0,keys.length()-1):""));
//			ILogAndTabMonitorSys ilm=(ILogAndTabMonitorSys) NCLocator.getInstance().lookup(ILogAndTabMonitorSys.class.getName());
//			ilm.writToDataLog(vop);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	
//	}
	
	/**
	 * @desc 把服务管理的消息服务的状态改为没有线程运行
	 * @param DipManagerserverVO mvo 服务管理VO
	 * */
	public boolean updateMesVO(DipManagerserverVO mvo) {
		if(mvo==null||mvo.getPrimaryKey()==null||mvo.getPrimaryKey().length()<=0){
			return false;
		}
		String pk=mvo.getPrimaryKey();
		List<DipManagerserverVO> dmvos= getMsgBufVO();
		if(dmvos==null||dmvos.size()<=0){
			return false;
		}else{
			for(DipManagerserverVO vo:dmvos){
				if(vo.getPrimaryKey().equals(pk)){
					MessservtypeVO tvo=vo.getMesvo();
					if(tvo.getType()!=null&&tvo.getType().equals("时间段")){
						String pkstart=tvo.getVdef4();
						String pkend=tvo.getVdef5();
						TimingSettingVO tsvos=null;
						TimingSettingVO tsvoe=null;
						if(pkstart!=null&&pkend!=null){
							try {
								BaseDAO dao=new BaseDAO(IContrastUtil.DESIGN_CON);
								tsvos=(TimingSettingVO) dao.retrieveByPK(TimingSettingVO.class, pkstart);
								tsvoe=(TimingSettingVO) dao.retrieveByPK(TimingSettingVO.class, pkend);
								TimeConfigVO tcvos=PaConstant.transTimingSettingVO2TimeConfigVO(tsvos);
								TimeConfigVO tcvoe=PaConstant.transTimingSettingVO2TimeConfigVO(tsvoe);
								if(tcvos!=null&&tcvoe!=null){
									tcvos.resume();
									tcvoe.resume();
									Long times=tcvos.getScheExecTime();
									Long timee=tcvoe.getScheExecTime();
									String timess=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(times));
									String timese=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timee));
									String sql="update dip_messservtype set dlm='"+timess+"',vdef3='"+timese+"' where pk_messservtype='"+tvo.getPrimaryKey()+"'";
									dao.executeUpdate(sql);
									tvo.setVdef3(timese);
									tvo.setDlm(timess);
								}
							} catch (DAOException e) {
								e.printStackTrace();
							}catch(Exception e){
								e.printStackTrace();
							}
						}
					}
					if(vo.getMstatus()!=null&&vo.getMstatus().equals("停止中")){
						vo.setMstatus("停止");
					}
//					vo.setRunservice(vo.getm);
					vo.setStartandstorp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					return true;
				}
			}
		}
		return false;
	}
	
	public List<DipManagerserverVO> syhDipMserState(String servername){
		List<DipManagerserverVO> dmvos= getMsgBufVO();
		List<DipManagerserverVO> retl=new ArrayList<DipManagerserverVO>();
		if(dmvos==null||dmvos.size()<=0){
			return null;
		}else{
			for(DipManagerserverVO vo:dmvos){
				MessservtypeVO mtvo=vo.getMesvo();
					if(mtvo.getVdef2()!=null&&mtvo.getVdef2().equals(servername)){
						retl.add((DipManagerserverVO)vo.clone());
//						updateMesVO(vo);
					}
			}
		}
		return retl;
		
	}
	/**
	 * @desc 判断当前的服务是否还是启动着的
	 * */
	public boolean isStart(DipManagerserverVO mvo){
		if(mvo==null||mvo.getPrimaryKey()==null||mvo.getPrimaryKey().length()<=0){
			return false;
		}
		String pk=mvo.getPrimaryKey();
		List<DipManagerserverVO> dmvos= getMsgBufVO();
		if(dmvos==null||dmvos.size()<=0){
			return false;
		}else{
			for(DipManagerserverVO vo:dmvos){
				if(vo.getPrimaryKey().equals(pk)){
					if((vo.getMstatus()==null||vo.getMstatus().length()==0)||(vo.getMstatus()!=null&&(vo.getMstatus().equals("运行")||vo.getMstatus().equals("运行启动中")))){
						return true;
					}
				}
			}
		}
		return false;
	}
	public static void main(String[] arg){
		List<DipManagerserverVO> list=new ArrayList<DipManagerserverVO>();
		DipManagerserverVO mvo=new DipManagerserverVO();
		mvo.setMstatus("wworek");
		list.add(mvo);
		
		for(DipManagerserverVO vo:list){
			vo.setMstatus("kk");
		}
		System.out.println(list.get(0).getMstatus());
	}
	static Map<String,List<DipManagerserverVO>>BUFLISTMAP=new HashMap<String, List<DipManagerserverVO>>();
	public List<DipManagerserverVO> getOrSetUpdateBufList(String servername, boolean isget, DipManagerserverVO dmsvo) {
		synchronized(lock){
			List listm=null;
			if(isget){
				if(BUFLISTMAP!=null&&BUFLISTMAP.containsKey(servername)){
					listm=BUFLISTMAP.get(servername);
				}
				BUFLISTMAP.put(servername, new ArrayList<DipManagerserverVO>());
			}else{
				MessservtypeVO mtvo=dmsvo.getMesvo();
				servername=mtvo.getVdef2();
				if(servername!=null&&servername.length()>0){
					if(BUFLISTMAP.containsKey(servername)){
						listm=BUFLISTMAP.get(servername);
					}
					if(listm==null){
						listm=new ArrayList<DipManagerserverVO>();
					}else{
						for(int i=0;i<listm.size();i++){
							DipManagerserverVO voi=(DipManagerserverVO) listm.get(i);
							if(voi.getPrimaryKey().equals(dmsvo.getPrimaryKey())){
								listm.remove(voi);
							}
						}
					}
					listm.add(dmsvo);
				}
				BUFLISTMAP.put(servername, listm);
			}
			return listm;
		}
	}
	
}
