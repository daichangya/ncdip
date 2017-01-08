package nc.vo.pub.ldap;

import java.util.Properties;

import nc.bs.framework.common.RuntimeEnv;
/**
 * Ldap配置文件信息
 * */
public class LdapConfigXml {
	private Properties props=null;
    private  String ncHome = null;
	private  String fileUrl = null;
	public LdapConfigXml() {
		ncHome = RuntimeEnv.getInstance().getProperty(RuntimeEnv.SERVER_LOCATION_PROPERTY).trim().replace('\\','/');
        if(!ncHome.endsWith("/"))
            ncHome += "/";
        fileUrl = ncHome + "pfxx/LdapConfigXml.xml";
		ParseXML myRead = new ParseXML();
		try {
			myRead.parse(fileUrl);
			props = new Properties();
			props = myRead.getProps();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public String getProvider_url() {
		return props.getProperty("provider_url");
	}
	
	public String getSecurity_principal() {
		return props.getProperty("security_principal");
	}
	public String getSecurity_credentials() {
		return props.getProperty("security_credentials");
	}
	public String getIsauth() {
		return props.getProperty("isauth");
	}
}
