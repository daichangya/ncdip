package nc.bs.dip.fun;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.naming.NamingException;

import nc.bd.accperiod.AccountCalendar;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.DataManageObject;
import nc.itf.dip.pub.ILogAndTabMonitorSys;
import nc.itf.dip.pub.IQueryField;
import nc.jdbc.framework.exception.DbException;
import nc.ui.fi.uforeport.NCFuncForUFO;
import nc.util.dip.sj.IContrastUtil;
import nc.vo.dip.contdata.DipContdataVO;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.dateprocess.DateprocessVO;
import nc.vo.framework.rsa.Encode;
//import nc.vo.hi.raceandtrain.RaceAndTrainInterface;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;

public class DefFormmulaFunc extends DataManageObject{

	  public DefFormmulaFunc() throws NamingException {
		super();
		// TODO Auto-generated constructor stub
	}

	IQueryField iq=(IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName());
	ILogAndTabMonitorSys ilo=(ILogAndTabMonitorSys) NCLocator.getInstance().lookup(ILogAndTabMonitorSys.class.getName());
	/**
	 * DefFormmulaFunc 构造子注解。
	 */
//	public DefFormmulaFunc() {
//		super();
//	}
	public String defGetColValue(String tablename,String fieldname,String pkfield,String pkvalue) throws Exception{
		if(tablename==null||"".equals(tablename)||fieldname==null||"".equals(fieldname)||pkfield==null||"".equals(pkfield)||pkvalue==null||"".equals(pkvalue)){
			return "";
		}
		StringBuffer sb=new StringBuffer();
		sb.append("defgetcolvalue(\"");
		sb.append(tablename);
		sb.append("\",\"");
		sb.append(fieldname);
		sb.append("\",\"");
		sb.append(pkfield);
		sb.append("\",\"");
		sb.append(pkvalue);
		sb.append("\")");
		String key=sb.toString();
		if(iq.getFormmulaCacheValue(key)!=null){
			String res=(String) iq.getFormmulaCacheValue(key);
			return res;
		}else{
			String sql="select "+ fieldname + "  from  "+tablename +" where " +pkfield+" = '"+ pkvalue+"'";
			String value=iq.queryfield(sql);
			if(value==null||"".equals(value)){
				value="";
			}
			iq.putFormmulaCache(key, value);
			return value;	
		}
		
		
	}
	
	
	
	public String random(Object num) throws Exception{
		String ret="";
		int ws=0;
		if(num!=null){
			if(num instanceof Integer){
				ws=(Integer) num;
				if(ws>0){
					for(int i=0;i<ws;i++){
						ret=ret+=new DecimalFormat("0").format(new Random().nextInt(10));
					}
				}
			}
		}
		return ret;
	}
	/**
	 * 提供平台公式解析自定义函数，得到计算后结果
	 * 根据 年，月， 得到 该月的最后一天的日期
	 * @author 北京商佳科技 袁廷勤
	 * @param yyyy 年份
	 * @param mmmm 月份
	 * @throws Exception
	 * @since nc5.2 2011-05-13
	 */

