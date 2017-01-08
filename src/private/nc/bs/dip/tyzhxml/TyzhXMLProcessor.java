package nc.bs.dip.tyzhxml;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.dip.in.ValueTranslator;
import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.ILogAndTabMonitorSys;
import nc.itf.dip.pub.IQueryField;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.RetMessage;
import nc.vo.dip.in.RowDataVO;
import nc.vo.dip.tyxml.ConfigVO;
import nc.vo.dip.tyxml.DipTYXMLDatachangeBVO;
import nc.vo.dip.tyxml.DipTYXMLDatachangeHVO;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;

/**
 * @author Administrator
 * @数据转换主流程
 */
public class TyzhXMLProcessor {
	public static final String DEFAULT_TEMPLET = "#DEFAULT#";
	public static final String PKFIELD_ATTRIBUTE = "#DEFAULT#";
	
	public static final int DETAIL_UNION_TWO = 0;
	public static final int DETAIL_UNION_THREE = 1;
	public static final int DETAIL_UNION_FOUR = 2;
	
	public static final String SEPARATE_TAG = "#";
	
	public static int voucherPerFile = 1;
	

	private String pk_tyzh;  //数据转换任务主线索号（数据转换配置主表PK）

	private ConfigVO config;
	private List<DipTYXMLDatachangeBVO>  templet;
	private IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	private BaseDAO dao;
	public  int success=0;//转换成功的xml数量
	public  int fail=0;//转换失败的xml数量
	public  int fg=0;//覆盖的数量
	public  int hl=0;//忽略的数量
	public BaseDAO getDAO(){
		if(dao == null){
			dao = new BaseDAO(IContrastUtil.DESIGN_CON);
		}
		return dao;
	}
	
	private ILogAndTabMonitorSys logUtil;
	public ILogAndTabMonitorSys getLogUtil(){
		if(logUtil == null){
			logUtil = (ILogAndTabMonitorSys)NCLocator.getInstance().lookup(ILogAndTabMonitorSys.class.getName());
		}
		return logUtil;
	}
	
	public void errLogOut(String log){
		getLogUtil().writToDataLog_RequiresNew(pk_tyzh, IContrastUtil.YWLX_TYZHXML, log);
	}
	private  Integer countthread=0;
	private boolean sysfailed = false;
	
	private List errList = new ArrayList();

