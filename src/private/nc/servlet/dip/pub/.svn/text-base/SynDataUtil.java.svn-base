package nc.servlet.dip.pub;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.dip.pub.ILogAndTabMonitorSys;
import nc.itf.dip.pub.IQueryField;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.RetMessage;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.dip.dataformatdef.DataformatdefBVO;
import nc.vo.dip.dataformatdef.DataformatdefHVO;
import nc.vo.dip.datarec.DipDatarecHVO;
import nc.vo.pub.BusinessException;

public class SynDataUtil {
	private static BaseDAO baseDao;

	private static BaseDAO getBaseDao() {
		if (baseDao == null) {
			// 可以加数据源
			baseDao = new BaseDAO(IContrastUtil.DESIGN_CON);
		}
		return baseDao;
	}
	private IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	/**
	 * 获取一次性全部数据并插入到数据库
	 * 
	 * @param ip
	 *            地址(如：127.0.0.1,有端口就带上端口如127.0.0.1:80)
	 * @param classname
	 *            需要操作的vo类名（如：nc.vo.bd.CorpVO.class.getName()）
	 * @throws MalformedURLException
	 * @throws DAOException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
//	public void resAllData(String ip, String classname)
//			throws MalformedURLException, DAOException, IOException,
//			ClassNotFoundException {
//		String url = "";
//		url = ip + "/SynDataServlet?classname=" + classname;
//		// 实现数据初始化
//		synOpt(url);
//	}

	/**
	 * 分批获取数据并插入到数据库
	 * 
	 * @param ip
	 *            地址(如：127.0.0.1,有端口就带上端口如127.0.0.1:80)
	 * @param tableName
	 *            表名
	 * @throws Exception 
	 */
	public RetMessage resAllDataByPages(String hpk,DipDatarecHVO vo,String ip, String tableName,String pk_datadefinh,String datarech)
			throws Exception {
		// 对应的vo类
		String voname = null;
		// 数量定义
		int number = 0;
		// 字段定义
		RetMessage retmessage=getOneDataFormatdefHFields(vo);
		if(!retmessage.getIssucc()){
		return retmessage;	
		}
		String fields = retmessage.getMessage();
//		tableName = tableName.toUpperCase();
		// 从配置文件中读取每次调用的用到的vo类名
		/*
		 * InputStream
		 * inputStream=getServletContext().getResourceAsStream("/WEB-INF/jyprj.properties");
		 * Properties properties=new Properties(); properties.load(inputStream);
		 * String dataSource=(String)properties.get("vo");
		 */
		/*if ("BD_CORP".equals(tableName)) {
			voname = nc.vo.bd.CorpVO.class.getName().toString();
		} else if ("BD_DEPTDOC".equals(tableName)) {
			voname = nc.vo.bd.b04.DeptdocVO.class.getName();
		} else if ("BD_PSNDOC".equals(tableName)) {
			this.resAllDataByPages(ip, "BD_PSNCL");
			this.resAllDataByPages(ip, "BD_PSNBASDOC");
			voname = nc.vo.bd.b06.PsndocVO.class.getName();
		} else if ("BD_PSNBASDOC".equals(tableName)) {
			voname = nc.vo.bd.psndoc.PsnbasdocVO.class.getName();
		} else if ("BD_PSNCL".equals(tableName)) {
			voname = nc.vo.bd.b05.PsnclVO.class.getName();
		} else if ("BD_GLORGBOOK".equals(tableName)) {
			voname=nc.vo.bd.b54.GlorgbookVO.class.getName();
		} else if ("BD_VOUCHERTYPE".equals(tableName)) {
			voname=nc.vo.bd.b18.VouchertypeVO.class.getName();
		} else if ("BD_GLBOOK".equals(tableName)) {
			voname=nc.vo.bd.b52.GlbookVO.class.getName();
		} else if ("BD_ACCSUBJ".equals(tableName)) {
			voname=nc.vo.bd.subjobligate.AccsubjVO.class.getName();
		}else{
			throw new ClassNotFoundException("没有找到"+tableName+"对应的类");
		}*/
		voname=vo.getSourceparam();
		String vonameArray[]=null;
		if(voname!=null&&voname.trim().length()>0&&voname.contains(",")){
			if(voname.split(",")!=null&&voname.split(",").length==3){
				vonameArray=voname.split(",");
			}
		}
		if(vonameArray==null||vonameArray.length!=3){
			return new RetMessage(false,"主动抓取数据来源参数设置错误，必须是"+IContrastUtil.DATAORIGIN_ZDZQ_PARAMETAR);
		}
		String urlStr = "";
		// 每次获取多少条
		int size = 100;// 可以通过公共参数配置
		// 返回数据
		Object obj = null;
		if(ip.startsWith("http://")){
			ip=ip.substring(7);
		}
		ip=ip.split("/")[0];
//		if(ip.indexOf("/SynDataServlet")>0){
//			ip=ip.substring(0,ip.indexOf("/SynDataServlet"));
//		}
//		if(ip.startsWith("http://")){
//			urlStr =  ip + "/SynCountServlet?classname=" + voname;
//		}else{
		
		urlStr = "http://" + ip + "/SynCountServlet?classname=" + vonameArray[2]+"&par="+vonameArray[0]+"|"+vonameArray[1];
//		}
		obj = getDate(urlStr);
		if (obj instanceof Integer) {
			// 得到数据总条数
			number = (Integer) obj;
		}else throw new Exception(obj.toString());
		
//		if(ip.startsWith("http://")){
//			urlStr =  ip + "/SynDataServlet?classname=" + voname;
//		}else{
		urlStr = "http://" + ip + "/SynDataServlet?classname=" + vonameArray[2]+"&par="+vonameArray[0]+"|"+vonameArray[1]+"&fields="+fields;
//		}
		int DataNumber = number;
		return synDataByPage(  hpk,vo,pk_datadefinh, DataNumber, size, urlStr,datarech);
	}

