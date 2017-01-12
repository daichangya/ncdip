package nc.ui.sm.login;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.TimeZone;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.metal.MetalComboBoxUI;

import nc.bs.framework.common.RuntimeEnv;
import nc.bs.framework.comn.NetStreamConstants;
import nc.bs.logging.Logger;
import nc.ui.bd.ref.busi.Corp_GroupsDefaultRefModel;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ServerTimeProxy;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPasswordField;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.style.Style;
import nc.vo.ml.Language;
import nc.vo.pub.CommonConstant;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.sm.config.Account;
import nc.vo.sm.config.ConfigParameter;
import nc.vo.sm.login.IControlConstant;
import nc.vo.sm.login.LoginFailureInfo;
import nc.vo.sm.login.LoginSessBean;

public class LoginUISupport {
	private static class RequestFocusAction extends AbstractAction{
		private static final long serialVersionUID = 3403610494958197404L;
		private Component comp = null;

		public RequestFocusAction(Component comp) {
			super();
			this.comp = comp;
		}

		public void actionPerformed(ActionEvent e) {
			comp.requestFocus();
			
		}
		
	}
	private static class LanguageCombobox extends UIComboBox{
		private static final long serialVersionUID = -7307027422867254504L;

		@Override
		public void updateUI() {
			setUI(new LanguageComboboxUI());
		}
	}
	private static class CustomContainerAdapter extends ContainerAdapter{
		private Component comp = null;
		
		public CustomContainerAdapter(Component comp) {
			super();
			this.comp = comp;
		}

		@Override
		public void componentRemoved(ContainerEvent e) {
			JApplet applet = ClientAssistant.getApplet();
			applet.getLayeredPane().remove(comp);
			applet.getContentPane().removeContainerListener(this);
		}
	}
	public static interface IUIUpdateListener {
		public void uiUpdate(LoginUIEvent event);
	}
	
	public static class LoginUIEvent extends AWTEvent {
		private static final long serialVersionUID = -5201877997812643873L;

		public static final int LOGIN_START = 0;

		public static final int LOGIN_END = 1;

		public LoginUIEvent(Object source) {
			super(source, -1);
		}

		public LoginUIEvent(Object source, int id) {
			super(source, id);
		}

	}

	@SuppressWarnings("unchecked")
	ArrayList al_listener = new ArrayList();

	private static class TransFocusAction extends AbstractAction {
		private static final long serialVersionUID = 1976819505587381597L;

		public void actionPerformed(ActionEvent e) {
			Object o = e.getSource();
			if (o instanceof Component) {
				((Component) o).transferFocus();
			}
		}

	}

	private static class CustomUI extends BasicButtonUI {
		protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
			super.paintText(g, c, textRect, text);
			AbstractButton b = (AbstractButton) c;
			ButtonModel model = b.getModel();
			if (model.isArmed() || model.isRollover()) {
				g.drawLine(textRect.x, textRect.y + textRect.height - 1, textRect.x + textRect.width, textRect.y + textRect.height - 1);
			}
		}
	}

	private class ItemHandler implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			Object source = e.getSource();
			if (getCbbAccount().equals(source)) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					// 获得所选帐套：
					Account selAccount = (Account) getCbbAccount().getSelectedItem();
					// // 在界面上设置该帐套的语种：
					// String langCode = (m_LangCode == null ?
					// selAccount.getLang().trim() : m_LangCode);
					// Language accLang =
					// NCLangRes.getInstance().getLanguage(langCode);
					// if (accLang != null)
					// getCbbLanguage().setSelectedItem(accLang);

					// 设置数据源
					// put data source url:
					System.setProperty(CommonConstant.USER_DATA_SOURCE, selAccount.getDataSourceName());
					System.setProperty(CommonConstant.DEFAULT_DATA_SOURCE, selAccount.getDataSourceName());

//					PoolManager.clearConnection();

					getRpCorp().getRefModel().clearData();
					getRpCorp().setPK(null);

					// // 系统管理员帐套：
					// if
					// (selAccount.getDataSourceName().equals(IControlConstant.SYS_ADM_DATASOURCE))
					// {
					// getRpCorp().setEnabled(false);
					// // getRpCorp().getUITextField().setEnabled(false);
					// // getRpCorp().getUIButton().setEnabled(false);
					// } else {
					// getRpCorp().setEnabled(true);
					// // getRpCorp().getUIButton().setEnabled(true);
					// // getRpCorp().getUITextField().setEnabled(true);
					// // 清空缓存：
					// }
					try {
						if (!IControlConstant.SYS_ADM_DATASOURCE.equals(selAccount.getDataSourceName())) {
							// 调用sxj的一个方法
							nc.vo.bd.access.BdinfoManager.clear();
						}
					} catch (Exception ee) {
						ee.printStackTrace();
					}
					fireUIUpdate(new LoginUIEvent(getCbbAccount()));
				}
			} else if (getCbbLanguage().equals(source)) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Language language = (Language) getCbbLanguage().getSelectedItem();
					NCLangRes.getInstance().setCurrLanguage(language);
					ClientEnvironment.getInstance().setLanguage(language.getCode());
					// 更新系统环境变量中Login_Lang_Code属性的值,该值在NCClassLoader中加载资源时被使用
					// 以及在nc.ui.pub.HelpBean中显示帮助文件时使用
					System.setProperty("Login_Lang_Code", language.getCode());
					// zsb+:重新设置界面风格，这样语种变化可以重新设置各种组件字体等
					Style.refreshStyle();

					Font font = new Font(language.getFontName(), Font.PLAIN, 12);
					getCbbAccount().setFont(font);
					getCbbLanguage().setFont(font);
					getTfUser().setFont(font);
					getPfUserPWD().setFont(font);
					getRpCorp().getUITextField().setFont(font);
					getRpDate().getUITextField().setFont(font);
					// 重新设置提示
					getLoginBtn().setFont(font);
					getLoginBtn().setText(LoginPanelRes.getBtnLogin());
					getLoginBtn().setToolTipText(LoginPanelRes.getBtnLoginTip());
					String optionText = NCLangRes.getInstance().getStrByID("sysframev5", "UPPsysframev5-000060")/*选项*/;
					getOptionBtn().setFont(font);
					getOptionBtn().setText(optionText);
					getOptionBtn().setToolTipText(optionText);
					getHelpBtn().setFont(font);
					getHelpBtn().setText(LoginPanelRes.getBtnHelp());
					getAboutBtn().setFont(font);
					getAboutBtn().setText(LoginPanelRes.getBtnAbout());
					getLblAccount().setFont(font);
					getLblAccount().setText(LoginPanelRes.getAccount2());
					getLblCorp().setFont(font);
					getLblCorp().setText(LoginPanelRes.getCorporation2());
					getLblDate().setFont(font);
					getLblDate().setText(LoginPanelRes.getDate2());
					getLblUser().setFont(font);
					getLblUser().setText(LoginPanelRes.getUser2());
					getLblUserPWD().setFont(font);
					getLblUserPWD().setText(LoginPanelRes.getPassword2());

					String str1 = NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000246");
					setBtnNewString(getCkbZipRemoteStream(), str1, font);
