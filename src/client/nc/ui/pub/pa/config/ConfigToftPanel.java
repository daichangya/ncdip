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
 * Ԥ���������ý���
 * 
 * @author gss 2004-11-23
 * @modifier yzy 2006-1-10 ʹ�����ݿ�VO����ļ�ʵ�壬ʹ���µķ������BO
 * @modifier leijun 2007-6-20 ҵ��Ԥ����Ŀ�����µ�UI
 */
public class ConfigToftPanel extends ToftPanel {
	class RegistryTableModel extends VOTableModel {
		String[] columns = {
				NCLangRes.getInstance().getStrByID("101502", "UPP101502-000114")/* "����" */,
				NCLangRes.getInstance().getStrByID("101502", "UPP101502-000228")/* "�ļ���" */,
				NCLangRes.getInstance().getStrByID("101502", "UPP101502-000229")/* "Ԥ������" */,
				NCLangRes.getInstance().getStrByID("101502", "UPP101502-000312")/* "��������" */,
				NCLangRes.getInstance().getStrByID("101502", "UPP101502-000060") /* "Ԥ����ʽ" */};

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

	// ��Ŀ����Table
	private UITable m_registryTable = null;

	// ��Ŀ����Tab
	private UIPanel ivjUIPanel3 = null;

	private ButtonObject m_boAddRegistry = new ButtonObject(NCLangRes.getInstance().getStrByID(
			"common", "UC001-0000002")/*@res "����"*/, NCLangRes.getInstance().getStrByID("common",
			"UC001-0000002")/*@res "����"*/, 2, "����");/*-=notranslate=-*/

	protected ButtonObject m_boCopyRegistry = new ButtonObject(NCLangRes.getInstance().getStrByID(
			"common", "UC001-0000043")/*@res "����"*/, NCLangRes.getInstance().getStrByID("common",
			"UC001-0000043")/*@res "����"*/, 2, "����"); /*-=notranslate=-*/

	protected ButtonObject m_boDelRegistry = new ButtonObject(NCLangRes.getInstance().getStrByID(
			"common", "UC001-0000039")/*@res "ɾ��"*/, NCLangRes.getInstance().getStrByID("common",
			"UC001-0000039")/*@res "ɾ��"*/, 2, "ɾ��"); /*-=notranslate=-*/

	protected ButtonObject m_boEditRegistry = new ButtonObject(NCLangRes.getInstance().getStrByID(
			"common", "UC001-0000045")/* �޸� */, NCLangRes.getInstance().getStrByID("common",
			"UC001-0000045")/* �޸� */, 2, "�޸�"); /*-=notranslate=-*/

	protected ButtonObject m_boBizConfig = new ButtonObject(NCLangRes.getInstance().getStrByID(
			"101502", "UPP101502-000367")/*ҵ������ */, NCLangRes.getInstance().getStrByID("101502",
			"UPP101502-000367")/* ҵ������ */, 2, "ҵ������"); /*-=notranslate=-*/

	// sxj 2002-03-29 Ϊ���ݿ⸴������-���ں���ֻ���������ݴ����Ԥ�������!
	private String m_specialPlugin = null;

	// ��Ŀ�ı�ģ��
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
			// ��ù�˾��������Ŀ
			String whereSql = null;
			String loginCorp = PfUtilUITools.getLoginCorp();
			if ("0001".equals(loginCorp))
				//��ѯ���Ų������Ŀ
				whereSql = "(pk_corp='" + loginCorp + "' or pk_corp='' or pk_corp is null)";
			else
				//��ѯ��˾�������Ŀ
				whereSql = "pk_corp='" + loginCorp + "'";

			//����ѯ��ʱ��Ŀ
			whereSql += " and (registrytype is null or registrytype<>" + PaConstant.TASKTYPE_TEMP+")";
			AlertregistryVO[] registryvos = NCLocator.getInstance().lookup(
					IPreAlertConfigQueryService.class).queryRegistriesByClause(whereSql, null);

