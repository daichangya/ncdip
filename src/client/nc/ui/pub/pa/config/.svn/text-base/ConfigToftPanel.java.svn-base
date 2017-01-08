package nc.ui.pub.pa.config;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ListSelectionModel;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.pa.PaConstant;
import nc.itf.uap.pa.IPreAlertConfigQueryService;
import nc.itf.uap.pa.IPreAlertConfigService;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.change.PfUtilUITools;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.beans.table.VOTableModel;
import nc.ui.pub.pa.taskcenter.IBizConfigUI;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.pa.AlertregistryVO;
import nc.vo.pub.pa.PreAlertException;

/**
 * 预警条件设置界面
 * 
 * @author gss 2004-11-23
 * @modifier yzy 2006-1-10 使用数据库VO替代文件实体，使用新的服务代替BO
 * @modifier leijun 2007-6-20 业务预警条目部署新的UI
 */
public class ConfigToftPanel extends ToftPanel {
	class RegistryTableModel extends VOTableModel {
		String[] columns = {
				NCLangRes.getInstance().getStrByID("101502", "UPP101502-000114")/* "名称" */,
				NCLangRes.getInstance().getStrByID("101502", "UPP101502-000228")/* "文件名" */,
				NCLangRes.getInstance().getStrByID("101502", "UPP101502-000229")/* "预警类型" */,
				NCLangRes.getInstance().getStrByID("101502", "UPP101502-000312")/* "触发策略" */,
				NCLangRes.getInstance().getStrByID("101502", "UPP101502-000060") /* "预警方式" */};

		public RegistryTableModel(Class c) {
			super(c);
		}

		public int getColumnCount() {
			return columns.length;
		}

