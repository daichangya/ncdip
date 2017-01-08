package nc.impl.dip.pub;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.dip.fun.YzFormulaParse;
import nc.bs.dip.in.ValueTranslator;
import nc.bs.dip.tyzh.TyzhProcessor;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.dip.pub.ILogAndTabMonitorSys;
import nc.itf.dip.pub.IQueryField;
import nc.itf.dip.pub.checkplugin.ICheckPlugin;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.util.dip.sj.CheckMessage;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.RetMessage;
import nc.vo.dip.contdata.DipContdataVO;
import nc.vo.dip.contdatawh.ContDataMapUtilVO;
import nc.vo.dip.datacheck.DataCheckVO;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.dip.dateprocess.DateprocessVO;
import nc.vo.dip.in.RowDataVO;
import nc.vo.dip.sysregister.DipSysregisterHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;

public class AutoContData {
	String hpk;
	DipContdataVO hvo;
	public AutoContData(String hpk) {
		this.hpk=hpk;
	}
	BaseDAO bd;
	private BaseDAO getBaseDAO(){
		if(bd==null){
			bd=new BaseDAO(IContrastUtil.DESIGN_CON);
		}
		return bd;
	}
	IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	public RetMessage autoContData(List<String> errlist){
		DipDatadefinitHVO dzdhvo=null;
		DipDatadefinitHVO bdzdhvo=null;
		Map<String, DipDatadefinitBVO> map=new HashMap<String, DipDatadefinitBVO>();
		RetMessage rt=new RetMessage();
		if(hpk==null||hpk.length()<=0){
			errlist.add("对照定义的主键为空！");
		}else{
			try {
				hvo=(DipContdataVO) getBaseDAO().retrieveByPK(DipContdataVO.class, hpk);
			} catch (DAOException e) {
				e.printStackTrace();
			}
			if(hvo==null){
				errlist.add("没有找到对应的对照定义");
			}else{
				
				//liyunzhe 修改 把删除操作放到最上边，在执行自动对照前，是先删除在对照 2012-06-19 strat 
				try {
					if(hvo.getDeletetype()!=null&&hvo.getDeletetype().trim().length()>0){
						dataClear();
//						rt=new RetMessage(true,"删除数据成功");
						rt.setIssucc(true);
						rt.setMessage("删除数据成功");
					}
				} catch (Exception e1) {
					Logger.error(e1.getMessage());
					e1.printStackTrace();
					rt= new RetMessage(false,"数据清理失败");
					rt.setErrlist(errlist);
					return rt;
				}
				//liyunzhe 修改 把删除操作放到最上边，在执行自动对照前，是先删除在对照 2012-06-19 strat
				if(hvo.getDef_str_1()==null||hvo.getDef_str_1().equals("Y")){
					try {
						dzdhvo=(DipDatadefinitHVO) getBaseDAO().retrieveByPK(DipDatadefinitHVO.class, hvo.getContabcode());
						bdzdhvo=(DipDatadefinitHVO) getBaseDAO().retrieveByPK(DipDatadefinitHVO.class, hvo.getExtetabcode());
					} catch (DAOException e) {
						e.printStackTrace();
					}
					if(dzdhvo==null||bdzdhvo==null){
						errlist.add("没有找到对照的对照表定义或被对照表定义");
					}else{
						List<DipDatadefinitBVO> blist=null;
						try {
							blist=(List<DipDatadefinitBVO>) getBaseDAO().retrieveByClause(DipDatadefinitBVO.class, "pk_datadefinit_b in ('"+hvo.getContcolcode()+"','"+hvo.getDef_str_2()+"','"+hvo.getDef_str_3()+"','"+hvo.getExtecolcode()+"')");
						} catch (DAOException e) {
							e.printStackTrace();
						}
						if(blist==null||blist.size()<=0){
							errlist.add("对照双方的对照字段定义没有找到");
						}else{
							for(DipDatadefinitBVO bvoi:blist){
								if(bvoi.getPrimaryKey().equals(hvo.getContcolcode())){
									map.put("cp", (DipDatadefinitBVO) bvoi.clone());
								}
								if(bvoi.getPrimaryKey().equals(hvo.getDef_str_2())){
									map.put("cd", (DipDatadefinitBVO) bvoi.clone());
								}
								if(bvoi.getPrimaryKey().equals(hvo.getExtecolcode())){
									map.put("ep", (DipDatadefinitBVO) bvoi.clone());
								}
								if(bvoi.getPrimaryKey().equals(hvo.getDef_str_3())){
									map.put("ed", (DipDatadefinitBVO) bvoi.clone());
								}
							}
							if(map==null||map.size()<3){
								errlist.add("对照双方的字段定义没有找到");
							}else{
								String sql="select c from (select count(*) c from "+bdzdhvo.getMemorytable()+" group by "+map.get("ed").getEname()+" ) where c>1";
								String count=null;
								try {
									count=iqf.queryfield(sql);
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
								if(count!=null&&count.length()>0&&Integer.parseInt(count)>0){
									errlist.add("被对照表的选择的自动对照字段不唯一");
								}
							}
						}
					}
					
					if(errlist!=null&&errlist.size()>0){
//						rt= new RetMessage(false,"检查失败");
						rt.setIssucc(false);
						rt.setMessage(rt.getMessage()==null?"":(rt.getMessage()+";")+"自动对照检查失败");
						rt.setErrlist(errlist);
						return rt;
					}
					
//					modify by zhw 2012-05-29 自动对照逻辑修改，根据自动对照字段设置的函数取出值后再到被自动对照字段中查看是否有相同数据
					int scount=0;
					try {
						List<DipDatadefinitBVO> c = (List<DipDatadefinitBVO>) getBaseDAO().retrieveByClause(
								DipDatadefinitBVO.class,
								"pk_datadefinit_h = '" + dzdhvo.getPk_datadefinit_h() + "'");
						if (c == null || c.size() == 0) {
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
						List list = (List)getBaseDAO().executeQuery("select * from "+dzdhvo.getMemorytable()+" where nvl(dr,0)=0", new MapListProcessor());
						if(list == null || list.size() == 0){
							throw new DAOException(rt.getMessage()==null?"":(rt.getMessage()+";")+"自动对照维护未找到符合条件的数据");
						}
						RowDataVO[] data;
						data = new RowDataVO[list.size()];
						List<RowDataVO> listvo=new ArrayList<RowDataVO>();
						for(int i=0;i<list.size();i++){
							Map map1 = (Map)list.get(i);
							data[i] = new RowDataVO();
							String[] fields = (String[])map1.keySet().toArray(new String[0]);
							for(int j=0;j<fields.length;j++){
								if (!fields[j].equals("ts") && !fields[j].equals("dr")) {
									
									String field = fields[j];
									Object value="";
									if(fields[j]!=null&&map1.get(fields[j])!=null&&tab.get(fields[j].toUpperCase())!=null){
										value = ValueTranslator.translate(
												map1.get(fields[j])==null?null:map1.get(fields[j]).toString(), 
														tab.get(fields[j].toUpperCase()).getType(),
														tab.get(fields[j].toUpperCase()).getLength() == null?0: tab.get(fields[j].toUpperCase()).getLength());
										if(value instanceof String){
											value=value.toString().trim();
										}
									}
									data[i].setAttributeValue(field, value);
								}	
							}
							data[i].setTableName(dzdhvo.getMemorytable());
							data[i].setPKField(tab.get("#DEFAULT#")==null?"":tab.get("#DEFAULT#").getEname());
							data[i].setPrimaryKey((String)data[i].getAttributeValue(data[i].getPKFieldName()));
							Object formulaCal = YzFormulaParse.getFormulaCal(data[i], hvo.getDef_str_2());
							List<Map> cplist = iqf.queryListMapSingleSql("select "+map.get("ep").getEname()+" ep from "+bdzdhvo.getMemorytable()+" where "+map.get("ed").getEname()+"='"+formulaCal+"'");
							if(null != cplist && cplist.size() > 0){
								//liyunzhe 注释 删操作是dataClear()方法中做的。 start 2012-06-19
//								if(scount ==0){
//									iqf.exesql("delete from "+hvo.getMiddletab());
//								}
//								liyunzhe 注释 删操作是dataClear()方法中做的。 end 2012-06-19
								RowDataVO voi=new RowDataVO();
								voi.setPKField("pk");
								voi.setTableName(hvo.getMiddletab());
								Map map2 = cplist.get(0);
								voi.setAttributeValue("extepk", map2.containsKey("EP")?map2.get("EP"):map2.get("ep"));
								Object attributeValue = data[i].getAttributeValue(map.get("cp").getEname().toLowerCase());
								if(null == attributeValue)
									attributeValue = data[i].getAttributeValue(map.get("cp").getEname().toUpperCase());
								voi.setAttributeValue("contpk", attributeValue);
								voi.setAttributeValue("pk", iqf.getOID());
								listvo.add(voi);
								scount ++;
							}
						}
						if(listvo!=null && listvo.size()>0){
							getBaseDAO().insertVOList(listvo);
							scount=listvo.size();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Logger.error(e.getMessage());
						rt.setIssucc(false);
						rt.setMessage(rt.getMessage()==null?"":(rt.getMessage()+";")+"自动维护检查失败");
						rt.setErrlist(errlist);
						return rt;
					}
					rt.setSu(scount);
				}
			}
			
	
           }
				
//		String sql="select b."+map.get("cp").getEname()+" cp,e."+map.get("ep").getEname()+" ep  from "+bdzdhvo.getMemorytable()+" e left join "+dzdhvo.getMemorytable()+" b on e."+map.get("ed").getEname()+"=b."+map.get("cd").getEname()+"  where b."+map.get("cp").getEname()+" is not null";
//		List<Map> list=null;
//		try {
//			list=iqf.queryListMapSingleSql(sql);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return new RetMessage(false,"在查询对照结果的时候失败："+e.getMessage()+"【"+sql+"】");
//		}
		
//		int scount=0;
//		if(list!=null&&list.size()>0){
//			try {
//				iqf.exesql("delete from "+hvo.getMiddletab());
//			} catch (BusinessException e) {
//				e.printStackTrace();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			} catch (DbException e) {
//				e.printStackTrace();
//			}
//			List<RowDataVO> listvo=new ArrayList<RowDataVO>();
//			for(Map mapi:list){
//				RowDataVO voi=new RowDataVO();
//				voi.setPKField("pk");
//				voi.setTableName(hvo.getMiddletab());
//				voi.setAttributeValue("extepk", mapi.containsKey("EP")?mapi.get("EP"):mapi.get("ep"));
//				voi.setAttributeValue("contpk", mapi.containsKey("CP")?mapi.get("CP"):mapi.get("cp"));
//				voi.setAttributeValue("pk", iqf.getOID());
//				listvo.add(voi);
//			}
//			if(listvo!=null&&listvo.size()>0){
//				try {
//					getBaseDAO().insertVOList(listvo);
//					scount=listvo.size();
//				} catch (DAOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
		//modify by zhw 2012-05-29 -----------------------------------------------------------------------------end
		
		//liyunzhe 注释 2012-06-19
//		rt=new RetMessage(true,"成功");
//		rt.setSu(scount);
//		liyunzhe 注释 2012-06-19
		return rt;
		
	}
	
	/**
	 * 数据清理
	 * @author zhw 2012-06-18
	 */
	private void dataClear() throws Exception {
		if(null != hvo && null != hvo.getMiddletab() && !"".equals(hvo.getMiddletab())){
			String deletetype = hvo.getDeletetype();
			if(null != deletetype && !"".equals(deletetype)){
				//删除表中的所有信息
				if("1".equals(deletetype)){
					String sql="delete from "+hvo.getMiddletab();
					getBaseDAO().executeUpdate(sql);
				}else if("2".equals(deletetype)){
				//liyunzhe add 2012-06-19 删除错误数据 start
				//删除表中的错误数据，目前错误数据包括三种，。
				//0007	数据对照维护表中为空校验	数据对照维护	nc.bs.dip.contdatacheck.DateIsNullVerify	数据对照维护表里的被对照字段或对照字段为空校验	true
				//数据对照维护表中存在，对照字段为空或者被对照字段为空校验。
			    //0008	数据对照维护表数据存在校验	数据对照维护	nc.bs.dip.contdatacheck.RecordIsExistsVerify	数据对照维护表里的数据在对照系统和被对照系统中都存在校验	true
//					0009	被对照系统和对照系统一对多校验	数据对照维护	nc.bs.dip.contdatacheck.OneToManyVerify	被对照系统和对照系统一对多校验	true
						String  contabcodesql=" select hh.memorytable from Dip_Datadefinit_h hh where hh.pk_datadefinit_h='"+hvo.getContabcode()+"' ";
						String conMemorytable=iqf.queryfield(contabcodesql);
						String  contcolcodesql="select bb.ename from dip_datadefinit_b bb where bb.pk_datadefinit_b='"+hvo.getContcolcode()+"'";
						String contename=iqf.queryfield(contcolcodesql);
						String  extetabcodesql=" select hh.memorytable from Dip_Datadefinit_h hh where hh.pk_datadefinit_h='"+hvo.getExtetabcode()+"' ";
						String exteMemorytable=iqf.queryfield(extetabcodesql);
						String  extecolcodesql="select bb.ename from dip_datadefinit_b bb where bb.pk_datadefinit_b='"+hvo.getContcolcode()+"'";
						String exteename=iqf.queryfield(extecolcodesql);
						if(conMemorytable!=null&&!conMemorytable.trim().equals("")&&
						   contename!=null&&!contename.trim().equals("")&&
						   exteMemorytable!=null&&!exteMemorytable.trim().equals("")&&
						   exteename!=null&&!exteename.trim().equals("")){
							ContDataMapUtilVO contvo=new ContDataMapUtilVO();
	       					contvo.setMiddletablename(hvo.getMiddletab());
	       					contvo.setExtecolname(exteename);
	       					contvo.setExtetabname(exteMemorytable);
	       					contvo.setContabname(conMemorytable);
	       					contvo.setContcolname(contename);
						  List list= iqf.queryVOBySql("select * from dip_datacheckregist cc where (cc.type='数据对照维护' or cc.pk_dip_datacheckregist in('0001AA1000000002V5QL','0001AA1000000002QW79','0001AA1000000002QW78'))and nvl(dr,0)=0  order by code", new DataCheckVO());
						  if(list!=null&&list.size()>0){
							  DateprocessVO dataprocessvo=new DateprocessVO();
								DipSysregisterHVO sysvo=(DipSysregisterHVO) iqf.querySupervoByPk(DipSysregisterHVO.class, hvo.getPk_xt());
								if(sysvo!=null){
									dataprocessvo.setSyscode(sysvo.getCode());
									dataprocessvo.setSysname(sysvo.getExtname());
								}
								for(int i=0;i<list.size();i++){
									DataCheckVO checkvo=(DataCheckVO) list.get(i);
									check(contvo, checkvo, dataprocessvo);
								}
						  } 
						}
				//liyunzhe add 2012-06-19 删除错误数据 end	
					
				}
			}
		}
	}
	/**
	 * liyunzhe add 增加校验删除错误数据方法 2012-06-19
	 * @param contvo
	 * @param checkvo
	 * @param dataprocessvo
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws DbException 
	 * @throws SQLException 
	 * @throws  
	 */
	public void check(ContDataMapUtilVO contvo,DataCheckVO checkvo,DateprocessVO dataprocessvo) throws Exception{
		DateprocessVO vo=(DateprocessVO) dataprocessvo.clone();
		List<String> errlist = new ArrayList<String>();//存错误数据主键
				contvo.setCheckclassname(checkvo.getImplementss());
				contvo.setCheckname(checkvo.getName());
				Object obj=Class.forName(checkvo.getImplementss()).newInstance();
				if(obj instanceof ICheckPlugin){
					ICheckPlugin check = (ICheckPlugin) obj;
					CheckMessage cmsg=check.doCheck(contvo);
					if(cmsg.getMap()!=null){
						List list1=(List) cmsg.getMap().get(checkvo.getImplementss());
						if(list1!=null&&list1.size()>0){
							for(int j=0;j<list1.size();j++){
								errlist.add(list1.get(j)==null?"":list1.get(j).toString());
							}
						}	
					}
					String active=hvo.getName();
					String code=hvo.getCode();
					ILogAndTabMonitorSys ilt=(ILogAndTabMonitorSys) NCLocator.getInstance().lookup(ILogAndTabMonitorSys.class.getName());
					vo.setActivecode(code);
					vo.setSdate(new UFDateTime(new Date()).toString());
					vo.setActivetype("自动对照");
					vo.setActive(active);
					if(cmsg.getMessage()!=null&&cmsg.getMessage().length()>0){
						//把所有的错误信息写到数据错里日志中。这个是明细日志。
						vo.setContent(cmsg.getMessage());
						vo.setDef_str_1(IContrastUtil.DATAPROCESS_ERR);
						ilt.writToDataLog_RequiresNew(vo);
					}else{
						vo.setContent("校验["+contvo.getCheckname()+"],没有错误记录");
						vo.setDef_str_1(IContrastUtil.DATAPROCESS_HINT);
					}
					ilt.writToDataLog_RequiresNew(vo);
				}
		if(errlist!=null&&errlist.size()>0){
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<errlist.size();i++){
				sb.append("'"+errlist.get(i)+"',");
				if(i%900==0||i==errlist.size()-1){
					String deletesql="delete from "+hvo.getMiddletab()+" where pk in ("+sb.toString().substring(0, sb.length()-1)+")";
					sb=new StringBuffer();
						iqf.exesql(deletesql);
				}
			}
		}
	}
		
}




