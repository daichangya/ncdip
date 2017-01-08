package nc.ui.dip.dlg.movefoleder;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.SJUtil;
import nc.vo.bd.period.AccperiodschemeVO;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.dip.dataformatdef.DataformatdefBVO;
import nc.vo.dip.datalook.DipDesignVO;
import nc.vo.dip.datalook.VDipDesignVO;
import nc.vo.dip.taskregister.DipTaskregisterVO;
import nc.vo.dip.warningset.DipWarningsetDayTimeVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
/**
 * 这个类是弹出一个对话框。主要方法就是getRet()这个方法，通过这个方法，可以返回选择的是第几种方式。注意构造。
 * 可以直接运行这个类，然后可以看到运行的结果
 */
public class MovefolderDLG extends UIDialog {
	private boolean isOK=false;
	
	private static final long serialVersionUID = 1L;
	private UIButton boOK = null;
	private UIButton boCancel = null;
	private UIPanel panelBackground = null;
	private UIPanel panelButtons = null;
	private BillCardPanel mipanel=null;
	private String busstype=null;
	private SuperVO vo;
	EventHandler eventHandler = new EventHandler();
	//当前框的标题
	String thetitle="文件夹选择";
	
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
	public MovefolderDLG(Container owner,String busstype,SuperVO vo) {
		super(owner);
		this.busstype=busstype;
		this.vo=vo;
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
			mipanel.loadTemplet("H4H1H0H0", null, ClientEnvironment.getInstance().getUser().getPrimaryKey(), ClientEnvironment.getInstance().getCorporation().getPk_corp()) ;
			mipanel.getBillTable().setSortEnabled(false);
			mipanel.getBodyItem("issyspref").setShow(false);
			List<DipTaskregisterVO> msvos = null;
			IUAPQueryBS ibs=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			String sql="select "+IContrastUtil.getYwbmName().get(busstype)+" code," +
					IContrastUtil.getYwName().get(busstype)+" name," +
					IContrastUtil.getPkMap().get(busstype)+" pk_taskregister from "
					+IContrastUtil.getTabNameMap().get(busstype)+" where pk_xt='"+vo.getAttributeValue("pk_xt")+"' and isfolder='Y' and " +
					IContrastUtil.getPkMap().get(busstype)+"<>'"+vo.getAttributeValue((String)IContrastUtil.getFpkMap().get(busstype))+"' and nvl(dr,0)=0";
			try {
				msvos=(List<DipTaskregisterVO>) ibs.executeQuery(sql, new BeanListProcessor(DipTaskregisterVO.class));
			} catch (BusinessException e) {
				e.printStackTrace();
			}
			if(msvos!=null&&msvos.size()>0){
				mipanel.getBillModel().setBodyDataVO(msvos.toArray(new DipTaskregisterVO[msvos.size()]));
				mipanel.getBillModel().setEnabled(false);
			}
			mipanel.setBillData(mipanel.getBillData());
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
		return (String)getMipanel().getBodyValueAt(getMipanel().getBillTable().getSelectedRow(), "pk_taskregister");
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
		final MovefolderDLG this$0;
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == getBoOK()){
				onOK();
			}else if (e.getSource() == getBoCancel()){
				closeCancel();
			}
		};
		EventHandler() {
			this$0 = MovefolderDLG.this;
		}

	}
	public void onOK() {
		isOK=true;
		this.close();
	}
	public static void main (String[] ar){
		JPanel jp=new JPanel();
		MovefolderDLG a=new MovefolderDLG(jp,IContrastUtil.YWLX_DY,new DipDatadefinitHVO());
		a.showModal();
//		System.out.println(a.getRes()+"");
	}
}
