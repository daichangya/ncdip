package nc.ui.dip.util;

import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.ui.fi.uforeport.NCFuncForUFO;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.FunctionUtil;
import nc.vo.dip.datalook.DipAuthDesignVO;
import nc.vo.dip.datalook.DipDesignVO;
import nc.vo.dip.util.ClientEvnUtilVO;
import nc.vo.dip.util.QueryUtilVO;
import nc.vo.pub.lang.UFBoolean;

public class ClientEnvDef {
	public static Map<String,ClientEvnUtilVO> queryMap;
	public static Map<String,ClientEvnUtilVO> queryAuthMap;
	public static ClientEvnUtilVO getQueryMap(String tablename) throws UifException{
		if(queryMap==null){
			queryMap=new HashMap<String, ClientEvnUtilVO>();
		}
//		if(queryMap.containsKey(tablename)){
//			return queryMap.get(tablename);
//		}else{
			DipDesignVO[] listVO=(DipDesignVO[]) HYPubBO_Client.queryByCondition(DipDesignVO.class, "pk_datadefinit_h='"+tablename+"' and designtype=1");
			if(listVO!=null){
				String[] field=new String[listVO.length+1];
				QueryUtilVO[] vos=new QueryUtilVO[listVO.length];
				Map<String,String> map=new HashMap<String, String>();
				for(int i=0;i<listVO.length;i++){
					DipDesignVO bvo = listVO[i];
					vos[i]=new QueryUtilVO();
					vos[i].setCname(bvo.getCname());
					vos[i].setEname(bvo.getEname());
					String consvalue = bvo.getConsvalue();
					if(null != consvalue && !"".equals(consvalue)){
						String value = FunctionUtil.getFuctionValue(consvalue);
						vos[i].setGetvalue(value);
						vos[i].setCztype("等于");
					}
					vos[i].setIslock(bvo.getIslock());
					field[i+1]=bvo.getCname();
					map.put(bvo.getCname(), bvo.getEname());
				}
				ClientEvnUtilVO evo=new ClientEvnUtilVO();
				evo.setCnames(field);
				evo.setVos(vos);
				evo.setMap(map);
				queryMap.put(tablename, evo);
				return evo;
			}else {
				return null;
			}
		}
//	}
	
	public static ClientEvnUtilVO getQueryAuthMap(String tablename) throws UifException{
		if(queryAuthMap==null){
			queryAuthMap=new HashMap<String, ClientEvnUtilVO>();
		}
		DipAuthDesignVO[] listVO=(DipAuthDesignVO[]) HYPubBO_Client.queryByCondition(DipAuthDesignVO.class, "pk_datadefinit_h='"+tablename+"' and designtype=1");
		if(listVO!=null){
			String[] field=new String[listVO.length+1];
			QueryUtilVO[] vos=new QueryUtilVO[listVO.length];
			Map<String,String> map=new HashMap<String, String>();
			for(int i=0;i<listVO.length;i++){
				DipAuthDesignVO bvo = listVO[i];
				vos[i]=new QueryUtilVO();
				vos[i].setCname(bvo.getCname());
				vos[i].setEname(bvo.getEname());
				String consvalue = bvo.getConsvalue();
				if(null != consvalue && !"".equals(consvalue)){
					if(FunctionUtil.SYSREFPK.equals(consvalue)){
						vos[i].setIsref(new UFBoolean("Y"));
					}else{
						String value = FunctionUtil.getFuctionValue(consvalue);
						vos[i].setGetvalue(value);
						vos[i].setCztype("等于");
					}
				}
				vos[i].setIslock(bvo.getIslock());
				field[i+1]=bvo.getCname();
				map.put(bvo.getCname(), bvo.getEname());
			}
			ClientEvnUtilVO evo=new ClientEvnUtilVO();
			evo.setCnames(field);
			evo.setVos(vos);
			evo.setMap(map);
			queryAuthMap.put(tablename, evo);
			return evo;
		}else {
			return null;
		}
	}
//	}
	
	public static void putQueryVO(String tablename,QueryUtilVO[] vo){
		ClientEvnUtilVO qvo=queryMap.get(tablename);
		qvo.setVos(vo);
		queryMap.put(tablename, qvo);
	}
	
	public static void putQueryAuthVO(String tablename,QueryUtilVO[] vo){
		ClientEvnUtilVO qvo=queryAuthMap.get(tablename);
		qvo.setVos(vo);
		queryAuthMap.put(tablename, qvo);
	}
	
	private static NCFuncForUFO fu=null;
	public static NCFuncForUFO getNCFuncForUFO(){
		return fu;
	}
	public static void setNCFuncForUFO(NCFuncForUFO ss){
		fu=ss;
	}
	static Map<String,String> accountmap=null;
	public static Map getAccountMap(){
		if(accountmap==null){
			IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
			try {
				accountmap=iqf.getAccountMap();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return accountmap;
	}
	private static String[] calenv=null;
	public static String[] getCalenv(){
		return calenv;
	}
	public static void setCalenv(String[] s){
		calenv=s;
	}
}
