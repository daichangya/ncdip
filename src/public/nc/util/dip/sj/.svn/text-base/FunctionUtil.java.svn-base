package nc.util.dip.sj;

import java.util.ArrayList;
import java.util.Random;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.dip.actionset.ActionSetBVO;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.pub.lang.UFDateTime;

public class FunctionUtil {
	
	public static final String CORPPKNAME="登录公司主键";
    public static final String CORPPKENAME="CORPPK";
    public static final String USERIDNAME="登录用户主键";
    public static final String USERIDENAME="CUSERID";
    public static final String COUNTNAME="记录数函数";
    public static final String COUNTENAME="COUNT";
    public static final String MESSAGECOUNTNAME="报文数函数";
    public static final String MESSAGECOUNTENAME="MESSAGECOUNT";
    public static final String PKNAME="主键函数";
    public static final String PKENAME="SYS.PK";
    public static final String PKENAME_ACTION="@SYS.PK@";
    public static final String MARKNAME="分割标记";
    public static final String TIMENAME="时间函数";
    public static final String TOMONTHFUN="月份函数";
    public static final String TOMONTHFUNPK="MM";
    public static final String TOUPPER="转换大写";
    public static final String TOUPPERPK="TOUPPER";
    public static final String TOLOWER="转换小写";
    public static final String TOLOWERPK="TOLOWER";
    public static final String TRIM="去空格";
    public static final String TRIMPK="TRIM";
    
    public static final String SYSUSERID="用户主键函数";
    public static final String SYSUSERIDPK="SYS.USERID";
    
    public static final String SYSUSERCODE="用户编码函数";
    public static final String SYSUSERCODEPK="SYS.USERCODE";
    
    public static final String SYSUSERNAME="用户名称函数";
    public static final String SYSUSERNAMEPK="SYS.USERNAME";
    
    public static final String SYSUSERDATE="用户日期函数";
    public static final String SYSUSERDATEPK="SYS.USERDATE";
    
    public static final String SELECTPK="选择主键函数";
    public static final String SELECTPKPK="SYS.SELECTPK";
    
    public static final String SELECTPKPK_ACTION="@SYS.SELECTPK@";
    
    public static final String SHOWREPLACE="显示替换函数";
    public static final String SHOWREPLACEPK="SYS.SHOWREPLACE";
    
    public static final String SAVEREPLACE="保存替换函数";
    public static final String SAVEREPLACEPK="SYS.SAVEREPLACE";
    
    public static final String STRREPLACE="常量替换函数";
    public static final String STRREPLACEPK="SYS.TOSTRING";
    
    public static final String SYSTS="用户时间函数";
    public static final String SYSTSPK="SYS.TS";
    
    public static final String SYSUPDATETS="修改时间函数";
    public static final String SYSUPDATETSPK="SYS.UPDATETS";
    public static final String SYSUPDATETSPK_ACTION="@SYS.UPDATETS@";
    
    public static final String SYSUPDATEUSERID="修改用户主键函数";
    public static final String SYSUPDATEUSERIDPK="SYS.UPDATEUSERID";
    public static final String SYSUPDATEUSERIDPK_ACTION="@SYS.UPDATEUSERID@";
    
    public static final String SYSUPDATEUSERCODE="修改用户编码函数";
    public static final String SYSUPDATEUSERCODEPK="SYS.UPDATEUSERCODE";
    public static final String SYSUPDATEUSERCODEPK_ACTION="@SYS.UPDATEUSERCODE@";
    
    public static final String SYSUPDATEUSERNAME="修改用户名称函数";
    public static final String SYSUPDATEUSERNAMEPK="SYS.UPDATEUSERNAME";
    public static final String SYSUPDATEUSERNAMEPK_ACTION="@SYS.UPDATEUSERNAME@";
    
    public static final String SYSREF="参照函数";
    public static final String SYSREFPK="SYS.REF";
    public static final String SYSVERSION="版本函数";
    public static final String SYSVERSIONPK="SYS.VERSION";
    
