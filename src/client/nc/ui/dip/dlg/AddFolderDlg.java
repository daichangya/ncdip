package nc.ui.dip.dlg;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;

public class AddFolderDlg extends UIDialog{
	public AddFolderDlg(Container owner,List<String> listcode,List<String> listname,String tsyscode,String tsysname){
		super(owner);
		init();
		this.listcode=listcode;
		this.listname=listname;
		if(tsysname!=null&&!tsysname.trim().equals("")){
			jTextField2.setText(tsysname);	
		}
		if(tsyscode!=null&&!tsyscode.trim().equals("")){
			jTextField1.setText(tsyscode);
		}
	}
//	public AddFolderDlg(Container owner,String tsyscode,String tsysname){
//		super(owner);
//		init();
//		jTextField2.setText(tsysname);
//		jTextField1.setText(tsyscode);
//	}
	EventHandler eventHandler=new EventHandler();
	public void init(){
		setSize(350, 250);// 设置DLG大小setSize(350, 250);
		setLocation(400, 260);
		setContentPane(getPanelBackground());
		getBoOK().addActionListener(eventHandler);
		getBoCancel().addActionListener(eventHandler);
	}
	List<String> listcode;
	List<String> listname;
	boolean isOk=false;
	String code=null;
	String name=null;
	UIPanel panelBackground;
	UIPanel midPanel;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JTextField jTextField2;
	private JTextField jTextField1;
	UIButton boOK;
	UIButton boCancel;
	UIPanel panelButtons;
	
	
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
	
	private Component getMidPanel() {
		// TODO Auto-generated method stub
		if (midPanel == null) {
			midPanel = new UIPanel();
			JPanel jp1=new JPanel();
			try {
				jp1.setLayout(null);
				jp1.setPreferredSize(new java.awt.Dimension(400, 204));
				{
					jLabel1 = new JLabel();
					jp1.add(jLabel1);
					jLabel1.setText("\u6587\u4ef6\u5939\u7f16\u7801\uff1a");
					jLabel1.setBounds(40, 59, 85, 27);
				}
				{
					jLabel2 = new JLabel();
					jp1.add(jLabel2);
					jLabel2.setText("\u6587\u4ef6\u5939\u540d\u79f0\uff1a");
					jLabel2.setBounds(40, 116, 85, 27);
				}
				{
					jTextField1 = new JTextField();
					jp1.add(jTextField1);
					jTextField1.setBounds(121, 61, 199, 22);
				}
				{
					jTextField2 = new JTextField();
					jp1.add(jTextField2);
					jTextField2.setBounds(121, 116, 199, 22);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			midPanel.add(jp1);
		}
		return midPanel;
	}

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

	private UIButton getBoOK() {
		if (boOK == null) {
			try {
				boOK = new UIButton();
				boOK.setName("m_btOK");
				boOK.setText("确定");
			} catch (java.lang.Throwable e) {
				e.printStackTrace();
			}
		}
		return boOK;
	}
	/**
	 * 按钮[取消]
	 * 
	 * @return
	 */
	private UIButton getBoCancel() {
		if (boCancel == null) {
			try {
				boCancel = new UIButton();
				boCancel.setName("m_btCancel");
				boCancel.setText("取消");
			} catch (java.lang.Throwable e) {
				e.printStackTrace();
			}
		}
		return boCancel;
	}
	/**
	 * 按钮监听内部类
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
		String tempcode=jTextField1.getText();
		 String tempname=jTextField2.getText();
		if(tempcode==null||tempcode.trim().length()<=0||tempname==null||tempname.trim().length()<=0){
			MessageDialog.showErrorDlg(this, "提示", "编码和名称不能为空！");
			return;
		}
		String err="";
		if(listcode!=null&&listcode.size()>0){
			for(int i=0;i<listcode.size();i++){
				if(tempcode.equals(listcode.get(i))){
					err="文件夹编码已经存在！";
					break;
				}
			}
		}
		if(listname!=null&&listname.size()>0){
			for(int i=0;i<listname.size();i++){
				if(tempname.equals(listname.get(i))){
					err=err+"\n文件夹名称已经存在！";
					break;
				}
			}
		}
		if(err!=null&&err.length()>0){
			MessageDialog.showErrorDlg(this, "提示", err);
			return;
		}
		setCode(tempcode);
		setName(tempname);
		setOk(true);
		this.close();
	}
	
	public static void main(String[] args) {
//		AddFolderDlg add=new AddFolderDlg(new JFrame(),new ArrayList(),new ArrayList());
//		add.showModal();
	}

	public boolean isOk() {
		return isOk;
	}

	public void setOk(boolean isOk) {
		this.isOk = isOk;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
