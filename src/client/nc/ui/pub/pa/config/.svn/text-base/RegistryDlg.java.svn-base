package nc.ui.pub.pa.config;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.pa.AlertMethod;
import nc.bs.pub.pa.PaConstant;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.pa.IPreAlertConfigQueryService;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.pub.DapCall;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITablePane;
import nc.ui.pub.beans.UITextArea;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.beans.util.MiscUtils;
import nc.ui.pub.component.ChooseUserDlg;
import nc.ui.pub.component.RoleUserBizDelegator;
import nc.ui.sm.funcreg.FuncRegTree;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.logging.Debug;
import nc.vo.ml.Language;
import nc.vo.pf.pub.BasedocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pa.AlertTimeConfig;
import nc.vo.pub.pa.AlertregistryVO;
import nc.vo.pub.pa.AlerttypeBVO;
import nc.vo.pub.pa.AlerttypeVO;
import nc.vo.pub.pa.Key;
import nc.vo.sm.UserVO;
import nc.vo.sm.funcreg.FuncRegisterVO;
import nc.vo.sm.nodepower.OrgnizeTypeVO;
import nc.vo.uap.pf.OrganizeUnit;
import nc.vo.uap.pf.OrganizeUnitComparator;
import nc.vo.uap.pf.OrganizeUnitTypes;
import nc.vo.uap.pf.RoleUserParaVO;

/**
 * 预警条目部署对话框
 * 
 * @author gss 2004-11-23
 * @modifier yzy 2006-1-20 使用数据库VO代替文件实体，并使用新的权限体系进行修改
 * @modifier leijun 2006-5-9 修改预警方式面板，更加清晰
 * @modifier huangzg 2006-7-7 控制其可操作性 2006-7-7
 * @modifier guowl 2009-5 参数支持下拉框类型
 */
public class RegistryDlg extends UIDialog implements ChangeListener, ActionListener {
	public final static String EMPTY = "empty";

	public final static String MODULE_FA = "fa";// 预警类型模块--财务

	public final static String MODULE_GL = "gl";// 预警类型模块--总账

	// 为自定义查询预警类型定义的运算符
	public final static String operator[] = { "", "=", ">", ">=", "<", "<=" };

	// 自定义查询预警中是否包含宏变量
	private boolean hasMacroVar = false;

	private UIRefPane accountRefPane = null;// 主体账簿

	private UIPanel buttonPanel = null;

	private ChooseUserDlg chooserUserdlg = null;

	private UILabel countLabel = null;

	private UIPanel genePanel = null;

	// sxj 2002-11-04 增加状态(true),修改状态(false)
	private boolean isAdd = false;

	// 定时Button
	private UIButton ivjbtnAlartTime = null;

	private UIButton ivjbtnButton = null;

	private UIButton ivjbtnCancel = null;

	// 功能节点触发
	private UIButton ivjbtnFunc = null;

	// 系统登陆
	private UIButton ivjbtnLogin = null;

	// 手机短信
	private UIButton ivjbtnMobile = null;
	
	//IM消息
	private UIButton ivjbtnIM = null;

	private UIButton ivjbtnOK = null;

	// 电子邮件按钮
	private UIButton ivjbtnRealEmail = null;

	// 待办事务按钮
	private UIButton ivjbtnWorkitem = null;

	private UIComboBox ivjcboLanguageName = null;

	private UIComboBox ivjcboTypeName = null;

	// 预警方式-自动调用
	private UICheckBox ivjchkAutoInvoke = null;

	private UICheckBox ivjchkButton = null;

	// 预警方式-邮件
	private UICheckBox ivjchkEmail = null;

	// 预警方式-功能点
	private UICheckBox ivjchkFunc = null;

	// 预警方式-登陆
	private UICheckBox ivjchkLogin = null;

	// 预警方式-手机短信
	private UICheckBox ivjchkMobile = null;

	// 预警方式-预警平台
	private UICheckBox ivjchkPrealertPlatform = null;
	
	//预警方式_IM	
	private UICheckBox ivjchkIm=null;

	// 预警方式-待办事务
	private UICheckBox ivjchkWorkitem = null;

	private UIPanel ivjJDialogContentPane = null;

	private UITabbedPane m_registryTabPane = null;

	private UILabel m_labName = null;

	private UILabel m_labDesc = null;

	private UILabel ivjLable23 = null;

	private UILabel ivjLable24 = null;

	private UILabel ivjlblBusinessPluginName = null;

	private UILabel ivjlblEnable = null;

	private UILabel ivjlblLanguage = null;

	private UILabel ivjlblMessage = null;

	private UILabel ivjlblMsgFileName = null;

	private UILabel ivjlblName = null;

	private UILabel ivjlblTypeDescriptionValue = null;

	private UIPanel ivjpgeCondition = null;

	// 预警方式 面板
	private UIPanel ivjpgeMethod = null;

	private UIPanel ivjpnlAlartMethod = null;

	private UIPanel ivjpnlMsgConfig;

	private UIRadioButton ivjrdoActive = null;

	private UIRadioButton ivjrdoDeActive = null;

	// 产生方式-定时Radio
	private UIRadioButton ivjrdoRunCycle = null;

	// 产生方式-即时
	private UIRadioButton ivjrdoRunInTime = null;

	private UITextField ivjtxtAlartMsgFileName = null;

	private UITextField ivjtxtAlartName = null;

	private UIScrollPane m_spMessage = null;

	private UITablePane ivjUITablePane1 = null;

	// sxj 2003-11-03 add
	// 按钮预警
	private FuncRegisterVO[] m_AlartBtnVOs = null;

	private FuncRegisterVO[] m_AlartEnterVOs = null;

	private AlertMethod m_AlartMethod = null;

	private AlertTimeConfig m_AlartTimeConfig = null;

	private AlerttypeVO m_AlartType = null;

	// sxj 2001-12-15
	BasedocVO[] m_baseDocVOs = null;

	private boolean m_BtnFlag = false;

	private String m_corpPK = null;

	// sxj 2001-11-30
	// 账套
	private String m_dataSource = null;

	// 逻辑选择框
	private UIComboBox m_Editor = new UIComboBox();

	// 变量类型
	EditTypeConst m_editTypeConst = new EditTypeConst();

	private OrganizeUnit[] m_emailUserVOs = null;

	// 是否需要真正电子邮件
	private boolean m_EmailVOsFlag = false;

	// 按钮是否按下的标志
	private boolean m_EnterFlag = false;

	private Box m_fileBox = null;

	Hashtable<String, BasedocVO> m_ht = new Hashtable<String, BasedocVO>();

	// 预警条目键值
	private AlerttypeBVO[] m_keys = null;

	private boolean m_LoginFlag = false;

	private OrganizeUnit[] m_loginUserVOs = null;

	private OrganizeUnit[] m_mobileUserVOs = null;
	
	private OrganizeUnit[] m_imUserVOs =null;

	private boolean m_MobileVOsFlag = false;

	private boolean m_MsgCentralFlag = false;
	
	private boolean m_MsgIMFlag=false;

	// 预警条目
	AlertregistryVO m_registry = null;

	// 当前的预警类型
	private AlerttypeVO m_selectedType = null;

	// 为数据传输的Plugin
	private String m_specialPlugin = null;

	private OrganizeUnit[] m_workitemUserVOs = null;

	private ChooseUserDlg msgChooseUserdlg = null;
	
	private ChooseUserDlg imChooseUserDlg =null;

	// 产生方式Panel
	private UIPanel produceMethod = null;

	private boolean[] selections = new boolean[4];// 纪录触发方式三者的选择状态,还有预警平台和自动调用

	private TriggerFunctionDlg triggerFuncDlg = null;

	private UITextArea txtAreaMessage = null;

	private UILabel m_descLabel;

	private UITextField m_tfMaxlog;

	private UILabel m_labMaxlog;

	private HashMap<Integer, String> refPKMap = null;

	private HashMap<String, ComboxValue[]> refname_Cmbitems_Map = new HashMap<String, ComboxValue[]>();

	private PaRegistryTableModel registryTableModel;

	private boolean isQePreAlert;

	/**
	 * @param owner
	 */
	public RegistryDlg(Container owner) {
		super(owner);
		initialize();
	}

	/**
	 * @param owner
	 * @param typenameid
	 */
	public RegistryDlg(Container owner, String typenameid) {
		super(owner);
		m_specialPlugin = typenameid;
		initialize();
	}

	/**
	 * 向panel中添加组件compo
	 * 
	 * @param panel
	 * @param compo
	 * @param constraints
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	private void add(UIPanel panel, JComponent compo, GridBagConstraints constraints, int x, int y,
			int w, int h) {
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridheight = h;
		constraints.gridwidth = w;
		panel.add(compo, constraints);
	}

	private FuncRegisterVO[] alartmethod2Funcregistervos() {
		if (m_AlartMethod != null) {
			String[] funCodes = m_AlartMethod.getNext();
			if (funCodes != null) {
				FuncRegisterVO[] initFuncRegVOs = new FuncRegisterVO[funCodes.length];
				for (int i = 0; i < funCodes.length; i++) {
					FuncRegisterVO tempVO = new FuncRegisterVO();
					tempVO.setFunCode(funCodes[i]);
					// 从多语资源中获取功能节点名称
					tempVO.setFunName(FuncRegTree.getFunTransStr(funCodes[i], null));
					initFuncRegVOs[i] = tempVO;
				}
				return initFuncRegVOs;
			}
		}
		return null;
	}

	/**
	 * Comment
	 */

	public void btnAlartTime_ActionPerformed(ActionEvent actionEvent) {
		if (m_AlartTimeConfig == null)
			m_AlartTimeConfig = new AlertTimeConfig();
		AlertTimingDlg timingDlg = new AlertTimingDlg(this);
		timingDlg.setAlertTime(m_AlartTimeConfig);
		timingDlg.setLocationRelativeTo(this);
		timingDlg.setModal(true);
		timingDlg.setVisible(true);
		if (timingDlg.getResult() == UIDialog.ID_OK) {
			m_AlartTimeConfig = timingDlg.getAlertTime();
		}
		return;
	}

