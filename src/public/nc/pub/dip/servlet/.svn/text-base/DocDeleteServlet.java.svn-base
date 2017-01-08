package nc.pub.dip.servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.dao.BaseDAO;
import nc.bs.logging.Logger;
import nc.util.dip.sj.FunctionUtilWithPri;
import nc.vo.dip.authorize.define.DipADContdataVO;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.dip.datalook.DipAuthDesignVO;

import org.json.JSONArray;
import org.json.JSONObject;

public class DocDeleteServlet extends HttpServlet {

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
				Collection col = dao.retrieveByClause(DipADContdataVO.class,
						"code='" + doctypecode + "' and coalesce(dr,0)=0 ");
				if (null != col && col.size() > 0) {
					DipADContdataVO[] vos = (DipADContdataVO[]) col
							.toArray(new DipADContdataVO[col.size()]);
					String contabcode = vos[0].getContabcode();
					Boolean isDr = false;
					DipDatadefinitHVO hvo = (DipDatadefinitHVO) dao
							.retrieveByPK(DipDatadefinitHVO.class, contabcode);
					if (null != hvo) {
						String is_dr = hvo.getIs_dr();
						if (null != is_dr && "Y".equals(is_dr)) {
							isDr = true;
						}
						Collection collection = dao
								.retrieveByClause(
										DipDatadefinitBVO.class,
										"pk_datadefinit_h='"
												+ hvo.getPk_datadefinit_h()
												+ "' and ispk='Y' and coalesce(dr,0)=0");
						if (null != collection && collection.size() > 0) {
							DipDatadefinitBVO[] bvos = (DipDatadefinitBVO[]) collection
									.toArray(new DipDatadefinitBVO[collection
											.size()]);
							String pkfield = bvos[0].getEname();
							JSONArray dataArray = (JSONArray) jsonObject
									.get("data");
							int length = dataArray.length();
							String whereStr = "";
							for (int i = 0; i < length; i++) {
								JSONObject object = (JSONObject) dataArray
										.get(i);
								String docid = (String) object.get("docid");
								whereStr += "'" + docid + "',";
							}
							whereStr = whereStr.substring(0,
									whereStr.length() - 1);
							String sql = "";
							if (isDr) {
								Collection designCol = dao
										.retrieveByClause(
												DipAuthDesignVO.class,
												"coalesce(dr,0)=0 and pk_datadefinit_h = '"
														+ vos[0].getPrimaryKey()
														+ "' and consvalue is not null and designtype=2 ");
								HashMap<String, String> updateAutoInMap = new HashMap<String, String>();
								if (null != designCol && designCol.size() > 0) {
									DipAuthDesignVO[] array = (DipAuthDesignVO[]) designCol
											.toArray(new DipAuthDesignVO[designCol
													.size()]);
									for (DipAuthDesignVO dipAuthDesignVO : array) {
										if (FunctionUtilWithPri
												.isUpdateAutoIn(dipAuthDesignVO
														.getConsvalue())) {
											updateAutoInMap.put(dipAuthDesignVO
													.getEname(),
													dipAuthDesignVO
															.getConsvalue());
										}
									}
								}
								String strFiled = "dr=1,";
								Iterator<String> iterator = updateAutoInMap
										.keySet().iterator();
								FunctionUtilWithPri functionUtilWithPri = new FunctionUtilWithPri();
								functionUtilWithPri.setUsercode(username.toUpperCase());
								while (iterator.hasNext()) {
									String key = iterator.next();
									String value = functionUtilWithPri
											.getFuctionValueWhenUpdate(updateAutoInMap
													.get(key));
									strFiled = strFiled + key + "='" + value
											+ "',";
								}
								strFiled = strFiled.substring(0,
										strFiled.length() - 1);
								sql = "update " + hvo.getMemorytable()
										+ " set " + strFiled + " where "
										+ pkfield + " in(" + whereStr + ")";
							} else {
								sql = "delete from " + hvo.getMemorytable()
										+ " where " + pkfield + " in("
										+ whereStr + ")";
							}
							dao.executeUpdate(sql);
							output.put("success", "Y");
						} else {
							output.put("success", "N");
							output.put("msg", "找不到对应档案表主键");
						}
					} else {
						output.put("success", "N");
						output.put("msg", "找不到对应档案表");
					}
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
