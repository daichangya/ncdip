package nc.pub.dip.servlet;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.dao.BaseDAO;
import nc.bs.logging.Logger;
import nc.vo.dip.authorize.define.DipADContdataVO;

import org.json.JSONArray;
import org.json.JSONObject;

public class DocListServlet extends HttpServlet {

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
				String username = (String) jsonObject.get("username");
				BaseDAO dao = new BaseDAO();
				Collection col = dao.retrieveByClause(DipADContdataVO.class, "pk_contdata_h in("
						+"SELECT pk_contdata_h FROM dip_adcontdata_b WHERE pk_fp in(SELECT pk_role_group FROM dip_rolegroup_b "
						+"WHERE pk_role in(SELECT pk_role FROM sm_user_role WHERE cuserid in(select cuserid from sm_user where upper(user_code)='"
						+username.toUpperCase()+"'))) and is_master='Y' ) and ismobile='Y'  order by code ");
				output.put("success", "Y");
				output.put("msg", "查询成功");
				if(null != col && col.size()>0){
					JSONArray jsonArray = new JSONArray();
					DipADContdataVO[] vos = (DipADContdataVO[])col.toArray(new DipADContdataVO[col.size()]);
					for (DipADContdataVO vo : vos) {
						JSONObject object = new JSONObject();
						object.put("doctypecode", vo.getCode());
						object.put("doctypename", vo.getCode()+vo.getName());
						String pk_sysregister_h = vo.getPk_sysregister_h();
						DipADContdataVO dipADContdataVO = (DipADContdataVO)dao.retrieveByPK(DipADContdataVO.class, pk_sysregister_h);
						String docsupername = vo.getContsys();
						if(null != dipADContdataVO){
							docsupername = vo.getContsys()+"->"+dipADContdataVO.getName();
						}
						object.put("docsupername", docsupername);
						jsonArray.put(object);
					}
					output.put("result", jsonArray);
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
