package nc.pub.dip.startup;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.impl.dip.pub.EsbMapUtilVO;
import nc.itf.dip.pub.ILogAndTabMonitorSys;
import nc.itf.dip.pub.IServerBufferManage;
import nc.util.dip.sj.IContrastUtil;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.dataformatdef.DataformatdefBVO;
import nc.vo.dip.dataformatdef.DataformatdefHVO;
import nc.vo.dip.datarec.DipDatarecSpecialTab;
import nc.vo.dip.dataverify.DataverifyBVO;
import nc.vo.dip.dataverify.DataverifyHVO;
import nc.vo.dip.dateprocess.DateprocessVO;
import nc.vo.dip.managerserver.DipManagerserverVO;
import nc.vo.dip.messagelogo.MessagelogoVO;
import nc.vo.dip.runsys.DipRunsysBVO;
import nc.vo.dip.util.DipCountVO;
import nc.vo.dip.view.VDipDsreceiveVO;

public class ThreadUtil implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3616420711960742658L;
	private static Map<String, EsbMapUtilVO> instance = null; 

	public static synchronized Map getInstanceMap() {
		if (instance==null) 
			instance=new HashMap<String, EsbMapUtilVO>(); 
		return instance; 
	} 
	private static BaseDAO bd;
	public static byte lock[] = new byte[0]; 
	public static BaseDAO getBaseDAO(){
		if(bd==null){
			bd=new BaseDAO(IContrastUtil.DESIGN_CON);
		}
		return bd;
	}
	public final static int OPTYPE_B=0;//开始标志
	public final static int OPTYPE_D=1;//数据计数
	public final static int OPTYPE_E=2;//接数计数
	public final static int OPTYPE_C=3;//数据校验的计数器标志
	public final static int OPTYPE_S=4;//保存成功失败的计数器
	public final static int OPTYPE_M=5;//报文计数器
	
	public final static String COUNT_C="count";//计数总数
	
	private static Map<String,DipCountVO> count=new Hashtable<String, DipCountVO>();
	/**
	 * @desc 计数器
	 * @param pk_rec 哪个定义的接数主键
	 * @param opType 标记代表什么意思
	 *OPTYPE_B=0;//开始标志
	 *OPTYPE_D=1;//数据计数
	 *OPTYPE_E=2;//接数计数
	 * */
	public static DipCountVO getOrSetcount(String pk_rec,int opType,String cname){
		DipCountVO countret=null;
		if(opType==OPTYPE_B){
			DipCountVO cla=new DipCountVO();
			count.put(pk_rec, cla);
		}else if(opType==OPTYPE_D){
			if(count.containsKey(pk_rec)){
				DipCountVO c=count.get(pk_rec);
				c.setAddCount();
				count.put(pk_rec, c);
			}
		}else if(opType==OPTYPE_E){
			if(count.containsKey(pk_rec)){
				countret=count.get(pk_rec);
				count.remove(pk_rec);
			}
		}else if(opType==OPTYPE_C){
			if(count.containsKey(pk_rec)){
				if(cname!=null&&cname.length()>0){
					countret=count.get(pk_rec);
						if(cname.endsWith("f")){
							countret.setAddFClass(cname.substring(0,cname.length()-1));
						}else{
	
							countret.setAddCClass(cname.substring(0,cname.length()-1));
						}
						
					count.put(pk_rec, countret);
				}
			}
		}else if(opType==OPTYPE_S){
			if(count.containsKey(pk_rec)){
				if(cname!=null&&cname.length()>0){
				countret=count.get(pk_rec);
					if(cname.endsWith("f")){
						countret.setAddFcount();
					}else{

						countret.setAddScount();
					}
					
				count.put(pk_rec, countret);
				}
			}
		}else if(opType==OPTYPE_M){
			if(count.containsKey(pk_rec)){
				DipCountVO c=count.get(pk_rec);
				c.setAddMcount();
				count.put(pk_rec, c);
			}
		}
		return countret;
	}
	static Map<String,String> hzmap=new HashMap<String, String>();
	public static Map<String,String> getHzMap(){
		return hzmap;//key 是 pk_datarec_h,value 是回执str 可能包含时间函数，记录数函数等等。
		             //key 还可能是pk_datarec_h+‘s’,value 是回执服务器注册pk
	}
	public static void Instance(/*DipManagerserverVO dvo*/){
	/*	MessservtypeVO mtvo=dvo.getMesvo();
		if(mtvo==null){
			Logger.debug("没有找到对应的消息格式的连接定义");
			return;
		}
		//得到消息服务器的注册信息
		String ippk=mtvo.getVdef1();
		if(ippk==null||ippk.length()<=0){
			Logger.debug("没有找到消息服务器的主键");
			return;
		}*/
		try{
			
//			String ss=" select sysvalue from dip_runsys_b where syscode='DIP-0000011' and nvl(dr,0)=0 ";
			List<DipRunsysBVO> runbvo=(List<DipRunsysBVO>) getBaseDAO().retrieveByClause(DipRunsysBVO.class, " syscode='DIP-0000011' and nvl(dr,0)=0 ");
			String esc=null;//转义字符运行参数
			if(runbvo!=null&&runbvo.size()==1){
				if(runbvo.get(0).getSysvalue()!=null&&!runbvo.get(0).getSysvalue().equals("")){
					esc=runbvo.get(0).getSysvalue();
					Logger.debug("转义字符运行参数值是------"+esc);
				}
			}
			
		/*	
			MsrVO mvo=(MsrVO) getBaseDAO().retrieveByPK(MsrVO.class, ippk);
			if(mvo==null){
				Logger.debug("没有查询到消息服务器配置"+ippk);
				return;
			}*/
			//查询分割标记的主键
			/*String gs=mvo.getFgbj();
			if(gs==null||gs.length()<=0){
				Logger.debug("消息服务器配置没有定义分割标记，或者没有保存分割标记的主键");
				return;
			}*/
			//1、查到数据接收、系统、数据定义的联合vo
			List<VDipDsreceiveVO> vdiprvos=(List<VDipDsreceiveVO>) getBaseDAO().retrieveByClause(VDipDsreceiveVO.class, " (ioflag is null or ioflag='输入')  and nvl(dr,0)=0 and sourcetype='0001AA10000000013SVI' and bj is not null");
			if(vdiprvos==null||vdiprvos.size()<=0){
				Logger.debug("没有找到接收类型为消息总线的接收定义");
				return;
			}
			//2、接收格式的找到的key
			Map<String,String> jamap=new HashMap<String,String>();//接收主键，格式key   ，Pk_datarec_h，分割标记+系统标志+分割标志+站点标志+分割标志+业务标志+分割标记
			String pkmemtab="";//数据定义主表主键
			/**
			 * //key 数据定义主表主键，Value 数据接收定义的主表主键
			 * Pk_datadefinit_h(),Pk_datarec_h()
			 */
			Map<String,String> dymap=new HashMap<String, String>();//key 数据定义主表主键，Value 数据接收定义的主表主键
			String jspk="";//数据接收的主键
			Map<String ,VDipDsreceiveVO> dsmap=new HashMap<String, VDipDsreceiveVO>();//同步定义主键 pk_Datarec_h，联合VDipDsreceiveVO
			for(VDipDsreceiveVO jsvoi:vdiprvos){
//				String bj=vdiprvos.get(0).getBj();
				String bj=jsvoi.getBj();
				jspk=jspk+"'"+jsvoi.getPk_datarec_h()+"',";
				List<DipDatarecSpecialTab> listspecialhvo=(List<DipDatarecSpecialTab>) getBaseDAO().retrieveByClause(DipDatarecSpecialTab.class, "pk_datarec_h='" + jsvoi.getPk_datarec_h() + "' and (nvl(dr,0)=0 ) and is_back='Y' ");
				String xtbz="";
				String zdbz="";
				String ywbz="";
				if(listspecialhvo!=null&&listspecialhvo.size()>0){
					for(int i=0;i<listspecialhvo.size();i++){
						DipDatarecSpecialTab specialvo=listspecialhvo.get(i);
						if(specialvo.getName().equals("系统标志")){
							String xtvalue=specialvo.getValue();
							if(xtvalue!=null&&xtvalue.trim().length()>0){
								xtbz=xtvalue;
							}
						}
						if(specialvo.getName().equals("站点标志")){
							String zdvalue=specialvo.getValue();
							if(zdvalue!=null&&zdvalue.trim().length()>0){
								zdbz=zdvalue;
							}
						}
						if(specialvo.getName().equals("业务标志")){
							String ywvalue=specialvo.getValue();
							if(ywvalue!=null&&ywvalue.trim().length()>0){
								ywbz=ywvalue;
							}
						}
					}
					
					
				}
				
				
				pkmemtab=pkmemtab+"'"+jsvoi.getPk_datadefinit_h()+"',";
				dsmap.put(jsvoi.getPk_datarec_h(), jsvoi);
				dymap.put(jsvoi.getPk_datadefinit_h(), jsvoi.getPk_datarec_h());
				jamap.put(jsvoi.getPk_datarec_h(), bj+jsvoi.getExtcode()+bj+(jsvoi.getSitecode()==null||jsvoi.getSitecode().length()<=0?(jsvoi.getDef_str_1()==null?"000000":jsvoi.getDef_str_1()):jsvoi.getSitecode())+bj+jsvoi.getBusicode()+bj);
				if(jsvoi.getBackcon()!=null&&jsvoi.getBackcon().equals("Y")){
					if(getHzMap()==null||!getHzMap().containsKey(jsvoi.getPk_datarec_h())){
						List<DataformatdefBVO> listi=(List<DataformatdefBVO>) getBaseDAO().retrieveByClause(DataformatdefBVO.class, "pk_dataformatdef_h=(" +
								"select pk_dataformatdef_h from dip_dataformatdef_h where messflowtype='0001ZZ1000000001GFD7' and pk_datarec_h='"+jsvoi.getPk_datarec_h()+"'" +
								" and rownum<2 and nvl(dr,0)=0) order by code");
						if(listi!=null&&listi.size()>0){
							String rets="";
							for(int i=0;i<listi.size();i++){
								DataformatdefBVO bvoi=listi.get(i);
								if(bvoi.getVdef2().equals("业务数据")){
									rets=rets+",";
								}else if(bvoi.getVdef2().equals("标志头")||bvoi.getVdef2().equals("标志尾")||bvoi.getVdef2().equals("自定义")){
									rets=rets+(bvoi.getDatakind()!=null&&bvoi.getDatakind().length()>0?bvoi.getDatakind():"")+",";
								}else if(bvoi.getVdef2().equals("固定标志")&&bvoi.getVdef3().equals("业务标志")){
									if(!ywbz.trim().equals("")){
										rets=rets+ywbz+",";
									}else{
										rets=rets+jsvoi.getBusicode()+",";
									}
								}else if(bvoi.getVdef2().equals("固定标志")&&bvoi.getVdef3().equals("系统标志")){
									if(!xtbz.trim().equals("")){
										rets=rets+xtbz+",";
									}else{
										rets=rets+jsvoi.getExtcode()+",";
									}
								}else if(bvoi.getVdef2().equals("固定标志")&&bvoi.getVdef3().equals("站点标志")){
									if(!zdbz.trim().equals("")){
										rets=rets+zdbz+",";
									}else{
										rets=rets+jsvoi.getSitecode()+",";	
									}
									
								}else if(bvoi.getVdef2().equals("时间函数")){
									rets=rets+"时间函数<"+bvoi.getDatakind()+">"+",";
								}else if(bvoi.getVdef2().equals("主键函数")){
									rets=rets+"主键函数"+",";
								}else if(bvoi.getVdef2().equals("记录数函数")){
									rets=rets+"记录数函数"+",";
								}else if(bvoi.getVdef2().equals("报文数函数")){
									rets=rets+"报文数函数"+",";
								}else{
									rets=rets+bvoi.getVdef3()+",";
								}
							}
							rets=rets.substring(0,rets.length()-1);
							getHzMap().put(jsvoi.getPk_datarec_h(), rets);
						}else{
							getHzMap().put(jsvoi.getPk_datarec_h(), null);
						}
						getHzMap().put(jsvoi.getPk_datarec_h()+"s", jsvoi.getBackmsr());//增加回执服务器pk
					}
				}
			}
			//3、数据接收对应的数据定义的格式,map，key数据接收主键，value表格式
			Map<String,List<DipDatadefinitBVO>> ddbmap=new HashMap<String, List<DipDatadefinitBVO>>();//key 数据接收定义的主表主键，value 数据格式， pk_Datarec_h,value 是DipDatadefinitBVO list,是整个表对应的 list
			List<DipDatadefinitBVO> ddfbvos=(List<DipDatadefinitBVO>) getBaseDAO().retrieveByClause(DipDatadefinitBVO.class, "pk_datadefinit_h in ("+pkmemtab.substring(0,pkmemtab.length()-1)+") and nvl(dr,0)=0");
			if(ddfbvos==null||ddfbvos.size()<=0){
				Logger.debug("没有找到数据定义附表，主表主键为："+pkmemtab);
				return;
			}else{
				for(DipDatadefinitBVO bvod:ddfbvos){
					String key=dymap.get(bvod.getPk_datadefinit_h());
					List l;
					if(ddbmap.containsKey(key)){
						l=ddbmap.get(key);
					}else{
						l=new ArrayList<DipDatadefinitBVO>();
					}
					l.add(bvod);
					ddbmap.put(key, l);
				}
			}
			//数据校验的方法
			List<DataverifyHVO> vhvos=(List<DataverifyHVO>) getBaseDAO().retrieveByClause(DataverifyHVO.class, "pk_datachange_h in ("+jspk.substring(0,jspk.length()-1)+") and nvl ( dr, 0 ) = 0");
			String vhvostr="";
			Map<String ,String> vmap=new HashMap<String, String>();//数据校验的主表的map,key是数据校验的主表主键，value是数据接收定义的主表主键
			Map<String ,List> verfiemap=new HashMap<String,List>();//数据校验的map，key是数据接收的主表主键，值是校验的list
			if(vhvos!=null&&vhvos.size()>0){
				for(DataverifyHVO vhvo:vhvos){
					if(!vmap.containsKey(vhvo.getPrimaryKey())){
						vmap.put(vhvo.getPrimaryKey(), vhvo.getPk_datachange_h());
					}
					vhvostr=vhvostr+"'"+vhvo.getPrimaryKey()+"',";
				}
				vhvostr=vhvostr.substring(0,vhvostr.length()-1);
				List<DataverifyBVO> vbvos=(List<DataverifyBVO>) getBaseDAO().retrieveByClause(DataverifyBVO.class, "pk_dataverify_h in ("+vhvostr+") and nvl(dr,0)=0");
				if(vbvos!=null&&vbvos.size()>0){
					for(DataverifyBVO bvoi:vbvos){
						String key=vmap.get(bvoi.getPk_dataverify_h());
						List fa;
						if(verfiemap.containsKey(key)){
							fa=verfiemap.get(key);
						}else{
							fa=new ArrayList<String>();
						}
						fa.add(bvoi.getName());
						verfiemap.put(key, fa);
					}
				}
			}
			//接收的格式
			List<DataformatdefHVO> gsh=(List<DataformatdefHVO>) getBaseDAO().retrieveByClause(DataformatdefHVO.class, "pk_datarec_h in ("+jspk.substring(0,jspk.length()-1)+") and nvl(dr,0)=0");
			if(gsh==null||gsh.size()<=0){
				return;
			}
			Map gszb=new HashMap<String, String>();//格式主表主键与流类型的Map  ，pk_dataformat_h, 流类型，是开始，数据，结束
			Map<String, List> jsygs=new HashMap<String, List>();//接收与格式的Map  key Pk_datarec_h,list Pk_dataformatdef_h主键s
			String gszbpk="";
			for(DataformatdefHVO hvos:gsh){
				gszb.put(hvos.getPrimaryKey(),hvos.getMessflowtype());
				gszbpk=gszbpk+"'"+hvos.getPrimaryKey()+"',";
				List list;
				if(jsygs.containsKey(hvos.getPk_datarec_h())){
					list=jsygs.get(hvos.getPk_datarec_h());
				}else{
					list=new ArrayList<String>();
				}
				list.add(hvos.getPrimaryKey());
				jsygs.put(hvos.getPk_datarec_h(), list);
			}
			List<DataformatdefBVO> fmb=(List<DataformatdefBVO>) getBaseDAO().retrieveByClause(DataformatdefBVO.class, "pk_dataformatdef_h in ("+gszbpk.substring(0,gszbpk.length()-1)+")","code");
			if(fmb==null||fmb.size()<=0){
				return;
			}
			HashMap<String, ArrayList> blm=new HashMap<String, ArrayList>();//主表主键和vo的映射  pk_dataformatdef_h,格式定义的子表DataformatdefBVO vo list，list中的字表vo是按照code排序的。
			for(DataformatdefBVO bvo:fmb){
				String pkh=bvo.getPk_dataformatdef_h();
				ArrayList<DataformatdefBVO> l;
				if(blm.containsKey(pkh)){
					l=blm.get(pkh);
				}else{
					l=new ArrayList<DataformatdefBVO>();
				}
				l.add(bvo);
				blm.put(pkh, l);
			}
			Iterator it=jamap.keySet().iterator();
			String keys="";
			Map gsmap=new HashMap<String, EsbMapUtilVO>();
			while(it.hasNext()){
				//3、接收主键，格式key,jamap
				//1、根据接收与格式的关系，找格式gszb
				//2、根据格式的主表主键，找到流和主表主键的关系 blm 主表主键和vo的映射
				String key=(String) it.next();//数据接收的主表主键
				if(jsygs.containsKey(key)){
					List<String> l=(List<String>) jsygs.get(key);//格式主表主键与流类型的Map
					VDipDsreceiveVO vdipvo=dsmap.get(key);
					if(vdipvo==null||vdipvo.getBj()==null||vdipvo.getBj().trim().equals("")){
						break;
					}
					String bj=vdipvo.getBj();
					String bjesc=bj;
					if(vdipvo.getIsesc()!=null&&vdipvo.getIsesc().equalsIgnoreCase("y")){
						bjesc="\\"+bj;
					}
					if(l!=null&&l.size()>0){
//						Map<String,Object> map=new HashMap<String, Object>();
//						String ikey="";
						for(String s:l){//得到的是格式定义的主表主键
							if(blm.containsKey(s)){
								List<DataformatdefBVO> dal=blm.get(s);
								String data="";
								String ltype=(String) gszb.get(s);//得到是流类型；
								if(dal!=null&&dal.size()>0){
									EsbMapUtilVO vo=new EsbMapUtilVO();
									data=bj+dal.get(0).getDatakind()+jamap.get(key);//头格式
									String endflag=dal.get(dal.size()-1).getDatakind();//结束标记
									String mapbj="";
								    //liyunzhe add 2012-08-02 增加延时 start
									DataformatdefHVO hvo=(DataformatdefHVO) getBaseDAO().retrieveByPK(DataformatdefHVO.class, s);
									if(hvo!=null&&hvo.getDelayed()!=null){
										vo.setDelaytime(hvo.getDelayed());//设置延迟时间。
									}
									//liyunzhe add 2012-08-02 增加延时 end	
									if(ltype.equals("0001AA100000000142YQ")){//开始
										mapbj="b";
									}else if(ltype.equals("0001AA100000000142YS")){//数据
										mapbj="d";
									//liyunzhe add 2012-08-02 合并类型、合并分隔符、合并count  start	
										if(hvo!=null){
											if(hvo.getMergestyle()!=null&&hvo.getMergestyle().trim().length()>0){
												vo.setMergeStyle(hvo.getMergestyle());
												if(!hvo.getMergestyle().equals("0")){
													MessagelogoVO logvo=(MessagelogoVO) getBaseDAO().retrieveByPK(MessagelogoVO.class, hvo.getMergemark());
													if(logvo!=null&&logvo.getCdata()!=null){
														if(logvo.getVdef1()!=null&&logvo.getVdef1().booleanValue()){
															vo.setMergemarkesc("\\"+logvo.getCdata());
														}else{
															vo.setMergemarkesc(logvo.getCdata());	
														}
													    vo.setMergemark(logvo.getCdata());
														vo.setMergeCount(hvo.getMergecount());
													}
												}
											}
										}
									//liyunzhe add 2012-08-02 合并类型、合并分隔符、合并count  end
									}else if(ltype.equals("0001ZZ10000000018K7M")){//结束
										mapbj="e";
									}else {
										continue;
									}
									if(verfiemap!=null&&verfiemap.containsKey(key)){
										vo.setCheckname(verfiemap.get(key));//数据校验
									}
									if(ddbmap!=null&&ddbmap.containsKey(key)){
										vo.setTypeddb(ddbmap.get(key));//数据定义
									}
									vo.setTablename(dsmap.get(key).getMemorytable());//存储表名
									vo.setSysvo(dsmap.get(key));//联合vo
									vo.setBj(bj);//标记
									vo.setBjesc(bjesc);
									vo.setType(mapbj);//类型
									vo.setData(dal);//数据项，估计就是格式
									vo.setKey(jamap.get(key));//key串
									vo.setPk_datarec(key);
									vo.setEsc(esc);
									vo.setEndflag(endflag);
									gsmap.put(data.toUpperCase(), vo);
									keys=keys+"【"+data.toUpperCase()+"("+vo.getType()+")】,";
								}
							}
						}
					}
				}
				
			}

			instance=gsmap;
			Logger.debug("查询到的格式有："+(keys.length()>0?keys.substring(0,keys.length()-1):""));
			DateprocessVO vop=new DateprocessVO();
			vop.setActivetype("数据接收");
			vop.setActive("ESB消息接收");
			vop.setSdate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			vop.setContent("查询到的格式有："+(keys.length()>0?keys.substring(0,keys.length()-1):""));
			vop.setDef_str_1(IContrastUtil.DATAPROCESS_HINT);
			ILogAndTabMonitorSys ilm=(ILogAndTabMonitorSys) NCLocator.getInstance().lookup(ILogAndTabMonitorSys.class.getName());
			if(gsmap!=null&&gsmap.size()>0){
				vop.setContent("查询到的格式大小是"+gsmap.size());
				ilm.writToDataLog_RequiresNew(vop);
				Iterator itr=gsmap.keySet().iterator();
				int i=0;
				while(itr.hasNext()){
					String key=itr.next().toString();
					DateprocessVO dddvo=new DateprocessVO();
					dddvo.setSyscode(((EsbMapUtilVO)gsmap.get(key)).getSysvo().getCode());
					dddvo.setSysname(((EsbMapUtilVO)gsmap.get(key)).getSysvo().getExtname());
					dddvo.setActivetype("数据接收");
					dddvo.setActive("ESB消息接收");
					dddvo.setContent("第"+(i+1)+"个格式是： "+key);
					dddvo.setDef_str_1(IContrastUtil.DATAPROCESS_HINT);
					ilm.writToDataLog_RequiresNew(dddvo);
					i++;
				}
					
				
			}else{
				vop.setContent("查询到的格式为空");
			}
			/*if(keys.length()>0){
				keys=keys.substring(0, keys.length()-1);
				if(keys.contains(",")){
					
					
					String[] sss=keys.split(",");
					vop.setContent("查询到的格式大小是"+sss.length);
					ilm.writToDataLog_RequiresNew(vop);
					for(int i=0;i<sss.length;i++){
						DateprocessVO dddvo=new DateprocessVO();
						dddvo.setActivetype("数据接收");
						dddvo.setActive("ESB消息接收");
						dddvo.setContent("第"+(i+1)+"个格式是： "+sss[i]);
						ilm.writToDataLog_RequiresNew(dddvo);
					}
				}else{
					vop.setContent("查询到的格式有1个："+(keys.length()>0?keys.substring(0,keys.length()-1):""));
				}
				
			}else{
				vop.setContent("查询到的格式为空");
			}*/
			
			
			
			//ilm.writToDataLog_RequiresNew(vop);
		}catch(Exception e){
			e.printStackTrace();
			Logger.debug("查询格式出错："+e.getMessage());
		}
	
	}
	public static List<DipManagerserverVO> state =null;
	/**
	 * @desc 与主服务器同步消息数据
	 * */
	public static void syhDipMserState(String servername){
		/*IServerBufferManage isb=(IServerBufferManage) NCLocator.getInstance().lookup(IServerBufferManage.class.getName());
		state=isb.syhDipMserState(servername);*/
		syhOrStart(servername, null);
	}
	/**
	 * @desc 判断当前的服务是否还是启动着的
	 * */
	public static boolean isStart(DipManagerserverVO mvo){
		return syhOrStart(null, mvo);
	}
	
	public static boolean syhOrStart(String servername,DipManagerserverVO mvo){
		synchronized (lock) {
		
			if(servername!=null&&servername.length()>0){
				IServerBufferManage isb=(IServerBufferManage) NCLocator.getInstance().lookup(IServerBufferManage.class.getName());
				state=isb.syhDipMserState(servername);
				return false;
			}else{//判断改服务是否启动时候，如果服务是运行就表示true，否则返回false，也就是在DataReceiverJMSImp中不接受esb数据。
				if(mvo==null||mvo.getPrimaryKey()==null||mvo.getPrimaryKey().length()<=0){
					return false;
				}
				String pk=mvo.getPrimaryKey();
				List<DipManagerserverVO> dmvos= state;
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
		}
	}

}
