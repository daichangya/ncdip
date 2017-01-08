  package nc.vo.dip.datadefcheck;
   	
	import java.util.ArrayList;

import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
	
/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 * 创建日期:2011-4-7
 * @author ${vmObject.author}
 * @version Your Project 1.0
 */
     public class DipDatadefinitCVO extends SuperVO {
           
    	 
             public UFBoolean is_content;
             public String pk_datadefinit_c;
             public String pk_datadefinit_b;
             public String dcode;
             public String dname;
             public Integer dtype;
             public String input_value;
             public String quote_value;
             public String quote_table;
             public String quote_field;
             public String def1;
             public String def2;
             public String def3;
             public String def4;
             public String def5;
             public Integer dr;
             public String ts;
             public String ref_pk;




             public static final String  IS_CONTENT="is_content";   
             public static final String  PK_DATADEFINIT_C="pk_datadefinit_c";   
             public static final String  PK_DATADEFINIT_B="pk_datadefinit_b";   
             public static final String  DCODE="dcode";  
             public static final String  DNAME="dname";   
             public static final String  DTYPE="dtype"; 
             public static final String  INPUT_VALUE="input_value";   
             public static final String  QUOTE_VALUE="quote_value";   
             public static final String  QUOTE_TABLE="quote_table";   
             public static final String  QUOTE_FIELD="quote_field";   
             public static final String  DEF1="def1";   
             public static final String  DEF2="def2";   
             public static final String  DEF3="def3";   
             public static final String  DEF4="def4";   
             public static final String  DEF5="def5";   

             public static final String  DR="dr";   

  
             
             
       
	public String getRef_pk() {
				return ref_pk;
			}

			public void setRef_pk(String ref_pk) {
				this.ref_pk = ref_pk;
			}

	public UFBoolean getIs_content() {
		return is_content;
	}

	public void setIs_content(UFBoolean is_content) {
		this.is_content = is_content;
	}

	public String getPk_datadefinit_c() {
		return pk_datadefinit_c;
	}

	public void setPk_datadefinit_c(String pk_datadefinit_c) {
		this.pk_datadefinit_c = pk_datadefinit_c;
	}

	public String getPk_datadefinit_b() {
		return pk_datadefinit_b;
	}

	public void setPk_datadefinit_b(String pk_datadefinit_b) {
		this.pk_datadefinit_b = pk_datadefinit_b;
	}

	public String getDcode() {
		return dcode;
	}

	public void setDcode(String dcode) {
		this.dcode = dcode;
	}

	public String getDname() {
		return dname;
	}

	public void setDname(String dname) {
		this.dname = dname;
	}

	public Integer getDtype() {
		return dtype;
	}

	public void setDtype(Integer dtype) {
		this.dtype = dtype;
	}

	public String getInput_value() {
		return input_value;
	}

	public void setInput_value(String input_value) {
		this.input_value = input_value;
	}

	public String getQuote_value() {
		return quote_value;
	}

	public void setQuote_value(String quote_value) {
		this.quote_value = quote_value;
	}

	public String getQuote_table() {
		return quote_table;
	}

	public void setQuote_table(String quote_table) {
		this.quote_table = quote_table;
	}

	public String getQuote_field() {
		return quote_field;
	}

	public void setQuote_field(String quote_field) {
		this.quote_field = quote_field;
	}

	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}

	public String getDef2() {
		return def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}

	public String getDef3() {
		return def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}

	public String getDef4() {
		return def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}

	public String getDef5() {
		return def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	/**
	  * <p>取得父VO主键字段.
	  * <p>
	  * 创建日期:2011-4-7
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	  	 
	 	    return "pk_datadefinit_b";
	 	
	}   
    
    /**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2011-4-7
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	 	  return "pk_datadefinit_c";
	 	}
    
	/**
      * <p>返回表名称.
	  * <p>
	  * 创建日期:2011-4-7
	  * @return java.lang.String
	 */
	public java.lang.String getTableName() {
				
		return "dip_datadefinit_c";
	}    
	
	 public void validate() throws ValidationException {
			
		 	ArrayList errFields = new ArrayList(); // errFields record those null

	                                                      // fields that cannot be null.
	       		  // 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:
		
		   		if (pk_datadefinit_b == null) {
				errFields.add(new String("pk_datadefinit_b"));
				  }else if(dcode == null){
				errFields.add(new String("dcode"));
				  }else if(dname == null){
				errFields.add(new String("dname"));
				  }else if(dtype == null){
				errFields.add(new String("dtype"));
				  }
		   	
		    StringBuffer message = new StringBuffer();
			message.append("下列字段不能为空:");
			if (errFields.size() > 0) {
			String[] temp = (String[]) errFields.toArray(new String[0]);
			message.append(temp[0]);
			for ( int i= 1; i < temp.length; i++ ) {
				message.append(",");
				message.append(temp[i]);
			}
			throw new NullFieldException(message.toString());
			}
		 }
    
 }