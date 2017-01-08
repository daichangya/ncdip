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
		Object res=null;//���д��ȥ��ֵ
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
					res="servlet��������";
				}
			}
		}else{
			res="servlet��������";
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
			res="�����ʽ����";
			return res;
		}
		String functionname=formula.substring(0,index);
		String funStrParam=formula .substring(index+2).replace("'", "").replace("||", "\r\n");
		boolean flag=ServletUtil.validateResources(usercode, dataSource, ServletUtil.DIP_ROLEANDFUNCTION_C, functionname);
		if(!flag){
			res="�û�"+usercode+"û�з���"+functionname+"Ȩ��";
			return res;
		}
		if(isSystem.equals("no")){//��ϵͳ����ʽ
			   res=noSystemHandleMethod(formula, functionname, dataSource, funStrParam,usercode);
        }else if(isSystem.equals("yes")){//ϵͳ����ʽ
					//"&par="+accountcode 0+"|"+unitcode 1+"|"+ usercode 2+"|"+password 3;
			 String accountcode=ServletUtil.getAccountCode(dataSource);
			 if(accountcode!=null&&accountcode.trim().length()>0){
				 res=systemHandleMethod( dataSource, accountcode, functionname, funStrParam, usercode, unitcode);
			 }else{
				 res="��������Դ�����ױ������";
			 }
        }else{
    	   res="servlet��������";
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
			res="û���ҵ�"+functionname+"��Ӧ��ע�ắ��";
		}
		return res;
	}
	
	public Object systemHandleMethod(String dataSource,String accountcode,
			String functionname,String funStrParam,String usercode,String unitcode) throws Exception{
//		���ױ��룬��˾���룬�û�����
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
//								//����Դ����, //����,//��λ,//����, //�û�,//����,//��������
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
