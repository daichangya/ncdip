package nc.ui.dip.datachange.ref;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JPanel;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.view.VDipCrerefVO;
/**
 * ������ǵ���һ���Ի�����Ҫ��������getRet()���������ͨ��������������Է���ѡ����ǵڼ��ַ�ʽ��ע�⹹�졣
 * ����ֱ����������࣬Ȼ����Կ������еĽ��
 */
public class DataRefDLG extends UIDialog {
	private boolean isOK=false;
	
	private static final long serialVersionUID = 1L;
	private UIButton boOK = null;
	private UIButton boCancel = null;
	private UIPanel panelBackground = null;
	private UIPanel panelButtons = null;
	private JPanel mipanel=null;
	private String pksys;
	private VDipCrerefVO vo;
	private String pkdipc;
	EventHandler eventHandler = new EventHandler();
	//��ǰ��ı���
	String thetitle;
	public DataRefDLG(Container owner,String pksys,String thetitle,String pkdipc){
		super(owner);
		this.pksys=pksys;
		this.thetitle=thetitle;
		this.pkdipc=pkdipc;
		setSize(950, 470);// ����DLG��С
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
			mipanel=new DataRefPanel(pksys,pkdipc);
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
//				panelBackground.add(getPanelButton(), "North");
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
		final DataRefDLG this$0;
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == getBoOK()){
				DataRefPanel dfp=(DataRefPanel) this$0.getMipanel();
				int row=dfp.getJpField().getBodyTable().getSelectedRow();
				VDipCrerefVO vos= (VDipCrerefVO) dfp.getJpField().getBodyBillModel().getBodyValueRowVO(row, VDipCrerefVO.class.getName());
				setVo(vos);
				onOK();
			}else if (e.getSource() == getBoCancel()){
				closeCancel();
			}
		};
		EventHandler() {
			this$0 = DataRefDLG.this;
			// super();
		}

	}
	public void onOK() {
		isOK=true;
		this.close();
	}



	public VDipCrerefVO getVo() {
		return vo;
	}



	public void setVo(VDipCrerefVO vo) {
		this.vo = vo;
	}
}
