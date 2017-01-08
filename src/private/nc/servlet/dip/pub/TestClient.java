package nc.servlet.dip.pub;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.vo.bd.CorpVO;


public class TestClient {
	private static BaseDAO baseDao;
	private static  BaseDAO getBaseDao(){
    	if (baseDao==null){
    		baseDao=new BaseDAO("test");
    	}
    	return baseDao;
    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		Object obj=null;
		String url = "";
//		String number ="";
		String classname = nc.vo.bd.CorpVO.class.getName();
		try {
//			url="http://127.0.0.1:80/SynCountServlet?table=bd_corp";
//			obj = getDate(url);
//			 if(obj instanceof String){
//				 number = (String) obj;
//			 }
//			 if(null!=number&&!"".equals(number)){
				 url="http://127.0.0.1:80/SynDataServlet";
				 synData(classname,url);
//			 }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	
	}
	
	public void execute(String url){
		
	}
	
	private static HttpURLConnection getConnect(String url)
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
	public static Object getDate(String url) throws MalformedURLException, IOException, ClassNotFoundException{
		Object obj = null;
		HttpURLConnection conn = getConnect(url);
		ObjectOutputStream output = new ObjectOutputStream(conn.getOutputStream());
		output.flush();
		output.close();
		ObjectInputStream input = new ObjectInputStream(conn.getInputStream());
		obj = input.readObject();
		return obj;
	}
	
	
	/**
	 * 获取全部数据并插入数据库
	 * @param classname 类名
	 * @param url servlet地址
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws DAOException 
	 */
	public static void synData(String classname,String url) throws MalformedURLException, IOException, ClassNotFoundException, DAOException{
		url = url+"?classname="+classname;
		//实现数据初始化
		synOpt(url);	
	}
	
	
	/**
	 * 获取数据,分批处理
	 * @param tableName 表名
	 * @param fields 字段
	 * @param DataNumber 总数
	 * @param size 每次获取多少条数据
	 * @param url servlet地址
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws DAOException 
	 */
	public static void synDataByPage(String tableName,String fields,int DataNumber,int size,String url) throws MalformedURLException, IOException, ClassNotFoundException, DAOException{
		if (DataNumber != 0) {
			// 计算发送次数
			int num = (DataNumber + size - 1) / size;
			// 开始条数
			int startcount = 1;
			// 获取条数
			int count = size;
			tableName = tableName.toUpperCase();
			for (int j = 1; j <= num; j++) {
				Logger.error("\nnum="+num+"\n");
				if (j == 1 && num == 1) {
					url = url+"?table="+tableName+"&fields="+fields+"&startcount="+startcount+"&count="+DataNumber;
					//实现数据初始化
					synOpt(url);
				} else {
					url = url+"?table="+"&fields="+fields+"&startcount="+startcount+"&count="+count;
					//实现数据初始化
					synOpt(url);
					if (j == num - 1) {
						startcount = startcount + size;
						size = DataNumber - count * j;
					} else {
						startcount = startcount + size;
					}
				}
			}
		} else {
			Logger.error("数量为0，没有数据");
		}
	}
	
	/**
	 * 将获得的数据插入到表中
	 * @param url servlet地址
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws DAOException 
	 */
	@SuppressWarnings("unchecked")
	public static void synOpt(String url) throws MalformedURLException, IOException, ClassNotFoundException, DAOException{
		Object obj = getDate(url);
		List list = new ArrayList();
		if(obj instanceof ArrayList){
			list = (List) obj;
			for(int i=0;i<list.size();i++){
				nc.vo.bd.CorpVO vo = (CorpVO) list.get(i);
				System.out.print("=="+vo.pk_corp+"==");
				System.out.print(vo.unitcode+"==");
				System.out.println(vo.unitname+"==");
			}
//			Iterator iteror = list.iterator();
//			while(iteror.hasNext()){
//				System.out.println("==【"+iteror.next()+"】==");
//			}
//			getBaseDao().insertVOList(list);
		}else{
			System.out.println("所得数据不是list");
		}
		
		
		
	}
	
}
