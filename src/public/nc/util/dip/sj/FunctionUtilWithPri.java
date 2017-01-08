package nc.util.dip.sj;

import java.util.ArrayList;
import java.util.Collection;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.vo.bd.settle.UserVO;
import nc.vo.dip.actionset.ActionSetBVO;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;

public class FunctionUtilWithPri {
	
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
    
    public static final String NOTEDITSTRS[]={CORPPKNAME,USERIDNAME,COUNTNAME,MESSAGECOUNTNAME,PKNAME,TIMENAME,TOMONTHFUN,
    	TOUPPER,TOLOWER,TRIM,SYSUSERID,SYSUSERCODE,SYSUSERNAME,SYSUSERDATE,SELECTPK,SHOWREPLACE,SAVEREPLACE};
   
    public static final String SYSREF="参照函数";
    public static final String SYSREFPK="SYS.REF";
    public static final String SYSVERSION="版本函数";
    public static final String SYSVERSIONPK="SYS.VERSION";
    
	public String usercode = null;
	
	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	/**
	 *这个类汇总生成消息标志节点预置函数功能。
	 */
	private static IQueryField iqf;
	
	public String getLogInCorpPK(){
		String pk_corp="1001";
		try {
			Collection col = new BaseDAO().retrieveByClause(UserVO.class, "upper(user_code)='"+getUsercode()+"'");
			if(null != col && col.size()>0){
				UserVO[] userVOs = (UserVO[])col.toArray(new UserVO[col.size()]);
				pk_corp = userVOs[0].getPk_corp();
			}
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pk_corp;
	}
	
	public String getLogInUserCuserID(){
		String cuserid="";
		try {
			Collection col = new BaseDAO().retrieveByClause(UserVO.class, "upper(user_code)='"+getUsercode()+"'");
			if(null != col && col.size()>0){
				UserVO[] userVOs = (UserVO[])col.toArray(new UserVO[col.size()]);
				cuserid = userVOs[0].getPrimaryKey();
			}
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cuserid;
	}
	
	public String getLogInUserCode(){
		String usercode="";
		try {
			Collection col = new BaseDAO().retrieveByClause(UserVO.class, "upper(user_code)='"+getUsercode()+"'");
			if(null != col && col.size()>0){
				UserVO[] userVOs = (UserVO[])col.toArray(new UserVO[col.size()]);
				usercode = userVOs[0].getUser_code();
			}
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return usercode;
	}
	
	public String getLogInUserDate(){
		String userdate=new UFDate().toString();
		return userdate;
	}
	
	public String getLogInUserDateTime(){
		UFDateTime serverTime = new UFDateTime(System.currentTimeMillis());
		return serverTime.toString();
	}
	
	public String getLogInUserName(){
		String username="";
		try {
			Collection col = new BaseDAO().retrieveByClause(UserVO.class, "upper(user_code)='"+getUsercode()+"'");
			if(null != col && col.size()>0){
				UserVO[] userVOs = (UserVO[])col.toArray(new UserVO[col.size()]);
				username = userVOs[0].getUser_name();
			}
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	public String getFuctionValue(String functionkey){
		String  value=null;
		if(FunctionUtilWithPri.CORPPKENAME.equals(functionkey)){
			value= getLogInCorpPK();
		}else if(FunctionUtilWithPri.USERIDENAME.equals(functionkey)){
			value= getLogInUserCuserID();
		}else if(FunctionUtilWithPri.PKENAME.equals(functionkey)){
			value= getGeneratorPK(getLogInCorpPK());
		}else if(FunctionUtilWithPri.SYSUSERIDPK.equals(functionkey)){
			value= getLogInUserCuserID();
		}else if(FunctionUtilWithPri.SYSUSERCODEPK.equals(functionkey)){
			value= getLogInUserCode();
		}else if(FunctionUtilWithPri.SYSUSERNAMEPK.equals(functionkey)){
			value= getLogInUserName();
		}else if(FunctionUtilWithPri.SYSUSERDATEPK.equals(functionkey)){
			value= getLogInUserDate();
		}else if("yyyy-MM-dd HH:mm:ss".equals(functionkey)||FunctionUtilWithPri.SYSTSPK.equals(functionkey)){
			value= getLogInUserDateTime();
		}else if("yyyy-MM-dd".equals(functionkey)){
			value= getLogInUserDate();
		}else if(functionkey.startsWith(FunctionUtilWithPri.STRREPLACEPK)){
			int indexOf = functionkey.indexOf("(");
			value = functionkey.substring(indexOf+1, functionkey.length()-1);
			
		}else if(FunctionUtilWithPri.SYSVERSIONPK.equals(functionkey)){
			String dateTime = getLogInUserDateTime().replaceAll(":", "").replaceAll("-", "").replaceAll(" ", "");
			int i=(int)(Math.random()*900)+100;
			String random = i+"";
			value = dateTime+random;
		}
		return value;
	}
	
	public String getFuctionValueWhenUpdate(String functionkey){
		String  value=null;
		if(FunctionUtilWithPri.SYSUPDATEUSERIDPK.equals(functionkey)){
			value= getLogInUserCuserID();
		}else if(FunctionUtilWithPri.SYSUPDATEUSERCODEPK.equals(functionkey)){
			value= getLogInUserCode();
		}else if(FunctionUtilWithPri.SYSUPDATEUSERNAMEPK.equals(functionkey)){
			value= getLogInUserName();
		}else if(FunctionUtilWithPri.SYSUPDATETSPK.equals(functionkey)){
			value= getLogInUserDateTime();
		}
		return value;
	}
	
	public static RetMessage getSaveReplaceFuctionValue(String filedname,String fieldvalue,String showcode,String pk_datadefinit_h){
		RetMessage ret=new RetMessage();
		String  value="";
		if(fieldvalue!=null&&!fieldvalue.trim().equals("")){
			try {
				Collection collection = new BaseDAO().retrieveByClause(DipDatadefinitBVO.class, 
						"pk_datadefinit_h='"+pk_datadefinit_h+"' and ename='"+filedname+"' and coalesce(dr,0)=0 ");
				if(null != collection && collection.size()>0){
					DipDatadefinitBVO[] vos = (DipDatadefinitBVO[])collection.toArray(new DipDatadefinitBVO[collection.size()]);
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
			try {
				Collection collection = new BaseDAO().retrieveByClause(DipDatadefinitBVO.class, 
						"pk_datadefinit_h='"+pk_datadefinit_h+"' and ename='"+filedname+"' and coalesce(dr,0)=0 ");
				if(null != collection && collection.size()>0){
					DipDatadefinitBVO[] vos = (DipDatadefinitBVO[])collection.toArray(new DipDatadefinitBVO[collection.size()]);
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
		if(FunctionUtilWithPri.TOMONTHFUNPK.equals(functionkey)){
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
		}else if(FunctionUtilWithPri.TOUPPERPK.equals(functionkey)){
			if(fieldvalue!=null&&!fieldvalue.trim().equals("")){
				value=fieldvalue.toUpperCase();
			}
		}else if(FunctionUtilWithPri.TOLOWERPK.equals(functionkey)){
			if(fieldvalue!=null&&!fieldvalue.trim().equals("")){
				value=fieldvalue.toLowerCase();
			}
		}else if(FunctionUtilWithPri.TRIMPK.equals(functionkey)){
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
			if(arg.equals(FunctionUtilWithPri.TOMONTHFUNPK)){
				value=FunctionUtilWithPri.TOMONTHFUN;
			}else if(arg.equals(FunctionUtilWithPri.CORPPKENAME)){
				value=FunctionUtilWithPri.CORPPKNAME;
			}else if(arg.equals(FunctionUtilWithPri.PKENAME)){
				value=FunctionUtilWithPri.PKNAME;
			}else if(arg.equals(FunctionUtilWithPri.USERIDENAME)){
				value=FunctionUtilWithPri.USERIDNAME;
			}else if(FunctionUtilWithPri.TOUPPERPK.equals(arg)){
				value=FunctionUtilWithPri.TOUPPER;
			}else if(FunctionUtilWithPri.TOLOWERPK.equals(arg)){
				value=FunctionUtilWithPri.TOLOWER;
			}
		}
		return value;
	}
	
	public static Boolean isUpdateAutoIn(String consvalue){
		if(consvalue.equals(FunctionUtilWithPri.SYSUPDATEUSERIDPK)||
				consvalue.equals(FunctionUtilWithPri.SYSUPDATEUSERCODEPK)||
				consvalue.equals(FunctionUtilWithPri.SYSUPDATEUSERNAMEPK)||
				consvalue.equals(FunctionUtilWithPri.SYSUPDATETSPK)){
			return true;
		}
		return false;
	}
	
	public static Boolean isAutoIn(String consvalue){
		if(consvalue.equals(FunctionUtilWithPri.CORPPKENAME)||
				consvalue.equals(FunctionUtilWithPri.USERIDENAME)||
				consvalue.equals(FunctionUtilWithPri.PKENAME)||
				consvalue.equals(FunctionUtilWithPri.SYSUSERIDPK)||
				consvalue.equals(FunctionUtilWithPri.SYSUSERCODEPK)||
				consvalue.equals(FunctionUtilWithPri.SYSUSERNAMEPK)||
				consvalue.equals(FunctionUtilWithPri.SYSUSERDATEPK)||
				consvalue.equals(FunctionUtilWithPri.SELECTPKPK)||
				consvalue.equals("yyyy-MM-dd HH:mm:ss")||
				consvalue.equals("yyyy-MM-dd")||
				consvalue.equals(FunctionUtilWithPri.SYSTSPK)||
				consvalue.startsWith(FunctionUtilWithPri.STRREPLACEPK)){
			return true;
		}
		return false;
	}
	
	public String execAction(ActionSetBVO rowVO,String pk_datadefinit_h,
			ArrayList<String> list,String whereCondition,String pkTableField) throws Exception{
		String verifycon = rowVO.getVerifycon();
		DipDatadefinitHVO datadefinitVO = (DipDatadefinitHVO)new BaseDAO().retrieveByPK(DipDatadefinitHVO.class,
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
	
	private String replaceString(String sql){
		if(sql.indexOf(FunctionUtilWithPri.SYSUPDATETSPK_ACTION)!=-1){
			String str = getFuctionValueWhenUpdate(FunctionUtilWithPri.SYSUPDATETSPK);
			sql = sql.replaceAll(FunctionUtilWithPri.SYSUPDATETSPK_ACTION, "'"+str+"'");
		}
		if(sql.indexOf(FunctionUtilWithPri.SYSUPDATEUSERCODEPK_ACTION)!=-1){
			String str = getFuctionValueWhenUpdate(FunctionUtilWithPri.SYSUPDATEUSERCODEPK);
			sql = sql.replaceAll(FunctionUtilWithPri.SYSUPDATEUSERCODEPK_ACTION, "'"+str+"'");
		}
		if(sql.indexOf(FunctionUtilWithPri.SYSUPDATEUSERNAMEPK_ACTION)!=-1){
			String str = getFuctionValueWhenUpdate(FunctionUtilWithPri.SYSUPDATEUSERNAMEPK);
			sql = sql.replaceAll(FunctionUtilWithPri.SYSUPDATEUSERNAMEPK_ACTION, "'"+str+"'");
		}
		if(sql.indexOf(FunctionUtilWithPri.SYSUPDATEUSERIDPK_ACTION)!=-1){
			String str = getFuctionValueWhenUpdate(FunctionUtilWithPri.SYSUPDATEUSERIDPK);
			sql = sql.replaceAll(FunctionUtilWithPri.SYSUPDATEUSERIDPK_ACTION, "'"+str+"'");
		}
		if(sql.indexOf(FunctionUtilWithPri.PKENAME_ACTION)!=-1){
			String str = getFuctionValue(FunctionUtilWithPri.PKENAME);
			sql = sql.replaceAll(PKENAME_ACTION, "'"+str+"'");
		}
		return sql;
	}
	
	private String execUpdateSql(String[] split,String tablename,String whereCondition,String pkTableField) throws Exception{
		ArrayList<String> sqlList = new ArrayList<String>();
		for (int i = 0; i < split.length; i++) {
			String sql = split[i].replaceAll(FunctionUtilWithPri.SELECTPKPK_ACTION, "select "+pkTableField+" from "+tablename+" where 1=1 "+whereCondition+"");
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

	private String verifySql(String[] split, int i,String tablename,String whereCondition,String pkTableField)
			throws Exception {
		String msg = null;
		String verifySql = split[i];
		String[] split2 = verifySql.split("#");
		String sql = split2[0];
		sql = sql.replaceAll(FunctionUtilWithPri.SELECTPKPK_ACTION, "select "+pkTableField+" from "+tablename+" where 1=1 "+whereCondition+"");
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
