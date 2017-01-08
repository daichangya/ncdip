package nc.ui.pub.pa.config;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;

import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.textfield.UITextType;

/**
 * 有效期设置Panel 创建日期 2006-2-16
 * 
 * @author Huangzg
 */

public class DurationSettingPanel extends UIPanel {
	String name = "";

	UILabel labelExpireBeginDate = null;

	UILabel labelExpireBeginTime = null;

	UILabel labelExpireEndTime = null;

	UICheckBox checkBoxExpireEndDate = null;

	UIRefPane refBeginDate = null;

	UIRefPane refEndDate = null;

	// -------------

	TimeSpinner spinnerEffBeignTime = null;

	TimeSpinner spinnerEffEndTime = null;

	// ----------------
	DurationEventHandler durationEventHandler = new DurationEventHandler();

	private boolean m_isEnable = true;// 全局可编辑性

	public DurationSettingPanel() {
		super();
		initialize();
		initListener();
	}

	public DurationSettingPanel(String name) {
		super();
		this.name = name;
		initialize();
		initListener();
	}

	private void initialize() {
		setName(name);
		setBorder(new javax.swing.border.EtchedBorder());
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		Insets insets = new Insets(2, 4, 2, 4);
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = insets;
		UIPanel blank = new UIPanel();
		blank.setSize(new Dimension(20, 10));
		blank.setPreferredSize(new Dimension(20, 10));
		blank.setMaximumSize(new Dimension(20, 10));
		add(this, blank, constraints, 0, 0, 1, 1);
		add(this, getLabelExpireBeginDate(), constraints, 1, 0, 1, 1);

		Box beginDate = Box.createHorizontalBox();
		beginDate.add(getRefBeginDate());

		// beginDate.add(getTxtFieldExpireBeginDate());
		// beginDate.add(Box.createHorizontalStrut(1));
		// beginDate.add(getBtnExpireBeginDate());
		constraints.weightx = 80;
		add(this, beginDate, constraints, 2, 0, 1, 1);

		constraints.weightx = 0;
		add(this, getChkBoxExpireEndDate(), constraints, 3, 0, 1, 1);
		Box endDate = Box.createHorizontalBox();
		endDate.add(getRefEndDate());

		constraints.weightx = 100;
		add(this, endDate, constraints, 4, 0, 1, 1);

		constraints.weightx = 0;
		UIPanel blank2 = new UIPanel();
		blank2.setSize(new Dimension(20, 10));
		blank2.setPreferredSize(new Dimension(20, 10));
		blank2.setMaximumSize(new Dimension(20, 10));
		add(this, blank2, constraints, 0, 1, 1, 1);
		add(this, getLabelExpireBeginTime(), constraints, 1, 1, 1, 1);
		constraints.weightx = 80;
		add(this, getSpnEffBeignTime(), constraints, 2, 1, 1, 1);

		constraints.weightx = 0;
		add(this, getLabelExpireEndTime(), constraints, 3, 1, 1, 1);
		constraints.weightx = 100;
		add(this, getSpnEffEndTime(), constraints, 4, 1, 1, 1);
		setBorder(BorderFactory.createTitledBorder(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"101502", "UPP101502-000024")/* @res""有效期"" */));

	}

	private void initListener() {
		getChkBoxExpireEndDate().addActionListener(durationEventHandler);
	}

	public UIRefPane getRefBeginDate() {
		if (refBeginDate == null) {
			refBeginDate = new UIRefPane();
			refBeginDate.setRefNodeName("日历");
			refBeginDate.setTextType(UITextType.TextDate);
		}
		return refBeginDate;
	}

	public UIRefPane getRefEndDate() {
		if (refEndDate == null) {
			refEndDate = new UIRefPane();
			refEndDate.setRefNodeName("日历");
			refEndDate.setTextType(UITextType.TextDate);
		}
		return refEndDate;
	}

	private UILabel getLabelExpireBeginDate() {
		if (labelExpireBeginDate == null) {
			labelExpireBeginDate = new UILabel();
			labelExpireBeginDate.setName("m_lblExpireBeginDate");
			labelExpireBeginDate.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("101502",
					"UPP101502-000011")/* @res""开始日期:"" */);
		}
		return labelExpireBeginDate;
	}

	public UICheckBox getChkBoxExpireEndDate() {
		if (checkBoxExpireEndDate == null) {
			checkBoxExpireEndDate = new UICheckBox();
			checkBoxExpireEndDate.setName("m_chkExpireEndDate");
			checkBoxExpireEndDate.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("101502",
					"UPP101502-000002")/* @res""终止日期:"" */);
		}
		return checkBoxExpireEndDate;
	}

	private UILabel getLabelExpireBeginTime() {
		if (labelExpireBeginTime == null) {
			labelExpireBeginTime = new UILabel();
			labelExpireBeginTime.setName("m_lblExpireBeginTime");
			labelExpireBeginTime.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("101502",
					"UPP101502-000012")/* @res""开始时间:"" */);
		}
		return labelExpireBeginTime;
	}

	public UILabel getLabelExpireEndTime() {
		if (labelExpireEndTime == null) {
			labelExpireEndTime = new UILabel();
			labelExpireEndTime.setName("m_lblExpireEndTime");
			labelExpireEndTime.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("101502",
					"UPP101502-000013")/* @res""终止时间:"" */);
		}
		return labelExpireEndTime;
	}

	// ----------------------------------

	// 增加组件
	public void add(UIPanel panel, JComponent compo, GridBagConstraints constraints, int x, int y,
			int w, int h) {
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridheight = h;
		constraints.gridwidth = w;
		panel.add(compo, constraints);
	}

	// ---

	class DurationEventHandler implements ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == getChkBoxExpireEndDate() && m_isEnable)
				onCheckBoxExpire(e);
		}
	}

	public void onCheckBoxExpire(ActionEvent e) {
		boolean bChecked = getChkBoxExpireEndDate().isSelected();
		getRefEndDate().setEnabled(bChecked);
		getSpnEffBeignTime().setEnabled(bChecked);
		getSpnEffEndTime().setEnabled(bChecked);
	}

	public TimeSpinner getSpnEffBeignTime() {
		if (spinnerEffBeignTime == null) {
			spinnerEffBeignTime = new TimeSpinner();
		}
		return spinnerEffBeignTime;
	}

	public TimeSpinner getSpnEffEndTime() {
		if (spinnerEffEndTime == null) {
			spinnerEffEndTime = new TimeSpinner();
		}
		return spinnerEffEndTime;
	}

	public void setEnabled(boolean flag) {
		m_isEnable = flag;
		getChkBoxExpireEndDate().setEnabled(flag);
		getLabelExpireBeginDate().setEnabled(flag);
		getLabelExpireBeginTime().setEnabled(flag);
		getLabelExpireEndTime().setEnabled(flag);
		getRefBeginDate().setEnabled(flag);
		getRefEndDate().setEnabled(flag);
		getSpnEffBeignTime().setEnabled(flag);
		getSpnEffEndTime().setEnabled(flag);
	}

}
