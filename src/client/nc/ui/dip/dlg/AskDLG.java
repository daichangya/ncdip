package nc.ui.dip.dlg;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.trade.base.AbstractBillUI;
import nc.util.dip.sj.SJUtil;
/**
 * ������ǵ���һ���Ի�����Ҫ��������getRet()���������ͨ��������������Է���ѡ����ǵڼ��ַ�ʽ��ע�⹹�졣
 * ����ֱ����������࣬Ȼ����Կ������еĽ��
 */
public class AskDLG extends UIDialog {
	public boolean isOK=false;
	
	private static final long serialVersionUID = 1L;
	private ButtonGroup bg;
	private UIButton boOK = null;
	private UIButton boCancel = null;
	private UIPanel panelBackground = null;
	private JPanel midPanel;
	private UIPanel panelButtons = null;
	private JCheckBox jc1=null;
	private JCheckBox jc2=null;
	private JCheckBox jc3=null;
	JRadioButton[] jr;
	String[] ss;
	String []s=null;
	EventHandler eventHandler = new EventHandler();
	//��ǰ��ı���
	String thetitle;
	//ѯ�����
	String message;
	//ѯ��ѡ��ĳ���
	int sslen;

	private int results;

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
	
	public AskDLG(Container owner,String title,String message,String[] ss) {
		super(owner);
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
			setSize(350, 250);// ����DLG��СsetSize(350, 250);
			setLocation(400, 260);
			setContentPane(getPanelBackground());
			try {
				initConnections();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setResizable(true);
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
	
	public String[] getJcs(){
		s=new String[3];
		if(jc1!=null&&jc1.isSelected()){
			s[0]="1";
		}
		if(jc2!=null&&jc2.isSelected()){
			s[1]="2";
		}
		if(jc3!=null&&jc3.isSelected()){
			s[2]="3";
		}
		return s;
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
				panelBackground.add(getMidPanel(), "Center");
				panelBackground.add(getPanelButton(), "South");
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return panelBackground;
	}
	/**
	 * ����DLGPanel
	 * 
	 * @return
	 */
	private JPanel getMidPanel() {
		if (midPanel == null) {
			midPanel = new JPanel();
			int len;
			if(message==null){
				len=sslen;
			}else{
				len=sslen+1;
			}
//			midPanel.setLayout(new GridLayout(len+1,1,1,1));
			midPanel.setLayout(null);
			//midPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			if(message!=null){
				JLabel lb=new JLabel(message);
				lb.setBounds(20, 20, 250, 25);
				midPanel.add(lb);
				
			}
			for(int i=0;i<sslen;i++){
				if(i==0){
					jr[i]=new JRadioButton(ss[i],true);
					jr[i].setBounds(20, 57, 400, 25);
				}else{
					jr[i]=new JRadioButton(ss[i],false);
					jr[i].setBounds(20, 57+31*i, 400, 25);
				}
				midPanel.add(jr[i]);
				bg.add(jr[i]);
			}
			if("ѡ�����ݶ���ά����������?".equals(message)&&"�����ñ�ȫ������".equals(ss[1])){
				JPanel jp1=new JPanel();
				jp1.setBounds(40, 115, 200, 60);
				jp1.setLayout(new GridLayout(3,2));
				
				 jc1=new JCheckBox("���ж���ά������");
				 jc2=new JCheckBox("����δά������");
				 jc3=new JCheckBox("������δά������");
				 jc1.setEnabled(false);
				jp1.add(jc1);
				jp1.add(jc2);
				jp1.add(jc3);
				midPanel.add(jp1);
				jc1.setSelected(true);
//				midPanel.add(jc1);
//				midPanel.add(jc2);
//				midPanel.add(jc3); 
			}

		}
		return midPanel;
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
}
