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
 * 处理所有连接的session，一个Application只有一个LoginAppBean， 它记录并管理所有的登录session，进行连接超时、最大用户数的控制，释放用户资源,并记录登录日志 创建日期：(2001-3-16 21:06:16)
 * 
 * @author：何胜利 尚未处理：启动一线程，每隔几秒钟调用一次checkSession()方法 2001-04-26 由张伟帮助实现 修改人：雷军 2004-7-27 修改了锁定用户的几个方法，只需更新锁定信息。
 */
public class LoginAppBean implements IViewConstant, IControlConstant {

    /**
     * 与帐套无关的变量：
     */
    /** 为提交登录请求的用户产生一个流水号 */
    private Random m_rand = null;

    // /** 允许的最大用户连接数 */
    // private int m_maxUser;

//    /** 定义session的超时间隔（毫秒） */
//    private long m_interval;

    /** 定义上次连接与当前时间的差值 */
    private long m_conInterval;

    /** 定义允许登录失败的最大次数 */
    private int m_allowedMaxFailure;

    public static final int OVER_MAX_USER = 1;

    public static final int USER_EXIST = 2;

    public static final int LOGIN_OK = 0;

    public static final String ACC_ADM_PK_CORP = CommonConstant.GROUP_CODE;

    // 用于解密加密的工具
    private Encode coder = null;

    private CheckSessionThread m_CheckSessionThread;

    private static LoginAppBean m_instance = new LoginAppBean();

    private HashMap m_illegalLoginContainer = new HashMap();

    private HashMap m_loginUserContainer = new HashMap();

