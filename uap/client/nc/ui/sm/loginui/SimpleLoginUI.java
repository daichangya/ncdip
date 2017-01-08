package nc.ui.sm.loginui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;

import nc.bs.uap.sf.facility.SFServiceFacility;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.style.Style;
import nc.ui.sm.login.ClientAssistant;
import nc.ui.sm.login.ClientUIConfig;
import nc.ui.sm.login.LoginInfoCookie;
import nc.ui.sm.login.LoginUISupport;
import nc.ui.sm.login.NCAppletStub;
import nc.ui.sm.login.LoginUISupport.LoginUIEvent;
import nc.vo.bd.CorpVO;
import nc.vo.ml.Language;
import nc.vo.pub.BusinessException;
import nc.vo.sm.config.Account;
import nc.vo.sm.login.LoginSessBean;

/**
 * 简单登录界面 创建日期:2006-8-2
 * 
 * @author licp
 * @since 5.0
 */
public class SimpleLoginUI extends JPanel {
 	private static final long serialVersionUID = -6663398624467818979L;

	private LoginUISupport uiSupport = null;

    private JPanel compPanel = null;

    private SpringLayout sl = null;

    private JComponent[] lbls = null;

    private JComponent[] comps = null;

    public SimpleLoginUI() {
        super();
        initialize();
    }

