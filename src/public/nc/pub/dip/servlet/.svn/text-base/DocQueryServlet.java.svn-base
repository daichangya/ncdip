package nc.pub.dip.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.dao.BaseDAO;
import nc.bs.logging.Logger;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.util.dip.sj.FunctionUtilWithPri;
import nc.util.dip.sj.RetMessage;
import nc.vo.dip.authorize.define.DipADContdataVO;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.dip.datalook.DipAuthDesignVO;
import nc.vo.sm.UserVO;

import org.json.JSONArray;
import org.json.JSONObject;

public class DocQueryServlet extends HttpServlet {

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
				String username = (String)jsonObject.get("username");
				BaseDAO dao = new BaseDAO();
				Collection usercol = dao.retrieveByClause(UserVO.class, "upper(user_code)='"+username.toUpperCase()+"'");
				if(null != usercol && usercol.size()>0){
					UserVO[] userVos = (UserVO[])usercol.toArray(new UserVO[usercol.size()]);
					Collection col = dao.retrieveByClause(DipADContdataVO.class,
							"code='" + doctypecode + "' and coalesce(dr,0)=0 ");
					if (null != col && col.size() > 0) {
						DipADContdataVO[] vos = (DipADContdataVO[]) col.toArray(new DipADContdataVO[col.size()]);
						String contabcode = vos[0].getContabcode();
						DipDatadefinitHVO hvo = (DipDatadefinitHVO) dao.retrieveByPK(DipDatadefinitHVO.class, contabcode);
						if (null != hvo) {
							JSONArray jsonArray = (JSONArray)jsonObject.get("condition");
//							String whereSql = "   and coalesce(dr,0)=0 and rownum < 101";
							String whereSql = "   and coalesce(dr,0)=0 and rownum < 101";
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject object = (JSONObject)jsonArray.get(i);
								String filedcode = (String)object.get("fieldcode");
								String fieldvalue = (String)object.get("fieldvalue");
								whereSql += " and "+filedcode+" like '%"+fieldvalue+"%' ";
							}
							Collection queryCol = dao.retrieveByClause(DipAuthDesignVO.class, "pk_datadefinit_h = '"
									+vos[0].getPrimaryKey()
									+"' and designtype = 5 and coalesce(dr, 0) = 0 and consvalue is not null and consvalue<>'SYS.REF' ");
							final FunctionUtilWithPri functionUtilWithPri = new FunctionUtilWithPri();
							functionUtilWithPri.setUsercode(username.toUpperCase());
							if(null != queryCol && queryCol.size()>0){
								DipAuthDesignVO[] queryVos = (DipAuthDesignVO[])queryCol.toArray(new DipAuthDesignVO[queryCol.size()]);
								for (DipAuthDesignVO vo : queryVos) {
									String filedcode = vo.getEname();
									String fieldvalue = functionUtilWithPri.getFuctionValue(vo.getConsvalue());
									whereSql += " and "+filedcode+"='"+fieldvalue+"' ";
								}
							}
							whereSql += " order by ts desc";
							String defwhere="exists "
								+" (select 1 "
								+"  from "+vos[0].getMiddletab()+" ss "
								+" where ss.contpk = "+vos[0].getContabname()+"."+vos[0].getContcolname()
								+" and ss.extepk in "
								+"  (select pk_role_corp_alloc "
								+"   from v_dip_corproleauth "
								+" where pk_role in "
								+"   (select pk_role "
								+"    from sm_user_role cc "
								+"   where cc.cuserid = '"+userVos[0].getPrimaryKey()+"'))) ";
							Collection retrieveByClause = dao.retrieveByClause(DipADContdataVO.class," pk_master='"+vos[0].getPk_contdata_h()+"' and coalesce(dr,0)=0 ");
							if(null != retrieveByClause && retrieveByClause.size()>0){
								DipADContdataVO[] array = (DipADContdataVO[])retrieveByClause.toArray(new DipADContdataVO[retrieveByClause.size()]);
								for (DipADContdataVO dipADContdataVO : array) {
									defwhere+=" and exists "
										+" (select 1 "
										+"  from "+dipADContdataVO.getMiddletab()+" ss "
										+" where ss.contpk = "+dipADContdataVO.getContabname()+"."+dipADContdataVO.getContcolname()
										+" and ss.extepk in "
										+"  (select pk_role_corp_alloc "
										+"   from v_dip_corproleauth "
										+" where pk_role in "
										+"   (select pk_role "
										+"    from sm_user_role cc "
										+"   where cc.cuserid = '"+userVos[0].getPrimaryKey()+"'))) ";
								}
							}
							String sql = "SELECT  ename FROM dip_auth_design WHERE pk_datadefinit_h = ? and designtype = 4 and coalesce(dr, 0) = 0 ";
							SQLParameter parameter = new SQLParameter();
							parameter.addParam(vos[0].getPrimaryKey());
							ArrayList<String> fieldList = (ArrayList<String>)dao.executeQuery(sql,parameter, new ResultSetProcessor() {
								public ArrayList<String> handleResultSet(ResultSet rs) throws SQLException {
									ArrayList<String> fieldList = new ArrayList<String>();
									while(rs.next()){
										fieldList.add(rs.getString(1));
									}
									return fieldList;
								}
							});
							if(fieldList.size()>0){
								String fields = "";
								for (int i = 0; i < fieldList.size(); i++) {
									fields += fieldList.get(i)+",";
								}
								fields = fields.substring(0, fields.length()-1);
								String querySql = "select "+fields+" from "+hvo.getMemorytable()+" where "+defwhere+whereSql;
								Collection designCol = dao.retrieveByClause(
										DipAuthDesignVO.class,
										"coalesce(dr,0)=0 and pk_datadefinit_h = '"
												+ vos[0].getPrimaryKey()
												+ "' and consvalue is not null and designtype=4 ");
								HashMap<String,String> showFunctionMap=new HashMap<String,String>();
								HashMap<String,String> showAutoInMap=new HashMap<String,String>();
								if (null != designCol && designCol.size() > 0) {
									DipAuthDesignVO[] array = (DipAuthDesignVO[]) designCol
											.toArray(new DipAuthDesignVO[designCol
													.size()]);
									for (DipAuthDesignVO dipAuthDesignVO : array) {
										if(FunctionUtilWithPri.isAutoIn(dipAuthDesignVO.getConsvalue())){
											showAutoInMap.put(dipAuthDesignVO.getEname(), dipAuthDesignVO.getConsvalue());
										}else{
											showFunctionMap.put(dipAuthDesignVO.getEname(), dipAuthDesignVO.getConsvalue());
										}
									}
								}
								final HashMap<String,String> finalShowAutoInMap =  showAutoInMap;
								final HashMap<String,String> finalShowFunctionMap =  showFunctionMap;
								final String pkdetadefinith = hvo.getPrimaryKey(); 
								JSONArray array= (JSONArray)dao.executeQuery(querySql, new ResultSetProcessor() {
									public JSONArray handleResultSet(ResultSet rs) throws SQLException {
										JSONArray jsonArray = new JSONArray();
										ResultSetMetaData metaData = rs.getMetaData();
										int columnCount = metaData.getColumnCount();
										HashMap<String, HashMap<String, String>> showReplaceMap = new HashMap<String, HashMap<String, String>>();
										while(rs.next()){
											JSONObject object = new JSONObject();
											for (int i = 1; i < columnCount+1; i++) {
												String columnName = metaData.getColumnName(i);
												String value = rs.getString(columnName);
												String string = finalShowFunctionMap.get(columnName.toUpperCase());
												if(null != string && string.startsWith(FunctionUtilWithPri.SHOWREPLACEPK)){
													int indexOf = string.indexOf("(");
													String substring = string.substring(indexOf+1, string.length()-1);
													String[] split = substring.split(",");
													if(null != split && split.length==2){
														String str = rs.getString(split[0].toUpperCase());
														if(null != str){
															if(null != showReplaceMap.get(columnName)){
																HashMap<String, String> hashMap = showReplaceMap.get(columnName);
																String newStr = hashMap.get(str);
																if(null != newStr){
																	value = newStr;
																}else{
																	RetMessage ret=FunctionUtilWithPri.getShowReplaceFuctionValue(split[0].toUpperCase(),str, 
																			split[1],pkdetadefinith);
																	if(ret.getIssucc()){
																		value=ret.getValue().toString();
																	}
																	hashMap.put(str, value);
																	showReplaceMap.put(columnName, hashMap);
																}
															}else{
																HashMap<String, String> hashMap = new HashMap<String, String>();
																RetMessage ret=FunctionUtilWithPri.getShowReplaceFuctionValue(split[0].toUpperCase(),str, 
																		split[1],pkdetadefinith);
																if(ret.getIssucc()){
																	value=ret.getValue().toString();
																}
																hashMap.put(str, value);
																showReplaceMap.put(columnName, hashMap);
															}
														}
													}
												}
												string = finalShowAutoInMap.get(columnName.toUpperCase());
												if(null != string){
													value = functionUtilWithPri.getFuctionValue(string);
												}
												object.put(columnName, value==null?"":value);
											}
											jsonArray.put(object);
										}
										return jsonArray;
									}
								});
								output.put("success", "Y");
								output.put("msg", "查询成功");
								output.put("result", array);
							}else{
								output.put("success", "N");
								output.put("msg", "找不到档案的显示模板");
							}
						} else {
							output.put("success", "N");
							output.put("msg", "找不到对应档案表");
						}
					} else {
						output.put("success", "N");
						output.put("msg", "通过doctypecode找不到对应档案表");
					}
				}else {
					output.put("success", "N");
					output.put("msg", "通过username找不到对应用户");
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
