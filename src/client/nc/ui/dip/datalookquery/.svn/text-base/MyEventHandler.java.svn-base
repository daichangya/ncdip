package nc.ui.dip.datalookquery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import nc.ui.bd.pub.AbstractBdBillCardUI;
import nc.ui.bd.pub.DefaultBDBillCardEventHandle;
import nc.ui.dip.contwhquery.ContWHQueryClientUI;
import nc.ui.dip.util.ClientEnvDef;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.BillCardUI;
import nc.vo.dip.util.QueryUtilVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;


/**
 * 
 * 该类是AbstractMyEventHandler抽象类的实现类， 主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends DefaultBDBillCardEventHandle {
	DatalookQueryClientUI ui = (DatalookQueryClientUI)this.getBillUI();
	public MyEventHandler(BillCardUI arg0, ICardController arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onBoLineDel() throws Exception {
		CircularlyAccessibleValueObject[] selectedVOs = getBillCardPanelWrapper()
			.getSelectedBodyVOs();
		if (selectedVOs != null && selectedVOs.length > 0){
			for (CircularlyAccessibleValueObject vo : selectedVOs) {
				Object value = vo.getAttributeValue("islock");
				if(null != value && new UFBoolean(value.toString()).booleanValue()){
					MessageDialog.showErrorDlg(ui, "错误", "不能删除已锁定行");
					return;
				}
			}
		}
		super.onBoLineDel();
	}
	public void onButtonAction(ButtonObject bo) throws Exception {
		int intBtn = Integer.parseInt(bo.getTag());
		long lngTime = System.currentTimeMillis();
		buttonActionBefore(getBillUI(), intBtn);

		switch (intBtn) {
		default:
			break;
		case IBillButton.AddLine:
			onBoLineAdd();
			break;
		case IBillButton.DelLine:
			onBoLineDel();
			break;
		case 1: // '\001'
			if (!getBillUI().isBusinessType().booleanValue()) {
				getBillUI().showHintMessage(
						NCLangRes.getInstance().getStrByID("uifactory",
						"UPPuifactory-000061"));
				onBoAdd(bo);
				buttonActionAfter(getBillUI(), intBtn);
			}
			break;

		case 3: // '\003'
			getBillUI().showHintMessage(
					NCLangRes.getInstance().getStrByID("uifactory",
					"UPPuifactory-000067"));
			onBoEdit();
			buttonActionAfter(getBillUI(), intBtn);
			break;

		case 32: // ' '
			getBillUI().showHintMessage(
					NCLangRes.getInstance().getStrByID("uifactory",
					"UPPuifactory-000070"));
			onBoDelete();
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					(new StringBuilder()).append(
							NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000071")).append(
									System.currentTimeMillis() - lngTime).toString());
			break;

		case 0: // '\0'
			getBillUI().showHintMessage(
					NCLangRes.getInstance().getStrByID("uifactory",
					"UPPuifactory-000072"));
			onBoSave();
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					(new StringBuilder()).append(
							NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000073")).append(
									System.currentTimeMillis() - lngTime).toString());
			break;

		case 7: // '\007'
			onBoCancel();
			getBillUI().showHintMessage("");
			buttonActionAfter(getBillUI(), intBtn);
			break;

		case 8: // '\b'
			getBillUI().showHintMessage(
					NCLangRes.getInstance().getStrByID("uifactory",
					"UPPuifactory-000076"));
			onBoRefresh();
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					(new StringBuilder()).append(
							NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000077")).append(
									System.currentTimeMillis() - lngTime).toString());
			break;

		}
	}


	/**
	 * 编辑
	 */
	public void onBoEdit() throws Exception {
		int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable()
		.getSelectedRow();
		CircularlyAccessibleValueObject selectedVOs[] = getBillCardPanelWrapper()
		.getSelectedBodyVOs();
		if (row == -1 || selectedVOs == null || selectedVOs.length == 0) {
			JOptionPane.showMessageDialog(getBillCardPanelWrapper()
					.getBillCardPanel(), "必须选择一条数据");
		} else {
			super.onBoEdit();
			((AbstractBdBillCardUI) getBillUI()).setBDUIExtendStatus(2);
			int rowCount = getBillCardPanelWrapper().getBillCardPanel()
			.getRowCount();
			getBillCardPanelWrapper().getBillCardPanel().getBillModel()
			.setRowEditState(true);
			int index = 0;
			int rows[] = new int[rowCount - 1];
			for (int i = 0; i < rowCount; i++) {
				if (i != row) {
					rows[index++] = i;
				}
			}
			getBillCardPanelWrapper().getBillCardPanel().getBillModel()
			.setNotEditAllowedRows(rows);
		}
	}

	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCancel();
	}

	@Override
	protected void onBoSave() throws Exception {

		String op="";
		StringBuffer str = new StringBuffer();
		QueryUtilVO[] bvos=new QueryUtilVO[ui.getBillCardPanel().getBillModel().getRowCount()];
		for (int row = 0; row < ui.getBillCardPanel().getBillModel().getRowCount(); row++) {
			bvos[row]=(QueryUtilVO) getBillCardPanelWrapper().getBillCardPanel().getBillModel().getBodyValueRowVO(row, QueryUtilVO.class.getName());
			String kh=(String) ui.getBillCardPanel().getBillModel().getValueAt(row, "kh");
			if(kh!=null&&kh.trim().length()>0){
				if(str.length()<=0){
					str.append(" and (");
				}
				str.append(" "+kh);
			}

			
			String getvalue = ui.getBillCardPanel().getBillModel().getValueAt(row, "getvalue")==null?"":ui.getBillCardPanel().getBillModel().getValueAt(row, "getvalue").toString();
			if(!"".equals(getvalue)){
				if(str.length()<=0){
					str.append(" and (");
				}
				String ename = ui.getBillCardPanel().getBillModel().getValueAt(row, "ename")==null?"":ui.getBillCardPanel().getBillModel().getValueAt(row, "ename").toString();
				String cztype = ui.getBillCardPanel().getBillModel().getValueAt(row, "cztype")==null?"":ui.getBillCardPanel().getBillModel().getValueAt(row, "cztype").toString();
				//目前只考虑到字符类型的sql拼接，后续处理小数类型
				//2011-6-10
				//if("like".equals(cztype))
				String cov=convertType(cztype);
				if(cov.length()==0){
					
				}else if("包含".equals(cztype))
					str.append(" "+ename+" "+convertType(cztype)+"'%"+getvalue+"%'");
				else if("开始是".equals(cztype))
					str.append(" "+ename+" "+convertType(cztype)+"'"+getvalue+"%'");
				else if("结束是".equals(cztype))
					str.append(" "+ename+" "+convertType(cztype)+"'%"+getvalue+"'");
				else
					str.append(" "+ename+" "+convertType(cztype)+" '"+getvalue+"' ");
				
			}else{
				if(null != bvos[row].getIslock() && bvos[row].getIslock().booleanValue()){
					throw new Exception("已锁定行不能为空");
				}
			}
			String ope=(String) ui.getBillCardPanel().getBillModel().getValueAt(row, "ope");
			if(ope!=null&&ope.trim().length()>0){
				if(str.length()<=0){
					str.append(" and (");
				}
				op=" "+ope+" " ;
				str.append(ope);
			}
			/*if(row<(ui.getBillCardPanel().getBillModel().getRowCount()-1)){
				String getvalue2 = ui.getBillCardPanel().getBillModel().getValueAt(row+1, "getvalue")==null?"":ui.getBillCardPanel().getBillModel().getValueAt(row+1, "getvalue").toString();
				String getvalue3 = ui.getBillCardPanel().getBillModel().getValueAt(row+1, "cztype")==null?"":ui.getBillCardPanel().getBillModel().getValueAt(row+1, "cztype").toString();
				if(getvalue2!=null&&getvalue2.length()>0&&getvalue3!=null&&getvalue3.length()>0){
					str.append(op);
				}
			}*/
			
		}
		if(((DatalookQueryClientUI)getBillUI()).isAuth){
			ClientEnvDef.putQueryAuthVO(((DatalookQueryClientUI)getBillUI()).key,bvos );
		}else{
			ClientEnvDef.putQueryVO(((DatalookQueryClientUI)getBillUI()).key,bvos );
		}
		if(str.length()>0){
			str.append(")");
		}
		str.append(" and 1=1 ");
		ui.returnSql = str.toString()/*.substring(0,str.toString().length()-op.length())*/ ; 
	
		
		
		/*
//		super.onBoSave();
		String sql = "";
		StringBuffer str = new StringBuffer();
		for (int row = 0; row < ui.getBillCardPanel().getBillModel().getRowCount(); row++) {
			String getvalue = ui.getBillCardPanel().getBillModel().getValueAt(row, "getvalue")==null?"":ui.getBillCardPanel().getBillModel().getValueAt(row, "getvalue").toString();
			if(!"".equals(getvalue)){
				String ename = ui.getBillCardPanel().getBillModel().getValueAt(row, "ename")==null?"":ui.getBillCardPanel().getBillModel().getValueAt(row, "ename").toString();
				String cztype = ui.getBillCardPanel().getBillModel().getValueAt(row, "cztype")==null?"":ui.getBillCardPanel().getBillModel().getValueAt(row, "cztype").toString();
				//目前只考虑到字符类型的sql拼接，后续处理小数类型
				//if("like".equals(cztype))
				//2011-06-20 程莉
				String cov=convertType(cztype);
				if(cov.length()==0){
					
				}else if("like".equals(convertType(cztype)))
					//str.append(" and "+ename+" "+convertType(cztype)+" %"+getvalue+"%"+"");
					str.append(" and "+ename+" "+convertType(cztype)+" '%"+getvalue+"%'"+"");
				else
					str.append(" and "+ename+" "+convertType(cztype)+" '"+getvalue+"' "+"");
//				liyunzhe  给查询模板增加缓存，
				String[] strtr=new String[3];
				strtr[0]=ename;
				strtr[1]=cztype;
				strtr[2]=getvalue;
				List<String[]> list=new ArrayList<String[]> ();
				if(map.get(ui.key)!=null){
					list=map.get(ui.key);
					boolean flag=false;
					for(int kk=0;kk<list.size();kk++){
						if(list.get(kk)!=null&&list.get(kk)[0].equals(ename)){
							list.remove(kk);
							list.add(strtr);
							map.put(ui.key, list);
							flag=true;
						}
					}
					
					if(!flag){
						list.add(strtr);
						map.put(ui.key, list);
					}
					
				}else{
					list.add(strtr);
					map.put(ui.key, list);

				}
				
			}else{
				if(map.get(ui.key)!=null){
					List<String[]> list=map.get(ui.key);
					String ename = ui.getBillCardPanel().getBillModel().getValueAt(row, "ename")==null?"":ui.getBillCardPanel().getBillModel().getValueAt(row, "ename").toString();
					for(int kk=0;kk<list.size();kk++){
						if(list.get(kk)!=null&&list.get(kk)[0].equals(ename)){
							list.remove(kk);
							map.put(ui.key, list);
							break;
						}
					}
				}
			}
				
			    
			
			
		}
		
		
		
		
		
		str.append(" and 1=1 ");
		ui.returnSql = str.toString() ; 
	*/}
	public String convertType(String cztype){
		//SX,小于,小于等于,等于,大于,大于等于,包含
		String type = "";
		if("".equals(cztype))
			type = "";
		if("小于".equals(cztype))
			type = "<";
		if("小于等于".equals(cztype))
			type = "<=";
		if("等于".equals(cztype))
			type = "=";
		if("大于".equals(cztype))
			type = ">";
		if("大于等于".equals(cztype))
			type = ">=";
		if("包含".equals(cztype) || "开始是".equals(cztype) || "结束是".equals(cztype))
			type = "like";


		return type;
	}

}