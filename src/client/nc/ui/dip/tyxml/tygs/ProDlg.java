package nc.ui.dip.tyxml.tygs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
/**
 * 这个类是弹出一个对话框。主要方法就是getRet()这个方法，通过这个方法，可以返回选择的是第几种方式。注意构造。
 * 可以直接运行这个类，然后可以看到运行的结果
 */
public class ProDlg extends UIDialog {
	private boolean isOK=false;
	
	private static final long serialVersionUID = 1L;
	private UIButton boLinAdd = null;
	private UIButton boLinDel = null;
	private UIButton boOK = null;
	private UIButton boCancel = null;
	private UIPanel panelBackground = null;
	private UIPanel panelButtons = null;
	private BillCardPanel mainpanel=null;
	private String fo=null;
	private String pksourcetab=null;
	EventHandler eventHandler = new EventHandler();
	//当前框的标题
	String thetitle="属性编辑";
	/**
	*版权信息：商佳科技
	*作者：   王艳冬
	*版本：   
	*描述：   弹出一个询问的单选的框dialog
	*创建日期：2011-04-14
	*参数
	*@param   owner	当前的ClientUI
	*@param   pks	表主键
	 * @throws Exception 
	*/
	public ProDlg(Container owner,String fo,String pksourcetab ) {
		super(owner);
		this.fo=fo;
		this.pksourcetab=pksourcetab;
		setSize(600, 300);// 设置DLG大小
		setContentPane(getPanelBackground());
		try {
			initConnections();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setResizable(true);
	}
	public BillCardPanel getMipanel(){
		if(mainpanel==null){
			mainpanel= new BillCardPanel();
			mainpanel.loadTemplet("H4H1Hh", null, ClientEnvironment.getInstance().getUser().getPrimaryKey(), ClientEnvironment.getInstance().getCorporation().getPk_corp()) ;
			mainpanel.getBillTable().setSortEnabled(false);
			mainpanel.getBodyItem("pk").setName("属性名");
			mainpanel.getBodyItem("pk").setWidth(200);
			mainpanel.getBodyItem("gs").setWidth(200);
			
			if(fo!=null&&fo.length()>0){
				String[] s=fo.split("\\+\",\"\\+");
				for(int k=0;k<s.length;k++){
					mainpanel.addLine();
					String[] si=s[k].split(":\"\\+");
					mainpanel.setBodyValueAt(si[0].replace("\"", ""), k, "pk");
					mainpanel.setBodyValueAt(si[1], k, "gs");
				}
			}
			UIRefPane rf=(UIRefPane) mainpanel.getBodyItem("gs").getComponent();

			ProEditListener listener = new ProEditListener(this,"gs",rf,pksourcetab);
			if(rf!=null){
				rf.getUIButton().removeActionListener(rf.getUIButton().getListeners(ActionListener.class)[0]);
				rf.getUIButton().addActionListener(listener);
				rf.setAutoCheck(false);
				rf.setEditable(false);
			}

			mainpanel.setBillData(mainpanel.getBillData());
		}
		return mainpanel;
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
	 * 按钮[确定]
	 * 
	 * @return
	 */
	private UIButton getBoLinAdd() {
		if (boLinAdd == null) {
			try {
				boLinAdd = new UIButton();
				boLinAdd.setName("boLinAdd");
				boLinAdd.setText("增行");
			} catch (java.lang.Throwable e) {
				e.printStackTrace();
			}
		}
		return boLinAdd;
	}
	/**
	 * 按钮[确定]
	 * 
	 * @return
	 */
	private UIButton getBoLinDel() {
		if (boLinDel == null) {
			try {
				boLinDel = new UIButton();
				boLinDel.setName("boLinDel");
				boLinDel.setText("删行");
			} catch (java.lang.Throwable e) {
				e.printStackTrace();
			}
		}
		return boLinDel;
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
				getPanelButton().add(getBoLinAdd(), getBoLinAdd().getName());
				getPanelButton().add(getBoLinDel(), getBoLinDel().getName());
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
		getBoLinAdd().addActionListener(eventHandler);
		getBoLinDel().addActionListener(eventHandler);
		getBoCancel().addActionListener(eventHandler);
	}
	/**
	 * 按钮监听内部类
	 * 
	 * @author jieely 2008-8-21
	 */
	class EventHandler implements java.awt.event.ActionListener {
		final ProDlg this$0;
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == getBoOK()){
				onOK();
			}else if (e.getSource() == getBoCancel()){
				closeCancel();
			}else if(e.getSource()==getBoLinAdd()){
				onBoLineAdd();
			}else if(e.getSource()==getBoLinDel()){
				onBoLineDel();
			}
		};
		EventHandler() {
			this$0 = ProDlg.this;
		}

	}
	public void onOK() {
		isOK=true;
		int rowcount=getMipanel().getBillTable().getRowCount();
		StringBuffer sb=new StringBuffer();
		if(rowcount>0){
			for(int i=0;i<rowcount;i++){
				String pk=(String) getMipanel().getBodyValueAt(i, "pk");
				String val=(String) getMipanel().getBodyValueAt(i, "gs");
				if(pk!=null&&pk.length()>0){
					if(i>0&&sb.length()>0){
						sb.append("+\",\"+");
					}
					sb.append("\""+pk+":\"+");
					sb.append(val==null?"\"\"":val);
				}
			}
		}
		fo=sb.toString();
		this.close();
	}
	public void onBoLineAdd() {
		getMipanel().addLine();
	}
	public void onBoLineDel() {
		getMipanel().delLine();
	}
	public static void main (String[] ar){
	}
	public boolean isOK() {
		return isOK;
	}
	public void setOK(boolean isOK) {
		this.isOK = isOK;
	}
	public String getFormula() {
		return fo;
	}
}