		public String getColumnName(int col) {
			return columns[col];
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			AlertregistryVO aregVO = (AlertregistryVO) getVO(rowIndex);
			switch (columnIndex) {
				case 0:
					return new DefaultConstEnum(aregVO.getPrimaryKey(), aregVO.getAlertname());
				case 1:
					return aregVO.getFilename();
				case 2:
					String nameRid = aregVO.getAlertTypeVo().getName_resid();
					return nameRid == null ? aregVO.getAlertTypeVo().getType_name() : NCLangRes.getInstance()
							.getStrByID("prealerttype", nameRid);
				case 3:
					return aregVO.getIstiming();
				case 4:
					return (null == aregVO.getAlertMethod() ? "" : aregVO.getAlertMethod().toString());
				default:
					return "";
			}
		}

		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}
	}

	private UIScrollPane ivjJScrollPane2 = null;

	// 条目设置Table
	private UITable m_registryTable = null;

	// 条目设置Tab
	private UIPanel ivjUIPanel3 = null;

	private ButtonObject m_boAddRegistry = new ButtonObject(NCLangRes.getInstance().getStrByID(
			"common", "UC001-0000002")/*@res "增加"*/, NCLangRes.getInstance().getStrByID("common",
			"UC001-0000002")/*@res "增加"*/, 2, "增加");/*-=notranslate=-*/

	protected ButtonObject m_boCopyRegistry = new ButtonObject(NCLangRes.getInstance().getStrByID(
			"common", "UC001-0000043")/*@res "复制"*/, NCLangRes.getInstance().getStrByID("common",
			"UC001-0000043")/*@res "复制"*/, 2, "复制"); /*-=notranslate=-*/

	protected ButtonObject m_boDelRegistry = new ButtonObject(NCLangRes.getInstance().getStrByID(
			"common", "UC001-0000039")/*@res "删除"*/, NCLangRes.getInstance().getStrByID("common",
			"UC001-0000039")/*@res "删除"*/, 2, "删除"); /*-=notranslate=-*/

	protected ButtonObject m_boEditRegistry = new ButtonObject(NCLangRes.getInstance().getStrByID(
			"common", "UC001-0000045")/* 修改 */, NCLangRes.getInstance().getStrByID("common",
			"UC001-0000045")/* 修改 */, 2, "修改"); /*-=notranslate=-*/

	protected ButtonObject m_boBizConfig = new ButtonObject(NCLangRes.getInstance().getStrByID(
			"101502", "UPP101502-000367")/*业务配置 */, NCLangRes.getInstance().getStrByID("101502",
			"UPP101502-000367")/* 业务配置 */, 2, "业务配置"); /*-=notranslate=-*/

	// sxj 2002-03-29 为数据库复制增加-现在好像只是用于数据传输的预警插件中!
	private String m_specialPlugin = null;

	// 条目的表模型
	private RegistryTableModel m_tmRegistry = null;

	public ConfigToftPanel() {
		this(null);
	}

	public ConfigToftPanel(String typeNameForDBCopy) {
		super();
		m_specialPlugin = typeNameForDBCopy;

		initUI();

		initDataOfRegistryTable();
	}

	private void initDataOfRegistryTable() {
		try {
			// 获得公司下所有条目
			String whereSql = null;
			String loginCorp = PfUtilUITools.getLoginCorp();
			if ("0001".equals(loginCorp))
				//查询集团部署的条目
				whereSql = "(pk_corp='" + loginCorp + "' or pk_corp='' or pk_corp is null)";
			else
				//查询公司部署的条目
				whereSql = "pk_corp='" + loginCorp + "'";

			//不查询临时条目
			whereSql += " and (registrytype is null or registrytype<>" + PaConstant.TASKTYPE_TEMP+")";
			AlertregistryVO[] registryvos = NCLocator.getInstance().lookup(
					IPreAlertConfigQueryService.class).queryRegistriesByClause(whereSql, null);

			if (StringUtil.isEmptyWithTrim(m_specialPlugin)) {
				//过滤出业务预警的条目
				for (int i = 0; i < (registryvos == null ? 0 : registryvos.length); i++) {
					if (registryvos[i].getAlertTypeVo().getTasktype() == PaConstant.TASKTYPE_PA)
						getRegistryTableModel().addVO(registryvos[i]);
				}
			} else {
				//过滤出特定业务插件的条目
				for (int i = 0; i < (registryvos == null ? 0 : registryvos.length); i++) {
					if (registryvos[i].getAlertTypeVo().getBusi_plugin().equals(m_specialPlugin))
						getRegistryTableModel().addVO(registryvos[i]);
				}
			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}
	}

	private UIScrollPane getJScrollPane2() {
		if (ivjJScrollPane2 == null) {
			ivjJScrollPane2 = new UIScrollPane();
			ivjJScrollPane2.setName("JScrollPane2");
			ivjJScrollPane2.setAutoscrolls(true);
			ivjJScrollPane2.setViewportView(getRegistryTable());
			ivjJScrollPane2.setColumnHeaderView(m_registryTable.getTableHeader());
			ivjJScrollPane2.getViewport().setScrollMode(2);
		}
		return ivjJScrollPane2;
	}

	public RegistryTableModel getRegistryTableModel() {
		if (m_tmRegistry == null) {
			m_tmRegistry = new RegistryTableModel(AlertregistryVO.class);
		}
		return m_tmRegistry;
	}

	private UITable getRegistryTable() {
		if (m_registryTable == null) {
			m_registryTable = new UITable();
			m_registryTable.setName("m_registryTable");

			m_registryTable.setBounds(0, 0, 200, 200);
			m_registryTable.setModel(getRegistryTableModel());
			m_registryTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			m_registryTable.setColumnWidth(new int[] { 100, 50, 30, 30, 200 });
			m_registryTable.addSortListener();
			m_registryTable.getColumn(getRegistryTableModel().columns[3]).setCellRenderer(
					new RegistryCellRender());
		}
		return m_registryTable;
	}

	public String getTitle() {
		return NCLangRes.getInstance().getStrByID("101502", "UPP101502-000232")/* "预警条目配置" */;
	}

	public UIPanel getUIPanel3() {
		if (ivjUIPanel3 == null) {
			ivjUIPanel3 = new UIPanel();
			ivjUIPanel3.setName("UIPanel3");
			ivjUIPanel3.setLayout(new java.awt.BorderLayout());
			ivjUIPanel3.add(getJScrollPane2(), "Center");

		}
		return ivjUIPanel3;
	}

	private void initUI() {
		setName("ConfigToftPanel");
		setLayout(new BorderLayout());
		add(getUIPanel3(), BorderLayout.CENTER);

		// 初始化表格连接
		initTableConnections();
		// 初始化条配置处理按
		//setButtons(new ButtonObject[] { m_boAddRegistry, m_boEditRegistry, m_boCopyRegistry,m_boDelRegistry,m_boBizConfig });
		setButtons(new ButtonObject[] { m_boAddRegistry, m_boEditRegistry, m_boCopyRegistry,
				m_boDelRegistry });
	}

	private void initTableConnections() {
		getRegistryTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					onEditRegistryButtonClick();
				}
			}

		});
	}

	/**
	 * 新增预警条目
	 */
	public void onAddRegistryButtonClick() {

		RegistryDlg aRegistryDlg = new RegistryDlg(this, m_specialPlugin) {
			protected String checkOK() {
				String superhint = super.checkOK();
				if (superhint != null && superhint.length() > 0)
					return superhint;
				else {//更新服务器上的数据
					AlertregistryVO registry = getRegistry();

					registry.timeConfig2VO();
					registry.method2VO();

					try {
						String[] keys = NCLocator.getInstance().lookup(IPreAlertConfigService.class)
								.insertAlertRegistries(new AlertregistryVO[] { registry });
						if (keys == null || keys[0] == null)
							return NCLangRes.getInstance().getStrByID("101502", "UPP101502-000267")/* "下列预警条目已经在其它公司注册过，请修改名称" */;
						registry.setPrimaryKey(keys[0]);
					} catch (BusinessException e) {
						Logger.error(e.getMessage(), e);
						MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("pfworkflow",
								"UPPpfworkflow-000227")/* 提示 */, e.getMessage());
					}

				}
				return null;
			}
		};
		aRegistryDlg.setCorpPK(PfUtilUITools.getLoginCorp());
		aRegistryDlg.setDataSource(PfUtilUITools.getLoginDs());
		aRegistryDlg.setLocationRelativeTo(this);
		aRegistryDlg.setAdd(true); // 增加状态

		if (aRegistryDlg.showModal() == UIDialog.ID_OK) {
			getRegistryTableModel().addVO(aRegistryDlg.getCompletedRegitry());
			int rowNumber = getRegistryTableModel().getRowCount();
			getRegistryTable().getSelectionModel().setSelectionInterval(rowNumber - 1, rowNumber - 1);

		}

	}

	public void onButtonClicked(ButtonObject bo) {
		if (bo == m_boAddRegistry) {
			onAddRegistryButtonClick();
		} else if (bo == m_boCopyRegistry) {
			onCopyRegistryButtonClick();
		} else if (bo == m_boDelRegistry) {
			onDelRegistryButtonClick();
		} else if (bo == m_boEditRegistry) {
			onEditRegistryButtonClick();
		} else if (bo == m_boBizConfig) {
			try {
				onBizConfig();
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
				MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("pfworkflow",
						"UPPpfworkflow-000227")/* 提示 */, e.getMessage());
			}
		}
	}

	private void onBizConfig() throws Exception {
		int selrow = checkSelectRow();
		if (selrow < 0)
			return;
		AlertregistryVO selectedRegVO = (AlertregistryVO) getRegistryTableModel().getVO(selrow);
		String bizclassName = selectedRegVO.getAlertTypeVo().getBizconfigclass();
		if (bizclassName == null || bizclassName.trim().length() == 0) {
			throw new PreAlertException(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000369")/*"该类型没有配置业务配置UI类"*/);
		} else {
			Object obj = Class.forName(bizclassName).newInstance();
			if (obj instanceof IBizConfigUI) {
				IBizConfigUI bizconfig = (IBizConfigUI) obj;
				bizconfig.getBizConfigDLG(selectedRegVO).showModal();
			} else {
				throw new PreAlertException(NCLangRes.getInstance()
						.getStrByID("101502", "UPP101502-000370")/*"该业务配置UI类没有实现接口 IBizConfigUI"*/);
			}
		}
	}

	/**
	 * 拷贝预警条目  
	 */
	public void onCopyRegistryButtonClick() {
		RegistryCopyDlg copyDlg = new RegistryCopyDlg(this);
		copyDlg.setCorpPK(PfUtilUITools.getLoginCorp());
		copyDlg.setDataSource(PfUtilUITools.getLoginDs());
		copyDlg.setLocationRelativeTo(this);
		if (copyDlg.showModal() == UIDialog.ID_OK) {
			AlertregistryVO[] registries = copyDlg.getRegistries();
			AlertregistryVO[] registrieSuccess = null;
			try {
				String[] pks = NCLocator.getInstance().lookup(IPreAlertConfigService.class)
						.copyAlertRegistries(registries);

				if (pks == null || pks.length <= 0)
					return;
				ArrayList al = new ArrayList();
				ArrayList success = new ArrayList();
				for (int j = 0; j < pks.length; j++) {
					if (pks[j] == null) {
						al.add(registries[j].getAlertname());
					} else {
						AlertregistryVO registry = (AlertregistryVO) registries[j].clone();
						registry.setPrimaryKey(pks[j]);
						success.add(registry);
					}

				}
				String[] names = (String[]) al.toArray(new String[al.size()]);
				registrieSuccess = (AlertregistryVO[]) success.toArray(new AlertregistryVO[success.size()]);
				if (names != null && names.length > 0) {
					String hint = NCLangRes.getInstance().getStrByID("101502", "UPP101502-000267")/* "下列预警条目已经在其它公司注册过，请修改名称" */
							+ ":\n";
					for (int i = 0; i < names.length; i++) {
						hint += "\n    " + names[i];
					}
					MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("101502",
							"UPP101502-000034")/* 错误提示 */, hint);
					return;
				}
			} catch (Exception e) {
				MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("101502",
						"UPP101502-000034")/* 错误提示 */, NCLangRes.getInstance().getStrByID("101502",
						"UPP101502-000268" + e.getMessage())/* "添加预警条目出错！" */);
				Logger.error(e.getMessage(), e);
			}
			if (registrieSuccess != null) {
				getRegistryTableModel().addVO(registrieSuccess);
			}

		}
	}

	public void onDelRegistryButtonClick() {
		// 获取选中的那些行索引
		int iSelectedRow = checkSelectRow();
		if (iSelectedRow < 0)
			return;
		if (MessageDialog.showOkCancelDlg(this, NCLangRes.getInstance().getStrByID("101502",
				"UPP101502-000281")/* "确定删除条目 " */, NCLangRes.getInstance().getStrByID("101502",
				"UPP101502-000282"))/* "是否确定删除该条目?" */== UIDialog.ID_CANCEL)
			return;

		String pkReg = null;
		Object obj = getRegistryTableModel().getValueAt(iSelectedRow, 0);
		if (obj instanceof DefaultConstEnum)
			pkReg = ((DefaultConstEnum) obj).getValue().toString();

		try {
			// 删除服务器端的数据
			NCLocator.getInstance().lookup(IPreAlertConfigService.class).deleteRegistriesByPk(
					new String[] { pkReg });

			delBizConfig(iSelectedRow);

			getRegistryTableModel().removeVO(iSelectedRow);
		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("101502",
					"UPP101502-000034")/* "错误提示" */, NCLangRes.getInstance().getStrByID("101502",
					"UPP101502-000238")/* 删除选中的预警条目出错！" */);
			Logger.error(e.getMessage(), e);
		}
	}

	private void delBizConfig(int selrow) throws Exception {
		AlertregistryVO selectedRegVO = (AlertregistryVO) getRegistryTableModel().getVO(selrow);
		String bizclassName = selectedRegVO.getAlertTypeVo().getBizconfigclass();
		if (bizclassName != null && bizclassName.trim().length() > 0) {
			Object obj = Class.forName(bizclassName).newInstance();
			if (obj instanceof IBizConfigUI) {
				IBizConfigUI bizconfig = (IBizConfigUI) obj;
				bizconfig.deleteRegistry(selectedRegVO);
			} else {
				Logger.warn(bizclassName + "is not instanceof nc.ui.pub.pa.taskcenter.IBizConfigUI");
			}
		}
	}

	private int checkSelectRow() {
		int iSelectedRow = getRegistryTable().getSelectedRow();
		if (iSelectedRow < 0) {
			MessageDialog
					.showHintDlg(this, NCLangRes.getInstance().getStrByID("pfworkflow",
							"UPPpfworkflow-000227")/* 提示 */, NCLangRes.getInstance().getStrByID("common",
							"UCH004")/* 请选择要处理的数据行 */);
			iSelectedRow = -1;
		}
		return iSelectedRow;
	}

	public void onEditRegistryButtonClick() {
		// 获取选中的那些行索引
		int iSelectedRow = getRegistryTable().getSelectedRow();
		if (iSelectedRow < 0)
			return;

		AlertregistryVO selectedRegVO = (AlertregistryVO) getRegistryTableModel().getVO(iSelectedRow);

		RegistryDlg aRegistryDlg = new RegistryDlg(this, m_specialPlugin) {
			protected String checkOK() {
				String superhint = super.checkOK();
				if (superhint != null && superhint.length() > 0)
					return superhint;
				else {//更新服务器上的数据
					AlertregistryVO registry = getRegistry();

					registry.timeConfig2VO();
					registry.method2VO();

					try {

						int result = NCLocator.getInstance().lookup(IPreAlertConfigService.class)
								.updateAlertRegistries(new AlertregistryVO[] { registry });
						if (result <= 0)
							return NCLangRes.getInstance().getStrByID("101502", "UPP101502-000296")/* 预警条目重复。同公司,同类型,同预警名 */;
					} catch (BusinessException e) {
						Logger.error(e.getMessage(), e);
					}

				}
				return null;
			}
		};
		aRegistryDlg.setLocationRelativeTo(this);

		aRegistryDlg.setRegistry(selectedRegVO);
		aRegistryDlg.setCorpPK(PfUtilUITools.getLoginCorp());
		aRegistryDlg.setDataSource(PfUtilUITools.getLoginDs());
		aRegistryDlg.setAdd(false); // 修改状态
		if (aRegistryDlg.showModal() == UIDialog.ID_OK) {
			AlertregistryVO registry = aRegistryDlg.getCompletedRegitry();
			getRegistryTableModel().removeVO(iSelectedRow);
			getRegistryTableModel().insertVO(registry, iSelectedRow);
			getRegistryTable().getSelectionModel().setLeadSelectionIndex(iSelectedRow);
			getRegistryTable().updateUI();

			//			AlertregistryVO registry = aRegistryDlg.getRegistry();
			//			if (registry == null)
			//				return;
			//			registry.timeConfig2VO();
			//			registry.method2VO();
			//			try {
			//				// 更新服务器上的数据
			//				int result = ClientServiceLocator.updateAlertRegistries(new AlertregistryVO[] { registry });
			//				if (result <= 0) {
			//					MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("101502",
			//							"UPP101502-000034")/* 错误提示 */, NCLangRes.getInstance().getStrByID("101502",
			//							"UPP101502-000296")/* 预警条目重复。同公司,同类型,同预警名 */);
			//					return;
			//				}
			//				getRegistryTableModel().removeVO(iSelectedRow);
			//				getRegistryTableModel().insertVO(registry, iSelectedRow);
			//				getRegistryTable().getSelectionModel().setLeadSelectionIndex(iSelectedRow);
			//				getRegistryTable().updateUI();
			//			} catch (Exception e) {
			//				Logger.error(e.getMessage(), e);
			//			}
		}
	}

	public void onRefreshRegistrysButtonClick() {
		//清空表模型后，再次获取数据
		getRegistryTableModel().clearTable();
		initDataOfRegistryTable();

		getRegistryTable().validate();
	}

}
