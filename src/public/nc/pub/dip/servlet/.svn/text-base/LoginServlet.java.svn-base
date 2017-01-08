package nc.pub.dip.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.dao.BaseDAO;
import nc.bs.logging.Logger;
import nc.bs.uap.sf.facility.SFServiceFacility;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.vo.framework.rsa.Encode;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.ldap.LdapHelper;
import nc.vo.sm.UserVO;

import org.json.JSONObject;

public class LoginServlet extends HttpServlet {

	private Encode coder = null;
	
	private Encode getCodeTool() {
        if (coder == null)
            coder = new Encode();
        return coder;
    }
	  
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.addHeader("Access-Control-Allow-Origin", "*");
		String str = request.getParameter("data");
		String msg = "";
		// 从请求参数中取值
//		String data = URLDecoder.decode(str.toString(), "UTF-8");
		String data = new String(str.toString().getBytes("iso-8859-1"),"UTF-8") ;
		JSONObject output = new JSONObject();
		try {
			if (data != null && !"".equals(data)) {
				JSONObject jsonObject = new JSONObject(data);
				String username = (String) jsonObject.get("username");
				String password = (String) jsonObject.get("password");
				msg = validateUser(username, password);
				if (null == msg) {
					output.put("success", "Y");
				} else {
					output.put("success", "N");
					output.put("msg", msg);
				}
			}else{
				output.put("success", "N");
				output.put("msg", "数据为空");
			}
		} catch (Exception e) {
			Logger.error(e.getMessage());
			e.printStackTrace();
			output.put("success", "N");
			output.put("msg", e.getMessage());
		}
		response.getWriter().write(output.toString());
	}

	private String validateUser(String username, String password) throws Exception {
		UserVO user = getUser("1001", username, "ncdip");
		// 用户被锁定：
		if (user == null) {
			return "用户不存在";
		}
		if (user.getLocked().booleanValue()) {
			return "用户已锁定";
		}
		SQLParameter parameter = new SQLParameter();
		parameter.addParam(user.getPrimaryKey());
		Boolean isAdmin = (Boolean) new BaseDAO()
				.executeQuery(
						"select 1 from sm_user_role where cuserid=? and pk_role='COMPANYADMINISTRATOR' and coalesce(dr,0)=0 ",
						parameter, new ResultSetProcessor() {
							public Boolean handleResultSet(ResultSet rs)
									throws SQLException {
								if (rs.next()) {
									return true;
								}
								return false;
							}
						});
		if (!isAdmin) {
			 boolean ldapauth = LdapHelper.authenticate(username,password);
			 if (!ldapauth) {
				 return "用户不存在";
			 }
			 if(!"true".equals(LdapHelper.getLdapXml().getIsauth())){//不启用Ldap，需要校验nc密码
				 String decode = getCodeTool().decode(user.getUserPassword());
				 if(!decode.equals(password)){
					 return "用户密码错误";
				 }
			 }
		}
		UFDate today = SFServiceFacility.getServiceProviderService()
					.getServerTime().getDate();

		// 账号尚未到生效期
		if (user.getAbleDate() != null) {
			if (today.before(user.getAbleDate())) {
				return "账号尚未到生效期";
			}
		}
		// 账号已过有效期
		if (user.getDisableDate() != null) {
			if (today.after(user.getDisableDate().getDateBefore(1))) {
				return "账号已过有效期";
			}
		}
		return null;
	}

	public UserVO getUser(String pk_corp, String usercode, String dbName) {
		UserVO user = null;
		try {
			user = SFServiceFacility.getUserQueryService()
					.findUserWithDataSource(pk_corp, usercode, dbName);
		} catch (Exception e) {
			Logger.error("Error", e);
		}
		return user;
	}
}