	public void execute(String url) {

	}
	
	/**
	 * liyunzhe add 2012-07-09,把根据同步定义得到查询的字段
	 * 如果是一个格式定义主表时候可以直接使用，例如中间表，主动抓取，webservice
	 * @param recHvo
	 * @return 
	 */
	public RetMessage getOneDataFormatdefHFields(DipDatarecHVO recHvo){
		
		StringBuffer fields=new StringBuffer();
		//查询主格式定义主vo
		String sqlFormath="  nvl(dr,0)=0 and pk_dataformatdef_h=( select hh.pk_dataformatdef_h from dip_dataformatdef_h hh where hh.pk_datarec_h='"+recHvo.getPk_datarec_h()+"' and nvl(hh.dr,0)=0 ) ";
		List list=new ArrayList();
		try {
			list=iqf.querySupervoByVoname(DataformatdefHVO.class, sqlFormath);
//		  	list=iqf.querySuperVO(sqlFormath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(list!=null&&list.size()>0){
			DataformatdefHVO formatHvo=(DataformatdefHVO) list.get(0);
			if(formatHvo!=null){
				String sqlFormatb=" pk_dataformatdef_h='"+formatHvo.getPk_dataformatdef_h()+"' and nvl(dr,0)=0  order by code";
				String flowdef=formatHvo.getMessflowdef();//对照类型，顺序还是字段，0表示顺序1表示字段
				List listFormatbs=null;
				if(flowdef!=null){
					try {
						listFormatbs=iqf.querySupervoByVoname(DataformatdefBVO.class,sqlFormatb);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(listFormatbs!=null&&listFormatbs.size()>0){
						for(int i=0;i<listFormatbs.size();i++){
							DataformatdefBVO formatbvo=(DataformatdefBVO) listFormatbs.get(i);
							if(formatbvo!=null){
								String field="";
								if(flowdef.equals("0")){//顺序
									field=formatbvo.getDatakind();//字段
								}else{
									field=formatbvo.getCorreskind();//字段
								}
								if(field!=null&&field.trim().length()>0){
									fields.append(field+",");
								}else{
									return new RetMessage(false,"查询字段不能为空");
								}
							}else{
								return new RetMessage(false,"格式定义不能为空");
							}
							if(i==listFormatbs.size()-1&&fields.length()>0){
								return new RetMessage(true,fields.toString().substring(0, fields.length()-1));
							}
						}
					}else{
						return new RetMessage(false,"没有找到数据格式定义");
					}
				}
			}
		}else{
			return new RetMessage(false,"没有找到数据格式定义");
		}
		return new RetMessage(false,"数据格式定义错误");
	}
	/**
	 * 
	 * 获取servlet连接
	 * 
	 * @param url
	 *            servlet地址
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private HttpURLConnection getConnect(String url)
			throws MalformedURLException, IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(url)
				.openConnection();
		conn.setDoOutput(true);
		return conn;
	}

	/**
	 * 调用servlet获取数据
	 * 
	 * @param url
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Object getDate(String url) throws MalformedURLException,
			IOException, ClassNotFoundException {
		Object obj = null;
		HttpURLConnection conn = getConnect(url);
		ObjectOutputStream output = new ObjectOutputStream(conn
				.getOutputStream());
		output.flush();
		output.close();
		ObjectInputStream input = new ObjectInputStream(conn.getInputStream());
		obj = input.readObject();
		return obj;
	}

	/**
	 * 获取数据,分批插入数据库
	 * 
	 * @param tableName
	 *            表名
	 * @param pk_datadefinh
	 *            字段
	 * @param DataNumber
	 *            总数
	 * @param size
	 *            每次获取多少条数据
	 * @param url
	 *            servlet地址
	 * @param drh
	 *            数据同步表的主表主键
	 *            
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws DAOException
	 */
	public RetMessage synDataByPage(String hpk,DipDatarecHVO vo,String pk_datadefinh, int DataNumber, int size,
			String url,String drh) throws MalformedURLException, IOException,
			ClassNotFoundException, DAOException {
		RetMessage ret=new RetMessage();
		Integer succ=0;
		Integer fa=0;
		Integer hl=0;
		if (DataNumber != 0) {
			// 计算发送次数
			int num = (DataNumber  / size)+1;
			String tempurl="";
			for (int j = 1; j <= num; j++) {
				if(num==j){
					tempurl=url+"&startcount="+((j-1)*size+1)+"&count="+DataNumber;
				}else{
					tempurl=url+"&startcount="+((j-1)*size+1)+"&count="+(j*size);
				}
				String ss=synOpt(  hpk,vo,tempurl,pk_datadefinh,drh,succ,fa,hl);
				if(ss!=null&&ss.length()>0){
				String[] sp=ss.split(";");
				if(sp.length==3){
					succ=Integer.parseInt(sp[0]);
					fa=Integer.parseInt(sp[1]);
					hl=Integer.parseInt(sp[2]);
				}
				}
			}
			ret.setIssucc(true);
			ret.setMessage("成功"+succ+";失败"+fa+";忽略"+hl+"条数据");
		} else {
			ret.setIssucc(false);
			ret.setMessage("数量为0，没有数据");
			Logger.error("数量为0，没有数据");
		}

		ilm.writToDataLog_RequiresNew(hpk, IContrastUtil.YWLX_TB, "成功"+succ+";失败"+fa+";忽略"+hl+"条数据");
		return ret;
	}
	ILogAndTabMonitorSys ilm = (ILogAndTabMonitorSys) NCLocator.getInstance().lookup(ILogAndTabMonitorSys.class.getName());
	public boolean isfu(String pk_repeatdata){
		if(pk_repeatdata.equals("0001ZZ10000000018K6M")){
			return true;
		}else return false;
	}
	/**
	 * 将获得的数据插入到表中
	 * 
	 * @param url
	 *            servlet地址
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	public String synOpt(String hpk,DipDatarecHVO vo,String url,String pk_datadefinh,String drh,Integer succ,Integer fa,Integer hl) throws MalformedURLException, IOException,
			ClassNotFoundException, DAOException {
		Object obj = getDate(url);
		List list = new ArrayList();
		if (obj instanceof List) {
			list = (List) obj;
			if (null != list && list.size() > 0) {
				Hashtable<String, DipDatadefinitBVO> hashTable = new Hashtable<String, DipDatadefinitBVO>();
				boolean isfg=isfu(vo.getRepeatdata());
				// 0：顺序，1：字段对照
				// 数据字段
				try {
//					IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
//					IQueryField query=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
					
					// 存储表名
					String memorytable = vo.getMemorytable();
					HashMap<String, String> map = new HashMap<String, String>();
					DataformatdefHVO fhvo = null;
					// 数据格式定义表头
					String pk = "select pk_dataformatdef_h from dip_dataformatdef_h where pk_datarec_h='" + vo.getPk_datarec_h() + "' and rownum<=1 and nvl(dr,0)=0";
					pk = iqf.queryfield(pk);
					List<DataformatdefHVO> listf = (List<DataformatdefHVO>) getBaseDao().retrieveByClause(DataformatdefHVO.class, "pk_datarec_h='" + vo.getPk_datarec_h() + "' and rownum<=1 and nvl(dr,0)=0");
					if(list!=null&&list.size()>0){
						fhvo=listf.get(0);
					}else{
						ilm.writToDataLog_RequiresNew(hpk, IContrastUtil.YWLX_TB, "没有找到相应的格式定义的格式！");
						throw new Exception( "没有找到相应的格式定义的格式！");
					}
					String def_pk="";
					Map fmap=new HashMap<String, String>();
					DipDatadefinitHVO hvo=(DipDatadefinitHVO) getBaseDao().retrieveByPK(DipDatadefinitHVO.class, memorytable);
					// 2、查数据定义
					List<DipDatadefinitBVO> dataDefinitBvo = null;
					try {// 数据定义表体VO
						dataDefinitBvo = (List<DipDatadefinitBVO>) getBaseDao().retrieveByClause( DipDatadefinitBVO.class, "pk_datadefinit_h='" + memorytable + "' and nvl(dr,0)=0");
						if(dataDefinitBvo!=null&&dataDefinitBvo.size()>0){
							for(int i=0;i<dataDefinitBvo.size();i++){
								DipDatadefinitBVO bvo=dataDefinitBvo.get(i);
								fmap.put(bvo.getEname().toLowerCase(), bvo);
							}
						}else throw new DAOException("");
					} catch (DAOException e1) {
						e1.printStackTrace();
						ilm.writToDataLog_RequiresNew(hpk, IContrastUtil.YWLX_TB,  "查询数据数据格式定义错误！" + e1.getMessage());
						throw new Exception( "查询数据数据格式定义错误！" + e1.getMessage());
					}
					if (dataDefinitBvo == null || dataDefinitBvo.size() <= 0) {
						ilm.writToDataLog_RequiresNew(hpk, IContrastUtil.YWLX_TB, "数据定义中没有找掉表的结构定义！");
						throw new Exception( "数据定义中没有找掉表的结构定义！");
					}
					boolean haskey=false;
					for (DipDatadefinitBVO bvo : dataDefinitBvo) {
						hashTable.put(bvo.getEname(), bvo);
						if (bvo.getIspk() != null && bvo.getIspk().booleanValue()) {
							hashTable.put("#PKFIELD#", bvo);
							def_pk=bvo.getEname();
							haskey=true;
						}
					}
					for(int i=0;i<list.size();i++){
						Map vmap=(Map) list.get(i);
						Iterator<String> it=vmap.keySet().iterator();
						String sqli="insert into "+hvo.getMemorytable()+"(";
						String infileds="";
						String values="";
						String sqlu="update "+hvo.getMemorytable()+" set ";
						String keyvalue="";
						while( it.hasNext()){
							String key=it.next();
							Object ob=vmap.get(key);
							if(haskey&&def_pk!=null&&def_pk.length()>0&&def_pk.equalsIgnoreCase(key)){
								if (ob instanceof String) {
									if(ob!=null){
										keyvalue="'"+ob.toString()+"'";
									}else{
										keyvalue=null;
									}
								}else{
									keyvalue=ob+"";
								}
							}
							if(fmap.containsKey(key.toLowerCase())){
								infileds=infileds+key+",";
								if (ob instanceof String) {
									if(ob!=null){
									values=values+"'"+ob.toString()+"',";
									sqlu=sqlu+key+"="+"'"+ob.toString()+"',";
									}else{
										values=values+null+",";
										sqlu=sqlu+key+"=null,";
									}
								}else{
									values=values+ob+",";
									sqlu=sqlu+key+"="+ob+",";
								}
							}
						}
						sqli=sqli+infileds.substring(0,infileds.length()-1)+") values ("+values.substring(0,values.length()-1)+")";
						if(haskey){
							if(isfg){
								String sql="delete from "+hvo.getMemorytable()+" where "+def_pk+"='"+keyvalue+"'";
								iqf.exesql(sql);
								iqf.exesql(sqli);
								succ++;
							}else{
								String sql="select 1 from "+hvo.getMemorytable()+" where "+def_pk+"='"+keyvalue+"'";
								List listss=iqf.queryfieldList(sql);
								if(listss==null||listss.size()<=0){
									iqf.exesql(sqli);
									succ++;
								}else{
									hl++;
								}
								
							}
						}else{
							iqf.exesql(sqli);
							succ++;
						}
					}
				}catch(Exception e ){
					e.printStackTrace();

					ilm.writToDataLog_RequiresNew(hpk, IContrastUtil.YWLX_TB, e.getMessage());
					fa++;
				}
				
				}
			}
		return succ+";"+fa+";"+hl;
	}

}
