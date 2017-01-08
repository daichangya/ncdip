package nc.bs.dip.tyzh;



import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.dip.in.ValueTranslator;
import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.ILogAndTabMonitorSys;
import nc.itf.dip.pub.IQueryField;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.RetMessage;
import nc.vo.dip.in.RowDataVO;
import nc.vo.dip.tyzhq.ConfigVO;
import nc.vo.dip.tyzhq.DipTYZHDatachangeBVO;
import nc.vo.pub.BusinessException;

/**
 * @author Administrator
 * @����ת��������
 */
public class TyzhProcessor {
	public static final String DEFAULT_TEMPLET = "#DEFAULT#";
	public static final String PKFIELD_ATTRIBUTE = "#DEFAULT#";
	
	public static final int DETAIL_UNION_TWO = 0;
	public static final int DETAIL_UNION_THREE = 1;
	public static final int DETAIL_UNION_FOUR = 2;
	
	public static final String SEPARATE_TAG = "#";
	
	public static int voucherPerFile = 1;
	

	private String pk_tyzh;  //����ת�������������ţ�����ת����������PK��

	private ConfigVO config;
	private List<DipTYZHDatachangeBVO>  templet;
	private IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	private BaseDAO dao;
	public  int success=0;//ת���ɹ���xml����
	public  int fail=0;//ת��ʧ�ܵ�xml����
	public  int fg=0;//���ǵ�����
	public  int hl=0;//���Ե�����
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
		getLogUtil().writToDataLog_RequiresNew(pk_tyzh, IContrastUtil.YWLX_TYZH, log);
	}
	private  Integer countthread=0;
	private boolean sysfailed = false;
	
	private List errList = new ArrayList();

	@SuppressWarnings("unchecked")
	public TyzhProcessor(String clue){
		pk_tyzh = clue;
	}
	Vector<RowDataVO> vvos=new Vector<RowDataVO>();
	public  Integer countmap=0;//�̼߳�����
	@SuppressWarnings("unchecked")
	public RetMessage execute(){
		success=0;
		fail=0;
		String errMsg = "";
		try{
			if(!dataPrepare()){
				return new RetMessage(false,"���ݽ���ʧ�ܣ�");
			}
			templet =  (List<DipTYZHDatachangeBVO>)getDAO().executeQuery("select dip_tyzhq_b.*,dip_datadefinit_b.ename yename,dip_datadefinit_b.type ytype from dip_tyzhq_b left join dip_datadefinit_b on dip_tyzhq_b.pk_lyzd=dip_datadefinit_b.pk_datadefinit_b" +
					" where nvl(dip_tyzhq_b.dr,0)=0 and  nvl(dip_datadefinit_b.dr,0)=0 and pk_tyzhq_h='"+pk_tyzh+"' and changformu is not null", new BeanListProcessor(DipTYZHDatachangeBVO.class));
			if(templet==null||templet.size()<=0){
				errLogOut("û���ҵ�ת������");
				return new RetMessage(false,"û���ҵ�ת������");
			}
			
			String sourcesqlcount="select count(*) from "+config.getParent().getSourcetab()+" where nvl(dr,0)=0";
			String sourcecountamount=iqf.queryfield(sourcesqlcount);
			Integer sourcecountint=sourcecountamount==null?0:Integer.parseInt(sourcecountamount);
			if(sourcecountint<=0){
				errLogOut("��Դ��û������");
				return new RetMessage(false,"��Դ��û������");
			}
			if(sourcecountint>100000){
				errLogOut("��Դ���ܳ���10��������,ʵ�ʴ���"+sourcecountint+"������");
				return new RetMessage(false,"��Դ���ܳ���10��������,ʵ�ʴ���"+sourcecountint+"������");
			}
			
			String ismultthreadsql="select count(*) from dip_datadefinit_b where pk_datadefinit_h='"+config.getPk_sourtab()+"' and (ispk='Y' or issyspk='Y') and nvl(dr,0)=0";
			String ismultthreadcount=iqf.queryfield(ismultthreadsql);
			RowDataVO[] vos=null;
			if(ismultthreadcount==null||!ismultthreadcount.equals("1")){
				//��ѯ������Ҫת��������
				List list = (List)getDAO().executeQuery("select * from "+config.getParent().getSourcetab()+" where nvl(dr,0)=0", new MapListProcessor());
				if(list == null || list.size() == 0){
					errLogOut("δ�ҵ���������������");
					throw new DAOException("δ�ҵ���������������");
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
			
				vos=formatVoucher(null,data);
			}else{
				String sqlcount="select count(*) from "+config.getParent().getSourcetab()+" where nvl(dr,0)=0";
				String countamount=iqf.queryfield(sqlcount);
				Integer countint=countamount==null?0:Integer.parseInt(countamount);
				if(countint<=0){
					errLogOut("δ�ҵ���������������");
					throw new DAOException("δ�ҵ���������������");
				}
				
				Integer oncecount=100;
				int amount=(countint%oncecount==0)?(countint/oncecount):(countint/oncecount+1);
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
				if(vvos!=null&&vvos.size()>0){
					vos=vvos.toArray(new RowDataVO[vvos.size()]);
				}
			}
			if(vos==null||vos.length<=0){
				errLogOut("�ҵ�ģ�壬û������������ҵ�����ݣ�");
				return new RetMessage(false,"�ҵ�ģ�壬û������������ҵ�����ݣ�");
			}
			if(vos!=null&&vos.length==0){
				errLogOut("ת���������Ϊ0");
				return new RetMessage(false,"ת���������Ϊ0");
			}
			writeVoucher(vos);
		}catch (Exception e) {
			e.printStackTrace();
			//����״̬����״̬�ͽ��
			sysfailed = true;
			
			errLogOut(e.getClass().getSimpleName() + ": " + e.getMessage());
			errMsg = e.getMessage();
		}
		if(sysfailed){
			
			return new RetMessage(false, "����ת��ִ��ʧ��: "+errMsg);
		}
		success=success+hl;
		errLogOut("ת����ɣ��ɹ�"+success+"�����ݣ�ʧ��"+fail+"�����ݣ�����"+fg+"�����ݣ�����"+hl+"������"+"--"+IContrastUtil.DATAPROCESS_HINT);
		RetMessage mes=new RetMessage();
		mes.setIssucc(true);
		mes.setSu(success);
		mes.setFa(fail);
		mes.setMessage("ת����ɣ��ɹ�"+success+"�����ݣ�ʧ��"+fail+"�����ݣ�����"+fg+"�����ݣ�����"+hl+"������");
		return mes;
	}


	private RowDataVO[] formatVoucher(Object object, RowDataVO[] data) {
		TyzhGenerator gen=new TyzhGenerator(config);
		List<RowDataVO> list=new ArrayList<RowDataVO>();
		for(int i=0;i<data.length;i++){
			RowDataVO vo = null;
			try {
				vo = gen.crtVoucher(data[i], templet);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(vo != null){
				list.add(vo);
			}else{
				fail++;
			}
		}
		return list.toArray(new RowDataVO[list.size()]);
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
			RowDataVO[] vos=null;
			try{
				//��ѯ������Ҫת��������
//				List list = (List)getDAO().executeQuery("select * from "+config.getParent().getSourcetab()+" where nvl(dr,0)=0", new MapListProcessor());
				List list = (List)getDAO().executeQuery("select * from  ( select "+config.getParent().getSourcetab()+".*,rownum rwn  from  "+config.getParent().getSourcetab()+" where rownum<="+(num+1)*oncecount+"  ) where rwn>"+num*oncecount, new MapListProcessor());
				if(list == null || list.size() == 0){
					errLogOut("δ�ҵ���������������");
					throw new DAOException("δ�ҵ���������������");
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
			
				vos=formatVoucher(null,data);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(vos!=null&&vos.length>0){
					vvos.addAll(new ArrayList<RowDataVO>(Arrays.asList(vos)));
				}
				countmap++;
				countthread--;
			}
		}
	}
	/**
	 * ��������ϵ�ƾ֤д��XML�ļ�
	 * @throws IOException 
	 */
	private void writeVoucher(RowDataVO[] vos) throws IOException {
		if(vos!=null&&vos.length>0){
			for(int i=0;i<vos.length;i++){
				RowDataVO vo=vos[i];
				String[] att=vo.getAttributeNames();
				String fild="";
				String val="";
				String pkvalue="";
				for(int j=0;j<att.length;j++){
					if(config.getTagpk()!=null){
						if(att[j].toLowerCase().equals(config.getTagpk().toLowerCase())){
							pkvalue=vo.getAttributeValue(att[j])+"";
						}	
					}
					
					fild=fild+att[j]+",";
					val=val+"'"+vo.getAttributeValue(att[j])+"',";
				}
				if(pkvalue!=null&&pkvalue.length()>0){
					String querysql="select "+config.getTagpk()+" from "+config.getParent().getTargettab()+" where "+config.getTagpk()+"='"+pkvalue+"'";
					String pkf="";
					try {
						pkf=iqf.queryfield(querysql);
					} catch (BusinessException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (DbException e) {
						e.printStackTrace();
					}
					if(pkf!=null&&pkf.length()>0){
						if(config.isIsfg()){
							String dsql="delete from "+config.getParent().getTargettab()+" where "+config.getTagpk()+"='"+pkvalue+"'";
							try {
								iqf.exesql(dsql);
								fg++;
							} catch (BusinessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (DbException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else {
							hl++;
							continue;
						}
					}
				}
				String sql="insert into "+config.getParent().getTargettab()+" ("+fild.substring(0,fild.length()-1)+") values ("+val.substring(0,val.length()-1)+")";
				try {
					iqf.exesql(sql);
				} catch (BusinessException e) {
					e.printStackTrace();
					errLogOut(e.getMessage()+sql);
					fail++;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					errLogOut(e.getMessage()+sql);
					fail++;
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					errLogOut(e.getMessage()+sql);
					fail++;
				}catch(Exception e){
					e.printStackTrace();
					errLogOut(e.getMessage()+sql);
					fail++;
				}
				success++;
			}
		}
	}
	
	
	
	



	/**
	 * @desc 
	 * 		1��ȡ������ת����ͷVO����˾���գ��ֶζ��գ�Ӱ�����أ��ϲ�ƾ֤���ã��Ƿ�̶���֯
	 * 		2��ȡ�ù�˾����ģ���map
	 * 		3����ѯҵ������
	 * ȡ�����ú�ҵ������
	 * @throws DAOException 
	 */
	@SuppressWarnings("unchecked")
	private boolean dataPrepare() throws DAOException {
		VOFormat f = new VOFormat();
		//1��ȡ������ת����ͷVO����˾���գ��ֶζ��գ�Ӱ�����أ��ϲ�ƾ֤���ã��Ƿ�̶���֯
		config = f.getConfigVO(pk_tyzh);
		if(config==null){
			return false;
		}
		return true;
	}
}
