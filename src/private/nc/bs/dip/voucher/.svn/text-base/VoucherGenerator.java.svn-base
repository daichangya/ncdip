package nc.bs.dip.voucher;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import nc.bs.dip.fun.YzFormulaParse;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.server.Module;
import nc.itf.dip.pub.IQueryField;
import nc.vo.dip.credence.CredenceBVO;
import nc.vo.dip.credence.CredenceHVO;
import nc.vo.dip.in.RowDataVO;
import nc.vo.dip.voucher.ConfigVO;
import nc.vo.dip.voucher.FreeValueVO;
import nc.vo.dip.voucher.TempletVO;
import nc.vo.dip.voucher.VoucherDetailVO;
import nc.vo.dip.voucher.VoucherHVO;
import nc.vo.dip.voucher.VoucherVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class VoucherGenerator {
	public static final String DATE = "UFDate";
	public static final String STRING = "String";
	public static final String INTEGER = "Integer";
	public static final String DOUBLE = "UFDouble";
	public static final String BOOLEAN = "UFBoolean";

	public static final UFDouble ZERO_DOUBLE = new UFDouble(0);
	public static final UFDouble ONE_DOUBLE = new UFDouble(1);

	private ConfigVO config;

	public VoucherGenerator(ConfigVO vo){
		config = vo;
	}

	public VoucherVO crtVoucher(RowDataVO vo, TempletVO templet,String pkcorp) throws Exception{
		CredenceBVO[] t_detail = templet.getChildren();
		ArrayList<CredenceBVO> remain = new ArrayList<CredenceBVO>();
		if(config.getInfluence()!=null && config.getInfluence().size() > 0){
			String[] field = config.getInfluence().keySet().toArray(new String[0]);
			for(int i=0;i<t_detail.length;i++){
				boolean match = true;
				for(int j=0;j<field.length;j++){
					if (t_detail[i].getAttributeValue(field[j]) != null) {
						Object parsedValue = t_detail[i].getAttributeValue(field[j]);
						//if(parsedValue!=null&&parsedValue.toString()!=""&&parsedValue.toString().length()>0){
							match = match(vo.getAttributeValue(config.getInfluence()
									.get(field[j]).toLowerCase()), parsedValue);
							if (!match) {
								break;
							}
						//}
						
					}				
				}

				if(match){
					if(t_detail[i].getCtrl() == null || t_detail[i].getCtrl().trim().equals("")){
						t_detail[i].setCtrl("\"1.00\"");//true;
					}
					String ctrl = calcValue(vo, t_detail[i].getCtrl(), BOOLEAN);
					if(ctrl != null && !ctrl.equals("")){
						try{
							match = new UFBoolean(ctrl).booleanValue();
							if(match){
								remain.add(t_detail[i]);
							}
						}catch (Exception e) {
							throw new Exception("");
						}
					}else{
						remain.add(t_detail[i]);
					}
				}

//				if(match){  //符合影响因素条件的加入remain
//				remain.add(t_detail[i]);
//				}
			}
		}else{
			for(int i=0;i<t_detail.length;i++){
				if(t_detail[i].getCtrl() == null || t_detail[i].getCtrl().trim().equals("")){
					t_detail[i].setCtrl("\"1.00\"");
				}
				String ctrl = calcValue(vo, t_detail[i].getCtrl(), BOOLEAN);
				if(ctrl != null && !ctrl.equals("")){
					try{
						boolean match = new UFBoolean(ctrl).booleanValue();
						if(match){
							remain.add(t_detail[i]);
						}
					}catch (Exception e) {
						throw new Exception("");
					}
				}else{
					remain.add(t_detail[i]);
				}
			}
		}
		VoucherHVO hvo = crtVoucherHead(vo, templet.getParent(),pkcorp);
		VoucherDetailVO[] bvo = crtDetail(vo, remain.toArray(new CredenceBVO[remain.size()]));
		if(bvo == null || bvo.length == 0){
			return null;
		}
		VoucherVO voucher = new VoucherVO();

		String pk_corp = "";
		if(!config.isIsgdzz()){
			pk_corp = templet.getParent().getUnit();
		}else{
			pk_corp=pkcorp;
//			String keytem = (String)vo.getAttributeValue(config.getParent().getOrg().toLowerCase());  //用组织字段找原系统组织
//			pk_corp = config.getOrgMap().get(keytem == null?"":keytem.trim());
		}

		String glorgbookcode = iqf.queryfield("select glorgbookcode from bd_glorgbook where pk_glbook='"+config.getParent().getPk_glorg()+"' and pk_glorg=(select pk_glorg from bd_glorg where pk_entityorg='"+pk_corp+"')");
		voucher.setGlorgbookcode(glorgbookcode);
		voucher.setSrcVo(new RowDataVO[]{vo});
		voucher.setParent(hvo);
		voucher.setChildren(bvo);

		voucher.setId(vo.getPrimaryKey());
		voucher.setCheckpass(true);
		return voucher;
	}

	private VoucherDetailVO[] crtDetail(RowDataVO vo, CredenceBVO[] models) throws Exception {
		String ss=DataChangeProcessor.dataPrecision;//表示是否启用精度控制
		boolean isStar=false;
		int count=0;
		if(ss!=null&&!"".equals(ss)){
			if("Y".equals(ss)){
				isStar=true;
				count=DataChangeProcessor.beforPrecision;
			}
		}
		//转换前的。表示按照几位小数位去四舍五入。
		VoucherDetailVO[] detail = new VoucherDetailVO[models.length];
		List<VoucherDetailVO> list=new ArrayList<VoucherDetailVO>();
		int j=1;
		for(int i=0;i<detail.length;i++){
			String money=null;
			if(models[i].getMoney()==null||"".equals(models[i].getMoney())){
				money="0";
			}else{
			    money=calcValue(vo, models[i].getMoney(), DOUBLE);
			    if(isStar){
			    	money=round(money,count);
			    }
			}
			if(money!=null){
				Double d=new Double(money);
				double b=d.doubleValue();
				if(b==0){
					continue;
				}
			}
			
			if(money==null||money.length()<=0||money.equals("0")){
				continue;
			}
			detail[i] = new VoucherDetailVO();
			detail[i].setEntry_id(j+++"");
			detail[i].setAbstract_m(calcValue(vo, models[i].getSummary(), STRING));
//			detail[i].setAccount_code(calcValue(vo, models[i].getSubjects(), STRING));
			detail[i].setAccount_code( models[i].getSubjects());
			
			detail[i].setCurrency(calcValue(vo, models[i].getCurrency(), STRING));
			String balance = models[i].getDirection();
			//detail[i].setFree1();
			if(balance.equals("借")){
				if(models[i].getNumbers()!=null&&models[i].getNumbers().length()>0){
					detail[i].setDebit_quantity(calcValue(vo, models[i].getNumbers(), STRING));
				}
				
				detail[i].setPrimary_debit_amount(money);
				detail[i].setSecondary_debit_amount("0.00");
				detail[i].setNatural_debit_currency(money);

				detail[i].setCredit_quantity("0");
				detail[i].setPrimary_credit_amount("0.00");
				detail[i].setNatural_credit_currency("0.00");
				detail[i].setSecondary_credit_amount("0.00");
			}else{
				if(models[i].getNumbers()!=null&&models[i].getNumbers().length()>0){
					detail[i].setCredit_quantity(calcValue(vo, models[i].getNumbers(), STRING));	
				}
				
				detail[i].setPrimary_credit_amount(money);
				detail[i].setNatural_credit_currency(money);
				detail[i].setSecondary_credit_amount("0.00");

				detail[i].setDebit_quantity("0");
				detail[i].setPrimary_debit_amount("0.00");
				detail[i].setSecondary_debit_amount("0.00");
				detail[i].setNatural_debit_currency("0.00");
			}

			String assStr = calcValue(vo, models[i].getAssistant(), STRING);
			if(assStr.trim().equals("")){
				continue;
			}
			assStr = assStr.replace("，", ",");
			assStr = assStr.replace("：", ":");
			assStr = assStr.trim();

			String[] assGrp = assStr.split(",");
			ArrayList<FreeValueVO> freelist = new ArrayList<FreeValueVO>();
			for(String ass: assGrp){
				FreeValueVO free = new FreeValueVO();
				if(ass.split(":").length != 2){
					free.setAssType(ass.split(":")[0]);
					freelist.add(free);
				}else{
					free.setAssType(ass.split(":")[0]);
					free.setAssValue(ass.split(":")[1]);
					freelist.add(free);
				}
			}
			detail[i].setFreevalue(freelist.toArray(new FreeValueVO[0]));
			if(models[i].getVerificationno()!=null&&models[i].getVerificationno().length()>0){
				detail[i].setFree1(calcValue(vo, models[i].getVerificationno(), STRING));
			}
			
			list.add(detail[i]);
		}
		if(list!=null&&list.size()>0){
			return list.toArray(new VoucherDetailVO[list.size()]);
		}else{
			return null;
		}
	}

	IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	private VoucherHVO crtVoucherHead(RowDataVO vo, CredenceHVO parent,String pkcorp) throws Exception{
		VoucherHVO hvo = new VoucherHVO();

		String pk_corp = "";
		if(config.getParent().getGuding() != null && config.getParent().getGuding().equals("Y")){
			pk_corp = parent.getUnit();
		}else{
			pk_corp=pkcorp;
//			String keytem = (String)vo.getAttributeValue(config.getParent().getOrg().toLowerCase());  //用组织字段找原系统组织
//			pk_corp = config.getOrgMap().get(keytem == null?"":keytem.trim());
			if(pk_corp == null){
				throw new Exception("非法的组织信息 或 组织对照没有配置");
			}
		}

		String unitcode = "";
		try {
			unitcode = iqf.queryfield("select unitcode from bd_corp where pk_corp='"+pk_corp+"'");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("无法找到公司编码，查找的公司主键为："+pk_corp);
		}
		
		hvo.setCompany(unitcode);
		hvo.setVoucher_type(calcValue(vo, parent.getCredtype(), STRING));	
		
		if(parent.getAttmentnum()!=null&&parent.getAttmentnum().length()>0){
			hvo.setAttachment_number(calcValue(vo, parent.getAttmentnum(), INTEGER));	
		}
		
		hvo.setEnter(calcValue(vo, parent.getVoperatorid(), STRING));
		hvo.setPrepareddate(calcValue(vo, parent.getDoperatordate(), DATE));
		if(hvo.getPrepareddate()!=null&&hvo.getPrepareddate().length()>7){
		hvo.setFiscal_year(hvo.getPrepareddate().substring(0, 4));
		hvo.setAccounting_period(hvo.getPrepareddate().substring(5, 7));
		}
		//hvo.setPosting_date(hvo.getPrepareddate());
		hvo.setVoucher_id("0");
		if(parent.getVdef1()!=null&&parent.getVdef1().length()>0){
			hvo.setVoucherkind(calcValue(vo, parent.getVdef1(), STRING));	
		}
		

		return hvo;
	}

	/**
	 * 检查data是否在valueScope中或者相等
	 * @param data - 业务数据的对应值
	 * @param valueScope - 影响因素包含的所有值
	 * @return
	 */
	private boolean match(Object data, Object valueScope) {
		if(data!=null){
			if(valueScope!=null&&!valueScope.toString().equals("")&&valueScope.toString().length()>0){
				String str=valueScope.toString();
				str=str.replaceAll("\"", "");
				if(str.contains("|")){
					str=str.replace("|", "-");
					String ss[]=str.split("--");
					for(int i=0;i<ss.length;i++){
						String aa=ss[i];
						if(aa!=null&&!aa.equals("")&&aa.length()>0){
							if(aa.equals(data.toString())){
								return true;
							}
						}
					}	
				}else{
					if(str.equals(data.toString())){
						return true;
					}
				}
			}
	      }
		return false;
	}

	private String calcValue(RowDataVO vo, String formulas, String v_type){
		if(formulas==null){
			return "";
		}
		Object o = YzFormulaParse.getFormulaCal(vo, formulas);
		if(o == null){
			o = "";
		}
		if(o.toString().trim().equals("")){
			if(v_type.equals(INTEGER)){
				o = "0";
			}
			if(v_type.equals(DOUBLE)){
				o = "0.00";
			}
		}
		if(v_type.equals(STRING)){
			return o.toString();
		}else if(v_type.equals(DATE)){
			try{
				if(o instanceof String){
					return o.toString();
				}
				UFDate date = (UFDate)o;
				return date.toString();
			}catch(Exception e){
				e.printStackTrace();
			}
			String date = (String)o;
			date = UFDate.getValidUFDateString(date).trim();
			return date;
		}else if(v_type.equals(INTEGER)){
			if(o instanceof Integer){
				return Integer.toString((Integer)o);
			}else{
				return o.toString();
			}
		}else if(v_type.equals(DOUBLE)){
			if(o instanceof Double){
				return new UFDouble((Double)o).toString();
			}else if(o instanceof UFDouble){
				return ((UFDouble)o).toString();
			}else if(o instanceof Integer){
				return new UFDouble((Integer)o).toString();
			}else{
				return o.toString();
			}
		}else if(v_type.equals(BOOLEAN)){
			if(o.toString().contains(".")){
				UFDouble tmp = new UFDouble(o.toString());
				if(tmp.compareTo(ZERO_DOUBLE) == 0){
					return "N";
				}else{
					return "Y";
				}
			}
			if(new UFBoolean(o.toString()).booleanValue()){
				return "Y";
			}else{
				return "N";
			}
		}
		return "";
	}
	/**
	 * 四舍五入方法。按照指定的小数位数去四舍五入。
	 * @param value
	 * @param count
	 * @return
	 */
	private String round(String value, int count){
		if(count<=0||count>8){
			return value;
		}
		BigDecimal b = new BigDecimal(value);
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, count, BigDecimal.ROUND_HALF_UP).toString();	
	}
	
}
