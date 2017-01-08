package nc.ui.dip.dataproce.dlg;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JPanel;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.control.ControlVO;
import nc.vo.dip.dataproce.DipDataproceHVO;

public class ControlDlg extends UIDialog{
	private boolean isOK=false;

	private static final long serialVersionUID = 1L;
	private UIButton boOK = null;
	private UIButton boCancel = null;
	private UIPanel panelBackground = null;
	private UIPanel panelButtons = null;
	private JPanel mipanel=null;
//	private DipDataproceHVO hvo;
	private ControlVO cvo;
	private ControlVO[] vos;
	private String pk_bus;
	private String pk;
	
	String fpk;
	String ywlx;
	String pktabnames;
	
	EventHandler eventHandler = new EventHandler();
	//��ǰ��ı���
	String thetitle="����";
	public ControlDlg(Container owner,String fpk,String ywlx,String pktabnames){
		super(owner);
		this.fpk=fpk;
		this.ywlx=ywlx;
		this.pktabnames=pktabnames;
		setSize(800, 470);// ����DLG��С
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
			mipanel=new ControlPanel(fpk,ywlx,pktabnames);
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
		final ControlDlg this$0;
		public void actionPerformed(java.awt.event.ActionEvent e) {
			ControlPanel cp=(ControlPanel) this$0.getMipanel();
			ControlVO[] ctlvo=(ControlVO[]) cp.getJpField().getBodyBillModel("dip_control").getBodyValueVOs(ControlVO.class.getName());
			if (e.getSource() == getBoOK()){
				for(int i=0;i<ctlvo.length;i++){
					if(ctlvo[i].getKzclzt()==null || "".equals(ctlvo[i].getKzclzt())){
						MessageDialog.showErrorDlg(null, "����", (i+1)+"�С����ƴ���״̬������Ϊ�գ�");
						return;
					}else if(ctlvo[i].getKzjszt()==null || "".equals(ctlvo[i].getKzjszt())){
						MessageDialog.showErrorDlg(null, "����", (i+1)+"�С����ƽ���״̬������Ϊ�գ�");
						return;
					}else{
						setVos(ctlvo);
					}
				}
				onOK();
			}
			if (e.getSource() == getBoCancel()){
				//��������ȡ����ť��
				try {
					ControlVO[] vos=(ControlVO[]) HYPubBO_Client.queryByCondition(ControlVO.class, "pk_bus='"+fpk+"'");
					for(int i=0;i<vos.length;i++){
						if((vos[i].getKzclzt()==null || "".equals(vos[i].getKzclzt()))||(vos[i].getKzjszt()==null || "".equals(vos[i].getKzjszt()))){
							HYPubBO_Client.deleteByWhereClause(ControlVO.class, "pk_control='"+vos[i].getPk_control()+"'");
						}
					}
				} catch (UifException e2) {
					e2.printStackTrace();
				}
				closeCancel();
			}
		};
		EventHandler() {
			this$0 = ControlDlg.this;
		}

	}
	public void onOK() {
		isOK=true;
		this.close();
	}


	public ControlVO getCvo() {
		return cvo;
	}


	public void setCvo(ControlVO cvo) {
		this.cvo = cvo;
	}


	public ControlVO[] getVos() {
		return vos;
	}


	public void setVos(ControlVO[] vos) {
		this.vos = vos;
	}
}
