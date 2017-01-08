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
 * 这个类是弹出一个对话框。主要方法就是getRet()这个方法，通过这个方法，可以返回选择的是第几种方式。注意构造。
 * 可以直接运行这个类，然后可以看到运行的结果
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
	//当前框的标题
	String thetitle;
	//询问语句
	String message;
	//询问选择的长度
	int sslen;

	private int results;

	int isSelect =0;
	

	/**
	*版权信息：商佳科技
	*作者：   王艳冬
	*版本：   
	*描述：   弹出一个询问的单选的框dialog
	*创建日期：2011-04-14
	*参数
	*@param   owner	当前的ClientUI
	*@param   title	弹出框的标题
	*@param   message	询问消息
	*@param   ss数组   单选的可能选择
	 * @throws Exception 
	*/
	
	public AskDLG(Container owner,String title,String message,String[] ss) {
		super(owner);
		if(SJUtil.isNull(ss)||ss.length==0){
			((AbstractBillUI)owner).showErrorMessage("请传入选择数据");
			
		}else{
			isSelect=0;
			sslen=ss.length;
			this.thetitle=title;
			this.message=message;
			this.ss=ss;
			bg=new ButtonGroup();
			jr=new JRadioButton[sslen];
			setSize(350, 250);// 设置DLG大小setSize(350, 250);
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
	*版权信息：商佳科技
	*作者：   王艳冬
	*版本：   
	*描述：   返回选择的是第几种方式
	*创建日期：2011-04-14
	*参数
	*@return 返回选择的是第几种方式
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
	 * 设置DLG名称
	 */
	@Override
	public String getTitle() {
		return thetitle;
	}
	/**
	 * 布局DLGPanel
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
	 * 生成DLGPanel
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
			if("选择数据对照维护导出类型?".equals(message)&&"导出该表全部数据".equals(ss[1])){
				JPanel jp1=new JPanel();
				jp1.setBounds(40, 115, 200, 60);
				jp1.setLayout(new GridLayout(3,2));
				
				 jc1=new JCheckBox("已有对照维护数据");
				 jc2=new JCheckBox("对照未维护数据");
				 jc3=new JCheckBox("被对照未维护数据");
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
	 * 按钮[确定]
	 * 
	 * @return
	 */
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
	 * DLG按钮组
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
		isOK=true;
		this.close();
	}
	
	public static void main (String[] ar){
		JPanel jp=new JPanel();
		AskDLG a=new AskDLG(jp,"提示","将要删除数据！是否继续",new String[]{"删除当前查询数据范围","ceshi2"});
		a.showModal();
		System.out.println(a.getRes()+"");
	}
}
