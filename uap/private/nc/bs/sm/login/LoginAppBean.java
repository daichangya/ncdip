package nc.bs.sm.login;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import nc.bd.accperiod.AccountCalendar;
import nc.bd.accperiod.InvalidAccperiodExcetion;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.sm.identityverify.IAMode;
import nc.bs.sm.identityverify.IAModeFactory;
import nc.bs.uap.lock.PKLock;
import nc.bs.uap.sf.excp.SystemFrameworkException;
import nc.bs.uap.sf.facility.SFServiceFacility;
import nc.itf.uap.cil.ICilService;
import nc.itf.uap.rbac.function.IFuncPower;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.ui.bd.BDGLOrgBookAccessor;
import nc.ui.uap.sf.SFServiceHelper;
import nc.vo.bd.CorpVO;
import nc.vo.bd.b54.GlorgbookVO;
import nc.vo.framework.rsa.Encode;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CommonConstant;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.ldap.LdapHelper;
import nc.vo.pub.services.ServerEnvironment;
import nc.vo.sm.UserVO;
import nc.vo.sm.cmenu.CustomMenuItemVO;
import nc.vo.sm.cmenu.CustomMenuShortcutVO;
import nc.vo.sm.config.Account;
import nc.vo.sm.config.AccountAdm;
import nc.vo.sm.config.ConfigParameter;
import nc.vo.sm.config.SysAdm;
import nc.vo.sm.identityverify.IAConfVO;
import nc.vo.sm.login.ConfigParser;
import nc.vo.sm.login.IControlConstant;
import nc.vo.sm.login.ILoginResult;
import nc.vo.sm.login.IViewConstant;
import nc.vo.sm.login.LoginFailureInfo;
import nc.vo.sm.login.LoginSessBean;
import nc.vo.uap.rbac.power.PowerResultVO;
import nc.vo.uap.rbac.userpassword.PasswordSecurityLevelFinder;
import nc.vo.uap.rbac.userpassword.PasswordSecurityLevelVO;

/**
 * �����������ӵ�session��һ��Applicationֻ��һ��LoginAppBean�� ����¼���������еĵ�¼session���������ӳ�ʱ������û����Ŀ��ƣ��ͷ��û���Դ,����¼��¼��־ �������ڣ�(2001-3-16 21:06:16)
 * 
 * @author����ʤ�� ��δ��������һ�̣߳�ÿ�������ӵ���һ��checkSession()���� 2001-04-26 ����ΰ����ʵ�� �޸��ˣ��׾� 2004-7-27 �޸��������û��ļ���������ֻ�����������Ϣ��
 */
public class LoginAppBean implements IViewConstant, IControlConstant {

    /**
     * �������޹صı�����
     */
    /** Ϊ�ύ��¼������û�����һ����ˮ�� */
    private Random m_rand = null;

    // /** ���������û������� */
    // private int m_maxUser;

//    /** ����session�ĳ�ʱ��������룩 */
//    private long m_interval;

    /** �����ϴ������뵱ǰʱ��Ĳ�ֵ */
    private long m_conInterval;

    /** ���������¼ʧ�ܵ������� */
    private int m_allowedMaxFailure;

    public static final int OVER_MAX_USER = 1;

    public static final int USER_EXIST = 2;

    public static final int LOGIN_OK = 0;

    public static final String ACC_ADM_PK_CORP = CommonConstant.GROUP_CODE;

    // ���ڽ��ܼ��ܵĹ���
    private Encode coder = null;

    private CheckSessionThread m_CheckSessionThread;

    private static LoginAppBean m_instance = new LoginAppBean();

    private HashMap m_illegalLoginContainer = new HashMap();

    private HashMap m_loginUserContainer = new HashMap();

    /**
     * LoginAppBean ������ע�⡣
     */
    private LoginAppBean() {
        super();
        // ���õ�¼������
        nc.vo.sm.login.ConfigParser parser = ConfigParserOnServer.getConfigParser();
        setMaxFailure(parser.getMaxLoginFailure());
        // setMaxUser(parser.getMaxUser() <= 2 ? 500 : parser.getMaxUser());
//        setInterval(parser.getMaxInactiveTime());
        setConInterval(parser.getClaimingInterval());
        //
        if (parser.getClaimingInterval() > 1) {
            CheckSessionThread.INTER_LEAVE_TIME = parser.getClaimingInterval() - 1;
        } else {
            CheckSessionThread.INTER_LEAVE_TIME = 60000;
        }
        //
        m_CheckSessionThread = new CheckSessionThread();
        m_CheckSessionThread.setLoginAppBean(this);
        m_CheckSessionThread.start();

        //
        Logger.debug("=====================");
        Logger.debug("Claiming Interval: " + parser.getClaimingInterval());
        Logger.debug("=====================");
    }

    /**
     * �˴����뷽��˵���� �������ڣ�(2001-6-23 16:49:11)
     * 
     * @return int
     */
    private int checkAccount(String accountCode, UFDate today) throws SystemFrameworkException {

        ConfigParameter config = null;
        try {
            config = AccountXMLUtil.getConfigParameter();
        } catch (Exception e) {
            Logger.error("Error",e);
            throw new SystemFrameworkException(e.getMessage());
        }
        Account[] accounts = config.getAryAccounts();
        int pos = -1;
        for (int i = 0; i < accounts.length; i++) {
            if (accounts[i].getAccountCode().equals(accountCode)) {
                pos = i;
                break;
            }
        }

        // δ�ҵ������ף�
        if (pos == -1) {
            String err = "���ұ���Ϊ\"" + accountCode + "\"������ʧ�ܣ�û���ҵ�������";
            Logger.error(err);
            throw new SystemFrameworkException(err);
        }
        if (accounts[pos].isLocked()) {
            return LoginFailureInfo.ACCOUNT_LOCKED;
        }
        //
        if (today.before(accounts[pos].getEffectDate())) {
            return LoginFailureInfo.ACCOUNT_BEFORE_EFFECT;
        }
        if (today.after(accounts[pos].getExpireDate().getDateBefore(1))) {
            return LoginFailureInfo.ACCOUNT_EXPIRED;
        }
        return LoginFailureInfo.LOGIN_SUCCESS;
    }

    /**
     * ����ѵ�½�û�������ʱ���Ѷ��ߣ����hashTable��ɾ�����ͷ���Դ�� �������ڣ�(2001-3-17 16:34:20)
     */
    public void checkTimeoutUser() {

        LoginSessBean lsb = null;
        long currentTime = System.currentTimeMillis();
        Collection users = getLoginUserContainer().values();
        Iterator it = users.iterator();
        while (it.hasNext()) {
            Enumeration enums = ((Hashtable) it.next()).elements();
            while (enums.hasMoreElements()) {
                lsb = (LoginSessBean) enums.nextElement();
                long lastConnectTime = lsb.getVoteTime();
                long conInterval = currentTime - lastConnectTime;

                if (conInterval > m_conInterval) {
                    removeUser(lsb.getDataSourceName(), lsb.getUserId(), lsb.getSID());
                }
            }
        }
    }

    // /**
    // * �˴����뷽��˵���� �������ڣ�(2001-3-17 11:21:48)
    // */
    // private int getMaxUser() {
    // return m_maxUser;
    // }

    /**
     * �˴����뷽��˵���� �������ڣ�(2001-3-17 11:21:48)
     */
    private Random getRandom() {

        if (m_rand == null) {
            m_rand = new Random(2011);
        }
        return m_rand;
    }