	@SuppressWarnings("unchecked")
	public TyzhXMLProcessor(String clue){
		pk_tyzh = clue;
	}
	Vector<DipTYXMLDatachangeBVO> vvos=new Vector<DipTYXMLDatachangeBVO>();
	public  Integer countmap=0;//线程计数器
	@SuppressWarnings("unchecked")
	public RetMessage execute(){
		success=0;
		fail=0;
		String errMsg = "";
		try{
			if(!dataPrepare()){
				return new RetMessage(false,"数据解析失败！");
			}
			templet =  (List<DipTYXMLDatachangeBVO>)getDAO().retrieveByClause(DipTYXMLDatachangeBVO.class, "pk_tyxml_h='"+pk_tyzh+"' ","orderno");//.executeQuery("select dip_tyzhq_b.*,dip_datadefinit_b.ename yename,dip_datadefinit_b.type ytype from dip_tyzhq_b left join dip_datadefinit_b on dip_tyzhq_b.pk_lyzd=dip_datadefinit_b.pk_datadefinit_b" +
//					" where nvl(dip_tyzhq_b.dr,0)=0 and  nvl(dip_datadefinit_b.dr,0)=0 and pk_tyzhq_h='"+pk_tyzh+"' and changformu is not null", new BeanListProcessor(DipTYXMLDatachangeBVO.class));
			if(templet==null||templet.size()<=0){
				errLogOut("没有找到转换定义");
				return new RetMessage(false,"没有找到转换定义");
			}
			String ismultthreadsql="select count(*) from dip_datadefinit_b where pk_datadefinit_h='"+config.getPk_sourtab()+"' and (ispk='Y' or issyspk='Y') and nvl(dr,0)=0";
			String ismultthreadcount=iqf.queryfield(ismultthreadsql);
			List<DipTYXMLDatachangeBVO> vos=new ArrayList<DipTYXMLDatachangeBVO>();
//			if(ismultthreadcount==null||!ismultthreadcount.equals("1")){
			String sqlcount="select count(*) from "+config.getParent().getSourcetab()+" where nvl(dr,0)=0";
			String countamount=iqf.queryfield(sqlcount);//表的总的记录数
			Integer countint=countamount==null?0:Integer.parseInt(countamount);//表的总的记录数
			if(countint<=0){
				errLogOut("未找到符合条件的数据");
				throw new DAOException("未找到符合条件的数据");
			}
			DipTYXMLDatachangeHVO hvo=config.getParent();
			Integer combcount=1;//合并条目数
			if(hvo.getIscombo()!=null&&hvo.getIscombo().equals("Y")){
				combcount=hvo.getMaxcomnum();
			}
			int amount=(countint%combcount==0)?(countint/combcount):(countint/combcount+1);
			if(true){
				//查询所有需要转换的数据
				for(int num=0;num<amount;num++){
					List list = (List)getDAO().executeQuery("select * from  ( select "+config.getParent().getSourcetab()+".*,rownum rwn  from  "+config.getParent().getSourcetab()+" where rownum<="+(num+1)*combcount+"  ) where rwn>"+num*combcount, new MapListProcessor());
	//				List list = (List)getDAO().executeQuery("select * from "+config.getParent().getSourcetab()+" where nvl(dr,0)=0", new MapListProcessor());
					if(list == null || list.size() == 0){
						errLogOut("未找到符合条件的数据");
						throw new DAOException("未找到符合条件的数据");
					}
					RowDataVO[] data;
					data = new RowDataVO[list.size()];
					for(int i=0;i<list.size();i++){
						Map map = (Map)list.get(i);
						data[i] = new RowDataVO();
						String[] fields = (String[])map.keySet().toArray(new String[0]);
						for(int j=0;j<fields.length;j++){
							if (!fields[j].equals("ts") && !fields[j].equals("dr")) {
								
								String field = fields[j];
								Object value="";
								if(fields[j]!=null&&map.get(fields[j])!=null&&config.getDatadef().get(fields[j].toUpperCase())!=null){
									value = ValueTranslator.translate(
											map.get(fields[j])==null?null:map.get(fields[j]).toString(), 
											config.getDatadef().get(fields[j].toUpperCase()).getType(),
											config.getDatadef().get(fields[j].toUpperCase()).getLength() == null?0: config.getDatadef().get(fields[j].toUpperCase()).getLength());
									if(value instanceof String){
										value=value.toString().trim();
									}
								}
								data[i].setAttributeValue(field, value);
								System.out.println("[FIELD] " + field + "  [VALUE]" + value);
							}	
						}
						data[i].setTableName(config.getParent().getSourcetab());
						data[i].setPKField(config.getDatadef().get(PKFIELD_ATTRIBUTE)==null?"":config.getDatadef().get(PKFIELD_ATTRIBUTE).getEname());
						data[i].setPrimaryKey((String)data[i].getAttributeValue(data[i].getPKFieldName()));
					}
				
					vos=formatVoucher(data);
					if(vos==null||vos.size()<=0){
						errLogOut("找到模板，没有满足条件的业务数据！");
						return new RetMessage(false,"找到模板，没有满足条件的业务数据！");
					}
					if(vos!=null&&vos.size()==0){
						errLogOut("转换后的数据为0");
						return new RetMessage(false,"转换后的数据为0");
					}
					Document listd=createListD(vos);
					writeToFileD(listd,num);
					success=success+list.size();
					fail++;
				}
			}else{
				
				Integer oncecount=100;
				amount=(countint%oncecount==0)?(countint/oncecount):(countint/oncecount+1);
				String c=iqf.queryfield("select sysvalue from dip_runsys_b where nvl(dr,0)=0 and syscode='DIP-0000008'");
				int theadcount=(c==null||c.length()<=0)?5:Integer.parseInt(c);
				for(int i=0;i<amount;i++){
					while( countthread>theadcount){
						try{
							Thread.sleep(1000);
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
					countthread++;
					new Threandfromat(i,oncecount).start();
				}
				while(amount>countmap){
					try{
						Thread.sleep(1000);
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
//				if(vvos!=null&&vvos.size()>0){
//					vos.addAll(vvos);
//				}
				
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			//处理状态运行状态和结果
			sysfailed = true;
			
			errLogOut(e.getClass().getSimpleName() + ": " + e.getMessage());
			errMsg = e.getMessage();
		}
		if(sysfailed){
			
			return new RetMessage(false, "数据转换执行失败: "+errMsg);
		}
		success=success+hl;
		errLogOut("转换完成，成功"+success+"条数据，失败"+fail+"条数据");
		RetMessage mes=new RetMessage();
		mes.setIssucc(true);
		mes.setSu(success);
//		mes.setFa(fail);
		mes.setMessage("转换完成，成功"+success+"条数据，写到"+fail+"个文件中");
		return mes;
	}
	/**
	 * @desc 合并Document
	 * */
	private List<Document> comboD(List<Document> listd) {
		DipTYXMLDatachangeHVO hvo=config.getParent();
		Integer combcount=1;		
		Integer combl=2;
		if(listd==null||listd.size()<=0){
			return listd;
		}
		if(hvo.getIscombo()!=null&&hvo.getIscombo().equals("Y")){
			combcount=hvo.getMaxcomnum();
			combl=hvo.getComlev()==null?2:hvo.getComlev();
			if(combl<2){
				combl=2;
			}
		}
		if(combcount>1){
			List<Document> listr=new ArrayList<Document>();
			int count=listd.size()/combcount+1;
			for(int i=0;i<count;i++){
				Document doc=listd.get(0+combcount*i);
				Element root=doc.getRootElement();
				int tonum=(i+1)*combcount;
				for(int k=0+i*combcount+1;k<(tonum<listd.size()?tonum:listd.size());k++){
					Document tempdoc=listd.get(k);
					Element rooti=tempdoc.getRootElement();
					List<Element> es=rooti.elements();
					if(es!=null&&es.size()>0){
						for( Element e:es){
							e.setParent(null);
							root.add(e);
						}
					}
				}
				listr.add(doc);
			}
			return listr;
		}
		return listd;
	}

	/**
	 * @desc 把转换完的document写到文件里
	 * */
	private void writeToFileD(Document listd,int i) {
		if(listd==null){
			return;
		}
		String filedir=config.getFilepath();
		String filename=config.getParent().getFilename();
		String filePath=filedir+"/"+(filename.endsWith(".xml")?filename.substring(0,filename.indexOf(".xml")):filename)+(i+1)+".xml";
		File dir = new File(filedir);
		if(!dir.exists()){
			dir.mkdir();
		}else {
			if(i==0){
				File[] fils=dir.listFiles();
				if(fils!=null&&fils.length>0){
					for(int k=0;k<fils.length;k++){
						if(fils[k].getName().startsWith(filename.endsWith(".xml")?filename.substring(0,filename.indexOf(".xml")):filename)){
							fils[k].delete();
						}
					}
				}
			}
		}
			org.dom4j.io.XMLWriter out =null;
			FileOutputStream ots=null;
			try{
				Document doc=listd;
				new File(filePath).createNewFile();
				OutputFormat format = OutputFormat.createPrettyPrint();
				ots=new FileOutputStream(filePath);
				out = new org.dom4j.io.XMLWriter(ots,format);
				out.write(doc);
			}catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(ots!=null){
					try {
						ots.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(out!=null){
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
	}

	private List<DipTYXMLDatachangeBVO> formatVoucher(RowDataVO[] data) {
//		private List<List<DipTYXMLDatachangeBVO>> formatVoucher(RowDataVO[] data) {
		TyzhGenerator gen=new TyzhGenerator(config);
//		List<List<DipTYXMLDatachangeBVO>> list=new ArrayList<List<DipTYXMLDatachangeBVO>>();
		List<DipTYXMLDatachangeBVO> vo = null;
		try {
			if(data!=null&&data.length>0){
				vo=gen.crtVoucher(data, templet);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	/*	for(int i=0;i<data.length;i++){
			List<DipTYXMLDatachangeBVO> vo = null;
			try {
				vo = gen.crtVoucher(data[i], templet);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(vo != null&&vo.size()>0){
				list.add(vo);
				success++;
			}else{
				fail++;
			}
		}*/
		return vo;
	}
	class Threandfromat extends Thread{
		Integer num;
		Integer oncecount;
		public Threandfromat(Integer num,Integer oncecount){
			super();
			this.num=num;
			this.oncecount=oncecount;
		}
		@Override
		public void run() {
			List<DipTYXMLDatachangeBVO> vos=null;
			try{
				//查询所有需要转换的数据
//				List list = (List)getDAO().executeQuery("select * from "+config.getParent().getSourcetab()+" where nvl(dr,0)=0", new MapListProcessor());
				List list = (List)getDAO().executeQuery("select * from  ( select "+config.getParent().getSourcetab()+".*,rownum rwn  from  "+config.getParent().getSourcetab()+" where rownum<="+(num+1)*oncecount+"  ) where rwn>"+num*oncecount, new MapListProcessor());
				if(list == null || list.size() == 0){
					errLogOut("未找到符合条件的数据");
					throw new DAOException("未找到符合条件的数据");
				}
				RowDataVO[] data;
				data = new RowDataVO[list.size()];
				for(int i=0;i<list.size();i++){
					Map map = (Map)list.get(i);
					data[i] = new RowDataVO();
					String[] fields = (String[])map.keySet().toArray(new String[0]);
					for(int j=0;j<fields.length;j++){
						if (!fields[j].equals("ts") && !fields[j].equals("dr")) {
							
							String field = fields[j];
							Object value="";
							if(fields[j]!=null&&map.get(fields[j])!=null&&config.getDatadef().get(fields[j].toUpperCase())!=null){
								value = ValueTranslator.translate(
										map.get(fields[j])==null?null:map.get(fields[j]).toString(), 
										config.getDatadef().get(fields[j].toUpperCase()).getType(),
										config.getDatadef().get(fields[j].toUpperCase()).getLength() == null?0: config.getDatadef().get(fields[j].toUpperCase()).getLength());
								if(value instanceof String){
									value=value.toString().trim();
								}
							}
							data[i].setAttributeValue(field, value);
							System.out.println("[FIELD] " + field + "  [VALUE]" + value);
						}	
					}
					data[i].setTableName(config.getParent().getSourcetab());
					data[i].setPKField(config.getDatadef().get(PKFIELD_ATTRIBUTE)==null?"":config.getDatadef().get(PKFIELD_ATTRIBUTE).getEname());
					data[i].setPrimaryKey((String)data[i].getAttributeValue(data[i].getPKFieldName()));
				}
			
				vos=formatVoucher(data);
				/*if(vos==null||vos.size()<=0){
					errLogOut("找到模板，没有满足条件的业务数据！");
					return new RetMessage(false,"找到模板，没有满足条件的业务数据！");
				}
				if(vos!=null&&vos.size()==0){
					errLogOut("转换后的数据为0");
					return new RetMessage(false,"转换后的数据为0");
				}*/
				Document listd=createListD(vos);
//				listd=comboD(listd);
				writeToFileD(listd,num);
				success=success+list.size();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(vos!=null&&vos.size()>0){
					vvos.addAll(vos);
				}
				countmap++;
				countthread--;
			}
		}
	}
	/**
	 * 将处理的转换完的vo转成doucomet
	 * @throws IOException 
	 */
	private Document createListD(List<DipTYXMLDatachangeBVO> voi) throws IOException {
		
		Document doc=DocumentFactory.getInstance().createDocument();
		if(voi!=null&&voi.size()>0){
			Map<String,Element> emap=new HashMap<String, Element>();//Element 的map
			Map<String,Integer> ordermap=new HashMap<String, Integer>();//编号的map
				String roote="";
				emap=new HashMap<String, Element>();
				ordermap=new HashMap<String, Integer>();
				if(voi!=null&&voi.size()>0){
					DipTYXMLDatachangeBVO bvoi=voi.get(0);
					String bq=bvoi.getBqfield();
					bq=bq.substring(1,bq.length()-1);
					bq=bq.replace("><", ",");
					roote=bq.split(",")[0];
				}
				if(voi!=null&&voi.size()>0){
					for(int i=0;i<voi.size();i++){
						voi.get(i).setOrderno(i+1);
						voi.get(i).setPrimaryKey((i+1)+"-"+voi.get(i).getPrimaryKey());
					}
					for(DipTYXMLDatachangeBVO vi:voi){
						String bq=vi.getBqfield();
						bq=bq.substring(1,bq.length()-1);
						bq=bq.replace("><", ",");
						String[] bqs=bq.split(",");
						Element el=null;
						for(int i=0;i<bqs.length;i++){
							String strtemp="";
							for(int j=0;j<=i;j++){
								strtemp=strtemp+bqs[j]+",";
							}
							if(strtemp.length()>0){
								strtemp=strtemp.substring(0,strtemp.length()-1);
							}
							String key=i+"-"+bqs[i];
							if(emap.containsKey(key)&&ordermap.containsKey(key)){
								Integer ord=ordermap.get(key);
								Integer temporder=0;
								for(int k=0;k<voi.size();k++){
									if(strtemp.equals(voi.get(k).getBqfield().substring(1,voi.get(k).getBqfield().length()-1).replace("><", ","))){
										temporder=voi.get(k).getOrderno();
									}
									if(vi.getPrimaryKey().equals(voi.get(k).getPrimaryKey())){
										break;
									}
								}
								if(ord==temporder||temporder==0){
									el=emap.get(key);
								}else{
									if(bqs[i].equals(roote)){
										if(doc.getRootElement()==null){
											el=doc.addElement(bqs[i]);
										}else{
											el=doc.getRootElement();
										}
									}else{
										el=el.addElement(bqs[i]);
									}
									emap.put(key, el);
									ordermap.put(key, vi.getOrderno());
								}
							}else{
								if(bqs[i].equals(roote)){
									el=doc.addElement(bqs[i]);
								}else{
									el=el.addElement(bqs[i]);
								}
								emap.put(key, el);
								ordermap.put(key, vi.getOrderno());
							}
						}
						if(vi.getPro()!=null&&vi.getPro().length()>0){
							String pro=vi.getPro();
							String[] pros=pro.split(" ");
							for(int i=0;i<pros.length;i++){
								el.addAttribute(pros[i].split("=")[0], pros[i].split("=")[1]);
							}
						}
						if(vi.getChangformu()!=null&&vi.getChangformu().length()>0){
							el.setText(vi.getChangformu());
						}
					}
				}
		}
		return doc;
	}	
	
	
	



	/**
	 * @desc 
	 * 		1、取得数据转换表头VO，公司对照，字段对照，影响因素，合并凭证设置，是否固定组织
	 * 		2、取得公司对照模板的map
	 * 		3、查询业务数据
	 * 取得配置和业务数据
	 * @throws DAOException 
	 */
	@SuppressWarnings("unchecked")
	private boolean dataPrepare() throws DAOException {
		VOFormat f = new VOFormat();
		//1、取得数据转换表头VO，公司对照，字段对照，影响因素，合并凭证设置，是否固定组织
		config = f.getConfigVO(pk_tyzh);
		if(config==null){
			return false;
		}
		return true;
	}
}
