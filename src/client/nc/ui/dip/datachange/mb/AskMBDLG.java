package nc.ui.dip.datachange.mb;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import nc.ui.bd.ref.DataChangeModeRefModel;
import nc.ui.bd.ref.DataCopyRefModel;
import nc.ui.dip.dlg.AskDLG;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.trade.base.AbstractBillUI;
import nc.util.dip.sj.SJUtil;

public class AskMBDLG  extends UIDialog implements ActionListener{
	public boolean isOK=false;
	
	private static final long serialVersionUID = 1L;
	private UIRefPane refpnlOrgize = null;
	private ButtonGroup bg;
	private UIButton boOK = null;
	private UIButton boCancel = null;
	private UIPanel panelBackground = null;
	private JPanel norPanel;
	private JPanel midPanel;
	private UIPanel panelButtons = null;
	JRadioButton[] jr;
	String[] ss;
	EventHandler eventHandler = new EventHandler();
	//��ǰ��ı���
	String thetitle;
	//ѯ�����
	String message;
	//ѯ��ѡ��ĳ���
	int sslen;
	String pksys;


	int isSelect =0;
	

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
	
	public AskMBDLG(Container owner,String pksys,String title,String message,String[] ss) {
		super(owner);
		this.pksys=pksys;
		if(SJUtil.isNull(ss)||ss.length==0){
			((AbstractBillUI)owner).showErrorMessage("�봫��ѡ������");
			
		}else{
			isSelect=0;
			sslen=ss.length;
			this.thetitle=title;
			this.message=message;
			this.ss=ss;
			bg=new ButtonGroup();
			jr=new JRadioButton[sslen];
			setSize(450, 300);// ����DLG��С
			setLocation(400, 260);
			setContentPane(getPanelBackground());
			try {
				initConnections();
			} catch (Exception e) {
				e.printStackTrace();
			}
			setResizable(false);
		}
	}
	public void setIsSelect(int i){
		if(i<0){
			i=0;
		}
		if(jr!=null&&jr.length>0&&i>(jr.length-1)){
			jr[jr.length-1].setSelected(true);
		}
		jr[i].setSelected(true);
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
		for(int i=0;i<sslen;i++){
			if(jr[i].isSelected()){
				return i;
			}
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
				panelBackground.add(getNorPanel(), BorderLayout.NORTH);
				panelBackground.add(getMidPanel(), BorderLayout.CENTER);
				panelBackground.add(getPanelButton(), BorderLayout.SOUTH);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return panelBackground;
	}
	private JPanel getMidPanel(){
		if(midPanel==null){
			midPanel=new JPanel();
			JLabel jLabel3=new JLabel();
			jLabel3.setText("ģ�帴��/����");
			midPanel.add(jLabel3);
			jLabel3.setBounds(10,25,70,20);
			midPanel.add(getOrgnizeRefPnl());
			getOrgnizeRefPnl().setBounds(60,25, 150, 20);
		}
		return midPanel;
	}
	 public UIRefPane getOrgnizeRefPnl(){
			if (refpnlOrgize == null){
				refpnlOrgize = new UIRefPane(this);
				refpnlOrgize.setPreferredSize(new Dimension(150,20));
				refpnlOrgize.setRefInputType(1 /** ����*/);
				DataChangeModeRefModel model = new DataChangeModeRefModel();
				model.setWherePart("nvl(dr,0)=0 and fpk='"+pksys+"'");
				refpnlOrgize.setRefModel(model);
				refpnlOrgize.getUIButton().setEnabled(false);
				refpnlOrgize.getUITextField().setEnabled(false);
			}
			return refpnlOrgize;
		}
		
	/**
	 * ����DLGPanel
	 * 
	 * @return
	 */
	private JPanel getNorPanel() {
		if (norPanel == null) {
			norPanel = new JPanel();
			int len;
			if(message==null){
				len=sslen;
			}else{
				len=sslen+1;
			}
			norPanel.setLayout(new GridLayout(len,1,1,1));
			if(message!=null){
				norPanel.add(new JLabel(message));
			}
			for(int i=0;i<sslen;i++){
				if(i==0){
					jr[i]=new JRadioButton(ss[i],true);
				}else{
					jr[i]=new JRadioButton(ss[i],false);
				}
				jr[i].addActionListener(this);
				norPanel.add(jr[i]);
				bg.add(jr[i]);
			}

		}
		return norPanel;
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
	
	public static void main (String[] ar){
		JPanel jp=new JPanel();
		AskDLG a=new AskDLG(jp,"��ʾ","��Ҫɾ�����ݣ��Ƿ����",new String[]{"ɾ����ǰ��ѯ���ݷ�Χ","ceshi2"});
		a.showModal();
		System.out.println(a.getRes()+"");
	}
	public void actionPerformed(ActionEvent arg0) {
		for(int i=0;i<jr.length;i++){
			if(arg0.getSource().equals(jr[i])){
				if(i==5||i==4){//
					getOrgnizeRefPnl().getUIButton().setEnabled(true);
				}else{//
					getOrgnizeRefPnl().getUIButton().setEnabled(false);
				}
				break;
			}
		}
		
	}
}