    public UserVO getUser(String pk_corp, String usercode, String dbName) {

        UserVO user = null;
        try {
            user = SFServiceFacility.getUserQueryService().findUserWithDataSource(pk_corp, usercode, dbName);
        } catch (Exception e) {
            Logger.error("Error",e);
        }
        return user;
    }

    /**
     * �˴����뷽��˵���� �������ڣ�(2001-3-17 11:21:48)
     */
    public void setConInterval(long interval) {
        this.m_conInterval = interval;
    }

//    /**
//     * �˴����뷽��˵���� �������ڣ�(2001-3-17 11:21:48)
//     */
//    public void setInterval(long interval) {
//        this.m_interval = interval; // ���ó�ʱʱ����
//    }

    /**
     * �˴����뷽��˵���� �������ڣ�(2001-3-17 11:21:48)
     */
    public void setMaxFailure(int maxFailure) {
        this.m_allowedMaxFailure = maxFailure; // ��������ʧ�ܵ�������
    }
   
    /**
     * modifier ewei
     * ���ݵ�ǰ���û�VO�õ��������ʧ��ֵ
     */
    public int getMaxFailure(UserVO user){
    	nc.vo.sm.login.ConfigParser parser = ConfigParserOnServer.getConfigParser();
    	if (user.getPwdlevelcode()==null){	
    		return parser.getMaxLoginFailure();
    	}
    	else{
    		PasswordSecurityLevelVO plvo=PasswordSecurityLevelFinder.getPWDLV(user.getPwdlevelcode());
    		if(plvo==null){
    			return parser.getMaxLoginFailure();
    		}else{
    			return plvo.getErrorloginThreshold()==null?parser.getMaxLoginFailure():plvo.getErrorloginThreshold().intValue();
    		}
    	}
    }
    
    /**
     * modifier ewei
     * �������׹���Ա�õ�����¼ʧ�ܴ���
     */
    public int getMaxFailure(AccountAdm accadm){
    	nc.vo.sm.login.ConfigParser parser = ConfigParserOnServer.getConfigParser();
    	if (accadm.getPasswordLevel()==null){	
    		return parser.getMaxLoginFailure();
    	}
    	else{
    		PasswordSecurityLevelVO plvo=PasswordSecurityLevelFinder.getPWDLV(accadm.getPasswordLevel());
    		if(plvo!=null){
    			return plvo.getErrorloginThreshold()==null?parser.getMaxLoginFailure():plvo.getErrorloginThreshold().intValue();
    		}else{
    			return parser.getMaxLoginFailure();
    		}
    	} 
    	
    }
    // /**
    // * �˴����뷽��˵���� �������ڣ�(2001-3-17 11:21:48)
    // */
    // public void setMaxUser(int maxUser) {
    // this.m_maxUser = maxUser; // �������������û���
    // }

    /**
     * ���ݲ�Ʒ��Ȩ���ж��Ƿ�ɴ򿪸ò�Ʒ��һ���ڵ㡣�������£�������û��Ѵ򿪸� ��Ʒ�������ڵ㣬��ɴ򿪸ýڵ㣻���������ǰʹ�øò�Ʒ���û�δ�ﵽ�����Ȩ���� ��ɴ򿪸ýڵ㡣 �������ڣ�(2001-10-18 11:22:59)
     * 
     * @return boolean
     * @param product
     *            java.lang.String
     */
    public  boolean canOpenModule(String product, String userId) {

        // ���ServerEnvironment:
        // ��ǰ���׵�ServerEnvironment:
        nc.vo.pub.services.ServerEnvironment se = null;
        // �������׵�ServerEnvironment:
        nc.vo.pub.services.ServerEnvironment[] ses = null;
        try {
            se = SFServiceFacility.getServiceProviderService().getServerEnv();
            ses = SFServiceFacility.getServiceProviderService().getAllSEInAllContainer().toArray(new ServerEnvironment[0]);//SFServiceFacility.getServiceProviderService().getAllServerEnvs();
        } catch (Exception e) {
            Logger.error("Error",e);
        }
        // �����Ȩ����
        int license = getProductLicense(product);

        // �жϣ�
        int userCount = 0;
        HashSet set = new HashSet();
        for (int i = 0; i < ses.length; i++) {
            HashSet users = (HashSet) ses[i].getProductUsers().get(product);
            if (users != null) {
//                userCount += users.size();
            	set.addAll(users);
            }
        }
        userCount = set.size();
        
        String msg = "licp ===>canOpenModule ?  product = '" + product + "'�� liencese = " + license + " ��userCount = " + userCount;
        Logger.debug(msg);
        //
        if (license > userCount) {
            // ��Ȩ�����ڸò�Ʒ��ǰ�����û���������Դ򿪸�ģ�飺
            return true;
        } else {
            // ������û��Ѵ򿪸ò�Ʒ������ģ��(������ʹ�øò�Ʒ)������Դ򿪸�ģ�飺
            LinkedList userPowers = (LinkedList) se.getOpenModules().get(userId);
            if (userPowers != null) {
                Iterator it = userPowers.iterator();
                while (it.hasNext()) {
                    nc.vo.sm.user.UserPowerVO userPower = (nc.vo.sm.user.UserPowerVO) it.next();
                    String funcProductCode = getProductCode(userPower.getFunCode());
                    if (funcProductCode.equals(product)) {
                        return true;
                    }

//                    nc.vo.sm.user.UserPowerVO userPower = (nc.vo.sm.user.UserPowerVO) it.next();
//                    if (userPower.getFunCode().startsWith(product)) {
//                        return true;
//                    }
                }
            }
        }
        return false;
    }

    /**
     * �˴����뷽��˵���� �������ڣ�(2001-4-29 16:31:00)
     */
    private Hashtable getIllegalLogin(String dsName) {

        Hashtable illegalLogin = (Hashtable) getIllegalLoginContainer().get(dsName);
        if (illegalLogin == null) {
            illegalLogin = new Hashtable();
            getIllegalLoginContainer().put(dsName, illegalLogin);
        }
        return illegalLogin;
    }

    /**
     * �˴����뷽��˵���� �������ڣ�(2001-11-13 11:37:28)
     * 
     * @return java.util.HashMap
     */
    private HashMap getIllegalLoginContainer() {
    	synchronized (LoginAppBean.class) {
			byte sysid = InvocationInfoProxy.getInstance().getSysid();
			String sysFlag = Byte.toString(sysid);
			HashMap map = (HashMap)m_illegalLoginContainer.get(sysFlag);
			if(map == null){
				map = new HashMap();
				m_illegalLoginContainer.put(sysFlag, map);
			}
			return map;
		}
//        if (m_illegalLoginContainer == null) {
//            m_illegalLoginContainer = new HashMap();
//        }
//        return m_illegalLoginContainer;
    }

    /**
     * �˴����뷽��˵���� �������ڣ�(2001-4-29 16:31:00)
     */
    public Hashtable getLoginUser(String dsName) {

        Hashtable loginUsers = (Hashtable) getLoginUserContainer().get(dsName);
        if (loginUsers == null) {
            loginUsers = new Hashtable();
            getLoginUserContainer().put(dsName, loginUsers);
        }
        return loginUsers;
    }

    /**
     * �˴����뷽��˵���� �������ڣ�(2001-11-13 11:36:22)
     * 
     * @return java.util.HashMap
     */
    private HashMap getLoginUserContainer() {
    	
//        if (m_loginUserContainer == null) {
//            m_loginUserContainer = new HashMap();
//        }
        synchronized (LoginAppBean.class) {
			byte sysid = InvocationInfoProxy.getInstance().getSysid();
			String sysFlag = Byte.toString(sysid);
			HashMap map =(HashMap) m_loginUserContainer.get(sysFlag);
			if(map == null){
				map = new HashMap();
				m_loginUserContainer.put(sysFlag, map);
			}
			return map;
		}
//        return m_loginUserContainer;
    }
   

