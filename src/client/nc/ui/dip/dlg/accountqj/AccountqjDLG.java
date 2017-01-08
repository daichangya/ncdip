package nc.ui.dip.dlg.accountqj;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JPanel;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.SJUtil;
import nc.vo.bd.period.AccperiodschemeVO;
import nc.vo.dip.dataformatdef.DataformatdefBVO;
import nc.vo.dip.datalook.DipDesignVO;
import nc.vo.dip.datalook.VDipDesignVO;
import nc.vo.dip.warningset.DipWarningsetDayTimeVO;
/**
 * ������ǵ���һ���Ի�����Ҫ��������getRet()���������ͨ��������������Է���ѡ����ǵڼ��ַ�ʽ��ע�⹹�졣
 * ����ֱ����������࣬Ȼ����Կ������еĽ��
 */
public class AccountqjDLG extends UIDialog {
	private boolean isOK=false;
	
	private static final long serialVersionUID = 1L;
	private UIButton boOK = null;
	private UIButton boCancel = null;
	private UIPanel panelBackground = null;
	private UIPanel panelButtons = null;
	private BillCardPanel mipanel=null;
	
	EventHandler eventHandler = new EventHandler();
	//��ǰ��ı���
	String thetitle="����ڼ䷽��";
	
	/**
	*��Ȩ��Ϣ���̼ѿƼ�
	*���ߣ�   ���޶�
	*�汾��   
	*������   ����һ��ѯ�ʵĵ�ѡ�Ŀ�dialog
	*�������ڣ�2011-04-14
	*����
	*@param   owner	��ǰ��ClientUI
	*@param   pks	������
	 * @throws Exception 
	*/
	public AccountqjDLG(Container owner) {
		super(owner);
		setSize(600, 300);// ����DLG��С
		setContentPane(getPanelBackground());
		try {
			initConnections();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setResizable(true);
	}
	public BillCardPanel getMipanel(){
		if(mipanel==null){
			mipanel=new BillCardPanel();
			mipanel.loadTemplet("100403", null, ClientEnvironment.getInstance().getUser().getPrimaryKey(), ClientEnvironment.getInstance().getCorporation().getPk_corp()) ;
			mipanel.getBillTable().setSortEnabled(false);
			AccperiodschemeVO[] msvos = null;
			try {
				msvos = (AccperiodschemeVO[]) HYPubBO_Client.queryByCondition(AccperiodschemeVO.class, "nvl(dr,0)=0");
			} catch (UifException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(msvos!=null&&msvos.length>0){
			mipanel.getBillModel().setBodyDataVO(msvos);
			mipanel.getBillModel().setEnabled(false);
			}
		}
		return mipanel;
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
	public String getRes(){
		if(!isOK){
			return null;
		}
		return (String)getMipanel().getBodyValueAt(getMipanel().getBillTable().getSelectedRow(), "pk_accperiodscheme");
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
		final AccountqjDLG this$0;
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == getBoOK()){
				onOK();
			}else if (e.getSource() == getBoCancel()){
				closeCancel();
			}
		};
		EventHandler() {
			this$0 = AccountqjDLG.this;
		}

	}
	public void onOK() {
		isOK=true;
		this.close();
	}
	public static void main (String[] ar){
		JPanel jp=new JPanel();
		AccountqjDLG a=new AccountqjDLG(jp);
		a.showModal();
//		System.out.println(a.getRes()+"");
	}
}
