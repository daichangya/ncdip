package nc.ui.dip.credence;





import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;


import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.MessageEvent;
import nc.ui.pub.MessageListener;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillListPanel;
import nc.vo.dip.warningset.MyBillVO;
import nc.vo.pub.BusinessRuntimeException;

public class FzhsDlg extends UIDialog implements MessageListener,ActionListener{
	public void actionPerformed(ActionEvent e) {
		UIButton bt=(UIButton) e.getSource();
		
		if(bt.getName().equals("确定")){
			fo="";
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					Object gs=getUIScrollPane().getBodyValueAt(i, "gs");
					fo=fo+"\""+getUIScrollPane().getBodyValueAt(i, "pk")+":\"+"+((gs==null||gs.toString().length()<=0)?"\"\"":gs.toString());
					if(i<(list.size()-1)){
						fo=fo+"+"+"\",\"+";
					}
				}
			}
			close();
		}
	}
	String fo="";
	BillListPanel listpanel;
	String pk_spsq;
	MyBillVO[] mybillvo;
	UIPanel MainPanel;
	UIPanel ButtonPane;
	BillCardPanel mainpanel;
	UIButton btnSave ;
	List list;
	String pk_credence_h;String pk_datadef;
	public String getFO(){
		return fo;
	}
	public FzhsDlg(List list,String pk_credence_h,String pk_datadef,Object value){
		super();
		this.pk_credence_h=pk_credence_h;
		this.pk_datadef=pk_datadef;
		this.list=list;
		if(value!=null){
			fo=value.toString();
		}
		initialize();
		
	}
	private void initialize() {
		setContentPane(getMainPanel());
		setSize(400, 300);
		setLocation(120, 190);
		setTitle("辅助核算");
	}
	private UIPanel getMainPanel() {
		if (MainPanel == null) {
			MainPanel = new UIPanel();
			MainPanel.setLayout(new BorderLayout());
			MainPanel.setName("MainPanel");
			MainPanel.add(getButtonPane(),"North");
			MainPanel.add(getUIScrollPane(), "Center");

		}
		return MainPanel;
	}
	public BillCardPanel getUIScrollPane(){
		if(mainpanel==null){
			mainpanel= new BillCardPanel();
			mainpanel.loadTemplet("H4H1Hh", null, ClientEnvironment.getInstance().getUser().getPrimaryKey(), ClientEnvironment.getInstance().getCorporation().getPk_corp()) ;
			mainpanel.getBillTable().setSortEnabled(false);
			if(fo!=null&&fo.length()>0){
				String[] s=fo.split("\\+\",\"\\+");
				boolean istrue=true;
				if(s==null||s.length!=list.size()){
					istrue=false;
				}
				for(int k=0;k<list.size();k++){
					Map map=(Map) list.get(k);
					mainpanel.addLine();
					String key=map.get("bdname".toUpperCase()).toString();
					mainpanel.setBodyValueAt(key, k, "pk");
					mainpanel.getBodyItem("pk").setEnabled(false);

					for(int i=0;i<s.length;i++){
						String[] si=s[i].split(":\"\\+");
						if(key.equals(si[0].substring(1))){
//							mainpanel.setBodyValueAt(si[0].substring(1), i, "pk");
							mainpanel.setBodyValueAt(si[1], k, "gs");
							break;
						}
					}
				}
			}else{
				for(int i=0;i<list.size();i++){
					Map map=(Map) list.get(i);
					mainpanel.addLine();
					mainpanel.setBodyValueAt(map.get("bdname".toUpperCase()).toString(), i, "pk");
					mainpanel.getBodyItem("pk").setEnabled(false);
				}
			}
			UIRefPane rf=(UIRefPane) mainpanel.getBodyItem("gs").getComponent();

			FzhsListener listener = new FzhsListener(this,"gs","gs",rf,pk_credence_h,pk_datadef);
			if(rf!=null){
				rf.getUIButton().removeActionListener(rf.getUIButton().getListeners(ActionListener.class)[0]);
				rf.getUIButton().addActionListener(listener);
				rf.setAutoCheck(false);
				rf.setEditable(false);
			}
//			listpanel.setBodyValueVO(vos);
		/*	listpanel.getBodyTable().addMouseListener(new MouseAdapter(){
				@Override
				public void mouseExited(MouseEvent mouseevent) {
				}
				@Override
				public void mouseReleased(MouseEvent mouseevent) {
//					int row=listpanel.getHeadTable().getSelectedRow();
//					listpanel.setBodyValueVO(mybillvo[row].getChildrenVO());
//					listpanel.getBodyBillModel().execLoadFormula();
//					listpanel.updateUI();
				}
				public void mouseClicked(MouseEvent e) {
//					mouseClick(e);
				}
				public void mousePressed(MouseEvent mouseevent) {		
					
				}
			});*/
		}
		return mainpanel;
	}
	private UIPanel getButtonPane()
	{
	   if(ButtonPane == null)
	   {
	       FlowLayout flowLayout1 = new FlowLayout();
	       ButtonPane = new UIPanel();
	       ButtonPane.setLayout(flowLayout1);
	       ButtonPane.setName("ButtonPane");
	       ButtonPane.setPreferredSize(new Dimension(20, 30));
	    		   
	    		   btnSave = new UIButton();
	    	       btnSave.setName("确定");
	    	       btnSave.setText("确定");
	    	       btnSave.addActionListener(this);
	    		   ButtonPane.add(btnSave);//.add(btnSave,null);
	       
	      
	   }
	   return ButtonPane;
	}

