package nc.ui.dip.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillData;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.card.BillCardUI;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.message.MsrVO;


/**
 *
 *该类是AbstractMyEventHandler抽象类的实现类，
 *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 *@author author
 *@version tempProject version
 */

public class MyEventHandler 
extends AbstractMyEventHandler{

	BillCardUI billUI;

	ICardController control;
	public MyEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	
		
		this.billUI=billUI;
		this.control=control;
	}
	public boolean checkPropertys(String propertysValue,String name,int row){
		if(propertysValue==null||propertysValue.trim().equals("")){
			return true;
		}
		String errmessage="第"+row+"行"+name+"格式错误,应该是a=b,如果是多个用“;”号隔开";
		if(propertysValue.contains(";")){
			String prepertys[]=propertysValue.split(";");
			if(prepertys!=null&&prepertys.length>0){
				for(int a=0;a<prepertys.length;a++){
					if(prepertys[a]!=null&&prepertys[a].length()>0){
						if(!(prepertys[a].contains("=")&&prepertys[a].split("=")!=null&&prepertys[a].split("=").length==2&&prepertys[a].split("=")[0]!=null&&prepertys[a].split("=")[0].trim().length()>0&&
								prepertys[a].split("=")[1]!=null&&prepertys[a].split("=")[1].trim().length()>0)){
							getBillUI().showErrorMessage(errmessage);
							return false;
						}
					}else{
						getBillUI().showErrorMessage(errmessage);
						return false;
					}
				}
			}else{
				getBillUI().showErrorMessage(errmessage);
				return false;
			}
			
		}else{
			if(propertysValue.contains("=")){
				String prepertys[]=propertysValue.split("=");
				if(!(prepertys!=null&&prepertys.length==2&&prepertys[0]!=null&&prepertys[0].trim().length()>0&&prepertys[1]!=null&&prepertys[1].trim().length()>0)){
					getBillUI().showErrorMessage(errmessage);
					return false;
				}
			}else{
				getBillUI().showErrorMessage(errmessage);
				return false;
			}
		}
		return true;
	}
	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		
		int k=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		List<Integer> list=new ArrayList<Integer>();
		for(int i=0;i<k;i++){
			
			String code=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i,  MsrVO.CODE);
			String name=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i,  MsrVO.NAME);
			String address=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i,  MsrVO.ADDRESS);
			String duankou=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i,  MsrVO.DUANKOU)==null?"":getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i,  MsrVO.DUANKOU).toString();
			String uname=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i,  MsrVO.UNAME)==null?"":getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i,  MsrVO.UNAME).toString();
			String upass=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i,  MsrVO.UPASS)==null?"":getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i,  MsrVO.UPASS).toString();
			String vdef9=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i,  MsrVO.VDEF9)==null?"":getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i,  MsrVO.VDEF9).toString();
			String vdef8=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i,  MsrVO.VDEF8)==null?"":getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i,  MsrVO.VDEF8).toString();
			Boolean vdef10=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i,  MsrVO.VDEF10)==null?false:(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i,  MsrVO.VDEF10);
			if(!(vdef10!=null&&vdef10.booleanValue())){
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(false, i,  MsrVO.VDEF10);
			}
			
			if((code==null||code.trim().equals(""))&&(name==null||name.trim().equals(""))&&(address==null||address.trim().equals(""))&&(duankou==null||duankou.trim().equals(""))&&(uname==null||uname.trim().equals(""))&&(upass==null||upass.trim().equals(""))){
//				getBillCardPanelWrapper().getBillCardPanel().getBillTable().changeSelection(i, 0, false, false);
//				getBillCardPanelWrapper().getBillCardPanel().delLine();
				list.add(i);
			}
			if(vdef9!=null&&vdef9.trim().length()>0){
				 String itemname=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getItemByKey(MsrVO.VDEF9).getName();
				if(!checkPropertys(vdef9,itemname,i+1)){
					return;
				}
			}
			if(vdef8!=null&&vdef8.trim().length()>0){
				 String itemname=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getItemByKey(MsrVO.VDEF8).getName();
				if(!checkPropertys(vdef8,itemname,i)){
					return;
				}
			}
		}
		
		if(list!=null&&list.size()>0){
			int in[]=new int[list.size()];
			for(int i=0;i<list.size();i++){
				in[i]=list.get(i);
			}
			getBillCardPanelWrapper().getBillCardPanel().getBillModel().delLine(in);
		}
		
		
		BillData bd=getBillCardPanelWrapper().getBillCardPanel().getBillData();
		if(bd !=null){
			bd.dataNotNullValidate();
		}
//		//此处增加表头重复校验：数据类型编码、名称
//		MyClientUI ui=(MyClientUI)getBillUI();
////		主键
//		String pk=(String)ui.getBillCardPanel().getBodyItem("pk_dip_msr").getValueObject();
//		if(pk ==null || pk.trim().equals("")){
//			pk=" ";//保存时避免重复的可以添加
//		}
//		
//		String code=(String)ui.getBillCardPanel().getBodyItem("code").getValueObject();
//		String name=(String)ui.getBillCardPanel().getBodyItem("name").getValueObject();
//		IUAPQueryBS bs=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//		
//		Collection ccode=bs.retrieveByClause(MsrVO.class, "code='"+code+"' and nvl(dr,0)=0 and pk_dip_msr <>'"+pk+"'");
//		
//		if(ccode !=null){
//			if(ccode.size()>=1){
//				ui.showWarningMessage("该【"+code+"】编码已经存在！");
//				return;
//			}
//		}
//		
//		Collection cname=bs.retrieveByClause(MsrVO.class, "name='"+name+"' and nvl(dr,0)=0 and pk_dip_msr <>'"+pk+"'");
//		if(cname !=null){
//			if(cname.size()>=1){
//				ui.showWarningMessage("该【"+name+"】名称已经存在！");
//				return;
//			}
//		}
		super.onBoSave();		
		ini();
	}
    
	/*删除判断，如果被引用不可删除
	 * 2011-5-28
	 * zlc*/
	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
			Map<String,String> map=IContrastUtil.getDocRefMap();
			String key="dip_msr";
			String value=map.get(key);
			int bodycount=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
			if(bodycount>=0){
				String pk=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(bodycount, new MsrVO().getPKFieldName().toLowerCase())==null?"":
					getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(bodycount, new MsrVO().getPKFieldName().toLowerCase()).toString();
				String isref=SJUtil.isExitRef(value,pk);
				if(isref!=null&&isref.length()>0){
					getBillUI().showErrorMessage("此条数据被引用，不可删除！");
					return ;
				}
				Integer flag=MessageDialog.showOkCancelDlg(this.getBillUI(),"提示","是否要删除?");
				if(flag==1){
				    this.onBoLineDel();
				    HYPubBO_Client.deleteByWhereClause(MsrVO.class,new MsrVO().getPKFieldName().toLowerCase()+"='"+pk+"'");
				}
			}else{
				getBillUI().showErrorMessage("请选择要删除的节点！");
				return;
			}
//		super.onBoDelete();
	}

	@Override
	public void onBoAdd(ButtonObject arg0) throws Exception {
		int rows=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		if(rows>0){
			super.onBoEdit();
			super.onBoLineAdd();
		}else{
			super.onBoAdd(arg0);
			super.onBoLineAdd();
		}
		
//		for(int i=0;i<50;i++){
//			getBillCardPanelWrapper().getBillCardPanel().getBodyItem("");
//		}
	}

	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		super.onBoQuery();
		getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormula();
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
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		super.onBoEdit();
		onBoLineAdd();
	}
	
	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		ini();
	}
	
	
	
}