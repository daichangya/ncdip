package nc.impl.uap.sf;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.logging.Logger;
import nc.bs.sm.loginui.Config;
import nc.bs.uap.sf.excp.SystemFrameworkException;
import nc.itf.uap.sf.ILogin3Service;
import nc.vo.framework.rsa.Encode;
import nc.vo.sm.config.Account;

/**
 * 此处插入类型说明。 创建日期：(2003-7-28 12:10:38)
 * 
 * @author：李充蒲
 */
public class Login3Impl implements ILogin3Service {
	/**
	 * Login3BO 构造子注解。
	 */
	public Login3Impl() {
		super();
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-7-28 12:12:35)
	 * 
	 * @return nc.vo.sm.config.Account[]
	 * @param userCode
	 *            java.lang.String
	 * @exception SystemFrameworkException
	 *                异常说明。
	 */
	public Account[] getValidateAccountVOs(String userCode, String psw)
			throws SystemFrameworkException {
		Account[] accounts = null;
		try {
			accounts =new Config().getValidateAccountVOs(userCode, psw);
		} catch (Exception e) {
			Logger.error("Error",e);
			throw new SystemFrameworkException(
					"nc.bs.sm.login3.Login3BO.getValidateAccountVOs(String, String) exception :" + e.getMessage());
		}
		return accounts;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-11-25 10:25:19)
	 * 
	 * @return nc.vo.bd.CorpVO
	 * @param dsName
	 *            java.lang.String
	 * @param userCode
	 *            java.lang.String
	 * @param userPWD
	 *            java.lang.String
	 * @exception SystemFrameworkException
	 *                异常说明。
	 */
	public nc.vo.bd.CorpVO[] getValidateCorp(String dsName, String userCode,
			String userPWD) throws SystemFrameworkException {
		java.util.Vector v = new java.util.Vector();
		Config config = new Config();
		try {
			if (config.isAccountUser(dsName, userCode,userPWD)) {// ||
																				// Config.getInstance().isGroupUser(dsName,
																				// userCode,
																				// userPWD)){
				nc.vo.bd.CorpVO account = new nc.vo.bd.CorpVO();
				account.setPk_corp("0001");
				account.setUnitcode("0001");
				account.setUnitname(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
					"smcomm", "UPP1005-000011")/* @res "集团（账套）" */);
				v.add(account);
			} else if (config.isGroupUser(dsName, userCode,userPWD)) {
				nc.vo.bd.CorpVO account = new nc.vo.bd.CorpVO();
				account.setPk_corp("0001");
				account.setUnitcode("0001");
				account.setUnitname(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
					"smcomm", "UPP1005-000005")/* @res "集团" */);
				v.add(account);

			}
			String whereSql = " (isseal is null or isseal<>'Y') and ishasaccount='Y' ";
			whereSql += " and pk_corp in (select b.pk_corp from sm_user a  , sm_user_role b  where a.cuserid = b.cuserid and a.user_code = '" + userCode + /*"'and a.user_password = '" + new Encode().encode(userPWD) + */"') order by unitcode";
			InvocationInfoProxy.getInstance().setUserDataSource(dsName);
			nc.vo.bd.CorpVO[] corps = new nc.bs.bd.CorpDMO().queryByWhereSQL(whereSql);
			int n = corps == null ? 0 : corps.length;
			for (int i = 0; i < n; i++) {
				v.add(corps[i]);
			}
		} catch (Exception e) {
			Logger.error("Error",e);
			throw new SystemFrameworkException(e.getMessage());
		}
		nc.vo.bd.CorpVO[] retrs = new nc.vo.bd.CorpVO[v.size()];
		if (v.size() > 0)
			v.copyInto(retrs);
		return retrs;
	}

	public nc.vo.bd.CorpVO[] getValidateCorpByUserId(String dsName,
			String userID) throws SystemFrameworkException {
		java.util.Vector v = new java.util.Vector();
		Config config = new Config();
		try {
			if (config.isAccountUserByUserid(dsName, userID)) {// ||
																				// Config.getInstance().isGroupUser(dsName,
																				// userCode,
																				// userPWD)){
				nc.vo.bd.CorpVO account = new nc.vo.bd.CorpVO();
				account.setPk_corp("0001");
				account.setUnitcode("0001");
				account.setUnitname(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
					"smcomm", "UPP1005-000011")/* @res "集团（账套）" */);
				v.add(account);
			} else if (config.isGroupUserByUserId(dsName, userID)) {
				nc.vo.bd.CorpVO account = new nc.vo.bd.CorpVO();
				account.setPk_corp("0001");
				account.setUnitcode("0001");
				account.setUnitname(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
					"smcomm", "UPP1005-000005")/* @res "集团" */);
				v.add(account);

			}
			String whereSql = " (isseal is null or isseal<>'Y') and ishasaccount='Y' ";
			whereSql += " and pk_corp in (select b.pk_corp from sm_user a  , sm_user_role b  where a.cuserid = b.cuserid and a.cUserId = '" + userID + "' ) order by unitcode";
			InvocationInfoProxy.getInstance().setUserDataSource(dsName);
			nc.vo.bd.CorpVO[] corps = new nc.bs.bd.CorpDMO().queryByWhereSQL(whereSql);
			int n = corps == null ? 0 : corps.length;
			for (int i = 0; i < n; i++) {
				v.add(corps[i]);
			}
		} catch (Exception e) {
			Logger.error("Error",e);
			throw new SystemFrameworkException(e.getMessage());
		}
		nc.vo.bd.CorpVO[] retrs = new nc.vo.bd.CorpVO[v.size()];
		if (v.size() > 0)
			v.copyInto(retrs);
		return retrs;
	}
}