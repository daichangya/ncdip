package nc.ui.dip.dlg;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.ui.bd.ref.DipADContRoleRefModel;
import nc.ui.bd.ref.DipADContdataRefModel;
import nc.ui.bd.ref.UserColRefModel;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.trade.business.HYPubBO_Client;
import nc.util.dip.sj.RetMessage;
import nc.vo.dip.authorize.define.DipADContdataVO;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
/**
 * 这个类是弹出一个对话框。主要方法就是getRet()这个方法，通过这个方法，可以返回选择的是第几种方式。注意构造。
 * 可以直接运行这个类，然后可以看到运行的结果
 */
public class FastSetAskDLG extends UIDialog {
	public boolean isOK=false;
	
	private static final long serialVersionUID = 1L;
	private ButtonGroup bg;
	private UIButton boOK = null;
	private UIButton boCancel = null;
	private UIPanel panelBackground = null;
	private JPanel midPanel;
	private UIPanel panelButtons = null;
	private UIRefPane midTableRef = null;
	private JLabel midTableLabel = null;
	private UIRefPane roleRef = null;
	private JLabel roleLabel = null;
	private UIRefPane roleCodeRef = null;
	private JLabel roleCodeLabel = null;
	private UIRefPane tableCodeRef = null;
	private JLabel tableCodeLabel = null;
	JRadioButton[] jr;
	String []s=null;
	EventHandler eventHandler = new EventHandler();
	//当前框的标题
	String thetitle;
	//询问选择的长度
	int sslen = 3;
	private DipADContdataVO dfvo = null;
	int isSelect =0;
	IQueryField queryfield=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	private String pk_role="";
	private String quotecolu = "";
	private String memorytable = "";
	private String returnMessage = "";
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
	
