package nc.util.dip.sj;

import java.util.HashMap;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.dip.consttab.DipConsttabVO;
import nc.vo.dip.datadefcheck.DipDatadefinitBVO;
import nc.vo.dip.datadefcheck.DipDatadefinitCVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class DataCheckUtil {

	public static HashMap<String, DipDatadefinitCVO[]> map = new HashMap<String, DipDatadefinitCVO[]>();
	
	public static HashMap<String, DipDatadefinitCVO[]> getDataCheckMap(String pkdetadefinith) throws UifException{
		map = new HashMap<String, DipDatadefinitCVO[]>();
		DipDatadefinitBVO[] vos = (DipDatadefinitBVO[])HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class,
				"pk_datadefinit_h='"
				+pkdetadefinith
				+"' and pk_datadefinit_b in(SELECT pk_datadefinit_b FROM dip_datadefinit_c WHERE coalesce(dr,0)=0) "
				+" and is_check='Y' and coalesce(dr,0)=0 ");
		for (DipDatadefinitBVO vo : vos) {
			String ename = vo.getEname();
			String pk_datadefinit_b = vo.getPk_datadefinit_b();
			DipDatadefinitCVO[] cvos = (DipDatadefinitCVO[])HYPubBO_Client.queryByCondition(DipDatadefinitCVO.class, 
					" pk_datadefinit_b='"+pk_datadefinit_b+"' and coalesce(dr,0)=0");
			if(null != cvos && cvos.length>0){
				map.put(ename, cvos);
			}
		}
		return map;
	}
	
	public static String checkData(String ename,String value,String middletab) throws Exception{
		DipDatadefinitCVO[] datadefinitCVOs = map.get(ename);
		String allmsg = "";
		for (DipDatadefinitCVO dipDatadefinitCVO : datadefinitCVOs) {
			String msg = checkDataByStep(dipDatadefinitCVO, value,middletab);
			if(null != msg){
				allmsg+=msg;
			}
		}
		if(!"".equals(allmsg)){
			return allmsg;
		}
		return null;
	}
	
	public static String checkDataByStep(DipDatadefinitCVO vo,String value,String middletab) throws Exception{
		Integer dtype = vo.getDtype();
		Boolean is_content = vo.getIs_content()==null?false:vo.getIs_content().booleanValue();
		Boolean check = check(vo, value, dtype,middletab);
		if(is_content){
			if(check){
				return vo.getDname()+"校验失败,";
			}
		}else{
			if(!check){
				return vo.getDname()+"校验失败,";
			}
		}
		return null;
	}

	private static Boolean check(DipDatadefinitCVO vo, String value, Integer dtype,String middletab) throws Exception {
		if("".equals(value)){
//			if(dtype==8){
//				return false;
//			}else{
				return false;
//			}
		}else{
			value=value.trim();
			if(dtype==1){//长度校验
				String def1 = vo.getDef1();
				Integer length = Integer.valueOf(vo.getInput_value());
				if(def1.equals("<")){
					if(value.length()<length){
						return true;
					}
				}else if(def1.equals("=")){
					if(value.length()==length){
						return true;
					}
				}else if(def1.equals("<=")){
					if(value.length()<=length){
						return true;
					}
				}
			}else if(dtype==2){//引用校验
				String pk_datadefinit_b = vo.getPk_datadefinit_b();
				DipDatadefinitBVO bvo = (DipDatadefinitBVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitBVO.class,
						pk_datadefinit_b);
				IQueryField queryfield = (IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName());
				String table = queryfield.queryfield("SELECT memorytable FROM v_dip_jgyyzdtree WHERE  pk_datadefinit_b ='"+bvo.getQuotetable()+"'");
				List list = queryfield.queryfieldList("select 1 from "+table+" where "+bvo.getQuotecolu()+"='"+value+"'");
				if(null != list && list.size()>0){
					return true;
				}
			}else if(dtype==10){//数据权限校验
				if(null != middletab){
					String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
					if(!"0001".equals(pk_corp)){
						IQueryField queryfield = (IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName());
						DipDatadefinitBVO bvo = (DipDatadefinitBVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitBVO.class,
								vo.getPk_datadefinit_b());
//						String sql = "select 1 from "+middletab+" where contpk='"
//								+value
//								+"' and extepk in "
//								+"  (select pk_role_corp_alloc "
//								+"   from v_dip_corproleauth "
//								+" where pk_role in "
//								+"   (select pk_role "
//								+"    from sm_user_role cc "
//								+"   where cc.cuserid = '"
//								+ClientEnvironment.getInstance().getUser().getPrimaryKey()+"')) ";
//						List list = queryfield.queryfieldList(sql);
//						if(null != list && list.size()>0){
//							return true;
//						}else{
							middletab = queryfield.queryfield("SELECT middletab FROM dip_adcontdata WHERE contcolcode='"
									+vo.getPk_datadefinit_b()
									+"' and contabcode='"
									+bvo.getPk_datadefinit_h()
									+"'");
							String sql = "select 1 from "+middletab+" where contpk='"
								+value
								+"' and extepk in "
								+"  (select pk_role_corp_alloc "
								+"   from v_dip_corproleauth "
								+" where pk_role in "
								+"   (select pk_role "
								+"    from sm_user_role cc "
								+"   where cc.cuserid = '"
								+ClientEnvironment.getInstance().getUser().getPrimaryKey()+"')) ";
							List list = queryfield.queryfieldList(sql);
							if(null != list && list.size()>0){
								return true;
							}
//						}
					}else{
						return true;
					}
				}else{
					return true;
				}
			}else if(dtype==3){//手工正则校验
				String pattern = vo.getInput_value();
		    	if(value.matches(pattern)){
		    		return true;
		    	}
			}else if(dtype==4){//引用正则校验
				String ref_pk = vo.getRef_pk();
				DipConsttabVO refVo = (DipConsttabVO)HYPubBO_Client.queryByPrimaryKey(DipConsttabVO.class, ref_pk);
				String descs = refVo.getDescs();
		    	if(value.matches(descs)){
		    		return true;
		    	}
			}else if(dtype==5){//手工关键字
				String def1 = vo.getDef1();
				String input_value = vo.getInput_value();
				String[] split = input_value.split(",");
				for (int i = 0; i < split.length; i++) {
					if(def1.equals("区分大小写")){
						if(value.indexOf(split[i])!=-1){
							return true;
						}
					}else{
						if(value.toLowerCase().indexOf(split[i].toLowerCase())!=-1){
							return true;
						}
					}
				}
			}else if(dtype==6){//引用关键字
				String ref_pk = vo.getRef_pk();
				DipConsttabVO refVo = (DipConsttabVO)HYPubBO_Client.queryByPrimaryKey(DipConsttabVO.class, ref_pk);
				String descs = refVo.getDescs();
				String[] split = descs.split(",");
				String def1 = vo.getDef1();
				for (int i = 0; i < split.length; i++){
					if(def1.equals("区分大小写")){
						if(value.indexOf(split[i])!=-1){
							return true;
						}
					}else{
						if(value.toLowerCase().indexOf(split[i].toLowerCase())!=-1){
							return true;
						}
					}
				}
			}else if(dtype==7){//转换校验
				String def1 = vo.getDef1();
				if("tonumber".equals(def1)){
					String pattern="[0-9]*";
					if(value.indexOf(".")>=1){
			    		String[] num=value.split(".");
			    		if(num.length==2){
			    			if(num[0].matches(pattern)&&num[1].matches(pattern)){
			                   return true;
			    			}
			    		}
			    	}else{
			    		if(value.matches(pattern)){
		                   return true;
		    			}
			    	}
				}else if("today".equals(def1)){
					try{
						UFDate ufDate = new UFDate(value);
						return true;
					}catch(IllegalArgumentException e){
						return false;
					}
				}
			}else if(dtype==8){
				return true;
			}else if(dtype==9){
				String def1 = vo.getDef1();
				UFDouble input_value = new UFDouble(vo.getInput_value());
				if(def1.equals("<")){
					if(new UFDouble(value).compareTo(input_value)<0){
						return true;
					}
				}else if(def1.equals("=")){
					if(new UFDouble(value).compareTo(input_value)==0){
						return true;
					}
				}else if(def1.equals("<=")){
					if(new UFDouble(value).compareTo(input_value)<=0){
						return true;
					}
				}else if(def1.equals(">")){
					if(new UFDouble(value).compareTo(input_value)>0){
						return true;
					}
				}else if(def1.equals(">=")){
					if(new UFDouble(value).compareTo(input_value)>=0){
						return true;
					}
				}
			}
		}
		return false;
	}
}
