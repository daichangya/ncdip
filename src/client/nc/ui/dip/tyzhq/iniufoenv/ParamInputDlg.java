package nc.ui.dip.tyzhq.iniufoenv;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JPanel;


import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.ui.dip.util.ClientEnvDef;
import nc.ui.fi.uforeport.NCFuncForUFO;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.util.dip.sj.SJUtil;
import nc.vo.fi.uforeport.UfoParseException;
/**
 * 这个类是弹出一个对话框。主要方法就是getRet()这个方法，通过这个方法，可以返回选择的是第几种方式。注意构造。
 * 可以直接运行这个类，然后可以看到运行的结果
 */
public class ParamInputDlg extends UIDialog {
	private boolean isOK=false;
	
	private static final long serialVersionUID = 1L;
	private UIButton boOK = null;
	private UIButton boCancel = null;
	private UIPanel panelBackground = null;
	private UIPanel panelButtons = null;
	private ParamInputPanel mipanel=null;
	private String funname=null;
	private String results=null;
	
	EventHandler eventHandler = new EventHandler();
	//当前框的标题
	String thetitle="参数录入";
	
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
	public ParamInputDlg(Container owner,String funname) {
		super(owner);
		setSize(500, 300);// 设置DLG大小
		setLocation(400, 200);
		setContentPane(getPanelBackground());
		try {
			initConnections();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setResizable(true);
		this.funname=funname;
	}

	
	public ParamInputPanel getMipanel() {
		if(SJUtil.isNull(mipanel)){
			mipanel=new ParamInputPanel();
		}
		return mipanel;
	}
	/*public void setMipanel(JPanel mipanel) {
		this.mipanel = mipanel;
	}*/
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
		return 0;
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
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == getBoOK())
				onOK();
			if (e.getSource() == getBoCancel())
				closeCancel();
		};

	}
	IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	public void onOK() {
		String ss=getMipanel().getCalenvFromPanel();
//		String ischeckpass=null;
		if(ss!=null&&ss.length()>0){
//			try {
//				ischeckpass=ClientEnvDef.getNCFuncForUFO().checkFunc(funname, ss);
//			} catch (UfoParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		if(ischeckpass!=null&&ischeckpass.equals("true")){
			if(!nc.vo.pf.pub.PfFormulaParse.isFormula(ss)){
				MessageDialog.showErrorDlg(this, "参数错误",
						"请输入合法参数");
				return;
			}
			setResults(funname+"("+ss+")");
			isOK=true;
			this.close();
		}
	}
	/*public DipWarningsetDayTimeVO getDTVO(){
		if(isOK){
			return ((DayTimePanel)getMipanel()).getDTVO();
		}else{
			return null;
		}
	}*/
	public static void main (String[] ar){
		JPanel jp=new JPanel();
		ParamInputDlg a=new ParamInputDlg(jp,"GL");
		a.showModal();
//		System.out.println(a.getRes()+"");
	}


	public String getResults() {
		return results;
	}


	private void setResults(String result) {
		this.results = result;
	}

}

