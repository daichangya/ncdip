package nc.ui.dip.effectdef;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.jdbc.framework.exception.DbException;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.MessageEvent;
import nc.ui.pub.MessageListener;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.ButtonVOFactory;
import nc.vo.dip.datachange.DipDatachangeHVO;
import nc.vo.dip.effectdef.CdSbodyVO;
import nc.vo.logging.Debug;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.button.ButtonVO;

public class EffSetDlg extends UIDialog implements MessageListener {
	class IvjEventHandler implements ActionListener {

		final EffSetDlg this$0;

		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == getBtnSave()){
				try {
					//添加影响因素
					CdSbodyVO[] bvo = (CdSbodyVO[])getBillUI().getVOFromUI().getChildrenVO();
					if(bvo[0].getPrimaryKey()==null||bvo[0].getPrimaryKey().length()<20){
						HYPubBO_Client.insertAry(bvo);
					}else{
						AggregatedValueObject vo=getBillUI().getChangedVOFromUI();
						if(vo.getChildrenVO()!=null&&vo.getChildrenVO().length>0){
							HYPubBO_Client.updateAry((CdSbodyVO[])vo.getChildrenVO());
						}
					}

					//凭证合并设置
					CdSbodyVO[] bvo2 = (CdSbodyVO[])getBillUI2().getVOFromUI().getChildrenVO();
					if(bvo2[0].getPrimaryKey()==null||bvo2[0].getPrimaryKey().length()<20){
						HYPubBO_Client.insertAry(bvo2);
					}else{
						AggregatedValueObject vo=getBillUI2().getChangedVOFromUI();
						if(vo.getChildrenVO()!=null&&vo.getChildrenVO().length>0){
							HYPubBO_Client.updateAry((CdSbodyVO[])vo.getChildrenVO());
						}
					}

					//分录合并设置
					int j=0;
					for(int i=0;i<3;i++){
						if(jr[i].isSelected()){
							j=i;
						}
					}
					hvo.setResults(j);	
					Object obj=jc.getSelectedItem();
					if(obj!=null){
						hvo.setDef_str_2(obj.toString().trim());	
					}
					Object obj2=jc1.getSelectedItem();
					if(obj2!=null){
						hvo.setExplanation(obj2.toString().trim());
					}
					
					

					//凭证发送参数设置
					String sender=getNewJPanel().getJtfsender().getText();
					hvo.setDef_str_1(sender);


					//转换后合并设置
					boolean sfhb=getCreHB().getJcbishb().isSelected();
					if(sfhb){
						if(getCreHB().getJrdbtn1().isSelected()){
							hvo.setSfhb(0);
						}else{
							hvo.setSfhb(1);
						}
					}else{
						hvo.setSfhb(-1);
					}
					//精度控制
					boolean isselect=getDatajd().getJcbishb().isSelected();
					if(isselect){
						if(getDatajd().getJcbisqyq().isSelected()){
							
							String j1=getDatajd().getJcomb1().getSelectedItem().toString();
							
							hvo.setDataprecision("Y");
							hvo.setBeforeprecision(j1==null?null:Integer.parseInt(j1));
						}else{
							hvo.setDataprecision("N");
							hvo.setBeforeprecision(null);
						}
						
						if(getDatajd().getJcbisqyh().isSelected()){
							String j2=getDatajd().getJcomb2().getSelectedItem().toString();
							
							hvo.setDataprecision("Y");
							
							hvo.setAfterprecision(j2==null?null:Integer.parseInt(j2));	
						}else{
							if(!(hvo.getDataprecision()!=null&&"Y".equals(hvo.getDataprecision()))){
								hvo.setDataprecision("N");	
							}
							
							hvo.setAfterprecision(null);
						}
						
					}else{
						hvo.setDataprecision("N");
						hvo.setBeforeprecision(null);
						hvo.setAfterprecision(null);
					}
					/*if(getDataPXPane().getOrgnizeRefPnl().getRefPK()!=null){
						hvo.setPxzd(getDataPXPane().getOrgnizeRefPnl().getRefPK());
					}else{
						hvo.setPxzd(null);
					}*/
					if(getDataPXPane().getISpxCheckBox().isSelected()){
						hvo.setPxzd("Y");
					}else{
						hvo.setPxzd("N");
					}
					HYPubBO_Client.update(hvo);

					close();

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}else {
				close();
				return;
			}
		}

