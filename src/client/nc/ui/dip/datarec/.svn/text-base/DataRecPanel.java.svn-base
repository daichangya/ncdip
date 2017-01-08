package nc.ui.dip.datarec;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.util.dip.sj.IContrastUtil;
import nc.vo.dip.runsys.DipRunsysBVO;

public class DataRecPanel extends UIDialog{
	
	class IevActionListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource()==getBtnSave()){
				onBoSave();
			}
		}
		
	}
	
	
	String dataorigin;//来源类型，例如主动抓取。
	String display;//显示在左边的数据，多个数据已,号隔开
	String value;//显示在右边的数据，多个数据以,号隔开
	BillItem item;
	private UIPanel buttonPane;
	private UIButton btnSave;
	BillCardPanel billcardpanel;
	public DataRecPanel(BillItem item,String dataorigin,String display) {
		// TODO Auto-generated constructor stub
		this.dataorigin=dataorigin;
		this.display=display;
		this.item=item;
		this.value=item.getValueObject()==null?"":item.getValueObject().toString();
		init();
	}
	
	public void init(){
		
		setSize(600, 350);
		setLocation(330, 200);
		this.setLayout(new BorderLayout());
		this.add(BorderLayout.NORTH, getButtonPane());
		this.add(BorderLayout.CENTER, getMainPane());
		addActionListener();
	}
	public void addActionListener(){
		getBtnSave().addActionListener( new IevActionListener());
	}
	private UIPanel getButtonPane()
	{
	   if(buttonPane == null)
	   {
	       FlowLayout flowLayout1 = new FlowLayout();
	       buttonPane = new UIPanel();
	       buttonPane.setLayout(flowLayout1);
	       buttonPane.setName("ButtonPane");
	       buttonPane.setPreferredSize(new Dimension(20, 30));
	       flowLayout1.setAlignment(0);
	       buttonPane.add(getBtnSave(), null);
//	       ButtonPane.add(getBtnCancel(), null);
	      
	   }
	   return buttonPane;
	}
	private UIButton getBtnSave()
	{
	   if(btnSave == null)
	   {
	       btnSave = new UIButton();
	       btnSave.setName("btnSave");
	       btnSave.setText("保存");
	   }
	   return btnSave;
	}
	private UIPanel getMainPane(){
		if (billcardpanel == null) {
			 billcardpanel=new BillCardPanel();
			 billcardpanel.loadTemplet("H4H3H3Ha", null, ClientEnvironment.getInstance().getUser().getPrimaryKey(), ClientEnvironment.getInstance().getCorporation().getPk_corp());
			 if(display!=null){
				if(display.contains(",")){
					String displayAry[]=display.split(",");
					String valueAry[]=null;
					if(value!=null&&value.contains(",")){
						valueAry=value.split(",");	
					}
					
					if(displayAry!=null&&displayAry.length>0){
						DipRunsysBVO [] bvos=new DipRunsysBVO[displayAry.length];
						for(int i=0;i<displayAry.length;i++){
							bvos[i]=new DipRunsysBVO();
							bvos[i].setSysname(displayAry[i]);
							if(valueAry!=null&&valueAry.length>=i+1){
								bvos[i].setSysvalue(valueAry[i]);	
							}
							
						}
						billcardpanel.getBillModel().setBodyDataVO(bvos);
					}

				}else{
					DipRunsysBVO [] bvos=new DipRunsysBVO[1];
					bvos[0]=new DipRunsysBVO();
					bvos[0].setSysname(display);
					bvos[0].setSysvalue(value==null?"":value);
					billcardpanel.getBillModel().setBodyDataVO(bvos);
				}
			}
			

		}
		return billcardpanel;
		
	}
	
	public void onBoSave(){
		int count=billcardpanel.getBillModel().getRowCount();
		StringBuffer sb=new StringBuffer();
		if(count>0){
			boolean flag5=false;
			boolean flag6=false;
			for(int i=0;i<count;i++){
				DipRunsysBVO bvo=(DipRunsysBVO) billcardpanel.getBillModel().getBodyValueRowVO(i, DipRunsysBVO.class.getName());
				if(bvo!=null){
					if(bvo.getSysvalue()!=null&&bvo.getSysvalue().trim().length()>0){
						if(bvo.getSysvalue().contains(",")){
							MessageDialog.showErrorDlg(this, "提示", "第"+(i+1)+"行参数值不能包含符号\",\"");
							return;	
						}
//						if//如果是 主动抓取 
						
						sb.append(bvo.getSysvalue().trim()+",");
					}else{
						//如果是webservice抓取，第五个值和第六个值可同时为空
//						if(IContrastUtil.DATAORIGIN_WEBSERVICEZQ.equals(dataorigin)&&(i==4||i==5)){
//							if(i==4){
//								flag5=true;
//							}
//							if(i==5){
//								flag6=true;
//							}
//						}else{
							MessageDialog.showErrorDlg(this, "提示", "第"+(i+1)+"行参数值不能为空");
							return;	
//						}
					}
				}else{
				    MessageDialog.showErrorDlg(this, "提示", "第"+(i+1)+"行不能为空");
				    return;
				}
			}
			
//			if(IContrastUtil.DATAORIGIN_WEBSERVICEZQ.equals(dataorigin)){
//				if(flag5!=flag6){
//					MessageDialog.showErrorDlg(this, "提示", display.split(",")[4]+","+display.split(",")[5]+"数两个参数值只能同时为空或者同时不为空");
//				    return;
//				}
//			}
			
			
		}else{
			MessageDialog.showErrorDlg(this, "提示", "界面没有数据");
		    return;
		}
		item.setValue(sb.toString().substring(0, sb.toString().length()-1));
		this.close();
	}
}
