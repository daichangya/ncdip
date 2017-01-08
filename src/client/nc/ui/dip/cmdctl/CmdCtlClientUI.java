package nc.ui.dip.cmdctl;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.dip.pub.IQueryField;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
//import nc.jdbc.framework.processor.ArrayProcessor;
//import nc.jdbc.framework.processor.MapListProcessor;
//import nc.jdbc.framework.processor.ResultSetProcessor;
//import nc.ui.dbcache.ConfigManager;
//import nc.ui.dbcache.DBCacheEnv;
//import nc.ui.dbcache.SQLFacade;
//import nc.ui.dbcache.TableInfo;
//import nc.ui.dbcache.TableInfoManager;
//import nc.ui.dbcache.VersionCheckService;
//import nc.ui.dbcache.exception.UiCacheException;
//import nc.ui.dbcache.gui.AutomaticCacheUI;
//import nc.ui.dbcache.gui.CacheConfigUI;
//import nc.ui.dbcache.gui.MessageBox;
//import nc.ui.dbcache.gui.StatusBar;
//import nc.ui.dbcache.gui.TableListPanel;
//import nc.ui.dbcache.gui.model.DefaultModel;
//import nc.ui.dbcache.gui.model.KeyWord;
//import nc.ui.dbcache.gui.model.KeyWordDocument;
//import nc.ui.dbcache.gui.model.ResultSetTableModel;
//import nc.ui.dbcache.gui.model.WizardModel;
//import nc.ui.dbcache.gui.wizard.Wizard;
//import nc.ui.dbcache.gui.wizard.WizardListener;
//import nc.ui.dbcache.loader.DataLoaderFactory;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UITable;
import nc.vo.logging.Debug;
import nc.vo.pub.BusinessException;

/**
 * @author： 贺扬 Date: 2004-11-24 Time: 15:33:02
 */
