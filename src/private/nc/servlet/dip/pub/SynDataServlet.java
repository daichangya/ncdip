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
public class SynDataServlet extends HttpServlet {

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
		// ���մ��ݹ�������Ϣ
		String classname = request.getParameter("classname");
		String startcount = request.getParameter("startcount");
		String count = request.getParameter("count");
		String argm = "non>="+startcount+" and non<="+count;
		String par=request.getParameter("par");
		String fields=request.getParameter("fields");//��ʾ����Щ�ֶΣ����Ϊ�ձ�ʾ�����ֶΡ�
		String datasource="";
		String usercode="";
		Object resultObj=null;
		Boolean flag=new Boolean(false);
		if(par!=null&&par.contains("|")){
			datasource=par.split("\\|")[0];
			usercode=par.split("\\|")[1];
		}
		if(datasource==null||datasource.trim().equals("")||usercode==null||usercode.trim().equals("")){
			resultObj="����Դ���û�������Ϊ��";
		}else{
			/**У������Դ��ip���û����Ƿ�Ϸ�*/
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
				 if(ServletUtil.validateResources(usercode, datasource, ServletUtil.DIP_ROLEANDTABLE_B, classname)){//У����Դ
					 List<Map> list = null;
					 String sql="";
					 if(fields==null||fields.trim().equals("")){
						 resultObj="��ѯ�ֶβ���Ϊ��"; 	 
					 }else{
						 sql="select "+fields+" from (select rownum non,"+classname+".* from "+classname+") where  "+argm;
						 //һ�λ�ȡȫ������
						 list = (List<Map>)new BaseDAO(datasource).executeQuery(sql, new MapListProcessor());
						 resultObj=list;
					 }
					 
				 }else{
					 resultObj=usercode+"û�б�"+classname+"����Ȩ��";	
				 }
			 } catch (BusinessException e) {
				 e.printStackTrace();
				 resultObj="У�����Դ"+classname+"����"+e.getMessage();
			 } 
		 }
		
		ObjectOutputStream oos = new ObjectOutputStream(response
				.getOutputStream());
		oos.writeObject(resultObj);
		oos.flush();
	}
	
	

}