    private void initialize() {
        setName("simpleLoginUI");
        // 界面风格
        try {
            nc.ui.pub.style.Style.refreshStyle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 首次使用ClientEnvironment时，创建一个新的Instance:
        nc.ui.pub.ClientEnvironment.newInstance().setDesktopApplet(ClientAssistant.getApplet());
        //
        sl = new SpringLayout();
        uiSupport = new LoginUISupport() {
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
                // 语种：
                Language selLang = (Language) getCbbLanguage().getSelectedItem();
                language = selLang.getCode();
                // workdate:
                workDate = getRpDate().getText();
                // 用户及密码
                userCode = getTfUser().getText();
                if (userCode == null || userCode.equals("")) {
                    throw new Exception(NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000231")/* @res "您没有输入用户编码！" */);
                }
                pwd = new String(getPfUserPWD().getPassword());
                //
                userScreenWidth = NCAppletStub.getInstance().getParameter("USER_WIDTH");
                userScreenHeight = NCAppletStub.getInstance().getParameter("USER_HEIGHT");
                //
                Account[] accounts = null;
                CorpVO[] corps = null;
                accounts = SFServiceFacility.getLogin3Service().getValidateAccountVOs(userCode, pwd);
                if (accounts == null || accounts.length == 0) {
                    throw new Exception("没有可供登录的账套");
                } else if (accounts.length == 1) {
                    accountCode = accounts[0].getAccountCode();
                    dsName = accounts[0].getDataSourceName();
                    if (dsName.equals("")) {
                        corpCode = "";
                        pk_corp = "";
                    } else {
                        corps = SFServiceFacility.getLogin3Service().getValidateCorp(dsName, userCode, pwd);
                        if (corps == null || corps.length == 0) {
                            throw new Exception("没有可供登录的公司");
                        } else if (corps.length == 1) {
                            corpCode = corps[0].getUnitcode();
                            pk_corp = corps[0].getPk_corp();
                        }
                    }

                }
                if (corpCode == null) {
            		LoginInfoCookie cookie = LoginInfoCookie.getInstance();
            		// 用Cookie初始化界面:
            		String cookieAccount = cookie.getAccountCode();
                    getCbbAccount().removeAllItems();
                    int index = -1;
                    for (int i = 0; i < accounts.length; i++) {
                        getCbbAccount().addItem(accounts[i]);
                        if(accounts[i].getAccountCode().equals(cookieAccount)){
                        	index = i;
                        }
					}
                    if(index != -1)
                    	getCbbAccount().setSelectedIndex(index);
                    
                    LoginOptionSelDlg dlg = new LoginOptionSelDlg(SimpleLoginUI.this, "选择账套和公司", this);
                    if (dlg.showModal() == UIDialog.ID_OK) {
                        Account selAccount = (Account) getCbbAccount().getSelectedItem();
                        accountCode = selAccount.getAccountCode();
                        dsName = selAccount.getDataSourceName();
                        corpCode = getRpCorp().getText().trim();
                        if (corpCode.equals("")) {
                            pk_corp = new String("");
                        } else {
                            pk_corp = getRpCorp().getRefPK();
                        }

                    } else {
                        return null;
                    }
                }
                setUIEnable(false);

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
        };
        
        lbls = new JComponent[3];
        lbls[0] = uiSupport.getLblDate();
        lbls[1] = uiSupport.getLblUser();
        lbls[2] = uiSupport.getLblUserPWD();
        comps = new JComponent[3];
        comps[0] = uiSupport.getRpDate();
        comps[1] = uiSupport.getTfUser();
        comps[2] = uiSupport.getPfUserPWD();
        uiSupport.addUIUpdateListener(new LoginUISupport.IUIUpdateListener() {
            public void uiUpdate(LoginUIEvent e) {
                Object sour = e.getSource();
                if (sour.equals(uiSupport.getCbbAccount())) {
                    getCompPanel().remove(uiSupport.getLblLoginResult());
                    Account selAccount = (Account) uiSupport.getCbbAccount().getSelectedItem();
                    String dsName = selAccount.getDataSourceName();
                    String userCode = uiSupport.getTfUser().getText();
                    String pwd = new String(uiSupport.getPfUserPWD().getPassword());
                    try {
                        if (!dsName.equals("")) {
                            uiSupport.getRpCorp().setEnabled(true);
                            CorpVO[] corps = SFServiceFacility.getLogin3Service().getValidateCorp(dsName, userCode, pwd);
                    		LoginInfoCookie cookie = LoginInfoCookie.getInstance();
                    		String cookieCorp = cookie.getPk_corp();
                    		String defaultCorp = null;
                            int count = corps == null ? 0 : corps.length;
                            String[] pk_corps = new String[count];
                            for (int i = 0; i < count; i++) {
                                pk_corps[i] = corps[i].getPk_corp();
                                if(pk_corps[i].equals(cookieCorp)){
                                	defaultCorp = pk_corps[i];
                                }
                            }
                            uiSupport.getRpCorp().getRefModel().setFilterPks(pk_corps);
                            if (count > 0) {
                            	if(defaultCorp != null){
                            		uiSupport.getRpCorp().setPK(defaultCorp);
                            	}else{
                            		uiSupport.getRpCorp().setPK(pk_corps[0]);
                            	}
                            }
                        } else {
                            uiSupport.getRpCorp().setEnabled(false);
                        }
                        
                    } catch (BusinessException e1) {
                        e1.printStackTrace();
                    }
                }
                if (sour.equals(uiSupport.getCbbLanguage()) || sour.equals(uiSupport.getOptionBtn())) {
                    layoutUI();
                } else if (sour.equals(uiSupport.getLoginBtn())) {
                    if (e.getID() == LoginUIEvent.LOGIN_START) {
                        loginStart();
                    } else if (e.getID() == LoginUIEvent.LOGIN_END) {
                        loginEnd();
                    }
                }
            }
        });

        setLayout(new BorderLayout());
//        add(uiSupport.getTopPanel(), BorderLayout.NORTH);
//        add(uiSupport.getBottomPanel(), BorderLayout.SOUTH);

        JPanel centerPane = uiSupport.getCenterPanel();
        centerPane.setLayout(null);
        centerPane.add(getCompPanel());
        add(centerPane, BorderLayout.CENTER);
        Dimension d = ClientAssistant.getApplet().getSize();
        setSize(d);
        setPreferredSize(d);

    }

    private void loginEnd() {
        uiSupport.setUIEnable(true);
        String text = uiSupport.getLblLoginResult().getText();
        if (text != null && text.trim().length() > 0) {
            getCompPanel().add(uiSupport.getLblLoginResult());
            sl.putConstraint(SpringLayout.WEST, uiSupport.getLblLoginResult(), 0, SpringLayout.WEST, getCompPanel());
            sl.putConstraint(SpringLayout.NORTH, uiSupport.getLblLoginResult(), 0, SpringLayout.NORTH, getCompPanel());
        }
        getCompPanel().remove(uiSupport.getLblLoginFlash());
        getCompPanel().validate();
        getCompPanel().repaint();

    }
    public void setUIEnable(boolean enable) {

//        uiSupport.getCbbAccount().setEnabled(enable);
        uiSupport.getCbbLanguage().setEnabled(enable);
//        if (((Account) getCbbAccount().getSelectedItem()).getDataSourceName().equals(IControlConstant.SYS_ADM_DATASOURCE)) {
//            getRpCorp().setEnabled(false);
//        } else {
//            getRpCorp().setEnabled(enable);
//        }
        uiSupport.getRpDate().setEnabled(enable && ClientUIConfig.getInstance().getDateCompEditable());
        uiSupport.getTfUser().setEnabled(enable);
        uiSupport.getPfUserPWD().setEnabled(enable);
        uiSupport.getLoginBtn().setEnabled(enable);
        uiSupport.getOptionBtn().setEnabled(enable);
        uiSupport.getCkbZipRemoteStream().setEnabled(enable);
//        uiSupport.getSetHomePageBtn().setEnabled(enable);
        uiSupport.getAboutBtn().setEnabled(enable);

    }
    private void loginStart() {
        getCompPanel().remove(uiSupport.getLblLoginResult());
        setUIEnable(false);
        getCompPanel().add(uiSupport.getLblLoginFlash());
        JComponent com = uiSupport.getLoginBtn();
        sl.putConstraint(SpringLayout.WEST, uiSupport.getLblLoginFlash(), 0, SpringLayout.WEST, com);
        if (uiSupport.isShowOptionPanel()) {
            com = uiSupport.getOptionPanel();
        }
        sl.putConstraint(SpringLayout.NORTH, uiSupport.getLblLoginFlash(), 6, SpringLayout.SOUTH, com);

        getCompPanel().validate();
        getCompPanel().repaint();
    }
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		ImageIcon icon =ClientUIConfig.getInstance().getLoginUIBGIcon(); 
		if(icon != null){
			icon.paintIcon(this, g, 0, 0);
		}
	}
    //
    private JPanel getCompPanel() {
        if (compPanel == null) {
            compPanel = uiSupport.getLoginCompsPanel();
            compPanel.setLayout(sl);
            for (int i = 0; i < lbls.length; i++) {
                compPanel.add(lbls[i]);
            }
            for (int i = 0; i < comps.length; i++) {
                compPanel.add(comps[i]);
            }

            compPanel.add(uiSupport.getLoginBtn());
            compPanel.add(uiSupport.getOptionBtn());
            layoutUI();

        }
        return compPanel;
    }

    private void layoutUI() {

        int dy = 45;
        int maxX = 0;
        for (int i = 0; i < lbls.length; i++) {
            sl.putConstraint(SpringLayout.WEST, lbls[i], 0, SpringLayout.WEST, getCompPanel());
            if (i > 0)
                dy += lbls[i - 1].getPreferredSize().height + 6;
            sl.putConstraint(SpringLayout.NORTH, lbls[i], dy, SpringLayout.NORTH, getCompPanel());
            maxX = Math.max(maxX, lbls[i].getPreferredSize().width);
        }
        dy = 45;
        for (int i = 0; i < comps.length; i++) {
            sl.putConstraint(SpringLayout.WEST, comps[i], maxX + 2, SpringLayout.WEST, getCompPanel());
            if (i > 0)
                dy += comps[i - 1].getPreferredSize().height + 6;
            sl.putConstraint(SpringLayout.NORTH, comps[i], dy, SpringLayout.NORTH, getCompPanel());
        }

        sl.putConstraint(SpringLayout.NORTH, uiSupport.getLoginBtn(), 6, SpringLayout.SOUTH, comps[comps.length - 1]);
        sl.putConstraint(SpringLayout.WEST, uiSupport.getLoginBtn(), 0, SpringLayout.WEST, comps[comps.length - 1]);
        sl.putConstraint(SpringLayout.NORTH, uiSupport.getOptionBtn(), 6, SpringLayout.SOUTH, comps[comps.length - 1]);
        sl.putConstraint(SpringLayout.WEST, uiSupport.getOptionBtn(), 8, SpringLayout.EAST, uiSupport.getLoginBtn());
        //
        JPanel optionPanel = uiSupport.getOptionPanel();
        if (uiSupport.isShowOptionPanel()) {
            getCompPanel().add(optionPanel);
            sl.putConstraint(SpringLayout.WEST, optionPanel, 0, SpringLayout.WEST, uiSupport.getLoginBtn());
            sl.putConstraint(SpringLayout.NORTH, optionPanel, 6, SpringLayout.SOUTH, uiSupport.getLoginBtn());
        } else {
            getCompPanel().remove(optionPanel);
        }
        getCompPanel().validate();
        getCompPanel().repaint();

    }

    private class LoginOptionSelDlg extends UIDialog {
  		private static final long serialVersionUID = 1275683260232186333L;

		private LoginUISupport uiSupport = null;

        private UIButton btnOK = null;

        private UIButton btnCancel = null;

        private UIPanel btnPanel = null;

        private UIPanel mainPanel = null;

        private UIPanel getBtnPanel() {
            if (btnPanel == null) {
                btnPanel = new UIPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
                btnPanel.setOpaque(false);
                btnPanel.add(getBtnOK());
                btnPanel.add(getBtnCancel());
            }
            return btnPanel;
        }

        private UIPanel getMainPanel() {
            if (mainPanel == null) {
                mainPanel = new UIPanel();
                mainPanel.setOpaque(false);
                SpringLayout sl = new SpringLayout();
                mainPanel.setLayout(sl);
                mainPanel.add(uiSupport.getLblAccount());
                mainPanel.add(uiSupport.getCbbAccount());
                mainPanel.add(uiSupport.getLblCorp());
                mainPanel.add(uiSupport.getRpCorp());
                
                
                uiSupport.getRpCorp().getUITextField().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");
                uiSupport.getRpCorp().getUITextField().getActionMap().put("enter", new AbstractAction() {
    				private static final long serialVersionUID = -6062357605270209625L;

					public void actionPerformed(ActionEvent e) {
                    	uiSupport.getRpCorp().processFocusLost();
                    	LoginOptionSelDlg.this.closeOK();
                    }

                });

//                uiSupport.getRpCorp().setEditable(false);
                sl.putConstraint(SpringLayout.WEST, uiSupport.getLblAccount(), 20, SpringLayout.WEST, mainPanel);
                sl.putConstraint(SpringLayout.WEST, uiSupport.getLblCorp(), 20, SpringLayout.WEST, mainPanel);
                sl.putConstraint(SpringLayout.NORTH, uiSupport.getLblAccount(), 20, SpringLayout.NORTH, mainPanel);
                sl.putConstraint(SpringLayout.NORTH, uiSupport.getLblCorp(), 20, SpringLayout.SOUTH, uiSupport.getLblAccount());

                sl.putConstraint(SpringLayout.WEST, uiSupport.getCbbAccount(), 20, SpringLayout.EAST, uiSupport.getLblAccount());
                sl.putConstraint(SpringLayout.WEST, uiSupport.getRpCorp(), 20, SpringLayout.EAST, uiSupport.getLblCorp());
                sl.putConstraint(SpringLayout.NORTH, uiSupport.getCbbAccount(), 20, SpringLayout.NORTH, mainPanel);
                sl.putConstraint(SpringLayout.NORTH, uiSupport.getRpCorp(), 20, SpringLayout.SOUTH, uiSupport.getCbbAccount());
            }
            return mainPanel;
        }

        public LoginOptionSelDlg(Container parent, String title, LoginUISupport uiSupport) {
            super(parent, title);
            this.uiSupport = uiSupport;
            init();
        }

        private void init() {
            setSize(300, 160);
            JPanel contentPane = new JPanel() {
 				private static final long serialVersionUID = -6632631150778879669L;

				Color c1 = new Color(0xBFD5EE);

                Color c2 = new Color(0xdbe6f2);

                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    Dimension d = getSize();
                    GradientPaint gp = new GradientPaint(0, 0, c1, d.width, 0, c2);
                    Paint oldPaint = g2.getPaint();
                    g2.setPaint(gp);
                    g2.fillRect(0, 0, d.width, d.height);
                    g2.setPaint(oldPaint);
                }
            };
            setContentPane(contentPane);
            getContentPane().setLayout(new BorderLayout());
            getContentPane().add(getBtnPanel(), BorderLayout.SOUTH);
            getContentPane().add(getMainPanel(), BorderLayout.CENTER);

        }

        private UIButton getBtnCancel() {
            if (btnCancel == null) {
                btnCancel = new UIButton("取消");
                btnCancel.setFont(new Font(Style.getFontname(), Font.PLAIN,12));
                btnCancel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        LoginOptionSelDlg.this.closeCancel();
                    }
                });
            }
            return btnCancel;
        }

        private UIButton getBtnOK() {
            if (btnOK == null) {
                btnOK = new UIButton("确定");
                btnOK.setFont(new Font(Style.getFontname(), Font.PLAIN,12));
                btnOK.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        LoginOptionSelDlg.this.closeOK();
                    }
                });
            }
            return btnOK;
        }

    }
}
