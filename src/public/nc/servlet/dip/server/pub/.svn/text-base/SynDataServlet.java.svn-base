package nc.servlet.dip.server.pub;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
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
	public String getIpAddr(HttpServletRequest request) {   
		   String ip = request.getHeader("x-forwarded-for");   
		   if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
		      ip = request.getHeader("Proxy-Client-IP");   
		    }   
		    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
		        ip = request.getHeader("WL-Proxy-Client-IP");   
		   }   
		    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
		        ip = request.getRemoteAddr();   
		    }   
		   return ip;   
		} 
	@Override
	protected void service(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		// 接收传递过来的信息
		String classname = request.getParameter("classname");
		String startcount = request.getParameter("startcount");
		String count = request.getParameter("count");
		String argm = "non>="+startcount+" and non<="+count;
		if(null==classname||"".equals(classname)){
			return;
		}else{
			try {
				InputStream inputStream=getServletContext().getResourceAsStream("/WEB-INF/wbxtprj.properties");
				Properties properties=new Properties();
				properties.load(inputStream);
				String dataSource=(String)properties.get("dataSource");
				String ips=(String)properties.get("authip");
				String ip=getIpAddr(request);
				if(ips.indexOf("["+ip+"]")>=0){
//					Class c = Class.forName(classname);
//					Class ptypes[] = new Class[0];   
//					Method m = c.getMethod("getTableName",ptypes);
//						Object obj = (Object)c.newInstance();
//							String tablename = m.invoke(obj).toString();
//					IUAPQueryBS ibs=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
					List<Map> list = null;
					String sql="select * from (select rownum non,"+classname+".* from "+classname+") where  "+argm;
					//一次获取全部数据
					 list = (List<Map>)new BaseDAO(dataSource).executeQuery(sql, new MapListProcessor());
					ObjectOutputStream oos = new ObjectOutputStream(response
								.getOutputStream());
					oos.writeObject(list);
					oos.flush();
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

	}
	

}
