package nc.ui.dip.dlg.warn;

import java.awt.BorderLayout;

import nc.bs.pub.pa.PaConstant;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollBar;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.pa.config.DurationSettingPanel;
import nc.ui.pub.pa.config.FrequencySettingPanel;
import nc.ui.pub.pa.config.OneDaySettingPanel;
import nc.ui.pub.pa.config.PreAlertUIsTools;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFTime;
import nc.vo.pub.pa.AlertTimeConfig;
import nc.vo.pub.pa.TimingSettingVO;
import nc.vo.uap.scheduler.TimeConfigVO;

/**
 * ͨ�ö�ʱ����Panel<br>
 * �����:����Ƶ�ʣ�һ���ڣ�����Ч������Panel.
 * 
 * @author Huangzg 2006-2-16
 * @since NC5.0
 */
public class WarnTimingSettingPanel extends UIPanel {
	// ����Ƶ��Panel
	private FrequencySettingPanel frequencyPanel = null;

	// һ��������Panel
	private OneDaySettingPanel pnlDaily = null;

	// ��Ч������Panel
	private DurationSettingPanel pnlDuration = null;

	public WarnTimingSettingPanel() {
		super();
		initialize();
		// SwingUtil.setContainerEnabled(this,false);

	}

	public FrequencySettingPanel getPnlFrequency() {
		if (frequencyPanel == null) {
			frequencyPanel = new FrequencySettingPanel();
		}
		return frequencyPanel;
	}

	// һ���ڣ�����Panel
	public OneDaySettingPanel getPnlDaily() {
		if (pnlDaily == null) {
			pnlDaily = new OneDaySettingPanel();
		}
		return pnlDaily;
	}

	public DurationSettingPanel getPnlDuration() {
		if (pnlDuration == null) {
			pnlDuration = new DurationSettingPanel();
		}
		return pnlDuration;
	}

	/**
	 * ��ʼ���ࡣ
	 */
	private void initialize() {
		setLayout(new BorderLayout());
		add(getPnlFrequency(), BorderLayout.NORTH);
		add(getPnlDaily(), BorderLayout.CENTER);
		add(getPnlDuration(), BorderLayout.SOUTH);
	}

	/**
	 * У��һ������ģ����ʾ΢����ť���ݵ��ı��������
	 */
	public static void spb_validateTextField(UITextField textField, UIScrollBar scrollBar) {
		PreAlertUIsTools.scb_validateTextField(textField, scrollBar);
	}

