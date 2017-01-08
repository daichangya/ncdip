package nc.bs.dip.fun;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.common.RuntimeEnv;
import nc.itf.dip.pub.IQueryField;
import nc.ui.pub.beans.MessageDialog;
import nc.vo.dip.in.RowDataVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.formulaset.FormulaParseFather;
import nc.vo.pub.formulaset.VarryVO;
/**
 * 中邮接口平台公式解析器
 * 
 * @author 袁廷勤 2011-05-14
 * 
 * 
 */
public class YzFormulaParse {


	/**
	 * 根据VO，计算公式:计算出结果
	 *@author 袁廷勤 2011-05-14
	 * @param vo	    表单vo
	 * @param formula	计算公式
	 * @return 			计算结果
	 * @throws BusinessException
	 */	
 //public static int k=0;
	
    public static Object getFormulaCal(Object vo,String formula){
    	return getFormulaCal(null,(RowDataVO)vo,formula);
    	
    }

	/**
	 * 根据VO，字段，得到该字段的值
	 *@author 袁廷勤 2011-05-14
	 * @param vo	    表单vo
	 * @param filedname	字段
	 * @return 			字段值 getField
	 * @throws BusinessException
	 */	

    
	   private static String getVoGetMethod(Object vo, String fieldname) {
		   Object attributeValue = ((SuperVO)(vo)).getAttributeValue(fieldname);
		   if(attributeValue==null){
			   attributeValue=((SuperVO)(vo)).getAttributeValue(fieldname.toLowerCase());
		   }
		   if(attributeValue!=null){
			   if (attributeValue instanceof String) {
				   String sv = attributeValue.toString();
				   return "\""+sv+"\"";
			   }else{
				   String sv = attributeValue.toString();
				   return sv;
			   }
		   }else{
			   return null;
		   }
		   
//		if (fieldname != null) {
//			Class c = vo.getClass();
//			Method[] methods = c.getMethods();
//			for (int i = 0; i < methods.length; i++) {
//				String className = methods[i].getName();
//				if (className.length() > 3
//						&& className.substring(0, 3).equals("get")) {
//					if (className.substring(3).equalsIgnoreCase(fieldname)) {
//						try {
//							Class[] cls = { String.class };
////							System.out.println(fieldname + "   " + cls + " "
////									+ c.getMethods());
//							Method method = c.getMethod("get"
//									+ className.substring(3));
//							sv = method.invoke(vo).toString();
//						} catch (Exception ex) {
//							ex.printStackTrace();
//						}
//					}
//				}
//			}
//		}
//		return sv;
	}