//					String str2 = NCLangRes.getInstance().getStrByID("sysframev5", "UPPsysframev5-000057");/*设为主页*/
//					setBtnNewString(getSetHomePageBtn(), str2, font);
					
					updateCopyRightStr();
					getBottomPanel().repaint();

					fireUIUpdate(new LoginUIEvent(getCbbLanguage()));

				}
			}
		}

	}
	private static class LanguageCellRender extends JLabel  implements ListCellRenderer{
		private static final long serialVersionUID = -2222264957994327437L;
		public LanguageCellRender() {
			super();
			
		}
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			setFont(list.getFont());
			if (isSelected) {
				setOpaque(true);
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setOpaque(false);
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			setText((value == null) ? "" : value.toString());
			return this;
			
		}
		
	}

	private static class LanguageComboboxUI extends MetalComboBoxUI{
		@Override
		public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
		}
		@Override
		protected JButton createArrowButton() {
			JButton btn = super.createArrowButton();
			btn.setBorderPainted(false);
			btn.setContentAreaFilled(false);
			return btn;
		}
		@Override
		protected void installComponents() {
			super.installComponents();
			comboBox.setBorder(null);
		}
		
	}
	private void setBtnNewString(AbstractButton btn, String newStr, Font font) {
		String oldStr = btn.getText();
		int oldW = ClientAssistant.getStringWidth(btn, oldStr);
		int newW = ClientAssistant.getStringWidth(btn, newStr);
		int dw = newW - oldW;
		Rectangle rect = btn.getBounds();
		rect.width += dw;
		btn.setText(newStr);
		Dimension size = new Dimension(rect.width, rect.height);
		btn.setFont(font);
		btn.setSize(size);
		btn.setPreferredSize(size);
		btn.setBounds(rect);

	}

	private class LoginAction extends AbstractAction {
		private static final long serialVersionUID = -6135751854829695365L;

		public void actionPerformed(ActionEvent e) {

			Runnable run = new Runnable() {
				public void run() {
					getLblLoginResult().setText("");
					getLoginBtn().setCursor(new Cursor(Cursor.WAIT_CURSOR));
					//
					try {
						LoginSessBean lsb = getLoginInfo();
						updateLoginInfo(lsb);
						if (lsb != null) {
							Object[] objs = LoginUIControl.getInstance().login(lsb, null);
							int resultCode = ((Integer) objs[0]).intValue();
							if (resultCode == LoginFailureInfo.LOGIN_SUCCESS) {// 登录成功
								LoginUIControl.getInstance().showDesktop((LoginSessBean) objs[1]);
							} else {
								getLblLoginResult().setText(LoginResult.getLoginStrResult(resultCode));
								// MessageDialog.showErrorDlg(ClientAssistant.getApplet(),
								// nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm",
								// "UPP1005-000215")/* @res "登录失败信息" */,
								// LoginResult.getLoginStrResult(resultCode));
							}
						}
					} catch (Exception e) {
						Logger.error(e.getMessage(), e);
						e.printStackTrace();
						String msg = e.getMessage();
						if(msg == null || msg.trim().length() == 0 ||"null".equals(msg)){
							msg = e.getClass().getCanonicalName()+":"+msg;
						}
						getLblLoginResult().setText(msg);
					}
					getLoginBtn().setCursor(Cursor.getDefaultCursor());
					fireUIUpdate(new LoginUIEvent(getLoginBtn(), LoginUIEvent.LOGIN_END));
				}
			};
			fireUIUpdate(new LoginUIEvent(getLoginBtn(), LoginUIEvent.LOGIN_START));
			new Thread(run).start();

		}
	}

	private static class HelpBtnAction extends AbstractAction {
		private static final long serialVersionUID = -4441484147402759648L;

		public void actionPerformed(ActionEvent e) {
			java.net.URL url = null;
			try {
				String prefix = ClientAssistant.getSysURLContextString();
				url = new java.net.URL(prefix + "help/" + ClientEnvironment.getInstance().getLanguage() + "/about.html");
			} catch (java.net.MalformedURLException ee) {
				ee.printStackTrace();
				MessageDialog.showErrorDlg(ClientAssistant.getApplet(), nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000232")/*
																																				 * @res
																																				 * "错误"
																																				 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000062")/*
																							 * @res
																							 * "找不到帮助文件！"
																							 */);
			}
			ClientAssistant.getApplet().getAppletContext().showDocument(url, "_blank");
		}

	}

	private static class AboutBtnAction extends AbstractAction {
		private static final long serialVersionUID = -8686211649732823588L;

		public void actionPerformed(ActionEvent e) {
			AboutDialog2 dlg = new AboutDialog2(ClientAssistant.getApplet());
			Dimension d = ClientAssistant.getApplet().getSize();
			Dimension dlgD = dlg.getSize();
			int x = (Math.abs(d.width - dlgD.width)) / 2;
			int y = (d.height - dlgD.height) / 2;
			dlg.setLocation(x, y);
			dlg.setVisible(true);
		}

	}

	// /////////////
	private ItemHandler itemHandler = new ItemHandler();

	private TransFocusAction transFocusAction = new TransFocusAction();

	private ConfigParameter m_account = null;

	//
	private UIComboBox cbbLanguage = null;

	private UIComboBox cbbAccount = null;

	private UIRefPane rpCorp = null;

	private UIRefPane rpDate = null;

	private UITextField tfUser = null;

	private UIPasswordField pfUserPWD = null;

	private UILabel lblAccount = null;

	private UILabel lblCorp = null;

	private UILabel lblDate = null;

	private UILabel lblUser = null;

	private UILabel lblUserPWD = null;

	private JButton helpBtn = null;

	private UIButton loginBtn = null;

	private JButton aboutBtn = null;

	private UIButton optionBtn = null;

	private JCheckBox ckbZipRemoteStream = null;