	/**
	 * �� AlartTimeConfig ��ֵ���õ������ϣ����Ի����ಢ��ά��һ�� AlartTimeConfig ���󣬶���ֱ�������ڽ�����
	 */
	public void setAlertTime(AlertTimeConfig timeConfig) {
		// --------------------------���÷���Ƶ�ʵ�Panel--------------------
		// **����Ƶ����������**
		int nOccurType = timeConfig.getOccurType();
		if (nOccurType == AlertTimeConfig.OCCURS_DAILY) {
			getPnlFrequency().getm_rdoDaily().setSelected(true);
		} else if (nOccurType == AlertTimeConfig.OCCURS_WEEKLY) {
			getPnlFrequency().getm_rdoWeekly().setSelected(true);
		} else {
			getPnlFrequency().getm_rdoMonthl().setSelected(true);
		}
		// **��������**
		int nOccurInterval = timeConfig.getOccurInterval();
		boolean[] bOccurSelected = timeConfig.getOccurSelected();
		if (nOccurType == AlertTimeConfig.OCCURS_WEEKLY) {
			// ���������ڵļ��
			getPnlFrequency().getSpinWeekly().setValue(new Integer(nOccurInterval));
			// ���������ڵ�ѡ����
			UICheckBox[] chksSelected = new UICheckBox[] { getPnlFrequency().getm_chkSun(),//һ������sunday����ǰ
					getPnlFrequency().getm_chkMon(), getPnlFrequency().getm_chkTue(),
					getPnlFrequency().getm_chkWed(), getPnlFrequency().getm_chkThur(),
					getPnlFrequency().getm_chkFri(), getPnlFrequency().getm_chkSat() };
			for (int i = 0; i < bOccurSelected.length; i++)
				chksSelected[i].setSelected(bOccurSelected[i]);
		} else if (nOccurType == AlertTimeConfig.OCCURS_MONTHLY) {
			// �������ڵ�ʱ����
			getPnlFrequency().getSpinMonthly().setValue(new Integer(nOccurInterval));
			UICheckBox[] chksSelected = new UICheckBox[] { getPnlFrequency().getJCheckBox1(),
					getPnlFrequency().getJCheckBox2(), getPnlFrequency().getJCheckBox3(),
					getPnlFrequency().getJCheckBox4(), getPnlFrequency().getJCheckBox5(),
					getPnlFrequency().getJCheckBox6(), getPnlFrequency().getJCheckBox7(),
					getPnlFrequency().getJCheckBox8(), getPnlFrequency().getJCheckBox9(),
					getPnlFrequency().getJCheckBox10(), getPnlFrequency().getJCheckBox11(),
					getPnlFrequency().getJCheckBox12(), getPnlFrequency().getJCheckBox13(),
					getPnlFrequency().getJCheckBox14(), getPnlFrequency().getJCheckBox15(),
					getPnlFrequency().getJCheckBox16(), getPnlFrequency().getJCheckBox17(),
					getPnlFrequency().getJCheckBox18(), getPnlFrequency().getJCheckBox19(),
					getPnlFrequency().getJCheckBox20(), getPnlFrequency().getJCheckBox21(),
					getPnlFrequency().getJCheckBox22(), getPnlFrequency().getJCheckBox23(),
					getPnlFrequency().getJCheckBox24(), getPnlFrequency().getJCheckBox25(),
					getPnlFrequency().getJCheckBox26(), getPnlFrequency().getJCheckBox27(),
					getPnlFrequency().getJCheckBox28(), getPnlFrequency().getJCheckBox29(),
					getPnlFrequency().getJCheckBox30(), getPnlFrequency().getJCheckBox31() };
			for (int i = 0; i < chksSelected.length; i++) {
				chksSelected[i].setSelected(bOccurSelected[i]);
			}
		}
		// �������
		getPnlFrequency().getm_pnlDailyDecription().setVisible(
				getPnlFrequency().getm_rdoDaily().isSelected());
		getPnlFrequency().getm_pnlWeekly().setVisible(getPnlFrequency().getm_rdoWeekly().isSelected());
		getPnlFrequency().getm_pnlMonthly().setVisible(getPnlFrequency().getm_rdoMonthl().isSelected());
		// --------------����ÿ��Panel�Ķ���-----------------------------------------/
		// ����ÿ���ڲ���ʱ��
		boolean bDailyRunOnce = timeConfig.getDailyRunOnce();
		if (bDailyRunOnce == true) {
			getPnlDaily().getRbtnRunOnce().setSelected(true);
			getPnlDaily().makeRunOnce();
		} else {
			getPnlDaily().getRbtnRunCycle().setSelected(true);
			getPnlDaily().makeRunCycle();
		}
		String strDailyRunOnceTime = timeConfig.getDailyRunOnceTime() + "";
		getPnlDaily().getSpnRunonce().setText(strDailyRunOnceTime);
		int nRunCycleInterval = timeConfig.getDailyRunCycleInterval();		
		getPnlDaily().getSpinnerPer().setValue(new Integer(nRunCycleInterval));//Added by guowl
		int nRunCycleUint = timeConfig.getDailyRunCycleUnit();
		getPnlDaily().getm_cboUnit().setSelectedIndex(nRunCycleUint);
		String strDailyRunCycleBeginTime = timeConfig.getDailyRunCycleBeginTime() + "";
		getPnlDaily().getSpnBeginTime().setText(strDailyRunCycleBeginTime);
		String strDailyRunCycleEndTime = timeConfig.getDailyRunCycleEndTime() + "";
		getPnlDaily().getSpnEndTime().setText(strDailyRunCycleEndTime);
		// TODO:�б�Ҫ��
		// �������
		if (getPnlDaily().getRbtnRunCycle().isSelected() == false) {
			getPnlDaily().makeRunOnce();
		} else {
			getPnlDaily().makeRunCycle();
		}
		// ----------------������Ч��Panle-------------------------------------------
		// ������Ч��
		String strExpireBeginDate = timeConfig.getExpireBeginDate().toString();
		getPnlDuration().getRefBeginDate().setText(strExpireBeginDate);
		String strExpireBeginTime = timeConfig.getExpireBeginTime().toString();
		getPnlDuration().getSpnEffBeignTime().setText(strExpireBeginTime);
		String strExpireEndDate = timeConfig.getExpireEndDate() == null ? null : timeConfig
				.getExpireEndDate().toString();
		String strExpireEndTime = timeConfig.getExpireEndTime() == null ? null : timeConfig
				.getExpireEndTime().toString();
		if (strExpireEndDate != null && strExpireEndTime != null && strExpireEndDate.length() > 0
				&& strExpireEndTime.length() > 0) {
			strExpireEndDate = timeConfig.getExpireEndDate().toString();
			getPnlDuration().getRefEndDate().setText(strExpireEndDate);
			strExpireEndTime = timeConfig.getExpireEndTime().toString();
			getPnlDuration().getSpnEffEndTime().setText(strExpireEndTime);
			getPnlDuration().getChkBoxExpireEndDate().setSelected(true);
			
			// getPnlDuration().getRefEndDate().setEnabled(true);
			// getPnlDuration().getSpnEffEndTime().setEnabled(true);
		} else {
			getPnlDuration().getChkBoxExpireEndDate().setSelected(false);
			getPnlDuration().getRefBeginDate().setEnabled(false);
			getPnlDuration().getSpnEffBeignTime().setEnabled(false);
			getPnlDuration().getRefEndDate().setEnabled(false);
			getPnlDuration().getSpnEffEndTime().setEnabled(false);
		}}

