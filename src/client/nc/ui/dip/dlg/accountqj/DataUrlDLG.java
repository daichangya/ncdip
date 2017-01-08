package nc.ui.dip.dlg.accountqj;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JPanel;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.bd.period.AccperiodschemeVO;
import nc.vo.dip.dataurl.DataurlVO;
/**
 * 这个类是弹出一个对话框。主要方法就是getRet()这个方法，通过这个方法，可以返回选择的是第几种方式。注意构造。
 * 可以直接运行这个类，然后可以看到运行的结果
 */
public class DataUrlDLG extends UIDialog {
	private boolean isOK=false;
	
	private static final long serialVersionUID = 1L;
	private UIButton boOK = null;
	private UIButton boCancel = null;
	private UIPanel panelBackground = null;
	private UIPanel panelButtons = null;
	private BillCardPanel mipanel=null;
	
	EventHandler eventHandler = new EventHandler();
	//当前框的标题
	String thetitle="数据同步url注册";
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
	public DataUrlDLG(Container owner) {
		super(owner);
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
		if(mipanel==null){
			mipanel=new BillCardPanel();
			mipanel.loadTemplet("H4H1H1H5", null, ClientEnvironment.getInstance().getUser().getPrimaryKey(), ClientEnvironment.getInstance().getCorporation().getPk_corp()) ;
			mipanel.getBillTable().setSortEnabled(false);
//			mipanel.setBillType("H4H1H1H5");
			
		//	mipanel.getHeadItems();
			DataurlVO [] dataUrlvos = null;
			try {
				dataUrlvos = (DataurlVO[]) HYPubBO_Client.queryByCondition(DataurlVO.class, "nvl(dr,0)=0");
			} catch (UifException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(dataUrlvos!=null&&dataUrlvos.length>0){
			mipanel.getBillModel().setBodyDataVO(dataUrlvos);
			mipanel.getBillModel().setEnabled(false);
			}
		}
		return mipanel;
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
	public String getRes(){
		if(!isOK){
			return null;
		}
		return (String)getMipanel().getBodyValueAt(getMipanel().getBillTable().getSelectedRow(), "code");
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
		final DataUrlDLG this$0;
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == getBoOK()){
				onOK();
			}else if (e.getSource() == getBoCancel()){
				closeCancel();
			}
		};
		EventHandler() {
			this$0 = DataUrlDLG.this;
		}

	}
	public void onOK() {
		isOK=true;
		this.close();
	}
	public static void main (String[] ar){
		JPanel jp=new JPanel();
		DataUrlDLG a=new DataUrlDLG(jp);
		a.showModal();
//		System.out.println(a.getRes()+"");
	}
}
