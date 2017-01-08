package nc.servlet.dip.pub;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.fi.uforeport.NCFuncForUFO;

public class ExecuFBySDServlet  extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		service(arg0, arg1);
	}

	@Override
	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		service(arg0, arg1);
	}
	@Override
	protected void service(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		String formula=new String(request.getParameter("fou").getBytes("ISO-8859-1"),"gb18030");
		String par=request.getParameter("par");
		String isSystem=request.getParameter("issystem");
		String usercode="";
		String dataSource="";
		String unitcode="";
		Object res=null;//最后写出去的值
		if(par!=null&&par.contains("|")){
			String pars[]=par.split("\\|");
			if(pars!=null&&isSystem!=null){
				if(isSystem.equals("no")&&pars.length==2){
					dataSource=pars[0];
					usercode=pars[1];
				}else if(isSystem.equals("yes")&&pars.length==3){
					dataSource=pars[0];
					unitcode=pars[1];
					usercode=pars[2];
				}else{
					res="servlet参数错误";
				}
			}
		}else{
			res="servlet参数错误";
		}
		if(res==null){
			Object validateObj=ServletUtil.validate(dataSource, request, usercode);
			if(validateObj instanceof Boolean){
				try {
					res=handelFunction(formula, dataSource, request, isSystem, usercode, unitcode);
				} catch (Exception e) {
					e.printStackTrace();
					res=e.getMessage();
				}
			}else{
				res=validateObj.toString();
			}
		}
		ObjectOutputStream oos = new ObjectOutputStream(response
				.getOutputStream());
		oos.writeObject(res);
		oos.flush();
	}
	
	public Object handelFunction(String formula,String dataSource,HttpServletRequest request,String isSystem,String usercode,String unitcode) throws Exception{
		Object res=null;	
		formula=formula.replace(" ", "");
		int index=formula.indexOf("|");
		if(index<0){
			res="输入格式错误";
			return res;
		}
		String functionname=formula.substring(0,index);
		String funStrParam=formula .substring(index+2).replace("'", "").replace("||", "\r\n");
		boolean flag=ServletUtil.validateResources(usercode, dataSource, ServletUtil.DIP_ROLEANDFUNCTION_C, functionname);
		if(!flag){
			res="用户"+usercode+"没有访问"+functionname+"权限";
			return res;
		}
		if(isSystem.equals("no")){//非系统处理方式
			   res=noSystemHandleMethod(formula, functionname, dataSource, funStrParam,usercode);
        }else if(isSystem.equals("yes")){//系统处理方式
					//"&par="+accountcode 0+"|"+unitcode 1+"|"+ usercode 2+"|"+password 3;
			 String accountcode=ServletUtil.getAccountCode(dataSource);
			 if(accountcode!=null&&accountcode.trim().length()>0){
				 res=systemHandleMethod( dataSource, accountcode, functionname, funStrParam, usercode, unitcode);
			 }else{
				 res="根据数据源得帐套编码错误";
			 }
        }else{
    	   res="servlet参数错误";
        }
			return res;
	}
	
	public Object noSystemHandleMethod(String formula,String functionname,String dataSource,String strParam,String usercode) throws Exception{
		Object res=null;
		String sql="select module_class from nc_iufo_module where module_prefix='"+functionname+"'";
		ArrayList<HashMap> list=null;
		BaseDAO bd=ServletUtil.getBaseDao(dataSource);
		list=(ArrayList<HashMap>) bd.executeQuery(sql, new MapListProcessor());
		if(list!=null||list.size()>0){
			HashMap map=list.get(0);
			if(map!=null&&map.get("module_class")!=null){
				String classname=map.get("module_class").toString();
				if(classname.trim().toString().length()>0){
						Class cl=(Class) Class.forName(classname).newInstance();
						Method checkFunc=cl.getMethod("checkFunc", new Class[]{String.class,String.class});
						Object obj=checkFunc.invoke(cl, new String[]{functionname,strParam});
						if(obj==null){
							Method callFunc=cl.getMethod("callFunc", new Class[]{String.class,String.class});
							res=callFunc.invoke(cl, new String[]{functionname,strParam});
						}else{
							res=obj;
						}
			}
		}
		}else{
			res="没有找到"+functionname+"对应的注册函数";
		}
		return res;
	}
	
	public Object systemHandleMethod(String dataSource,String accountcode,
			String functionname,String funStrParam,String usercode,String unitcode) throws Exception{
//		帐套编码，公司编码，用户编码
		Object res=null;
		String date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		NCFuncForUFO fu=new NCFuncForUFO();
		List list=null;
		BaseDAO bd=ServletUtil.getBaseDao(dataSource);
		list=(List) bd.executeQuery("select user_password from sm_user where user_code='"+usercode+"'",new MapListProcessor());
		if(list!=null&&list.size()>0){
			Map map=(Map) list.get(0);
			String password=null;
			if(map.containsKey("user_password")){
				password=(String) map.get("user_password");
			}
			if(map.containsKey("user_password".toUpperCase())){
				password=(String) map.get("user_password".toUpperCase());
			}
			nc.vo.framework.rsa.Encode ecode=new  nc.vo.framework.rsa.Encode();
//								//数据源名称, //账套,//单位,//日期, //用户,//密码,//语言类型
			String[] calEnv = {dataSource,accountcode,unitcode, date, unitcode,ecode.decode(password==null?"":password), date,"simpchn"};
			fu=new NCFuncForUFO();
			fu.setCalEnv(calEnv);
			res= fu.checkFunc(functionname , funStrParam);
			if(res==null){
			   res=fu.calcFuncValues(new String[]{functionname}, new String[]{funStrParam});	
			}
		}
		return res;
		}
}
