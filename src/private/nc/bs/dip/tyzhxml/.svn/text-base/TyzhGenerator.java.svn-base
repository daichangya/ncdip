package nc.bs.dip.tyzhxml;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import nc.bs.dip.fun.YzFormulaParse;
import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.vo.dip.in.RowDataVO;
import nc.vo.dip.tyxml.ConfigVO;
import nc.vo.dip.tyxml.DipTYXMLDatachangeBVO;
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

	public List<DipTYXMLDatachangeBVO> crtVoucher(RowDataVO vo, List<DipTYXMLDatachangeBVO> t_detail) throws Exception{
		ArrayList<DipTYXMLDatachangeBVO> remain = new ArrayList<DipTYXMLDatachangeBVO>();
			for(int i=0;i<t_detail.size();i++){
				if(t_detail.get(i).getContrl() == null || t_detail.get(i).getContrl().trim().equals("")){
					remain.add(t_detail.get(i));
				}else{
					String ctrl = calcValue(vo, t_detail.get(i).getContrl(), BOOLEAN);
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
			List<DipTYXMLDatachangeBVO> bvo = crtDetail(vo, remain.toArray(new DipTYXMLDatachangeBVO[remain.size()]));
		if(bvo == null){
			return null;
		}
		return bvo;
	}

	private List<DipTYXMLDatachangeBVO> crtDetail(RowDataVO vo, DipTYXMLDatachangeBVO[] models) throws Exception {
		
		List<DipTYXMLDatachangeBVO> svo=new ArrayList<DipTYXMLDatachangeBVO>();
		for(int i=0;i<models.length;i++){
			DipTYXMLDatachangeBVO svoi=new DipTYXMLDatachangeBVO();
			DipTYXMLDatachangeBVO bvo=models[i];
			svoi.setBqfield(bvo.getBqfield());
			svoi.setRandend(bvo.getRandend()==null?new UFBoolean(false):bvo.getRandend());
			svoi.setRandstart(bvo.getRandstart()==null?new UFBoolean(false):bvo.getRandstart());
			if(bvo.getChangformu()!=null&&bvo.getChangformu().length()>0){
				svoi.setChangformu(calcValue(vo, bvo.getChangformu(),STRING));
			}
			svoi.setOrderno(bvo.getOrderno());
			svoi.setPrimaryKey(bvo.getPrimaryKey());
			if(bvo.getPro()!=null&&bvo.getPro().length()>0){
				String[] ss=bvo.getPro().split("\\+\",\"\\+");
				StringBuffer sb=new StringBuffer();
				for(int j=0;j<ss.length;j++){
					String[] si=ss[j].split(":\"\\+");
					sb.append(si[0].replace("\"", ""));
					sb.append("=");
					sb.append(calcValue(vo, si[1],STRING));
					if((j+1)<ss.length){
						sb.append(" ");
					}
				}
				svoi.setPro(sb.toString());
			}
			svo.add(svoi);
		}
		return svo;
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

	private String calcValue(RowDataVO vo, String formulas, String v_type){
//		formulas = formulas == null ? "":formulas;
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

	public List<DipTYXMLDatachangeBVO> crtVoucher(RowDataVO[] data, List<DipTYXMLDatachangeBVO> t_detail) throws Exception {
		boolean iscomb=false;
		List<DipTYXMLDatachangeBVO> bvo=new ArrayList<DipTYXMLDatachangeBVO>();
			for(int i=0;i<t_detail.size();i++){
				DipTYXMLDatachangeBVO bvoi=null;
				if(t_detail.get(i).getRandstart()!=null&&t_detail.get(i).getRandstart().booleanValue()){
					iscomb=true;
				}
				if(!iscomb){
					if(t_detail.get(i).getContrl() == null || t_detail.get(i).getContrl().trim().equals("")){
						bvoi=t_detail.get(i);
					}else{
						String ctrl = calcValue(data[0], t_detail.get(i).getContrl(), BOOLEAN);
						if(ctrl != null && !ctrl.equals("")){
							boolean match = new UFBoolean(ctrl).booleanValue();
							if(match){
								bvoi=t_detail.get(i);
							}
						}else{
							bvoi=t_detail.get(i);
						}
					}
					if(bvoi!=null){
						List<DipTYXMLDatachangeBVO> temp=crtDetail(data[0],new DipTYXMLDatachangeBVO[]{bvoi});
						if(temp!=null){
							bvo.add(temp.get(0));
						}
					}
				}else{
					int tempb=0;
					for(int j=i;j<t_detail.size();j++){
						if(t_detail.get(j).getRandend()!=null&&t_detail.get(j).getRandend().booleanValue()){
							tempb=j;
							break;
						}
					}
					for(int j=0;j<data.length;j++){
						for(int k=i;k<=tempb;k++){
							bvoi=null;
							if(t_detail.get(k).getContrl() == null || t_detail.get(k).getContrl().trim().equals("")){
								bvoi=t_detail.get(k);
							}else{
								String ctrl = calcValue(data[j], t_detail.get(k).getContrl(), BOOLEAN);
								if(ctrl != null && !ctrl.equals("")){
									boolean match = new UFBoolean(ctrl).booleanValue();
									if(match){
										bvoi=t_detail.get(k);
									}
								}else{
									bvoi=t_detail.get(k);
								}
							}
							if(bvoi!=null){
								List<DipTYXMLDatachangeBVO> temp=crtDetail(data[j],new DipTYXMLDatachangeBVO[]{bvoi});
								if(temp!=null){
									bvo.add(temp.get(0));
								}
							}
						}
					}
					i=tempb;
				}
				if(t_detail.get(i).getRandend()!=null&&t_detail.get(i).getRandend().booleanValue()){
					iscomb=false;
				}
			}
		if(bvo == null){
			return null;
		}
		return bvo;
	}
	
}
