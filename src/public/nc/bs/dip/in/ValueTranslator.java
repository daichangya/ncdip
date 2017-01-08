package nc.bs.dip.in;

import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class ValueTranslator {
	public static final String V_INTEGER = "integer";
	public static final String V_CHAR = "char";
	public static final String V_VARCHAR = "varchar";
	public static final String V_VARCHAR2 = "varchar2";
	public static final String V_DECIMAL = "decimal";
	public static final String V_NUMBER = "number";
	public static final String V_SMALLINT = "smallint";
	public static final String V_LONG = "long";
//	public static final String V_DATE = "date";
	
	public static Object translate(String oldValue, String v_type, Integer length){
		try {
			if(v_type.equalsIgnoreCase(V_INTEGER) || v_type.equalsIgnoreCase(V_LONG) || v_type.equalsIgnoreCase(V_SMALLINT)){
				Integer value;
				if(oldValue.contains(".")){
					oldValue = oldValue.replace(".", "#");
					String[] str = oldValue.split("#");
					
					if(Integer.parseInt(str[1]) == 0){
						value = Integer.parseInt(str[0]);
					}else{
						return oldValue;
					}
				}else{
					value = Integer.parseInt(oldValue);
				}
				return value;
			}else if(v_type.equalsIgnoreCase(V_DECIMAL) || v_type.equalsIgnoreCase(V_NUMBER)){
				UFDouble value = new UFDouble(oldValue);
				return value;
			}/*else if(v_type.equals(V_DATE)){
				
			}*/else if(v_type.equalsIgnoreCase(V_CHAR) && length == 10){
				UFDate value = new UFDate(oldValue);
				return value;
			}else if(v_type.equalsIgnoreCase(V_CHAR) && length == 1){
				UFBoolean value = new UFBoolean(oldValue);
				return value;
			}else if(v_type.equalsIgnoreCase(V_CHAR) && length == 19){
				UFDateTime value = new UFDateTime(oldValue);
				return value;
			}else{
				return oldValue;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return oldValue;
		}
	}
	
	public static boolean validate(Object value, String v_type, int length){
		try {
			if(v_type.equals(V_INTEGER) || v_type.equals(V_LONG) || v_type.equals(V_SMALLINT)){
				if(value instanceof Integer){
					return true;
				}
				return false;
			}else if(v_type.equals(V_DECIMAL) || v_type.equals(V_NUMBER)){
				if(value instanceof Integer){
					value = new UFDouble((Integer)value);
					return true;
				}else if(value instanceof UFDouble){
					return true;
				}
				return false;
			}/*else if(v_type.equals(V_DATE)){
				
			}*/else if(v_type.equals(V_CHAR) && length == 10){
				if(value instanceof UFDate){
					return true;
				}
				return false;
			}else if(v_type.equals(V_CHAR) && length == 1){
				if(value instanceof UFBoolean){
					return true;
				}
				return false;
			}else if(v_type.equals(V_CHAR) && length == 19){
				if(value instanceof UFDateTime){
					return true;
				}
				return false;
			}else{
				if(value instanceof String){
					return true;
				}
				return false;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
	}
}