	public static Object getFormulaCal(RowDataVO tvo, RowDataVO vo, String formula) {
    	
    	//把表头和表体的特殊标志号去掉。
    	if(formula.indexOf("@")>=0){
    		String[] names=((SuperVO)(vo)).getAttributeNames();
    		if(names!=null&&names.length>0){
    			for(String name:names){
    				if(formula.indexOf("@"+name.toLowerCase()+"@")>=0){
    					String valu=getVoGetMethod(vo, name);
    					if(formula.length()==("@"+name.toLowerCase()+"@").length()){
    						formula=valu.replaceAll("\"", "");
    						return formula;
    					}else{
    							formula=formula.replaceAll("@"+name.toLowerCase()+"@", valu);
    					}
    				}
    				if(formula.indexOf("@"+name.toUpperCase()+"@")>=0){
    					String valu=getVoGetMethod(vo, name);
    					if(formula.length()==("@"+name.toUpperCase()+"@").length()){
    						formula=valu.replaceAll("\"", "");
    						return formula;
    					}else{
    							formula=formula.replaceAll("@"+name.toUpperCase()+"@", valu);
    					}
    							
    				}
    			}
    		}
    	}
    	if(formula.indexOf("#")>=0){
    		String[] names=((SuperVO)(tvo)).getAttributeNames();
    		if(names!=null&&names.length>0){
    			for(String name:names){
    				if(formula.indexOf("#"+name.toLowerCase()+"#")>=0){
    					String valu=getVoGetMethod(tvo, name);
    					if(formula.length()==("#"+name.toLowerCase()+"#").length()){
    						formula=valu.replaceAll("\"", "");
    						return formula;
    					}else{
    							formula=formula.replaceAll("#"+name.toLowerCase()+"#", valu);
    					}
    				}
    				if(formula.indexOf("#"+name.toUpperCase()+"#")>=0){
    					String valu=getVoGetMethod(tvo, name);
    					if(formula.length()==("#"+name.toUpperCase()+"#").length()){
    						formula=valu.replaceAll("\"", "");
    						return formula;
    					}else{
    							formula=formula.replaceAll("#"+name.toUpperCase()+"#", valu);
    					}
    							
    				}
    			}
    		}
    	}
    	if(formula==null||"".equals(formula)){
    		return "";
    	}else if(!formula.contains("initEnv")){
    		return calFormula(formula);
    	}else{
    		return calContainEnvFormu(formula) ;
    	}
		
    }  	
	private static String calContainEnvFormu(String formula){
		formula=formula.replace(" ", "");
		String function="initEnvHrFunction";
		if(!formula.contains(function)){
			function="initEnv";
		}
		while(formula.contains(function)){
			int index=formula.indexOf(function);
			String temp=formula.substring(index+function.length()+1);
			int counti=getStrLindex(temp);
			temp=temp.substring(0,counti);//此时的temp是initEnv函数里边的所有东西，包括参数和函数，包括-b-e的业务函数和函数
			//先把业务函数里边包含的函数解析了吧
			String ywhs=temp.substring(temp.indexOf("-b")+2,temp.indexOf("-e"));//此时的业务函数是GLQC（"","",...）的形式
			String ft=ywhs.substring(ywhs.indexOf("(")+1,ywhs.length()-1);
			String repla=jxlchs(ft);
			String tt="";
			if(repla.indexOf(",")>0){
				String[] s=repla.split(",");
				for(int j=0;j<s.length;j++){
					s[j]=calFormula(s[j]);
						s[j]=s[j]==null?null:("\""+s[j]+"\"");
					tt=tt+s[j]+",";
				}
				repla=tt.substring(0,tt.length()-1);
			}
			String qft=temp.substring(0,temp.indexOf("-b"));
			String replq=jxlchs(qft);
			tt="";
			if(replq.indexOf(",")>0){
				String[] s=replq.split(",");
				for(int j=0;j<s.length;j++){
					s[j]=calFormula(s[j]);
//					tt=tt+s[j]+",";
					tt=tt+(s[j]==null?null:"\""+s[j]+"\"")+",";
				}
				replq=tt;
			}
			String gs=function+"("+replq+"\""+ywhs.substring(0,ywhs.indexOf("("))+"|"+repla.replace("\"", "'").replace(",", "|")+"\")";
			String res= calFormula(gs);
			formula=formula.replace(function+"("+temp+")", res==null?"null":res);
//			formula=formula.replace("initEnv("+temp+")", "\""+calFormula(gs)+"\"");
			if(formula.equals(res)){
				formula="\""+formula+"\"";
			}
		}
		formula=calFormula(formula);
		return formula;
	}
	private static String jxlchs(String ft){
		int countb=0;
		int counte=0;
		if(ft.indexOf("(")>0){
	    	//判断函数的左右括号是否相同
	    	  countb=(ft+"a").split("\\(").length;
			  counte=(ft+"a").split("\\)").length;
			if(countb!=counte){
				return "";
			}
			//循环，直到里边不包含函数
	    	while(ft.indexOf("(")>0){
		    	String tempft=ft;
		    	int fbindex=ft.indexOf("(");
		    	int fromind=0;
		    	int count=0;
		    	//如果函数嵌套函数
			    while(tempft.length()>=fbindex+1&&tempft.indexOf("(",fbindex+1)>=0&&tempft.indexOf(")",fbindex+1)>=0&&tempft.indexOf("(",fbindex+1)<tempft.indexOf(")",fbindex+1)){
			    	tempft=tempft.substring(fbindex+1);
			    	fromind=fromind+fbindex+1;
			    	count++;
			    }
		    	int fteindex=tempft.indexOf(")")+fromind;
		    	fromind=fromind==0?fbindex:fromind;
		    	String lfstr=ft.substring(0,fteindex);
		    	lfstr=lfstr.substring(0,lfstr.lastIndexOf("("));
		    	int from=(lfstr.lastIndexOf(",")<lfstr.lastIndexOf("(")?lfstr.lastIndexOf("("):lfstr.lastIndexOf(","))+1;
		    	//最里层的函数
		    	String temform=ft.substring(from,fteindex+1);
		    	//把参数里边的函数都替换成“”
//		    	ft=ft.replace(temform, calFormula(temform));
		    	ft=ft.replace(temform, "\""+calFormula(temform)+"\"");
	    	}
	    }
		 return ft;
	}
	/**
	 * 返回字符串里边“)”的位置，例如“abc)”,则返回3
	 * */
	private static int getStrLindex(String temp){
		int countl=0;
		int count=0;
		char[] chara=temp.toCharArray();
		for(char c:chara){
			if(c=='('){
				countl--;
			}else if(c==')'){
				countl++;
			}
			if(countl==1){
				break;
			}
			count++;
		}
		return count;
	}
	private static String calFormula(String formula){
		String res="0";
		if(formula==null||"".equals(formula)){return "";}
		if(formula.indexOf("\"\"")>=0&&formula.indexOf("*")>=0){
			formula=formula.replace("\"\"", "0");
		}
     IQueryField iq=(IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName());
    	try {
    		
    		
			if(iq.getFormmulaCache().formmulaFlag!=null&&iq.getFormmulaCache().formmulaFlag.equals("yes")&&iq.getFormmulaCacheValue(formula)!=null){
				res=(String) iq.getFormmulaCacheValue(formula);
			}else{
 
				FormulaParseFather f;
			if (RuntimeEnv.getInstance().isRunningInServer()) {
				f = new nc.bs.pub.formulaparse.FormulaParse();
			} else {
				f = new nc.ui.pub.formulaparse.FormulaParse();
			}
			f.setSelfMethod("nc.bs.dip.fun.DefFormmulaFunc", "getGenPK", String.class,
					new Class[] {});	
			f.setSelfMethod("nc.bs.dip.fun.DefFormmulaFunc", "cmonth", String.class,
					new Class[] {});	
			f.setSelfMethod("nc.bs.dip.fun.DefFormmulaFunc", "cyear", String.class,
					new Class[] {});	
			f.setSelfMethod("nc.bs.dip.fun.DefFormmulaFunc", "initEnv", String.class,
					new Class[] { String.class,String.class,String.class ,String.class, String.class});
			f.setSelfMethod("nc.bs.dip.fun.DefFormmulaFunc", "initEnvHrFunction", String.class,
					new Class[] { String.class,String.class,String.class,String.class});
			f.setSelfMethod("nc.bs.dip.fun.DefFormmulaFunc", "getYMLastDay", String.class,
					new Class[] { String.class, String.class});	
			f.setSelfMethod("nc.bs.dip.fun.DefFormmulaFunc", "getValueByConst", String.class,
					new Class[] { String.class});	
			f.setSelfMethod("nc.bs.dip.fun.DefFormmulaFunc", "getCurAccountYear", String.class,
					new Class[] {  String.class });	
			f.setSelfMethod("nc.bs.dip.fun.DefFormmulaFunc", "getCurAccountMonth", String.class,
					new Class[] {  String.class });
			f.setSelfMethod("nc.bs.dip.fun.DefFormmulaFunc", "getDataByKey", String.class,
					new Class[] { String.class,String.class, Object.class});
			f.setSelfMethod("nc.bs.dip.fun.DefFormmulaFunc", "querySqlByDesign", String.class,
					new Class[] { String.class,String.class});	
			f.setSelfMethod("nc.bs.dip.fun.DefFormmulaFunc", "random", String.class,
					new Class[] {  Object.class});
			f.setSelfMethod("nc.bs.dip.fun.DefFormmulaFunc", "defGetColValue", String.class,
					new Class[] { String.class,String.class,String.class,String.class});
			f.setSelfMethod("nc.bs.dip.fun.DefFormmulaFunc", "trim", String.class,
					new Class[] {  Object.class});
			f.setSelfMethod("nc.bs.dip.fun.DefFormmulaFunc", "formatDef", String.class,
					new Class[] {  Object.class,Object.class});
			//defGetColValue
			if ((formula==null) ||(formula.equals(""))){//没公式:有错误,弹出提示框，并退出
				BusinessException x=new BusinessException("没有定义公式，请检查!!");
				try {
					throw new BusinessException(x.getMessage(),x);
				} catch (BusinessException e) {
					e.printStackTrace();
				}
			}
			else {//有公式
				f.setExpress(formula);//设置公式
				/*VarryVO[] varrys = f.getVarryArray();	
				for (int j = 0; j < varrys.length; j++)
				{
					String[] varries = varrys[j].getVarry();
					//提取公式变量非必须步骤
					if(varries != null)
					{
						for (int k = 0; k < varries.length; k++)
						{
							//从外部环境取得变量的值
							//Object varryValue = getVarryValue(varries[j]);
							//传递给公式
							String fv=getVoGetMethod(vo, varries[k].toString());
							f.addVariable(varries[k],fv);
						}
					}
				}
			    */
//			获得变量名
				boolean isok=f.check();
				if(isok){
//					f.getValue();
					res = f.getValue();
				}
				if(iq.getFormmulaCache().formmulaFlag!=null&&iq.getFormmulaCache().formmulaFlag.equals("yes")&&formula.indexOf("getGenPK")<0){
					iq.putFormmulaCache(formula, res);
				}
					
			
			//	k++;
			}
			
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return res;
	}
	
}