			if (StringUtil.isEmptyWithTrim(m_specialPlugin)) {
				//���˳�ҵ��Ԥ������Ŀ
				for (int i = 0; i < (registryvos == null ? 0 : registryvos.length); i++) {
					if (registryvos[i].getAlertTypeVo().getTasktype() == PaConstant.TASKTYPE_PA)
						getRegistryTableModel().addVO(registryvos[i]);
				}
			} else {
				//���˳��ض�ҵ��������Ŀ
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
		return NCLangRes.getInstance().getStrByID("101502", "UPP101502-000232")/* "Ԥ����Ŀ����" */;
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

		// ��ʼ���������
		initTableConnections();
		// ��ʼ�������ô���
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
	 * ����Ԥ����Ŀ
	 */
	public void onAddRegistryButtonClick() {

		RegistryDlg aRegistryDlg = new RegistryDlg(this, m_specialPlugin) {
			protected String checkOK() {
				String superhint = super.checkOK();
				if (superhint != null && superhint.length() > 0)
					return superhint;
				else {//���·������ϵ�����
					AlertregistryVO registry = getRegistry();

					registry.timeConfig2VO();
					registry.method2VO();

					try {
						String[] keys = NCLocator.getInstance().lookup(IPreAlertConfigService.class)
								.insertAlertRegistries(new AlertregistryVO[] { registry });
						if (keys == null || keys[0] == null)
							return NCLangRes.getInstance().getStrByID("101502", "UPP101502-000267")/* "����Ԥ����Ŀ�Ѿ���������˾ע��������޸�����" */;
						registry.setPrimaryKey(keys[0]);
					} catch (BusinessException e) {
						Logger.error(e.getMessage(), e);
						MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("pfworkflow",
								"UPPpfworkflow-000227")/* ��ʾ */, e.getMessage());
					}

				}
				return null;
			}
		};
		aRegistryDlg.setCorpPK(PfUtilUITools.getLoginCorp());
		aRegistryDlg.setDataSource(PfUtilUITools.getLoginDs());
		aRegistryDlg.setLocationRelativeTo(this);
		aRegistryDlg.setAdd(true); // ����״̬

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
						"UPPpfworkflow-000227")/* ��ʾ */, e.getMessage());
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
			throw new PreAlertException(NCLangRes.getInstance().getStrByID("101502", "UPP101502-000369")/*"������û������ҵ������UI��"*/);
		} else {
			Object obj = Class.forName(bizclassName).newInstance();
			if (obj instanceof IBizConfigUI) {
				IBizConfigUI bizconfig = (IBizConfigUI) obj;
				bizconfig.getBizConfigDLG(selectedRegVO).showModal();
			} else {
				throw new PreAlertException(NCLangRes.getInstance()
						.getStrByID("101502", "UPP101502-000370")/*"��ҵ������UI��û��ʵ�ֽӿ� IBizConfigUI"*/);
			}
		}
	}

	/**
	 * ����Ԥ����Ŀ  
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
					String hint = NCLangRes.getInstance().getStrByID("101502", "UPP101502-000267")/* "����Ԥ����Ŀ�Ѿ���������˾ע��������޸�����" */
							+ ":\n";
					for (int i = 0; i < names.length; i++) {
						hint += "\n    " + names[i];
					}
					MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("101502",
							"UPP101502-000034")/* ������ʾ */, hint);
					return;
				}
			} catch (Exception e) {
				MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("101502",
						"UPP101502-000034")/* ������ʾ */, NCLangRes.getInstance().getStrByID("101502",
						"UPP101502-000268" + e.getMessage())/* "���Ԥ����Ŀ����" */);
				Logger.error(e.getMessage(), e);
			}
			if (registrieSuccess != null) {
				getRegistryTableModel().addVO(registrieSuccess);
			}

		}
	}

	public void onDelRegistryButtonClick() {
		// ��ȡѡ�е���Щ������
		int iSelectedRow = checkSelectRow();
		if (iSelectedRow < 0)
			return;
		if (MessageDialog.showOkCancelDlg(this, NCLangRes.getInstance().getStrByID("101502",
				"UPP101502-000281")/* "ȷ��ɾ����Ŀ " */, NCLangRes.getInstance().getStrByID("101502",
				"UPP101502-000282"))/* "�Ƿ�ȷ��ɾ������Ŀ?" */== UIDialog.ID_CANCEL)
			return;

		String pkReg = null;
		Object obj = getRegistryTableModel().getValueAt(iSelectedRow, 0);
		if (obj instanceof DefaultConstEnum)
			pkReg = ((DefaultConstEnum) obj).getValue().toString();

		try {
			// ɾ���������˵�����
			NCLocator.getInstance().lookup(IPreAlertConfigService.class).deleteRegistriesByPk(
					new String[] { pkReg });

			delBizConfig(iSelectedRow);

			getRegistryTableModel().removeVO(iSelectedRow);
		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("101502",
					"UPP101502-000034")/* "������ʾ" */, NCLangRes.getInstance().getStrByID("101502",
					"UPP101502-000238")/* ɾ��ѡ�е�Ԥ����Ŀ����" */);
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
							"UPPpfworkflow-000227")/* ��ʾ */, NCLangRes.getInstance().getStrByID("common",
							"UCH004")/* ��ѡ��Ҫ����������� */);
			iSelectedRow = -1;
		}
		return iSelectedRow;
	}

	public void onEditRegistryButtonClick() {
		// ��ȡѡ�е���Щ������
		int iSelectedRow = getRegistryTable().getSelectedRow();
		if (iSelectedRow < 0)
			return;

		AlertregistryVO selectedRegVO = (AlertregistryVO) getRegistryTableModel().getVO(iSelectedRow);

		RegistryDlg aRegistryDlg = new RegistryDlg(this, m_specialPlugin) {
			protected String checkOK() {
				String superhint = super.checkOK();
				if (superhint != null && superhint.length() > 0)
					return superhint;
				else {//���·������ϵ�����
					AlertregistryVO registry = getRegistry();

					registry.timeConfig2VO();
					registry.method2VO();

					try {

						int result = NCLocator.getInstance().lookup(IPreAlertConfigService.class)
								.updateAlertRegistries(new AlertregistryVO[] { registry });
						if (result <= 0)
							return NCLangRes.getInstance().getStrByID("101502", "UPP101502-000296")/* Ԥ����Ŀ�ظ���ͬ��˾,ͬ����,ͬԤ���� */;
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
		aRegistryDlg.setAdd(false); // �޸�״̬
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
			//				// ���·������ϵ�����
			//				int result = ClientServiceLocator.updateAlertRegistries(new AlertregistryVO[] { registry });
			//				if (result <= 0) {
			//					MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("101502",
			//							"UPP101502-000034")/* ������ʾ */, NCLangRes.getInstance().getStrByID("101502",
			//							"UPP101502-000296")/* Ԥ����Ŀ�ظ���ͬ��˾,ͬ����,ͬԤ���� */);
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
		//��ձ�ģ�ͺ��ٴλ�ȡ����
		getRegistryTableModel().clearTable();
		initDataOfRegistryTable();

		getRegistryTable().validate();
	}

}
