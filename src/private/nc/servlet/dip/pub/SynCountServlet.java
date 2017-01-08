package nc.servlet.dip.pub;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pub.BusinessException;


@SuppressWarnings("serial")
public class SynCountServlet extends HttpServlet{
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
		// 接收传递过来的信息
//		IQueryField query;
		String classname = request.getParameter("classname");
		String par=request.getParameter("par");
		String datasource="";
		String usercode="";
		Object resultObj=null;
		Boolean flag=new Boolean(false);
		if(par!=null&&par.contains("|")){
			datasource=par.split("\\|")[0];
			usercode=par.split("\\|")[1];
		}
		if(datasource==null||datasource.trim().equals("")||usercode==null||usercode.trim().equals("")){
			resultObj="数据源和用户名不能为空";
		}else{
			/**校验数据源，ip，用户名是否合法*/
			Object validateResult=ServletUtil.validate(datasource, request, usercode);
			if(validateResult!=null){
				if(validateResult instanceof Boolean){
					 flag=(Boolean) validateResult;
				}else{
					resultObj=validateResult.toString();
				}
			}
		}
		if(flag){
			 try {
				 if(ServletUtil.validateResources(usercode, datasource, ServletUtil.DIP_ROLEANDTABLE_B, classname)){//校验资源
					 String sql="select count(*) count from "+classname;
						//一次获取全部数据
						List<Map> list = null;
							list = (List<Map>)new BaseDAO(datasource).executeQuery(sql, new MapListProcessor());
						if(null!=list&&list.size()>0){
							Map map=list.get(0);
							resultObj = Integer.parseInt(map.get("count").toString());
						}
				 }else{
					 resultObj="用户"+usercode+"没有表"+classname+"访问权限";	
				 }
			 } catch (BusinessException e) {
				 e.printStackTrace();
			 } catch (IllegalArgumentException e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
			 } catch (SecurityException e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
			 }
		 }
		ObjectOutputStream oos = new ObjectOutputStream(response
				.getOutputStream());
		oos.writeObject(resultObj);
		oos.flush();
	}
}
