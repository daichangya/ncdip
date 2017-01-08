package nc.pub.dip.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.dao.BaseDAO;
import nc.bs.logging.Logger;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.vo.dip.authorize.define.DipADContdataVO;
import nc.vo.dip.datalook.DipAuthDesignVO;

import org.json.JSONArray;
import org.json.JSONObject;

public class DocRefListServlet extends HttpServlet {

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.addHeader("Access-Control-Allow-Origin", "*");
		String str = request.getParameter("data");
		// 从请求参数中取值
//		String data = URLDecoder.decode(str.toString(), "UTF-8");
		String data = new String(str.toString().getBytes("iso-8859-1"),"UTF-8") ;
		JSONObject output = new JSONObject();
		try {
			if (data != null && !"".equals(data)) {
				JSONObject jsonObject = new JSONObject(data);
				String doctypecode = (String) jsonObject.get("doctypecode");
				String filedcode = (String) jsonObject.get("fieldcode");
				String searchtext = (String) jsonObject.get("searchtext");
				BaseDAO dao = new BaseDAO();
				String sql = "SELECT memorytable FROM v_dip_jgyyzdtree WHERE pk_datadefinit_b in("
					+"SELECT quotetable FROM dip_datadefinit_b WHERE pk_datadefinit_h in( "
					+"SELECT pk_datadefinit_h FROM dip_adcontdata WHERE code=? and " 
					+"coalesce(dr,0)=0) and ename=? and coalesce(dr,0)=0)";
				SQLParameter parameter = new SQLParameter();
				parameter.addParam(doctypecode);
				parameter.addParam(filedcode);
				String memorytable = (String)dao.executeQuery(sql, parameter, new ResultSetProcessor() {
					public Object handleResultSet(ResultSet rs) throws SQLException {
						while(rs.next()){
							return rs.getString(1);
						}
						return null;
					}
				});
				Collection col = dao.retrieveByClause(DipADContdataVO.class,"code='"+doctypecode+"' and coalesce(dr,0)=0 ");
				if(null != col && col.size()>0){
					DipADContdataVO[] vos = (DipADContdataVO[])col.toArray(new DipADContdataVO[col.size()]);
					Collection designCol = dao.retrieveByClause(DipAuthDesignVO.class,
							"coalesce(dr,0)=0 and pk_datadefinit_h = '"
									+ vos[0].getPrimaryKey()
									+ "' and consvalue like 'SYS.SAVEREPLACE%' and consvalue like '%"+filedcode+"%' and designtype=2 ");
					if (null != designCol && designCol.size() > 0) {
						DipAuthDesignVO[] array = (DipAuthDesignVO[]) designCol.toArray(new DipAuthDesignVO[designCol.size()]);
						for (DipAuthDesignVO dipAuthDesignVO : array) {
							String ename = dipAuthDesignVO.getEname();
							output.put("code", filedcode);
							output.put("name", ename);
						}
					}
				}
				if(null != memorytable){
					String refSql = "select C_CODE,C_NAME from  "+memorytable+" where (c_code like '%"+searchtext+"%' or c_name like '%"+searchtext+"%') and rownum < 101";
					JSONArray jsonArray= (JSONArray)dao.executeQuery(refSql, new ResultSetProcessor() {
						public Object handleResultSet(ResultSet rs) throws SQLException {
							JSONArray jsonArray = new JSONArray();
							while(rs.next()){
								JSONObject object = new JSONObject();
								ResultSetMetaData metaData = rs.getMetaData();
								int columnCount = metaData.getColumnCount();
								for (int i = 1; i < columnCount+1; i++) {
									String columnName = metaData.getColumnName(i);
									String value = rs.getString(columnName);
									object.put(columnName, value);
								}
								jsonArray.put(object);
							}
							return jsonArray;
						}
					});
					output.put("success", "Y");
					output.put("msg", "查询成功");
					output.put("data", jsonArray);
				}else{
					output.put("success", "N");
					output.put("msg", "找不到参照表");
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
}
