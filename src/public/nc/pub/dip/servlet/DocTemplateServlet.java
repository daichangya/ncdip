package nc.pub.dip.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.dao.BaseDAO;
import nc.bs.logging.Logger;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.util.dip.sj.FunctionUtilWithPri;
import nc.vo.dip.actionset.ActionSetHVO;
import nc.vo.dip.authorize.define.DipADContdataVO;

import org.json.JSONArray;
import org.json.JSONObject;

public class DocTemplateServlet extends HttpServlet {

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
				String username = (String) jsonObject.get("username");
				BaseDAO dao = new BaseDAO();
				Collection col = dao.retrieveByClause(DipADContdataVO.class,
						"code='" + doctypecode + "' and coalesce(dr,0)=0 ");
				if (null != col && col.size() > 0) {
					DipADContdataVO[] vos = (DipADContdataVO[])col.toArray(new DipADContdataVO[col.size()]);
					String contabcode = vos[0].getContabcode();
					String queryRefSql = "SELECT a.ename,b.memorytable FROM dip_datadefinit_b a "
							+"inner join v_dip_jgyyzdtree b on a.quotetable=b.pk_datadefinit_b "
							+"WHERE a.pk_datadefinit_h=? and a.quotetable is not null and coalesce(a.dr,0)=0 ";
					SQLParameter sqlParameter = new SQLParameter();
					sqlParameter.addParam(contabcode);
					final HashMap<String, String> refMap = (HashMap<String, String>)dao.executeQuery(queryRefSql, sqlParameter, new ResultSetProcessor() {
						public HashMap<String, String> handleResultSet(ResultSet rs) throws SQLException {
							HashMap<String, String> refMap = new HashMap<String, String>();
							while(rs.next()){
								refMap.put(rs.getString(1), rs.getString(2));
							}
							return refMap;
						}
					});
					
					String primaryKey = vos[0].getPrimaryKey();
					String sql = 
						"SELECT  b.ename          fieldcode,\n" +
						"                b.cname          fieldname,\n" + 
						"                b.datatype       fieldtype,\n" + 
						"                b.ispk,\n" + 
						"                b.designlen      showflag,\n" + 
						"                b.is_single_list is_single_listtemp,\n" + 
						"                b.is_list        is_listtemp,\n" + 
						"                coalesce(b.desigplen,0)      digits ,\n" + 
						"                b.disno,\n" + 
						"                b.consvalue,\n" +
						"                b.islock\n" +
						"  FROM (SELECT *\n" + 
						"               FROM dip_auth_design\n" + 
						"              WHERE pk_datadefinit_h = ?\n" + 
						"                and designtype = 4\n" + 
						"                and coalesce(dr, 0) = 0) b\n" + 
						"order by b.disno";
					SQLParameter parameter = new SQLParameter();
					parameter.addParam(primaryKey);
					JSONArray jsonArray = (JSONArray)dao.executeQuery(sql,parameter, new ResultSetProcessor() {
						public JSONArray handleResultSet(ResultSet rs) throws SQLException {
							HashMap<String, String> map = new HashMap<String, String>();
							JSONArray jsonArray = new JSONArray();
							while(rs.next()){
								LinkedHashMap<String, String> linkedMap = new LinkedHashMap<String, String>();
								String fieldcode = rs.getString("fieldcode");
								if(map.get(fieldcode)!=null){
									continue;
								}
								map.put(fieldcode, fieldcode);
								linkedMap.put("fieldcode", fieldcode);
								linkedMap.put("fieldname", rs.getString("fieldname"));
								String fieldtype = rs.getString("fieldtype");
								String memorytable = refMap.get(fieldcode);
								String consvalue = rs.getString("consvalue");
								if(null != consvalue && FunctionUtilWithPri.SYSREFPK.equals(consvalue)){
									if(fieldtype.toUpperCase().indexOf("DATE")!=-1){
										fieldtype = "date";
									}else if("ZMDM_OCALYEAR".equals(memorytable)){
										fieldtype = "year";
									}else if("ZMDM_OCALMONTH2".equals(memorytable)){
										fieldtype = "month";
									}else if("ZMDM_OCALDAY".equals(memorytable)){
										fieldtype = "date_ref";
									}else{
										fieldtype = "ref";
									}
								}else{
									if(",BINARY_DOUBLE,BINARY_FLOAT,INTEGER,INTERVAL,LONG,LONGRAW,NUMBER,".indexOf(fieldtype.toUpperCase())>=0){
										fieldtype = "number";
									}else{
										fieldtype = "text";
									}
								}
								linkedMap.put("fieldtype", fieldtype);
								linkedMap.put("ispk", rs.getString("ispk")==null?"N":rs.getString("ispk"));
								
								int double1 = rs.getInt("showflag");
								if(double1>0){
									linkedMap.put("showflag", "Y");
								}else{
									linkedMap.put("showflag", "N");
								}
								String string = rs.getString("is_single_listtemp")==null?"N":rs.getString("is_single_listtemp");
								linkedMap.put("is_single_listtemp", string);
								linkedMap.put("is_listtemp", rs.getString("is_listtemp")==null?"N":rs.getString("is_listtemp"));
								
//								if(double1>0){
									linkedMap.put("is_addtemp", "Y");
//								}else{
//									linkedMap.put("is_addtemp", "N");
//								}
								
//								if(double1>0){
									linkedMap.put("is_edittemp", "Y");
//								}else{
//									linkedMap.put("is_edittemp", "N");
//								}
								
								linkedMap.put("maxlength",double1+"");
								linkedMap.put("digits",rs.getInt("digits")+"");
								String islock = rs.getString("islock");
								if(null != islock && "Y".equals(islock)){
									linkedMap.put("is_editfield","N");
								}else{
									linkedMap.put("is_editfield","Y");
								}
								jsonArray.put(new JSONObject(linkedMap));
							}
							return jsonArray;
						}
					});
					
					output.put("success", "Y");
					output.put("result", jsonArray);
					
					sql = 
						"SELECT  b.ename          fieldcode,\n" +
						"                b.cname          fieldname,\n" + 
						"                b.datatype       fieldtype,\n" + 
						"                b.ispk,\n" + 
						"                b.designlen      showflag,\n" + 
						"                coalesce(c.designlen,0)      is_qrytemp,\n" + 
						"                b.is_single_list is_single_listtemp,\n" + 
						"                b.is_list        is_listtemp,\n" + 
						"                coalesce(b.desigplen,0)      digits ,\n" + 
						"                b.disno,\n" + 
						"                b.consvalue,\n" +
						"                b.islock\n" +
						"  FROM (SELECT *\n" + 
						"               FROM dip_auth_design\n" + 
						"              WHERE pk_datadefinit_h = ?\n" + 
						"                and designtype = 5\n" + 
						"                and coalesce(dr, 0) = 0 and (consvalue is null or consvalue='SYS.REF') ) c inner join (SELECT *\n" + 
						"               FROM dip_auth_design\n" + 
						"              WHERE pk_datadefinit_h = ?\n" + 
						"                and designtype = 4\n" + 
						"                and coalesce(dr, 0) = 0) b\n" + 
						"    on c.ename = b.ename\n" + 
						"order by c.disno";
					parameter = new SQLParameter();
					parameter.addParam(primaryKey);
					parameter.addParam(primaryKey);
					JSONArray qryJsonArray = (JSONArray)dao.executeQuery(sql,parameter, new ResultSetProcessor() {
						public JSONArray handleResultSet(ResultSet rs) throws SQLException {
							HashMap<String, String> map = new HashMap<String, String>();
							JSONArray jsonArray = new JSONArray();
							while(rs.next()){
								LinkedHashMap<String, String> linkedMap = new LinkedHashMap<String, String>();
								String fieldcode = rs.getString("fieldcode");
								if(map.get(fieldcode)!=null){
									continue;
								}
								map.put(fieldcode, fieldcode);
								linkedMap.put("fieldcode", fieldcode);
								linkedMap.put("fieldname", rs.getString("fieldname"));
								String fieldtype = rs.getString("fieldtype");
								String memorytable = refMap.get(fieldcode);
								String consvalue = rs.getString("consvalue");
								if(null != consvalue && FunctionUtilWithPri.SYSREFPK.equals(consvalue)){
									if(fieldtype.toUpperCase().indexOf("DATE")!=-1){
										fieldtype = "date";
									}else if("ZMDM_OCALYEAR".equals(memorytable)){
										fieldtype = "year";
									}else if("ZMDM_OCALMONTH2".equals(memorytable)){
										fieldtype = "month";
									}else if("ZMDM_OCALDAY".equals(memorytable)){
										fieldtype = "date_ref";
									}else{
										fieldtype = "ref";
									}
								}else{
									if(",BINARY_DOUBLE,BINARY_FLOAT,INTEGER,INTERVAL,LONG,LONGRAW,NUMBER,".indexOf(fieldtype.toUpperCase())>=0){
										fieldtype = "number";
									}else{
										fieldtype = "text";
									}
								}
								linkedMap.put("fieldtype", fieldtype);
								linkedMap.put("ispk", rs.getString("ispk")==null?"N":rs.getString("ispk"));
								
								int double1 = rs.getInt("showflag");
								if(double1>0){
									linkedMap.put("showflag", "Y");
								}else{
									linkedMap.put("showflag", "N");
								}
								
								int double2 = rs.getInt("is_qrytemp");
								if(double2>0){
									linkedMap.put("is_qrytemp", "Y");
								}else{
									linkedMap.put("is_qrytemp", "N");
								}
								
								String string = rs.getString("is_single_listtemp")==null?"N":rs.getString("is_single_listtemp");
								linkedMap.put("is_single_listtemp", string);
								linkedMap.put("is_listtemp", rs.getString("is_listtemp")==null?"N":rs.getString("is_listtemp"));
								
//								if(double1>0){
									linkedMap.put("is_addtemp", "Y");
//								}else{
//									linkedMap.put("is_addtemp", "N");
//								}
								
//								if(double1>0){
									linkedMap.put("is_edittemp", "Y");
//								}else{
//									linkedMap.put("is_edittemp", "N");
//								}
								
								linkedMap.put("maxlength",double1+"");
								linkedMap.put("digits",rs.getInt("digits")+"");
								String islock = rs.getString("islock");
								if(null != islock && "Y".equals(islock)){
									linkedMap.put("is_editfield","N");
								}else{
									linkedMap.put("is_editfield","Y");
								}
								jsonArray.put(new JSONObject(linkedMap));
							}
							return jsonArray;
						}
					});
					output.put("qryresult", qryJsonArray);
					Collection collection = dao.retrieveByClause(ActionSetHVO.class, "pk_role_group in("
							+"SELECT pk_role_group FROM dip_rolegroup_b WHERE pk_role in("
							+"SELECT pk_role FROM sm_user_role WHERE cuserid in(select cuserid from sm_user where upper(user_code)='"
							+username.toUpperCase()
							+"')) and coalesce(dr,0)=0) and pk_contdata_h='"
							+primaryKey
							+"' and coalesce(dr,0)=0");
					JSONObject jsonObject2 = new JSONObject();
					if(null != collection && collection.size()>0){
						ActionSetHVO[] groupVOs = (ActionSetHVO[])collection.toArray(new ActionSetHVO[collection.size()]);
						Boolean is_add = false;
						Boolean is_delete = false;
						Boolean is_edit = false;
						Boolean is_query = false;
						for (ActionSetHVO vo : groupVOs) {
							if(!is_add && null != vo.getIs_add() && vo.getIs_add().booleanValue()){
								is_add=true;
							}
							if(!is_delete && null != vo.getIs_del() && vo.getIs_del().booleanValue()){
								is_delete=true;
							}
							if(!is_edit && null != vo.getIs_edit() && vo.getIs_edit().booleanValue()){
								is_edit=true;
							}
							if(!is_query && null != vo.getIs_query() && vo.getIs_query().booleanValue()){
								is_query=true;
							}
						}
						if(is_add){
							jsonObject2.put("is_add","Y");
						}
						if(is_delete){
							jsonObject2.put("is_delete","Y");
						}
						if(is_edit){
							jsonObject2.put("is_edit","Y");
						}
						if(is_query){
							jsonObject2.put("is_query","Y");
						}
					}
					output.put("button", jsonObject2);
				} else {
					output.put("success", "N");
					output.put("msg", "通过doctypecode找不到对应档案表");
				}
			} else {
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
