package nc.vo.dip.ftpsourceregister;

import java.util.ArrayList;

import nc.util.dip.sj.IContrastUtil;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDateTime;

public class FtpSourceRegisterVO extends SuperVO{
	 private String pk_ftpsourceregister;
	 private String code;
	 private String name;
	 private String address;
	 private String port;
	 private String username;
	 private String password;
	 private String path;
	 private String defstr_1;
	 private String defstr_2;
	 private String defstr_3;
	 private String defstr_4;
	 private String ts;
	 private Integer dr;
	 
	 
	 public static final String PK_FTPSOURCEREGISTER="pk_ftpsourceregister";
	 public static final String CODE="code";
	 public static final String NAME="name";
	 public static final String ADDRESS="address";
	 public static final String PORT="port";
	 public static final String USERNAME="username";
	 public static final String PASSWORD="password";
	 public static final String PATH="path";
	 public static final String DEFSTR_1="defstr_1";
	 public static final String DEFSTR_2="defstr_2";
	 public static final String DEFSTR_3="defstr_3";
	 public static final String DEFSTR_4="defstr_4";
	 public static final String TS="ts";
	 public static final String DR="dr";
	 
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDefstr_1() {
		return defstr_1;
	}

	public void setDefstr_1(String defstr_1) {
		this.defstr_1 = defstr_1;
	}

	public String getDefstr_2() {
		return defstr_2;
	}

	public void setDefstr_2(String defstr_2) {
		this.defstr_2 = defstr_2;
	}

	public String getDefstr_3() {
		return defstr_3;
	}

	public void setDefstr_3(String defstr_3) {
		this.defstr_3 = defstr_3;
	}

	public String getDefstr_4() {
		return defstr_4;
	}

	public void setDefstr_4(String defstr_4) {
		this.defstr_4 = defstr_4;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPk_ftpsourceregister() {
		return pk_ftpsourceregister;
	}

	public void setPk_ftpsourceregister(String pk_ftpsourceregister) {
		this.pk_ftpsourceregister = pk_ftpsourceregister;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public Object getTs() {
		if(ts==null){
			return null;
		}else{
			if(IContrastUtil.VERSION.equals("nc502")){
				  return new UFDateTime(ts);
			  }else if(IContrastUtil.VERSION.equals("nc507")){
				  return ts;  
			  }
		}
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_ftpsourceregister";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "dip_ftpsourceregister";
	}
	@Override
	 public void validate() throws ValidationException {
			
		 	ArrayList errFields = new ArrayList(); // errFields record those null

	                                                      // fields that cannot be null.
	       		  // 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:
		
		   		if (pk_ftpsourceregister == null) {
				errFields.add(new String("pk_ftpsourceregister"));
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