		IvjEventHandler() {
			this$0 = EffSetDlg.this;
		}
	}

	private static final long serialVersionUID = 0xe2e9c134242a4e4aL;

	ClientUI billUI;

	UFBoolean m_isAuthorizedSettle;

	UIPanel ButtonPane;

	private UIPanel MainPanel;

	private UIScrollPane MainScrollPane;

	private UIScrollPane UIScrollPane;

	private UIButton btnSave;

	private ButtonObject btnSaveObj;

	private UIButton btnCancel;
	private ButtonObject btnCancelObj;

	private String hpk;
	private boolean isEffect;
	private IvjEventHandler ivjEventHandler;
	public int flag=0;//1表示添加影响因素。2表示凭证模板设置

	public String sqlReturn = "";
	DipDatachangeHVO hvo;
	public EffSetDlg(Container parent, UFBoolean isAuthorizedSettle,
			String hpk,boolean isEffect,DipDatachangeHVO hvo) {

		super(parent);
		billUI = null;

		m_isAuthorizedSettle = null;
		ButtonPane = null;
		MainPanel = null;
		MainScrollPane = null;
		UIScrollPane = null;
		ivjEventHandler = new IvjEventHandler();
		btnSave = null;
		btnSaveObj = null;
		btnCancel=null;
		btnCancelObj=null;
		m_isAuthorizedSettle = isAuthorizedSettle;
		this.hpk=hpk;
		this.isEffect=isEffect;
		this.hvo=hvo;
		initialize();
	}

	private void initConnection()
	{
		getBtnSave().addActionListener((ActionListener) ivjEventHandler);
		getBtnCancel().addActionListener((ActionListener)ivjEventHandler);
	}



	@Override
	protected void close() {
		super.close();
	}

	public ClientUI getBillUI() {
		if (billUI == null) {
			if (isAuthorizedSettle()) {
				billUI = new ClientUI();
				billUI.initdef(hpk,true);
				//billUI.initdef(hpk,isEffect);
			} else {
				billUI = new ClientUI();
				//billUI.initdef(hpk,isEffect);
				billUI.initdef(hpk,true);
			}
			try {
				//使表体可以处于编辑状态
				billUI.setBillOperate(IBillOperate.OP_EDIT);
			} catch (Exception e) {
				e.printStackTrace();
			}

			billUI.addMessageListener(this);
		}
		//2011-7-19 影响因素第一行没录入数据，其他行都不可编辑。现在不需做控制，固注释了
		/*int row=billUI.getBillCardPanel().getRowCount();
		if(row>0){
			String value=null;
			for(int i=0;i<row;i++){
				value=billUI.getBillCardPanel().getBodyValueAt(i, "ys")==null?"":billUI.getBillCardPanel().getBodyValueAt(0, "ys").toString();
				if(value==null || "".equals(value) || value.length()==0){
					//设置其它列不可编辑
					if(i !=0 && (i+1)<row){
						billUI.getBillCardPanel().getBillModel("dip_effectdef").setCellEditable((i+1), "ys", false);
					}
				}else{
					if(i+1<row){
						billUI.getBillCardPanel().getBillModel("dip_effectdef").setCellEditable((i+1), "ys", true);
					}
				}
			}
		}*/
		//设置不可排序
		billUI.getBillCardPanel().getBillTable().setSortEnabled(false);
		return billUI;
	}

	//分录合并
	ClientUI billUI2=null;
	public ClientUI getBillUI2() {
		if (billUI2 == null) {
			if (isAuthorizedSettle()) {
				billUI2 = new ClientUI();
				billUI2.initdef(hpk,false);
			} else {
				billUI2 = new ClientUI();
				billUI2.initdef(hpk,false);
			}
			try {
				//使表体可以处于编辑状态
				billUI2.setBillOperate(IBillOperate.OP_EDIT);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//2011-7-19 影响因素第一行没录入数据，其他行都不可编辑。现在不需做控制，固注释了
			/*int row=billUI2.getBillCardPanel().getRowCount();
			if(row>0){
				String value=null;
				for(int i=0;i<row;i++){
					value=billUI2.getBillCardPanel().getBodyValueAt(i, "ys")==null?"":billUI2.getBillCardPanel().getBodyValueAt(0, "ys").toString();
					if(value==null || "".equals(value) || value.length()==0){
						//设置其它列不可编辑
						if(i !=0 && (i+1)<row){
							billUI2.getBillCardPanel().getBillModel("dip_effectdef").setCellEditable((i+1), "ys", false);
						}
					}else{
						if(i+1<row){
							billUI.getBillCardPanel().getBillModel("dip_effectdef").setCellEditable((i+1), "ys", true);
						}
					}
				}
			}*/
			//设置不可排序
			billUI2.getBillCardPanel().getBillTable().setSortEnabled(false);
			billUI2.addMessageListener(this);
		}
		return billUI2;
	}

	UITabbedPane ivjTablledPane=null;
	private UITabbedPane getTabbedPane() {
		if (ivjTablledPane == null) {
			try {
				ivjTablledPane = new UITabbedPane();
				ivjTablledPane.setName("FiledTabbedPane");
				ivjTablledPane.addTab("影响因素", getBillUI());
				ivjTablledPane.addTab("转换前合并", getBillUI2());
				ivjTablledPane.addTab("转换后合并", getCreHB());
				ivjTablledPane.addTab("分录合并",getMidPanel());
				ivjTablledPane.addTab("数据排序", getDataPXPane());
				ivjTablledPane.addTab("数据精度", getDatajd());
				ivjTablledPane.addTab("模板参数", getNewJPanel());
				
				
				
			} catch (Exception ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjTablledPane;
	}
	private DataPXPane pkpan;
	public  DataPXPane getDataPXPane(){
		if(pkpan==null){
			pkpan=new DataPXPane(hvo);
		}
		return pkpan;
	}
	/**
	 * 凭证发送参数
	 */
	private NewJPanel jpanel;
	public NewJPanel getNewJPanel(){
		if(jpanel==null){
			jpanel=new NewJPanel(hvo);
		}
		return jpanel;
	}
	/**
	 * 转换后合并设置
	 */
	private CreJPanel cpanel;
	public CreJPanel getCreHB(){
		if(cpanel==null){
			cpanel=new CreJPanel(hvo);
		}
		return cpanel;
	}
	/**
	 * 转换前后精度设置panel
	 * */
	private JDJPanel jdpanel;
	public JDJPanel getDatajd(){
		if(jdpanel==null){
			jdpanel=new JDJPanel(hvo);
		}
		return jdpanel;
	}

	/**
	 * 生成DLGPanel
	 * 
	 * @return
	 */
	private JPanel midPanel;
	private JPanel orderPanel;
	String message;

	JRadioButton[] jr=new JRadioButton[3];
	JComboBox jc=new JComboBox();
	
	String cc[]={"","摘要"};
	JComboBox jc1=new JComboBox(cc);
	String[] ss={"相同科目+相同辅助核算项","相同科目+相同辅助核算项+相同借贷方向","相同科目+相同辅助核算项+相同借贷方向+相同摘要"};
	//String[] sss={"相同方向+相同科目+相同辅助核算项"};
	private ButtonGroup bg=new ButtonGroup();
	private JPanel getMidPanel() {
		if (midPanel == null) {
			int sslen=ss.length;
			midPanel = new JPanel();
			midPanel.setLayout(new java.awt.GridLayout());
			midPanel.setBounds(4, 26, 200, 7);
			int len;
			if(message==null){
				len=sslen;
			}else{
				len=sslen+1;
			}
			midPanel.setLayout(new GridLayout(len+1,1,1,1));
			if(message!=null){
				midPanel.add(new JLabel(message));
			}

			for(int i=0;i<sslen;i++){
				if(i==0){
					jr[i]=new JRadioButton(ss[i],true);
				}else{
					jr[i]=new JRadioButton(ss[i],false);
				}
				midPanel.add(jr[i]);
				bg.add(jr[i]);
			}
			
//			if(orderPanel==null){
//				orderPanel=new JPanel();
//				orderPanel.setBounds(14, 26, 200, 10);
//				jr2[0]=new JRadioButton(sss[0],false);
//				orderPanel.add(jr2[0]);
//				//bg.add(jr2[0]);
//			}
			JLabel lab=new JLabel("方向+科目+辅助核算");
			jc.setSize(30, 5);
			jc1.setSize(30,5);
			jc.addItem("");
			//lab.add(jj);
//			midPanel.add(lab);
//			midPanel.add(jj);
			List list=getItemValue();
			if(list.size()!=0){
				for(int i=0;i<list.size();i++){
					if(list.get(i)!=null){
						jc.addItem(list.get(i).toString());	
					}
					
				}
			}
			if(orderPanel==null){
				orderPanel=new JPanel();
//				orderPanel.setBorder(createt)
				orderPanel.setBounds(12, 26, 200, 2);
				orderPanel.setPreferredSize(new Dimension(20,30));
				
				//orderPanel.setb
				orderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
				JLabel jl=new JLabel("分录排序:");
				jl.setBounds(25, 120, 100, 25);

				JLabel jl3=new JLabel("凭证排序:");
				jl3.setBounds(25, 70, 100, 25);
				getDataPXPane().add(jl);
				getDataPXPane().add(jl3);
				getDataPXPane().add(lab);
				lab.setBounds(100, 120, 120, 25);
				getDataPXPane().add(jc);
				jc.setBounds(215, 120, 100, 25);
				JLabel lb2=new JLabel(" + ");
				lb2.setBounds(318, 120,15, 25);
				getDataPXPane().add(lb2);
				getDataPXPane().add(jc1);
				jc1.setBounds(335, 120, 70, 25);
			}
			
			midPanel.add(orderPanel);
			

		}
		jr[hvo.getResults()].setSelected(true);
		jc.setSelectedItem(hvo.getDef_str_2());
		jc1.setSelectedItem(hvo.getExplanation());
		return midPanel;
	}
	private List getItemValue(){
		IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
		List list=new ArrayList();
		String sql=" select bd.bdname "+
					" from (select distinct a.pk_bdinfo, bdname "+
							" from bd_subjass a, bd_bdinfo b "+
							" where a.pk_bdinfo = b.pk_bdinfo "+
							" and nvl(a.dr, 0) = 0 "+
							" and nvl(b.dr, 0) = 0) bd "+
					" order by bd.bdname ";
		try {
			list=iq.queryfieldList(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	//保存按钮
	private UIButton getBtnSave(){
		if(btnSave == null){
			btnSave = new UIButton();
			btnSave.setName("btnSave");
			btnSave.setText(getBtnSaveObj().getName());
		}
		return btnSave;
	}

	private ButtonObject getBtnSaveObj(){
		if(btnSaveObj == null){
			ButtonVO btnVO = ButtonVOFactory.getInstance().build(0);
			btnSaveObj = btnVO.buildButton();
		}
		return btnSaveObj;
	}

	//取消按钮
	private UIButton getBtnCancel(){
		if(btnCancel == null){
			btnCancel = new UIButton();
			btnCancel.setName("btnCancel");
			btnCancel.setText(getBtnCancelObj().getName());
		}
		return btnCancel;
	}

	private ButtonObject getBtnCancelObj(){
		if(btnCancelObj == null){
			ButtonVO btnVO = ButtonVOFactory.getInstance().build(7);
			btnCancelObj = btnVO.buildButton();
		}
		return btnCancelObj;
	}
	private UIPanel getButtonPane(){
		if(ButtonPane == null){
			ButtonPane = new UIPanel();
			ButtonPane.setLayout( new FlowLayout(FlowLayout.CENTER));
			ButtonPane.setName("ButtonPane");
			ButtonPane.setPreferredSize(new Dimension(30, 40));
			//flowLayout1.setAlignment(0);
//			ButtonPane.add(getBtnEffect(), null);
//			ButtonPane.add(getBtnModel(), null);
			ButtonPane.add(getBtnSave(), null);
			ButtonPane.add(getBtnCancel(), null);

		}
		return ButtonPane;
	}

	private UIPanel getMainPanel() {
		if (MainPanel == null) {
			MainPanel = new UIPanel();
			MainPanel.setLayout(new BorderLayout());
			MainPanel.setName("MainPanel");
			MainPanel.add(getButtonPane(),"South");
			MainPanel.add(getTabbedPane(), "Center");

		}
		return MainPanel;
	}

	private UIScrollPane getMainScrollPane() {
		if (MainScrollPane == null) {
			MainScrollPane = new UIScrollPane();
			MainScrollPane.setName("MainScrollPane");
			MainScrollPane.setViewportView(getMainPanel());

		}
		return MainScrollPane;
	}

	private UIScrollPane getUIScrollPane() {
		if (UIScrollPane == null) {
			UIScrollPane = new UIScrollPane();
			UIScrollPane.setName("TablePane");
		}
		return UIScrollPane;
	}

	private void handleException(Exception e) {
		String msg = e.getMessage() != null && e.getMessage().length() != 0 ? e
				.getMessage() : e.getClass().getName();
				Debug.error(msg, e);
				MessageDialog.showErrorDlg(this, null, msg);
	}

	private void initialize() {
		setContentPane(getMainPanel());
		setSize(450, 350);
		setLocation(330, 200);
		setTitle(NCLangRes.getInstance().getStrByID("400122", "参数设置"));
//		initTable();
		initConnection();
		//getBillUI().getBillCardPanel().getBodyItem("effectname").setEdit(true);
		// setButtonStatus();
	}

	private void initTable() {
		getTabbedPane();
//		getUIScrollPane().setViewportView(getTabbedPane());
	}

	public boolean isAuthorizedSettle() {
		return m_isAuthorizedSettle.booleanValue();
	}



	public void setIsAuthorizedSettle(UFBoolean authorizedSettle) {
		m_isAuthorizedSettle = authorizedSettle;
	}

	protected void processWindowEvent(WindowEvent e) {
		try {
			if (e.getID() == 201) {
				boolean closeResult = getBillUI().onClosing();
				if (!closeResult) {
					return;
				}
			}
		} catch (Exception exception) {
			MessageDialog.showErrorDlg(this, null, exception.getMessage());
			return;
		}
		super.processWindowEvent(e);
	}

	/*	private void onBtnSave()
	{
	   try
	   {
	       getBillUI().onButtonAction(getBtnSaveObj());
	      // setReturnSql(getBillUI().returnSql);
	       this.close();

	   }
	   catch(Exception e)
	   {
	       handleException(e);
	   }
//	   setButtonStatus();
	}*/

	public String getReturnSql(){
		return sqlReturn;
	}

	public void setReturnSql(String sql){
		sqlReturn = sql;
	}
	/*	private void onBtnCancel()
	{
	   try
	   {
	       getBillUI().onButtonAction(getBtnCancelObj());

	   }
	   catch(Exception e)
	   {
	       handleException(e);
	   }
//	   setButtonStatus();
	}*/
	public void messageReceived(MessageEvent e) {
		if (e.getMessageType() == 1) {
			throw new BusinessRuntimeException(e.getMessageContent().toString());
		} else {
			return;
		}
	}

}