	public AlertTimeConfig getAlertTime() {
		AlertTimeConfig atc = new AlertTimeConfig();
		// ----------------�ӷ���Ƶ��Panel�õ�ֵ-------------------
		// ���÷�������
		if (getPnlFrequency().getm_rdoDaily().isSelected() == true) {
			atc.setOccurType(AlertTimeConfig.OCCURS_DAILY);
		} else if (getPnlFrequency().getm_rdoWeekly().isSelected() == true) {
			atc.setOccurType(AlertTimeConfig.OCCURS_WEEKLY);
		} else {
			atc.setOccurType(AlertTimeConfig.OCCURS_MONTHLY);
		}
		// +++++++++++++++++
		// ���������ڵķ�����
		boolean[] wk = null;
		if (atc.getOccurType() == AlertTimeConfig.OCCURS_WEEKLY) {
			atc.setOccurInterval(Integer
					.parseInt(getPnlFrequency().getSpinWeekly().getValue().toString()));
			// ���������ڵ�ѡ����
			wk = new boolean[7];
			UICheckBox[] chksSelected = new UICheckBox[] { getPnlFrequency().getm_chkSun(),
					getPnlFrequency().getm_chkMon(), getPnlFrequency().getm_chkTue(),
					getPnlFrequency().getm_chkWed(), getPnlFrequency().getm_chkThur(),
					getPnlFrequency().getm_chkFri(), getPnlFrequency().getm_chkSat() };
			for (int i = 0; i < chksSelected.length; i++)
				wk[i] = chksSelected[i].isSelected();
		} else if (atc.getOccurType() == AlertTimeConfig.OCCURS_MONTHLY) {
			atc.setOccurInterval(Integer.parseInt(getPnlFrequency().getSpinMonthly().getValue()
					.toString()));
			// �������ڵ�ʱ����
			wk = new boolean[31];
			UICheckBox[] chksSelected = new UICheckBox[] { getPnlFrequency().getJCheckBox1(),
					getPnlFrequency().getJCheckBox2(), getPnlFrequency().getJCheckBox3(),
					getPnlFrequency().getJCheckBox4(), getPnlFrequency().getJCheckBox5(),
					getPnlFrequency().getJCheckBox6(), getPnlFrequency().getJCheckBox7(),
					getPnlFrequency().getJCheckBox8(), getPnlFrequency().getJCheckBox9(),
					getPnlFrequency().getJCheckBox10(), getPnlFrequency().getJCheckBox11(),
					getPnlFrequency().getJCheckBox12(), getPnlFrequency().getJCheckBox13(),
					getPnlFrequency().getJCheckBox14(), getPnlFrequency().getJCheckBox15(),
					getPnlFrequency().getJCheckBox16(), getPnlFrequency().getJCheckBox17(),
					getPnlFrequency().getJCheckBox18(), getPnlFrequency().getJCheckBox19(),
					getPnlFrequency().getJCheckBox20(), getPnlFrequency().getJCheckBox21(),
					getPnlFrequency().getJCheckBox22(), getPnlFrequency().getJCheckBox23(),
					getPnlFrequency().getJCheckBox24(), getPnlFrequency().getJCheckBox25(),
					getPnlFrequency().getJCheckBox26(), getPnlFrequency().getJCheckBox27(),
					getPnlFrequency().getJCheckBox28(), getPnlFrequency().getJCheckBox29(),
					getPnlFrequency().getJCheckBox30(), getPnlFrequency().getJCheckBox31() };
			for (int i = 0; i < chksSelected.length; i++) {
				wk[i] = chksSelected[i].isSelected();
			}
		}
		atc.setOccurSelected(wk);
		// ++++++++++++++++++++++++++++
		// --------------------------��һ����panel�õ�ֵ--------------------------------------
		// ����ÿ���ڲ���ʱ��
		atc.setDailyRunOnce(getPnlDaily().getRbtnRunOnce().isSelected());
		atc.setDailyRunOnceTime(new UFTime(getPnlDaily().getSpnRunonce().getText()));
		//Modified by guowl
		atc.setDailyRunCycleInterrval(Integer.parseInt(getPnlDaily().getSpinnerPer().getValue().toString()));
		atc.setDailyRunCycleUnit(getPnlDaily().getm_cboUnit().getSelectedIndex());
		atc.setDailyRunCycleBeginTime(new UFTime(getPnlDaily().getSpnBeginTime().getText()));
		atc.setDailyRunCycleEndTime(new UFTime(getPnlDaily().getSpnEndTime().getText()));
		// ++++++++++++++++++++++++++++
		// --------------------------����Ч��panel�õ�ֵ--------------------------------------
		// ������Ч��
		atc.setExpireBeginDate(new UFDate(getPnlDuration().getRefBeginDate().getText()));
		atc.setExpireBeginTime(new UFTime(getPnlDuration().getSpnEffBeignTime().getText()));
		if (getPnlDuration().getChkBoxExpireEndDate().isSelected() == true) {
			atc.setExpireEndDate(new UFDate(getPnlDuration().getRefEndDate().getText()));
			atc.setExpireEndTime(new UFTime(getPnlDuration().getSpnEffEndTime().getText()));
		} else {
			atc.setExpireEndDate(null);
			atc.setExpireEndTime(null);
		}
		return atc;}