//public class CmdCtlClientUI extends ToftPanel implements WizardListener {
//
//	private CtlResultSetTableModel tableModel;
//	private boolean issucc=false;
//	private SQLFacade sqlFacade;
//
//	private JTextField tableField;
//
//	private JComboBox pkComb;
//
//	private JComboBox colNameComb;
//
//	private JTextPane sqlTextArea;
//
//	private JTextPane queryArea;
//
//	private JTable resultTable;
//
//	private JList tableList;
//
//	private DefaultListModel model;
//
//	private StatusBar status;
//
////	private JButton queryBtn;
////
////	private JButton clearBtn;
//
//	private DataLoaderFactory factoryData;
//
//	final private ButtonObject boWizard = new ButtonObject("查询");
//	final private ButtonObject boStop = new ButtonObject("停止");
//
//	final private ButtonObject boAttribute = new ButtonObject("属性设置");
//
//	final private ButtonObject boServer = new ButtonObject("数据同步服务");
//
//	final private ButtonObject autoInit = new ButtonObject("数据初始化");
//
//	private ArrayList buttons = new ArrayList();
//
//	private JDialog dlg;
//
//	private JCheckBox dynamicCheckBox;
//
//	private JCheckBox staticCheckBox;
//
//	private JCheckBox updateCheckBox;
//
//	private QueryThread query;
//
//	final String ERROR_MESSAGE = "缓存无法在两个客户端同时使用！";
//
//	public CmdCtlClientUI() throws Exception {
//		initButtons();
//		factoryData = DataLoaderFactory.getInstance();
//		sqlFacade = new SQLFacade();
//		tableModel = new CtlResultSetTableModel(sqlFacade);
//		model = new DefaultListModel();
//
//		tableList = new JList(model);
//		// tableList.setCellRenderer(new TableInfoCellRender());
//		queryArea = new JTextPane();
//		KeyWordDocument querydoc = new KeyWordDocument();
//		querydoc.setKeywords(KeyWord.getKeyWords());
//		queryArea.setDocument(querydoc);
//		KeyWordDocument DDLdoc = new KeyWordDocument();
//		DDLdoc.setKeywords(KeyWord.getKeyWords());
//		sqlTextArea = new JTextPane();
//		sqlTextArea.setDocument(DDLdoc);
//		resultTable = new UITable(tableModel);
//
//		resultTable
//				.setDefaultRenderer(Object.class, new RowTableCellRenderer());
//		dynamicCheckBox = new JCheckBox("动态表");
//		staticCheckBox = new JCheckBox("静态表");
//		updateCheckBox = new JCheckBox("自动维护更新");
//		status = new StatusBar();
////		queryBtn = new UIButton("查询");
////		clearBtn = new UIButton("停止");
////		clearBtn.setEnabled(false);
//		status.setPreferredSize(new Dimension(20, 20));
//		resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//		// VersionCheckService checkCacheService = new VersionCheckService();
//		// checkCacheService.startVersionChecker();
//		initComponents();
//		getTableList();
//
//	}
//
//	/**
//	 * 子类实现该方法，返回业务界面的标题。
//	 * 
//	 * @return java.lang.String (00-6-6 13:33:25)
//	 */
//	public String getTitle() {
//		return "前台数据库管理";
//	}
//
//	/**
//	 * 子类实现该方法，响应按钮事件。
//	 * 
//	 * @param bo
//	 *            ButtonObject (00-6-1 10:32:59)
//	 */
//	public void onButtonClicked(ButtonObject bo) {
//
//		if (bo.getName().equals("查询")) {
//			executeQuery();
//			/*
//			if (!DBCacheEnv.canUseDB) {
//				// MessageBox.showMessage()
//				MessageBox.showMessage("错误窗口", ERROR_MESSAGE);
//				return;
//			}
//			dlg = new JDialog((Frame) SwingUtilities.getAncestorOfClass(
//					Frame.class, this), "查询");
//			Wizard wizard = new Wizard();
//			wizard.addWizardListener(this);
//			dlg.setContentPane(wizard);
//			wizard.start(new TableListPanel(new WizardModel()));
//			dlg.pack();
//			dlg.setModal(true);
//			center(dlg);
//			dlg.show();
//			return;
//
//		*/}
//		if(bo.getName().equals("停止")){
//			stopQuery();
//		}
//		// if (bo.getName().equals("停止数据库")) {
//		//
//		// DatabaseManager.getInstance().stopServer();
//		// return;
//		//
//		// }
//		//
//		// if (bo.getName().equals("启动数据库")) {
//		//
//		// DatabaseManager.getInstance().startServer();
//		// return;
//		//
//		// }
//		// if (bo.getName().equals("优化数据库")) {
//		// DatabaseManager.getInstance().optimizeServer();
//		// return;
//		// }
//		if (bo.getName().equals("停止服务")) {
//			if (!DBCacheEnv.canUseDB) {
//				// MessageBox.showMessage()
//				MessageBox.showMessage("错误窗口", ERROR_MESSAGE);
//				return;
//			}
//			try {
//				VersionCheckService.getInstance().stopVersionCheckService();
//
//			} catch (Exception e) {
//				MessageBox.showError(e);
//			}
//			return;
//		}
//		if (bo.getName().equals("属性设置")) {
//			if (!DBCacheEnv.canUseDB) {
//				// MessageBox.showMessage()
//				MessageBox.showMessage("错误窗口", ERROR_MESSAGE);
//				return;
//			}
//			CacheConfigUI ui = new CacheConfigUI(this, "缓存设置");
//			ui.pack();
//			ui.show();
//			center(ui);
//			return;
//		}
//		if (bo.getName().equals("启动服务")) {
//			if (!DBCacheEnv.canUseDB) {
//				// MessageBox.showMessage()
//				MessageBox.showMessage("错误窗口", ERROR_MESSAGE);
//				return;
//			}
//			try {
//				VersionCheckService.getInstance().startVersionChecker();
//			} catch (Exception e) {
//				MessageBox.showError(e);
//			}
//			return;
//		}
//		// if (bo.getName().equals("关闭数据库")) {
//		// try {
//		// SQLFacade facade = new SQLFacade();
//		// facade.update("SHUTDOWN COMPACT;");
//		// } catch (UiCacheException e) {
//		// MessageBox.showError(e);
//		// }
//		// return;
//		// }
//		if (bo.getName().equals("刷新表列表")) {
//			refreashTableList();
//		}
//		if (bo.getName().equals("数据初始化")) {
//			if (!DBCacheEnv.canUseDB) {
//				// MessageBox.showMessage()
//				MessageBox.showMessage("错误窗口", ERROR_MESSAGE);
//				return;
//			}
//			autoInitialized();
//		}
//	}
//
//	private void autoInitialized() {
//		DefaultModel model = new DefaultModel();
//
//		final AutomaticCacheUI ui = new AutomaticCacheUI(this, "数据初试化", model);
//		ui.setModal(true);
//		ui.setResizable(false);
//		ui.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//		ui.addWindowListener(new WindowAdapter() {
//			public void windowClosing(WindowEvent e) {
//				ui.stopInitTables();
//			}
//		});
//		ui.pack();
//		center(ui);
//		ui.show();
//		refreashTableList();
//	}
//
//	private void initComponents() {
//		tableField = new JTextField(10);
//		pkComb = new JComboBox();
//		colNameComb = new JComboBox();
//		JSplitPane splitePane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
//		splitePane.setOneTouchExpandable(true);
//		splitePane.setDividerSize(8);
//		splitePane.setDividerLocation(180);
//		splitePane.add(ceateLeftPanel());
//		splitePane.add(createTabPanel());
//
//		// pane.setSelectedIndex(0);
//		this.add(splitePane, BorderLayout.CENTER);
//		this.add(status, BorderLayout.SOUTH);
//	}
//
//	private JPanel ceateLeftPanel() {
//		final JPopupMenu menu = new JPopupMenu();
//		JMenuItem flushItem = new JMenuItem("刷新表数据");
//		menu.add(flushItem);
//		flushItem.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				new UpdateTableThread().start();
//
//			}
//		});
//		JMenuItem initItem = new JMenuItem("初始化表数据");
//		menu.add(initItem);
//		initItem.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				new InitTableThread().start();
//			}
//		});
//		// JMenuItem deleteItem = new JMenuItem("删除表数据");
//		// menu.add(deleteItem);
//		// deleteItem.addActionListener(new ActionListener() {
//		// public void actionPerformed(ActionEvent e) {
//		// new DeleteDataThread().start();
//		// }
//		// });
//		JMenuItem dropItem = new JMenuItem("删除该缓存表");
//		menu.add(dropItem);
//		dropItem.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				new DeleteTableThread().start();
//
//			}
//		});
//		JPanel leftPanel = new JPanel();
//		leftPanel.setLayout(new GridLayout(1, 1));
//
//		tableList.addListSelectionListener(new ListSelectionListener() {
//			public void valueChanged(ListSelectionEvent e) {
//				String tableName = (String) tableList.getSelectedValue();
//				try {
//					showTableSetting(tableName);
//					queryArea.setText("SELECT * FROM " + tableName);
//					// new QueryThread().start();
//				} catch (Exception e1) {
//					// MessageBox.showError("错误窗口", e1);
//				}
//			}
//		});
//
//		tableList.addMouseListener(new MouseAdapter() {
//			public void mouseReleased(MouseEvent e) {
//				if (e.isPopupTrigger()) {
//					if (tableList.getSelectedIndex() != -1)
//						menu.show(e.getComponent(), e.getX(), e.getY());
//				}
//			}
//		});
//		JScrollPane scroll = new JScrollPane(tableList);
//		scroll.setBorder(createBorder("数据表列表"));
//		scroll.setPreferredSize(new Dimension(150, 400));
//		scroll.setMinimumSize(new Dimension(150, 400));
//		leftPanel.add(scroll);
//		return leftPanel;
//	}
//
//	private void getTableList() {
//		IUAPQueryBS iuq=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//		ArrayList<Map> res = null;
//		try {
//			res = (ArrayList<Map>) iuq.executeQuery("select table_name from user_tables where table_name like 'DIP%'", new MapListProcessor());
//		} catch (BusinessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if(res!=null&&res.size()>0){
//			for (int i=0;i<res.size();i++) {
//				Map map=res.get(i);
//				model.addElement(map.get("table_name").toString().toLowerCase());
//			}
//		}
//	/*	TableInfoManager.getInstance().reload();
//		Collection tables = TableInfoManager.getInstance().getTableNameList();
//		for (Iterator iterator = tables.iterator(); iterator.hasNext();) {
//			model.addElement(iterator.next());
//		}
//*/
//	}
//
//	/**
//	 * 刷新表列表
//	 */
//	private void refreashTableList() {
//		tableList.transferFocus();
//		model.clear();
//		TableInfoManager.getInstance().reload();
//		Collection tables = TableInfoManager.getInstance().getTableNameList();
//		for (Iterator iterator = tables.iterator(); iterator.hasNext();) {
//			model.addElement(iterator.next());
//		}
//	}
//
//	private JComponent createTabPanel() {
//		JTabbedPane pane = new JTabbedPane();
//		pane.add("查  询", createQueryPanel());
//		// pane.setIconAt(0, IconFactory.getIcon("toc_open.gif"));
////		pane.add("属  性", createPropertyPanel());
//		// pane.setIconAt(1, IconFactory.getIcon("toc_open.gif"));
//		pane.setSelectedIndex(0);
//		return pane;
//	}
//
//	private JComponent createQueryPanel() {
//		JSplitPane splitePane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
//		splitePane.setOneTouchExpandable(true);
//		splitePane.setDividerSize(8);
//		splitePane.setDividerLocation(100);
//		JPanel inputPanel = new JPanel();
//		JPanel buttonPanel = new JPanel();
//		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
//		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.LINE_AXIS));
//		inputPanel.add(new JScrollPane(queryArea));
////		queryBtn.addActionListener(new ActionListener() {
////			public void actionPerformed(ActionEvent e) {
////				executeQuery();
////			}
////		});
////
////		clearBtn.addActionListener(new ActionListener() {
////			public void actionPerformed(ActionEvent e) {
////				stopQuery();
////			}
////		});
////		buttonPanel.add(queryBtn);
//		buttonPanel.add(Box.createRigidArea(new Dimension(5, 5)));
////		buttonPanel.add(clearBtn);
//		inputPanel.add(Box.createRigidArea(new Dimension(4, 20)));
//		inputPanel.add(buttonPanel);
//		inputPanel.add(Box.createRigidArea(new Dimension(4, 20)));
//		inputPanel.setBorder(createBorder(" 查询条件 "));
//		JScrollPane scrol = new JScrollPane(resultTable);
//		scrol.setBorder(createBorder("查询结果"));
//		splitePane.add(inputPanel);
//		splitePane.add(scrol);
//		return splitePane;
//	}
//
//	private void executeQuery() {
//		if (!DBCacheEnv.canUseDB) {
//			// MessageBox.showMessage()
//			MessageBox.showMessage("错误窗口", ERROR_MESSAGE);
//			return;
//		}
//		status.showMessage("");
//		status.startProgress();
////		queryBtn.setEnabled(false);
////		clearBtn.setEnabled(true);
//		query = new QueryThread(this);
//		query.setDaemon(true);
//		query.start();
//		
//
//	}
//
//	private void stopQuery() {
////		queryBtn.setEnabled(true);
////		clearBtn.setEnabled(false);
//		status.stopProgress();
//		if (query != null)
//			query.stop();
//	}
//
//	private JComponent createPropertyPanel() {
//		JPanel main = new JPanel();
//		main.setLayout(new BorderLayout());
//		JPanel sqlPanel = new JPanel();
//		JPanel topPanel = new JPanel();
//		topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
//		topPanel.add(new JLabel("表名:", JLabel.RIGHT));
//		topPanel.add(tableField);
//		topPanel.add(new JLabel("主键:", JLabel.RIGHT));
//		topPanel.add(pkComb);
//		topPanel.add(new JLabel("查询列名:", JLabel.RIGHT));
//		topPanel.add(colNameComb);
//		topPanel.add(dynamicCheckBox);
//		topPanel.add(staticCheckBox);
//		topPanel.add(updateCheckBox);
//		sqlPanel.setLayout(new GridLayout(1, 1));
//		sqlTextArea.setEditable(false);
//
//		sqlPanel.add(new JScrollPane(sqlTextArea));
//		sqlPanel.setBorder(createBorder("SQL语句"));
//
//		main.add(sqlPanel, BorderLayout.CENTER);
//		main.add(topPanel, BorderLayout.NORTH);
//		return main;
//	}
//
//	private void showTableSetting(String tableName) {
////		TableInfo table = TableInfoManager.getInstance()
////				.getTableInfo(tableName);
//		sqlTextArea.setText(null);
//		tableField.setText(tableName);
//		tableField.setEnabled(false);
//		colNameComb.removeAllItems();
//		pkComb.removeAllItems();
//		java.util.List colNames = new ArrayList();//table.getColumnNames();
//		if (colNames.size() == 0)
//			colNameComb.addItem("所有列");
//		for (Iterator iterator = colNames.iterator(); iterator.hasNext();) {
//			colNameComb.addItem((String) iterator.next());
//		}
////		for (Iterator iterator = table.getPrimaryKeys().iterator(); iterator
////				.hasNext();) {
////			pkComb.addItem((String) iterator.next());
////		}
//
//		dynamicCheckBox.setSelected(true);
////		dynamicCheckBox.setSelected(table.isDynamicLoad());
//		dynamicCheckBox.setEnabled(false);
//		staticCheckBox.setSelected(false);
////		staticCheckBox.setSelected(table.isInitLoad());
//		staticCheckBox.setEnabled(false);
//		updateCheckBox.setSelected(false);
////		updateCheckBox.setSelected(!table.isLazyLoad());
//		updateCheckBox.setEnabled(false);
//	}
//
//	private TitledBorder createBorder(String title) {
//		return BorderFactory.createTitledBorder(new EtchedBorder(), title);
//	}
//
//	public final nc.ui.pub.ButtonObject[] getButtons() {
//
//		return (nc.ui.pub.ButtonObject[]) buttons.toArray(new ButtonObject[0]);
//	}
//
//	protected void initButtons() {
//		// addButton(boRefresh);
//		
//		boWizard.setHint(boWizard.getName());
//		boStop.setHint(boStop.getName());
////		autoInit.setHint(autoInit.getName());
//		addButton(boWizard);
//		addButton(boStop);
//		// addButton(boAttribute);
//		// boModify.addChileButtons(createSubButtons(new String[]{"刷新缓存表数据",
//		// "初始化缓存表数据","删除缓存表数据","删除缓存表"}));
//		// addButton(boModify);
//		// boImprove.addChileButtons(createSubButtons(new String[]{"关闭数据库",
//		// "优化"}));
//		// addButton(boImprove);
//		// boDatabase.addChileButtons(createSubButtons(new String[] { "停止数据库",
//		// "启动数据库"}));
//		// boDatabase.setCheckboxGroup(true);
//		// boDatabase.getChildButtonGroup()[1].setSelected(true);
////		boServer
////				.addChileButtons(createSubButtons(new String[] { "停止服务", "启动服务" }));
////		boServer.setCheckboxGroup(true);
////		boServer.getChildButtonGroup()[1].setSelected(true);
////
////		// addButton(autoInit);
////		addButton(boServer);
//		// addButton(boDatabase);
//
//	}
//
//	protected void addButton(ButtonObject button) {
//		buttons.add(button);
//	}
//
//	private ButtonObject[] createSubButtons(String names[]) {
//		ButtonObject[] buttons = new ButtonObject[names.length];
//		for (int i = 0; i < names.length; i++) {
//			buttons[i] = new ButtonObject(names[i]);
//			buttons[i].setHint(names[i]);
//		}
//		return buttons;
//	}
//
//	final public void center(final Component dlg) {
//		final Dimension dlgSize = dlg.getPreferredSize();
//		final Dimension frmSize = getSize();
//		final Point loc = getLocationOnScreen();
//		dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
//				(frmSize.height - dlgSize.height) / 2 + loc.y);
//
//	}
//
//	public void wizardFinished(Wizard wizard) {
//		dlg.dispose();
//
//		refreashTableList();
//
//	}
//
//	public void wizardCancelled(Wizard wizard) {
//		dlg.dispose();
//	}
//
//	public void wizardPanelChanged(Wizard wizard) {
//
//	}
//
//	public static void main(String[] args) throws Exception {
//		JFrame f = new JFrame();
//		f.getContentPane().add(new CmdCtlClientUI());
//		f.pack();
//		f.show();
//	}
//
//	private class UpdateTableThread extends Thread {
//		public void run() {
//			try {
//				String tableName = (String) tableList.getSelectedValue();
//				// staticQuery.(tableName.toLowerCase());
//				TableInfo table = TableInfoManager.getInstance().getTableInfo(
//						tableName.toLowerCase());
//				factoryData.createAllNewDataLoader().LoadTableData(table);
//			} catch (Exception e) {
//				Logger.error(e,e);
////				MessageBox.showError("错误窗口", e.getCause());
//			}
//		}
//	}
//
//	private class InitTableThread extends Thread {
//		public void run() {
//			try {
//				String tableName = (String) tableList.getSelectedValue();
//				TableInfo table = TableInfoManager.getInstance().getTableInfo(
//						tableName.toLowerCase());
//				factoryData.createAllInitDataLoader().LoadTableData(table);
//				// facade.update("CHECKPOINT DEFRAG;");
//			} catch (UiCacheException e1) {
//				MessageBox.showError("错误窗口", e1.getCause());
//			}
//		}
//	}
//
//	private class DeleteTableThread extends Thread {
//		public void run() {
//			try {
//				String tableName = (String) tableList.getSelectedValue();
//				ConfigManager.getInstance().delete(tableName);
//				refreashTableList();
//				SQLFacade facade = new SQLFacade();
//				facade.update("DROP   TABLE  " + tableName + "; ");
//			} catch (Exception e1) {
//				// MessageBox.showError("错误窗口", e1.getCause());
//			}
//		}
//	}
//
//	private class DeleteDataThread extends Thread {
//		public void run() {
//			try {
//				String tableName = (String) tableList.getSelectedValue();
//				SQLFacade facade = new SQLFacade();
//				facade.update("DELETE  FROM  " + tableName + "; ");
//			} catch (UiCacheException e1) {
//				MessageBox.showError("错误窗口", e1.getCause());
//			}
//		}
//	}
//
//	private class QueryThread extends Thread {
//		Container sc;
//		public QueryThread(Container sc){
//			this.sc=sc;
//		}
//		public void run() {
//			String sql = queryArea.getText().trim();
//			if(sql==null||sql.length()<=0){
//				return;
//			}
//			final long time = System.currentTimeMillis();
//			try {
//				tableModel.executeSQL(sql);
//				
//			} catch (Exception e1) {
//
//			Debug.debug("执行出错", e1);
//			MessageDialog.showErrorDlg(sc, "执行SQL", e1.getMessage());
//			} finally {
//				SwingUtilities.invokeLater(new Runnable() {
//					public void run() {
//						status.stopProgress();
//						status.showMessage("共耗时:"
//								+ (System.currentTimeMillis() - time) + "毫秒");
////						queryBtn.setEnabled(true);
////						clearBtn.setEnabled(false);
//						String sql = queryArea.getText().trim();
//						if(tableModel.issucc){
//							if(sql!=null&&(sql.toLowerCase().trim().startsWith("update"))){
//								MessageDialog.showHintDlg(sc, "更新操作", "执行成功，共更新"+tableModel.updatecount+"行记录");
//							}else if(sql!=null&&(sql.toLowerCase().trim().startsWith("delete"))){
//								MessageDialog.showHintDlg(sc, "删除操作", "执行成功，共删除"+tableModel.updatecount+"行记录");
//							}
//						}
//					}
//				});
//
//			}
//		}
//	}
//
//	private static class RowTableCellRenderer extends DefaultTableCellRenderer {
//		public Component getTableCellRendererComponent(JTable table,
//				Object value, boolean isSelected, boolean hasFocus, int row,
//				int column) {
//			if (row % 2 == 0)
//				setBackground(Color.white); // 设置奇数行底色
//			else if (row % 2 == 1)
//				setBackground(new Color(242, 247, 252)); // 设置偶数行底色
//			return super.getTableCellRendererComponent(table, value,
//					isSelected, hasFocus, row, column);
//		}
//	}
//
//	public boolean isIssucc() {
//		return issucc;
//	}
//
//	public void setIssucc(boolean issucc) {
//		this.issucc = issucc;
//	}
//
//}