    /**
     * LoginAppBean 构造子注解。
     */
    private LoginAppBean() {
        super();
        // 设置登录常量：
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
     * 此处插入方法说明。 创建日期：(2001-6-23 16:49:11)
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

        // 未找到该帐套：
        if (pos == -1) {
            String err = "查找编码为\"" + accountCode + "\"的账套失败，没有找到该账套";
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
     * 检查已登陆用户，若超时或已断线，则从hashTable中删除并释放资源。 创建日期：(2001-3-17 16:34:20)
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
    // * 此处插入方法说明。 创建日期：(2001-3-17 11:21:48)
    // */
    // private int getMaxUser() {
    // return m_maxUser;
    // }

    /**
     * 此处插入方法说明。 创建日期：(2001-3-17 11:21:48)
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
     * 此处插入方法说明。 创建日期：(2001-3-17 11:21:48)
     */
    public void setConInterval(long interval) {
        this.m_conInterval = interval;
    }

//    /**
//     * 此处插入方法说明。 创建日期：(2001-3-17 11:21:48)
//     */
//    public void setInterval(long interval) {
//        this.m_interval = interval; // 设置超时时间间隔
//    }

    /**
     * 此处插入方法说明。 创建日期：(2001-3-17 11:21:48)
     */
    public void setMaxFailure(int maxFailure) {
        this.m_allowedMaxFailure = maxFailure; // 设置允许失败的最大次数
    }
   
    /**
     * modifier ewei
     * 根据当前的用户VO得到最大允许失败值
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
     * 根据账套管理员得到最大登录失败次数
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
    // * 此处插入方法说明。 创建日期：(2001-3-17 11:21:48)
    // */
    // public void setMaxUser(int maxUser) {
    // this.m_maxUser = maxUser; // 设置允许的最大用户数
    // }

    /**
     * 根据产品授权数判断是否可打开该产品的一个节点。规则如下：如果该用户已打开该 产品的其他节点，则可打开该节点；否则，如果当前使用该产品的用户未达到最大授权数， 则可打开该节点。 创建日期：(2001-10-18 11:22:59)
     * 
     * @return boolean
     * @param product
     *            java.lang.String
     */
    public  boolean canOpenModule(String product, String userId) {

        // 获得ServerEnvironment:
        // 当前账套的ServerEnvironment:
        nc.vo.pub.services.ServerEnvironment se = null;
        // 所有账套的ServerEnvironment:
        nc.vo.pub.services.ServerEnvironment[] ses = null;
        try {
            se = SFServiceFacility.getServiceProviderService().getServerEnv();
            ses = SFServiceFacility.getServiceProviderService().getAllSEInAllContainer().toArray(new ServerEnvironment[0]);//SFServiceFacility.getServiceProviderService().getAllServerEnvs();
        } catch (Exception e) {
            Logger.error("Error",e);
        }
        // 获得授权数：
        int license = getProductLicense(product);

        // 判断：
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
        
        String msg = "licp ===>canOpenModule ?  product = '" + product + "'。 liencese = " + license + " 。userCount = " + userCount;
        Logger.debug(msg);
        //
        if (license > userCount) {
            // 授权数大于该产品当前的总用户数，则可以打开该模块：
            return true;
        } else {
            // 如果该用户已打开该产品的其他模块(即正在使用该产品)，则可以打开该模块：
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
     * 此处插入方法说明。 创建日期：(2001-4-29 16:31:00)
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
     * 此处插入方法说明。 创建日期：(2001-11-13 11:37:28)
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
     * 此处插入方法说明。 创建日期：(2001-4-29 16:31:00)
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
     * 此处插入方法说明。 创建日期：(2001-11-13 11:36:22)
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
     * 获得一个产品的授权数目。 创建日期：(2001-10-18 9:24:01)
     * 
     * @param String
     *            产品编码
     * @return java.util.HashMap
     */
    private int getProductLicense(String product) {
        // 该产品的授权数目：
//        ILicenseService iILicenseService = (ILicenseService) NCLocator.getInstance().lookup(ILicenseService.class.getName());
    	ICilService service = NCLocator.getInstance().lookup(ICilService.class);
        int intLicNum = service.getProductLicense(product);
        if (intLicNum <= 0)
            // 如果没有默认授权，则授权数为0：
            return 0;
        else
            return intLicNum;
    }

    /**
     * 此处插入方法说明。 创建日期：(2001-12-1 16:02:08)
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
     * 响应客户端发来的连接测试 创建日期：(2001-4-22 15:55:55)
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
        // 获得授权数：
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
     * 此处插入方法说明。 创建日期：(2001-3-17 11:21:48)
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
                    // 用户已存在
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
        // 设置流水号：
        String sid = getSID();
        lsb.setSID(sid);
        // 添加用户：
        lsb.setIsLogin(true);
        lsb.setLoginTime(System.currentTimeMillis());
        lsb.setVoteTime(System.currentTimeMillis());
        if(!lsb.isHideLogin())
        	getLoginUser(lsb.getDataSourceName()).put(sid, lsb);
        return LOGIN_OK;
    }

    /**
     * 判断某个账套中某个用户是否在线。 在线返回true,不在线返回false 创建日期：(2001-3-17 11:21:48)
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
     * 从一个账套中查找一个账套管理员 如果没有找到，就返回null。 创建日期：(2003-6-16 10:12:30)
     * modified by ewei 添加passwordlevel和starttime两个属性的转换 
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
                    user.setCorpId(ACC_ADM_PK_CORP);// 账套管理员公司id为“0001”
                    break;
                }
            }
        }
        return user;
    }

    /**
     * 从配置文件中查找账套。 没有找到就返回null 创建日期：(2003-6-16 9:53:15)
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
                Logger.error("nc.bs.sm.login.LoginAppBean.findAccountFromXml(String)方法获取配置文件时出错");
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
            Logger.warn("在查找accountCode=" + accountCode + "的账套时，没有找到该账套。");
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
     * 此处插入方法说明。 创建日期：(2001-10-19 10:11:08)
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
     * 得到一个nonce数做为sid号。 创建日期：(2002-11-13 10:59:23)
     * 
     * @return java.lang.String
     */
    private synchronized String getSID() {
        long sid = getRandom().nextLong();
        long currentTimeMillis = System.currentTimeMillis();
        return new StringBuffer("").append(currentTimeMillis).append(sid).toString();
    }

    /**
     * 验证用户身份的方法。 创建日期：(2003-6-16 14:33:32)
     * 
     * @return int
     * @param lsb
     *            nc.vo.sm.login.LoginSessBean
     * @param userVO
     *            nc.vo.sm.UserVO
     */
    private int identityVerify(LoginSessBean lsb, UserVO user) {
        if (user != null) { // 说明用户名正确
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
            //ewei+ 此处再添加一个属性，用来判断是否使用ca签名
            lsb.put("busi_ifcauser", user.getIsCa());
            String log = ">>>用户" + user.getUserName() + "配置使用的认证方式为：" + iaCode + "。\n实际使用的认证方式为：" + conf.getIdentityVerifyCode();
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
                // 如果认证方式类不是默认的静态密码认证方式，就将数据库中的静态密码回填到lsb中
                lsb.setPassword(getCodeTool().decode(user.getUserPassword()));
            }
            return retr;
        } else { // 说明用户名称错误
            return LoginFailureInfo.NAME_WRONG;
        }

    }

    /**
     * 锁定用户本次登陆（登陆失败次数过多） 创建日期：(2001-3-17 11:21:48)
     */
    private void lockAccAdm(int failType, LoginFailureInfo fail, LoginSessBean lsb) {

        // 在web server上处理登录失败：
        if (failType == LoginFailureInfo.NAME_RIGHT_PWD_WRONG) {
            if (!lockAccAdmFromXml(failType, fail, lsb)) {
                lockUser(failType, fail, lsb);
            }
        }
    }

    /**
     * 锁定用户本次登陆（登陆失败次数过多） 创建日期：(2001-3-17 11:21:48)
     */
    private boolean lockAccAdmFromXml(int failType, LoginFailureInfo fail, LoginSessBean lsb) {

        // 查找用户：
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
        
        // Xml中不存在该用户:
        if (pos == -1) {
            return false;
        } else {
        	int maxfailure = getMaxFailure(adms[pos]);
        		if (!adms[pos].isLocked()) {
        		LinkedList info = null;
            	// 1 没有失败记录，表示该用户的第一次登录失败
            	if ((info = (LinkedList) getIllegalLogin(lsb.getDataSourceName()).get(adms[pos].getAccountAdmCode())) == null) {
                	info = new LinkedList();
                	info.addLast(fail);
                	//
                	getIllegalLogin(lsb.getDataSourceName()).put(adms[pos].getAccountAdmCode(), info);
                	return true;
            	}
            	// 2 已有登录失败的记录：
            	int failNum = info.size();
            	// 2.1 失败次数小于允许次数：
            	if (failNum < maxfailure) {
                	info.addLast(fail);
                	getIllegalLogin(lsb.getDataSourceName()).put(adms[pos].getAccountAdmCode(), info);
            	}
            	// 2.2 失败次数达到允许次数：
            	else {
            		// 2.2.1 锁定用户：
                	try {
                    	SFServiceFacility.getServiceProviderService().addLockedUser(lsb);
                    	adms[pos].setLocked(new UFBoolean(true));
                    	AccountXMLUtil.updateAdmLockedTag(config);// AccountBO_Client.updateAdmLockedTag(config);

                	} catch (Exception e) {
                		Logger.error("Error",e);
                	}
                	// 2.3 清除失败记录：
                	getIllegalLogin(lsb.getDataSourceName()).remove(adms[pos].getAccountAdmCode());
            	}
        	}
        }
        return true;
    }

    /**
     * 锁定用户本次登陆（登陆失败次数过多） 创建日期：(2001-3-17 11:21:48)
     */
    private void lockSysAdm(int failType, LoginFailureInfo fail, LoginSessBean lsb) {

        // 在web server上处理登录失败：
        if (failType == LoginFailureInfo.NAME_RIGHT_PWD_WRONG) {
            // 1. 查找用户：
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
            // 2.处理用户锁定：
            if (!sysAdms[pos].isLocked()) {
                LinkedList info = null;
                // 4.1 没有失败记录，表示该用户的第一次登录失败
                if ((info = (LinkedList) getIllegalLogin(lsb.getDataSourceName()).get(sysAdms[pos].getSysAdmCode())) == null) {
                    info = new LinkedList();
                    info.addLast(fail);
                    //
                    getIllegalLogin(lsb.getDataSourceName()).put(sysAdms[pos].getSysAdmCode(), info);
                    return;
                }
                // 4.2 已有登录失败的记录：
                int failNum = info.size();
                // 4.2.1 失败次数小于允许次数：
                if (failNum < m_allowedMaxFailure) {
                    info.addLast(fail);
                    getIllegalLogin(lsb.getDataSourceName()).put(sysAdms[pos].getSysAdmCode(), info);
                }
                // 4.2.2 失败次数达到允许次数：
                else {
                    // 4.2.2.1 锁定用户：
                    try {
                        // ServiceProviderBO_Client.addLockedUser(lsb);
                        SFServiceFacility.getServiceProviderService().addLockedUser(lsb);
                        sysAdms[pos].setLocked(new UFBoolean(true));
                        AccountXMLUtil.updateAdmLockedTag(config);// AccountBO_Client.updateAdmLockedTag(config);
                    } catch (Exception e) {
                        Logger.error("Error",e);
                    }
                    // 4.2.2 清除失败记录：
                    getIllegalLogin(lsb.getDataSourceName()).remove(sysAdms[pos].getSysAdmCode());
                }
            }
        }
    }

    /**
     * 锁定用户本次登陆（登陆失败次数过多） 创建日期：(2001-3-17 11:21:48)
     */
    private void lockUser(int failType, LoginFailureInfo fail, LoginSessBean lsb) {
        String pk_corp = lsb.getPk_crop();
        String userCode = lsb.getUserCode();
        String dsName = lsb.getDataSourceName();
        if (pk_corp.equals("")) {
            pk_corp = CommonConstant.GROUP_CODE;
        }
        // 在web server上处理登录失败：
        if (failType == LoginFailureInfo.NAME_RIGHT_PWD_WRONG) {
            // 查找用户：
            UserVO user = getUser(pk_corp, userCode, dsName);// getUser(request.getParameter("pk_corp"),
            //
            int maxfailure = getMaxFailure(user);
            if (!user.getLocked().booleanValue()) {
                LinkedList info = null;
                // 4.1 没有失败记录，表示该用户的第一次登录失败
                if ((info = (LinkedList) getIllegalLogin(lsb.getDataSourceName()).get(user.getPrimaryKey())) == null) {
                    info = new LinkedList();
                    info.addLast(fail);
                    //
                    getIllegalLogin(lsb.getDataSourceName()).put(user.getPrimaryKey(), info);
                    Logger.warn("登录失败1：" + user.getUserCode());
                    return;
                }
                // 4.2 已有登录失败的记录：
                int failNum = info.size();
                Logger.warn("登录失败：" + user.getUserCode() + "Count" + failNum);
                // 4.2.1 失败次数小于允许次数：
                if (failNum < maxfailure) {
                    info.addLast(fail);
                    getIllegalLogin(lsb.getDataSourceName()).put(user.getPrimaryKey(), info);
                }
                // 4.2.2 失败次数达到允许次数：
                else {
                    // 4.2.2.1 锁定用户：
                    try {
                        SFServiceFacility.getServiceProviderService().addLockedUser(lsb);
                        user.setLocked(new UFBoolean(true));
                        SFServiceFacility.getIUserManage().updateLockedTag(user);
                    } catch (Exception e) {
                        Logger.error("Error",e);
                    }
                    // 4.2.2 清除失败记录：
                    getIllegalLogin(lsb.getDataSourceName()).remove(user.getPrimaryKey());
                }
            }
        }
    }

    /**
     * 用户登录
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
            // 将该用户置为准备登录的状态：
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
     * 校验登录信息
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
        // 1. 检验帐套有效性:
        UFDate today = null;
        try {
            today = SFServiceFacility.getServiceProviderService().getServerTime().getDate();
        } catch (Exception e) {
            Logger.error("Error",e);
            throw new SystemFrameworkException(e.getMessage());
        }
        //检查业务日期
        String workDate = lsb.getWorkDate();
        UFDate date = new UFDate(workDate);
        String pkCorp = lsb.getPk_crop();
        if ((pkCorp != null) && (!pkCorp.trim().equals("")) && (!pkCorp.trim().equals(nc.ui.pub.CommonMark.GROUP_CODE))) {
            try {
                boolean isValideDate = false;
                isValideDate = SFServiceFacility.getConfigService().isValidDate(date);
                if (!isValideDate) {
                    return ILoginResult.ILLEGAL_ACCPRERIOD;//业务日期不在系统定义的会计期间之内
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SystemFrameworkException(e.getMessage());        
            }
        }
        //
        int result = checkAccount(accountCode, today);
        // 帐套有效处理：
        if (result == LoginFailureInfo.LOGIN_SUCCESS) {
            // 2. 检验用户登录:
            if (alwaysStaticPWD) {
                tl.set(new Boolean(true));
            }
            result = loginValidate(lsb);
        }
        return result;
    }

    /**
     * 此处插入方法说明。 创建日期：(2001-11-10 15:41:56)
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
        // 系统管理员：
        if (dsName.equals(SYS_ADM_DATASOURCE)) {
            lsb.setUserType(LoginSessBean.SYSTEM_ADM);
            validateResult = loginValidateSysAdm(lsb);
        }
        // 帐套管理员：
        else if (pk_corp.trim().equals(ACC_ADM_PK_CORP) || pk_corp.trim().equals("")) {
            lsb.setUserType(LoginSessBean.ACCOUNT_ADM);
            validateResult = loginValidateAccAdm(lsb);
        }
        // 普通用户：
        else {
            lsb.setUserType(LoginSessBean.USER);
            validateResult = loginValidateUser(lsb);
        }
        //
        return validateResult;
    }

    /**
     * 此处插入方法说明。 创建日期：(2001-11-10 15:41:56)
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
     * 此处插入方法说明。 创建日期：(2001-11-10 15:41:56)
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
        lsb.setCorpName(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("smcomm", "UPP1005-000005")/* @res "集团" */);
        //
        Account account = findAccountFromXml(accountCode);
        UserVO accountAdm = findAccountAdmFromAccount(account, userCode);
        if (accountAdm==null){
        	return LoginFailureInfo.NAME_WRONG;
        }
        //判断锁定提前
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
     * 此处插入方法说明。 创建日期：(2001-11-10 15:41:56)
     * 
     * @return int
     * @param request
     *            javax.servlet.http.HttpServletRequest
     * @param reponse
     *            javax.servlet.http.HttpServletResponse
     */
    private int loginValidateSysAdm(LoginSessBean lsb) {

        lsb.setCorpName(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("smcomm", "UPP1005-000005")/* @res "集团" */);

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
        // 用户名错：
        if (pos == -1) {
            return LoginFailureInfo.NAME_WRONG;
        }
        //用户被锁定
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
            String log = ">>>用户" + user.getUserName() + "配置使用的认证方式为：" + iaCode + "。\n实际使用的认证方式为：" + conf.getIdentityVerifyCode();
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
                // 如果认证方式类不是默认的静态密码认证方式，就将数据库中的静态密码回填到lsb中
                lsb.setPassword(getCodeTool().decode(user.getUserPassword()));
            }
            if(retr == LoginFailureInfo.LOGIN_LEGALIDENTITY){
            	retr = LoginFailureInfo.LOGIN_SUCCESS;
            }
            return retr;
///////////
//        String message = null;
//        // 用户名和密码都错：
//        if (pos == -1) {
//            return LoginFailureInfo.NAME_WRONG;
//        }
//        // 用户名正确，密码错误：
//        else if (!sysAdms[pos].getPassword().equals(password)) {
//            return LoginFailureInfo.NAME_RIGHT_PWD_WRONG;
//        }
//        // 输入了正确的用户名和密码的情况：
//        lsb.setUserId(sysAdms[pos].getSysAdmCode());
//        lsb.setUserName(sysAdms[pos].getSysAdmName());
//        if (sysAdms[pos].isLocked()) {
//            return LoginFailureInfo.USER_LOCKED;
//        }
//        //
//        return LoginFailureInfo.LOGIN_SUCCESS;
    }

    /**
     * 此处插入方法说明。 创建日期：(2001-11-10 15:41:56)
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
        // 设置公司信息：
        try {
            CorpVO corp = SFServiceHelper.getCorpQryService().findCorpVOByPK(pk_corp, dsName);

            if (corp != null) {
                lsb.setCorpName(corp.getUnitname());
            } else {
                lsb.setCorpName(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("smcomm", "UPP1005-000005")/*
                                                                                                                 * @res "集团"
                                                                                                                 */);
            }
        } catch (Exception e) {
            Logger.error("Error",e);
        }
        UserVO user = getUser(pk_corp, userCode, dsName);
        
        // 用户被锁定：
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
        		if(!"true".equals(LdapHelper.getLdapXml().getIsauth())){//不启用Ldap，需要校验nc密码
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
        
        // 账号尚未到生效期
        if (user.getAbleDate() != null) {
            if (today.before(user.getAbleDate())) {
                return LoginFailureInfo.USER_BEFORE_EFFECT;
            }
        }
        // 账号已过有效期
        if (user.getDisableDate() != null) {
            if (today.after(user.getDisableDate().getDateBefore(1))) {
                return LoginFailureInfo.USER_EXPIRED;
            }
        }
        return LoginFailureInfo.LOGIN_SUCCESS;
    }

    /**
     * 锁定用户本次登陆（登陆失败次数过多） 创建日期：(2001-3-17 11:21:48)
     */
    private void onLoginFailure(int failType, LoginSessBean lsb) {
    	if(lsb.isHideLogin())
    		return;
        String dsName = lsb.getDataSourceName();
        String pk_corp = lsb.getPk_crop();
        // 1. 获得登录失败的所有信息：
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

        // 2. 在服务器端记录登录失败的信息：
        try {
            SFServiceFacility.getServiceProviderService().addLoginFailureInfo(fail);
        } catch (Exception e) {
            Logger.error("Error",e);
        }
        // 3.针对不同类型用户的处理：
        // 系统管理员：
        if (dsName.equals(SYS_ADM_DATASOURCE)) {
            lockSysAdm(failType, fail, lsb);
        }
        // 帐套管理员：
        else if (pk_corp.trim().equals(ACC_ADM_PK_CORP) || pk_corp.trim().equals("")) {
            lockAccAdm(failType, fail, lsb);
        }
        // 普通用户：
        else {
            lockUser(failType, fail, lsb);
        }

    }

    /**
     * 此处插入方法说明。 创建日期：(2001-11-10 15:48:52)
     * 
     * @param request
     *            javax.servlet.http.HttpServletRequest
     * @param response
     *            javax.servlet.http.HttpServletResponse
     */
    private void onLoginSuccess(LoginSessBean lsb) {
    	if(!lsb.isHideLogin()){
        // 注册到服务器上：
        try {
            SFServiceFacility.getServiceProviderService().addLoginUser(lsb);
        } catch (Exception e) {
            Logger.error("Error",e);
        }
        //一旦登录成功，就将以前登录失败的记录清空
        UserVO user = (UserVO)lsb.get("USERVO");
        if(user != null){
            getIllegalLogin(lsb.getDataSourceName()).remove(user.getPrimaryKey());
        }
        // 记入日志：
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
                    log.setCompanyname(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("smcomm", "UPP1005-000005")/* @res "集团" */);
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
         * add by leijun 10/10/2003 实现客户端远程调用的合并
         */

        // 明确区分当前登录用户的身份
        boolean isUser = lsb.getUserType() == LoginSessBean.USER ? true : false;
        boolean isAccAdm = lsb.getAccAdmType() == LoginSessBean.ACCOUNT_ADM_FROM_XML ? true : false;

        ConfigParser cp = ConfigParserOnServer.getConfigParser();
        if (cp != null) {
            lsb.put("CONFIGPARSER", cp);
            Logger.debug("### 获取CONFIGPARSER成功！");
        }

        String dbName = lsb.getDataSourceName();
        if (dbName == null || dbName.equals("")) {
        } else {
            try {
                if (isUser && !"".equals(lsb.getPk_crop()) ) {
                    Logger.debug("@@@ 用户非帐套管理员");
                    CorpVO cVo = SFServiceHelper.getCorpQryService().findCorpVOByPK(lsb.getPk_crop(), dbName);
                    //nc.vo.bd.CorpVO cVo = new InitLogin().fetchCorpVO(lsb.getPk_crop(), dbName);
                    if (cVo != null) {
                        lsb.put("CORPVO", cVo);
                        Logger.debug("### 获取CORPVO成功！");
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
  			//集团没有默认账簿
  			return ;
  		}
  		String pk_MainglorgbookOfCurrentCorp = BDGLOrgBookAccessor.getDefaultPk_GLOrgBook(sCorpPK);//公司主账簿
  		String sOrgbook = null;
  		try {
  			IFuncPower iPower = (IFuncPower) NCLocator.getInstance().lookup(IFuncPower.class.getName());
  			PowerResultVO voRet = iPower.queryPowerOrgByUserCorp(lsb.getUserId(), sCorpPK);
  			if (voRet.isPowerControl()) {//进行了权限控制，则查找此主账簿是否有权限，如果没有，则取一个有权限的主体账簿
  				String[] sPowerOrg = voRet.getPowerId();
  				if (sPowerOrg != null && sPowerOrg.length > 0) {
  					int i = 0;
  					for (; i < sPowerOrg.length; i++) {
  						GlorgbookVO vo = BDGLOrgBookAccessor.getGlOrgBookVOByPrimaryKey(sPowerOrg[i]);
  						if (vo.isSeal() || !vo.isBeginUse())//如果封存则不能登录，或者没有启用也不能登录
  							continue;
  						sOrgbook = sPowerOrg[i];
  						if (pk_MainglorgbookOfCurrentCorp.equals(sPowerOrg[i]))//找到了主账簿，则终止循环
  							break;
  					}
  				}
  			}
  		} catch (Exception e) {
  			//e.printStackTrace();
  			throw new RuntimeException("获取有权限主体账簿错误："+e.getMessage());
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
					throw new RuntimeException("获取有权限会计期间错误："+e.getMessage());
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
				throw new RuntimeException("获取会计期间错误："+e.getMessage());
			}
		}

		/**
     * 从用户表中除掉已中断的用户。 创建日期：(2001-3-17 14:52:07)
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