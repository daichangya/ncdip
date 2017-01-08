package nc.bs.dip.voucher;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.dip.in.ValueTranslator;
import nc.bs.dip.xml.XMLWriter;
import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.ILogAndTabMonitorSys;
import nc.itf.dip.pub.checkplugin.ICheckPlugin;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.util.dip.sj.CheckMessage;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.RetMessage;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.dataverify.DataverifyBVO;
import nc.vo.dip.in.RowDataVO;
import nc.vo.dip.voucher.ConfigVO;
import nc.vo.dip.voucher.FreeValueVO;
import nc.vo.dip.voucher.TempletVO;
import nc.vo.dip.voucher.VoucherDetailVO;
import nc.vo.dip.voucher.VoucherHVO;
import nc.vo.dip.voucher.VoucherVO;
import nc.vo.pub.lang.UFDouble;

/**
 * @author Administrator
 * @数据转换主流程
 */
public class DataChangeProcessor {
	public static final String DEFAULT_TEMPLET = "#DEFAULT#";
	public static final String PKFIELD_ATTRIBUTE = "#DEFAULT#";
	
	public static final int DETAIL_UNION_TWO = 0;
	public static final int DETAIL_UNION_THREE = 1;
	public static final int DETAIL_UNION_FOUR = 2;
	
	public static final String SEPARATE_TAG = "#";
	
	public static int voucherPerFile = 1;
	
	public static int beforPrecision=0;//转换前精度
	public static int afterPrecision=0;//转化后精度
	public static String dataPrecision="N";//精度控制是否启用，

	private String clue_id;  //数据转换任务主线索号（数据转换配置主表PK）

	private ConfigVO config;
//	private TempletVO templet;
	private Hashtable<String, TempletVO>  templet;
//	private RowDataVO[] data;
	
//	private VoucherVO[] voucher;

	private BaseDAO dao;
	public static int success=0;//转换成功的xml数量
	public static int fail=0;//转换失败的xml数量
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
		
		getLogUtil().writToDataLog_RequiresNew(clue_id, IContrastUtil.YWLX_ZH, log);//.writToDataLog_RequiresNew(logVo);
	}
	
	private int formatErr = 0;
	private int checkErr = 0;
	private boolean sysfailed = false;
	
	private List errList = new ArrayList();