    /**
     * ���һ����Ʒ����Ȩ��Ŀ�� �������ڣ�(2001-10-18 9:24:01)
     * 
     * @param String
     *            ��Ʒ����
     * @return java.util.HashMap
     */
    private int getProductLicense(String product) {
        // �ò�Ʒ����Ȩ��Ŀ��
//        ILicenseService iILicenseService = (ILicenseService) NCLocator.getInstance().lookup(ILicenseService.class.getName());
    	ICilService service = NCLocator.getInstance().lookup(ICilService.class);
        int intLicNum = service.getProductLicense(product);
        if (intLicNum <= 0)
            // ���û��Ĭ����Ȩ������Ȩ��Ϊ0��
            return 0;
        else
            return intLicNum;
    }

    /**
     * �˴����뷽��˵���� �������ڣ�(2001-12-1 16:02:08)
     */
    private int getLoginUserCount() {

        int userCount = 0;
        Collection tables = getLoginUserContainer().values();
        Iterator it = tables.iterator();
        while (it.hasNext()) {
            userCount += ((Hashtable) it.next()).size();
        }
        return userCount;
    }

    /**
     * ��Ӧ�ͻ��˷��������Ӳ��� �������ڣ�(2001-4-22 15:55:55)
     */
    public boolean onClientConnected(String dsName, String sid, String userIp) {

        Object obj = getLoginUser(dsName).get(sid);
        if (obj != null) {
            LoginSessBean lsb = (LoginSessBean) obj;
            if (lsb.getUserIp().equals(userIp)) {
                lsb.setVoteTime(System.currentTimeMillis());
                return true;
            }
        }
        return false;
    }