//	private BillListPanel getUITablePane1rc() {
//		if (listpanel == null) {
//			listpanel = new BillListPanel();
//			
//			listpanel.loadTemplet("H4H2H5", null, ClientEnvironment.getInstance().getUser().getPrimaryKey(), ClientEnvironment.getInstance().getCorporation().getPk_corp()) ;
//			listpanel.setSize(1200, 700);
//			listpanel.getHeadTable().addMouseListener(new MouseAdapter(){
//				@Override
//				public void mouseExited(MouseEvent mouseevent) {
//				}
//				@Override
//				public void mouseReleased(MouseEvent mouseevent) {
//					int row=listpanel.getHeadTable().getSelectedRow();
//					listpanel.setBodyValueVO(mybillvo[row].getChildrenVO());
//					listpanel.getBodyBillModel().execLoadFormula();
////					listpanel.updateUI();
//				}
//				public void mouseClicked(MouseEvent e) {
////					mouseClick(e);
//				}
//				public void mousePressed(MouseEvent mouseevent) {		
//					
//				}
//			});
//			/*ArapSpsqVO[] vos = null;
//			try {
//				vos =(ArapSpsqVO[]) HYPubBO_Client.queryByCondition(ArapSpsqVO.class, "(vdef1 = '"+pk_spsq+"' or pk_spsq = '"+pk_spsq+"') or (dr = 1 and (vdef1 = '"+pk_spsq+"' or pk_spsq = '"+pk_spsq+"'))");
//			} catch (UifException e) {
//				e.printStackTrace();
//			}
//			listpanel.setHeaderValueVO(vos);
//			listpanel.getHeadBillModel().execLoadFormula();
//			listpanel.getParentListPanel().setShowThMark(true);*/
//			/*DipWarningsetVO[] hvo=new DipWarningsetVO[mybillvo.length];
//			for(int i=0;i<hvo.length;i++){
//				hvo[i]=(DipWarningsetVO) mybillvo[i].getParentVO();
//			}
//			listpanel.setHeaderValueVO(hvo);
//			if(hvo.length>0){
//				listpanel.getHeadTable().getSelectionModel().setSelectionInterval(0, 0);
//				listpanel.setBodyValueVO(mybillvo[0].getChildrenVO());
//			}*/
//			listpanel.getBodyBillModel().execLoadFormula();
//			listpanel.getHeadBillModel().execLoadFormula();
//		}
//		return listpanel;
//	}
	public void messageReceived(MessageEvent e) {
		if (e.getMessageType() == 1) {
			throw new BusinessRuntimeException(e.getMessageContent().toString());
		} else {
			return;
		}
		
	}
 
}
