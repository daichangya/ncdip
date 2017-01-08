package nc.pub.dip.servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.dip.pub.IQueryField;
import nc.util.dip.sj.DataCheckUtilWithPri;
import nc.util.dip.sj.FunctionUtil;
import nc.util.dip.sj.FunctionUtilWithPri;
import nc.util.dip.sj.RetMessage;
import nc.vo.dip.authorize.define.DipADContdataVO;
import nc.vo.dip.datadefcheck.DipDatadefinitCVO;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.dip.datalook.DipAuthDesignVO;
import org.json.JSONObject;

public class DocSaveServlet extends HttpServlet {

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
				JSONObject docData = (JSONObject) jsonObject.get("data");
				if(null != docData && docData.length()>0){
					BaseDAO dao = new BaseDAO();
					Collection col = dao.retrieveByClause(DipADContdataVO.class,"code='"+doctypecode+"' and coalesce(dr,0)=0 ");
					if(null != col && col.size()>0){
						DipADContdataVO[] vos = (DipADContdataVO[])col.toArray(new DipADContdataVO[col.size()]);
						String contabcode = vos[0].getContabcode();
						DipDatadefinitHVO hvo = (DipDatadefinitHVO)dao.retrieveByPK(DipDatadefinitHVO.class, contabcode);
						if(null != hvo){
							Collection collection = dao.retrieveByClause(DipDatadefinitBVO.class,"pk_datadefinit_h='"
											+ hvo.getPk_datadefinit_h()+ "' and ispk='Y' and coalesce(dr,0)=0");
							if (null != collection && collection.size() > 0) {
								DipDatadefinitBVO[] bvos = (DipDatadefinitBVO[]) collection.toArray(new DipDatadefinitBVO[collection.size()]);
								String pkfield = bvos[0].getEname();
								
								Collection col2 = dao.retrieveByClause(DipAuthDesignVO.class,"pk_datadefinit_h = '"
										+vos[0].getPrimaryKey()
										+"' and designtype = 4 and coalesce(dr, 0) = 0 ");
								if(null != col2 && col2.size()>0){
									StringBuffer insertField = new StringBuffer();
									StringBuffer insertFieldvalue = new StringBuffer(); 
									IQueryField queryfield = (IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName());
									HashMap<String, DipDatadefinitCVO[]> checkMap = DataCheckUtilWithPri.getDataCheckMap(hvo.getPrimaryKey());
									DataCheckUtilWithPri dataCheckUtilWithPri = new DataCheckUtilWithPri();
									dataCheckUtilWithPri.setUsercode(username.toUpperCase());
									DipAuthDesignVO[] designVos = (DipAuthDesignVO[])col2.toArray(new DipAuthDesignVO[col2.size()]);
									String errorMsg = "";
									for (int i = 0; i < designVos.length; i++) {
										String fieldcode = designVos[i].getEname();
										if(!fieldcode.toUpperCase().equals(pkfield.toUpperCase())){
											if(null !=checkMap.get(fieldcode)){
												String name = designVos[i].getCname();
												String message = name+"";
												String string = docData.getString(fieldcode);
												String checkDataMsg = dataCheckUtilWithPri.checkData(fieldcode, string,vos[0].getMiddletab());
												if(null != checkDataMsg){
													errorMsg+= message+"  "+checkDataMsg;
												}
											}
										}
									}
									if("".equals(errorMsg)){
										JSONObject returnObject = new JSONObject();
										Collection designCol = dao.retrieveByClause(DipAuthDesignVO.class,
												"coalesce(dr,0)=0 and pk_datadefinit_h = '"
														+ vos[0].getPrimaryKey()
														+ "' and consvalue is not null and designtype=2 ");
										HashMap<String,String> autoInMap=new HashMap<String,String>();
										HashMap<String, String> functionMap = new HashMap<String, String>();
										if (null != designCol && designCol.size() > 0) {
											DipAuthDesignVO[] array = (DipAuthDesignVO[]) designCol.toArray(new DipAuthDesignVO[designCol.size()]);
											for (DipAuthDesignVO dipAuthDesignVO : array) {
												if(FunctionUtil.isAutoIn(dipAuthDesignVO.getConsvalue())){
													autoInMap.put(dipAuthDesignVO.getEname(), dipAuthDesignVO.getConsvalue());
												}else if(FunctionUtil.isUpdateAutoIn(dipAuthDesignVO.getConsvalue())){
													
												}else{
													functionMap.put(dipAuthDesignVO.getEname(), dipAuthDesignVO.getConsvalue());
												}
											}
										}
										FunctionUtilWithPri functionUtilWithPri = new FunctionUtilWithPri();
										functionUtilWithPri.setUsercode(username.toUpperCase());
										for (int i = 0; i < designVos.length; i++) {
											String fieldcode = designVos[i].getEname();
											insertField.append(fieldcode+",");
											String value = "";
											if(fieldcode.toUpperCase().equals(pkfield.toUpperCase())){
												value = queryfield.getOID();
											}else{
												value = docData.getString(fieldcode);
												if(null != autoInMap.get(fieldcode)){
													value = functionUtilWithPri.getFuctionValue(autoInMap.get(fieldcode));
												}
												String function = functionMap.get(fieldcode);
												if(null != function && !function.startsWith(FunctionUtil.SAVEREPLACEPK) && !function.startsWith(FunctionUtil.SHOWREPLACEPK)){
													RetMessage ret = FunctionUtilWithPri.getFuctionValue(value, functionMap.get(fieldcode));
													if(ret.getIssucc()){
														value=ret.getValue().toString();
													}
												}else if(null != function && function.startsWith(FunctionUtil.SAVEREPLACEPK)){
													int indexOf = function.indexOf("(");
													String substring = function.substring(indexOf+1, function.length()-1);
													String[] split = substring.split(",");
													if(null != split && split.length==2){
														String fieldvalue = docData.getString(split[0].toUpperCase());
														if("".equals(fieldvalue) && null != autoInMap.get(split[0].toUpperCase())){
															fieldvalue = functionUtilWithPri.getFuctionValue(autoInMap.get(split[0].toUpperCase()));
														}
														RetMessage ret=FunctionUtilWithPri.getSaveReplaceFuctionValue(split[0].toUpperCase(),fieldvalue, 
																split[1],hvo.getPk_datadefinit_h());
														if(ret.getIssucc()){
															value=ret.getValue().toString();
														}
													}
												}
											}
											returnObject.put(fieldcode, value);
											insertFieldvalue.append("'"+value+"',");
										}
										String field = insertField.toString().substring(0,insertField.toString().length()-1);
										String fieldvalue = insertFieldvalue.toString().substring(0,insertFieldvalue.toString().length()-1);
										String insertsql = "insert into "+hvo.getMemorytable()+"("+field+") " +" values("+fieldvalue+")";
										dao.executeUpdate(insertsql);
										output.put("success", "Y");
										output.put("msg", "保存成功");
										output.put("data",returnObject);
									}else{
										output.put("success", "N");
										output.put("msg", errorMsg);
									}
								}else{
									output.put("success", "N");
									output.put("msg", "找不到档案的显示模板");
								}
							}else{
								output.put("success", "N");
								output.put("msg", "找不到对应档案表主键");
							}
						}else{
							output.put("success", "N");
							output.put("msg", "找不到对应档案表");
						}
					}else{
						output.put("success", "N");
						output.put("msg", "通过doctypecode找不到对应档案表");
					}
				}else{
					output.put("success", "N");
					output.put("msg", "data数据为空");
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