    public static final String NOTEDITSTRS[]={CORPPKNAME,USERIDNAME,COUNTNAME,MESSAGECOUNTNAME,PKNAME,TIMENAME,TOMONTHFUN,
    	TOUPPER,TOLOWER,TRIM,SYSUSERID,SYSUSERCODE,SYSUSERNAME,SYSUSERDATE,SELECTPK,SHOWREPLACE,SAVEREPLACE};
   
	
	
	/**
	 *这个类汇总生成消息标志节点预置函数功能。
	 */
	private static IQueryField iqf;
	
	public static String getLogInCorpPK(){
		String pk=ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		return pk;
	}
	
	public static String getLogInUserCuserID(){
		String cuserid=ClientEnvironment.getInstance().getUser().getPrimaryKey();
		return cuserid;
	}
	
	public static String getLogInUserCode(){
		String usercode=ClientEnvironment.getInstance().getUser().getUserCode();
		return usercode;
	}
	
	public static String getLogInUserDate(){
		String userdate=ClientEnvironment.getServerTime().getDate().toString();
		return userdate;
	}
	
	public static String getLogInUserDateTime(){
		UFDateTime serverTime = ClientEnvironment.getServerTime();
		return serverTime.toString();
	}
	
	public static String getLogInUserName(){
		String username=ClientEnvironment.getInstance().getUser().getUserName();
		return username;
	}
	
	public static String getGeneratorPK(String pk_corp){
		String pk=getIqf().getOID(pk_corp);
		return pk;
	}


	public static IQueryField getIqf() {
		if(iqf==null){
			iqf=NCLocator.getInstance().lookup(IQueryField.class);
		}
		return iqf;
	}
	
	public static String getFuctionValue(String functionkey){
		String  value=null;
		if(FunctionUtil.CORPPKENAME.equals(functionkey)){
			value= getLogInCorpPK();
		}else if(FunctionUtil.USERIDENAME.equals(functionkey)){
			value= getLogInUserCuserID();
		}else if(FunctionUtil.PKENAME.equals(functionkey)){
			value= getGeneratorPK(getLogInCorpPK());
		}else if(FunctionUtil.SYSUSERIDPK.equals(functionkey)){
			value= getLogInUserCuserID();
		}else if(FunctionUtil.SYSUSERCODEPK.equals(functionkey)){
			value= getLogInUserCode();
		}else if(FunctionUtil.SYSUSERNAMEPK.equals(functionkey)){
			value= getLogInUserName();
		}else if(FunctionUtil.SYSUSERDATEPK.equals(functionkey)){
			value= getLogInUserDate();
		}else if("yyyy-MM-dd HH:mm:ss".equals(functionkey)||FunctionUtil.SYSTSPK.equals(functionkey)){
			value= getLogInUserDateTime();
		}else if("yyyy-MM-dd".equals(functionkey)){
			value= getLogInUserDate();
		}else if(functionkey.startsWith(FunctionUtil.STRREPLACEPK)){
			int indexOf = functionkey.indexOf("(");
			value = functionkey.substring(indexOf+1, functionkey.length()-1);
			
		}else if(FunctionUtil.SYSVERSIONPK.equals(functionkey)){
			String dateTime = getLogInUserDateTime().replaceAll(":", "").replaceAll("-", "").replaceAll(" ", "");
			int i=(int)(Math.random()*900)+100;
			String random = i+"";
			value = dateTime+random;
		}
		return value;
	}
	
	public static String getFuctionValueWhenUpdate(String functionkey){
		String  value=null;
		if(FunctionUtil.SYSUPDATEUSERIDPK.equals(functionkey)){
			value= getLogInUserCuserID();
		}else if(FunctionUtil.SYSUPDATEUSERCODEPK.equals(functionkey)){
			value= getLogInUserCode();
		}else if(FunctionUtil.SYSUPDATEUSERNAMEPK.equals(functionkey)){
			value= getLogInUserName();
		}else if(FunctionUtil.SYSUPDATETSPK.equals(functionkey)){
			value= getLogInUserDateTime();
		}
		return value;
	}
	