//	private Map<String,String> mapunit=new HashMap<String, String>();
	@SuppressWarnings("unchecked")
	public DataChangeProcessor(String clue){
		clue_id = clue;
//		try {
//			List<DipDatachangeBVO> bvos=(List<DipDatachangeBVO>) getDAO().retrieveByClause(DipDatachangeBVO.class, "pk_datachange_h='"+clue+"' and nvl(dr,0)=0");
//			if(bvos!=null&&bvos.size()>0){
//				String pks="";
//				Map<String, String> temp=new HashMap<String, String>();
//				for(DipDatachangeBVO bvo:bvos){
//					pks=pks+"'"+bvo.getOrgcode()+"',";
//					temp.put(bvo.getOrgcode(), bvo.getDef_str_1());
//				}
//				pks=pks.substring(0,pks.length()-1);
//				IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
//				try {
//					List<Map<String,String>> maps=iqf.queryListMapSingleSql("select pk_corp,unitcode from bd_corp where pk_corp in("+pks+")");
//					if(maps!=null&&maps.size()>0){
//						for(int i=0;i<maps.size();i++){
//							Map map=maps.get(i);
//							String corp=map.get("pk_corp".toUpperCase()).toString();
//							String unnitcode=map.get("unitcode".toUpperCase()).toString();
//							mapunit.put(unnitcode, temp.get(corp));
//						}
//					}
//				} catch (BusinessException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (DbException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		} catch (DAOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	public static Hashtable<String,Integer> countmap;//线程计数器
	public RetMessage execute(){
		success=0;
		fail=0;
		String errMsg = "";
		try{
			if(!dataPrepare()){
				
				return new RetMessage(false,"数据解析失败！");

			}
			dataPrecision=config.getParent().getDataprecision()==null?"N":config.getParent().getDataprecision();
			beforPrecision=config.getParent().getBeforeprecision()==null?0:config.getParent().getBeforeprecision();
			afterPrecision=config.getParent().getAfterprecision()==null?0:config.getParent().getAfterprecision();
			if(config.isIsgdzz()){
				List list = (List)getDAO().executeQuery("select * from "+config.getParent().getBusidata()+" where nvl(dr,0)=0", new MapListProcessor());
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
					data[i].setTableName(config.getParent().getBusidata());
					data[i].setPKField(config.getDatadef().get(PKFIELD_ATTRIBUTE)==null?"":config.getDatadef().get(PKFIELD_ATTRIBUTE).getEname());
					data[i].setPrimaryKey((String)data[i].getAttributeValue(data[i].getPKFieldName()));
				}
				TempletVO	tempVo = templet.get(DEFAULT_TEMPLET);
				if(tempVo==null){
					
					return new RetMessage(false,"没有找到对应得固定组织凭证转换模板！");
				}
				VoucherVO[] vos=formatVoucher(null,data);
				if(vos==null||vos.length<=0){
					return new RetMessage(false,"找到模板，没有满足条件的业务数据！");
				}
				vos=voucherCheck(vos);
				vos=voucherUnion(vos);//凭证合并
				vos=afterVoucherChange(vos);//凭证转换后合并。
				vos=detailUnion(vos);//分录合并
				vos=orderByDetailId(vos);
//				if(isReaptDetailUnion){
//					detailUnion(vos);
//				}
//				voucherCheck();
				writeVoucher(vos);
				if(vos!=null&&vos.length==0){
					return new RetMessage(false,"生成xml数量为0");
				}
			}else{
				if(countmap==null){
					countmap=new Hashtable<String, Integer>();
				}
				if(!countmap.containsKey(clue_id)){
					countmap.put(clue_id, 0);
				}
				if(config.getTemptablename()==null||config.getTemptablename().length()<=0){
					return new RetMessage(false,"没有创建成功公司对照临时表");
				}
				String sql="select distinct extepk from "+config.getTemptablename()+" where extepk is not null";
				List pkl=(List) getDAO().executeQuery(sql, new MapListProcessor());
				if(pkl==null||pkl.size()<=0){
					errLogOut("临时表里边没有对应的NC组织");
					throw new DAOException("临时表里边没有对应的NC组织");
				}
				int nodatacont=0;
				int transnum=1;
				String sqlq="select bb.sysvalue from dip_runsys_b bb where bb.syscode='DIP-0000008' and nvl(dr,0)=0";
				List ss=(List)getDAO().executeQuery(sqlq, new MapListProcessor());
				if(ss.size()==1){
					String ss1=((Map)ss.get(0)).get("sysvalue").toString();
					//String ss1=ss.get(0).toString().split("=")[1];	
					String regex="[0-9]+";
					
					if(ss1.matches(regex)){
						int k=Integer.parseInt(ss1);
						if(k>0&&k<=10){
							transnum=k;
						}
					}
				}
				
				
				
				for(int k=0;k<pkl.size();k++){

					SJUtil.PrintLog("d:/aa/a", clue_id, "第K次循环"+k);
						Map mapl=(Map) pkl.get(k);
						String pkcorp=mapl.get("extepk").toString();
						if( !templet.containsKey(pkcorp)&&!templet.containsKey(DEFAULT_TEMPLET)){
							++nodatacont;
							SJUtil.PrintLog("d:/aa/a", clue_id, "公司没有配置或未取到默认模板 nodatacont"+nodatacont);
							errLogOut(pkcorp+"公司没有配置或未取到默认模板");
							continue;
						}
						
//						3、查询业务数据
//						List list = (List)getDAO().executeQuery("select * from "+config.getParent().getBusidata()+" where nvl(dr,0)=0", new MapListProcessor());
						List list = (List)getDAO().executeQuery("Select a.* From  "+config.getParent().getBusidata()+" a, "+config.getTemptablename()+" b where  a."+config.getYWZD()+"=b."+config.getYWZD()+" and extepk='"+pkcorp+"' and nvl(dr,0)=0", new MapListProcessor());
						if(list == null || list.size()<= 0){
							++nodatacont;
							SJUtil.PrintLog("d:/aa/a", clue_id, "公司未找到符合条件的数据 nodatacont"+nodatacont);
							errLogOut(pkcorp+"公司未找到符合条件的数据");
							continue;
						}
					if(countmap.get(clue_id)!=null){
						boolean flag=true;
//						int count=0;
						while(flag){
//							count++;
//							if(count>=3){
//								++nodatacont;
//							}
							if(countmap.get(clue_id)<transnum){
								countmap.put(clue_id, ((Integer)countmap.get(clue_id))+1);
								++nodatacont;
								SJUtil.PrintLog("d:/aa/a", clue_id, "开始计数了 nodatacont"+nodatacont);
								ChangeVouch cvo=new ChangeVouch(list,pkcorp);
								cvo.start();
								flag=false;
							}else{
								flag=false;
								while(countmap.get(clue_id)!=null&&((Integer) countmap.get(clue_id))>=transnum){
	
									SJUtil.PrintLog("d:/aa/a", clue_id, "((Integer) countmap.get(clue_id))"+((Integer) countmap.get(clue_id))+"");
									SJUtil.PrintLog("d:/aa/a", clue_id, "transnum："+transnum);
//									System.out.println("((Integer) countmap.get(clue_id))"+((Integer) countmap.get(clue_id))+"");
//									System.out.println("transnum："+transnum);
									try {
										Thread.sleep(2000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
								flag=true;
							}
						}
					}
				}
				while(((Integer)countmap.get(clue_id)!=0)||(nodatacont<pkl.size())){

					SJUtil.PrintLog("d:/aa/a", clue_id, "wai"+(((Integer)countmap.get(clue_id)!=0)||countmap.get(clue_id)!=null||(nodatacont<pkl.size())));
					SJUtil.PrintLog("d:/aa/a", clue_id,"wai"+(((Integer)countmap.get(clue_id)!=0)||countmap.get(clue_id)!=null||(nodatacont<pkl.size())));
					SJUtil.PrintLog("d:/aa/a", clue_id,"wai nodatacont"+nodatacont);
					SJUtil.PrintLog("d:/aa/a", clue_id,"wai  pkl.size()"+pkl.size());
					SJUtil.PrintLog("d:/aa/a", clue_id,"外((Integer) countmap.get(clue_id))"+((Integer) countmap.get(clue_id))+"");
					try {
						Thread.sleep(4000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				countmap.remove(clue_id);
				
		
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
		
		if(formatErr > 0){
			RetMessage mes=new RetMessage();
			mes.setIssucc(false);
			mes.setMessage("数据转换完成！但有"+formatErr+"条记录在凭证转换时出错");
			mes.setSu(success);
			mes.setFa(fail);
			return mes;
		}
		
		if(checkErr > 0){
			RetMessage mes=new RetMessage();
			mes.setIssucc(false);
			mes.setMessage("数据转换完成！但有"+checkErr+"条记录在凭证校验时出错");
			mes.setSu(success);
			mes.setFa(fail);
			return mes;
		}
		RetMessage mes=new RetMessage();
		mes.setIssucc(true);
		mes.setSu(success);
		mes.setFa(fail);
		return mes;
	}


	/**
	 * 将处理完毕的凭证写入XML文件
	 * @throws IOException 
	 */
	private void writeVoucher(VoucherVO[] voucher) throws IOException {
		if(voucher == null || voucher.length == 0){
			return;
		}
		String pxzd=config.getParent().getPxzd()==null?"N":config.getParent().getPxzd();
		if("Y".equals(pxzd)){
			String prepareddate[]=new String[voucher.length];
			Map<String,VoucherVO> map=new HashMap<String, VoucherVO>();
			for(int m=0;m<voucher.length;m++){
				String date=voucher[m].getParent().getPrepareddate();
				String date1=date+"-"+m;
				map.put(date1, voucher[m]);
				prepareddate[m]=date1;
			}
			VoucherVO[] voucherVo=new VoucherVO[voucher.length];
			Arrays.sort(prepareddate);
			for(int m=0;m<prepareddate.length;m++){
				VoucherVO vo=map.get(prepareddate[m]);
				voucherVo[m]=vo;
			}
			
			voucher=voucherVo;
		}
		
		
		
		
		VoucherVO[] vGrp = new VoucherVO[voucherPerFile];
		int grpcount = 0;
		XMLWriter writer = new XMLWriter(config.getParent().getOutpath());
		
		for(int i=0, j=0;i<voucher.length;i++){
			for(j=i-voucherPerFile*grpcount;j<voucherPerFile;j++){
				vGrp[j] = voucher[i];
			}
			if(j == voucherPerFile){
//				String comp=vGrp[0].getParent().getCompany();
				RetMessage rm=writer.write(vGrp, config.getParent().getDef_str_1().trim(),dataPrecision,afterPrecision);
				SJUtil.PrintLog("d:/aa/a", "","  写凭证XML文件 后返回RetMessage:"+rm);
				if(rm!=null){
					success=rm.getSu()==null?0:success+rm.getSu();
					fail=rm.getFa()==null?0:fail+rm.getFa();
					SJUtil.PrintLog("d:/aa/a", "","  写凭证XML文件 后计数，su:"+DataChangeProcessor.success+" fa:"+fail);
				}
				vGrp = new VoucherVO[voucherPerFile];
				grpcount = grpcount + 1;
			}
		}
		/*if(vGrp != null){
			String comp=vGrp[0].getParent().getCompany();
			writer.write(vGrp,mapunit.get(comp));
		}*/
	}

	/**
	 * 合并凭证分录
	 */
	private VoucherVO[] detailUnion(VoucherVO[] voucher) {
		Integer detailUnion = config.getParent().getResults();
		//默认的合并是按照4个参数相同合并，相同科目+相同辅助核算+相同借贷方向+相同摘要
		//3个参数合并，相同科目+相同辅助核算+相同借贷方向
		//2个参数合并，相同科目+相同辅助核算
		detailUnion = detailUnion == null ? DETAIL_UNION_FOUR : detailUnion;
		
		if(voucher == null || voucher.length == 0){
			return voucher;
		}
		List<VoucherVO> list=new ArrayList<VoucherVO>();
		for(VoucherVO vo: voucher){
			if(!vo.isCheckpass()){
				continue;
			}
			
			Hashtable<String, VoucherDetailVO> detailList = new Hashtable<String, VoucherDetailVO>();
			Hashtable<String, VoucherDetailVO> detailList2 = new Hashtable<String, VoucherDetailVO>();
			for(VoucherDetailVO body: vo.getChildren()){
				String key = body.getAccount_code();
				//相同科目+相同辅助核算
				for(FreeValueVO free: body.getFreevalue()){
					key = key + SEPARATE_TAG + free.getAssValue()/*.trim()*/;
				}
				//如果不是两个参数合并，就加上相同借贷方向
//				if(new Double(body.getNatural_credit_currency())==new Double(0.00)){
//					
//				}
				if(detailUnion != DETAIL_UNION_TWO){
					if(body.getNatural_credit_currency() != null 
							&& !body.getNatural_credit_currency().trim().equals("")
							&& !(new Double(body.getNatural_credit_currency()).doubleValue()==new Double(0.00).doubleValue())){
						key = key + SEPARATE_TAG + "贷";
					}else{
						key = key + SEPARATE_TAG + "借";
					}
				}
				//如果是四个参数合并就加上相同摘要。
				if(detailUnion == DETAIL_UNION_FOUR){
					key = key + SEPARATE_TAG + body.getAbstract_m();
				}
				
				if(detailList.get(key) == null){
					body.setEntry_id(Integer.toString(detailList.size()+1));
					detailList.put(key, body);
				}else{
					VoucherDetailVO detail = detailList.get(key);
//					detail.setCredit_quantity(Integer.toString((Integer.parseInt(detail.getCredit_quantity()) + Integer.parseInt(body.getCredit_quantity()))));
					detail.setPrimary_credit_amount(new UFDouble(detail.getPrimary_credit_amount()).add(new UFDouble(body.getPrimary_credit_amount())).toString());
					detail.setSecondary_credit_amount(new UFDouble(detail.getSecondary_credit_amount()).add(new UFDouble(body.getSecondary_credit_amount())).toString());
					detail.setNatural_credit_currency(new UFDouble(detail.getNatural_credit_currency()).add(new UFDouble(body.getNatural_credit_currency())).toString());
					
//					detail.setDebit_quantity(new UFDouble(detail.getDebit_quantity()).add(new UFDouble(body.getDebit_quantity())).toString());
					detail.setPrimary_debit_amount(new UFDouble(detail.getPrimary_debit_amount()).add(new UFDouble(body.getPrimary_debit_amount())).toString());
					detail.setSecondary_debit_amount(new UFDouble(detail.getSecondary_debit_amount()).add(new UFDouble(body.getSecondary_debit_amount())).toString());
					detail.setNatural_debit_currency(new UFDouble(detail.getNatural_debit_currency()).add(new UFDouble(body.getNatural_debit_currency())).toString());
					//分录抵销  lyz
					  if(detail.getNatural_credit_currency() != null 
								&& !detail.getNatural_credit_currency().trim().equals("")
								&& !(new UFDouble(detail.getNatural_credit_currency()).doubleValue()==new Double(0).doubleValue())
								&&detail.getNatural_debit_currency()!=null
								&&!detail.getNatural_debit_currency().trim().equals("")
								&&!(new UFDouble(detail.getNatural_debit_currency()).doubleValue()==new Double(0).doubleValue())){
						  Double credit=new UFDouble(detail.getNatural_credit_currency()).toDouble();
						  Double debit=new UFDouble(detail.getNatural_debit_currency()).toDouble();
						  
						  if(credit.doubleValue()==debit.doubleValue()){
							  continue;
						  }else{
							  if(credit.doubleValue()>debit.doubleValue()){
								  detail.setPrimary_credit_amount(new UFDouble(detail.getPrimary_credit_amount()).multiply(new UFDouble(detail.getPrimary_debit_amount())).abs().toString());
								  detail.setSecondary_credit_amount(new UFDouble(detail.getSecondary_credit_amount()).multiply(new UFDouble(detail.getSecondary_debit_amount())).abs().toString());
								  detail.setNatural_credit_currency(new UFDouble(detail.getNatural_credit_currency()).multiply(new UFDouble(detail.getNatural_debit_currency())).abs().toString());
							  
								  detail.setPrimary_debit_amount(new UFDouble(0.00).toString());
								  detail.setSecondary_debit_amount(new UFDouble(0.00).toString());
								  detail.setNatural_debit_currency(new UFDouble(0.00).toString());
							  }else{
								  detail.setPrimary_credit_amount(new UFDouble(0.00).toString());
								  detail.setSecondary_credit_amount(new UFDouble(0.00).toString());
								  detail.setNatural_credit_currency(new UFDouble(0.00).toString());
							  
								  detail.setPrimary_debit_amount(new UFDouble(detail.getPrimary_credit_amount()).multiply(new UFDouble(detail.getPrimary_debit_amount())).abs().toString());
								  detail.setSecondary_debit_amount(new UFDouble(detail.getSecondary_credit_amount()).multiply(new UFDouble(detail.getSecondary_debit_amount())).abs().toString());
								  detail.setNatural_debit_currency(new UFDouble(detail.getNatural_credit_currency()).multiply(new UFDouble(detail.getNatural_debit_currency())).abs().toString());
							  }
						  }
					  }
					detailList.put(key, detail);
				}
			}
			vo.setChildren(detailList.values().toArray(new VoucherDetailVO[0]));
			list.add(vo);
		}
		VoucherVO[] vou=list.toArray(new VoucherVO[list.size()]);
		return vou;
	}
//排序分录id号码
	private VoucherVO[] orderByDetailId(VoucherVO[] vos) {
		// TODO Auto-generated method stub
		//先借，后待，然后按照科目排序
		//部门档案0，产品1，责任中心2，客商辅助核算3
		//String paixu="部门档案";//按照部门档案就行排序。
		String paixu=config.getParent().getDef_str_2();
		if(paixu==null){
			paixu="";
		}
//		if(paixu!=null&&paixu.equals("")){
//			return vos;
//		}
	
		for(int j=0;j<vos.length;j++){
			ArrayList<VoucherDetailVO> listCredit=new ArrayList<VoucherDetailVO>();//贷方
			ArrayList<VoucherDetailVO> listDebit=new ArrayList<VoucherDetailVO>();//借方
			VoucherDetailVO detvo[]=vos[j].getChildren();
			//这个for循环是把借贷分开。
			for(int m=0;m<detvo.length;m++){
				if(detvo[m].getNatural_credit_currency() != null 
						&& !detvo[m].getNatural_credit_currency().trim().equals("")
						&& !(new Double(detvo[m].getNatural_credit_currency()).doubleValue()==new Double(0.00).doubleValue())){
				//String	key = "贷";
				listCredit.add(detvo[m]);
				}else{
					//String	key ="借";
					listDebit.add(detvo[m]);
					
				}	
			}
			//对借方进行科目排序。
			listDebit=(ArrayList<VoucherDetailVO>) detailOrderByAccountCode(paixu, listDebit);
			//对贷方进行科目排序
			listCredit=(ArrayList<VoucherDetailVO>) detailOrderByAccountCode(paixu, listCredit);
								
			List<VoucherDetailVO> over=new ArrayList<VoucherDetailVO>();
			for(int p=0;p<listDebit.size();p++){
				VoucherDetailVO vvvv=(VoucherDetailVO)listDebit.get(p);
				vvvv.setEntry_id((p+1)+"");
				over.add(vvvv);
			}
			int w=listDebit.size();
			for(int q=0;q<listCredit.size();q++){
				VoucherDetailVO vvvv=(VoucherDetailVO)listCredit.get(q);
				vvvv.setEntry_id((q+1+w)+"");
				over.add(vvvv);
				
			}
			VoucherDetailVO[] det=over.toArray(new VoucherDetailVO[over.size()]);
			
			vos[j].setChildren(det);
			
		}
		return vos;
	}
	
	private List<VoucherDetailVO> detailOrderByAccountCode(String paixu, ArrayList<VoucherDetailVO> accountCodeList){
		List list=accountCodeList;
		Map mapCredit=new HashMap<String, VoucherDetailVO>();
		Map mapCreditAccountCode=new HashMap<String,ArrayList<VoucherDetailVO>>();//科目编码相同的分录，key是科目编码，value是科目编码相同的所有分录。
		List listCreditAccountCode=new ArrayList<VoucherDetailVO>();
		String strCredit[]=new String[list.size()];
		for(int n=0;n<list.size();n++){
			String accountCode=((VoucherDetailVO)list.get(n)).getAccount_code();//科目编码
			if(mapCredit.get(accountCode)==null){
				mapCredit.put(accountCode, list.get(n));	
				strCredit[n]=accountCode;
			}else{
				 if(mapCreditAccountCode.get(accountCode)==null){
					 List list2=new ArrayList<VoucherDetailVO>();
					 list2.add(mapCredit.get(accountCode));
					 list2.add(list.get(n));
					 mapCreditAccountCode.put(accountCode, list2);
					 strCredit[n]="";
				 }else{
					 ArrayList<VoucherDetailVO> list3=((ArrayList<VoucherDetailVO>) mapCreditAccountCode.get(accountCode));
					 list3.add((VoucherDetailVO) list.get(n));
					 mapCreditAccountCode.put(accountCode,list3);
					 strCredit[n]="";
				 }
			}	
			
		}
		
		List<VoucherDetailVO> rightList=new ArrayList<VoucherDetailVO>();
		Arrays.sort(strCredit);
		//这个科目是按照从小到大排序过的。
		
		for(int i=0;i<strCredit.length;i++){
			if(strCredit[i]!=null&&!strCredit[i].equals("")){
				if(mapCreditAccountCode.get(strCredit[i])==null){
					rightList.add((VoucherDetailVO) mapCredit.get(strCredit[i]));
				}else{
					ArrayList<VoucherDetailVO> array=(ArrayList<VoucherDetailVO>)mapCreditAccountCode.get(strCredit[i]);
					List<VoucherDetailVO> ll=null;
					if(paixu.equals("")){ //不按照相同辅助核算排序
						ll=array;
					}else{//按照辅助核算排序
					    ll=orderBy(paixu, array);
					}
						if(ll!=null&&ll.size()>0){
							for(int m=0;m<ll.size();m++){
								rightList.add(ll.get(m));
							}
						}
					
				}
			}
			
		}
		
		return rightList;
	}
	
	
//	private void detailOrderByAss(String paixu,Map<String,ArrayList<VoucherDetailVO>> map){
//		
////		把科目相同的分录（mapCreditAccountCode），按照选择的排序项（例如部门档案）进行排序。
//		//有以下几种情况，一、科目没有选择排序项。二、科目有选择排序项，也有值。三、科目有选择排序项，但是没有值。
//		Iterator it1=map.keySet().iterator();
//		 List endList=new ArrayList<VoucherDetailVO>();
//		while(it1.hasNext()){
//			String str=(String)it1.next();
//			ArrayList<VoucherDetailVO> strList=new ArrayList<VoucherDetailVO>();
//			if(str!=null){
//				strList=(ArrayList<VoucherDetailVO>) map.get(str);
//				orderBy(paixu, strList);
//				
//			}
//		}
//		
//		
//	}
	//对一个相同科目的所有分录进行按排序项从小到大排列。
	private List<VoucherDetailVO> orderBy(String paixu,ArrayList<VoucherDetailVO> strList){
		List<VoucherDetailVO> list=new ArrayList<VoucherDetailVO>();
		//strList=(ArrayList<VoucherDetailVO>) map.get(str);
			Map<String,VoucherDetailVO> mmmp=new HashMap<String,VoucherDetailVO>();//辅助核算中有排序项目
			List<String> listAssValue=new ArrayList<String>();//辅助核算中有排序项目，就把排序项的值放入list中。
			//List<VoucherDetailVO> listAssNotValue=new ArrayList<VoucherDetailVO>();//辅助核算没有排序项目。
			//List<VoucherDetailVO> listNotAss=new ArrayList<VoucherDetailVO>();//没有辅助核算。
			List<VoucherDetailVO> listValueIsNull=new ArrayList<VoucherDetailVO>();//表示辅助核算项有选择排序的项，但是值是null或者""。
			Map<String,List<VoucherDetailVO>> mp=new HashMap<String,List<VoucherDetailVO>>();//表示辅助核算排序项也相同的分录，也就是要按照摘要去排序的分录。
		 for(int p=0;p<strList.size();p++){
			 VoucherDetailVO vv= strList.get(p);
			 FreeValueVO[] fv=vv.getFreevalue();
			 if(fv!=null){
				 boolean flag=false;//表示是否有排序选择项。
				 boolean valueIsNull=false;//表示辅助核算项有选择排序的项，但是值是null或者""。
				 for(int kk=0;kk<fv.length;kk++){
					  String type=fv[kk].getAssType();
					  if(type!=null){
						  if(type.equals(paixu)){
							  flag=true;
							  String assValue=fv[kk].getAssValue();
							  if(assValue!=null&&!(assValue.equals(""))){
								  
								  if(mmmp.get(assValue)==null){
									  mmmp.put(assValue, vv);
									  listAssValue.add(assValue);  
								  }else{
									  if(mp.get(assValue)==null){
										  List<VoucherDetailVO> listvo=new ArrayList<VoucherDetailVO>();
										  VoucherDetailVO detailvo=mmmp.get(assValue);
										  listvo.add(detailvo);
										  listvo.add(vv);
										  mp.put(assValue, listvo);
									  }else{
										  List<VoucherDetailVO> listvo=mp.get(assValue);
										  listvo.add(vv);
										  mp.put(assValue, listvo);
									  }
									  
//									  mmmp.put(assValue+"0", vv);
//									  listAssValue.add(assValue+"0");
								  }
			
							  }else{
								  valueIsNull=true;
								  list.add(vv);
								  break;
								  
							  }
						  }
					  }
					  		if(kk==fv.length-1&&!flag){//return 辅助核算没有排序项目。
					  			list=strList;
					  			return list;
					  		}
				  } 
				 
			 }else{//没有辅助核算
				 list=strList;
				return list;
			 }
			  
		 }

		 //把辅助核算中有排序项目且有值的，进行排序
		 if(listAssValue!=null&&listAssValue.size()>0){
			 String[] assHaseValue=listAssValue.toArray(new String[listAssValue.size()]);
			 Arrays.sort(assHaseValue);
			  for(int ll=0;ll<assHaseValue.length;ll++){
				  if(mp.get(assHaseValue[ll])!=null){
					  List<VoucherDetailVO> assValueIsEquals=mp.get(assHaseValue[ll]);//辅助核算项相同的分录。
					  String str=config.getParent().getExplanation();
					  if(str!=null&&str.trim().equals("摘要")){//辅助核算项相同的分录,如果按摘要排序项不为空，按照摘要去排序。
						  List<VoucherDetailVO> abc=orderByExplation(assValueIsEquals);
						  if(abc!=null){
							  for(int d=0;d<abc.size();d++){
								  list.add(abc.get(d));
							  }  
						  }
						  
					  }else{
						  
						  for(int b=0;b<assValueIsEquals.size();b++){
							  list.add(assValueIsEquals.get(b));
						  }
					  }
				  }else{
					  VoucherDetailVO mmvo= mmmp.get(assHaseValue[ll]);
					  list.add(mmvo);  
				  }
				  
			  } 
		 }
		 return list;
	
	} 
	//按照摘要进行排序
	private List<VoucherDetailVO> orderByExplation(List<VoucherDetailVO> list){
		Map<String,VoucherDetailVO> map=new HashMap<String,VoucherDetailVO>();//放摘要不相同的分录。
		Map<String,List<VoucherDetailVO>> mapAbstractIsEquals=new HashMap<String,List<VoucherDetailVO>>();//放摘要相同的分录。
		List<VoucherDetailVO> right=new ArrayList<VoucherDetailVO>();
		List<String> tt=new ArrayList<String>();
		for(int i=0;i<list.size();i++){
			VoucherDetailVO vo=list.get(i);
			
			if(vo!=null){
				String str=vo.getAbstract_m()==null?"":vo.getAbstract_m();
				if(map.get(str)==null){
					map.put(str, vo);	
					tt.add(str);
				}else{
					if(mapAbstractIsEquals.get(str)==null){
						List<VoucherDetailVO> ll=new ArrayList<VoucherDetailVO>();
						ll.add(map.get(str));
						ll.add(vo);
						mapAbstractIsEquals.put(str, ll);
					}else{
						List<VoucherDetailVO> ll=new ArrayList<VoucherDetailVO>();
						ll=mapAbstractIsEquals.get(str);
						ll.add(vo);
						mapAbstractIsEquals.put(str, ll);
					}
				}	
				
				
				
			}
		}
		
		if(tt.size()==0){
			return right;
		}else{
			String st[]=tt.toArray(new String[tt.size()]);
			Arrays.sort(st);
			for(int k=0;k<st.length;k++){
				if(map.get(st[k])!=null){
					if(mapAbstractIsEquals.get(st[k])==null){//摘要相同的就一个分录
					  right.add(map.get(st[k]));	
					}else{
						List<VoucherDetailVO> gg=mapAbstractIsEquals.get(st[k]);
						for(int m=0;m<gg.size();m++){
							right.add(gg.get(m));
						}
					}	
				}
				
			}
		}
		
		
		return right;
	}
	
	/**
	 * 合并凭证
	 */
	private VoucherVO[] voucherUnion(VoucherVO[] voucher) {
		ArrayList<VoucherVO> rstList = new ArrayList<VoucherVO>();
		if(config.getCombine()==null){
			return voucher;
		}
		String[] fields = config.getCombine().values().toArray(new String[0]);
		if(fields!=null&&fields.length==0){
			return voucher;
		}
		Hashtable<String, List<VoucherVO>> grp = new Hashtable<String, List<VoucherVO>>();
		for(int i=0;i<voucher.length;i++){
			if(!voucher[i].isCheckpass()){
				rstList.add(voucher[i]);
				continue;
			}
			
			RowDataVO rowVo = voucher[i].getSrcVo()[0];
			StringBuilder key = new StringBuilder();
			for(int j=0;j<fields.length;j++){
				Object value = rowVo.getAttributeValue(fields[j].toLowerCase());
				if(value == null){
					value = "";
				}
				
				key.append(value.toString());
			}
			
			if(grp.get(key.toString()) == null){
				grp.put(key.toString(), new ArrayList<VoucherVO>());
			}
			
			List<VoucherVO> list = grp.get(key.toString());
			list.add(voucher[i]);
			grp.put(key.toString(), list);
		}
		
		//执行合并
		String[] keys = grp.keySet().toArray(new String[0]);
		for(int i=0;i<grp.size();i++){
			if(grp.get(keys[i]) == null || grp.get(keys[i]).size() == 0){
				continue;//没有凭证
			}
			
			List<VoucherVO> list = grp.get(keys[i]);
			VoucherVO vouch = new VoucherVO();
			vouch.setCheckpass(true);
			vouch.setGlorgbookcode(list.get(0).getGlorgbookcode());
			vouch.setParent(list.get(0).getParent());
			
			ArrayList<RowDataVO> srcVOs = new ArrayList<RowDataVO>();
			ArrayList<VoucherDetailVO> details = new ArrayList<VoucherDetailVO>();
			for(int j=0;j<list.size();j++){
				srcVOs.add(list.get(j).getSrcVo()[0]);
				details.addAll(Arrays.asList(list.get(j).getChildren()));
			}
			
			vouch.setSrcVo(srcVOs.toArray(new RowDataVO[0]));
			vouch.setChildren(details.toArray(new VoucherDetailVO[0]));
			
			vouch.setId(list.get(0).getId());
			
			rstList.add(vouch);
		}
		
		voucher = rstList.toArray(new VoucherVO[0]);
		return voucher;
	}
	
	
	/**
	 * 0表示相同公司+凭证类别
	 * 1表示相同公司+凭证类别+相同制单人
	 * @param vos
	 */
		private VoucherVO[] afterVoucherChange(VoucherVO[] vos) {
			// TODO Auto-generated method stub
			int k=config.getParent().getSfhb()==null?-1:config.getParent().getSfhb();
			if(k==0){
				//map中key存放的是相同公司+凭证类别，value中存放的是i表示要把那几张凭证合并，如果把第二和三张合并，则value是2,3。如果不需要合并的话就是一个值2。
				HashMap<String,String> map=new HashMap<String,String>();
				Set<String> set=new HashSet<String>();
				Set<Integer> intset=new HashSet<Integer>();//存放的是所有需要合并的凭证的数组下标。
				List<VoucherVO> voucherList=new ArrayList<VoucherVO>();//存放合并后的voucher。
				List<VoucherVO> voucherList2=new ArrayList<VoucherVO>();//存放合并后的所有的最后需要生成的xml的凭证。
				for(int i=0;i<vos.length;i++){
					if(vos[i].isCheckpass()){
						VoucherHVO hvo=vos[i].getParent();
						VoucherDetailVO dvo[]=vos[i].getChildren();
						String key=hvo.getCompany()+hvo.getVoucher_type();//
						String value=i+"";
						if(map.get(key)!=null){
							String ss=map.get(key);
							ss=ss+","+i;
							set.add(key);
							map.put(key, ss);
						}else{
								map.put(key,value);
						}
					}
					
				}
				
				Iterator<String> it=set.iterator();
				//list中的每个元素表示要把那几个凭证合并到一起。
				List<String[]> list=new ArrayList<String[]>();
				while(it.hasNext()){
					String str=map.get(it.next());
					if(str.contains(",")){
						String[] union= str.split(","); 
						list.add(union);
					}
				}
				
				for(int i=0;i<list.size();i++){
					String s1[]=list.get(i);
					List<Integer> intlist=new ArrayList<Integer>();
					for(int j=0;j<s1.length;j++){
						int m=Integer.parseInt(s1[j]);
						intlist.add(m);
						intset.add(m);//
						
					}
					VoucherVO voucher=voucherUnionByCondition(intlist, vos);
					voucherList.add(voucher);
				}
				
				Iterator<Integer> itset=intset.iterator();
				while(itset.hasNext()){
					int ss=itset.next();
					vos[ss]=null;
				}
			 
				for(int i=0;i<vos.length;i++){
					if(vos[i]!=null){
						voucherList2.add(vos[i]);
					}
				}
				
				for(int i=0;i<voucherList.size();i++){
					voucherList2.add(voucherList.get(i));
				}
				
				VoucherVO vou[]=voucherList2.toArray(new VoucherVO[voucherList2.size()]);
				return vou;
			}else if(k==1){
				//map中key存放的是相同公司+凭证类别，value中存放的是i表示要把那几张凭证合并，如果把第二和三张合并，则value是2,3。如果不需要合并的话就是一个值2。
				HashMap<String,String> map=new HashMap<String,String>();
				Set<String> set=new HashSet<String>();
				Set<Integer> intset=new HashSet<Integer>();//存放的是所有需要合并的凭证的数组下标。
				List<VoucherVO> voucherList=new ArrayList<VoucherVO>();//存放合并后的voucher。
				List<VoucherVO> voucherList2=new ArrayList<VoucherVO>();//存放合并后的所有的最后需要生成的xml的凭证。
				for(int i=0;i<vos.length;i++){
					if(vos[i].isCheckpass()){
						VoucherHVO hvo=vos[i].getParent();
						VoucherDetailVO dvo[]=vos[i].getChildren();
						String key=hvo.getCompany()+hvo.getVoucher_type()+hvo.getEnter();//
						String value=i+"";
						if(map.get(key)!=null){
							String ss=map.get(key);
							ss=ss+","+i;
							set.add(key);
							map.put(key, ss);
						}else{
								map.put(key,value);
						}
					}
					
				}
				
				Iterator<String> it=set.iterator();
				//list中的每个元素表示要把那几个凭证合并到一起。
				List<String[]> list=new ArrayList<String[]>();
				while(it.hasNext()){
					String str=map.get(it.next());
					if(str.contains(",")){
						String[] union= str.split(","); 
						list.add(union);
					}
				}
				
				for(int i=0;i<list.size();i++){
					String s1[]=list.get(i);
					List<Integer> intlist=new ArrayList<Integer>();
					for(int j=0;j<s1.length;j++){
						int m=Integer.parseInt(s1[j]);
						intlist.add(m);
						intset.add(m);//
						
					}
					VoucherVO voucher=voucherUnionByCondition(intlist, vos);
					voucherList.add(voucher);
				}
				
				Iterator<Integer> itset=intset.iterator();
				while(itset.hasNext()){
					int ss=itset.next();
					vos[ss]=null;
				}
			 
				for(int i=0;i<vos.length;i++){
					if(vos[i]!=null){
						voucherList2.add(vos[i]);
					}
				}
				
				for(int i=0;i<voucherList.size();i++){
					voucherList2.add(voucherList.get(i));
				}
				
				VoucherVO vou[]=voucherList2.toArray(new VoucherVO[voucherList2.size()]);
				return vou;
			}else{
				return vos;
			}
			
			
			
		}
	
		//转换生成凭证后，按照相同公司相同类别凭证去合并凭证。
		private VoucherVO voucherUnionByCondition(List<Integer> list,VoucherVO[] vo){
			VoucherVO voucherVo=new VoucherVO();
			List<VoucherDetailVO> detailVo=new ArrayList<VoucherDetailVO>();
			List<VoucherDetailVO> detailVo1=new ArrayList<VoucherDetailVO>();
			for(int i=0;i<list.size();i++){
				int s=list.get(i);
				if(i==0){
					voucherVo=vo[s];
				}
				VoucherDetailVO[] dd=vo[s].getChildren();
				for(int j=0;j<dd.length;j++){
					detailVo.add(dd[j]);
				}
				
				
			}
			for(int i=0;i<detailVo.size();i++){
				VoucherDetailVO vv=detailVo.get(i);
				vv.setEntry_id((i+1)+"");
				detailVo1.add(vv);
			}
			voucherVo.setChildren(detailVo1.toArray(new VoucherDetailVO[detailVo.size()]));
			return voucherVo;
		}
		

	/**
	 * 凭证校验
	 */
	@SuppressWarnings("unchecked")
	private VoucherVO[] voucherCheck(VoucherVO[] voucher) throws Exception{
		
		List<VoucherVO> voucherList=new ArrayList<VoucherVO>();
		if(voucher == null || voucher.length == 0){
			throw new Exception("没有有效的凭证可以处理");
		}
		
		List<DataverifyBVO> list = SJUtil.getCheckClassName(clue_id, null);
		if(list == null || list.size() == 0){
			if(voucher!=null&&voucher.length>0){
				for(VoucherVO v:voucher){
					v.setCheckpass(true);
				}
			}
			return voucher;
		}
		
		for(DataverifyBVO bvo : list){	
			if(voucher.length <= errList.size()){
				break;
			}
			try {
				String clsname = bvo.getName();
				Class cls = Class.forName(clsname);
				Object inst = cls.newInstance();
				if(inst instanceof ICheckPlugin){
					boolean shutdown = false;
					if(bvo.getVector().contains("终止")){
						shutdown = true;
					}
				    ICheckPlugin plugin = (ICheckPlugin)inst;
				    CheckMessage msg = plugin.doCheck(voucher, shutdown);
				    if(msg != null && msg.getErrList() != null){
				    	errList.addAll(msg.getErrList());				    
				    }
				    voucher = (VoucherVO[])msg.getValidData();
				    
				    if(msg.getErrList() != null && msg.getErrList().size() > 0){
				    	errLogOut("插件类<" + bvo.getName() + ">执行完毕： 【全部】 "+voucher.length+"  【错误】 "+msg.getErrList().size()+"  【正确】 "+(voucher.length - msg.getErrList().size()));
				    }else{
				    	errLogOut("插件类<" + bvo.getName() + ">执行完毕： 全部通过");
				    }
				}
			} catch (ClassNotFoundException e) {
				errLogOut("ClassNotFoundException: 未找到校验插件类<" + bvo.getName() + ">");
				e.printStackTrace();
			} catch (InstantiationException e) {
				errLogOut("InstantiationException: 不能实例化插件类<" + bvo.getName() + ">");
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				errLogOut("IllegalAccessException: 无法构造插件类实例<" + bvo.getName() + ">");
				e.printStackTrace();
			}
		}
		
		checkErr = errList.size();
		return voucher;
	}

	/**
	 * 制作凭证
	 */
	private VoucherVO[] formatVoucher(String pkcorp,RowDataVO[] data) {
		VoucherGenerator gen = new VoucherGenerator(config);
		ArrayList<VoucherVO> list = new ArrayList<VoucherVO>();
		for (int i = 0; i < data.length; i++) {
			try {
				TempletVO tempVo;
				if (!config.isIsgdzz()) {
					/*if(config.getParent().getOrg()==null){
						throw new Exception("组织字段为空！ ");
					}
					String keytem = (String)data[i].getAttributeValue(config.getParent().getOrg().toLowerCase());
					String tempKey = config.getOrgMap().get(keytem==null?"":keytem.trim());
					if(tempKey == null){
						throw new Exception("非法的组织信息 或 组织对照没有配置: " + keytem);
					}*/
					tempVo = templet.get(pkcorp);
					if(tempVo == null){
						tempVo = templet.get(DEFAULT_TEMPLET);
					}
				}else{
					tempVo = templet.get(DEFAULT_TEMPLET);
				}
				
				if(tempVo == null){  //在多次取默认模板处理后依然无模板的，如何处理？
					errLogOut(config.isIsgdzz()?"固定组织未配置或无法取得默认模板":pkcorp+"公司没有配置或未取到默认模板");
					return null;
//					throw new Exception("未配置或无法取得默认模板");
				}
				//String datePrecison=config.getParent().getDataprecision();
				//int cc=config.getParent().getBeforeprecision()==null?0:config.getParent().getBeforeprecision();
				VoucherVO vo = gen.crtVoucher(data[i], tempVo,pkcorp);
				if(vo != null){
					list.add(vo);
				}
			} catch (Exception e) {
				e.printStackTrace();
				formatErr = formatErr + 1;
				errLogOut("生成凭证缓存时出现异常："+e.getMessage());
				return null;
				//continue;
			}
		}	
		return list.toArray(new VoucherVO[0]);
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
		config = f.getConfigVO(clue_id);
		if(config==null){
			return false;
		}
		//2、取得公司对照模板的map
		templet = f.getBillTemplet(clue_id);//返回的是<组织，TempletVO（CredenceHVO，CredenceBVO[]）>
		if(templet==null||templet.size()<=0){
			errLogOut("未找到可用的模板定义");
			return false;
		}
		
		return true;
	}
	class ChangeVouch extends Thread{
		public ChangeVouch(){
			
		}
		List list;
		String pkcorp;
		public ChangeVouch(List list,String pkcorp){
			super();
			this.pkcorp=pkcorp;
			this.list=list;
		}
		@Override
		public void run() {
			try{
				RowDataVO[] data;
				data = new RowDataVO[list.size()];
				for(int i=0;i<list.size();i++){
					Map map = (Map)list.get(i);
					data[i] = new RowDataVO();
					String[] fields = (String[])map.keySet().toArray(new String[0]);
					for(int j=0;j<fields.length;j++){
						if (!fields[j].equals("ts") && !fields[j].equals("dr")) {
							
							String field = fields[j];
							Object value=null;
							if(map.get(fields[j])!=null){
								 value= ValueTranslator.translate(
										map.get(fields[j])==null?null:map.get(fields[j]).toString(), 
										config.getDatadef().get(fields[j].toUpperCase()).getType(),
										config.getDatadef().get(fields[j].toUpperCase()).getLength() == null?0: config.getDatadef().get(fields[j].toUpperCase()).getLength());	
							}else{
							value="";
							}
							
							if(value instanceof String){
								value=value.toString().trim();
							}
							data[i].setAttributeValue(field, value);
							System.out.println("[FIELD] " + field + "  [VALUE]" + value);
						}	
					}
					data[i].setTableName(config.getParent().getBusidata());
					data[i].setPKField(config.getDatadef().get(PKFIELD_ATTRIBUTE)==null?"":config.getDatadef().get(PKFIELD_ATTRIBUTE).getEname());
					data[i].setPrimaryKey((String)data[i].getAttributeValue(data[i].getPKFieldName()));
				}
				VoucherVO[] vos=formatVoucher(pkcorp,data);
				if(vos==null||vos.length<=0){
					SJUtil.PrintLog("d:/aa/a", clue_id,"进到线程里，转换接数，成功数据为0");
					countmap.put(clue_id, ((Integer) countmap.get(clue_id)-1));
					return;
	//				continue;
				}
				try {
					vos=voucherCheck(vos);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					SJUtil.PrintLog("d:/aa/a", clue_id,"进到线程里，校验"+pkcorp+"公司数据失败"+e.getMessage());
					errLogOut("校验"+pkcorp+"公司数据失败"+e.getMessage());
					countmap.put(clue_id, ((Integer) countmap.get(clue_id)-1));
					SJUtil.PrintLog("d:/aa/a", clue_id,"进到线程里，校验"+pkcorp+"公司数据失败，计数器减一返回");
					return;
				}
	//			System.out.println("数据校验");
					vos=voucherUnion(vos);//凭证合并
					SJUtil.PrintLog("d:/aa/a", clue_id,"进到线程里，合并凭证");
					vos=afterVoucherChange(vos);
					vos=detailUnion(vos);//分录合并
					SJUtil.PrintLog("d:/aa/a", clue_id,"进到线程里，分录合并");
					vos=orderByDetailId(vos);//排序分录id
					SJUtil.PrintLog("d:/aa/a", clue_id,"进到线程里，排序分录");
					
			//			voucherCheck();
				try{
					writeVoucher(vos);
	
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					SJUtil.PrintLog("d:/aa/a", clue_id,"进到线程里，写"+pkcorp+"公司数据失败"+e.getMessage());
					errLogOut("写"+pkcorp+"公司数据失败"+e.getMessage());
				}
	
	//			countmap.put(clue_id, ((Integer) countmap.get(clue_id)-1));
			}catch(Exception e){
				e.printStackTrace();
				SJUtil.PrintLog("d:/aa/a", clue_id,"进到线程里，执行线程失败"+e.getMessage());
				errLogOut("执行线程失败"+e.getMessage());
			}

			SJUtil.PrintLog("d:/aa/a", clue_id,"进到线程里，执行完了");
			countmap.put(clue_id, ((Integer) countmap.get(clue_id)-1));
			SJUtil.PrintLog("d:/aa/a", clue_id,"进到线程里，执行完了，计数器减一");
		}
		
	}
}