	public String getCurAccountYear(String yyyy) throws Exception{
		AccountCalendar ac=null;
		if(yyyy==null||yyyy.length()<=0){
			ac=AccountCalendar.getInstance();
		}else{
			ac=AccountCalendar.getInstanceByPeriodScheme(yyyy);
		}
		ac.setDate(new Date());
		if(ac==null){
			throw new Exception("没有得到当前的会计年");
		}else{
			return ac.getYearVO().getPeriodyear();
		}
	}
	public String getCurAccountMonth(String yyyy) throws Exception{
		AccountCalendar ac=null;
		if(yyyy==null||yyyy.length()<=0){
			ac=AccountCalendar.getInstance();
		}else{
			ac=AccountCalendar.getInstanceByPeriodScheme(yyyy);
		}
		ac.setDate(new Date());
		if(ac==null){
			throw new Exception("没有得到当前的会计年");
		}else{
			return ac.getMonthVO().getMonth();
		}
	}
	public String getValueByConst(String pk){
		if(pk==null||pk.length()<=0 ){
			return "" ;
		}else{
			try {
				return iq.queryfield("select descs from dip_consttab where pk_consttab='"+pk+"' and nvl(dr,0)=0");
			} catch (BusinessException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (DbException e) {
				e.printStackTrace();
			}
			return "";
		}
	}
	public String getYMLastDay(String yyyy, String mmmm) throws Exception{
		StringBuffer sb=new StringBuffer();
		sb.append("getymlastday(\"");
		sb.append(yyyy);
		sb.append("\",\"");
		sb.append(mmmm);
		sb.append("\")");
		String key=sb.toString();
		if(iq.getFormmulaCacheValue(key)!=null){
			return iq.getFormmulaCacheValue(key);
		}else{
			UFDate da  = new UFDate(); 
			int days = da.getDaysMonth(Integer.valueOf(yyyy), Integer.valueOf(mmmm));
//			System.out.println(da.getDaysMonth(Integer.valueOf(yyyy), Integer.valueOf(mmmm)));
		//	String js = yyyy+"-"+mmmm+"-"+ String.valueOf(days);
//			System.out.println(js);
			String value=yyyy+"-"+mmmm+"-"+ days;
			iq.putFormmulaCache(key, value);
		    return value;
		}
		
	}
	public String getGenPK(){
		return getOID();
	}
	public boolean isNull(String ss){
		if(ss==null||ss.length()<=0){
			return true;
		}else{
			return false;
		}
	}
	public String cmonth(){

		String date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		return date.substring(5,7);
	}
	public String cyear(){

		String date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		return date.substring(0,4);
	}
	public String initEnv(String pkServlet,String accountcode,String unitcode,String usercode,String func) throws Exception{
		if(isNull(accountcode)||isNull(unitcode)||isNull(usercode)||isNull(func)){
			throw new Exception("参数不能为空！");
		}
		if(pkServlet==null||pkServlet.length()<=0){
			NCFuncForUFO fu=new NCFuncForUFO();
			Map<String,String> map=iq.getAccountMap();
			if(map==null||map.size()<=0){
				throw new Exception("没有找到对应的帐套");
			}
			String sign=null;
			Iterator<String> it=map.keySet().iterator();
			while(it.hasNext()){
				String key=it.next();
				String value=map.get(key).split("\\|")[0];
				if(value.equals(accountcode)){
					sign=map.get(key).split("\\|")[1];
				}
			}
			String date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			Encode code = new Encode();
			String password=iq.queryfield("select user_password from sm_user where user_code='"+usercode+"'",sign);
			 String[] calEnv = {sign,
	                //数据源名称
					  accountcode, //账套
					  unitcode, //单位
					  date, //日期
					  usercode, //用户
					  code.decode(password), //密码
					  date,
				"simpchn"//语言类型
				    };
				fu=new NCFuncForUFO();
				fu.setCalEnv(calEnv);
				func.replace(" ", "");
				int index=func.indexOf("|");
				Object objjj=fu.calcFuncValues(new String[]{func.substring(0,index)} , new String[]{func .substring(index+2).replace("'", "").replace("||", "\r\n")});
//				calcFuncValues(new String[]{formula.substring(0,index)}, new String[]{formula .substring(index+2).replace("'", "").replace("||", "\r\n")});
				String obj=null;
				if(objjj==null){
					return obj;
				}
				if(objjj instanceof Object []){
					
					Object ob[]=(Object[]) objjj;
					if(ob!=null&&ob.length>0){
						if(ob[0] instanceof String[]){
							String []ss=(String[]) ob[0];
							obj=ss[0];
						}
					}else{
						obj="";
					}
				}else{
					obj = objjj.toString();
				}
			return obj;
		}else{
			String ip=iq.queryfield("select descriptions from dip_dataurl where code='"+pkServlet+"' and nvl(dr,0)=0");
			String urlStr="";
			if(isNull(ip)){
				throw new Exception("没有找到Servlet注册");
			}
			if(ip.startsWith("http://")){
				ip=ip.substring(7);
			}
			ip=ip.split("/")[0];
			urlStr = "http://" + ip + "/execuFBySDServlet?fou="+func+"&par="+accountcode+"|"+unitcode+"|"+ usercode;

			String obj = null;
			HttpURLConnection conn;
			ObjectOutputStream output = null;
			ObjectInputStream input=null;
			try {
				conn = getConnect(urlStr);
				output = new ObjectOutputStream(conn.getOutputStream());
				input = new ObjectInputStream(conn.getInputStream());
				Object objjj=input.readObject();
				if(objjj==null){
					return obj;
				}
				if(objjj instanceof Object []){
					
					Object ob[]=(Object[]) objjj;
					if(ob!=null&&ob.length>0){
						if(ob[0] instanceof String[]){
							String []ss=(String[]) ob[0];
							obj=ss[0];
						}
					}else{
						obj="";
					}
				}else{
					obj = objjj.toString();
				}
					
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new Exception("从servlet传回的数据解析失败");
			} catch (MalformedURLException e) {
				e.printStackTrace();
				throw new Exception("连接servlet失败");
			} catch (IOException e) {
				e.printStackTrace();
				throw new Exception("解析servlet返回流失败");
			}finally{
				try {
					output.flush();
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(obj.contains("没有权限访问")){
				DateprocessVO vo=new DateprocessVO();
				vo.setDef_str_1(IContrastUtil.DATAPROCESS_ERR);
				vo.setContent(obj+ip);
//				vo.setActivetype(IContrastUtil.)
				ilo.writToDataLog_RequiresNew(vo);
				obj=null;
			}
			return obj;
		}
	}
	public String initEnvHrFunction(String pkServlet,String datasource,String usercode,String func) throws Exception{
		if(isNull(func)||isNull(datasource)||isNull(usercode)){
			throw new Exception("数据源、用户编码、业务函数不能为空！");
		}
		if(pkServlet==null||pkServlet.length()<=0){
//			NCFuncForUFO fu=new NCFuncForUFO();
//			Map<String,String> map=iq.getAccountMap();
//			if(map==null||map.size()<=0){
//				throw new Exception("没有找到对应的帐套");
//			}
			String sign=null;
//			Iterator<String> it=map.keySet().iterator();
//			while(it.hasNext()){
//				String key=it.next();
//				String value=map.get(key).split("\\|")[0];
//				if(value.equals(accountcode)){
//					sign=map.get(key).split("\\|")[1];
//				}
//			}
//			String date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//			Encode code = new Encode();
//			String password=iq.queryfield("select user_password from sm_user where user_code='"+usercode+"'",sign);
//			 String[] calEnv = {sign,
//	                //数据源名称
//					  accountcode, //账套
//					  unitcode, //单位
//					  date, //日期
//					  usercode, //用户
//					  code.decode(password), //密码
//					  date,
//				"simpchn"//语言类型
//				    };
//				fu=new NCFuncForUFO();
//				fu.setCalEnv(calEnv);
//			nc.vo.hi.raceandtrain.RaceAndTrainInterface raceinterface=new RaceAndTrainInterface();
//				func.replace(" ", "");
//				int index=func.indexOf("|");
////				Object objjj=raceinterface.callFunc(new String[]{func.substring(0,index)} , new String[]{func .substring(index+2).replace("'", "").replace("||", "\r\n")});
//				Object objjj =raceinterface.callFunc(func.substring(0,index), func .substring(index+2).replace("'", "").replace("||", "\r\n"));
////				calcFuncValues(new String[]{formula.substring(0,index)}, new String[]{formula .substring(index+2).replace("'", "").replace("||", "\r\n")});
//				String obj=null;
//				if(objjj==null){
//					return obj;
//				}
//				if(objjj instanceof Object []){
//					
//					Object ob[]=(Object[]) objjj;
//					if(ob!=null&&ob.length>0){
//						if(ob[0] instanceof Object[]){
//							String []ss=(String[]) ob[0];
//							obj=ss[0];
//						}else{
//							obj=ob[0].toString();
//						}
//					}else{
//						obj="";
//					}
//				}else{
//					obj = objjj.toString();
//				}
//			return obj;
			return null;
		}else
		{
			String ip=iq.queryfield("select descriptions from dip_dataurl where code='"+pkServlet+"' and nvl(dr,0)=0");
			String urlStr="";
			if(isNull(ip)){
				throw new Exception("没有找到Servlet注册");
			}
			if(ip.startsWith("http://")){
				ip=ip.substring(7);
			}
			ip=ip.split("/")[0];
			urlStr = "http://" + ip + "/execuFBySDServlet?fou="+func+"&par="+datasource+"|"+usercode+"&issystem="+"no";
			String obj = null;
			HttpURLConnection conn;
			ObjectOutputStream output = null;
			ObjectInputStream input=null;
			try {
				conn = getConnect(urlStr);
				output = new ObjectOutputStream(conn.getOutputStream());
				input = new ObjectInputStream(conn.getInputStream());
				Object objjj=input.readObject();
				if(objjj==null){
					return obj;
				}
				if(objjj instanceof Object []){
					
					Object ob[]=(Object[]) objjj;
					if(ob!=null&&ob.length>0){
						if(ob[0] instanceof Object[]){
							String []ss=(String[]) ob[0];
							obj=ss[0];
						}else{
							obj=ob[0].toString();
						}
					}else{
						obj="";
					}
				}else{
					obj = objjj.toString();
				}
					
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new Exception("从servlet传回的数据解析失败");
			} catch (MalformedURLException e) {
				e.printStackTrace();
				throw new Exception("连接servlet失败");
			} catch (IOException e) {
				e.printStackTrace();
				throw new Exception("解析servlet返回流失败");
			}finally{
				try {
					output.flush();
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(obj.contains("没有权限访问")){
				DateprocessVO vo=new DateprocessVO();
				vo.setDef_str_1(IContrastUtil.DATAPROCESS_ERR);
				vo.setContent(obj+ip);
//				vo.setActivetype(IContrastUtil.)
				ilo.writToDataLog_RequiresNew(vo);
				obj=null;
			}
			return obj;
		}
	}
	private HttpURLConnection getConnect(String url) throws MalformedURLException, IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setDoOutput(true);
		return conn;
	}
	public String querySqlByDesign(String design,String sql){
		String ret = null;
		try {
			ret = iq.queryfield(sql, design);
		} catch (BusinessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DbException e) {
			e.printStackTrace();
		}
		return ret;
	}
	/**
	 * 提供平台公式解析自定义函数，得到计算后结果
	 * 根据 外系统字段值，nc系统的表名，nc表的字段，经过复杂算法得出外系统字段值对应的nc系统的字段值
	 * 算法步骤：
	 * 1、tablename,field 到数据对照表查到对应的nc对应的表和字段  对照表名：dip_contdata，
	 *    nc系统表名：contabcode：外系统表的字段:contcolcode
	 *    外系统表名：extetabcode：外系统表的字段:extecolcode
	 * 2、
	 * 
	 * @author 北京商佳科技 袁廷勤
	 * @param ftablename 外部系统表名字段
	 * @param nctable nc系统表名字段
	 * @param ffieldvalue 外系统表的字段值
	 * @throws Exception
	 * @return 返回  nc系统对应字段的值 
	 * @since nc5.2 2011-05-13
	 */

	public String getDataByKey(String ftablename,String nctable,Object ffieldvalue) throws Exception{
		//加缓存 getDataByKey( "DIP_DD_JYXT_SDFPCK.ZTBMZHBM" , "SM_USER.USER_CODE" ,  "55060100-02007")
		
		
		
		if(ftablename==null||ftablename.length()<=0||ftablename.indexOf(".")<=0||nctable==null||nctable.length()<=0||nctable.indexOf(".")<=0||ffieldvalue==null){
			return "";
		}
		StringBuffer sb=new StringBuffer();
			sb.append("getdatabykey(\""+ftablename+"\",\""+nctable+"\",\"");
			sb.append(ffieldvalue==null?"":ffieldvalue.toString());
			sb.append("\")");
			String key=sb.toString();
		if(iq.getFormmulaCacheValue(key)!=null){
			String res=(String) iq.getFormmulaCacheValue(key);
			return res;
		}else{
		ftablename=ftablename.replaceAll("\"", "");
		nctable=nctable.replaceAll("\"", "");
		String f=ffieldvalue.toString().replaceAll("\"", "");
		String[] str1=ftablename.split("\\.");
		String[] str2=nctable.split("\\.");
		//1、找外部系统的字段值
		//2、找外部 基本档案的对照字段的值
		//3、找到值之后，找对应关系中nc系统的对应主键的值
		//4、找nc系统的要找的字段的值
		String sqlstr="select dip_datadefinit_b.quotetable from dip_datadefinit_b where upper(ename)='"+str1[1].toUpperCase()+"' " +
				"and dip_datadefinit_b.pk_datadefinit_h=(select dip_datadefinit_h.pk_datadefinit_h from dip_datadefinit_h" +
				" where upper(dip_datadefinit_h.memorytable)='"+str1[0].toUpperCase()+"' and nvl(dr,0)=0 )";
		String mb=iq.queryfield(sqlstr);
		if(mb!=null&&mb.length()>0){
			DipDatadefinitBVO bvo=(DipDatadefinitBVO) iq.querySupervoByPk(DipDatadefinitBVO.class, mb);
			if(bvo!=null){
				String sql2="select * from dip_contdata where dip_contdata.contcolcode='"+mb
				+"' and upper(dip_contdata.extetabname)='"+str2[0].toUpperCase()+"' and nvl(dr,0)=0 ";/*"' and upper(dip_contdata.extecolname)='"+str2[1].toUpperCase()+"'"*/;
				List vo=iq.queryVOBySql(sql2, new DipContdataVO());
				if(vo!=null&&vo.size()>=1){//说明有对照
					DipContdataVO dvo=(DipContdataVO)vo.get(0);
					String wname=dvo.getContcolname();//系统字段名称
					String nname=dvo.getExtecolname();//nc系统字段名称
					if(wname!=null&&nname!=null&&wname.length()>0&&nname.length()>0){
						//2、找对应值
						String ss="";
						if(wname.toUpperCase().equals(bvo.getEname().toUpperCase())){
							ss=f;
						}else{
							String sql="select "+wname+" from "+dvo.getContabname()+" where "+bvo.getEname()+"="+(/*f.matches("[0-9]*[.]*[0-9]*")?f:*/"'"+f+"'");
							ss=iq.queryfield(sql);
						}
						if(ss!=null&&ss.length()>0){
							String sql3= "select Extepk from "+dvo.getMiddletab()+" where Contpk='"+ss+"' ";
							String extepk = iq.queryfield(sql3);
							if(extepk!=null&&extepk.length()>0){
							//根据主键查找nc系统字段的值
								String sql = "select "+str2[1]+" from "+str2[0]+" where "+dvo.getExtecolname()+"='"+extepk+"' and nvl(dr,0) =0 ";
								String returnvalue = iq.queryfield(sql);
								if(returnvalue==null||returnvalue.length()<=0){
									returnvalue="";
									Logger.debug("解析公式返回空:【"+dvo.getMiddletab()+"】"+sql);
								}
								
								iq.putFormmulaCache(key, returnvalue);
								return returnvalue;
							}
						}
					}
				}
			}
		}
		iq.putFormmulaCache(key, "");
	    return "";
	 }
	}	
	
	
	public String trim(Object obj){
		String str="";
		if(obj!=null){
			str=obj.toString().replace(" ", "");
		}
		return str;
	}
	
	public String formatDef(Object dou,Object num){
		NumberFormat nf=NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		nf.setMaximumFractionDigits(8);
		String ss=nf.format(dou);
		System.out.println(nf.format(dou));
		
		String regex1="^[0-9]*\\.{0,1}[0-9]*$";
		int k=0;
		if(num instanceof Integer){
			k=(Integer)num;
		}
		if(ss.matches(regex1)&&k>0&&k<=8){
			
			
			if(ss.contains(".")){
				String str[]=ss.split("\\.");
				if(str[1].length()>=k){
					str[1]=str[1].substring(0, k);
					return str[0]+"."+str[1];
				}else{
					int f=k-str[1].length();
					if(f>0){
						for(int i=0;i<f;i++){
							str[1]=str[1]+"0";	
						}
						
					}
					return str[0]+"."+str[1];
				}
			}else{
				String ssss=ss+".";
				for(int i=0;i<k;i++){
					ssss=ssss+"0";
				}
				return ssss;
			}	
		}
		return "";
	}
}