	public void onClickButton() {
		// 按钮配置按钮按下
		m_BtnFlag = true;
		TriggerButtonDlg dlg = newTriggerBtnDlg();
		if (dlg.isInitOK()) {
			// 只有初始化成功后才可显示该对话框
			dlg.setLocationRelativeTo(this);
			dlg.setModal(true);
			dlg.setVisible(true);
			if (dlg.getResult() == UIDialog.ID_OK) {
				m_AlartBtnVOs = dlg.getSelectedButtonVOs();
			}
		} else {
			MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("101502",
					"UPP101502-000079")/* @res""提示"" */, NCLangRes.getInstance().getStrByID("101502",
					"UPP101502-000292")/* @res""只有安装了HR或ARAP产品才可使用该功能！"" */);
		}
		return;
	}

	/**
	 * Comment
	 */
	public void onClickCancel(ActionEvent actionEvent) {
		this.closeCancel();
	}

	/**
	 * Comment
	 */
	public void btnMobile_ActionPerformed1(ActionEvent actionEvent) {
		// 手机短信配置按钮按下
		m_MobileVOsFlag = true;
		String[] userIds = null;
		if (m_AlartMethod != null) {
			userIds = m_AlartMethod.getMobiles();
		}
		RoleUserParaVO paravo = new RoleUserParaVO();
		OrganizeUnit[] roleuservos = RoleUserBizDelegator.getInstance().getLocalUserVOByPKCorp(
				getCorpPK());
		OrganizeUnit[] uservos = readChoosedUser(userIds);
		paravo.setCorppk(getCorpPK());
		paravo.setRoleuservos(roleuservos);
		paravo.setSelectedRoleUservos(uservos);
		paravo.setShowAllCorp(false);
		ChooseUserDlg dlg = new ChooseUserDlg(this, paravo);
		//
		dlg.setLocationRelativeTo(this);
		dlg.setModal(true);
		dlg.setTitle(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000050")/* @res""手机短信选择"" */);
		dlg.setVisible(true);
		if (dlg.getResult() == UIDialog.ID_OK) {
			m_mobileUserVOs = dlg.getResultVOs();
		}
		return;
	}
	

	/**
	 * Comment
	 */
	public void onClickOK(ActionEvent actionEvent) {
		// 必须在校验之前取做

		if (getTable().getCellEditor() != null) {
			getTable().getCellEditor().stopCellEditing();
		}

		// 检表模型中的数
		if (validateInput() == false)
			return;
		this.closeOK();
	}

	public void cboTypeName_ActionPerformed(ActionEvent actionEvent) {
		if (actionEvent.getSource().equals(getcboTypeName())) {
			Object obj = getcboTypeName().getSelectedItem();
			if (obj instanceof AlarmTypeConst) {
				String item = ((AlarmTypeConst) obj).getValue().toString();
				if (item.equals(EMPTY)) {
					setKeys(null);
					getTable().updateUI();
					return;
				}

				try {
					// 将此类型从服务其上读取
					AlerttypeVO at = NCLocator.getInstance().lookup(IPreAlertConfigQueryService.class)
							.queryAlertyTypeByPKAgg(item);

					// 控制keys
					AlerttypeBVO[] keys = composeKeys(m_AlartType, at);
					at.setAlertVariables(keys);

					if (at.getBusi_plugin() != null
							&& at.getBusi_plugin().equals("nc.bs.pub.querymodel.pa.QueryEnginePlugin") && isAdd()) {
						Class cls = Class.forName("nc.ui.pub.querymodel.pa.QueryRefKeysDefiner");
						IKeysUserDefiner definer = (IKeysUserDefiner) cls.newInstance();
						Key[] Qekeys = definer.getKeys(this);
						if (Qekeys != null) {
							List<Key> list = Arrays.asList(Qekeys);
							List<Key> keyList = new ArrayList<Key>(Qekeys.length);
							if (Qekeys.length > 0) {
								// 查询引擎返回的keys中最后一项表示是否包含宏变量
								hasMacroVar = (Boolean) Qekeys[Qekeys.length - 1].getValue();
								keyList = list.subList(0, list.size() - 1);
							}
							at
									.setAlertVariables(PaConstant.transKey2VO(keyList
											.toArray(new Key[keyList.size()])));
						}
					}
					setKeys(at.getAlertVariables());
					setSelectedType(at);
					setConditionUI(at);
					setKeys(at.getAlertVariables());
					getTable().updateUI();
				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
				}
			} else {
				setKeys(null);
				setSelectedType(null);
				setConditionUI(null);
				setKeys(null);
				getTable().updateUI();
			}
		}

	}

	private void changeAccout(String belong_system) {
		int type = getAccountType(belong_system);
		if (type == 2) {
			String refpk = getUIRefAccount().getRefPK();
			getUIRefAccount().setRefNodeName("固定资产核算账簿");
			getUIRefAccount().getRefModel().setMatchPkWithWherePart(true);// 取消关联.11.2+
			getUIRefAccount().setPK(refpk);
		} else if (type == 1) {
			String refpk = getUIRefAccount().getRefPK();
			getUIRefAccount().setRefNodeName("主体账簿");
			getUIRefAccount().setPK(refpk);
		} else {
			getUIRefAccount().setPK(null);
		}
	}

	/**
	 * Comment
	 */
	public void chkAlartMethodEmail_ActionPerformed(ActionEvent actionEvent) {
		getbtnWorkitem().setEnabled(getchkMsgCentral().isSelected());
	}

	/**
	 * Comment
	 */
	public void chkAlartMethodMobile_ActionPerformed(ActionEvent actionEvent) {
		getbtnMobile().setEnabled(getchkAlartMethodMobile().isSelected());
	}

	/**
	 * Comment
	 */
	public void chkAlartMethodMobile_ActionPerformed1(ActionEvent actionEvent) {
		getbtnMobile().setEnabled(getchkAlartMethodMobile().isSelected());
	}
	
	
	public void chkIM_ActionPerformed(ActionEvent actionEvent) {
		getbtnIM().setEnabled(getchkIM().isSelected());
	}

	/**
	 * Comment
	 */
	public void chkButton_ActionPerformed(ActionEvent actionEvent) {
		getbtnOK().setEnabled(getchkAlartMethodLogin().isSelected());
	}

	private AlerttypeBVO[] composeKeys(AlerttypeVO oldType, AlerttypeVO newtype) {
		if (oldType == null) {
			AlerttypeBVO[] newKeys = newtype.getAlertVariables();
			for (AlerttypeBVO alerttypeBVO : newKeys) {
				if (alerttypeBVO.getVartype().equals(m_editTypeConst.COMBOBOX.getValue().toString()))
					dealComboxItems(alerttypeBVO, true);
			}
			return newKeys;// 这时候是新增，返回新的空值
		}

		if (oldType != null && oldType.getAlertVariables() != null && newtype != null) {
			if (oldType.getBusi_plugin().equals("nc.bs.pub.querymodel.pa.QueryEnginePlugin")) {
				return oldType.getAlertVariables();// 如果是查询引擎插件,只能返回原先的值
			} else {// 否则,去比较新旧的值，以新的值为基准来设置其具体值！
				AlerttypeBVO[] newKeys = newtype.getAlertVariables();
				for (int i = 0; i < newKeys.length; i++) {
					if (newKeys[i].getVartype().equals(m_editTypeConst.COMBOBOX.getValue().toString()))
						dealComboxItems(newKeys[i], false);
					for (int j = 0; j < oldType.getAlertVariables().length; j++) {
						if (newKeys[i].getFieldname().equals(oldType.getAlertVariables()[j].getFieldname())) {
							newKeys[i].setValue(oldType.getAlertVariables()[j].getValue());
							newKeys[i].setRefervalue(oldType.getAlertVariables()[j].getRefervalue());
							continue;
						}
					}
				}
				return newKeys;
			}
		}
		return null;
	}

	private void dealComboxItems(AlerttypeBVO alertTypeBVO, boolean isNew) {
		String refName = alertTypeBVO.getRefername();
		String[] strValues = refName.split(";");
		if (strValues == null || strValues.length == 0) { return; }

		ArrayList<ComboxValue> cmbItems = new ArrayList<ComboxValue>();
		for (int i = 0; i < strValues.length; i++) {
			String strValue = strValues[i].trim();
			if (StringUtil.isEmptyWithTrim(strValue)) {
				break;
			}
			String tempStr = strValue.substring(1, strValue.length() - 1);
			if (StringUtil.isEmptyWithTrim(tempStr)) {
				break;
			}
			String[] temp = tempStr.split(",");
			if (temp == null || temp.length < 2) {
				break;
			} else {
				ComboxValue cmbValue = new ComboxValue();
				cmbValue.setDisplayName(temp[0].trim());
				cmbValue.setValue(temp[1].trim());
				if (temp.length > 2 && new UFBoolean(temp[2]).booleanValue()) {
					cmbValue.setIsDefault(UFBoolean.TRUE);
					if (isNew) {
						alertTypeBVO.setValue(temp[1].trim());
						alertTypeBVO.setRefervalue(temp[0].trim());
					}
				}
				cmbItems.add(cmbValue);
			}
		}
		refname_Cmbitems_Map.put(refName, cmbItems.toArray(new ComboxValue[0]));
	}

	private void funcregistervo2Alertmethod() {
		if (m_AlartEnterVOs != null) {
			String funcname = "";
			String funccode = "";
			for (int i = 0; i < m_AlartEnterVOs.length; i++) {
				/*
				 * 为了避免、功能节点名过长(特别是在英文下,于是直接直接存功能节点号算了。
				 * 当然最好的办法就是该数据库了,但是现在临近发板了，最好不让改库。
				 */
				funcname += m_AlartEnterVOs[i].getFunName() + ";";
				funccode += m_AlartEnterVOs[i].getFunCode() + ";";
			}
			if (m_AlartMethod == null)
				m_AlartMethod = new AlertMethod();
			m_AlartMethod.setNexts(funccode);
			m_AlartMethod.setNextsAttribute(funcname);
		}
	}

	/**
	 * 判断主体账簿的类型
	 */
	private int getAccountType(String modulename) {
		if (modulename.equalsIgnoreCase(MODULE_GL))
			return 1;
		else if (modulename.equalsIgnoreCase(MODULE_FA))
			return 2;
		else
			return 0;

	}

	/**
	 * @return nc.vo.pub.pa.AlartTimeConfig
	 */
	public AlertTimeConfig getAlartTimeConfig() {
		return m_AlartTimeConfig;
	}

	/**
	 * 根据对应基础档案类型的bdname得到BasedocVO对象 创建日期：(2001-8-14 22:16:28)
	 * 
	 * @return nc.vo.dap.pub.BasedocVO
	 * @param bdname
	 *            java.lang.String
	 */
	public BasedocVO getBasedocVO(String bdname) {
		if (bdname == null)
			return null;
		BasedocVO tembasedocvo = (BasedocVO) m_ht.get(bdname);
		return tembasedocvo;
	}

	private UIButton getbtnButton() {
		if (ivjbtnButton == null) {
			ivjbtnButton = new UIButton();
			ivjbtnButton.setName("btnButton");
			ivjbtnButton
					.setText(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000052")/* @res""按钮"" */);
			ivjbtnButton.setPreferredSize(new Dimension(125, 23));
			ivjbtnButton.setEnabled(false);
		}
		return ivjbtnButton;
	}

	private UIButton getbtnCancel() {
		if (ivjbtnCancel == null) {
			ivjbtnCancel = new UIButton();
			ivjbtnCancel.setName("btnCancel");
			ivjbtnCancel
					.setText(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000000")/* 取消 */);
			ivjbtnCancel.setPreferredSize(new Dimension(77, 22));
		}
		return ivjbtnCancel;
	}

	private UIButton getbtnLogin() {
		if (ivjbtnLogin == null) {
			ivjbtnLogin = new UIButton();
			ivjbtnLogin.setName("btnLogin");
			ivjbtnLogin
					.setText(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000053")/* @res""系统登录"" */);
			ivjbtnLogin.setPreferredSize(new Dimension(125, 23));
			ivjbtnLogin.setEnabled(false);
		}
		return ivjbtnLogin;
	}

	private UIButton getbtnMobile() {
		if (ivjbtnMobile == null) {
			ivjbtnMobile = new UIButton();
			ivjbtnMobile.setName("btnMobile");
			ivjbtnMobile
					.setText(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000054")/* @res""手机短信"" */);
			ivjbtnMobile.setPreferredSize(new Dimension(77, 23));
			ivjbtnMobile.setEnabled(false);
		}
		return ivjbtnMobile;
	}
	
	private UIButton getbtnIM() {
		if (ivjbtnIM == null) {
			ivjbtnIM = new UIButton();
			ivjbtnIM.setName("btnIM");
			ivjbtnIM.setText(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000125")/*IM消息*/);
			ivjbtnIM.setPreferredSize(new Dimension(77, 23));
			ivjbtnIM.setEnabled(false);
		}
		return ivjbtnIM;
	}

	private UIButton getbtnNext() {
		if (ivjbtnFunc == null) {
			ivjbtnFunc = new UIButton();
			ivjbtnFunc.setName("btnNext");
			ivjbtnFunc
					.setText(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000055")/* @res""触发点提示"" */);
			ivjbtnFunc.setPreferredSize(new Dimension(125, 23));
			ivjbtnFunc.setEnabled(false);
		}
		return ivjbtnFunc;
	}

	private UIButton getbtnOK() {
		if (ivjbtnOK == null) {
			ivjbtnOK = new UIButton();
			ivjbtnOK.setName("btnOK");
			ivjbtnOK.setText(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000001")/* 确定 */);
			ivjbtnOK.setPreferredSize(new Dimension(77, 22));
		}
		return ivjbtnOK;
	}

	private UIButton getbtnRealEmail() {
		if (ivjbtnRealEmail == null) {
			ivjbtnRealEmail = new UIButton();
			ivjbtnRealEmail.setName("btnRealEmail");
			ivjbtnRealEmail
					.setText(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000112")/* @res""电子邮件"" */);
			ivjbtnRealEmail.setPreferredSize(new Dimension(77, 23));
			ivjbtnRealEmail.setEnabled(false);
		}
		return ivjbtnRealEmail;
	}

	private UIButton getBtnTiming() {
		if (ivjbtnAlartTime == null) {
			ivjbtnAlartTime = new UIButton();
			ivjbtnAlartTime.setName("btnAlartTime");
			ivjbtnAlartTime
					.setText(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000051")/* @res""定时"" */);
			ivjbtnAlartTime.setEnabled(false);
		}
		return ivjbtnAlartTime;
	}

	private UIButton getbtnWorkitem() {
		if (ivjbtnWorkitem == null) {
			ivjbtnWorkitem = new UIButton();
			ivjbtnWorkitem.setName("btnEmail");
			ivjbtnWorkitem
					.setText(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000277")/* @res""消息中心"" */);
			ivjbtnWorkitem.setPreferredSize(new Dimension(77, 23));
			ivjbtnWorkitem.setEnabled(false);
		}
		return ivjbtnWorkitem;
	}

	private UIPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new UIPanel();
			buttonPanel.add(getbtnOK(), BorderLayout.CENTER);
			UIPanel blank = new UIPanel();
			blank.setPreferredSize(new Dimension(40, 20));
			buttonPanel.add(blank);
			buttonPanel.add(getbtnCancel(), BorderLayout.CENTER);
		}
		return buttonPanel;
	}

	public UIComboBox getcboLanguage() {
		if (ivjcboLanguageName == null) {
			ivjcboLanguageName = new UIComboBox();
			ivjcboLanguageName.setName("cboLanguageName");
			Language[] langCode = NCLangRes.getInstance().getAllLanguages();
			if (langCode != null) {
				for (int i = 0; i < langCode.length; i++) {
					String resId = "UPP101502-" + langCode[i].getCode();
					String langName = NCLangRes.getInstance().getStrByID("101502", resId);
					DefaultConstEnum langConst = new DefaultConstEnum(langCode[i].getCode(), langName);
					getcboLanguage().addItem(langConst);
				}
			}
		}
		return ivjcboLanguageName;
	}

	private UIComboBox getcboTypeName() {
		if (ivjcboTypeName == null) {
			ivjcboTypeName = new UIComboBox();
			ivjcboTypeName.setName("cboTypeName");
			ivjcboTypeName.setForeground(java.awt.Color.red);

			// 给其增加一个空的选项
			AlarmTypeConst emptyType = new AlarmTypeConst(EMPTY, "");
			ivjcboTypeName.addItem(emptyType);

			// 初始化预警条件
			if (m_specialPlugin != null) {
				AlerttypeVO typevo = null;
				try {
					String sql = "busi_plugin ='" + m_specialPlugin + "'";
					Collection coll = NCLocator.getInstance().lookup(IUAPQueryBS.class).retrieveByClause(
							AlerttypeVO.class, sql);
					if (coll != null && coll.size() > 0)
						typevo = ((AlerttypeVO) coll.iterator().next());
				} catch (BusinessException e) {
					Logger.error(e.getMessage(), e);
				}
				if (typevo != null) {
					String pulinname = NCLangRes.getInstance().getStrByID("prealerttype",
							typevo.getName_resid());
					AlarmTypeConst type = new AlarmTypeConst(typevo.getPrimaryKey(), pulinname);
					ivjcboTypeName.addItem(type);
				}
			} else {
				try {
					ArrayList alTypes = queryAllTypes();

					if (alTypes.size() > 0) {
						AlarmTypeConst[] constEnums = new AlarmTypeConst[alTypes.size()];
						for (int i = 0; i < alTypes.size(); i++) {
							AlerttypeVO atVO = (AlerttypeVO) alTypes.get(i);
							String typename = "";
							if (atVO.getName_resid() != null) {
								typename = NCLangRes.getInstance().getStrByID("prealerttype", atVO.getName_resid());
							} else {
								typename = atVO.getType_name();
							}
							constEnums[i] = new AlarmTypeConst(atVO.getPrimaryKey(), typename);
						}

						Arrays.sort(constEnums, new Comparator() {
							public int compare(Object o1, Object o2) {
								return MiscUtils.compareStringByBytes(((AlarmTypeConst) o1).getName(),
										((AlarmTypeConst) o2).getName());
							}
						});

						ivjcboTypeName.addItems(constEnums);
					}

					ivjcboTypeName.setPreferredSize(new Dimension(120, 20));
					ivjcboTypeName.setMaximumSize(new Dimension(180, 20));
				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
				}
			}
		}
		return ivjcboTypeName;
	}

	protected ArrayList queryAllTypes() throws BusinessException {
		AlerttypeVO[] atVos = NCLocator.getInstance().lookup(IPreAlertConfigQueryService.class)
				.queryAllAlertTypes();

		ArrayList<AlerttypeVO> alRet = new ArrayList<AlerttypeVO>();
		for (int i = 0; i < (atVos == null ? 0 : atVos.length); i++) {
			if (atVos[i].getTasktype() == PaConstant.TASKTYPE_PA)
				alRet.add(atVos[i]);
		}

		return alRet;
	}

	private UICheckBox getchkAlartMethodInvoke() {
		if (ivjchkAutoInvoke == null) {
			ivjchkAutoInvoke = new UICheckBox();
			ivjchkAutoInvoke.setName("chkAlartMethodInvoke");
			ivjchkAutoInvoke.setSelected(false);
			ivjchkAutoInvoke
					.setText(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000056")/* @res""自动调用"" */);
			ivjchkAutoInvoke.setEnabled(true);
			ivjchkAutoInvoke.setFocusPainted(true);
		}
		return ivjchkAutoInvoke;
	}

	private UICheckBox getchkAlartMethodLogin() {
		if (ivjchkLogin == null) {
			ivjchkLogin = new UICheckBox();
			ivjchkLogin.setName("a");
			ivjchkLogin.setText("");
			ivjchkLogin.setPreferredSize(new Dimension(22, 22));
		}
		return ivjchkLogin;
	}

	private UICheckBox getchkAlartMethodMobile() {
		if (ivjchkMobile == null) {
			ivjchkMobile = new UICheckBox();
			ivjchkMobile.setName("b");
			ivjchkMobile.setText("");
			ivjchkMobile.setPreferredSize(new Dimension(22, 22));
		}
		return ivjchkMobile;
	}

	private UICheckBox getchkAlartMethodNext() {
		if (ivjchkFunc == null) {
			ivjchkFunc = new UICheckBox();
			ivjchkFunc.setName("c");
			ivjchkFunc.setText("");
			ivjchkFunc.setPreferredSize(new Dimension(22, 22));
		}
		return ivjchkFunc;
	}

	private UICheckBox getchkAlartMethodPlatform() {
		if (ivjchkPrealertPlatform == null) {
			ivjchkPrealertPlatform = new UICheckBox();
			ivjchkPrealertPlatform.setName("chkAlartMethodPlatform");
			ivjchkPrealertPlatform.setSelected(true);
			ivjchkPrealertPlatform.setText(NCLangRes.getInstance().getStrByID("101502",
					"UPP101502-000057")/* @res""预警平台"" */);
		}
		return ivjchkPrealertPlatform;
	}
	
	private UICheckBox getchkIM() {
		if (ivjchkIm == null) {
			ivjchkIm = new UICheckBox();
			ivjchkIm.setName("i");
			ivjchkIm.setText("");
			ivjchkIm.setPreferredSize(new Dimension(22, 22));
		}
		return ivjchkIm;
	}

	private UICheckBox getchkAlartMethodRealEmail() {
		if (ivjchkEmail == null) {
			ivjchkEmail = new UICheckBox();
			ivjchkEmail.setName("chkAlartMethodRealEmail");
			ivjchkEmail.setText("");
			ivjchkEmail.setPreferredSize(new Dimension(22, 22));
		}
		return ivjchkEmail;
	}

	private UICheckBox getchkButton() {
		if (ivjchkButton == null) {
			ivjchkButton = new UICheckBox();
			ivjchkButton.setName("chkButton");
			ivjchkButton.setText("");
			ivjchkButton.setPreferredSize(new Dimension(22, 22));
		}
		return ivjchkButton;
	}

	private UICheckBox getchkMsgCentral() {
		if (ivjchkWorkitem == null) {
			ivjchkWorkitem = new UICheckBox();
			ivjchkWorkitem.setName("chkAlartMethodEmail");
			ivjchkWorkitem.setText("");
			ivjchkWorkitem.setPreferredSize(new Dimension(22, 22));
		}
		return ivjchkWorkitem;
	}

	private ChooseUserDlg getChooserUserDlg(String[] userIds) {
		if (chooserUserdlg == null) {
			RoleUserParaVO paravo = new RoleUserParaVO();
			OrganizeUnit[] roleuservos = RoleUserBizDelegator.getInstance().getLocalUserVOByPKCorp(
					getCorpPK());
			OrganizeUnit[] uservos = readChoosedUser(userIds);
			paravo.setCorppk(getCorpPK());
			paravo.setRoleuservos(roleuservos);
			m_loginUserVOs = uservos;
			paravo.setSelectedRoleUservos(uservos);
			paravo.setShowAllCorp(false);
			chooserUserdlg = new ChooseUserDlg(this, paravo);
		}
		return chooserUserdlg;
	}

	/**
	 * @return java.lang.String
	 */
	public java.lang.String getCorpPK() {
		return m_corpPK;
	}

	/**
	 * @return java.lang.String
	 */
	public java.lang.String getDataSource() {
		return m_dataSource;
	}

	private UIPanel getGeneralTab() {
		if (genePanel == null) {
			genePanel = new UIPanel();
			genePanel.setName("genePanel");
			GridBagLayout layOut = new GridBagLayout();
			genePanel.setLayout(layOut);
			GridBagConstraints constraints = new GridBagConstraints();
			Insets inset = new Insets(5, 5, 10, 3);// 6, 8, 6, 8
			constraints.insets = inset;
			constraints.weightx = 0;
			constraints.weighty = 0;
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.WEST;
			// 预警名称lbl
			add(genePanel, getlblName(), constraints, 0, 0, 1, 1);
			// 预警名称txt
			add(genePanel, gettxtAlartName(), constraints, 1, 0, 4, 1);

			// 预警信息文件名label
			add(genePanel, getlblMsgFileName(), constraints, 0, 1, 1, 1);
			// 预警信息文件名text
			add(genePanel, gettxtAlartMsgPanel(), constraints, 1, 1, 4, 1);

			// 预警状态lal
			constraints.fill = GridBagConstraints.NONE;
			add(genePanel, getlblEnable(), constraints, 0, 2, 1, 1);
			// 激活radio
			add(genePanel, getrdoActive(), constraints, 1, 2, 1, 1);
			// 
			add(genePanel, getrdoDeActive(), constraints, 2, 2, 1, 1);

			// leijun+
			add(genePanel, getMaxLogPane(), constraints, 3, 2, 2, 1);

			constraints.fill = GridBagConstraints.EAST;
			// 
			// 预警信息label
			add(genePanel, getlblMessage(), constraints, 0, 3, 1, 1);
			// 这个还是有用,千万别去掉，否则多语时候便会有问题,hzg.2006-11-9
			// 预警提示语言label
			add(genePanel, getlblLanguage(), constraints, 3, 3, 1, 1);
			// 预警提示语言combo
			add(genePanel, getcboLanguage(), constraints, 4, 3, 1, 1);
			// 预警提示信息txt
			constraints.fill = GridBagConstraints.BOTH;
			constraints.weightx = 100;
			constraints.weighty = 100;
			add(genePanel, getSpMessage(), constraints, 0, 4, 5, 1);
			// user code begin {1}
			ButtonGroup bg = new ButtonGroup();
			bg.add(getrdoActive());
			bg.add(getrdoDeActive());
			getrdoActive().setSelected(true);
		}
		return genePanel;
	}

	private Box getMaxLogPane() {
		Box maxlogBox = Box.createHorizontalBox();
		maxlogBox.add(getLabMaxLog());
		maxlogBox.add(Box.createHorizontalStrut(20));
		maxlogBox.add(getTfMaxLog());
		return maxlogBox;
	}

	private UITextField getTfMaxLog() {
		if (m_tfMaxlog == null) {
			m_tfMaxlog = new UITextField();
			m_tfMaxlog.setMinimumSize(new Dimension(50, 20));
			m_tfMaxlog.setTextType("TextInt");
		}
		return m_tfMaxlog;
	}

	private UILabel getLabMaxLog() {
		if (m_labMaxlog == null) {
			m_labMaxlog = new UILabel(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000381"));// 最大日志数
		}
		return m_labMaxlog;
	}

	private UIPanel getJDialogContentPane() {
		if (ivjJDialogContentPane == null) {
			ivjJDialogContentPane = new UIPanel();
			ivjJDialogContentPane.setName("JDialogContentPane");
			ivjJDialogContentPane.setLayout(new BorderLayout());
			ivjJDialogContentPane.add(getRegistryTabPane(), BorderLayout.CENTER);
			ivjJDialogContentPane.add(getButtonPanel(), BorderLayout.SOUTH);
		}
		return ivjJDialogContentPane;
	}

	protected UITabbedPane getRegistryTabPane() {
		if (m_registryTabPane == null) {
			m_registryTabPane = new UITabbedPane();
			m_registryTabPane.setName("m_registryTabPane");
			m_registryTabPane.insertTab(
					NCLangRes.getInstance().getStrByID("101502", "UPP101502-000058")/* @res""常规属性"" */,
					null, getGeneralTab(), null, 0);
			m_registryTabPane.insertTab(
					NCLangRes.getInstance().getStrByID("101502", "UPP101502-000059")/* @res""预警条件"" */,
					null, getpgeCondition(), null, 1);
			m_registryTabPane.insertTab(
					NCLangRes.getInstance().getStrByID("101502", "UPP101502-000060")/* @res""预警方式"" */,
					null, getpgeMethod(), null, 2);
		}
		return m_registryTabPane;
	}

	/**
	 * @return Returns the m_keys.
	 */
	AlerttypeBVO[] getKeys() {
		return m_keys;
	}

	private UILabel getLabName() {
		if (m_labName == null) {
			m_labName = new UILabel();
			m_labName.setName("Lable21");
			m_labName.setILabelType(UILabel.STYLE_NOTNULL);
			m_labName
					.setText(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000113")/* @res""类型"" */);
		}
		return m_labName;
	}

	private UILabel getLabDesc() {
		if (m_labDesc == null) {
			m_labDesc = new UILabel();
			m_labDesc.setName("Lable22");
			m_labDesc
					.setText(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000061")/* @res""描述:"" */);
		}
		return m_labDesc;
	}

	protected UILabel getLabRealParam() {
		if (ivjLable23 == null) {
			ivjLable23 = new UILabel();
			ivjLable23.setName("Lable23");
			ivjLable23
					.setText(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000062")/* @res""条件"" */);
		}
		return ivjLable23;
	}

	private UILabel getLabPlugin() {
		if (ivjLable24 == null) {
			ivjLable24 = new UILabel();
			ivjLable24.setName("Lable24");
			ivjLable24
					.setText(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000063")/* @res""业务插件:"" */);
		}
		return ivjLable24;
	}

	/**
	 * @return
	 */
	private UILabel getLableTotalAccount() {
		if (countLabel == null) {
			countLabel = new UILabel();
			countLabel.setName("countLabel");
			countLabel.setText(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000264")/* 总帐 */);
		}
		return countLabel;
	}

	private UILabel getlblBusinessPluginName() {
		if (ivjlblBusinessPluginName == null) {
			ivjlblBusinessPluginName = new UILabel();
			ivjlblBusinessPluginName.setName("lblBusinessPluginName");
			ivjlblBusinessPluginName.setText(NCLangRes.getInstance().getStrByID("101502",
					"UPP101502-000064")/* @res""业务插件类名"" */);
			ivjlblBusinessPluginName.setForeground(java.awt.Color.gray);
			ivjlblBusinessPluginName.setPreferredSize(new Dimension(250, 20));
			ivjlblBusinessPluginName.setMaximumSize(new Dimension(300, 20));
		}
		return ivjlblBusinessPluginName;
	}

	protected UILabel getlblEnable() {
		if (ivjlblEnable == null) {
			ivjlblEnable = new UILabel();
			ivjlblEnable.setName("lblEnable");
			ivjlblEnable
					.setText(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000065")/* @res""预警状态:"" */);
		}
		return ivjlblEnable;
	}

	protected UILabel getlblLanguage() {
		if (ivjlblLanguage == null) {
			ivjlblLanguage = new UILabel();
			ivjlblLanguage.setName("lblLanguage");
			ivjlblLanguage
					.setText(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000118")/* 语言" */);
		}
		return ivjlblLanguage;
	}

	protected UILabel getlblMessage() {
		if (ivjlblMessage == null) {
			ivjlblMessage = new UILabel();
			ivjlblMessage.setName("lblMessage");
			ivjlblMessage
					.setText(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000066")/* @res""预警信息"" */);
		}
		return ivjlblMessage;
	}

	protected UILabel getlblMsgFileName() {
		if (ivjlblMsgFileName == null) {
			ivjlblMsgFileName = new UILabel();
			ivjlblMsgFileName.setName("lblMsgFileName");
			ivjlblMsgFileName.setILabelType(UILabel.STYLE_NOTNULL);
			ivjlblMsgFileName
					.setText(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000067")/* @res""预警信息文件名(英语字符)"" */);
		}
		return ivjlblMsgFileName;
	}

	protected UILabel getlblName() {
		if (ivjlblName == null) {
			ivjlblName = new UILabel();
			ivjlblName.setName("lblName");
			ivjlblName.setILabelType(UILabel.STYLE_NOTNULL);
			ivjlblName
					.setText(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000068")/* @res""预警名称:"" */);
		}
		return ivjlblName;
	}

	private UILabel getlblTypeDescriptionValue() {
		if (ivjlblTypeDescriptionValue == null) {
			ivjlblTypeDescriptionValue = new UILabel();
			ivjlblTypeDescriptionValue.setName("lblTypeDescriptionValue");
			ivjlblTypeDescriptionValue.setText(NCLangRes.getInstance().getStrByID("101502",
					"UPP101502-000069")/* @res""类型描述"" */);
			ivjlblTypeDescriptionValue.setForeground(java.awt.Color.gray);
		}
		return ivjlblTypeDescriptionValue;
	}

	private ChooseUserDlg getMsgCentralChooseUserDlg(String[] userIds) {
		if (msgChooseUserdlg == null) {

			RoleUserParaVO paravo = new RoleUserParaVO();
			OrganizeUnit[] roleuservos = RoleUserBizDelegator.getInstance().getLocalUserVOByPKCorp(
					getCorpPK());
			OrganizeUnit[] uservos = readChoosedUser(userIds);
			paravo.setCorppk(getCorpPK());
			paravo.setRoleuservos(roleuservos);
			paravo.setSelectedRoleUservos(uservos);
			paravo.setShowAllCorp(false);
			msgChooseUserdlg = new ChooseUserDlg(this, paravo);

			msgChooseUserdlg.setLocationRelativeTo(this);
			msgChooseUserdlg.setModal(true);
			msgChooseUserdlg
					.setTitle(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000277")/* @res""消息中心"" */);//
		}
		return msgChooseUserdlg;
	}
	
	private ChooseUserDlg getIMChooseUserDlg(String[] userIds) {
		if (imChooseUserDlg == null) {

			RoleUserParaVO paravo = new RoleUserParaVO();
			OrganizeUnit[] roleuservos = RoleUserBizDelegator.getInstance().getLocalUserVOByPKCorp(
					getCorpPK());
			OrganizeUnit[] uservos = readChoosedUser(userIds);
			paravo.setCorppk(getCorpPK());
			paravo.setRoleuservos(roleuservos);
			paravo.setSelectedRoleUservos(uservos);
			paravo.setShowAllCorp(false);
			imChooseUserDlg = new ChooseUserDlg(this, paravo);

			imChooseUserDlg.setLocationRelativeTo(this);
			imChooseUserDlg.setModal(true);
			imChooseUserDlg.setTitle(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000125")/* @res""IM消息"" */);
		}
		return imChooseUserDlg;
	}
	

	private UIPanel getpgeCondition() {
		if (ivjpgeCondition == null) {
			ivjpgeCondition = new UIPanel();

			ivjpgeCondition.setName("pgeCondition");
			GridBagLayout layOut = new GridBagLayout();
			ivjpgeCondition.setLayout(layOut);
			GridBagConstraints constraints = new GridBagConstraints();
			Insets inset = new Insets(4, 4, 4, 4);
			constraints.insets = inset;
			constraints.weightx = 0;
			constraints.weighty = 0;
			constraints.fill = GridBagConstraints.BOTH;
			constraints.anchor = GridBagConstraints.WEST;
			// 预警类型label
			add(ivjpgeCondition, getLabName(), constraints, 0, 0, 1, 1);
			// 预警类型ComboBox
			// constraints.fill = GridBagConstraints.HORIZONTAL;
			add(ivjpgeCondition, getcboTypeName(), constraints, 1, 0, 1, 1);

			// 业务插件label
			constraints.weightx = 0;
			add(ivjpgeCondition, getLabPlugin(), constraints, 0, 1, 1, 1);
			// 业务插件类名label
			constraints.weightx = 100;
			// constraints.fill = GridBagConstraints.EAST;
			add(ivjpgeCondition, getlblBusinessPluginName(), constraints, 1, 1, 1, 1);

			// 总帐
			constraints.weightx = 0;
			add(ivjpgeCondition, getLableTotalAccount(), constraints, 0, 2, 1, 1);
			// 总帐参照UIRefPane 总帐系统使用的字段；
			constraints.weightx = 0;
			getUIRefAccount().setSize(new Dimension(120, 20));
			add(ivjpgeCondition, getUIRefAccount(), constraints, 1, 2, 1, 1);
			// 描述Label
			constraints.weightx = 0;
			add(ivjpgeCondition, getLabDesc(), constraints, 0, 3, 1, 1);
			// 类型描述Label
			// constraints.weightx = 100;
			add(ivjpgeCondition, getlblTypeDescriptionValue(), constraints, 1, 3, 1, 1);
			// 条件Label
			constraints.weightx = 0;
			// constraints.fill = GridBagConstraints.NONE;
			add(ivjpgeCondition, getLabRealParam(), constraints, 0, 4, 1, 1);
			// 条件Table
			constraints.weightx = 100;
			constraints.weighty = 100;
			// constraints.fill = GridBagConstraints.BOTH;
			add(ivjpgeCondition, getUITablePane1(), constraints, 0, 5, 2, 1);

		}
		return ivjpgeCondition;
	}

	/**
	 * 预警方式 面板
	 * 
	 * @return
	 */
	protected UIPanel getpgeMethod() {
		if (ivjpgeMethod == null) {
			ivjpgeMethod = new UIPanel();
			ivjpgeMethod.setName("pgeMethod");
			ivjpgeMethod.setLayout(new BorderLayout());
			ivjpgeMethod.add(getpnlProduceMethod(), BorderLayout.NORTH);
			ivjpgeMethod.add(getpnlAlartMethod(), BorderLayout.CENTER);
			// lj+ 2006-5-9 增加一个面板来配置预警消息
			ivjpgeMethod.add(getpnlMsgConfig(), BorderLayout.SOUTH);
		}
		return ivjpgeMethod;
	}

	/**
	 * 预警方式 面板
	 * 
	 * @return
	 */
	protected UIPanel getpnlAlartMethod() {
		if (ivjpnlAlartMethod == null) {
			ivjpnlAlartMethod = new UIPanel();
			ivjpnlAlartMethod.setMinimumSize(new Dimension(530, 150));
			ivjpnlAlartMethod.setName("pnlAlartMethod");
			GridBagLayout layOut = new GridBagLayout();
			ivjpnlAlartMethod.setLayout(layOut);
			GridBagConstraints constraints = new GridBagConstraints();
			Insets inset = new Insets(2, 4, 2, 4);
			constraints.insets = inset;
			constraints.weightx = 0;
			constraints.weighty = 0;
			constraints.anchor = GridBagConstraints.WEST;
			// 系统登录
			UIPanel login = new UIPanel();
			login.add(getchkAlartMethodLogin());
			login.add(getbtnLogin());
			add(ivjpnlAlartMethod, login, constraints, 0, 0, 1, 1);
			// 触发点
			UIPanel next = new UIPanel();
			next.add(getchkAlartMethodNext());// ());
			next.add(getbtnNext());// getbtnRealEmail());
			add(ivjpnlAlartMethod, next, constraints, 1, 0, 1, 1);
			// 按钮
			UIPanel button = new UIPanel();
			button.add(getchkButton());
			button.add(getbtnButton());
			add(ivjpnlAlartMethod, button, constraints, 2, 0, 1, 1);
			ivjpnlAlartMethod.setBorder(BorderFactory.createTitledBorder(NCLangRes.getInstance()
					.getStrByID("101502", "UPP101502-000291")/* @res""触发方式"" */));
		}
		return ivjpnlAlartMethod;
	}

	/**
	 * 消息配置面板
	 * 
	 * @return
	 */
	protected UIPanel getpnlMsgConfig() {
		if (ivjpnlMsgConfig == null) {
			ivjpnlMsgConfig = new UIPanel();
			ivjpnlMsgConfig.setPreferredSize(new Dimension(100, 100));
			ivjpnlMsgConfig.setLayout(new BorderLayout());

			UIPanel pnlConifg = new UIPanel();
			// 待办消息接收者
			UIPanel todo = new UIPanel();
			todo.add(getchkMsgCentral());
			todo.add(getbtnWorkitem());
			pnlConifg.add(todo);
			// 电子邮件接收者
			UIPanel email = new UIPanel();
			email.add(getchkAlartMethodRealEmail());
			email.add(getbtnRealEmail());
			pnlConifg.add(email);
			// 手机短信接收者
			UIPanel mobile = new UIPanel();
			mobile.add(getchkAlartMethodMobile());
			mobile.add(getbtnMobile());
			pnlConifg.add(mobile);
			//IM接收者
			UIPanel im = new UIPanel();
			im.add(getchkIM());
			im.add(getbtnIM());
			pnlConifg.add(im);
			
			
			// 查询方式
			UIPanel query = new UIPanel();
			query.add(getchkAlartMethodPlatform());
			query.add(getchkAlartMethodInvoke());
			query.setBorder(BorderFactory.createTitledBorder(NCLangRes.getInstance().getStrByID("101502",
					"UPP101502-000287")));

			ivjpnlMsgConfig.add(pnlConifg, "Center");
			ivjpnlMsgConfig.setBorder(BorderFactory.createTitledBorder(NCLangRes.getInstance()
					.getStrByID("101502", "UPP101502-000290")/* @res""消息接收者配置"" */));
		}
		return ivjpnlMsgConfig;
	}

	/**
	 * 产生方式 面板
	 * 
	 * @return
	 */
	protected UIPanel getpnlProduceMethod() {
		if (produceMethod == null) {
			produceMethod = new UIPanel();
			produceMethod.setName("pnlProduceMethod");
			Box box = Box.createHorizontalBox();
			box.add(Box.createGlue());
			box.add(getRdoRunInTime());
			box.add(Box.createGlue());
			box.add(Box.createHorizontalStrut(100));
			box.add(getRdoTiming());
			box.add(getBtnTiming());
			box.add(Box.createGlue());
			// user code begin {1}
			ButtonGroup bg = new ButtonGroup();
			bg.add(getRdoRunInTime());
			bg.add(getRdoTiming());
			getRdoRunInTime().setSelected(true);
			produceMethod.add(box);
			produceMethod.setBorder(BorderFactory.createTitledBorder(NCLangRes.getInstance().getStrByID(
					"101502", "UPP101502-000312")/* @res""触发策略"" */));
		}
		return produceMethod;
	}

	private UIRadioButton getrdoActive() {
		if (ivjrdoActive == null) {
			ivjrdoActive = new UIRadioButton();
			ivjrdoActive.setName("rdoActive");
			ivjrdoActive
					.setText(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000071")/* @res""激活"" */);
		}
		return ivjrdoActive;
	}

	private UIRadioButton getrdoDeActive() {
		if (ivjrdoDeActive == null) {
			ivjrdoDeActive = new UIRadioButton();
			ivjrdoDeActive.setName("rdoDeActive");
			ivjrdoDeActive
					.setText(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000072")/* @res""休眠"" */);
		}
		return ivjrdoDeActive;
	}

	protected UIRadioButton getRdoRunInTime() {
		if (ivjrdoRunInTime == null) {
			ivjrdoRunInTime = new UIRadioButton();
			ivjrdoRunInTime.setName("rdoRunInTime");
			ivjrdoRunInTime
					.setText(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000073")/* @res""即时"" */);
		}
		return ivjrdoRunInTime;
	}

	protected UIRadioButton getRdoTiming() {
		if (ivjrdoRunCycle == null) {
			ivjrdoRunCycle = new UIRadioButton();
			ivjrdoRunCycle.setName("rdoRunCycle");
			ivjrdoRunCycle.setText("");
			ivjrdoRunCycle.setPreferredSize(new Dimension(30, 22));
		}
		return ivjrdoRunCycle;
	}

	public AlertregistryVO getRegistry() {
		// 预警条件设置
		if (m_AlartType == null)
			m_AlartType = new AlerttypeVO();

		m_AlartType = getSelectedType();

		AlertregistryVO registry = new AlertregistryVO();
		// 设置预警信息
		registry.setAlertname(gettxtAlartName().getText());
		registry.setFilename(gettxtAlartMsgFileName().getText());
		registry.setEnabled(new UFBoolean(getrdoActive().isSelected()));

		if (getTfMaxLog().getText().length() > 0)
			registry.setMaxlogs(Integer.valueOf(getTfMaxLog().getText()));

		DefaultConstEnum langObj = (DefaultConstEnum) getcboLanguage().getSelectedItem();
		registry.setLanguage((String) langObj.getValue());
		registry.setMessage(gettxtMessage().getText());
		registry.setPk_corp(getCorpPK());
		if (isNecessaryAccount(m_AlartType.getBelong_system()))
			registry.setAccountpk(getUIRefAccount().getRefPK());
		// 预警时间配置
		if (m_AlartTimeConfig == null)
			m_AlartTimeConfig = new AlertTimeConfig();
		m_AlartTimeConfig.setJustInTime(getRdoRunInTime().isSelected()); // 即时产生
		// 预警方式
		if (m_AlartMethod == null)
			m_AlartMethod = new AlertMethod();
		m_AlartMethod.setEnable(AlertMethod.LOGIN, getchkAlartMethodLogin().isSelected());
		m_AlartMethod.setEnable(AlertMethod.TODO, getchkMsgCentral().isSelected());
		m_AlartMethod.setEnable(AlertMethod.MOBILE, getchkAlartMethodMobile().isSelected());
		m_AlartMethod.setEnable(AlertMethod.NEXT, getchkAlartMethodNext().isSelected());
		m_AlartMethod.setEnable(AlertMethod.PLATFORM, false);// getchkAlartMethodPlatform().isSelected()
		m_AlartMethod.setEnable(AlertMethod.INVOKE, getchkAlartMethodInvoke().isSelected());
		m_AlartMethod.setEnable(AlertMethod.BUTTON, getchkButton().isSelected());
		m_AlartMethod.setEnable(AlertMethod.REALMAIL, getchkAlartMethodRealEmail().isSelected());
		m_AlartMethod.setEnable(AlertMethod.IM,getchkIM().isSelected());
		// ///////////////////////////////////////////////////////////////////

		if (getchkAlartMethodLogin().isSelected() && m_LoginFlag) {
			if (m_loginUserVOs != null) {
				String accountename = "";
				String accountPK = "";
				for (int i = 0; i < m_loginUserVOs.length; i++) {
					accountename += m_loginUserVOs[i].getName() + ";";
					accountPK += m_loginUserVOs[i].getPk() + ";";
				}
				m_AlartMethod.setAccounts(accountPK);
				m_AlartMethod.setAccountNames(accountename);
			}
		}
		if (!(getchkAlartMethodLogin().isSelected())) {
			m_AlartMethod.setAccounts("");
			m_AlartMethod.setAccountNames("");
		}
		//
		if (getchkMsgCentral().isSelected() && m_MsgCentralFlag) {
			if (m_workitemUserVOs != null) {
				String emailName = "";
				String emailPK = "";
				for (int i = 0; i < m_workitemUserVOs.length; i++) {
					emailName += m_workitemUserVOs[i].getName() + ";";
					emailPK += m_workitemUserVOs[i].getPk() + ";";
				}
				m_AlartMethod.setTODOs(emailPK);
				m_AlartMethod.setTODONames(emailName);
			}
		}
		if (!(getchkMsgCentral().isSelected())) {
			m_AlartMethod.setTODOs("");
			m_AlartMethod.setTODONames("");
		}
		// 真正电子邮件的发送////////////////////////////////////////////////
		if (getchkAlartMethodRealEmail().isSelected() && m_EmailVOsFlag) {
			if (m_emailUserVOs != null) {
				String realEmails = "";
				String realEmailNames = "";
				for (int i = 0; i < m_emailUserVOs.length; i++) {
					realEmails += m_emailUserVOs[i].getPk() + ";";
					realEmailNames += m_emailUserVOs[i].getName() + ";";
				}
				m_AlartMethod.setRealEmails(realEmails);
				m_AlartMethod.setRealEmailNames(realEmailNames);
			}
		}
		if (!(getchkAlartMethodRealEmail().isSelected())) {
			m_AlartMethod.setRealEmails("");
			m_AlartMethod.setRealEmailNames("");
		}
		// /////////////////////////////////////////////////////////////////
		if (getchkAlartMethodMobile().isSelected() && m_MobileVOsFlag) {
			if (m_mobileUserVOs != null) {
				String mobileName = "";
				String mobilePK = "";
				for (int i = 0; i < m_mobileUserVOs.length; i++) {
					mobileName += m_mobileUserVOs[i].getName() + ";";
					mobilePK += m_mobileUserVOs[i].getPk() + ";";
				}
				m_AlartMethod.setMobiles(mobilePK);
				m_AlartMethod.setMobileNames(mobileName);
			}
		}
		if (!(getchkAlartMethodMobile().isSelected())) {
			m_AlartMethod.setMobiles("");
			m_AlartMethod.setMobileNames("");
		}
		// ///////////////////////////////////////////////////////////////////
		if(getchkIM().isSelected()&&m_MsgIMFlag){
			if(m_imUserVOs!=null){
				String imName ="";
				String imPK="";
				for(int i=0;i<m_imUserVOs.length;i++){
					imName+=m_imUserVOs[i].getName()+";";
					imPK+=m_imUserVOs[i].getPk()+";";
				}
				m_AlartMethod.setM_IMTODOs(imPK);
				m_AlartMethod.setM_IMTODOsNames(imName);
			}
		}
		
		if(!getchkIM().isSelected()){
			m_AlartMethod.setM_IMTODOs("");
			m_AlartMethod.setM_IMTODOsNames("");
		}
		
		// ///////////////////////////////////////////////////////////////////
		if (getchkAlartMethodNext().isSelected() && m_EnterFlag) {
			funcregistervo2Alertmethod();
		}
		if (!(getchkAlartMethodNext().isSelected())) {
			m_AlartMethod.setNexts("");
			m_AlartMethod.setNextsAttribute("");
		}
		// 按钮预警
		if (getchkButton().isSelected() && m_BtnFlag) {
			if (m_AlartBtnVOs != null) {
				String btnName = "";
				String btnID = "";
				for (int i = 0; i < m_AlartBtnVOs.length; i++) {
					btnName += m_AlartBtnVOs[i].getFunName() + ";";
					btnID += m_AlartBtnVOs[i].getPrimaryKey() + ";";
				}
				m_AlartMethod.setButtons(btnID);
				m_AlartMethod.setButtonsNames(btnName);
			}
		}
		if (!(getchkButton().isSelected())) {
			m_AlartMethod.setButtons("");
			m_AlartMethod.setButtonsNames("");
		}

		if (!isAdd) {
			registry.setPrimaryKey(m_registry.getPrimaryKey());
		}
		registry.setAlertMethod(m_AlartMethod);
		registry.setTimeConfig(m_AlartTimeConfig);
		registry.setAlertTypeVo(m_AlartType);
		registry.setPk_alerttype(m_selectedType.getPrimaryKey());
		registry.setIstiming(UFBoolean.valueOf(!m_AlartTimeConfig.isJustInTime()));
		registry.key2TypeVO(m_keys);

		m_registry = registry;
		return registry;
	}

	public AlertregistryVO getCompletedRegitry() {
		return m_registry;
	}

	public AlerttypeVO getSelectedType() {
		return m_selectedType;
	}

	private UITable getTable() {
		return getUITablePane1().getTable();
	}

	protected void setRefPKs(int row, String refPKs) {
		if (refPKMap == null)
			refPKMap = new HashMap<Integer, String>();
		refPKMap.put(row, refPKs);
	}

	/**
	 * 阈值表格列名
	 * 
	 * @return
	 */
	protected String[] getColumnNames() {
		String columns[] = {
				NCLangRes.getInstance().getStrByID("101502", "UPP101502-000105")/* 阈值名称 */,
				NCLangRes.getInstance().getStrByID("101502", "UPP101502-000103")/* 阈值描述 */,
				NCLangRes.getInstance().getStrByID("101502", "UPP101502-000088")/* @res""操作符"" */,
				NCLangRes.getInstance().getStrByID("101502", "UPP101502-000076") /* @res""阈值设置"" */
		};
		return columns;
	}

	/**
	 * 阈值条件表Model.
	 */
	public PaRegistryTableModel getTableModel() {
		if (registryTableModel == null) {
			registryTableModel = new PaRegistryTableModel();
		}
		return registryTableModel;
	}

	private TriggerButtonDlg newTriggerBtnDlg() {
		FuncRegisterVO[] configedBtnVOs = null;
		if (m_AlartMethod != null) {
			String[] elementPK = m_AlartMethod.getButtons();
			String[] elementName = m_AlartMethod.getButtonNames();
			if (elementPK != null) {
				configedBtnVOs = new FuncRegisterVO[elementPK.length];
				for (int i = 0; i < elementPK.length; i++) {
					FuncRegisterVO tempVO = new FuncRegisterVO();
					tempVO.setFunCode(elementPK[i]);
					tempVO.setFunName(elementName[i]);
					tempVO.setPrimaryKey(elementPK[i]);
					tempVO.setClassName("FuncRegisterVO");
					configedBtnVOs[i] = tempVO;
				}
			}
		}
		String moduleName = m_selectedType == null ? null : m_selectedType.getBelong_system();
		TriggerButtonDlg dlg = new TriggerButtonDlg(this, moduleName, configedBtnVOs);
		return dlg;
	}

	private TriggerFunctionDlg getTriggerFuncDlg() {
		if (triggerFuncDlg == null) {
			FuncRegisterVO[] initFuncRegVOs = alartmethod2Funcregistervos();
			triggerFuncDlg = new TriggerFunctionDlg(this, initFuncRegVOs);
		}
		return triggerFuncDlg;
	}

	private UITextField gettxtAlartMsgFileName() {
		if (ivjtxtAlartMsgFileName == null) {
			ivjtxtAlartMsgFileName = new UITextField(50);
			ivjtxtAlartMsgFileName.setMaxLength(50);
			ivjtxtAlartMsgFileName.setName("txtAlartMsgFileName");
			ivjtxtAlartMsgFileName.setMinimumSize(new Dimension(250, 20));
		}
		return ivjtxtAlartMsgFileName;
	}

	/**
	 * 文件名称输入框.
	 * 
	 * @return
	 */
	protected Box gettxtAlartMsgPanel() {
		if (m_fileBox == null) {
			m_fileBox = Box.createHorizontalBox();
			m_fileBox.add(gettxtAlartMsgFileName());
			m_fileBox.add(getDescLabel());
		}
		return m_fileBox;
	}

	protected UILabel getDescLabel() {
		if (m_descLabel == null)
			m_descLabel = new UILabel(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000272")/* 英文字母 */);
		return m_descLabel;
	}

	private UITextField gettxtAlartName() {
		if (ivjtxtAlartName == null) {
			ivjtxtAlartName = new UITextField(40);
			ivjtxtAlartName.setMaxLength(40);
			ivjtxtAlartName.setName("txtAlartName");
			ivjtxtAlartName.setMinimumSize(new Dimension(250, 20));
		}
		return ivjtxtAlartName;
	}

	private UITextArea gettxtMessage() {
		if (txtAreaMessage == null) {
			txtAreaMessage = new UITextArea();
			txtAreaMessage.setMaxLength(128);
			txtAreaMessage.setName("txtMessage");
		}
		return txtAreaMessage;
	}

	private UIRefPane getUIRefAccount() {
		if (accountRefPane == null) {
			accountRefPane = new UIRefPane();
			// accountRefPane.setRefNodeName("主体账簿");
			// accountRefPane.getRefModel().setUseDataPower(false);
		}
		return accountRefPane;
	}

	private UIScrollPane getSpMessage() {
		if (m_spMessage == null) {
			m_spMessage = new UIScrollPane();
			m_spMessage.setName("UIScrollPane1");
			m_spMessage.setViewportView(gettxtMessage());
		}
		return m_spMessage;
	}

	private UITablePane getUITablePane1() {
		if (ivjUITablePane1 == null) {
			ivjUITablePane1 = new UITablePane();
			ivjUITablePane1.setName("UITablePane1");
			ivjUITablePane1.getTable().setModel(getTableModel());
		}
		return ivjUITablePane1;
	}

	// 有效的预警条目键值
	private AlerttypeBVO[] getValidKeys() {
		if (getKeys() == null)
			return null;

		ArrayList<AlerttypeBVO> al = new ArrayList<AlerttypeBVO>();
		for (int i = 0; i < getKeys().length; i++) {
			// 为了排除查询引擎的行引起的空行问题
			if (getKeys()[i].getFieldname().equals("QryID"))
				continue;
			al.add(getKeys()[i]);
		}
		return al.toArray(new AlerttypeBVO[al.size()]);
	}

	private void initConnections() {
		getBtnTiming().addActionListener(this);
		getRdoRunInTime().addActionListener(this);
		getRdoTiming().addActionListener(this);
		getchkAlartMethodLogin().addActionListener(this);
		getchkMsgCentral().addActionListener(this);
		getchkAlartMethodNext().addActionListener(this);
		getbtnOK().addActionListener(this);
		getbtnCancel().addActionListener(this);
		getcboTypeName().addActionListener(this);
		getbtnNext().addActionListener(this);
		getbtnLogin().addActionListener(this);
		getbtnWorkitem().addActionListener(this);
		getchkButton().addActionListener(this);
		getbtnButton().addActionListener(this);
		getchkAlartMethodMobile().addActionListener(this);
		getbtnMobile().addActionListener(this);
		// 真正电子邮件按钮
		getbtnRealEmail().addActionListener(this);
		getchkAlartMethodRealEmail().addActionListener(this);
		getbtnIM().addActionListener(this);
		getchkIM().addActionListener(this);
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-10-25 20:01:18)
	 */
	private void initData() {
		m_Editor.addItem("Y");
		m_Editor.addItem("N");
		// 获得全部基础档案vos
		m_baseDocVOs = nc.vo.pf.change.PfUtilBaseTools.getAllBasedocVO();
		if (m_baseDocVOs == null) {
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("101502",
					"UPP101502-000079")/* @res""提示"" */, NCLangRes.getInstance().getStrByID("101502",
					"UPP101502-000080")/* @res""基础档案类型数据错误"" */);
		} else {
			for (int i = 0; i < m_baseDocVOs.length; i++) {
				m_ht.put(m_baseDocVOs[i].getDocName(), m_baseDocVOs[i]);
			}
		}
		// 默认情况下总帐参照不可用。
		getUIRefAccount().setEnabled(false);
	}

	private void initialize() {
		setName("ItemSetupDlg");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(550, 370);
		setResizable(true);
		setTitle(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000081")/* @res""预警条目设置"" */);
		setContentPane(getJDialogContentPane());
		initConnections();
		initStates();
		initData();
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-10-29 8:55:29)
	 */
	private void initRgsDisplayData(AlerttypeVO at) {
		setKeys(at.getAlertVariables());
		try {
			for (int i = 0; i < getcboTypeName().getItemCount(); i++) {
				String pk = at.getPrimaryKey();
				if (((AlarmTypeConst) getcboTypeName().getItemAt(i)).getValue().equals(pk))
					getcboTypeName().setSelectedIndex(i);
			}
			// 增加对放开FA(财务)放开,主体账簿
			if (isNecessaryAccount(at.getBelong_system())) {
				getUIRefAccount().setEnabled(true);
			} else {
				getUIRefAccount().setText("");
				getUIRefAccount().setEnabled(false);
			}

			setDescriptionTxt(at);

			getlblBusinessPluginName().setText(at.getBusi_plugin());
			getTable().updateUI();
			getTable().validate();
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	private void initStates() {
		// 如果是查询引擎的插件，就不能编辑预警类型!
		if (m_AlartType != null
				&& m_AlartType.getBusi_plugin().equals("nc.bs.pub.querymodel.pa.QueryEnginePlugin"))
			getcboTypeName().setEnabled(false);
		if (getRdoTiming().isSelected()) {
			onSelectedTiming();
		} else {
			// onSelectInTime();
			if (m_registry != null && m_registry.getAlertMethod() != null) {
				getchkAlartMethodPlatform().setSelected(
						m_registry.getAlertMethod().getEnable(AlertMethod.PLATFORM));
				getchkAlartMethodInvoke().setEnabled(m_registry.getIstiming().booleanValue());
			} else {
				getchkAlartMethodPlatform().setSelected(true);
				getchkAlartMethodInvoke().setEnabled(false);
			}
		}
	}

	public boolean isAdd() {
		return isAdd;
	}

	/**
	 * 判定是否必须设置主体账簿。
	 */
	private boolean isNecessaryAccount(String modulename) {
		return modulename.equalsIgnoreCase(MODULE_GL) || modulename.equalsIgnoreCase(MODULE_FA);
	}

	/**
	 * Comment
	 */
	public void onClickLogin() {
		// 系统登录配置按钮按下
		m_LoginFlag = true;
		// ///////////////////////////////
		String[] userIds = null;
		if (m_AlartMethod != null) {
			userIds = m_AlartMethod.getAcounts();
		}
		// huangzg++;
		ChooseUserDlg userChoosedlg = getChooserUserDlg(userIds);
		userChoosedlg.getTree2ListPnl().resetListModel(m_loginUserVOs, null);
		userChoosedlg.setModal(true);
		userChoosedlg
				.setTitle(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000049")/* @res""系统登录选择"" */);
		userChoosedlg.setLocationRelativeTo(this);
		userChoosedlg.setVisible(true);
		if (userChoosedlg.getResult() == UIDialog.ID_OK) {
			m_loginUserVOs = userChoosedlg.getResultVOs();
		}
		return;
	}

	/**
	 * Comment
	 */

	public void onClickMsgCentral(ActionEvent actionEvent) {
		// 消息中心配置按钮按下
		m_MsgCentralFlag = true;
		String[] userIds = null;
		if (m_AlartMethod != null) {
			userIds = m_AlartMethod.getToDOUserIds();
		}
		// huangzg++.2006-4-18
		ChooseUserDlg dlg = getMsgCentralChooseUserDlg(userIds);
		OrganizeUnit[] oldOrgUnits = dlg.getTree2ListPnl().getParaVo().getSelectedUservos();
		dlg.getTree2ListPnl().resetListModel(oldOrgUnits, null);
		dlg.setVisible(true);

		if (dlg.getResult() == UIDialog.ID_OK) {
			m_workitemUserVOs = dlg.getResultVOs();
			dlg.getTree2ListPnl().getParaVo().setSelectedRoleUservos(dlg.getResultVOs());
		} else
			dlg.getTree2ListPnl().getParaVo().setSelectedRoleUservos(oldOrgUnits);
		return;
	}
	
	public void onClickIMCentral(ActionEvent actionEvent) {
		// IM配置按钮按下
		m_MsgIMFlag = true;
		String[] userIds = null;
		if (m_AlartMethod != null) {
			userIds = m_AlartMethod.getIM_TODOUserIds();
		}
		ChooseUserDlg dlg =getIMChooseUserDlg(userIds);
		OrganizeUnit[] oldOrgUnits = dlg.getTree2ListPnl().getParaVo().getSelectedUservos();
		dlg.getTree2ListPnl().resetListModel(oldOrgUnits, null);
		dlg.setVisible(true);

		if (dlg.getResult() == UIDialog.ID_OK) {
			m_imUserVOs = dlg.getResultVOs();
			dlg.getTree2ListPnl().getParaVo().setSelectedRoleUservos(dlg.getResultVOs());
		} else
			dlg.getTree2ListPnl().getParaVo().setSelectedRoleUservos(oldOrgUnits);
		return;
	}

	/**
	 */
	public void onClickNext(ActionEvent actionEvent) {
		// 触发点配置按钮按下
		m_EnterFlag = true;
		TriggerFunctionDlg dlg = getTriggerFuncDlg();
		dlg.resetListModel(alartmethod2Funcregistervos());
		dlg.setLocationRelativeTo(this);
		dlg.setModal(true);
		dlg.setVisible(true);
		if (dlg.getResult() == UIDialog.ID_OK) {
			m_AlartEnterVOs = dlg.getCUserPowerVOS();
			funcregistervo2Alertmethod();
		}
		return;
	}

	private void onClickTiming(ActionEvent arg1) {
		this.btnAlartTime_ActionPerformed(arg1);
	}

	private void onClickWorkItem(ActionEvent arg1) {
		this.onClickMsgCentral(arg1);
	}

	private void onRealEmailButtonClick(ActionEvent actionEvent) {
		m_EmailVOsFlag = true;
		// ///////////////////////////////
		// OrganizeUnit[] userVos = null;
		if (m_AlartMethod != null) {
			String[] accountPK = m_AlartMethod.getRealEmailsArray();
			String[] accountNames = m_AlartMethod.getRealEmailNamesArray();
			if (accountPK != null && accountPK.length > 0) {
				m_emailUserVOs = new OrganizeUnit[accountPK.length];
				for (int i = 0; i < accountPK.length; i++) {
					m_emailUserVOs[i] = new OrganizeUnit();
					m_emailUserVOs[i].setOrgUnitType(OrganizeUnitTypes.Operator_INT);
					m_emailUserVOs[i].setPk(accountPK[i]);
					m_emailUserVOs[i].setCode(accountPK[i]);
					m_emailUserVOs[i].setName(accountNames[i]);
				}
			}
		}
		//
		EmailSelectionDlg dlg = new EmailSelectionDlg(this, m_emailUserVOs);
		dlg.setLocationRelativeTo(this);
		dlg.setModal(true);
		dlg.setTitle(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000279")/* 电子邮件地址选择 */);
		dlg.setVisible(true);
		if (dlg.getResult() == UIDialog.ID_OK) {
			m_emailUserVOs = dlg.getSelectedEmailAddresses();
			if (m_emailUserVOs != null && m_emailUserVOs.length > 0) {
				if (m_AlartMethod == null)
					m_AlartMethod = new AlertMethod();
				String userEmails = "";
				String userNames = "";
				for (int i = 0; i < m_emailUserVOs.length; i++) {
					userEmails += m_emailUserVOs[i].getPk() + ";";
					userNames += m_emailUserVOs[i].getName() + ";";
				}
				m_AlartMethod.setRealEmails(userEmails);
				m_AlartMethod.setRealEmailNames(userNames);
			}
		}
		return;
	}

	// 真正电子邮件复选框
	private void onRealEmailChkChick(ActionEvent actionEvent) {
		getbtnRealEmail().setEnabled(getchkAlartMethodRealEmail().isSelected());
	}

	private void onSelectButton() {
		getbtnButton().setEnabled(getchkButton().isSelected());
	}

	private void onSelectedTiming() {
		getBtnTiming().setEnabled(getRdoTiming().isSelected());
		setEnableByMethod(false);
	}

	private void onSelectInTime() {
		getBtnTiming().setEnabled(getRdoTiming().isSelected());
		setEnableByMethod(true);
	}

	private void onSelectLogin() {
		getbtnLogin().setEnabled(getchkAlartMethodLogin().isSelected());
	}

	private void onSelectNext() {
		getbtnNext().setEnabled(getchkAlartMethodNext().isSelected());
	}

	private void onTypeItemChanged(ActionEvent arg1) {
		this.cboTypeName_ActionPerformed(arg1);
	}

	public OrganizeUnit[] readChoosedUser(String[] userid) {
		try {
			if (userid == null)
				return null;
			String ids[] = userid;
			// 根据用户id读取用户的VO对象
			OrganizeUnit[] chooseduservos = new OrganizeUnit[ids.length];
			// yzy 2006-1-10 使用用户管理提供的查询服务替换自己的查询服务
			IUserManageQuery umq = (IUserManageQuery) NCLocator.getInstance().lookup(
					IUserManageQuery.class.getName());
			UserVO[] userVOs = umq.findNamesByPrimaryKeys(userid);
			if (userVOs != null && userVOs.length > 0) {
				for (int i = 0; i < userVOs.length; i++) {
					chooseduservos[i] = new OrganizeUnit(userVOs[i]);
				}
			}
			Arrays.sort(chooseduservos, new OrganizeUnitComparator());
			return chooseduservos;
		} catch (Exception e) {
			Logger.error("根据用户ID查出用户名称出错" + e);
			return null;
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-11-4 10:04:07)
	 * 
	 * @param newAdd
	 *            boolean
	 */
	public void setAdd(boolean newAdd) {
		isAdd = newAdd;
	}

	/**
	 */
	public void setAlartTimeConfig(nc.vo.pub.pa.AlertTimeConfig newM_AlartTimeConfig) {
		m_AlartTimeConfig = newM_AlartTimeConfig;
	}

	public void setAllComponentEnabled(Container container, boolean bool) {
		Component[] components = container.getComponents();
		for (int i = 0; i < components.length; i++) {
			components[i].setEnabled(bool);
			if (components[i] instanceof Container) {
				setAllComponentEnabled((Container) components[i], bool);
			}
		}
	}

	public void setComponentDisabled() {
		setAllComponentEnabled(getRegistryTabPane(), false);
		setAllComponentEnabled(getUITablePane1(), true);
		if (getUITablePane1().getViewport() != null)
			getUITablePane1().getViewport().getView().setEnabled(false);
	}

	private void setConditionUI(AlerttypeVO at) {
		getlblBusinessPluginName().setText(at.getBusi_plugin());
		changeAccout(at.getBelong_system());
		getUIRefAccount().setEnabled(isNecessaryAccount(at.getBelong_system()));
		setDescriptionTxt(at);
	}

	/**
	 */
	public void setCorpPK(java.lang.String newM_corpPK) {
		m_corpPK = newM_corpPK;
	}

	/**
	 */
	public void setDataSource(java.lang.String newM_dataSource) {
		m_dataSource = newM_dataSource;
	}

	private void setDescriptionTxt(AlerttypeVO at) {
		if (at.getDescrip_resid() != null)
			getlblTypeDescriptionValue().setText(
					NCLangRes.getInstance().getStrByID("prealerttype", at.getDescrip_resid().trim()));
		else
			getlblTypeDescriptionValue().setText(at.getDescription());
	}

	/**
	 * 根据定时和即时分别设置不同的状态
	 * 
	 * @param isInTime
	 *            是否为即时
	 */
	private void setEnableByMethod(boolean isInTime) {
		getchkAlartMethodLogin().setEnabled(isInTime);
		getchkAlartMethodNext().setEnabled(isInTime);
		getchkButton().setEnabled(isInTime);
		if (isInTime) {
			getchkAlartMethodLogin().setSelected(selections[0]);
			getchkAlartMethodNext().setSelected(selections[1]);
			getchkButton().setSelected(selections[2]);
			onSelectLogin();
			onSelectNext();
			onSelectButton();
			getchkAlartMethodPlatform().setSelected(selections[3]);
			getchkAlartMethodPlatform().setEnabled(true);

			getchkAlartMethodInvoke().setSelected(false);
			getchkAlartMethodInvoke().setEnabled(false);

		} else {
			selections = new boolean[] { getchkAlartMethodLogin().isSelected(),
					getchkAlartMethodNext().isSelected(), getchkButton().isSelected(),
					getchkAlartMethodPlatform().isSelected() };
			getchkAlartMethodLogin().setSelected(isInTime);
			getchkAlartMethodNext().setSelected(isInTime);
			getchkButton().setSelected(isInTime);
			getbtnLogin().setEnabled(isInTime);
			getbtnNext().setEnabled(isInTime);
			getbtnButton().setEnabled(isInTime);

			getchkAlartMethodPlatform().setSelected(false);
			getchkAlartMethodPlatform().setEnabled(false);
			getchkAlartMethodInvoke().setSelected(true);
			getchkAlartMethodInvoke().setEnabled(false);
		}
	}

	/**
	 * @param m_keys
	 *            The m_keys to set.
	 */
	void setKeys(AlerttypeBVO[] m_keys) {
		this.m_keys = m_keys;
	}

	public void setRegistry(AlertregistryVO registry) {
		if (registry == null)
			return;
		m_registry = registry;
		// 设置预警信息
		// AlartInfo ai = registry.getAlartInfo();
		gettxtAlartName().setText(registry.getAlertname());
		gettxtAlartMsgFileName().setText(registry.getFilename());
		if (registry.getEnabled().booleanValue()) {
			getrdoActive().setSelected(true);
		} else {
			getrdoDeActive().setSelected(true);
		}
		getcboLanguage().setSelectedIndex(0);

		getTfMaxLog().setText(registry.getMaxlogs().toString());
		for (int i = 0; i < getcboLanguage().getItemCount(); i++) {
			DefaultConstEnum langConst = (DefaultConstEnum) getcboLanguage().getItemAt(i);
			if (((String) langConst.getValue()).equals(registry.getLanguage())) {
				getcboLanguage().setSelectedIndex(i);
				break;
			}
		}
		gettxtMessage().setText(registry.getMessage());
		// gettxtModule().setText(ai.getModule());
		// 预警时间配置
		AlertTimeConfig atc = registry.getTimeConfig();
		getRdoTiming().setSelected(!atc.isJustInTime());
		getBtnTiming().setEnabled(getRdoTiming().isSelected());
		m_AlartTimeConfig = atc; // 设置到成员变量中
		// 预警方式
		AlertMethod am = registry.getAlertMethod();
		if (am != null) {
			getchkAlartMethodLogin().setSelected(am.getEnable(AlertMethod.LOGIN));
			getchkMsgCentral().setSelected(am.getEnable(AlertMethod.TODO));
			getchkAlartMethodMobile().setSelected(am.getEnable(AlertMethod.MOBILE));
			getchkAlartMethodNext().setSelected(am.getEnable(AlertMethod.NEXT));
			getchkAlartMethodPlatform().setSelected(am.getEnable(AlertMethod.PLATFORM));
			getchkAlartMethodInvoke().setSelected(am.getEnable(AlertMethod.INVOKE));
			getchkButton().setSelected(am.getEnable(AlertMethod.BUTTON));
			getchkAlartMethodRealEmail().setSelected(am.getEnable(AlertMethod.REALMAIL));
			getchkIM().setSelected(am.getEnable(AlertMethod.IM));
			m_AlartMethod = am; // 设置到成员变量中
		}
		getbtnLogin().setEnabled(getchkAlartMethodLogin().isSelected());
		getbtnWorkitem().setEnabled(getchkMsgCentral().isSelected());
		getbtnMobile().setEnabled(getchkAlartMethodMobile().isSelected());
		getbtnNext().setEnabled(getchkAlartMethodNext().isSelected());
		getbtnButton().setEnabled(getchkButton().isSelected());
		getbtnRealEmail().setEnabled(getchkAlartMethodRealEmail().isSelected());
		getbtnIM().setEnabled(getchkIM().isSelected());
		// 预警条件设置
		m_AlartType = registry.getAlertTypeVo();
		if (registry.getAccountpk() != null && isNecessaryAccount(m_AlartType.getBelong_system())) {
			if (getAccountType(m_AlartType.getBelong_system()) == 2) {
				getUIRefAccount().setRefNodeName("固定资产核算账簿");
			} else
				getUIRefAccount().setRefNodeName("主体账簿");
			getUIRefAccount().setPK(registry.getAccountpk());
		} else
			getUIRefAccount().setRefNodeName("主体账簿");
		// 预警条目的显示
		initRgsDisplayData(m_AlartType);
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-10-25 19:38:20)
	 * 
	 * @param newM_selectedType
	 *            nc.vo.pub.pa.AlartType
	 */
	public void setSelectedType(AlerttypeVO newM_selectedType) {
		m_selectedType = newM_selectedType;
		m_selectedType.setAlertVariables(newM_selectedType.getAlertVariables());

		isQePreAlert = m_selectedType.getBusi_plugin() != null
				&& m_selectedType.getBusi_plugin().equals("nc.bs.pub.querymodel.pa.QueryEnginePlugin");
	}

	public int showModal() {
		initStates();
		return super.showModal();
	}

	/**
	 * Invoked when the target of the listener has changed its state.
	 * 
	 * @param e
	 *            a ChangeEvent object
	 */
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == getRegistryTabPane()) {
			// AlartType at = getRegistry().getType();
			// setSelectedType(at);
		}
		if (getTable().isEditing())
			getTable().editingStopped(new ChangeEvent(getTable()));
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-12-6 8:52:37)
	 * 
	 * @return java.lang.String
	 */
	public String toString() {
		java.util.Hashtable ht = new java.util.Hashtable();
		Class cls = getClass();
		java.lang.reflect.Field[] f = cls.getDeclaredFields();
		try {
			java.lang.reflect.AccessibleObject.setAccessible(f, true);
			for (int i = 0; i < f.length; i++) {
				ht.put(f[i].getName(), f[i].get(this));
			}
		} catch (Exception e) {
		}
		if (cls.getSuperclass().getSuperclass() != null) {
			ht.put("super", super.toString());
		}
		return cls.getName() + ht;
	}

	/**
	 * @return
	 */
	public boolean validateInput() {
		String checkStr = checkOK();

		if (checkStr != null && checkStr.length() > 0) {
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("101502",
					"UPP101502-000034")/* @res""错误提示"" */, checkStr);
			return false;
		}
		return true;
	}

	protected String checkOK() {
		StringBuffer strBuffer = new StringBuffer();
		// 条目名称
		strBuffer.append(validateName());

		// 消息文件名 或条目描述
		strBuffer.append(validateDesc());

		// 是否选择预警类型
		AlarmTypeConst combType = (AlarmTypeConst) getcboTypeName().getSelectedItem();
		if (combType == null || combType.getValue().equals(EMPTY)) {
			strBuffer.append(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000074")/* @res""预警类型为空，请先选择！"" */
					+ "\n");
		}

		// @guowl+, 校验阈值
		strBuffer.append(validateRealParams());

		// 定时被选中，但没有选择时间配置
		if (getRdoTiming().isSelected() && m_AlartTimeConfig == null) {
			strBuffer.append(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000085")/* @res""定时被选中，但没有选择时间配置"" */
					+ "\n");
		}
		// 当所属系统为gl或fa时必须选择主体帐簿。
		AlerttypeVO at = getSelectedType();
		if (at != null && at.getBelong_system() != null && isNecessaryAccount(at.getBelong_system())) {
			if (getUIRefAccount().getRefPK() == null || getUIRefAccount().getRefPK().length() == 0) {
				strBuffer.append(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000271")/* @res""总账或财务类型的预警，必须选择主体账簿！"" */
						+ "\n");
			}
		}
		return strBuffer.toString();
	}

	protected String validateRealParams() {
		// 如果是定时的自定义查询预警，不允许出现宏变量
		boolean isQePrealert = false;
		AlerttypeVO at = getSelectedType();
		if (at != null && at.getBusi_plugin().equals("nc.bs.pub.querymodel.pa.QueryEnginePlugin")
				&& getRdoTiming().isSelected()) {
			isQePrealert = true;
			if (hasMacroVar)
				return NCLangRes.getInstance().getStrByID("101502", "UPP101502-000286")/* @res"定时的自定义查询预警中不允许包含宏变量！" */
						+ "\n";
		}
		for (int i = 0; i < getTableModel().getRowCount(); i++) {
			Object value = getTableModel().getValueAt(i, 3);
			// 校验阈值是否能为空
			if ((value == null || StringUtil.isEmptyWithTrim(value.toString()))
					&& getKeys()[i].getNotnullable() != null && getKeys()[i].getNotnullable().booleanValue())
				return NCLangRes.getInstance().getStrByID("101502", "UPP101502-000298", null,
						new String[] { getKeys()[i].getFieldname() })/* @res"参数'{0}'的值不能为空！" */
						+ "\n";

			if (value == null)
				continue;
			String strValue = value.toString();
			if (strValue.length() > 512)
				return NCLangRes.getInstance().getStrByID("101502", "UPP101502-000288")/* @res"预警的'阈值设置'或后台任务的'参数取值'长度超过限制！" */
						+ "\n";
			if (refPKMap != null) {
				String refPKs = refPKMap.get(i);
				if (refPKs != null && refPKs.length() > 1024)
					return NCLangRes.getInstance().getStrByID("101502", "UPP101502-000288")/* @res"预警的'阈值设置'或后台任务的'参数取值'长度超过限制！" */
							+ "\n";
			}

			if (isQePrealert && strValue.startsWith("#") && strValue.endsWith("#"))
				return NCLangRes.getInstance().getStrByID("101502", "UPP101502-000286")/* @res"定时的自定义查询预警中不允许包含宏变量！" */
						+ "\n";

		}
		return "";
	}

	/**
	 * 校验消息文件名 或条目描述
	 * 
	 * @return
	 */
	protected String validateDesc() {
		String alartMsgFileName = gettxtAlartMsgFileName().getText();
		if (StringUtil.isEmptyWithTrim(alartMsgFileName)) {
			return NCLangRes.getInstance().getStrByID("101502", "UPP101502-000285")/* @res""请输入非空的预警信息文件名"" */
					+ "\n";
		} else {
			String patternString = "[0-9a-zA-Z._]+";
			Pattern pattern = Pattern.compile(patternString);
			Matcher matcher = null;
			if (alartMsgFileName != null)
				matcher = pattern.matcher(alartMsgFileName);
			if (!matcher.matches()) { return NCLangRes.getInstance().getStrByID("101502",
					"UPP101502-000273")/* @res"必须输入英文字母组成的文件名!" */; }
		}
		return "";
	}

	/**
	 * 校验条目名称
	 * 
	 * @return
	 */
	protected String validateName() {
		String alartName = gettxtAlartName().getText();
		if (StringUtil.isEmptyWithTrim(alartName))
			return NCLangRes.getInstance().getStrByID("101502", "UPP101502-000082")/* @res""请输入非空的预警名称"" */
					+ "\n";
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getBtnTiming())
			onClickTiming(e);
		else if (e.getSource() == getRdoRunInTime())
			onSelectInTime();
		else if (e.getSource() == getRdoTiming())
			onSelectedTiming();
		else if (e.getSource() == getchkAlartMethodLogin())
			onSelectLogin();
		else if (e.getSource() == getchkMsgCentral())
			chkAlartMethodEmail_ActionPerformed(e);
		else if (e.getSource() == getchkAlartMethodNext())
			onSelectNext();
		else if (e.getSource() == getchkAlartMethodRealEmail())
			onRealEmailChkChick(e);
		else if (e.getSource() == getbtnOK())
			onClickOK(e);
		else if (e.getSource() == getbtnCancel())
			onClickCancel(e);
		else if (e.getSource() == getcboTypeName())
			onTypeItemChanged(e);
		else if (e.getSource() == getbtnNext())
			onClickNext(e);
		else if (e.getSource() == getbtnLogin())
			onClickLogin();
		else if (e.getSource() == getbtnWorkitem())
			onClickWorkItem(e);
		else if (e.getSource() == getchkButton())
			onSelectButton();
		else if (e.getSource() == getbtnButton())
			onClickButton();
		else if (e.getSource() == getchkAlartMethodMobile())
			chkAlartMethodMobile_ActionPerformed1(e);
		else if (e.getSource() == getbtnMobile())
			btnMobile_ActionPerformed1(e);
		else if (e.getSource() == getbtnRealEmail())
			onRealEmailButtonClick(e);
		else if(e.getSource()==getbtnIM()){
			onClickIMCentral(e);
		}else if(e.getSource()==getchkIM()){
			chkIM_ActionPerformed(e);
		}
		
	}

	class ComboxValue {
		String displayName = "";

		String value = null;

		UFBoolean isDefault = UFBoolean.FALSE;

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public UFBoolean getIsDefault() {
			return isDefault;
		}

		public void setIsDefault(UFBoolean isDefault) {
			this.isDefault = isDefault;
		}

		public String toString() {
			return displayName;
		}

	}

	class PaRegistryTableModel extends AbstractTableModel {
		UIRefPane m_ref = new UIRefPane();

		public int getColumnCount() {
			return getColumnNames().length;
		}

		public String getColumnName(int column) {
			return getColumnNames()[column];
		}

		public int getRowCount() {
			int rowCount = 0;
			if (getValidKeys() != null) {
				for (int i = 0; i < getValidKeys().length; i++) {
					if (!getValidKeys()[i].getFieldname().equals("QryID"))
						rowCount++;
				}
			}
			return rowCount;
		}

		public Object getValueAt(int row, int col) {
			if (getValidKeys()[row] == null || getValidKeys()[row].getVariablename() == null
					&& getValidKeys()[row].getVartype() == null)
				return null;
			switch (col) {
				case 0:
					// 新加列直接改成如此
					return getValidKeys()[row].getFieldname();
				case 1:
					String disid = getValidKeys()[row].getDisplaynameid();
					return disid != null ? NCLangRes.getInstance().getStrByID("prealerttype", disid)
							: getValidKeys()[row].getVariablename();
				case 2:
					if (isQePreAlert) {//如果是查询引擎预警					
						return getValidKeys()[row].getRefervalue();
					} else
						return "=";
				case 3:
					if (getValidKeys()[row].getVartype().equals(
							m_editTypeConst.REFERENCE.getValue().toString())) {
						return getValidKeys()[row].getRefervalue();
					} else if (!isQePreAlert
							&& getValidKeys()[row].getVartype().equals(
									m_editTypeConst.COMBOBOX.getValue().toString())) {
						return getCmbDispalyName(getValidKeys()[row]);
					} else if (getValidKeys()[row].getVartype().equals(
							m_editTypeConst.DOUBLE.getValue().toString())) {
						if (getValidKeys()[row].getValue() != null
								&& getValidKeys()[row].getValue().toString().trim().length() > 0) {
							// 2006-10-17 // 解决科学计数法表示的问题
							UFDouble value = new UFDouble(getValidKeys()[row].getValue().toString());
							return value;

						} else {
							getValidKeys()[row].getValue();
						}
					} else if (getValidKeys()[row].getVartype().equals(
							m_editTypeConst.BOOLEAN.getValue().toString())) {
						if (getValidKeys()[row].getValue() != null
								&& getValidKeys()[row].getValue() instanceof Boolean)
							return ((Boolean) getValidKeys()[row].getValue()).booleanValue() ? "Y" : "N";
						return getValidKeys()[row].getValue();
					} else
						return getValidKeys()[row].getValue();
			}
			return null;
		}

		private Object getCmbDispalyName(AlerttypeBVO alerttypeBVO) {
			String displayName = alerttypeBVO.getRefervalue();
			String strRefname = alerttypeBVO.getRefername();
			ComboxValue[] cmbItems = refname_Cmbitems_Map.get(strRefname);
			if (cmbItems == null || cmbItems.length == 0)
				return displayName;
			for (ComboxValue comboxValue : cmbItems) {
				if (comboxValue != null && comboxValue.getValue() != null
						&& comboxValue.getValue().equals(alerttypeBVO.getValue())) {
					displayName = comboxValue.getDisplayName();
					alerttypeBVO.setRefervalue(displayName);
					break;
				}
			}
			return displayName;
		}

		public boolean isCellEditable(int rowIndex, int columnIndex) {

			TableColumn column2 = getTable().getColumnModel().getColumn(3);/* 阈值设置 */
			String strEditType = getValidKeys()[rowIndex].getVartype();
			String strRefName = getValidKeys()[rowIndex].getRefername();
			int editType = 0;
			// 转换
			if (strEditType.equals(m_editTypeConst.CHARACTER.getValue().toString()))
				editType = IEditType.CHARACTER;
			else if (strEditType.equals(m_editTypeConst.BOOLEAN.getValue().toString()))
				editType = IEditType.LOGICAL;
			else if (strEditType.equals(m_editTypeConst.INTEGER.getValue().toString()))
				editType = IEditType.INGETER;
			else if (strEditType.equals(m_editTypeConst.DOUBLE.getValue().toString()))
				editType = IEditType.DOUBLE;
			else if (strEditType.equals(m_editTypeConst.REFERENCE.getValue().toString()))
				editType = IEditType.REFERENCE;
			else if (strEditType.equals(m_editTypeConst.COMBOBOX.getValue().toString()))
				editType = IEditType.COMMBOX;

			// 初始化参照
			switch (editType) {
				case IEditType.CHARACTER:
					UITextField tf = new UITextField();
					tf.setMaxLength(256);
					column2.setCellEditor(new DefaultCellEditor(tf));
					break;
				case IEditType.INGETER:
					UITextField tfi = new UITextField();
					tfi.setMaxLength(256);
					tfi.setTextType("TextInt");
					column2.setCellEditor(new DefaultCellEditor(tfi));
					break;
				case IEditType.DOUBLE:
					UITextField tfd = new UITextField();
					tfd.setMaxLength(256);
					tfd.setTextType("TextDbl");
					column2.setCellEditor(new DefaultCellEditor(tfd));
					break;
				case IEditType.LOGICAL:
					UITextField m_tf = new UITextField();
					m_tf.setMaxLength(256);
					column2.setCellEditor(new DefaultCellEditor(m_Editor));
					break;
				case IEditType.REFERENCE:
					UIRefPane ref = new UIRefPane();
					if (strRefName.equals("日历")) {
						ref.setRefNodeName(strRefName);
					} else {
						BasedocVO basedocvo = getBasedocVO(strRefName);
						if (basedocvo == null) {
							Debug.error("没有相应基础档案类型数据。");
						} else {
							ref = DapCall.getUIRefPane(basedocvo);
							// gss 针对总帐做的特殊处理,设置主体账簿PK
							if (m_selectedType != null && (isNecessaryAccount(m_selectedType.getBelong_system()))) {
								String pk = getUIRefAccount().getRefPK();
								AbstractRefModel refModel = ref.getRefModel();
								if (pk != null)
									refModel.setPk_GlOrgBook(OrgnizeTypeVO.ZHUZHANG_TYPE, pk);
							}

							// 对"会计科目"的特殊处理,允许其能选中间级,
							if (strRefName.equals("会计科目"))
								ref.setNotLeafSelectedEnabled(true);

						}
					}
					nc.ui.bd.manage.UIRefCellEditorNew refEditor = new nc.ui.bd.manage.UIRefCellEditorNew(ref);
					column2.setCellEditor(refEditor);
					// new:改成能让其编辑
					ref.setEditable(true);
					ref.setMultiSelectedEnabled(true);
					ref.setPK(getValidKeys()[rowIndex].getValue());
					m_ref = ref;
					break;
				case IEditType.COMMBOX:
					if (isQePreAlert)
						break;
					UIComboBox cmb = new UIComboBox();
					ComboxValue[] cmbItems = refname_Cmbitems_Map.get(strRefName);
					if (cmbItems != null) {
						if (!getValidKeys()[rowIndex].getNotnullable().booleanValue())
							cmb.addItem(new ComboxValue());
						for (ComboxValue cmbItem : cmbItems) {
							if (cmbItem == null)
								continue;
							cmb.addItem(cmbItem);
							if (cmbItem.toString().equals(getValidKeys()[rowIndex].getRefervalue()))
								cmb.setSelectedItem(cmbItem);
						}
					}
					column2.setCellEditor(new DefaultCellEditor(cmb));
					break;
				default:
					break;
			}
			if (columnIndex == 3) { return true; }
			if (columnIndex == 2 && isQePreAlert) {
				TableColumn column1 = getTable().getColumnModel().getColumn(2);/* 操作符 */
				UIComboBox cmb = new UIComboBox();
				for (int i = 0; i < operator.length; i++) {
					cmb.addItem(operator[i]);
				}
				column1.setCellEditor(new DefaultCellEditor(cmb));
				return true;
			}

			return false;
		}

		public void setValueAt(Object obj, int row, int col) {
			switch (col) {
				case 2:
					if (isQePreAlert)
						getValidKeys()[row].setRefervalue(obj == null ? "" : obj.toString());
					break;
				case 3:
					if (getValidKeys()[row].getVartype().equals(
							m_editTypeConst.REFERENCE.getValue().toString())) {
						getValidKeys()[row].setValue(convertAryToStr(m_ref.getRefPKs()));
						getValidKeys()[row].setRefervalue(convertAryToStr(m_ref.getRefNames()));
						setRefPKs(row, convertAryToStr(m_ref.getRefPKs()));
					} else if (!isQePreAlert
							&& getValidKeys()[row].getVartype().equals(
									m_editTypeConst.COMBOBOX.getValue().toString())) {
						getValidKeys()[row].setRefervalue(obj == null ? "" : obj.toString());
						getValidKeys()[row].setValue(obj == null ? null : ((ComboxValue) obj).getValue());
					} else {
						getValidKeys()[row].setValue(obj == null ? "" : obj.toString());
					}
					break;
			}
		}

		private String convertAryToStr(String[] values) {
			if (values == null || values.length == 0)
				return "";
			String retValue = "";
			for (String tmp : values) {
				retValue = retValue + "," + tmp;
			}
			if (!StringUtil.isEmptyWithTrim(retValue))
				retValue = retValue.substring(1);
			return retValue;
		}
	}
}