	/**
	 * ���ø�ʱ������Panel��DLG�ر�ʱ����õ��ø÷�����ȥ����У�飮<br>
	 * У��ɹ�������true. ��֮����false.
	 * 
	 * @return
	 */
	public boolean onClickOK(java.awt.event.ActionEvent arg1) {
		// �Խ����ϵ�ֵ����һ��У��
		return verifyInput();
	}

	private boolean verifyInput() {

		if (getPnlFrequency().getm_rdoWeekly().isSelected()) {// ���ѡ�е����ܡ�
			if (getPnlFrequency().getm_chkFri().isSelected()
					|| getPnlFrequency().getm_chkMon().isSelected()
					|| getPnlFrequency().getm_chkSat().isSelected()
					|| getPnlFrequency().getm_chkSun().isSelected()
					|| getPnlFrequency().getm_chkThur().isSelected()
					|| getPnlFrequency().getm_chkTue().isSelected()
					|| getPnlFrequency().getm_chkWed().isSelected()) {
			} else {
				MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("101502",
						"UPP101502-000034")/* @res""������ʾ"" */, NCLangRes.getInstance().getStrByID("101502",
						"UPP101502-000297")/* "������Ƶ��Ϊ��/��ʱ,��������ָ��һ��������" */);
				return false;
			}
		}
		if (getPnlFrequency().getm_rdoMonthl().isSelected()) {// ���ѡ�е�����
			if (getPnlFrequency().getJCheckBox1().isSelected()
					|| getPnlFrequency().getJCheckBox2().isSelected()
					|| getPnlFrequency().getJCheckBox3().isSelected()
					|| getPnlFrequency().getJCheckBox4().isSelected()
					|| getPnlFrequency().getJCheckBox5().isSelected()
					|| getPnlFrequency().getJCheckBox6().isSelected()
					|| getPnlFrequency().getJCheckBox7().isSelected()
					|| getPnlFrequency().getJCheckBox8().isSelected()
					|| getPnlFrequency().getJCheckBox9().isSelected()
					|| getPnlFrequency().getJCheckBox10().isSelected()
					|| getPnlFrequency().getJCheckBox11().isSelected()
					|| getPnlFrequency().getJCheckBox12().isSelected()
					|| getPnlFrequency().getJCheckBox13().isSelected()
					|| getPnlFrequency().getJCheckBox14().isSelected()
					|| getPnlFrequency().getJCheckBox15().isSelected()
					|| getPnlFrequency().getJCheckBox16().isSelected()
					|| getPnlFrequency().getJCheckBox17().isSelected()
					|| getPnlFrequency().getJCheckBox18().isSelected()
					|| getPnlFrequency().getJCheckBox19().isSelected()
					|| getPnlFrequency().getJCheckBox20().isSelected()
					|| getPnlFrequency().getJCheckBox21().isSelected()
					|| getPnlFrequency().getJCheckBox22().isSelected()
					|| getPnlFrequency().getJCheckBox23().isSelected()
					|| getPnlFrequency().getJCheckBox24().isSelected()
					|| getPnlFrequency().getJCheckBox25().isSelected()
					|| getPnlFrequency().getJCheckBox26().isSelected()
					|| getPnlFrequency().getJCheckBox27().isSelected()
					|| getPnlFrequency().getJCheckBox28().isSelected()
					|| getPnlFrequency().getJCheckBox29().isSelected()
					|| getPnlFrequency().getJCheckBox30().isSelected()
					|| getPnlFrequency().getJCheckBox31().isSelected()) {// ��������
			} else {
				MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("101502",
						"UPP101502-000034")/* @res""������ʾ"" */, NCLangRes.getInstance().getStrByID("101502",
						"UPP101502-000297")/* "������Ƶ��Ϊ��/��ʱ,��������ָ��һ��������" */);
				return false;
			}
		}
		if (getPnlDuration().getChkBoxExpireEndDate().isSelected()) { return checkBeignEndDate(); }
		return true;
	}

	/**
	 * ���ڽ����Ͽ�ʼ�ͽ������ڽ���У��,
	 */
	public boolean checkBeignEndDate() {
		UFDate expireBeginDate = new UFDate(getPnlDuration().getRefBeginDate().getText());
		if (expireBeginDate.toString().trim().length() == 0) {// ��ʼ���ڲ���Ϊ��
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("101502",
					"UPP101502-000034")/* @res""������ʾ"" */, NCLangRes.getInstance().getStrByID("101502",
					"UPP101502-000293")/* @res""��ʼ���ڲ���Ϊ��!"" */);
			return false;
		}
		UFTime expireBeginTime = new UFTime(getPnlDuration().getSpnEffBeignTime().getText());

		if (getPnlDuration().getChkBoxExpireEndDate().isSelected()
				&& getPnlDuration().getRefEndDate().getText().trim().length() == 0) {
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("101502",
					"UPP101502-000034")/* @res""������ʾ"" */, NCLangRes.getInstance().getStrByID("101502",
					"UPP101502-000294")/* @res""��ֹ����������,�����䲻��Ϊ��!"" */);
			return false;
		}
		UFDate expireEndDate = new UFDate(getPnlDuration().getRefEndDate().getText());

		UFTime expireEndTime = new UFTime(getPnlDuration().getSpnEffEndTime().getText());
		long beginDateTime = new UFDateTime(expireBeginDate, expireBeginTime).getMillis();
		long endDateTime = new UFDateTime(expireEndDate, expireEndTime).getMillis();
		if (beginDateTime > endDateTime) {
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("101502",
					"UPP101502-000034")/* @res""������ʾ"" */, NCLangRes.getInstance().getStrByID("101502",
					"UPP101502-000035")/* @res""��Ч�ڿ�ʼʱ�������Ч�ڽ���ʱ�䣬����������!"" */);
			return false;
		} else {
			return true;
		}
	}

	public TimingSettingVO getTimingSettingVO() {
		AlertTimeConfig alertTimeconfig = this.getAlertTime();
		return PaConstant.transAlertTimeConfig2TimingSettingVO(alertTimeconfig);
	}

	public void setTimingSettingVO(TimingSettingVO timeVO) {
		AlertTimeConfig alertTimeconfig = PaConstant.transTimingSettingVO2AlertTimeConfig(timeVO);
		this.setAlertTime(alertTimeconfig);
	}

	public TimeConfigVO getTimeConfigVO() {
		TimingSettingVO timesettingvo = getTimingSettingVO();
		return PaConstant.transTimingSettingVO2TimeConfigVO(timesettingvo);
	}

	public void setEnabled(boolean flag) {
		getPnlFrequency().setEnabled(flag);
		getPnlDaily().setEnabled(flag);
		getPnlDuration().setEnabled(flag);
	}
}
