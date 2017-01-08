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
	 * @desc ���·�����������߳�id���·�������״̬
	 * @param serverThreadName �������Լ��̵߳�Ψһid
	 * @param dipMVO Ҫ���µķ������vo
	 * @return boolean �����Ƿ���·������ɹ�
	 * */

	public static byte lock[] = new byte[0]; 
	public boolean setServerBufferManage(String serverThreadName,DipManagerserverVO dipMVO){
		boolean ret=true;
		synchronized (lock){
        	Logger.debug("д�����========"+serverThreadName+"===========");
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
	        	Logger.debug("д�����========"+serverThreadName+"===========");
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
	 * @desc ���ػ����У����з������״̬
	 * @param 
	 * @return DipManagerserverVO[] ���ػ����У����з������VO
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
	 * @desc �õ�Ԥ�����͵����з������
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
	 * @desc �õ���Ϣ�������͵����з������
	 * */
	public List<DipManagerserverVO> getMsgBufVO(){
//TODO wyd ��ʱ��ε����ּӽ�������ʱ�䣬��ʱȥ��
		synchronized (lock){
			List<MessservtypeVO> mtvo=null;
			try {
				mtvo = (List<MessservtypeVO>) new BaseDAO(IContrastUtil.DESIGN_CON).retrieveByClause(MessservtypeVO.class,"nvl(dr,0)=0");
			} catch (DAOException e) {
				e.printStackTrace();
			}
			//�����ǰ�Ļ�����ķ�������ǿյ���ô�����еĶ���ѯ����
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
			}else{//�����ǰ������ķ�������ǿյģ���ô���ٲ�ѯ�������еģ�Ȼ���뵱ǰ�ıȶԣ������ǰ��û�а�����ѯ�����ģ���ô������ӽ�ȥ
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
	 * @desc �����߳����ƣ����ػ����еķ���VO:Ԥ���������͵�
	 * @param threadName ��ǰ�߳�����
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
                        //2012-05-31 modify liyunzhe ��table���Ƹĳ��߳����ƣ�������Ϊ����д���ˣ�����ͱ���û�й�ϵ��������vo�б����еġ�
						//						if((vo.getThreadname()==null||vo.getTableName().length()<=0)
						if((vo.getThreadname()==null||vo.getThreadname().length()<=0)
								&&((vo.getMstatus()!=null&&vo.getMstatus().equals("����"))
								||(vo.getMstatus()!=null&&vo.getMstatus().equals("����������"))||
								(vo.getMstatus()==null||vo.getMstatus().length()==0))){
//							2012-05-31 liyunzhe modify end	
							MessservtypeVO mesvo=vo.getMesvo();
							if(threadName.startsWith(mesvo.getVdef2())){
								if(mesvo.getType().equals("��ʱ����")){
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
						}else if(vo.getMstatus()!=null&&vo.getMstatus().equals("ֹͣ��")){
							vo.setThreadname(null);
							vo.setMstatus("ֹͣ");
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
					retvo.setMstatus("����");
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
	 * @desc ֹͣ��������Ϣ����
	 * @param String pk_server ��Ϣ��������
	 * @param boolean isStart ������true;ͣ����false;
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
					if(isStart&&(vo.getMstatus()!=null&&!vo.getMstatus().equals("����"))){
						getOrSetUpdateBufList(vo.getRunservice(),false, vo);
						vo.setMstatus("����������");
						vo.setRunservice(vo.getMesvo().getVdef2());
						vo.setStartandstorp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					}else if(!isStart&&vo.getMstatus()!=null&&!vo.getMstatus().equals("ֹͣ")){
						if(vo.getRunservice()==null||vo.getRunservice().length()==0){
							vo.setMstatus("ֹͣ");
							vo.setRunservice(vo.getMesvo().getVdef2());
						}else{
							vo.setMstatus("ֹͣ��");
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
//			Logger.debug("û���ҵ���Ӧ����Ϣ��ʽ�����Ӷ���");
//			return;
//		}
//		//�õ���Ϣ��������ע����Ϣ
//		String ippk=mtvo.getVdef1();
//		if(ippk==null||ippk.length()<=0){
//			Logger.debug("û���ҵ���Ϣ������������");
//			return;
//		}
//		try{
//			MsrVO mvo=(MsrVO) getBaseDAO().retrieveByPK(MsrVO.class, ippk);
//			if(mvo==null){
//				Logger.debug("û�в�ѯ����Ϣ����������"+ippk);
//				return;
//			}
//			//��ѯ�ָ��ǵ�����
//			String gs=mvo.getFgbj();
//			if(gs==null||gs.length()<=0){
//				Logger.debug("��Ϣ����������û�ж���ָ��ǣ�����û�б���ָ��ǵ�����");
//				return;
//			}
//			//1���鵽���ݽ��ա�ϵͳ�����ݶ��������vo
//			List<VDipDsreceiveVO> vdiprvos=(List<VDipDsreceiveVO>) getBaseDAO().retrieveByClause(VDipDsreceiveVO.class, " bjpk='"+gs+"' and nvl(dr,0)=0 and sourcetype='0001AA10000000013SVI' and bj is not null");
//			if(vdiprvos==null||vdiprvos.size()<=0){
//				Logger.debug("û���ҵ��ָ���������"+gs+"����Ӧ��ϵͳ���߽�������Ϊ��Ϣ���ߵĽ��ն���");
//				return;
//			}
//			//2�����ո�ʽ���ҵ���key
//			Map<String,String> jamap=new HashMap<String,String>();//������������ʽkey
//			String pkmemtab="";//���ݶ�����������
//			Map<String,String> dymap=new HashMap<String, String>();//key ���ݶ�������������Value ���ݽ��ն������������
//			String bj=vdiprvos.get(0).getBj();
//			String jspk="";//���ݽ��յ�����
//			Map<String ,VDipDsreceiveVO> dsmap=new HashMap<String, VDipDsreceiveVO>();//��������������vo
//			for(VDipDsreceiveVO jsvoi:vdiprvos){
//				jspk=jspk+"'"+jsvoi.getPk_datarec_h()+"',";
//				pkmemtab=pkmemtab+"'"+jsvoi.getPk_datadefinit_h()+"',";
//				dsmap.put(jsvoi.getPk_datarec_h(), jsvoi);
//				dymap.put(jsvoi.getPk_datadefinit_h(), jsvoi.getPk_datarec_h());
//				jamap.put(jsvoi.getPk_datarec_h(), bj+jsvoi.getExtcode()+bj+(jsvoi.getSitecode()==null||jsvoi.getSitecode().length()<=0?(jsvoi.getDef_str_1()==null?"000000":jsvoi.getDef_str_1()):jsvoi.getSitecode())+bj+jsvoi.getBusicode()+bj);
//			}
//			//3�����ݽ��ն�Ӧ�����ݶ���ĸ�ʽ,map��key���ݽ���������value���ʽ
//			Map<String,List<DipDatadefinitBVO>> ddbmap=new HashMap<String, List<DipDatadefinitBVO>>();//key ���ݽ��ն��������������value ���ݸ�ʽ
//			List<DipDatadefinitBVO> ddfbvos=(List<DipDatadefinitBVO>) getBaseDAO().retrieveByClause(DipDatadefinitBVO.class, "pk_datadefinit_h in ("+pkmemtab.substring(0,pkmemtab.length()-1)+") and nvl(dr,0)=0");
//			if(ddfbvos==null||ddfbvos.size()<=0){
//				Logger.debug("û���ҵ����ݶ��帽����������Ϊ��"+pkmemtab);
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
//			//����У��ķ���
//			List<DataverifyHVO> vhvos=(List<DataverifyHVO>) getBaseDAO().retrieveByClause(DataverifyHVO.class, "pk_datachange_h in ("+jspk.substring(0,jspk.length()-1)+") and nvl ( dr, 0 ) = 0");
//			String vhvostr="";
//			Map<String ,String> vmap=new HashMap<String, String>();//����У��������map,key������У�������������value�����ݽ��ն������������
//			Map<String ,List> verfiemap=new HashMap<String,List>();//����У���map��key�����ݽ��յ�����������ֵ��У���list
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
//			//���յĸ�ʽ
//			List<DataformatdefHVO> gsh=(List<DataformatdefHVO>) getBaseDAO().retrieveByClause(DataformatdefHVO.class, "pk_datarec_h in ("+jspk.substring(0,jspk.length()-1)+") and nvl(dr,0)=0");
//			if(gsh==null||gsh.size()<=0){
//				return;
//			}
//			Map gszb=new HashMap<String, String>();//��ʽ���������������͵�Map
//			Map<String, List> jsygs=new HashMap<String, List>();//�������ʽ��Map
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
//			HashMap<String, ArrayList> blm=new HashMap<String, ArrayList>();//����������vo��ӳ��
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
//				//3��������������ʽkey,jamap
//				//1�����ݽ������ʽ�Ĺ�ϵ���Ҹ�ʽgszb
//				//2�����ݸ�ʽ�������������ҵ��������������Ĺ�ϵ blm ����������vo��ӳ��
//				String key=(String) it.next();//���ݽ��յ���������
//				if(jsygs.containsKey(key)){
//					List<String> l=(List<String>) jsygs.get(key);//��ʽ���������������͵�Map
//					if(l!=null&&l.size()>0){
////						Map<String,Object> map=new HashMap<String, Object>();
////						String ikey="";
//						for(String s:l){//�õ����Ǹ�ʽ�������������
//							if(blm.containsKey(s)){
//								List<DataformatdefBVO> dal=blm.get(s);
//								String data="";
//								String ltype=(String) gszb.get(s);//�õ��������ͣ�
//								if(dal!=null&&dal.size()>0){
//									data=bj+dal.get(0).getDatakind()+jamap.get(key);
//									String mapbj="";
//									if(ltype.equals("0001AA100000000142YQ")){//��ʼ
//										mapbj="b";
//									}else if(ltype.equals("0001AA100000000142YS")){//����
//										mapbj="d";
//									}else if(ltype.equals("0001ZZ10000000018K7M")){//����
//										mapbj="e";
//									}
//									EsbMapUtilVO vo=new EsbMapUtilVO();
//									if(verfiemap!=null&&verfiemap.containsKey(key)){
//										vo.setCheckname(verfiemap.get(key));//����У��
//									}
//									if(ddbmap!=null&&ddbmap.containsKey(key)){
//										vo.setTypeddb(ddbmap.get(key));//���ݶ���
//									}
//									vo.setTablename(dsmap.get(key).getMemorytable());//�洢����
//									vo.setSysvo(dsmap.get(key));//����vo
//									vo.setBj(bj);//���
//									vo.setType(mapbj);//����
//									vo.setData(dal);//��������ƾ��Ǹ�ʽ
//									vo.setKey(jamap.get(key));//key��
//									gsmap.put(data.toUpperCase(), vo);
//									keys=keys+"��"+data.toUpperCase()+"��,";
//								}
//							}
//						}
//					}
//				}
//				
//			}
//			BJmap.put(gs,gsmap);
//			Logger.debug("��ѯ���ĸ�ʽ�У�"+(keys.length()>0?keys.substring(0,keys.length()-1):""));
//			DateprocessVO vop=new DateprocessVO();
//			vop.setActivetype("���ݽ���");
//			vop.setActive("ESB��Ϣ����");
//			vop.setSdate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//			vop.setContent("��ѯ���ĸ�ʽ�У�"+(keys.length()>0?keys.substring(0,keys.length()-1):""));
//			ILogAndTabMonitorSys ilm=(ILogAndTabMonitorSys) NCLocator.getInstance().lookup(ILogAndTabMonitorSys.class.getName());
//			ilm.writToDataLog(vop);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	
//	}
	
	/**
	 * @desc �ѷ���������Ϣ�����״̬��Ϊû���߳�����
	 * @param DipManagerserverVO mvo �������VO
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
					if(tvo.getType()!=null&&tvo.getType().equals("ʱ���")){
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
					if(vo.getMstatus()!=null&&vo.getMstatus().equals("ֹͣ��")){
						vo.setMstatus("ֹͣ");
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
	 * @desc �жϵ�ǰ�ķ����Ƿ��������ŵ�
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
					if((vo.getMstatus()==null||vo.getMstatus().length()==0)||(vo.getMstatus()!=null&&(vo.getMstatus().equals("����")||vo.getMstatus().equals("����������")))){
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
