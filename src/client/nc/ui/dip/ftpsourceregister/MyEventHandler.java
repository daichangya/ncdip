package nc.ui.dip.ftpsourceregister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillData;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.card.BillCardUI;
import nc.util.dip.sj.RetMessage;
import nc.vo.dip.ftpsourceregister.FtpSourceRegisterVO;


/**
  *
  *该类是AbstractMyEventHandler抽象类的实现类，
  *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
  *@author author
  *@version tempProject version
  */
  
  public class MyEventHandler 
                                          extends AbstractMyEventHandler{

	public MyEventHandler(BillCardUI billUI, ICardController control){
		super(billUI,control);		
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		
//		增行
		int k=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		List<Integer> list=new ArrayList<Integer>();
		for(int i=0;i<k;i++){
			String code=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new FtpSourceRegisterVO().CODE);
			String name=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new FtpSourceRegisterVO().NAME);
			String address=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new FtpSourceRegisterVO().ADDRESS);
			String username=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new FtpSourceRegisterVO().USERNAME);
			String password=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new FtpSourceRegisterVO().PASSWORD);
			String port=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new FtpSourceRegisterVO().PORT);
			String path=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new FtpSourceRegisterVO().PATH);
			if((code==null||code.trim().equals(""))&&(name==null||name.trim().equals(""))&&(address==null||address.trim().equals(""))
					&&(username==null||username.trim().equals("")&&(password==null||password.trim().equals(""))&&(port==null||port.trim().equals(""))
							&&(path==null||path.trim().equals("")))){
				list.add(i);
			}
		}
		if(list!=null&&list.size()>0){
			int in[]=new int[list.size()];
			for(int i=0;i<list.size();i++){
				in[i]=list.get(i);
			}
			getBillCardPanelWrapper().getBillCardPanel().getBillModel().delLine(in);
		}
		Map<String,String> nameMap=new HashMap<String,String>();
		Map<String,String> codeMap=new HashMap<String,String>();
		
		
		
		BillData bd=getBillCardPanelWrapper().getBillCardPanel().getBillData();
		if(bd !=null){
			bd.dataNotNullValidate();
		}
		for(int i=0;i<k;i++){
			String code=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new FtpSourceRegisterVO().CODE);
			String name=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new FtpSourceRegisterVO().NAME);
			if(code!=null){
				if(codeMap.get(code.trim())==null){
					codeMap.put(code, k+"");
				}else{
					getBillUI().showErrorMessage("编码不能重复");
					return;
				}	
			}
			if(name!=null){
				if(nameMap.get(name.trim())==null){
					nameMap.put(name, k+"");
				}else{
					getBillUI().showErrorMessage("名称不能重复");
					return;
				}	
			}
			
		}
//		FilePathClientUI ui=(FilePathClientUI)getBillUI();
//		String pk=(String)ui.getBillCardPanel().getBodyItem("pk_filepath").getValueObject();
//		if(pk ==null || pk.trim().equals("")){
//			pk=" ";
//		}
//		String code=(String)ui.getBillCardPanel().getBodyItem("code").getValueObject();
//		String name=(String)ui.getBillCardPanel().getBodyItem("name").getValueObject();
//		
//		IUAPQueryBS bs=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//		
//		Collection ccode=bs.retrieveByClause(FilepathVO.class, "code='"+code+"' and nvl(dr,0)=0 and pk_filepath <>'"+pk+"'");
//		if(ccode !=null){
//			if(ccode.size()>=1){
//				ui.showWarningMessage("该【"+code+"】编码已经存在！");
//				return;
//			}
//		}
//		
//		Collection cname=bs.retrieveByClause(FilepathVO.class, "name='"+name+"' and nvl(dr,0)=0 and pk_filepath <>'"+pk+"'");
//		if(cname !=null){
//			if(cname.size()>=1){
//				ui.showWarningMessage("该【"+name+"】名称已经存在！");
//				return;
//			}
//		}
		super.onBoSave();
//		MyBillVO mybillvo=(MyBillVO) getBufferData().getCurrentVO();
//		FilepathVO fvo=(FilepathVO) mybillvo.getParentVO();
//		String path=fvo.getDescriptions();
//		ITaskExecute ite=(ITaskExecute) NCLocator.getInstance().lookup(ITaskExecute.class.getName());
//		ite.createFilePath(path);
		
	}
   
	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
			//Map<String,String> map=IContrastUtil.getDocRefMap();
//			String key="dip_filepath";
//			String value=map.get(key);
			int bodycount=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
			if(bodycount>=0){
				String pk=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(bodycount, new FtpSourceRegisterVO().getPKFieldName().toLowerCase())==null?"":
					getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(bodycount, new FtpSourceRegisterVO().getPKFieldName().toLowerCase()).toString();
				//String isref=SJUtil.isExitRef(value,pk);
//				if(isref!=null&&isref.length()>0){
//					getBillUI().showErrorMessage("此条数据被引用，不可删除！");
//					return ;
//				}
				Integer flag=MessageDialog.showOkCancelDlg(this.getBillUI(),"提示","是否要删除?");
				if(flag==1){
				    this.onBoLineDel();
				    HYPubBO_Client.deleteByWhereClause(FtpSourceRegisterVO.class,new FtpSourceRegisterVO().getPKFieldName().toLowerCase()+"='"+pk+"'");
				}
			}else{
				getBillUI().showErrorMessage("请选择要删除的节点！");
				return;
			}
            onBoRefresh();
	}		
	@Override
	public void onBoAdd(ButtonObject arg0) throws Exception {
		// TODO Auto-generated method stub
		int rows=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		if(rows>0){
			super.onBoEdit();
			super.onBoLineAdd();
		}else{
			super.onBoAdd(arg0);
			super.onBoLineAdd();
		}
	}
	public void ini(){
		try {
			doBodyQuery(" 1=1 order by code");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onBoRefresh() throws Exception {
		// TODO Auto-generated method stub
		super.onBoRefresh();
		ini();
	}
	
	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		super.onBoEdit();
		onBoLineAdd();
	}

	@Override
	protected void onBoFtpTest() throws Exception{
		// TODO Auto-generated method stub
		int bodycount=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		String pk_ftpsourceregister="";
		if(bodycount>=0){
			pk_ftpsourceregister=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(bodycount, new FtpSourceRegisterVO().getPKFieldName().toLowerCase())==null?"":
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(bodycount, new FtpSourceRegisterVO().getPKFieldName().toLowerCase()).toString();
		}else{
			getBillUI().showErrorMessage("请选择要删除的节点！");
			return;
		}
		IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
		RetMessage ret=iq.checkFtpServiceRegisterIsTure(pk_ftpsourceregister);
		
		if(ret!=null&&ret.getIssucc()){
		  MessageDialog.showWarningDlg(getBillUI(), "成功", "连接成功");
		}else{
			getBillUI().showErrorMessage("连接失败");
		}
		
		
	}
	
	
}