package nc.ui.dip.dlg.warntime;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JPanel;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.warningset.DipWarningsetDayTimeVO;
/**
 * ������ǵ���һ���Ի�����Ҫ��������getRet()���������ͨ��������������Է���ѡ����ǵڼ��ַ�ʽ��ע�⹹�졣
 * ����ֱ����������࣬Ȼ����Կ������еĽ��
 */
public class WarTimeDLG extends UIDialog {
	private boolean isOK=false;
	
	private static final long serialVersionUID = 1L;
	private UIButton boOK = null;
	private UIButton boCancel = null;
	private UIPanel panelBackground = null;
	private UIPanel panelButtons = null;
	private JPanel mipanel=null;
	private DipWarningsetDayTimeVO dtvo;
	
	EventHandler eventHandler = new EventHandler();
	//��ǰ��ı���
	String thetitle="Ԥ��ʱ������";
	
	/**
	*��Ȩ��Ϣ���̼ѿƼ�
	*���ߣ�   ���޶�
	*�汾��   
	*������   ����һ��ѯ�ʵĵ�ѡ�Ŀ�dialog
	*�������ڣ�2011-04-14
	*����
	*@param   owner	��ǰ��ClientUI
	*@param   title	������ı���
	*@param   message	ѯ����Ϣ
	*@param   ss����   ��ѡ�Ŀ���ѡ��
	 * @throws Exception 
	*/
	public WarTimeDLG(Container owner) {
		super(owner);
		setSize(500, 420);// ����DLG��С
		setContentPane(getPanelBackground());
		try {
			initConnections();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setResizable(true);
	}

	public WarTimeDLG(Container owner,DipWarningsetDayTimeVO dtvo){
		super(owner);
		this.dtvo=dtvo;
		setSize(580, 420);// ����DLG��С
		setContentPane(getPanelBackground());
		try {
			initConnections();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setResizable(true);
	}
	
	public JPanel getMipanel() {
		if(SJUtil.isNull(mipanel)){
			mipanel=new WarningTime(dtvo);
		}
		return mipanel;
	}
	public void setMipanel(JPanel mipanel) {
		this.mipanel = mipanel;
	}
	/**
	*��Ȩ��Ϣ���̼ѿƼ�
	*���ߣ�   ���޶�
	*�汾��   
	*������   ����ѡ����ǵڼ��ַ�ʽ
	*�������ڣ�2011-04-14
	*����
	*@return ����ѡ����ǵڼ��ַ�ʽ
	 * @throws Exception 
	*/
	public int getRes(){
		if(!isOK){
			return -1;
		}
		return 0;
	}
	/**
	 * ����DLG����
	 */
	@Override
	public String getTitle() {
		return thetitle;
	}
	/**
	 * ����DLGPanel
	 * @return
	 */
	private UIPanel getPanelBackground() {
		if (panelBackground == null) {
			try {
				panelBackground = new UIPanel();
				panelBackground.setName("PanelBackground");
				panelBackground.setLayout(new BorderLayout());
//				panelBackground.add(getNothPanel(),"North");
				panelBackground.add(getMipanel(), "Center");
				panelBackground.add(getPanelButton(), "South");
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return panelBackground;
	}
	/**
	 * ��ť[ȷ��]
	 * 
	 * @return
	 */
	private UIButton getBoOK() {
		if (boOK == null) {
			try {
				boOK = new UIButton();
				boOK.setName("m_btOK");
				boOK.setText("ȷ��");
			} catch (java.lang.Throwable e) {
				e.printStackTrace();
			}
		}
		return boOK;
	}
	/**
	 * ��ť[ȡ��]
	 * 
	 * @return
	 */
	private UIButton getBoCancel() {
		if (boCancel == null) {
			try {
				boCancel = new UIButton();
				boCancel.setName("m_btCancel");
				boCancel.setText("ȡ��");
			} catch (java.lang.Throwable e) {
				e.printStackTrace();
			}
		}
		return boCancel;
	}

	/**
	 * DLG��ť��
	 * 
	 * @return
	 */
	private UIPanel getPanelButton() {
		if (panelButtons == null) {
			try {
				panelButtons = new UIPanel();
				panelButtons.setName("PanelButton");
				panelButtons.setLayout(new FlowLayout());
				getPanelButton().add(getBoOK(), getBoOK().getName());
				getPanelButton().add(getBoCancel(), getBoCancel().getName());

			} catch (java.lang.Throwable e) {
				e.printStackTrace();
			}
		}
		return panelButtons;
	}
	private void initConnections() throws java.lang.Exception {
		getBoOK().addActionListener(eventHandler);
		getBoCancel().addActionListener(eventHandler);
	}
	/**
	 * ��ť�����ڲ���
	 * 
	 * @author jieely 2008-8-21
	 */
	class EventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == getBoOK())
				onOK();
			if (e.getSource() == getBoCancel())
				closeCancel();
		};

	}
	public void onOK() {
		isOK=true;
		this.close();
	}
	public DipWarningsetDayTimeVO getDTVO(){
		if(isOK){
			return ((WarningTime)getMipanel()).getDTVO();
		}else{
			return null;
		}
	}
	public static void main (String[] ar){
		JPanel jp=new JPanel();
		WarTimeDLG a=new WarTimeDLG(jp);
		a.showModal();
//		System.out.println(a.getRes()+"");
	}
}
