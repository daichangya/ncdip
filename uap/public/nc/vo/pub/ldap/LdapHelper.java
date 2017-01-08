package nc.vo.pub.ldap;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import nc.bs.logging.Logger;

public class LdapHelper {

    private static DirContext ctx;
    
    private static LdapConfigXml ldapXml=null;
    
    public static LdapConfigXml getLdapXml() {
    	if(null == ldapXml){
    		ldapXml = new LdapConfigXml();
    	}
		return ldapXml;
	}

	@SuppressWarnings(value = "unchecked")
    public static DirContext getCtx() {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, getLdapXml().getProvider_url());
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, getLdapXml().getSecurity_principal());
        env.put(Context.SECURITY_CREDENTIALS, getLdapXml().getSecurity_credentials());
        try {
            ctx = new InitialDirContext(env);
            Logger.error("��֤�ɹ�");
        } catch (javax.naming.AuthenticationException e) {
        	Logger.error("��֤ʧ��");
        } catch (Exception e) {
        	Logger.error("��֤����"+e.getMessage());
            e.printStackTrace();
        }
        return ctx;
    }
    
    public static void closeCtx(){
        try {
            ctx.close();
        } catch (NamingException ex) {
            Logger.error(ex);
        }
    }
  
    public static boolean authenticate(String usr, String pwd) {
    	if("true".equals(getLdapXml().getIsauth())){
	        DirContext ctx = null;
	        try {
	            ctx = LdapHelper.getCtx();
	            if(null == ctx){
	            	return false;
	            }
	            SearchControls constraints = new SearchControls();
	            constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
	            NamingEnumeration en = ctx.search("", "uid=" + usr, constraints); // ��ѯ�����û�
	            while (en != null && en.hasMoreElements()) {
	                Object obj = en.nextElement();
	                if (obj instanceof SearchResult) {
	                    SearchResult si = (SearchResult) obj;
	                    Logger.error("name:   " + si.getName());
	                    Attributes attrs = si.getAttributes();
	                    if (attrs == null) {
	                    	Logger.error("No   attributes");
	                    } else {
	                        Attribute attr = attrs.get("userPassword");
	                        Object o = attr.get();
	                        byte[] s = (byte[]) o;
	                        String pwd2 = new String(s);
	                        Logger.error(pwd2);
	                        Boolean success = verifySHA(pwd2, pwd);
	                        return success;
	                    }
	                } else {
	                    Logger.error(obj);
	                }
	            }
	            ctx.close();
	        } catch (Exception ex) {
	            try {
	                if (ctx != null) {
	                    ctx.close();
	                }
	            } catch (NamingException namingException) {
	                namingException.printStackTrace();
	            }
	            Logger.error(ex);
	        }
	        return false;
    	}else{
    		return true;
    	}
    }
    public static boolean verifySHA(String ldappw, String inputpw)
	    throws NoSuchAlgorithmException {
		
		// MessageDigest �ṩ����ϢժҪ�㷨���� MD5 �� SHA���Ĺ��ܣ�����LDAPʹ�õ���SHA-1
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		
		// ȡ�������ַ�
		if (ldappw.startsWith("{SSHA}")) {
		    ldappw = ldappw.substring(6);
		} else if (ldappw.startsWith("{SHA}")) {
		    ldappw = ldappw.substring(5);
		}
		
		// ����BASE64
		byte[] ldappwbyte = Base64.decode(ldappw);
		byte[] shacode;
		byte[] salt;
		
		// ǰ20λ��SHA-1���ܶΣ�20λ�����������ʱ���������
		if (ldappwbyte.length <= 20) {
		    shacode = ldappwbyte;
		    salt = new byte[0];
		} else {
		    shacode = new byte[20];
		    salt = new byte[ldappwbyte.length - 20];
		    System.arraycopy(ldappwbyte, 0, shacode, 0, 20);
		    System.arraycopy(ldappwbyte, 20, salt, 0, salt.length);
		}
		// ���û������������ӵ�ժҪ������Ϣ
		md.update(inputpw.getBytes());
		// �����������ӵ�ժҪ������Ϣ
		md.update(salt);
		
		// ��SSHA�ѵ�ǰ�û�������м���
		byte[] inputpwbyte = md.digest();
		
		// ����У����
		return MessageDigest.isEqual(shacode, inputpwbyte);
	}
}