package nc.bs.sm.loginui;

/**
 * �˴���������˵����
 * �������ڣ�(2003-7-28 13:24:05)
 * @author�������
 */
import java.util.HashMap;
import java.util.Vector;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.server.ServerConfiguration;
import nc.bs.logging.Logger;
import nc.bs.uap.sf.facility.SFServiceFacility;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.vo.framework.rsa.Encode;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.sm.UserVO;
import nc.vo.sm.config.Account;
import nc.vo.sm.config.AccountAdm;
import nc.vo.sm.config.ConfigParameter;
import nc.vo.sm.config.SysAdm;

public class Config {
//	private static Config instance = null;

	private Account[] m_accounts = null;

	private SysAdm[] m_sysAdms = null;

	// ���ڽ��ܼ��ܵĹ���
	private Encode coder = null;

	/**
	 * Config ������ע�⡣
	 */
	public Config()  {
		super();
		ConfigParameter config;
		try {
			config = SFServiceFacility.getConfigService().getAccountConfigPara();
		} catch (Exception e) {
			Logger.error("Error", e);
			throw new BusinessRuntimeException(e.getMessage());
		}
		m_accounts = config.getAryAccounts();
		m_sysAdms = config.getArySysAdms();
	}

	private boolean isCorrectAccount(Account account) {
		String dn = account.getDataSourceName();
		String dsName[] = ServerConfiguration.getServerConfiguration().getDataSourceNames();
		int count = dsName == null ? 0 : dsName.length;
		boolean isCorrect = false;
		for (int i = 0; i < count; i++) {
			if (dsName[i] != null && dsName[i].equals(dn)) {
				try {
					if (new Login3DMO(dn).canConnect()) {
						isCorrect = true;
						break;
					}
				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
				}
			}
		}
		return isCorrect;
	}

	/**
	 * ���ָ�������Ƿ����userCode��ָ�����û��� �������ڣ�(2003-7-28 13:38:40)
	 * 
	 * @return boolean
	 * @param userCode
	 *            java.lang.String
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	public boolean contianUser(String userCode,String psw,Account account) throws java.lang.Exception {
		boolean find = false;
		if (userCode != null) {
			// ���ȼ�����׹���Ա
			AccountAdm[] adms = account.getAryAccountAdms();
			for (int i = 0, n = adms == null ? 0 : adms.length; i < n; i++) {
				if (userCode.equals(adms[i].getAccountAdmCode()) && adms[i].getPassword().equals(psw)) {
					find = true;
					break;
				}
			}
			// �ڴ����ݿ��в���
			if (!find && !account.getDataSourceName().equals(nc.bs.sm.login.LoginAppBean.SYS_ADM_DATASOURCE)) {
				String dsName = account.getDataSourceName();
				Login3DMO dmo = new Login3DMO(dsName);
				Encode coder = getCodeTool();
				String password = coder.encode(psw);
				find = dmo.containUser(userCode);
			}
		}
		return find;
	}

	private Encode getCodeTool() {
		if (coder == null)
			coder = new Encode();
		return coder;
	}

//	public static Config getInstance() throws BusinessException {
//		if (instance == null) {
//			instance = new Config();
//		}
//		return instance;
//	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-7-28 14:03:21)
	 * 
	 * @return nc.vo.sm.config.Account
	 */
	private Account getSysAdmAccount() {
		for (int i = 0, n = m_accounts == null ? 0 : m_accounts.length; i < n; i++) {
			if (m_accounts[i].getDataSourceName().equals(nc.bs.sm.login.LoginAppBean.SYS_ADM_DATASOURCE))
				return m_accounts[i];
		}
		return null;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-7-28 13:49:14)
	 * 
	 * @return nc.vo.sm.config.Account
	 * @param userCode
	 *            java.lang.String
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	public Account[] getValidateAccountVOs(String userCode, String psw) throws java.lang.Exception {
		Vector<Account> v = new Vector<Account>();
		// �����ڸ��������м���û�������Щ����
		for (int i = 0, n = m_accounts == null ? 0 : m_accounts.length; i < n; i++) {
			if (isCorrectAccount(m_accounts[i]) && contianUser(userCode,psw,m_accounts[i]))
				v.add(m_accounts[i]);
		}
		// �ټ���Ƿ���ϵͳ�����е��û�
		for (int i = 0, n = m_sysAdms == null ? 0 : m_sysAdms.length; i < n; i++) {
			if (m_sysAdms[i].getSysAdmCode().equals(userCode) && m_sysAdms[i].getPassword().equals(psw)) {
				v.add(getSysAdmAccount());
				break;
			}
		}
		//
		Account[] validateAccounts = new Account[v.size()];
		if (v.size() > 0)
			v.copyInto(validateAccounts);
		return validateAccounts;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-12-18 12:22:18)
	 * 
	 * @return boolean
	 * @param dsName
	 *            java.lang.String
	 * @param userCode
	 *            java.lang.String
	 * @param pwd
	 *            java.lang.String
	 */
	public boolean isAccountUser(String dsName, String userCode, String pwd) {
		// ���ָ���������Ƿ�����û�
		boolean retr = false;
		for (int i = 0, n = m_accounts == null ? 0 : m_accounts.length; i < n; i++) {
			if (m_accounts[i].getDataSourceName().equals(dsName)) {
				AccountAdm[] adms = m_accounts[i].getAryAccountAdms();
				for (int j = 0, m = adms == null ? 0 : adms.length; j < m; j++) {
					if (adms[j].getAccountAdmCode().equals(userCode) && adms[j].getPassword().equals(pwd)) {
						retr = true;
						break;
					}
				}
				break;
			}
		}

		return retr;
	}