	public static RetMessage getSaveReplaceFuctionValue(String filedname,String fieldvalue,String showcode,String pk_datadefinit_h){
		RetMessage ret=new RetMessage();
		String  value="";
		if(fieldvalue!=null&&!fieldvalue.trim().equals("")){
			DipDatadefinitBVO[] vos;
			try {
				vos = (DipDatadefinitBVO[])HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, 
						"pk_datadefinit_h='"+pk_datadefinit_h+"' and ename='"+filedname+"' and coalesce(dr,0)=0 ");
				if(null != vos && vos.length>0){
					String quotetable = vos[0].getQuotetable();
					String quotecolu = vos[0].getQuotecolu();
					if(null != quotetable && null != quotecolu){
						String memorytable = getIqf().queryfield("SELECT memorytable FROM v_dip_jgyyzdtree WHERE pk_datadefinit_b='"+quotetable+"'");
						if(null != memorytable && !"".equals(memorytable)){
							String sql = "select "+showcode+" from "+memorytable+" where "+quotecolu+"='"+fieldvalue.trim()+"'";
							value = getIqf().queryfield(sql);
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(value!=null){//允许为空字符串
			ret.setIssucc(true);
			ret.setValue(value);
		}else{
			ret.setIssucc(false);
			ret.setMessage("函数["+SAVEREPLACE+"]取数错误");
		}
		return ret;
	}
	
	public static RetMessage getShowReplaceFuctionValue(String filedname,String fieldvalue,String showcode,String pk_datadefinit_h){
		RetMessage ret=new RetMessage();
		String  value="";
		if(fieldvalue!=null&&!fieldvalue.trim().equals("")){
			DipDatadefinitBVO[] vos;
			try {
				vos = (DipDatadefinitBVO[])HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, 
						"pk_datadefinit_h='"+pk_datadefinit_h+"' and ename='"+filedname+"' and coalesce(dr,0)=0 ");
				if(null != vos && vos.length>0){
					String quotetable = vos[0].getQuotetable();
					String quotecolu = vos[0].getQuotecolu();
					if(null != quotetable && null != quotecolu){
						String memorytable = getIqf().queryfield("SELECT memorytable FROM v_dip_jgyyzdtree WHERE pk_datadefinit_b='"+quotetable+"'");
						if(null != memorytable && !"".equals(memorytable)){
							String sql = "select "+showcode+" from "+memorytable+" where "+quotecolu+"='"+fieldvalue.trim()+"'";
							value = getIqf().queryfield(sql);
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(value!=null){//允许为空字符串
			ret.setIssucc(true);
			ret.setValue(value);
		}else{
			ret.setIssucc(false);
			ret.setMessage("函数["+SHOWREPLACE+"]取数错误");
		}
		return ret;
	}
	
	public static RetMessage getFuctionValue(String fieldvalue,String functionkey){
		RetMessage ret=new RetMessage();
		String  value="";
		if(FunctionUtil.TOMONTHFUNPK.equals(functionkey)){
			if(fieldvalue!=null&&!fieldvalue.trim().equals("")){
				try {
					int a=Integer.parseInt(fieldvalue);
					if(a<=0||a>=13){
						ret.setIssucc(false);
						ret.setMessage("函数["+getNameOrKey(functionkey)+"]取值错误，导入数据必须是1-12之间");
						return ret;
					}else{
						if(a<10){
							value="0"+a;
						}else{
							value=""+a;
						}
					}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ret.setIssucc(false);
					ret.setMessage("函数["+getNameOrKey(functionkey)+"]取值错误，导入数据必须是1-12之间");
					return ret;
				}
			}
		}else if(FunctionUtil.TOUPPERPK.equals(functionkey)){
			if(fieldvalue!=null&&!fieldvalue.trim().equals("")){
				value=fieldvalue.toUpperCase();
			}
		}else if(FunctionUtil.TOLOWERPK.equals(functionkey)){
			if(fieldvalue!=null&&!fieldvalue.trim().equals("")){
				value=fieldvalue.toLowerCase();
			}
		}else if(FunctionUtil.TRIMPK.equals(functionkey)){
			if(fieldvalue!=null&&!fieldvalue.trim().equals("")){
				value=fieldvalue.replace(" ", "");
			}
		}else{
			ret.setIssucc(false);
			ret.setMessage("没有找到"+functionkey+"所对应的函数");
			return ret;
		}
		if(value!=null){//允许为空字符串
			ret.setIssucc(true);
			ret.setValue(value);
		}else{
			ret.setIssucc(false);
			ret.setMessage("函数["+getNameOrKey(functionkey)+"]取数错误");
		}
		return ret;
	}
	
	public  static  String getNameOrKey(String arg){
		String value="";
		if(arg!=null&&!arg.trim().equals("")){
			if(arg.equals(FunctionUtil.TOMONTHFUNPK)){
				value=FunctionUtil.TOMONTHFUN;
			}else if(arg.equals(FunctionUtil.CORPPKENAME)){
				value=FunctionUtil.CORPPKNAME;
			}else if(arg.equals(FunctionUtil.PKENAME)){
				value=FunctionUtil.PKNAME;
			}else if(arg.equals(FunctionUtil.USERIDENAME)){
				value=FunctionUtil.USERIDNAME;
			}else if(FunctionUtil.TOUPPERPK.equals(arg)){
				value=FunctionUtil.TOUPPER;
			}else if(FunctionUtil.TOLOWERPK.equals(arg)){
				value=FunctionUtil.TOLOWER;
			}
		}
		return value;
	}
	
	public static Boolean isUpdateAutoIn(String consvalue){
		if(consvalue.equals(FunctionUtil.SYSUPDATEUSERIDPK)||
				consvalue.equals(FunctionUtil.SYSUPDATEUSERCODEPK)||
				consvalue.equals(FunctionUtil.SYSUPDATEUSERNAMEPK)||
				consvalue.equals(FunctionUtil.SYSUPDATETSPK)){
			return true;
		}
		return false;
	}
	
	public static Boolean isAutoIn(String consvalue){
		if(consvalue.equals(FunctionUtil.CORPPKENAME)||
				consvalue.equals(FunctionUtil.USERIDENAME)||
				consvalue.equals(FunctionUtil.PKENAME)||
				consvalue.equals(FunctionUtil.SYSUSERIDPK)||
				consvalue.equals(FunctionUtil.SYSUSERCODEPK)||
				consvalue.equals(FunctionUtil.SYSUSERNAMEPK)||
				consvalue.equals(FunctionUtil.SYSUSERDATEPK)||
				consvalue.equals(FunctionUtil.SELECTPKPK)||
				consvalue.equals("yyyy-MM-dd HH:mm:ss")||
				consvalue.equals("yyyy-MM-dd")||
				consvalue.equals(FunctionUtil.SYSTSPK)||
				consvalue.startsWith(FunctionUtil.STRREPLACEPK)||
				consvalue.startsWith(FunctionUtil.SYSVERSIONPK)){
			return true;
		}
		return false;
	}
	
	public static String execAction(ActionSetBVO rowVO,String pk_datadefinit_h,
			ArrayList<String> list,String whereCondition,String pkTableField) throws Exception{
		String verifycon = rowVO.getVerifycon();
		DipDatadefinitHVO datadefinitVO = (DipDatadefinitHVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class,
				pk_datadefinit_h);
		if(list.size()>0){
			for (String string : list) {
				whereCondition+="'"+string+"',";
			}
			whereCondition = whereCondition.substring(0, whereCondition.length()-1);
			whereCondition = " and "+pkTableField+" in("+whereCondition+")";
		}
		String memorytable = datadefinitVO.getMemorytable();
		if(null != verifycon && !"".equals(verifycon)){
			String[] split = verifycon.split(";");
			for (int i = 0; i < split.length; i++) {
				String verifyMsg = verifySql(split, i,memorytable,whereCondition,pkTableField );
				if(null != verifyMsg){
					return verifyMsg;
				}
			}
		}
		String updatecon = rowVO.getUpdatecon();
		if(null != updatecon && !"".equals(updatecon)){
			String[] split = updatecon.split(";");
			String updateMsg = execUpdateSql(split,memorytable,whereCondition,pkTableField);
			if(null != updateMsg){
				return updateMsg;
			}
		}
		return null;
	}
	
	private static String replaceString(String sql){
		if(sql.indexOf(FunctionUtil.SYSUPDATETSPK_ACTION)!=-1){
			String str = getFuctionValueWhenUpdate(FunctionUtil.SYSUPDATETSPK);
			sql = sql.replaceAll(FunctionUtil.SYSUPDATETSPK_ACTION, "'"+str+"'");
		}
		if(sql.indexOf(FunctionUtil.SYSUPDATEUSERCODEPK_ACTION)!=-1){
			String str = getFuctionValueWhenUpdate(FunctionUtil.SYSUPDATEUSERCODEPK);
			sql = sql.replaceAll(FunctionUtil.SYSUPDATEUSERCODEPK_ACTION, "'"+str+"'");
		}
		if(sql.indexOf(FunctionUtil.SYSUPDATEUSERNAMEPK_ACTION)!=-1){
			String str = getFuctionValueWhenUpdate(FunctionUtil.SYSUPDATEUSERNAMEPK);
			sql = sql.replaceAll(FunctionUtil.SYSUPDATEUSERNAMEPK_ACTION, "'"+str+"'");
		}
		if(sql.indexOf(FunctionUtil.SYSUPDATEUSERIDPK_ACTION)!=-1){
			String str = getFuctionValueWhenUpdate(FunctionUtil.SYSUPDATEUSERIDPK);
			sql = sql.replaceAll(FunctionUtil.SYSUPDATEUSERIDPK_ACTION, "'"+str+"'");
		}
		if(sql.indexOf(FunctionUtil.PKENAME_ACTION)!=-1){
			String str = getFuctionValue(FunctionUtil.PKENAME);
			sql = sql.replaceAll(PKENAME_ACTION, "'"+str+"'");
		}
		return sql;
	}
	
	private static String execUpdateSql(String[] split,String tablename,String whereCondition,String pkTableField) throws Exception{
		ArrayList<String> sqlList = new ArrayList<String>();
		for (int i = 0; i < split.length; i++) {
			String sql = split[i].replaceAll(FunctionUtil.SELECTPKPK_ACTION, "select "+pkTableField+" from "+tablename+" where 1=1 "+whereCondition+"");
			sql = replaceString(sql);
			sqlList.add(sql);
		}
		RetMessage retMessage = getIqf().exectEverySqlByHandCommit(sqlList);
		if(!retMessage.getIssucc()){
			return retMessage.getMessage();
		}else{
			return null;
		}
	}

	private static String verifySql(String[] split, int i,String tablename,String whereCondition,String pkTableField)
			throws Exception {
		String msg = null;
		String verifySql = split[i];
		String[] split2 = verifySql.split("#");
		String sql = split2[0];
		sql = sql.replaceAll(FunctionUtil.SELECTPKPK_ACTION, "select "+pkTableField+" from "+tablename+" where 1=1 "+whereCondition+"");
		sql = replaceString(sql);
		String count_str = getIqf().queryfield(sql);
		String fh = split2[1];
		String value = split2[2];
		int count=Integer.parseInt(count_str);
		int valueint = Integer.parseInt(value);
		if(fh.equals("<")){
			if(count<valueint){
				
			}else{
				msg = split2[3];
			}
		}else if(fh.equals(">")){
			if(count>valueint){
				
			}else{
				msg = split2[3];
			}
		}else if(fh.equals("=")){
			if(count==valueint){
				
			}else{
				msg = split2[3];
			}
		}else if(fh.equals("<=")){
			if(count<=valueint){
				
			}else{
				msg = split2[3];
			}
		}else if(fh.equals(">=")){
			if(count>=valueint){
				
			}else{
				msg = split2[3];
			}
		}
		return msg;
	}
}
