package nc.ui.dip.alertmanag;

import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillScrollPane.BillTable;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.list.ListEventHandler;
import nc.uif.pub.exception.UifException;
import nc.vo.dip.dateprocess.DateprocessVO;
import nc.vo.dip.processflow.DipProcessflowBVO;
import nc.vo.dip.warningset.DipWarningsetVOA;
import nc.vo.pub.SuperVO;


/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */


public class AlertManagClientUI extends AbstractAlertManagClientUI {
	 private AlertSplitBodyPane bodypan; 
      /* protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}*/
       
	/*public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {	}*/
       /**
   	 * 此方法关联控制器
   	 */
	 public AlertManagClientUI(){
		 super();
////		 getBillListPanel().getUISplitPane().setDividerLocation(200);
////		reWriteUIBody();
//		this.getBillListPanel().getBodyTable().addMouseListener(this);
//		this.getBillListPanel().getHeadTable().addMouseListener(this);
		 
		 if(this.getBillListPanel().getBodyBillModel("dip_warningset").getRowCount()>0){
			 this.getBillListPanel().getHeadTable().changeSelection(0, 0, false, false);
		 }
		 int a=this.getBillListPanel().getHeadTable().getSelectedRow();
//		 this.getBillListPanel().getHeadBillModel().getBillModelData();
//		 this.getBillListPanel().getBodyBillModel("dip_warningset").getBodyValueRowVO(0, "code");
	 }
//	 public void reWriteUIBody()
//		{ 
//		 CircularlyAccessibleValueObject vo[]=null;
//			List<CircularlyAccessibleValueObject> list=new ArrayList<CircularlyAccessibleValueObject>();
//			try {
//				vo=HYPubBO_Client.queryByCondition(DipWarningsetVO.class," tasktype<>'0001AA1000000001FVAD' and  nvl(dr,0)=0");
//			} catch (UifException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			CircularlyAccessibleValueObject vvv[]=null;
//			if(vo!=null&&vo.length>0){
//				vvv=new CircularlyAccessibleValueObject[vo.length];
//				for(int i=0;i<vo.length;i++){
//					String[] names=vo[i].getAttributeNames();
//					DipWarningsetVOA voa=new DipWarningsetVOA();
//					for(int k=0;k<names.length;k++){
//						voa.setAttributeValue(names[k]+"_a", vo[i].getAttributeValue(names[k]));
//					}
//					vvv[i]=voa;
//				}
//			}
//			getBillListPanel().getBodyBillModel("dip_warningset_a").setBodyDataVO(vvv);
//			getBillListPanel().getBodyBillModel("dip_warningset_a").execLoadFormula();
//		}
	 public AlertSplitBodyPane getBodySplitPane(){
			if(bodypan==null){
				bodypan=new AlertSplitBodyPane(true);
			}
			return bodypan;
		}
	 protected ListEventHandler createEventHandler() {
			return new MyEventHandler(this, getUIControl());
		}

		public String getRefBillType() {
			return null;
		}
		
   /*	protected IListController createController() {
   		return new AlertManagClientUICtrl();
   	}*/
	protected void initSelfData() {	
		
	}

	public void setDefaultData() throws Exception {
		
	}
	
	
	
	@Override
	protected void initBillData(String strWhere) throws Exception {
		// TODO Auto-generated method stub
		super.initBillData(strWhere);
		
		if(this.getBillListPanel().getHeadTable().getRowCount()>0){
			 this.getBillListPanel().getHeadTable().changeSelection(0, 0, false, false);
		 }//
//		 int a=this.getBillListPanel().getHeadTable().getSelectedRow();
//		 this.getBillListPanel().getHeadTable().getRowCount();
//		
	}
	@Override
	protected String getInitDataWhere() {
		// TODO Auto-generated method stub
		return " tasktype='0001AA1000000001FVAD' and nvl(dr,0)=0 ";
	}
	@Override
	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub
//		super.bodyRowChange(e);
		BillTable tab=(BillTable) e.getSource();
		int k=tab.getSelectedRow();
		if(k>=0){
			Object obj =getBillListPanel().getHeadBillModel().getValueAt(k, "pk_bustab");
			if(obj!=null){
				String pk_processflow_h=obj.toString();
				try {
					SuperVO vo[]=HYPubBO_Client.queryByCondition(DipProcessflowBVO.class, " pk_processflow_h='"+pk_processflow_h+"' and nvl(dr,0)=0 order by orderno ");
					DateprocessVO provo[]=null;
					if(vo!=null&& vo.length>0){
						DipWarningsetVOA objects[]=new DipWarningsetVOA[vo.length];
						for(int i=0;i<vo.length;i++){
							objects[i]=new DipWarningsetVOA();
							String pk_processflow_b=vo[i].getAttributeValue("pk_processflow_b")==null?"":vo[i].getAttributeValue("pk_processflow_b").toString();
							objects[i].setWcode_a(vo[i].getAttributeValue("code")==null?"":vo[i].getAttributeValue("code").toString());
							objects[i].setWname_a(vo[i].getAttributeValue("name")==null?"":vo[i].getAttributeValue("name").toString());
							objects[i].setTasktype_a(vo[i].getAttributeValue("tasktype")==null?"":vo[i].getAttributeValue("tasktype").toString());
							objects[i].setTabstatus_a(vo[i].getAttributeValue("orderno")==null?"":vo[i].getAttributeValue("orderno").toString());
							provo=(DateprocessVO[]) HYPubBO_Client.queryByCondition(DateprocessVO.class, " pk_processflow_b='"+pk_processflow_b+"' and nvl(dr,0)=0 order by sdate desc ");
							//没有想到更好的办法，所以在循环里面用HYPubBO_Client来查询
							if(provo!=null&&provo.length>0){
								
								if(provo[0].getSuccess()!=null){
									if(provo[0].getSuccess().booleanValue()){
										objects[i].setVdef1_a("成功");//是否成功	
									}else{
										objects[i].setVdef1_a("不成功");//是否成功
									}
									
								}
								objects[i].setVdef2_a(provo[0].getContent()==null?"":provo[0].getContent().toString());//描述
								objects[i].setVdef3_a(provo[0].getEdate()==null?"":provo[0].getEdate().toString());//开始时间
								objects[i].setVdef4_a(provo[0].getSdate()==null?"":provo[0].getSdate().toString());//结束时间	
							}
						}
						getBillListPanel().getBodyBillModel("dip_warningset_a").setBodyDataVO(objects);
						getBillListPanel().getBodyBillModel("dip_warningset_a").execLoadFormula();
					}else{
						getBillListPanel().getBodyBillModel("dip_warningset_a").clearBodyData();
					}
					
				} catch (UifException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}else{
				getBillListPanel().getBodyBillModel("dip_warningset_a").clearBodyData();
			}
			
		}
	}
	
	
}