//	private JButton setHomePageBtn = null;

	private JLabel lblLoginFlash = null;

	private JLabel lblLoginResult = null;

	//
	private JPanel topPanel = null;

	private JPanel bottomPanel = null;

	private JPanel centerPanel = null;

	// 容纳输入登录信息的控件的面板
	private JPanel loginCompsPanel = null;

	private JPanel optionPanel = null;

	private boolean isShowOptionPanel = false;

	//
	private String m_LangCode = null;

	private Dimension size = new Dimension(160, 20);

	private Border lblBorder = BorderFactory.createEmptyBorder(1, 1, 1, 10);

	private LoginAction loginAction = new LoginAction();

	private String copyRightStr = null;
	
	private JLabel welComeLbl = null;
	// ////////////
	public LoginUISupport() {
		super();
		init();
	}
	private void printClientTimeZone(){
		TimeZone tz = TimeZone.getDefault();
		String msg = "Client Time Zone is "+tz.getDisplayName();
		System.out.println(msg);
	}
	private void init() {
		printClientTimeZone();
		// 尽早地设置流压缩标志
		boolean isSel = LoginInfoCookie.getInstance().isZipRemoteStream();
		setNetStreamZipProp(isSel);

		// 预置默认语种为简体中文
		NCLangRes.getInstance().setCurrLanguage(NCLangRes.getDefaultLanguage());
		//
		updateCopyRightStr();
		initValuesByCookie();
		updateCorpState();		
		new PreLoadClassThread().start();
		//delete by zhw 2011-12-24 邮政HR登录界面修改
//		if(!hasLoginUIBackGroundIcon()){
//			JApplet applet = ClientAssistant.getApplet();
//			applet.getContentPane().addContainerListener(new CustomContainerAdapter(getWelComeLbl()));
//			applet.getLayeredPane().add(getWelComeLbl(), new Integer(999));
//		}
	}
	protected void updateCorpState(){
        Account selAccount = (Account)getCbbAccount().getSelectedItem();
        if (selAccount != null && selAccount.getDataSourceName().equals(IControlConstant.SYS_ADM_DATASOURCE)) {
            getRpCorp().setEnabled(false);
        } else {
            getRpCorp().setEnabled(true);
        }
	}
	private void initValuesByCookie() {

		LoginInfoCookie cookie = LoginInfoCookie.getInstance();
		// 用Cookie初始化界面:
		String accountCode = cookie.getAccountCode();
		String pk_corp = cookie.getPk_corp();
		String userCode = cookie.getUserCode();
		// 多语的变化信息在事件响应中进行设置
		m_LangCode = cookie.getLangCode();

		//
		if (accountCode != null) {
			Account account = getAccountByCode(accountCode);
			if (account != null) {
				getCbbAccount().setSelectedItem(account);
				System.setProperty(nc.vo.pub.CommonConstant.USER_DATA_SOURCE, account.getDataSourceName());
				System.setProperty(nc.vo.pub.CommonConstant.DEFAULT_DATA_SOURCE, account.getDataSourceName());

//				PoolManager.clearConnection();

				if (m_LangCode != null) {
					Language lang = NCLangRes.getInstance().getLanguage(m_LangCode);
					getCbbLanguage().setSelectedItem(lang);

				}

				System.setProperty("UserPKCorp", pk_corp);
				getRpCorp().setPK(pk_corp);
				getTfUser().setText(userCode);
			}
		} else if (getCbbAccount().getModel().getSize() > 0) {
			getCbbAccount().setSelectedIndex(0);
		}
	}

	private Account getAccountByCode(String accountCode) {
		Account[] accounts = getConfigParameter().getAryAccounts();
		if (accounts != null) {
			for (int i = 0; i < accounts.length; i++) {
				if (accounts[i].getAccountCode().equals(accountCode)) {
					return accounts[i];
				}
			}
		}
		return null;
	}

	/**
	 * 给子类预留接口修改登录信息
	 * 
	 * @param lsb
	 */
	protected void updateLoginInfo(LoginSessBean lsb) throws Exception {
	}

	protected LoginSessBean getLoginInfo() throws Exception {
		// 须送到服务器端的变量：
		String accountCode = null;
		String dsName = null;
		String pk_corp = null;
		String corpCode = null;
		String workDate = null;
		String language = null;
		String userCode = null;
		String pwd = null;
		String userScreenWidth = null;
		String userScreenHeight = null;
		// 为变量赋值：
		Account selAccount = (Account) getCbbAccount().getSelectedItem();
		accountCode = selAccount.getAccountCode();
		dsName = selAccount.getDataSourceName();
		corpCode = getRpCorp().getText().trim();
		if (corpCode.equals("")) {
			pk_corp = new String("");
		} else {
			pk_corp = getRpCorp().getRefPK();
		}
		//ewei midified 防止操作过快
		if(pk_corp == null){
			pk_corp = new String("");
		}		
		Language selLang = (Language) getCbbLanguage().getSelectedItem();
		language = selLang.getCode();
		// workdate:
		workDate = getRpDate().getText();
		//
		userCode = getTfUser().getText();
		pwd = new String(getPfUserPWD().getPassword());
		//
		userScreenWidth = NCAppletStub.getInstance().getParameter("USER_WIDTH");
		userScreenHeight = NCAppletStub.getInstance().getParameter("USER_HEIGHT");

		LoginSessBean lsb = new LoginSessBean();
		lsb.setAccountId(accountCode);
		lsb.setDataSourceName(dsName);
		lsb.setPk_corp(pk_corp);
		lsb.setCorpCode(corpCode);
		lsb.setWorkDate(workDate);
		lsb.setLanguage(language);
		lsb.setUserCode(userCode);
		lsb.setPassword(pwd);
		if (userScreenWidth != null) {
			try {
				lsb.setUserScreenWidth(new Integer(userScreenWidth).intValue());
				lsb.setIsUserResolutionValid(true);
			} catch (NumberFormatException e) {
				lsb.setIsUserResolutionValid(false);
			}
		}
		if (userScreenHeight != null) {
			try {
				lsb.setUserScreenHeight(new Integer(userScreenHeight).intValue());
			} catch (NumberFormatException e) {
				lsb.setIsUserResolutionValid(false);
			}
		}
		return lsb;

	}

	private ConfigParameter getConfigParameter() {
		if (m_account == null) {
			try {
				m_account = new ConfigParameter();
				String strAccoutnInfo = NCAppletStub.getInstance().getParameter("ACCOUNT");
				StringTokenizer st = new StringTokenizer(strAccoutnInfo, ",");
				int intAry = 0;
				Account[] vos = new Account[st.countTokens()];
				String tmpStr = null;
				while (st.hasMoreElements()) {
					Account vo = new Account();
					tmpStr = String.valueOf(st.nextElement());
					String tmpStr2 = "";
					int intBegin = 0;
					int intIndex = 0;
					int intNo = -1;
					int intLast = tmpStr.lastIndexOf(":");
					while (intIndex >= 0) {
						intIndex = tmpStr.indexOf(":", intBegin);
						if (intIndex > -1) {
							tmpStr2 = tmpStr.substring(intBegin, intIndex);
							intBegin = intIndex + 1;
							intNo++;
						} else {
							if (intBegin == intLast + 1) {
								tmpStr2 = tmpStr.substring(intBegin);
								vo.setLang(tmpStr2);
								break;
							}
						}
						switch (intNo) {
						case 0:
							vo.setDataSourceName(tmpStr2);
							break;
						case 1:
							vo.setAccountCode(tmpStr2);
							break;
						case 2:
							vo.setAccountName(tmpStr2);
							break;
						case 3:
							vo.setLang(tmpStr2);
							break;
						}
					}
					vos[intAry++] = vo;
				}
				m_account.setAryAccounts(vos);
				// m_account =
				// nc.ui.sm.config.AccountBO_Client.getConfigParameter();
			} catch (Exception e) {
				e.printStackTrace();
				ShowDialog.showErrorDlg(ClientAssistant.getApplet(), nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000019")/*
																																			 * @res
																																			 * "错误"
																																			 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000226")/*
																							 * @res
																							 * "找不到配置文件，初始化失败！"
																							 */);
			}
		}
		return m_account;
	}

	public UIComboBox getCbbLanguage() {
		if (cbbLanguage == null) {
			try {
				cbbLanguage = new LanguageCombobox();
				cbbLanguage.setRenderer(new LanguageCellRender()); 
				cbbLanguage.setOpaque(false);
				cbbLanguage.setEditable(false);
//				cbbLanguage.setPreferredSize(new Dimension(70, 23));
				cbbLanguage.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");
				cbbLanguage.getActionMap().put("enter", transFocusAction);
				cbbLanguage.setFont(new java.awt.Font(Style.getFontname(), 0, 12));
				Language[] langTypes = NCLangRes.getInstance().getAllLanguages();
				int maxW = 0;
				FontMetrics fm = cbbLanguage.getFontMetrics(cbbLanguage.getFont());
				int langCount = langTypes == null ? 0 : langTypes.length;
				for (int i = 0; i < langCount; i++) {
					cbbLanguage.addItem(langTypes[i]);
					String text = langTypes[i].getDisplayName();
					if(text != null){
						int width = fm.stringWidth(text);
						if(width > maxW){
							maxW = width;
						}
					}
				}
				cbbLanguage.setPreferredSize(new Dimension(maxW+20, 23));
				// 周善保+：数据项加完后再添加监听事件
				cbbLanguage.setSelectedIndex(-1);// 先不选中，再选中则可以触发选项事件
				cbbLanguage.addItemListener(itemHandler);
				if (langCount > 0) {
					cbbLanguage.setSelectedIndex(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return cbbLanguage;
	}

	@SuppressWarnings("unchecked")
	public UIComboBox getCbbAccount() {
		if (cbbAccount == null) {
			try {
				cbbAccount = new UIComboBox();
				cbbAccount.setPreferredSize(size);
				cbbAccount.setFont(new Font(Style.getFontname(), Font.PLAIN, 12));
				// 帐套：
				Account[] accounts = getConfigParameter().getAryAccounts();
				ArrayList al = new ArrayList();
				Account sysAccount = null;
				for (int i = 0; i < accounts.length; i++) {
					if (accounts[i].getDataSourceName().equals(IControlConstant.SYS_ADM_DATASOURCE)) {
						sysAccount = accounts[i];
					} else {
						if (!RuntimeEnv.getInstance().isDevelopMode() && accounts[i].getAccountCode().equals("design"))
							continue;
						else
							al.add(accounts[i]);
					}
				}
				if (sysAccount != null) {
					al.add(sysAccount);
				}
				for (int i = 0; i < al.size(); i++) {
					cbbAccount.addItem(al.get(i));
				}
				// 周善保：数据项加完后再添加监听事件
				cbbAccount.setSelectedIndex(-1);// 先不选中，再选中则可以触发选项事件
				cbbAccount.addItemListener(itemHandler);
				if (al.size() > 0)
					cbbAccount.setSelectedIndex(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return cbbAccount;
	}

	public UIPasswordField getPfUserPWD() {
		if (pfUserPWD == null) {
			try {
				pfUserPWD = new UIPasswordField();
				pfUserPWD.setFont(new Font(Style.getFontname(), Font.PLAIN, 12));
				pfUserPWD.setAllowOtherCharacter(true);
				pfUserPWD.setPreferredSize(size);
				InputMap input = pfUserPWD.getInputMap(JComponent.WHEN_FOCUSED);
				ActionMap action = pfUserPWD.getActionMap();
				input.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "login");
				action.put("login", loginAction);
				pfUserPWD.setVisible(ClientUIConfig.getInstance().getPwdCompVisible());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return pfUserPWD;
	}

	public UIRefPane getRpCorp() {
		if (rpCorp == null) {
			try {
				rpCorp = new UIRefPane();
				rpCorp.setPreferredSize(size);
//				rpCorp.setRefNodeName("公司目录(集团)");
				rpCorp.setIsCustomDefined(true);
				
				rpCorp.setRefModel(new Corp_GroupsDefaultRefModel("公司目录(集团)" ));
				
				rpCorp.getUITextField().setFont(new Font(Style.getFontname(), Font.PLAIN, 12));
				rpCorp.getUITextField().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");
				rpCorp.getUITextField().getActionMap().put("enter", new RequestFocusAction(getRpDate()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return rpCorp;
	}

	public UIRefPane getRpDate() {
		if (rpDate == null) {
			try {
				rpDate = new UIRefPane();
				rpDate.getUITextField().setFont(new Font(Style.getFontname(), Font.PLAIN, 12));
				rpDate.setPreferredSize(size);
				rpDate.setRefNodeName("日历");
				rpDate.getUITextField().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");
				rpDate.getUITextField().getActionMap().put("enter", new RequestFocusAction(getTfUser()));

//				String strServerTime = NCAppletStub.getInstance().getParameter("SERVERTIME");
//				UFDateTime datetime = new UFDateTime(strServerTime);
				
				//由于webstart登录方式会缓存applet页面，导致从applet参数取得的时间为一固定值，所以修改为下面的方式获得时间
				UFDateTime datetime = ServerTimeProxy.getInstance().getServerTime();
				rpDate.setText(datetime.getDate().toString());
				rpDate.setEnabled(ClientUIConfig.getInstance().getDateCompEditable().booleanValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return rpDate;
	}

	public UITextField getTfUser() {
		if (tfUser == null) {
			try {
				tfUser = new UITextField();
				tfUser.setFont(new Font(Style.getFontname(), Font.PLAIN, 12));
				tfUser.setPreferredSize(size);
				tfUser.setVisible(ClientUIConfig.getInstance().getUserCompVisible());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return tfUser;
	}

	public JButton getAboutBtn() {
		if (aboutBtn == null) {
			try {
				aboutBtn = new JButton();
				aboutBtn.setAction(new AboutBtnAction());
				aboutBtn.setFont(new Font(Style.getFontname(), Font.PLAIN, 12));
				aboutBtn.setText(LoginPanelRes.getBtnAbout());
				aboutBtn.setMargin(new Insets(0, 0, 0, 0));
				aboutBtn.setBorder(BorderFactory.createEmptyBorder());
				aboutBtn.setOpaque(false);
				aboutBtn.setFocusPainted(false);
				aboutBtn.setContentAreaFilled(false);
				aboutBtn.setRolloverEnabled(true);
				aboutBtn.setIcon(ClientAssistant.loadImageIcon("images/loginicon/about.png"));
				aboutBtn.setRolloverIcon(ClientAssistant.loadImageIcon("images/loginicon/about_over.png"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return aboutBtn;
	}

	public JButton getHelpBtn() {
		if (helpBtn == null) {
			try {
				helpBtn = new JButton();
				helpBtn.setAction(new HelpBtnAction());
				helpBtn.setFont(new Font(Style.getFontname(), Font.PLAIN, 12));
				helpBtn.setText(LoginPanelRes.getBtnHelp());
				helpBtn.setMargin(new Insets(0, 0, 0, 0));
				helpBtn.setBorder(BorderFactory.createEmptyBorder());
				helpBtn.setOpaque(false);
				helpBtn.setFocusPainted(false);
				helpBtn.setContentAreaFilled(false);
				helpBtn.setRolloverEnabled(true);
				helpBtn.setIcon(ClientAssistant.loadImageIcon("images/loginicon/help.png"));
				helpBtn.setRolloverIcon(ClientAssistant.loadImageIcon("images/loginicon/help_over.png"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return helpBtn;
	}

	public UIButton getLoginBtn() {
		if (loginBtn == null) {
			try {
				loginBtn = new UIButton();
				loginBtn.setAction(loginAction);
				loginBtn.setFont(new Font(Style.getFontname(), Font.PLAIN, 12));
				loginBtn.setText(LoginPanelRes.getBtnLogin());
				loginBtn.setToolTipText(LoginPanelRes.getBtnLoginTip());
				loginBtn.setPreferredSize(new Dimension(70, 20));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return loginBtn;
	}

	public UIButton getOptionBtn() {
		if (optionBtn == null) {
			try {
				optionBtn = new UIButton();
				optionBtn.setText(NCLangRes.getInstance().getStrByID("sysframev5", "UPPsysframev5-000060")/*
																											 * @res
																											 * "选项"
																											 */);
				optionBtn.setHorizontalTextPosition(UIButton.LEFT);
				optionBtn.setFont(new Font(Style.getFontname(), Font.PLAIN, 12));
				optionBtn.setIcon(ClientAssistant.loadImageIcon("images/loginicon/rightarrow.gif"));
				setShowOptionPanel(false);
				optionBtn.setPreferredSize(new Dimension(70, 20));
				optionBtn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setShowOptionPanel(!isShowOptionPanel());
						fireUIUpdate(new LoginUIEvent(optionBtn));
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return optionBtn;
	}

	public JCheckBox getCkbZipRemoteStream() {
		if (ckbZipRemoteStream == null) {
			String name = NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000246");
			ckbZipRemoteStream = new JCheckBox(name);
			ckbZipRemoteStream.setOpaque(false);
			ckbZipRemoteStream.setFocusPainted(false);
			ckbZipRemoteStream.setBounds(0, 0, 115, 20);
			ckbZipRemoteStream.setFont(new Font(Style.getFontname(), Font.PLAIN, 12));
			boolean isSel = LoginInfoCookie.getInstance().isZipRemoteStream();
			ckbZipRemoteStream.setSelected(isSel);
			setNetStreamZipProp(isSel);
			ckbZipRemoteStream.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					boolean isSel = ckbZipRemoteStream.isSelected();
					setNetStreamZipProp(isSel);
					LoginInfoCookie.getInstance().setZipRemoteStream(isSel);
					LoginInfoCookie.getInstance().writeToLocal();
				}
			});
		}
		return ckbZipRemoteStream;
	}

	private void setNetStreamZipProp(boolean isZip) {
		try {
			NetStreamConstants.STREAM_NEED_COMPRESS = isZip;
			ClassLoader cl = getClass().getClassLoader().getParent();
			if (cl == null)
				cl = ClassLoader.getSystemClassLoader();
			// System.out.println("netStream classloader is " + cl + "
			// compressed: " + isZip);
			Class<?> clazz = cl.loadClass("nc.bs.framework.comn.NetStreamConstants");
			Field field = clazz.getDeclaredField("STREAM_NEED_COMPRESS");
			field.set(null, isZip);
		} catch (Throwable thr) {
			// thr.printStackTrace();
		}

	}

//	public JButton getSetHomePageBtn() {
//		if (setHomePageBtn == null) {
//			setHomePageBtn = new JButton(NCLangRes.getInstance().getStrByID("sysframev5", "UPPsysframev5-000057")/*
//																													 * @res
//																													 * "设为主页"
//																													 */);
//			setHomePageBtn.setHorizontalAlignment(JButton.LEFT);
//			setHomePageBtn.setBounds(0, 26, 65, 20);
//			setHomePageBtn.setUI(new CustomUI());
//			setHomePageBtn.setFont(new Font(Style.getFontname(), Font.PLAIN, 12));
//			setHomePageBtn.setOpaque(false);
//			setHomePageBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//			setHomePageBtn.setRolloverEnabled(true);
//			setHomePageBtn.setBorder(BorderFactory.createEmptyBorder());
//			setHomePageBtn.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					ClientAssistant.setHomePage();
//				}
//
//			});
//		}
//		return setHomePageBtn;
//	}

	public UILabel getLblAccount() {
		if (lblAccount == null) {
			try {
				lblAccount = new UILabel();
				lblAccount.setFont(new Font(Style.getFontname(), Font.PLAIN, 12));
				lblAccount.setText(LoginPanelRes.getAccount2());
				lblAccount.setBorder(lblBorder);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return lblAccount;
	}

	public UILabel getLblCorp() {
		if (lblCorp == null) {
			try {
				lblCorp = new UILabel();
				lblCorp.setFont(new Font(Style.getFontname(), Font.PLAIN, 12));
				lblCorp.setText(LoginPanelRes.getCorporation2());
				lblCorp.setBorder(lblBorder);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return lblCorp;
	}

	public UILabel getLblDate() {
		if (lblDate == null) {
			try {
				lblDate = new UILabel();
				lblDate.setFont(new Font(Style.getFontname(), Font.PLAIN, 12));
				lblDate.setText(LoginPanelRes.getDate2());
				lblDate.setBorder(lblBorder);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return lblDate;
	}

	public UILabel getLblUser() {
		if (lblUser == null) {
			try {
				lblUser = new UILabel();
				lblUser.setFont(new Font(Style.getFontname(), Font.PLAIN, 12));
				lblUser.setText(LoginPanelRes.getUser2());
				lblUser.setBorder(lblBorder);
				lblUser.setVisible(ClientUIConfig.getInstance().getUserCompVisible());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return lblUser;
	}

	public UILabel getLblUserPWD() {
		if (lblUserPWD == null) {
			try {
				lblUserPWD = new UILabel();
				lblUserPWD.setFont(new Font(Style.getFontname(), Font.PLAIN, 12));
				lblUserPWD.setText(LoginPanelRes.getPassword2());
				lblUserPWD.setBorder(lblBorder);
				lblUserPWD.setVisible(ClientUIConfig.getInstance().getPwdCompVisible());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return lblUserPWD;
	}

	private boolean hasLoginUIBackGroundIcon() {
		return ClientUIConfig.getInstance().getLoginUIBGIcon() != null;
	}

	public JPanel getTopPanel() {
		if (topPanel == null) {
			topPanel = new JPanel();
			topPanel.setOpaque(false);
			topPanel.setLayout(new BorderLayout());
			JPanel p2 = new JPanel() {
				private static final long serialVersionUID = 6289212838013767586L;
				ImageIcon bgIcon = ClientAssistant.loadImageIcon("images/loginicon/topSpace.png");
				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					if (this.isOpaque()) {
						Graphics2D g2 = (Graphics2D) g;
						Dimension d = getSize();
						if (bgIcon != null){
							int iW = bgIcon.getIconWidth();
							int iH = bgIcon.getIconHeight();
							int x = (d.width - iW)/2;
							int y = d.height - iH;
							g2.drawImage(bgIcon.getImage(), x, y, iW, iH, this);
						}
					}
				}
			};
			p2.setBackground(new Color(0xc7d8ec));
			p2.setOpaque(!hasLoginUIBackGroundIcon());
			p2.setPreferredSize(new Dimension(30, 62));
			SpringLayout sl = new SpringLayout();
			p2.setLayout(sl);
			p2.add(getCbbLanguage());
			p2.add(getAboutBtn());
			p2.add(getHelpBtn());

			topPanel.add(p2, BorderLayout.CENTER);
			topPanel.setPreferredSize(new Dimension(30, 57));

			sl.putConstraint(SpringLayout.EAST, getHelpBtn(), -20, SpringLayout.EAST, p2);
			sl.putConstraint(SpringLayout.NORTH, getHelpBtn(), (p2.getPreferredSize().height - getHelpBtn().getPreferredSize().height) / 2, SpringLayout.NORTH, p2);
			sl.putConstraint(SpringLayout.NORTH, getAboutBtn(), (p2.getPreferredSize().height - getAboutBtn().getPreferredSize().height) / 2, SpringLayout.NORTH, p2);
			sl.putConstraint(SpringLayout.EAST, getAboutBtn(), -5, SpringLayout.WEST, getHelpBtn());
			sl.putConstraint(SpringLayout.NORTH, getCbbLanguage(), (p2.getPreferredSize().height - getCbbLanguage().getPreferredSize().height) / 2+2, SpringLayout.NORTH, p2);
			sl.putConstraint(SpringLayout.EAST, getCbbLanguage(), -16, SpringLayout.WEST, getAboutBtn());
		}
		return topPanel;
	}

	private void updateCopyRightStr(){
		copyRightStr = NCLangRes.getInstance().getStrByID("sysframev5", "UPPsysframev5-000061")/*版权所有  (c)1997-2015 用友软件股份有限公司*/;
	}
	
	public JPanel getBottomPanel() {
		if (bottomPanel == null) {
			bottomPanel = new JPanel();
			bottomPanel.setOpaque(false);
			bottomPanel.setLayout(new BorderLayout()); 
			bottomPanel.setPreferredSize(new Dimension(30, 62));
			JPanel p1 = new JPanel() {
				private static final long serialVersionUID = 2958009447658114338L;
				Color c1 = new Color(0xe9f1fc);
				Color c2 = new Color(0xf7c005);
				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					if (this.isOpaque()) {
						Graphics2D g2 = (Graphics2D) g;
						Paint oldPaint = g2.getPaint();
						int w = getSize().width;
						int h = getSize().height;
						GradientPaint gp = new GradientPaint(w-230, 0, c1, w-115, 0, c2);
						g2.setPaint(gp);
						g2.fillRect(w-230, 0, 115, h);
						gp = new GradientPaint(w-115, 0, c2, w, 0, c1);
						g2.setPaint(gp);
						g2.fillRect(w -115, 0, 115, h);
						g2.setPaint(oldPaint);
					}
				}
			};
			p1.setBackground(new Color(0xe9f1fc));
			p1.setOpaque(!hasLoginUIBackGroundIcon());
			p1.setPreferredSize(new Dimension(30, 1));
			
			JPanel p2 = new JPanel(){
				private static final long serialVersionUID = -6849133262584714814L;
				ImageIcon bgIcon = ClientAssistant.loadImageIcon("images/loginicon/ufida.gif");
				Color textColor = new Color(0x8da5c3);
				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					if(this.isOpaque()){
						Graphics2D g2 = (Graphics2D) g;
						Dimension d = getSize();
						int iconH = 0;
						if(bgIcon != null){
							iconH = bgIcon.getIconHeight();
							bgIcon.paintIcon(this, g2, d.width - 25 - bgIcon.getIconWidth(), 10);
						}
						String text = copyRightStr;
						if(text == null){
							text = "";
						}
						Font  font = getFont();
						FontMetrics fe = getFontMetrics(font);
						int strW = fe.stringWidth(text);
						Color oldC = g2.getColor();
						g2.setColor(textColor);
						g2.drawString(text, d.width-25-strW, 10+iconH+6+fe.getAscent());
						g2.setColor(oldC);
					}
				}
			};
			p2.setOpaque(!hasLoginUIBackGroundIcon());
			p2.setBackground(Color.white);
			bottomPanel.add(p1, BorderLayout.NORTH);
			bottomPanel.add(p2, BorderLayout.CENTER);
		}
		return bottomPanel;
	}

	public JLabel getWelComeLbl() {
		if(welComeLbl == null){
			welComeLbl = new JLabel();
			ImageIcon icon = ClientAssistant.loadImageIcon("images/loginicon/welcome.png");
			welComeLbl.setBorder(BorderFactory.createEmptyBorder());
			if(icon != null){
				welComeLbl.setIcon(icon);
				welComeLbl.setBounds(12,48, icon.getIconWidth(), icon.getIconHeight());
			}
		}
		return welComeLbl;
	}

	//modify by zhw 2011-12-24 修改登录界面
//	public JPanel getCenterPanel() {
//		if (centerPanel == null) {
//			centerPanel = new JPanel() {
//				private static final long serialVersionUID = 5767013748981058923L;
//				Color c1 = new Color(0xc7d8ec);
//				Color c2 = new Color(0xf9fcfe);
//				ImageIcon logo = ClientAssistant.loadImageIcon("images/loginicon/logo.png");
//				ImageIcon line = ClientAssistant.loadImageIcon("images/loginicon/line.png");
//				public void paintComponent(Graphics g) {
//					super.paintComponent(g);
//					if (this.isOpaque()) {
//						Graphics2D g2 = (Graphics2D) g;
//						Paint oldPaint = g2.getPaint();
//						int w = getSize().width;
//						int h = getSize().height;
//						GradientPaint gp = new GradientPaint(0, 0, c1, 0, h, c2);
//						g2.setPaint(gp);
//						g2.fillRect(0, 0, w, h);
//						g2.setPaint(oldPaint);
//						int x = getLeftLocation();
//						if(logo != null){
//							logo.paintIcon(this, g2, x , 205);
//							x += logo.getIconWidth()+30;
//						}
//						if(line != null){
//							line.paintIcon(this, g2, x , 80);
//						}
//	
//					}
//				}
//				private int getLeftLocation(){
//					Dimension d = getSize();
//					int width = getLblAccount().getPreferredSize().width+size.width+72;
//					if(logo != null){
//						width += logo.getIconWidth();
//					}
//					if(line != null){
//						width += line.getIconWidth();
//					}
//					return (d.width-width)/2-20;
//				}
//				@Override
//				public void doLayout() {
////					Dimension d = getSize();
//					int location = getLeftLocation()+65;
//					if(logo != null){
//						location += logo.getIconWidth();
//					}
//					if(line != null){
//						location += line.getIconWidth();
//					}
//					
//					getLoginCompsPanel().setLocation(location, 123);
//				}
//				
//				
//			};
//			centerPanel.setOpaque(!hasLoginUIBackGroundIcon());
//
//		}
//
//		return centerPanel;
//	}
	public JPanel getCenterPanel() {
        if (centerPanel == null) {
            centerPanel = new JPanel() {
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    int w = getSize().width;
                    int h = getSize().height;
                    g2.setPaint(new Color(0xeaf2fd));
                    g2.fillRect(0, 0, w, h);
                    Image topBackImg = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/loginicon/sylogin.gif"));
                    g2.drawImage(topBackImg, 0, 0, w, h, null, this);
                }
            };
        }
        return centerPanel;
    }
	//add by zhw 2011-12-24 -----------------------------------end
	

	public JPanel getLoginCompsPanel() {
		if (loginCompsPanel == null) {
			
			//modify by zhw 2011-12-24 邮政HR登录界面修改
//			loginCompsPanel = new JPanel();
			loginCompsPanel = new JPanel(){
            	public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Dimension d = ClientAssistant.getApplet().getSize();
                    int w = d.width;
                    int h = d.height;
                    Double double1 = new Double(w)*0.618;
                    int x = double1.intValue();
                    int y = h / 2;
                    getLoginCompsPanel().setBounds(x,y-150, 300, 300);
                }
            };
            //modify by zhw 2011-12-24 ------------end
            
			loginCompsPanel.setOpaque(false);
			Dimension d = ClientAssistant.getApplet().getSize();
			loginCompsPanel.setSize(d.width, d.height);
             int w = d.width;
             int h = d.height;
             Double double1 = new Double(w)*0.618;
             int x = double1.intValue();
             int y = h / 2;
             getLoginCompsPanel().setBounds(x,y-150, 300, 300);
			
		}
		return loginCompsPanel;
	}

	public JPanel getOptionPanel() {
		if (optionPanel == null) {
			optionPanel = new JPanel();
			optionPanel.setOpaque(false);
			optionPanel.setLayout(null);
			optionPanel.add(getCkbZipRemoteStream());
//			optionPanel.add(getSetHomePageBtn());
			optionPanel.setPreferredSize(new Dimension(600, 26));
		}
		return optionPanel;
	}

	@SuppressWarnings("unchecked")
	public void addUIUpdateListener(IUIUpdateListener listener) {
		al_listener.add(listener);
	}

	protected void fireUIUpdate(LoginUIEvent e) {
		int count = al_listener.size();
		for (int i = 0; i < count; i++) {
			IUIUpdateListener listener = (IUIUpdateListener) al_listener.get(i);
			listener.uiUpdate(e);
		}
	}

	public boolean isShowOptionPanel() {
		return isShowOptionPanel;
	}

	public void setShowOptionPanel(boolean isShowOptionPanel) {
		this.isShowOptionPanel = isShowOptionPanel;
		String name = nc.ui.ml.NCLangRes.getInstance().getStrByID("sysframev5", "UPPsysframev5-000060")/*
																										 * @res
																										 * "选项"
																										 */;
		if (isShowOptionPanel()) {
			getOptionBtn().setIcon(ClientAssistant.loadImageIcon("images/loginicon/leftarrow.gif"));
			// name += " <<";
		} else {
			getOptionBtn().setIcon(ClientAssistant.loadImageIcon("images/loginicon/rightarrow.gif"));
			// name += " >>";
		}
		getOptionBtn().setText(name);
	}

	public JLabel getLblLoginFlash() {
		if (lblLoginFlash == null) {
			lblLoginFlash = new JLabel();
			ImageIcon icon = getLoginFlashImage();
			lblLoginFlash.setIcon(icon);
			lblLoginFlash.setOpaque(false);
			lblLoginFlash.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			lblLoginFlash.setSize(icon.getIconWidth(), icon.getIconHeight());
			lblLoginFlash.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
		}
		return lblLoginFlash;
	}

	private ImageIcon getLoginFlashImage() {
		ImageIcon retrIcon = null;
		ImageIcon icon = ClientAssistant.loadImageIcon("images/loginicon/progress.gif");
		if (icon != null) {
			int w = icon.getIconWidth();
			int h = icon.getIconHeight();
			BufferedImage img = new BufferedImage(w - 2, h - 2, BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g = img.createGraphics();
			Image iconImg = icon.getImage();
			g.drawImage(iconImg, 1, 1, w - 1, h - 1, 0, 0, w - 1, h - 1, getLblLoginFlash());
			g.dispose();
			retrIcon = new ImageIcon(iconImg);
		}
		return retrIcon;
	}

	public JLabel getLblLoginResult() {
		if (lblLoginResult == null) {
			ImageIcon icon = ClientAssistant.loadImageIcon("images/loginicon/warn.gif");
			lblLoginResult = new JLabel(icon) {
				private static final long serialVersionUID = -3544675299514278098L;

				public void setText(String text) {
					if (text == null)
						text = "null";
					super.setText(text);
					Font f = this.getFont();
					if (f != null) {
						FontMetrics fm = this.getFontMetrics(f);
						int w = SwingUtilities.computeStringWidth(fm, text);
						int h = this.getPreferredSize().height;
						this.setPreferredSize(new Dimension(w + 30, h));
					}
				}

				public void paintComponent(Graphics g) {
					Insets inset = this.getInsets();
					Color c = g.getColor();
					Rectangle rect = this.getBounds();
					g.setColor(this.getBackground());
					g.fillRect(inset.left, inset.top, rect.width - inset.left - inset.right, rect.height - inset.bottom - inset.top);
					g.setColor(c);
					super.paintComponent(g);

				}
			};
			lblLoginResult.setOpaque(false);
			lblLoginResult.setHorizontalAlignment(JLabel.CENTER);
			// lblLoginResult.setHorizontalAlignment(SwingConstants.LEFT);
			lblLoginResult.setPreferredSize(new Dimension(200, 26));
			lblLoginResult.setBorder(new AbstractBorder() {
				private static final long serialVersionUID = 7680278292185216656L;

				Color borderColor = new Color(0xb69204);

				Color shadowColor = new Color(0xc6c8c7);

				public Insets getBorderInsets(Component c) {
					return new Insets(1, 1, 3, 3);
				}

				public boolean isBorderOpaque() {
					return true;
				}

				public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
					Color oldColor = g.getColor();
					g.setColor(shadowColor);
					Graphics2D g2 = (Graphics2D) g;
					Rectangle rect1 = new Rectangle(x, y, w - 3, h - 3);
					Rectangle rect2 = new Rectangle(x + 3, y + 3, w - 3, h - 3);
					Area a = new Area(rect2);
					a.subtract(new Area(rect1));
					g2.fill(a);
					g.setColor(borderColor);
					g2.draw(rect1);
					g.setColor(oldColor);
				}
			});
			lblLoginResult.setBackground(new Color(0xfff0c2));
		}
		return lblLoginResult;
	}

	public void setUIEnable(boolean enable) {

		getCbbAccount().setEnabled(enable);
		getCbbLanguage().setEnabled(enable);
		if (((Account) getCbbAccount().getSelectedItem()).getDataSourceName().equals(IControlConstant.SYS_ADM_DATASOURCE)) {
			getRpCorp().setEnabled(false);
		} else {
			getRpCorp().setEnabled(enable);
		}
		getRpDate().setEnabled(enable && ClientUIConfig.getInstance().getDateCompEditable());
		getTfUser().setEnabled(enable);
		getPfUserPWD().setEnabled(enable);
		getLoginBtn().setEnabled(enable);
		getOptionBtn().setEnabled(enable);
		getCkbZipRemoteStream().setEnabled(enable);
//		getSetHomePageBtn().setEnabled(enable);
		getAboutBtn().setEnabled(enable);

	}

}