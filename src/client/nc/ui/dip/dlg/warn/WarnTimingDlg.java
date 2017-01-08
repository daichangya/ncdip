package nc.ui.dip.dlg.warn;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.component.ButtonPanel;
import nc.vo.pub.pa.AlertTimeConfig;

/**
 *  定时时间设置DLG　<br>
 *  其由通用定时设置Panel，及含Ok/Cancel的ButtonPanel组成．
 *  @author Huangzg 2006-2-17
 *  @since NC5.0
 */
public class WarnTimingDlg extends UIDialog implements ActionListener {

	/**
	 * 时间配置 面板
	 */
	private WarnTimingSettingPanel timingSettingPane = null;

	/**
	 * 按钮 面板
	 */
	private ButtonPanel pnlButton = null;

	/**
	 * @param parent
	 */
	public WarnTimingDlg(Container parent) {
		super(parent);
		initialize();
		initConnections();
	}

	/**
	 * @param parent
	 */
	public WarnTimingDlg(java.awt.Dialog parent) {
		super(parent);
		initialize();
		initConnections();
	}

	private void initialize() {
		setName("EditAlartTimeFrame");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("101502", "UPP101502-000031")/* @res""编辑发生时间"" */);
		setSize(560, 420);
		setResizable(true);
		setContentPane(getContentPanel());

		initEnableState();
	}

	/**
	 * 获得主面板
	 * @return
	 */
	private UIPanel getContentPanel() {
		UIPanel contentPanel = new UIPanel();
		contentPanel.setLayout(new BorderLayout());
		contentPanel.add(getTimeSettingPanel(), BorderLayout.CENTER);
		contentPanel.add(getpnlButton(), BorderLayout.SOUTH);
		return contentPanel;
	}

	private void initConnections() {
		getpnlButton().getBtnOK().addActionListener(this);
		getpnlButton().getBtnCancel().addActionListener(this);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				closeCancel();
			};
		});
	}

	private void initEnableState() {
		//	getTimeSettingPanel().getPnlDaily().getTxtFieldRunCycle().setEditable(false);
	}

	public WarnTimingSettingPanel getTimeSettingPanel() {
		if (timingSettingPane == null) {
			timingSettingPane = new WarnTimingSettingPanel();
		}
		return timingSettingPane;
	}

	public ButtonPanel getpnlButton() {
		if (pnlButton == null) {
			pnlButton = new ButtonPanel();
		}
		return pnlButton;
	}

	public void onBtnOK(ActionEvent e) {
		if (getTimeSettingPanel().onClickOK(e)) {			
			this.dispose();
			this.closeOK();
		} else
			return;
	}

	public void onBtnCancel(ActionEvent e) {
		this.dispose();
		this.closeCancel();
	}

	//---下面为对外的接口

	/**
	 * 将 AlartTimeConfig 的值设置到界面上，本对话框类并不维护一个 AlartTimeConfig 对象，而是直接设置在界面上
	 */
	public void setAlertTime(AlertTimeConfig timeConfig) {
		getTimeSettingPanel().setAlertTime(timeConfig);
	}

	/**
	 * 从界面上得到时间设置值.<br>
	 * 以一个AlertTimeConfig的形式返回
	 * 
	 */
	public AlertTimeConfig getAlertTime() {
		return getTimeSettingPanel().getAlertTime();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getpnlButton().getBtnOK())
			onBtnOK(e);
		if (e.getSource() == getpnlButton().getBtnCancel())
			onBtnCancel(e);
	}
	public static void main(String[] args) {
		WarnTimingDlg dlg=new WarnTimingDlg(new JFrame());
		dlg.showModal();
	}
}