	public FastSetAskDLG(Container owner,DipADContdataVO dfvo,String pk_role) {
		super(owner);
		isSelect=0;
		this.dfvo = dfvo;
		this.pk_role = pk_role;
		bg=new ButtonGroup();
		jr=new JRadioButton[sslen];
		setSize(550, 250);// 设置DLG大小setSize(350, 250);
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
			midPanel.setLayout(null);
			JLabel lb=new JLabel("选择快速授权方式？");
			lb.setBounds(20, 20, 250, 25);
			midPanel.add(lb);
			jr[0]=new JRadioButton("授权数据复制",true);
			jr[0].setBounds(20, 57, 100, 25);
			midPanel.add(jr[0]);
			bg.add(jr[0]);
			midPanel.add(getMidTableLabel());
			midPanel.add(getMidTableRef());
			jr[1]=new JRadioButton("角色数据复制",false);
			jr[1].setBounds(20, 88, 100, 25);
			midPanel.add(jr[1]);
			bg.add(jr[1]);
			midPanel.add(getRoleRef());
			midPanel.add(getRoleLabel());
			jr[2]=new JRadioButton("自动对照授权",false);
			jr[2].setBounds(20, 119, 100, 25);
			midPanel.add(jr[2]);
			bg.add(jr[2]);
			midPanel.add(getRoleCodeRef());
			midPanel.add(getTableCodeRef());
			midPanel.add(getRoleCodeLabel());
			midPanel.add(getTableCodeLabel());
		}
		return midPanel;
	}
	
	private UIRefPane getMidTableRef(){
		if(midTableRef == null){
			midTableRef=new UIRefPane();
			midTableRef.setRefModel(new DipADContdataRefModel());
			midTableRef.setBounds(210, 57, 100, 25);
			String pk_contdata_h = this.dfvo.getPk_contdata_h();
			String whereStr = "";
			try {
				DipADContdataVO[] vos = (DipADContdataVO[])HYPubBO_Client.queryByCondition(DipADContdataVO.class, "contcolcode in(SELECT pk_datadefinit_b FROM dip_datadefinit_b "
						+" WHERE (quotetable,quotecolu) in(SELECT quotetable,quotecolu FROM dip_datadefinit_b WHERE pk_datadefinit_b in( "
						+" SELECT contcolcode FROM dip_adcontdata  WHERE pk_contdata_h='"+pk_contdata_h+"'))) "
						+" and pk_contdata_h<>'"+pk_contdata_h+"'");
				if(null != vos && vos.length>0){
					ArrayList<String> pkList = new ArrayList<String>();
					for (DipADContdataVO vo : vos) {
						String middletab = vo.getMiddletab();
						List list = queryfield.queryfieldList("select 1 from "
								+middletab
								+" where extepk in(SELECT pk_role FROM dip_rolegroup_b WHERE pk_role_group in(SELECT pk_fp FROM dip_adcontdata_b where pk_contdata_h='"
								+pk_contdata_h
								+"' and coalesce(dr,0)=0) and coalesce(dr,0)=0 )");
						if(null != list && list.size()>0){
							pkList.add(vo.getPk_contdata_h());
						}
					}
					if(pkList.size()>0){
						String pks = "";
						for (String string : pkList) {
							pks+="'"+string+"',";
						}
						whereStr = " Pk_contdata_h in("+pks.substring(0, pks.length()-1)+")";
					}else{
						whereStr = "1=2";
					}
				}else{
					whereStr = "1=2";
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				whereStr = "1=2";
			}
			midTableRef.setWhereString(whereStr);
		}
		return midTableRef;
	}
	
	private JLabel getMidTableLabel(){
		if(midTableLabel == null){
			midTableLabel=new JLabel("授权物理表");
			midTableLabel.setBounds(150, 57, 60, 25);
		}
		return midTableLabel;
	}
	
	private UIRefPane getRoleRef(){
		if(roleRef == null){
			roleRef=new UIRefPane();
			roleRef.setRefModel(new DipADContRoleRefModel());
			roleRef.setBounds(210, 88, 100, 25);
			String middletab = dfvo.getMiddletab();
			String whereStr = " pk_role in(select extepk from "+middletab+" where coalesce(dr,0)=0) and pk_role<>'"+pk_role+"'";
			roleRef.setWhereString(whereStr);
			roleRef.setEnabled(false);
			roleRef.setMultiSelectedEnabled(true);
		}
		return roleRef;
	}
	
	private JLabel getRoleLabel(){
		if(roleLabel == null){
			roleLabel=new JLabel("已授权角色");
			roleLabel.setBounds(150, 88, 60, 25);
		}
		return roleLabel;
	}
	
	private UIRefPane getRoleCodeRef(){
		if(roleCodeRef == null){
			roleCodeRef=new UIRefPane();
			roleCodeRef.setRefModel(new UserColRefModel());
			roleCodeRef.setBounds(210, 119, 100, 25);
			roleCodeRef.setEnabled(false);
			roleCodeRef.setWhereString(" table_name='SM_ROLE'");
		}
		return roleCodeRef;
	}
	private JLabel getRoleCodeLabel(){
		if(roleCodeLabel == null){
			roleCodeLabel=new JLabel("角色表字段");
			roleCodeLabel.setBounds(150, 119, 60, 25);
		}
		return roleCodeLabel;
	}
	
	private UIRefPane getTableCodeRef(){
		if(tableCodeRef == null){
			tableCodeRef=new UIRefPane();
			tableCodeRef.setRefModel(new UserColRefModel());
			tableCodeRef.setBounds(420, 119, 100, 25);
			tableCodeRef.setEnabled(false);
			try {
				DipDatadefinitBVO[] dataBVos = (DipDatadefinitBVO[])HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class,
						" pk_datadefinit_b in(SELECT contcolcode FROM dip_adcontdata  WHERE pk_contdata_h='"+dfvo.getPk_contdata_h()+"')");
				if(null != dataBVos && dataBVos.length>0){
					quotecolu = dataBVos[0].getQuotecolu();
					memorytable = queryfield.queryfield("SELECT memorytable FROM v_dip_jgyyzdtree WHERE pk_datadefinit_b='"
							+dataBVos[0].getQuotetable()
							+"'");
					tableCodeRef.setWhereString(" table_name='"+memorytable+"'");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tableCodeRef;
	}
	private JLabel getTableCodeLabel(){
		if(tableCodeLabel == null){
			tableCodeLabel=new JLabel("授权控制表字段");
			tableCodeLabel.setBounds(320, 119, 100, 25);
		}
		return tableCodeLabel;
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
		jr[0].addActionListener(eventHandler);
		jr[1].addActionListener(eventHandler);
		jr[2].addActionListener(eventHandler);
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
			else if (e.getSource() == getBoCancel())
				closeCancel();
			else if (e.getSource() == jr[0]){
				getMidTableRef().setEnabled(true);
				getRoleCodeRef().setEnabled(false);
				getRoleRef().setEnabled(false);
				getTableCodeRef().setEnabled(false);
				getRoleRef().setText(null);
				getRoleCodeRef().setText(null);
				getTableCodeRef().setText(null);
			}else if (e.getSource() == jr[1]){
				getRoleRef().setEnabled(true);
				getMidTableRef().setEnabled(false);
				getRoleCodeRef().setEnabled(false);
				getTableCodeRef().setEnabled(false);
				getMidTableRef().setText(null);
				getRoleCodeRef().setText(null);
				getTableCodeRef().setText(null);
			}else if (e.getSource() == jr[2]){
				getRoleCodeRef().setEnabled(true);
				getTableCodeRef().setEnabled(true);
				getMidTableRef().setEnabled(false);
				getRoleRef().setEnabled(false);
				getMidTableRef().setText(null);
				getRoleRef().setText(null);
			}
		};

	}
	public void onOK() {
		isOK=true;
		String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
		for(int i=0;i<sslen;i++){
			if(jr[i].isSelected()){
				if(i==0){
					String refPK = getMidTableRef().getRefPK();
					if(null == refPK || "".equals(refPK)){
						MessageDialog.showErrorDlg(this, "错误", "请选择授权物理表");
						return;
					}
					String insetSql = "insert into "+dfvo.getMiddletab()
								+" SELECT * FROM "+refPK+" where extepk in(SELECT pk_role FROM dip_rolegroup_b WHERE pk_role_group in(SELECT pk_fp FROM dip_adcontdata_b where pk_contdata_h='"
								+dfvo.getPk_contdata_h()
								+"' and coalesce(dr,0)=0) and coalesce(dr,0)=0 )";
					queryfield.exectEverySql(insetSql);
					setReturnMessage("已成功授权");
					break;
				}else if(i==1){
					String[] refPKs = getRoleRef().getRefPKs();
					if(null == refPKs || refPKs.length==0){
						MessageDialog.showErrorDlg(this, "错误", "请选择已授权角色");
						return;
					}
					if(null == pk_role || "1".equals(pk_role)){
						MessageDialog.showErrorDlg(this, "错误", "请在维护界面选择需要授权的角色");
						return;
					}
					String refPKsql = "";
					for (String string : refPKs) {
						refPKsql+="'"+string+"',";
					}
					refPKsql = refPKsql.substring(0, refPKsql.length()-1);
					ArrayList<String> list = new ArrayList<String>();
					String querySql = "SELECT distinct contpk FROM "+dfvo.getMiddletab()+" WHERE extepk in("+refPKsql+") and coalesce(dr,0)=0";
					try {
						List fieldList = queryfield.queryfieldList(querySql);
						for (Object object : fieldList) {
							String insertSql = "insert into "+dfvo.getMiddletab()+" (pk,contpk,extepk) values ('"+queryfield.getOID(pk_corp)+"','"+object+"','"+pk_role+"')";
							list.add(insertSql);
						}
						int size = list.size();
						if(size>0){
							RetMessage retMessage = queryfield.exectEverySqlByHandCommit(list);
							setReturnMessage(retMessage.getMessage());
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						MessageDialog.showErrorDlg(this, "错误", "授权角色失败");
						return;
					} 
					break;
				}else if(i==2){
					String roleCodePK = getRoleCodeRef().getRefPK();
					if(null == roleCodePK || "".equals(roleCodePK)){
						MessageDialog.showErrorDlg(this, "错误", "请选择角色表字段");
						return;
					}
					String tableCodePK = getTableCodeRef().getRefPK();
					if(null == tableCodePK || "".equals(tableCodePK)){
						MessageDialog.showErrorDlg(this, "错误", "请选择授权控制表字段");
						return;
					}
					ArrayList<String> list = new ArrayList<String>();
					String querySql = "SELECT SM_ROLE.PK_ROLE,"+memorytable+"."+quotecolu+" FROM SM_ROLE,"+memorytable+" WHERE "+roleCodePK+"="+tableCodePK
								+" and SM_ROLE.PK_ROLE in(SELECT pk_role FROM dip_rolegroup_b WHERE pk_role_group "
								+"in(SELECT pk_fp FROM dip_adcontdata_b where pk_contdata_h='"
								+dfvo.getPk_contdata_h()
								+"' and coalesce(dr,0)=0) and coalesce(dr,0)=0 ) and SM_ROLE.PK_ROLE not in(SELECT extepk FROM "+dfvo.getMiddletab()+" )";
					try {
						List fieldList = queryfield.queryFieldSingleSql(querySql);
						for (Object object : fieldList) {
							List values = (List)object;
							String insertSql = "insert into "+dfvo.getMiddletab()+" (pk,contpk,extepk) values ('"+queryfield.getOID(pk_corp)+"','"+values.get(1)+"','"+values.get(0)+"')";
							list.add(insertSql);
						}
						int size = list.size();
						if(size>0){
							queryfield.exectEverySql(list);
							setReturnMessage("已成功授权"+size+"条数据");
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						MessageDialog.showErrorDlg(this, "错误", "授权角色失败");
						return;
					} 
					break;
				}
			}
		}
		this.close();
	}
	public String getReturnMessage() {
		return returnMessage;
	}
	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}
	
}
