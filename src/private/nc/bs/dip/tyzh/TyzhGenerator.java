package nc.bs.dip.tyzh;

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
import nc.vo.dip.tyzhq.ConfigVO;
import nc.vo.dip.tyzhq.DipTYZHDatachangeBVO;
import nc.vo.dip.voucher.FreeValueVO;
import nc.vo.dip.voucher.TempletVO;
import nc.vo.dip.voucher.VoucherDetailVO;
import nc.vo.dip.voucher.VoucherHVO;
import nc.vo.dip.voucher.VoucherVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class TyzhGenerator {
	public static final String DATE = "UFDate";
	public static final String STRING = "String";
	public static final String INTEGER = "Integer";
	public static final String DOUBLE = "UFDouble";
	public static final String BOOLEAN = "UFBoolean";

	public static final UFDouble ZERO_DOUBLE = new UFDouble(0);
	public static final UFDouble ONE_DOUBLE = new UFDouble(1);

	private ConfigVO config;

	public TyzhGenerator(ConfigVO vo){
		config = vo;
	}

	public RowDataVO crtVoucher(RowDataVO vo, List<DipTYZHDatachangeBVO> t_detail) throws Exception{
		ArrayList<DipTYZHDatachangeBVO> remain = new ArrayList<DipTYZHDatachangeBVO>();
			for(int i=0;i<t_detail.size();i++){
				if(t_detail.get(i).getContrl() == null || t_detail.get(i).getContrl().trim().equals("")){
					remain.add(t_detail.get(i));
				}else{
					String ctrl = calcValue(null,vo, t_detail.get(i).getContrl(), BOOLEAN);
					if(ctrl != null && !ctrl.equals("")){
						boolean match = new UFBoolean(ctrl).booleanValue();
						if(match){
							remain.add(t_detail.get(i));
						}
					}else{
						remain.add(t_detail.get(i));
					}
				}
			}
			RowDataVO bvo = crtDetail(vo, remain.toArray(new DipTYZHDatachangeBVO[remain.size()]));
		if(bvo == null){
			return null;
		}
		return bvo;
	}

	private RowDataVO crtDetail(RowDataVO vo, DipTYZHDatachangeBVO[] models) throws Exception {
		RowDataVO svo=new RowDataVO();
		for(int i=0;i<models.length;i++){
			DipTYZHDatachangeBVO bvo=models[i];
			if(svo.getAttributeValue(bvo.getYename())==null){
				svo.setAttributeValue(bvo.getYename(), calcValue(svo,vo, models[i].getChangformu(), STRING));
			}
		}
		String[] s=svo.getAttributeNames();
		boolean isrete=false;
		if(s!=null&&s.length>0){
			for(int i=0;i<s.length;i++){
				if(svo.getAttributeValue(s[i])!=null){
					isrete=true;
				}
			}
		}
		if(isrete){
			return svo;
		}else{
			return null;
		}
	}

	IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());

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

	private String calcValue(RowDataVO tvo,RowDataVO vo, String formulas, String v_type){
//		formulas = formulas == null ? "":formulas;
		Object o = (tvo==null?YzFormulaParse.getFormulaCal(vo, formulas):YzFormulaParse.getFormulaCal(tvo,vo, formulas));
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
