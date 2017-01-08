package nc.bs.dip.plugin;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.pub.pa.IBusinessPlugin;
import nc.bs.pub.pa.IBusinessPlugin2;
import nc.bs.pub.pa.html.IAlertMessage;
import nc.util.dip.sj.IContrastUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.pa.Key;
public class SynAllDataPlugin implements IBusinessPlugin, IBusinessPlugin2 {
	private  BaseDAO baseDao;
	private   BaseDAO getBaseDao(){
    	if (baseDao==null){
    		baseDao=new BaseDAO("test");
    	}
    	return baseDao;
    }
	public int getImplmentsType() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Key[] getKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTypeDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTypeName() {
		// TODO Auto-generated method stub
		return null;
	}

	public IAlertMessage implementReturnFormatMsg(Key[] arg0, String arg1,
			UFDate arg2) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	public String implementReturnMessage(Key[] arg0, String arg1, UFDate arg2)
			throws BusinessException {
		return null;
	}

	public Object implementReturnObject(Key[] arg0, String arg1, UFDate arg2)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean implementWriteFile(Key[] arg0, String arg1, String arg2,
			UFDate arg3) throws BusinessException {
		// TODO Auto-generated method stub
		return false;
	}

	public IAlertMessage[] implementReturnFormatMsg(Key[] arg0, Object arg1,
			UFDate arg2) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 功能：没使用
	 * 
	 * */
	
	public String[] implementReturnMessage(Key[] arg0, Object arg1, UFDate arg2)
			throws BusinessException {
		
		List listValue = (List) new BaseDAO(IContrastUtil.DESIGN_CON).executeQuery("SELECT  DR,TS,DEPTCODE,DEPTNAME FROM BD_DEPTDOC",new nc.jdbc.framework.processor.ArrayListProcessor());
		if (null != listValue && listValue.size() > 0) {
			StringBuffer sql = new StringBuffer();
			sql.append("INSERT INTO ");
			sql.append("BD_DEPTDOC");
			sql.append(" ( ");
			sql.append("DR,TS,DEPTCODE,DEPTNAME ");
			sql.append(" ) ");
			sql.append(" VALUES( ");
			for (int k = 0; k < listValue.size(); k++) {
				Object[] s = (Object[])listValue.get(k);
				int objsize = s.length;
//				for(int j=0;j<objsize;j++){
					Integer dr = Integer.parseInt(s[0]==null?"0":s[0].toString());
					String ts = s[1]==null?null:s[1].toString();
					String DEPTCODE = s[2]==null?null:s[2].toString();
					String DEPTNAME = s[3]==null?null:s[3].toString();
					System.out.print(dr+"--"+ts+"--"+DEPTCODE+"--"+DEPTNAME);
//				}
//				nc.vo.bd.b04.DeptdocVO vo = (nc.vo.bd.b04.DeptdocVO) listValue.get(k);
//				 new BaseDAO("test").insertVOWithPK(vo);
//				sql.append();
//				
////				if (k < listValue.size() - 1) {
////					sql.append(",");
////				}
//				sql.append(" ) ");
//				Logger.error(sql);
				
//				//query.exesql(sql.toString());
			}
			
		}
		
//		System.out.println("===========1==========");
////		String classname = nc.vo.bd.CorpVO.class.getName();
//		String classname = nc.vo.bd.b04.DeptdocVO.class.getName();
//		System.out.println("===========2==========");
//		String url = "http://127.0.0.1:80/SynDataServlet?classname="+classname;
//		System.out.println("===========3==========");
//		try {
//			Object obj = getDateq(url);
//			
//			System.out.println("===========4==========");
//			List list = new ArrayList();
//			System.out.println("===========5==========");
//			if(obj instanceof ArrayList){
//				System.out.println("===========6==========");
//				list = (List) obj;
//				System.out.println("===========7==========");
//				int size = list.size();
//				for(int i=0;i<size;i++){
//					//nc.vo.bd.CorpVO vo =(CorpVO) list.get(i);
//					getBaseDao().insertVOWithPK((SuperVO)list.get(i));
//				}
//				
//				System.out.println("===========8==========");
//				System.out.println("数据同步完成\n");
//			}else{
//				System.out.println("所得数据不是list");
//			}
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		/*
		SynDataUtil util = new SynDataUtil();
		try {
			System.out.println("数据同步开始\n");
			//util.resAllData("http://192.168.1.7:80/SynDataServlet", nc.vo.bd.CorpVO.class.getName());
			util.resAllDataByPages("http://192.168.1.7:80", "bd_corp");
			System.out.println("数据同步完成\n");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	return null;
	}
	
	public Object implementReturnObject(Key[] arg0, Object arg1, UFDate arg2)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean implementWriteFile(Key[] arg0, String arg1, Object arg2,
			UFDate arg3) throws BusinessException {
		// TODO Auto-generated method stub
		return false;
	}
	public  Object getDate(String url) throws MalformedURLException, IOException, ClassNotFoundException{
		Object obj = null;
		HttpURLConnection conn = getConnect(url);
		ObjectOutputStream output = new ObjectOutputStream(conn.getOutputStream());
		output.flush();
		output.close();
		ObjectInputStream input = new ObjectInputStream(conn.getInputStream());
		obj = input.readObject();
		return obj;
	}
	
	private  HttpURLConnection getConnect(String url)
    throws MalformedURLException, IOException
  {
    HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
    conn.setDoOutput(true);
    return conn;
  }
	
	/**
	 * 调用servlet获取数据
	 * @param url
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public  Object getDateq(String url) throws MalformedURLException, IOException, ClassNotFoundException{
		Object obj = null;
		HttpURLConnection conn = getConnect(url);
		ObjectOutputStream output = new ObjectOutputStream(conn.getOutputStream());
		output.flush();
		output.close();
		ObjectInputStream input = new ObjectInputStream(conn.getInputStream());
		obj = input.readObject();
		return obj;
	}
}