    public boolean isLegitimateProduct(String product) {
        // �����Ȩ����
        int license = getProductLicense(product);
        String msg = "licp ====> LoginAppBean::isLegitimateProduct ? product ='" + product + "'  license = " + license;
        Logger.debug(msg);
        if (license > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * �˴����뷽��˵���� �������ڣ�(2001-3-17 11:21:48)
     */
    public  int addLoginUser(LoginSessBean lsb) {
    	if(!lsb.isHideLogin()){
        synchronized (LoginAppBean.class) {
            Hashtable ht = getLoginUser(lsb.getDataSourceName());
            Enumeration enum1 = ht.keys();
            while (enum1.hasMoreElements()) {
                String s = (String) enum1.nextElement();
                LoginSessBean l = (LoginSessBean) ht.get(s);
                if (l.isLogin() && l.getUserId().equals(lsb.getUserId())) {
                    // �û��Ѵ���
                    if (lsb.isForcedLogin()) {
                        String dsName = l.getDataSourceName();
                        String userId = l.getUserId();
                        String sid = l.getSID();
                        try {
                            SFServiceFacility.getSMVerifyService().stopUser(dsName, userId, sid);
                            break;
                        } catch (BusinessException e) {
                            Logger.error("Error", e);
                            return USER_EXIST;
                        }

                    } else {
                        return USER_EXIST;
                    }
                }
            }
        }
  	}
        // }
        // ������ˮ�ţ�
        String sid = getSID();
        lsb.setSID(sid);
        // ����û���
        lsb.setIsLogin(true);
        lsb.setLoginTime(System.currentTimeMillis());
        lsb.setVoteTime(System.currentTimeMillis());
        if(!lsb.isHideLogin())
        	getLoginUser(lsb.getDataSourceName()).put(sid, lsb);
        return LOGIN_OK;
    }

    /**
     * �ж�ĳ��������ĳ���û��Ƿ����ߡ� ���߷���true,�����߷���false �������ڣ�(2001-3-17 11:21:48)
     */
    public boolean checkUserOnline(String dsName, String userid) {
        boolean isOnline = false;
        Hashtable ht = getLoginUser(dsName);
        if (ht != null) {
            Enumeration enums = ht.keys();
            while (enums.hasMoreElements()) {
                String s = (String) enums.nextElement();
                LoginSessBean l = (LoginSessBean) ht.get(s);
                if ( l!=null && l.isLogin() && l.getUserId().equals(userid)) {
                    isOnline = true;
                    break;
                }
            }
        }
        return isOnline;
    }

    /**
     * ��һ�������в���һ�����׹���Ա ���û���ҵ����ͷ���null�� �������ڣ�(2003-6-16 10:12:30)
     * modified by ewei ���passwordlevel��starttime�������Ե�ת�� 
     * @return nc.vo.sm.UserVO
     * @param account
     *            nc.vo.sm.config.Account
     */
    public UserVO findAccountAdmFromAccount(Account account, String userCode) {
        UserVO user = null;
        if (account != null) {
            AccountAdm[] adms = account.getAryAccountAdms();
            for (int i = 0; i < adms.length; i++) {
                if (adms[i].getAccountAdmCode().equals(userCode)) {
                    user = new UserVO();
                    user.setUserCode(userCode);
                    user.setPrimaryKey(adms[i].getAccountAdmCode());
                    user.setUserName(adms[i].getAccountAdmName());
                    user.setUserPassword(getCodeTool().encode(adms[i].getPassword()));
                    user.setLocked(new UFBoolean(adms[i].isLocked()));
                    user.setAbleDate(adms[i].getEffectDate());
                    user.setDisableDate(adms[i].getExpireDate());
                    user.setPwdlevelcode(adms[i].getPasswordLevel());
                    user.setPwdparam(adms[i].getStarttime());
                    user.setIsCa(new UFBoolean(adms[i].isCa()));
                    user.setCorpId(ACC_ADM_PK_CORP);// ���׹���Ա��˾idΪ��0001��
                    break;
                }
            }
        }
        return user;
    }

    /**
     * �������ļ��в������ס� û���ҵ��ͷ���null �������ڣ�(2003-6-16 9:53:15)
     * 
     * @return nc.vo.sm.config.Account
     * @param accountCode
     *            java.lang.String
     */
    public Account findAccountFromXml(String accountCode) {
        Account retrAccount = null;
        if (accountCode != null) {
            ConfigParameter config = null;
            try {
                config = AccountXMLUtil.getConfigParameter();// AccountBO_Client.getConfigParameter();
            } catch (Exception e) {
                Logger.error("nc.bs.sm.login.LoginAppBean.findAccountFromXml(String)������ȡ�����ļ�ʱ����");
                Logger.error("Error",e);
            }
            Account[] accounts = config.getAryAccounts();
            int accountsCount = accounts == null ? 0 : accounts.length;
            for (int i = 0; i < accountsCount; i++) {
                if (accountCode.equals(accounts[i].getAccountCode())) {
                    retrAccount = accounts[i];
                    break;
                }
            }
        }
        if (retrAccount == null) {
            Logger.warn("�ڲ���accountCode=" + accountCode + "������ʱ��û���ҵ������ס�");
        }
        return retrAccount;
    }

    private Encode getCodeTool() {
        if (coder == null)
            coder = new Encode();
        return coder;
    }

    public static LoginAppBean getInstance() {
        return m_instance;
    }

    /**
     * �˴����뷽��˵���� �������ڣ�(2001-10-19 10:11:08)
     * 
     * @return java.lang.String
     * @param moduleCode
     *            java.lang.String
     */
    public static String getProductCode(String moduleCode) {
        ICilService iILicenseService =  NCLocator.getInstance().lookup(ICilService.class);
        String retrCode = iILicenseService.getProductCode(moduleCode);
        String msg = "licp ===> get product lisnece code , moduleCode = '" + moduleCode + "' .  lisence code ='" + retrCode + "'";
        Logger.debug(msg);
        return retrCode;
    }

    /**
     * �õ�һ��nonce����Ϊsid�š� �������ڣ�(2002-11-13 10:59:23)
     * 
     * @return java.lang.String
     */
    private synchronized String getSID() {
        long sid = getRandom().nextLong();
        long currentTimeMillis = System.currentTimeMillis();
        return new StringBuffer("").append(currentTimeMillis).append(sid).toString();
    }

    /**
     * ��֤�û���ݵķ����� �������ڣ�(2003-6-16 14:33:32)
     * 
     * @return int
     * @param lsb
     *            nc.vo.sm.login.LoginSessBean
     * @param userVO
     *            nc.vo.sm.UserVO
     */
    private int identityVerify(LoginSessBean lsb, UserVO user) {
        if (user != null) { // ˵���û�����ȷ
            lsb.setUserId(user.getPrimaryKey());
            lsb.setUserName(user.getUserName());

            // if (lsb.isLogin()) {
            // return LoginFailureInfo.LOGIN_LEGALIDENTITY;
            // }
            //
            String iaCode = user.getIdentityVerifyCode();
            Boolean isAlwaysStaticPWD = (Boolean) tl.get();

            nc.vo.sm.identityverify.IAConfVO conf = null;
            if (isAlwaysStaticPWD != null && isAlwaysStaticPWD.booleanValue()) {
                conf = IAModeFactory.getInstance().getDefaultIAConf();
            } else {
                conf = IAModeFactory.getInstance().getIAConfByCode(iaCode);
            }
            IAMode mode = IAModeFactory.getInstance().getIAModeByConf(conf);
            lsb.put(IAConfVO.class.getName(), conf);
            //ewei+ �˴������һ�����ԣ������ж��Ƿ�ʹ��caǩ��
            lsb.put("busi_ifcauser", user.getIsCa());
            String log = ">>>�û�" + user.getUserName() + "����ʹ�õ���֤��ʽΪ��" + iaCode + "��\nʵ��ʹ�õ���֤��ʽΪ��" + conf.getIdentityVerifyCode();
            Logger.debug(log);
            //
            int retr = LoginFailureInfo.UNKNOWN_ERROR;
            if (lsb.isLogin()) {
                retr = LoginFailureInfo.LOGIN_LEGALIDENTITY;
            } else {
                retr = mode.verify(lsb, user);
            }
            //
            lsb.put("USERVO", user);
            if (!conf.isStaticPWDMode()) {
                // �����֤��ʽ�಻��Ĭ�ϵľ�̬������֤��ʽ���ͽ����ݿ��еľ�̬������lsb��
                lsb.setPassword(getCodeTool().decode(user.getUserPassword()));
            }
            return retr;
        } else { // ˵���û����ƴ���
            return LoginFailureInfo.NAME_WRONG;
        }

    }

    /**
     * �����û����ε�½����½ʧ�ܴ������ࣩ �������ڣ�(2001-3-17 11:21:48)
     */
    private void lockAccAdm(int failType, LoginFailureInfo fail, LoginSessBean lsb) {

        // ��web server�ϴ����¼ʧ�ܣ�
        if (failType == LoginFailureInfo.NAME_RIGHT_PWD_WRONG) {
            if (!lockAccAdmFromXml(failType, fail, lsb)) {
                lockUser(failType, fail, lsb);
            }
        }
    }

    /**
     * �����û����ε�½����½ʧ�ܴ������ࣩ �������ڣ�(2001-3-17 11:21:48)
     */
    private boolean lockAccAdmFromXml(int failType, LoginFailureInfo fail, LoginSessBean lsb) {

        // �����û���
        String accountCode = lsb.getAccountId();// request.getParameter("accountcode");
        String userCode = lsb.getUserCode();// request.getParameter("usercode");
        String password = lsb.getPassword();// request.getParameter("pwd");
        ConfigParameter config = null;
        try {
            config = AccountXMLUtil.getConfigParameter();// AccountBO_Client.getConfigParameter();
        } catch (Exception e) {
            Logger.error("Error",e);
        }
        Account[] accounts = config.getAryAccounts();
        int accPos = -1;
        for (int i = 0; i < accounts.length; i++) {
            if (accountCode.equals(accounts[i].getAccountCode())) {
                accPos = i;
                break;
            }
        }
        AccountAdm[] adms = accounts[accPos].getAryAccountAdms();
        int pos = -1;
        for (int i = 0; i < adms.length; i++) {
            if (adms[i].getAccountAdmCode().equals(userCode)) {
                pos = i;
                break;
            }
        }
        
        // Xml�в����ڸ��û�:
        if (pos == -1) {
            return false;
        } else {
        	int maxfailure = getMaxFailure(adms[pos]);
        		if (!adms[pos].isLocked()) {
        		LinkedList info = null;
            	// 1 û��ʧ�ܼ�¼����ʾ���û��ĵ�һ�ε�¼ʧ��
            	if ((info = (LinkedList) getIllegalLogin(lsb.getDataSourceName()).get(adms[pos].getAccountAdmCode())) == null) {
                	info = new LinkedList();
                	info.addLast(fail);
                	//
                	getIllegalLogin(lsb.getDataSourceName()).put(adms[pos].getAccountAdmCode(), info);
                	return true;
            	}
            	// 2 ���е�¼ʧ�ܵļ�¼��
            	int failNum = info.size();
            	// 2.1 ʧ�ܴ���С�����������
            	if (failNum < maxfailure) {
                	info.addLast(fail);
                	getIllegalLogin(lsb.getDataSourceName()).put(adms[pos].getAccountAdmCode(), info);
            	}
            	// 2.2 ʧ�ܴ����ﵽ���������
            	else {
            		// 2.2.1 �����û���
                	try {
                    	SFServiceFacility.getServiceProviderService().addLockedUser(lsb);
                    	adms[pos].setLocked(new UFBoolean(true));
                    	AccountXMLUtil.updateAdmLockedTag(config);// AccountBO_Client.updateAdmLockedTag(config);

                	} catch (Exception e) {
                		Logger.error("Error",e);
                	}
                	// 2.3 ���ʧ�ܼ�¼��
                	getIllegalLogin(lsb.getDataSourceName()).remove(adms[pos].getAccountAdmCode());
            	}
        	}
        }
        return true;
    }

    /**
     * �����û����ε�½����½ʧ�ܴ������ࣩ �������ڣ�(2001-3-17 11:21:48)
     */
    private void lockSysAdm(int failType, LoginFailureInfo fail, LoginSessBean lsb) {

        // ��web server�ϴ����¼ʧ�ܣ�
        if (failType == LoginFailureInfo.NAME_RIGHT_PWD_WRONG) {
            // 1. �����û���
            String userCode = lsb.getUserCode();// request.getParameter("usercode");
            ConfigParameter config = null;
            try {
                config = AccountXMLUtil.getConfigParameter();// AccountBO_Client.getConfigParameter();
            } catch (Exception e) {
                Logger.error("Error",e);
            }

            SysAdm[] sysAdms = config.getArySysAdms();
            int pos = -1;
            for (int i = 0; i < sysAdms.length; i++) {
                if (sysAdms[i].getSysAdmCode().equals(userCode)) {
                    pos = i;
                    break;
                }
            }
            // 2.�����û�������
            if (!sysAdms[pos].isLocked()) {
                LinkedList info = null;
                // 4.1 û��ʧ�ܼ�¼����ʾ���û��ĵ�һ�ε�¼ʧ��
                if ((info = (LinkedList) getIllegalLogin(lsb.getDataSourceName()).get(sysAdms[pos].getSysAdmCode())) == null) {
                    info = new LinkedList();
                    info.addLast(fail);
                    //
                    getIllegalLogin(lsb.getDataSourceName()).put(sysAdms[pos].getSysAdmCode(), info);
                    return;
                }
                // 4.2 ���е�¼ʧ�ܵļ�¼��
                int failNum = info.size();
                // 4.2.1 ʧ�ܴ���С�����������
                if (failNum < m_allowedMaxFailure) {
                    info.addLast(fail);
                    getIllegalLogin(lsb.getDataSourceName()).put(sysAdms[pos].getSysAdmCode(), info);
                }
                // 4.2.2 ʧ�ܴ����ﵽ���������
                else {
                    // 4.2.2.1 �����û���
                    try {
                        // ServiceProviderBO_Client.addLockedUser(lsb);
                        SFServiceFacility.getServiceProviderService().addLockedUser(lsb);
                        sysAdms[pos].setLocked(new UFBoolean(true));
                        AccountXMLUtil.updateAdmLockedTag(config);// AccountBO_Client.updateAdmLockedTag(config);
                    } catch (Exception e) {
                        Logger.error("Error",e);
                    }
                    // 4.2.2 ���ʧ�ܼ�¼��
                    getIllegalLogin(lsb.getDataSourceName()).remove(sysAdms[pos].getSysAdmCode());
                }
            }
        }
    }

    /**
     * �����û����ε�½����½ʧ�ܴ������ࣩ �������ڣ�(2001-3-17 11:21:48)
     */
    private void lockUser(int failType, LoginFailureInfo fail, LoginSessBean lsb) {
        String pk_corp = lsb.getPk_crop();
        String userCode = lsb.getUserCode();
        String dsName = lsb.getDataSourceName();
        if (pk_corp.equals("")) {
            pk_corp = CommonConstant.GROUP_CODE;
        }
        // ��web server�ϴ����¼ʧ�ܣ�
        if (failType == LoginFailureInfo.NAME_RIGHT_PWD_WRONG) {
            // �����û���
            UserVO user = getUser(pk_corp, userCode, dsName);// getUser(request.getParameter("pk_corp"),
            //
            int maxfailure = getMaxFailure(user);
            if (!user.getLocked().booleanValue()) {
                LinkedList info = null;
                // 4.1 û��ʧ�ܼ�¼����ʾ���û��ĵ�һ�ε�¼ʧ��
                if ((info = (LinkedList) getIllegalLogin(lsb.getDataSourceName()).get(user.getPrimaryKey())) == null) {
                    info = new LinkedList();
                    info.addLast(fail);
                    //
                    getIllegalLogin(lsb.getDataSourceName()).put(user.getPrimaryKey(), info);
                    Logger.warn("��¼ʧ��1��" + user.getUserCode());
                    return;
                }
                // 4.2 ���е�¼ʧ�ܵļ�¼��
                int failNum = info.size();
                Logger.warn("��¼ʧ�ܣ�" + user.getUserCode() + "Count" + failNum);
                // 4.2.1 ʧ�ܴ���С�����������
                if (failNum < maxfailure) {
                    info.addLast(fail);
                    getIllegalLogin(lsb.getDataSourceName()).put(user.getPrimaryKey(), info);
                }
                // 4.2.2 ʧ�ܴ����ﵽ���������
                else {
                    // 4.2.2.1 �����û���
                    try {
                        SFServiceFacility.getServiceProviderService().addLockedUser(lsb);
                        user.setLocked(new UFBoolean(true));
                        SFServiceFacility.getIUserManage().updateLockedTag(user);
                    } catch (Exception e) {
                        Logger.error("Error",e);
                    }
                    // 4.2.2 ���ʧ�ܼ�¼��
                    getIllegalLogin(lsb.getDataSourceName()).remove(user.getPrimaryKey());
                }
            }
        }
    }

    /**
     * �û���¼
     */
    public int login(LoginSessBean lsb) throws SystemFrameworkException {
    	return login(lsb, false);
    }
    public int login(LoginSessBean lsb, boolean alwaysStaticPWD) throws SystemFrameworkException {
    	String dsName = lsb.getDataSourceName();
    	InvocationInfoProxy.getInstance().setUserDataSource(dsName);
    	//
        int result = verify(lsb, alwaysStaticPWD);
        //
        if (result == LoginFailureInfo.LOGIN_SUCCESS) {
            // �����û���Ϊ׼����¼��״̬��
            int status = addLoginUser(lsb);
            //
            if (status == USER_EXIST) {
                result = LoginFailureInfo.ALREADY_ONLINE;
            } else if (status == OVER_MAX_USER) {
                result = LoginFailureInfo.OVER_MAX_USER;
            } else if (status == LOGIN_OK) {
                result = LoginFailureInfo.LOGIN_SUCCESS;
            }
        }

        if (result == LoginFailureInfo.LOGIN_SUCCESS) {
            onLoginSuccess(lsb);
        } else {
            onLoginFailure(result, lsb);
        }
        return result;
    }

    private ThreadLocal tl = new ThreadLocal();

    /**
     * У���¼��Ϣ
     * 
     * @param lsb
     * @return
     * @throws SystemFrameworkException
     */
    public int verify(LoginSessBean lsb, boolean alwaysStaticPWD) throws SystemFrameworkException {
        String accountCode = lsb.getAccountId();
//        String dsName = lsb.getDataSourceName();
//        String pk_corp = lsb.getPk_crop();
//        String userCode = lsb.getUserCode();
//        String password = lsb.getPassword();
        // 1. ����������Ч��:
        UFDate today = null;
        try {
            today = SFServiceFacility.getServiceProviderService().getServerTime().getDate();
        } catch (Exception e) {
            Logger.error("Error",e);
            throw new SystemFrameworkException(e.getMessage());
        }
        //���ҵ������
        String workDate = lsb.getWorkDate();
        UFDate date = new UFDate(workDate);
        String pkCorp = lsb.getPk_crop();
        if ((pkCorp != null) && (!pkCorp.trim().equals("")) && (!pkCorp.trim().equals(nc.ui.pub.CommonMark.GROUP_CODE))) {
            try {
                boolean isValideDate = false;
                isValideDate = SFServiceFacility.getConfigService().isValidDate(date);
                if (!isValideDate) {
                    return ILoginResult.ILLEGAL_ACCPRERIOD;//ҵ�����ڲ���ϵͳ����Ļ���ڼ�֮��
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SystemFrameworkException(e.getMessage());        
            }
        }
        //
        int result = checkAccount(accountCode, today);
        // ������Ч����
        if (result == LoginFailureInfo.LOGIN_SUCCESS) {
            // 2. �����û���¼:
            if (alwaysStaticPWD) {
                tl.set(new Boolean(true));
            }
            result = loginValidate(lsb);
        }
        return result;
    }

    /**
     * �˴����뷽��˵���� �������ڣ�(2001-11-10 15:41:56)
     * 
     * @return int
     * @param request
     *            javax.servlet.http.HttpServletRequest
     */
    private int loginValidate(LoginSessBean lsb) {

        String dsName = lsb.getDataSourceName();
        String pk_corp = lsb.getPk_crop();
        //
        int validateResult = 0;
        // ϵͳ����Ա��
        if (dsName.equals(SYS_ADM_DATASOURCE)) {
            lsb.setUserType(LoginSessBean.SYSTEM_ADM);
            validateResult = loginValidateSysAdm(lsb);
        }
        // ���׹���Ա��
        else if (pk_corp.trim().equals(ACC_ADM_PK_CORP) || pk_corp.trim().equals("")) {
            lsb.setUserType(LoginSessBean.ACCOUNT_ADM);
            validateResult = loginValidateAccAdm(lsb);
        }
        // ��ͨ�û���
        else {
            lsb.setUserType(LoginSessBean.USER);
            validateResult = loginValidateUser(lsb);
        }
        //
        return validateResult;
    }

    /**
     * �˴����뷽��˵���� �������ڣ�(2001-11-10 15:41:56)
     * 
     * @return int
     * @param request
     *            javax.servlet.http.HttpServletRequest
     * @param reponse
     *            javax.servlet.http.HttpServletResponse
     */
    private int loginValidateAccAdm(LoginSessBean lsb) {

        int result = loginValidateAccAdmFromXml(lsb);
        if (result == LoginFailureInfo.LOGIN_SUCCESS) {
            lsb.setAccAdmType(LoginSessBean.ACCOUNT_ADM_FROM_XML);
        }
        if (result == LoginFailureInfo.NAME_WRONG) {
            result = loginValidateUser(lsb);
            if (result == LoginFailureInfo.LOGIN_SUCCESS) {
                lsb.setAccAdmType(LoginSessBean.ACCOUNT_ADM_FROM_DATABASE);
            }
        }
        return result;
    }

    /**
     * �˴����뷽��˵���� �������ڣ�(2001-11-10 15:41:56)
     * 
     * @return int
     * @param request
     *            javax.servlet.http.HttpServletRequest
     * @param reponse
     *            javax.servlet.http.HttpServletResponse
     */
    private int loginValidateAccAdmFromXml(LoginSessBean lsb) {
        String accountCode = lsb.getAccountId();
        String userCode = lsb.getUserCode();
        lsb.setCorpName(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("smcomm", "UPP1005-000005")/* @res "����" */);
        //
        Account account = findAccountFromXml(accountCode);
        UserVO accountAdm = findAccountAdmFromAccount(account, userCode);
        if (accountAdm==null){
        	return LoginFailureInfo.NAME_WRONG;
        }
        //�ж�������ǰ
        if (accountAdm.getLocked().booleanValue()) {
            return LoginFailureInfo.USER_LOCKED;
        }
        int identtityVerifyResult = identityVerify(lsb, accountAdm);
        if (identtityVerifyResult != LoginFailureInfo.LOGIN_LEGALIDENTITY) {
            return identtityVerifyResult;
        }
        //
        UFDate today = null;
        try {
            today = SFServiceFacility.getServiceProviderService().getServerTime().getDate();
        } catch (Exception e) {
            Logger.error("Error",e);
        }       
        if (accountAdm.getAbleDate() != null) {
            if (today.before(accountAdm.getAbleDate())) {
                return LoginFailureInfo.USER_BEFORE_EFFECT;
            }
        }
        if (accountAdm.getDisableDate() != null) {
            if (today.after(accountAdm.getDisableDate().getDateBefore(1))) {
                return LoginFailureInfo.USER_EXPIRED;
            }
        }
        return LoginFailureInfo.LOGIN_SUCCESS;
    }

    /**
     * �˴����뷽��˵���� �������ڣ�(2001-11-10 15:41:56)
     * 
     * @return int
     * @param request
     *            javax.servlet.http.HttpServletRequest
     * @param reponse
     *            javax.servlet.http.HttpServletResponse
     */
    private int loginValidateSysAdm(LoginSessBean lsb) {

        lsb.setCorpName(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("smcomm", "UPP1005-000005")/* @res "����" */);

        //
        ConfigParameter config = null;
        try {
            config = AccountXMLUtil.getConfigParameter();// AccountBO_Client.getConfigParameter();
        } catch (Exception e) {
            Logger.error("Error",e);
        }
        String userCode = lsb.getUserCode();
        String password = lsb.getPassword();

        SysAdm[] sysAdms = config.getArySysAdms();
        int pos = -1;
        for (int i = 0; i < sysAdms.length; i++) {
            if (sysAdms[i].getSysAdmCode().equals(userCode)) {
                pos = i;
                break;
            }
        }
        // �û�����
        if (pos == -1) {
            return LoginFailureInfo.NAME_WRONG;
        }
        //�û�������
        if (sysAdms[pos].isLocked()) {
            return LoginFailureInfo.USER_LOCKED;
        }
        
        UserVO user = new UserVO();
        user.setIdentityVerifyCode(IAModeFactory.getInstance().getSysAdmIACode());
        user.setUserCode(sysAdms[pos].getSysAdmCode());
        user.setPrimaryKey(sysAdms[pos].getSysAdmCode());
        user.setUserName(sysAdms[pos].getSysAdmName());
        user.setUserPassword(getCodeTool().encode(sysAdms[pos].getPassword()));
        user.setLocked(new UFBoolean(sysAdms[pos].isLocked()));
        user.setAbleDate(new UFDate("1900-1-1"));
        user.setDisableDate(new UFDate("9999-12-31"));

            lsb.setUserId(user.getPrimaryKey());
            lsb.setUserName(user.getUserName());
            String iaCode = user.getIdentityVerifyCode();
            Boolean isAlwaysStaticPWD = (Boolean) tl.get();

            nc.vo.sm.identityverify.IAConfVO conf = null;
            if (isAlwaysStaticPWD != null && isAlwaysStaticPWD.booleanValue()) {
                conf = IAModeFactory.getInstance().getDefaultIAConf();
            } else {
                conf = IAModeFactory.getInstance().getSysAdmIAConf();
            }
            IAMode mode = IAModeFactory.getInstance().getIAModeByConf(conf);
            lsb.put(IAConfVO.class.getName(), conf);
            String log = ">>>�û�" + user.getUserName() + "����ʹ�õ���֤��ʽΪ��" + iaCode + "��\nʵ��ʹ�õ���֤��ʽΪ��" + conf.getIdentityVerifyCode();
            Logger.debug(log);
            //
            int retr = LoginFailureInfo.UNKNOWN_ERROR;
            if (lsb.isLogin()) {
                retr = LoginFailureInfo.LOGIN_LEGALIDENTITY;
            } else {
                retr = mode.verify(lsb, user);
            }
            //
            lsb.put("USERVO", user);
            if (!conf.isStaticPWDMode()) {
                // �����֤��ʽ�಻��Ĭ�ϵľ�̬������֤��ʽ���ͽ����ݿ��еľ�̬������lsb��
                lsb.setPassword(getCodeTool().decode(user.getUserPassword()));
            }
            if(retr == LoginFailureInfo.LOGIN_LEGALIDENTITY){
            	retr = LoginFailureInfo.LOGIN_SUCCESS;
            }
            return retr;
///////////
//        String message = null;
//        // �û��������붼��
//        if (pos == -1) {
//            return LoginFailureInfo.NAME_WRONG;
//        }
//        // �û�����ȷ���������
//        else if (!sysAdms[pos].getPassword().equals(password)) {
//            return LoginFailureInfo.NAME_RIGHT_PWD_WRONG;
//        }
//        // ��������ȷ���û���������������
//        lsb.setUserId(sysAdms[pos].getSysAdmCode());
//        lsb.setUserName(sysAdms[pos].getSysAdmName());
//        if (sysAdms[pos].isLocked()) {
//            return LoginFailureInfo.USER_LOCKED;
//        }
//        //
//        return LoginFailureInfo.LOGIN_SUCCESS;
    }

    /**
     * �˴����뷽��˵���� �������ڣ�(2001-11-10 15:41:56)
     * 
     * @return int
     * @param request
     *            javax.servlet.http.HttpServletRequest
     * @param reponse
     *            javax.servlet.http.HttpServletResponse
     */
    private int loginValidateUser(LoginSessBean lsb) {
        //
        String pk_corp = lsb.getPk_crop(); // request.getParameter("pk_corp");
        String dsName = lsb.getDataSourceName();
        String userCode = lsb.getUserCode();
        if (pk_corp.equals("")) {
            pk_corp = CommonConstant.GROUP_CODE;
        }
        // ���ù�˾��Ϣ��
        try {
            CorpVO corp = SFServiceHelper.getCorpQryService().findCorpVOByPK(pk_corp, dsName);

            if (corp != null) {
                lsb.setCorpName(corp.getUnitname());
            } else {
                lsb.setCorpName(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("smcomm", "UPP1005-000005")/*
                                                                                                                 * @res "����"
                                                                                                                 */);
            }
        } catch (Exception e) {
            Logger.error("Error",e);
        }
        UserVO user = getUser(pk_corp, userCode, dsName);
        
        // �û���������
        if (user==null){
        	return LoginFailureInfo.NAME_WRONG;
        }
        if (user.getLocked().booleanValue()) {
            return LoginFailureInfo.USER_LOCKED;
        }

        try {
        	SQLParameter parameter = new SQLParameter();
        	parameter.addParam(user.getPrimaryKey());
        	Boolean isAdmin = (Boolean)new BaseDAO().executeQuery("select 1 from sm_user_role where cuserid=? and pk_role='COMPANYADMINISTRATOR' and coalesce(dr,0)=0 ", parameter, new ResultSetProcessor() {
        		public Boolean handleResultSet(ResultSet rs) throws SQLException {
        			if(rs.next()){
        				return true;
        			}
        			return false;
        		}
        	});
        	if(!isAdmin){
        		boolean ldapauth = LdapHelper.authenticate(userCode, lsb.getPassword());
        		if(!ldapauth){
        			return LoginFailureInfo.NAME_WRONG;
        		}
        		if(!"true".equals(LdapHelper.getLdapXml().getIsauth())){//������Ldap����ҪУ��nc����
        			lsb.setPassword(lsb.getPassword());
   			 	}else{
        			lsb.setPassword(getCodeTool().decode(user.getUserPassword()));
        		}
        	}
        } catch (DAOException e) {
        	Logger.error(e.getMessage());
        	e.printStackTrace();
        }
        int identtityVerifyResult = identityVerify(lsb, user);
        if (identtityVerifyResult != LoginFailureInfo.LOGIN_LEGALIDENTITY) {
            return identtityVerifyResult;
        }

        //
        UFDate today = null;
        try {
            today = SFServiceFacility.getServiceProviderService().getServerTime().getDate();
        } catch (Exception e) {
            Logger.error("Error",e);
        }
        
        // �˺���δ����Ч��
        if (user.getAbleDate() != null) {
            if (today.before(user.getAbleDate())) {
                return LoginFailureInfo.USER_BEFORE_EFFECT;
            }
        }
        // �˺��ѹ���Ч��
        if (user.getDisableDate() != null) {
            if (today.after(user.getDisableDate().getDateBefore(1))) {
                return LoginFailureInfo.USER_EXPIRED;
            }
        }
        return LoginFailureInfo.LOGIN_SUCCESS;
    }

    /**
     * �����û����ε�½����½ʧ�ܴ������ࣩ �������ڣ�(2001-3-17 11:21:48)
     */
    private void onLoginFailure(int failType, LoginSessBean lsb) {
    	if(lsb.isHideLogin())
    		return;
        String dsName = lsb.getDataSourceName();
        String pk_corp = lsb.getPk_crop();
        // 1. ��õ�¼ʧ�ܵ�������Ϣ��
        LoginFailureInfo fail = new LoginFailureInfo();
        fail.setFailType(failType);
        fail.setRemoteHost(lsb.getRemoteHost());
        fail.setServerName(lsb.getServerName());
        fail.setServerPort(lsb.getServerPort());
        fail.setUserIp(lsb.getUserIp());
        fail.setLoginAccount(lsb.getAccountId());
        fail.setUserId(lsb.getUserId());
        fail.setLoginUserCode(lsb.getUserCode());
        fail.setLoginPassword(lsb.getPassword());
        fail.setLoginCorp(lsb.getCorpCode());
        fail.setPk_corp(lsb.getPk_crop());
        try {
            fail.setLoginTime(SFServiceFacility.getServiceProviderService().getServerTime());
        } catch (Exception e) {
            Logger.error("Error",e);
        }
        switch (failType) {
        case LoginFailureInfo.ALREADY_ONLINE:
        case LoginFailureInfo.OVER_MAX_USER:
        case LoginFailureInfo.USER_LOCKED:
        case LoginFailureInfo.NAME_RIGHT_PWD_WRONG:
            fail.setUserId(lsb.getUserId());
            fail.setUserCode(lsb.getUserCode());
            fail.setUserName(lsb.getUserName());
        }

        // 2. �ڷ������˼�¼��¼ʧ�ܵ���Ϣ��
        try {
            SFServiceFacility.getServiceProviderService().addLoginFailureInfo(fail);
        } catch (Exception e) {
            Logger.error("Error",e);
        }
        // 3.��Բ�ͬ�����û��Ĵ���
        // ϵͳ����Ա��
        if (dsName.equals(SYS_ADM_DATASOURCE)) {
            lockSysAdm(failType, fail, lsb);
        }
        // ���׹���Ա��
        else if (pk_corp.trim().equals(ACC_ADM_PK_CORP) || pk_corp.trim().equals("")) {
            lockAccAdm(failType, fail, lsb);
        }
        // ��ͨ�û���
        else {
            lockUser(failType, fail, lsb);
        }

    }

    /**
     * �˴����뷽��˵���� �������ڣ�(2001-11-10 15:48:52)
     * 
     * @param request
     *            javax.servlet.http.HttpServletRequest
     * @param response
     *            javax.servlet.http.HttpServletResponse
     */
    private void onLoginSuccess(LoginSessBean lsb) {
    	if(!lsb.isHideLogin()){
        // ע�ᵽ�������ϣ�
        try {
            SFServiceFacility.getServiceProviderService().addLoginUser(lsb);
        } catch (Exception e) {
            Logger.error("Error",e);
        }
        //һ����¼�ɹ����ͽ���ǰ��¼ʧ�ܵļ�¼���
        UserVO user = (UserVO)lsb.get("USERVO");
        if(user != null){
            getIllegalLogin(lsb.getDataSourceName()).remove(user.getPrimaryKey());
        }
        // ������־��
        if (lsb.getUserType().equals(LoginSessBean.ACCOUNT_ADM) || lsb.getUserType().equals(LoginSessBean.USER)) {
            nc.vo.sm.log.OperatelogVO log = new nc.vo.sm.log.OperatelogVO();
            try {
                nc.vo.bd.CorpVO corp = null;
                String pk_corp = lsb.getPk_crop();
                try {
                   
                    String dsName = lsb.getDataSourceName();
                    corp = SFServiceHelper.getCorpQryService().findCorpVOByPK(pk_corp, dsName);// new
                } catch (Exception e) {
                    Logger.error("Error",e);
                }
                if (corp != null) {
                    log.setCompanyname(corp.getUnitname());
                } else {
                    log.setCompanyname(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("smcomm", "UPP1005-000005")/* @res "����" */);
                }
                log.setPKCorp(pk_corp);
                String loginTime = SFServiceFacility.getServiceProviderService().getServerTime().toString();// new
                log.setLoginTime(loginTime);
                log.setOperatorId(lsb.getUserId());
                log.setEnterip(lsb.getUserIp());
                log.setOperatetype(nc.vo.sm.log.OperatelogVO.LOGIN_NC);
                log.setOpratorname(lsb.getUserCode());
            } catch (Exception e) {
                Logger.error("Error",e);
            }
            try {
                String key = SFServiceFacility.getOperateLogService().insert(log, lsb.getDataSourceName());// new
                log.setPrimaryKey(key);
                lsb.put("_login_log_", log);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }}
        /***************************************************************************************************************************************************************************************************************************************************************************************************
         * add by leijun 10/10/2003 ʵ�ֿͻ���Զ�̵��õĺϲ�
         */

        // ��ȷ���ֵ�ǰ��¼�û������
        boolean isUser = lsb.getUserType() == LoginSessBean.USER ? true : false;
        boolean isAccAdm = lsb.getAccAdmType() == LoginSessBean.ACCOUNT_ADM_FROM_XML ? true : false;

        ConfigParser cp = ConfigParserOnServer.getConfigParser();
        if (cp != null) {
            lsb.put("CONFIGPARSER", cp);
            Logger.debug("### ��ȡCONFIGPARSER�ɹ���");
        }

        String dbName = lsb.getDataSourceName();
        if (dbName == null || dbName.equals("")) {
        } else {
            try {
                if (isUser && !"".equals(lsb.getPk_crop()) ) {
                    Logger.debug("@@@ �û������׹���Ա");
                    CorpVO cVo = SFServiceHelper.getCorpQryService().findCorpVOByPK(lsb.getPk_crop(), dbName);
                    //nc.vo.bd.CorpVO cVo = new InitLogin().fetchCorpVO(lsb.getPk_crop(), dbName);
                    if (cVo != null) {
                        lsb.put("CORPVO", cVo);
                        Logger.debug("### ��ȡCORPVO�ɹ���");
                    }
                    
                    //XXX:add by liujian2008-1-10
                    fetchYearAndMonthVO(lsb);
                    fetchPoweredYearAndMonthVO(lsb);
                }
                if (isUser) {
                    String userId = lsb.getUserId();
                    CustomMenuShortcutVO conditionVO = new CustomMenuShortcutVO();
                    conditionVO.setUserId(userId);
                    CustomMenuShortcutVO[] shortcuts = SFServiceFacility.getCustomMenuQueryService().queryShortcutsByVO(conditionVO, Boolean.TRUE);
                    lsb.put("shortcutVO", shortcuts);
                    CustomMenuItemVO condVo = new CustomMenuItemVO();
                    condVo.setUserid(userId);
                    CustomMenuItemVO[] itemVOs = SFServiceFacility.getCustomMenuQueryService().queryButtonsByVO(condVo, Boolean.TRUE);
                    lsb.put("menuitemVO", itemVOs);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

    }

    private void fetchPoweredYearAndMonthVO(LoginSessBean lsb) {
    	String sCorpPK = lsb.getPk_crop();
    	if ("0001".equals(sCorpPK)) {
  			//����û��Ĭ���˲�
  			return ;
  		}
  		String pk_MainglorgbookOfCurrentCorp = BDGLOrgBookAccessor.getDefaultPk_GLOrgBook(sCorpPK);//��˾���˲�
  		String sOrgbook = null;
  		try {
  			IFuncPower iPower = (IFuncPower) NCLocator.getInstance().lookup(IFuncPower.class.getName());
  			PowerResultVO voRet = iPower.queryPowerOrgByUserCorp(lsb.getUserId(), sCorpPK);
  			if (voRet.isPowerControl()) {//������Ȩ�޿��ƣ�����Ҵ����˲��Ƿ���Ȩ�ޣ����û�У���ȡһ����Ȩ�޵������˲�
  				String[] sPowerOrg = voRet.getPowerId();
  				if (sPowerOrg != null && sPowerOrg.length > 0) {
  					int i = 0;
  					for (; i < sPowerOrg.length; i++) {
  						GlorgbookVO vo = BDGLOrgBookAccessor.getGlOrgBookVOByPrimaryKey(sPowerOrg[i]);
  						if (vo.isSeal() || !vo.isBeginUse())//���������ܵ�¼������û������Ҳ���ܵ�¼
  							continue;
  						sOrgbook = sPowerOrg[i];
  						if (pk_MainglorgbookOfCurrentCorp.equals(sPowerOrg[i]))//�ҵ������˲�������ֹѭ��
  							break;
  					}
  				}
  			}
  		} catch (Exception e) {
  			//e.printStackTrace();
  			throw new RuntimeException("��ȡ��Ȩ�������˲�����"+e.getMessage());
  		}
  		if( sOrgbook!=null) {
  			GlorgbookVO bookVo = BDGLOrgBookAccessor.getGlOrgBookVOByPrimaryKey(sOrgbook);
  			lsb.put("poweredglorgbook", bookVo);
  			AccountCalendar ac = AccountCalendar.getInstanceByGlorgbook(sOrgbook);
  			try {
					ac.setDate(new UFDate(lsb.getWorkDate()));
					lsb.put("yearOfpoweredglorgbook", ac.getYearVO());
					lsb.put("monthOfpoweredglorgbook", ac.getMonthVO());
				} catch (InvalidAccperiodExcetion e) {
					throw new RuntimeException("��ȡ��Ȩ�޻���ڼ����"+e.getMessage());
				}
  		}
		}

		private void fetchYearAndMonthVO(LoginSessBean lsb) {
    	String pk_MainglorgbookOfCurrentCorp = BDGLOrgBookAccessor.getDefaultPk_GLOrgBook(lsb.getPk_crop());
    	AccountCalendar ac = null;
  		if(pk_MainglorgbookOfCurrentCorp==null)
  			ac = AccountCalendar.getInstance();
  		else
  			ac = AccountCalendar.getInstanceByGlorgbook(pk_MainglorgbookOfCurrentCorp);
  		try {
				ac.setDate(new UFDate(lsb.getWorkDate()));
				lsb.put("year", ac.getYearVO());
        lsb.put("month", ac.getMonthVO());
			} catch (InvalidAccperiodExcetion e) {
				throw new RuntimeException("��ȡ����ڼ����"+e.getMessage());
			}
		}

		/**
     * ���û����г������жϵ��û��� �������ڣ�(2001-3-17 14:52:07)
     * 
     * @param mysession
     *            javax.servlet.http.HttpSession
     */
    void removeUser(String dsName, String userId, String sid) {
      
        LoginSessBean lsb = (LoginSessBean) getLoginUser(dsName).get(sid);
        if(lsb == null)
        	return;
        boolean isLogin = lsb.isLogin();
        getLoginUser(dsName).remove(sid);
        
        if (isLogin) {
            try {
                PKLock.getInstance().releaseLocks(userId, dsName);
            } catch (Exception e) {
                Logger.error("Error",e);
            }
            try {
                SFServiceFacility.getServiceProviderService().removeLoginUser(dsName, userId);
            } catch (Exception e) {
                Logger.error("Error",e);
            }
        }
            Logger.debug("=======================");
            Logger.debug("User removed: " + userId);
            Logger.debug("=======================");
    }

}