	public boolean isAccountUserByUserid(String dsName, String userid) {
		// ���ָ���������Ƿ�����û�
		boolean retr = false;
		for (int i = 0, n = m_accounts == null ? 0 : m_accounts.length; i < n; i++) {
			if (m_accounts[i].getDataSourceName().equals(dsName)) {
				AccountAdm[] adms = m_accounts[i].getAryAccountAdms();
				for (int j = 0, m = adms == null ? 0 : adms.length; j < m; j++) {
					if (adms[j].getAccountAdmCode().equals(userid)) {
						retr = true;
						break;
					}
				}
				break;
			}
		}

		return retr;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-12-18 12:22:18)
	 * 
	 * @return boolean
	 * @param dsName
	 *            java.lang.String
	 * @param userCode
	 *            java.lang.String
	 * @param pwd
	 *            java.lang.String
	 */
	public boolean isGroupUser(String dsName, String userCode,String pwd) {
		// ���ָ���������Ƿ�����û�
		boolean retr = false;
		try {
			InvocationInfoProxy.getInstance().setUserDataSource(dsName);
			// nc.bs.sm.user.UserDMO dmo = new nc.bs.sm.user.UserDMO();
			// nc.vo.sm.UserVO user = dmo.findUser("0001", userCode, new
			// Encode().encode(pwd));
			IUserManageQuery query = (IUserManageQuery) NCLocator.getInstance().lookup(IUserManageQuery.class.getName());
			UserVO user = query.findUser("0001", userCode, getCodeTool().encode(pwd));
			if (user != null)
				retr = true;
		} catch (Exception e) {
			Logger.error("Error", e);
		}

		return retr;
	}

	public boolean isGroupUserByUserId(String dsName, String userId) {
		// ���ָ���������Ƿ�����û�
		boolean retr = false;
		try {
			InvocationInfoProxy.getInstance().setUserDataSource(dsName);
			// nc.bs.sm.user.UserDMO dmo = new nc.bs.sm.user.UserDMO();
			// nc.vo.sm.UserVO user = dmo.findByPrimaryKey(userId);
			IUserManageQuery query = (IUserManageQuery) NCLocator.getInstance().lookup(IUserManageQuery.class.getName());
			UserVO user = query.getUser(userId);
			if (user != null && "0001".equals(user.getCorpId()))
				retr = true;
		} catch (Exception e) {
			Logger.error("Error", e);
		}

		return retr;
	}

	public HashMap<Account, UserVO> getValidateAccountAndUser(String userCode) {
		HashMap<Account, UserVO> hm = new HashMap<Account, UserVO>();
		// ����ÿ������,�����û�,��¼����ָ���û������׺��û���Ϣ
		for (int i = 0, n = m_accounts == null ? 0 : m_accounts.length; i < n; i++) {
			UserVO user = null;
			String dsName = m_accounts[i].getDataSourceName();
			AccountAdm[] adms = m_accounts[i].getAryAccountAdms();
			if (dsName != null && dsName.trim().length() > 0) {
				for (int j = 0, m = adms == null ? 0 : adms.length; j < m; j++) {
					if (adms[j].getAccountAdmCode().equals(userCode)) {
						user = new UserVO();
						user.setPrimaryKey(userCode);
						user.setUserCode(userCode);
						user.setUserName(adms[j].getAccountAdmName());
						String pwd = adms[j].getPassword();
						user.setUserPassword(pwd);
						user.setCorpId("0001");
						break;
					}
				}
				if (user == null) {
					try {
						IUserManageQuery query = (IUserManageQuery) NCLocator.getInstance().lookup(IUserManageQuery.class.getName());
						user = query.findUserByCode(userCode, dsName);
						if (user != null) {
							String pwd = user.getUserPassword();
							pwd = getCodeTool().decode(pwd);
							user.setUserPassword(pwd);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}else{
				for (int j = 0, m = m_sysAdms == null ? 0 : m_sysAdms.length; j < m; j++) {
					if(m_sysAdms[j].getSysAdmCode().equals(userCode)){
						user = new UserVO();
						user.setPrimaryKey(userCode);
						user.setUserCode(userCode);
						user.setUserName(m_sysAdms[j].getSysAdmName());
						user.setUserPassword(m_sysAdms[j].getPassword());
						user.setCorpId("");
					}
				}
			}
			if (user != null) {
				hm.put(m_accounts[i], user);
			}
		}

		return hm;
	}
}
