package nc.ui.dip.dataformatdef;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillData;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.dataformatdef.DataformatdefBVO;
import nc.vo.dip.dataformatdef.DataformatdefHVO;
import nc.vo.dip.dataformatdef.MyBillVO;
import nc.vo.dip.dataorigin.DipDataoriginVO;
import nc.vo.dip.datarec.DipDatarecHVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;




/**
 *
 *该类是AbstractMyEventHandler抽象类的实现类，
 *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 *@author author
 *@version tempProject version
 */

public class MyEventHandler 
extends AbstractMyEventHandler{


	public MyEventHandler(DataForDefClientUI dataForDefClientUI, ICardController control){
		super(dataForDefClientUI,control);		
	}
	//取得当前ui类
	private DataForDefClientUI getSelfUI(){
		return (DataForDefClientUI) getBillUI();
	}
//	//取得选中的节点对象
//	private VOTreeNode getSelectNode(){
//	return  getSelfUI().getBillTreeSelectNode();
//	}

	@Override
	protected void onBoSave() throws Exception {
		BillData bd=getBillCardPanelWrapper().getBillCardPanel().getBillData();
		if(bd !=null){
			bd.dataNotNullValidate();
		}

		AggregatedValueObject checkVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);
		Object[] ob=checkVO.getChildrenVO();
		if(ob==null||ob.length<=0){
			getSelfUI().showErrorMessage("没有编辑表体！");
		}
		int notnull=0;
		int isnull=0;
		Map<Integer,DataformatdefBVO> map=new HashMap<Integer, DataformatdefBVO>();
		DataformatdefBVO[] bvos=(DataformatdefBVO[]) checkVO.getChildrenVO();
		/*for(int i=0;i<bvos.length;i++){
			map.put(bvos[i].getCode(), bvos[i]);
		}
		for(int i=0;i<bvos.length;i++){
			
		}*/
		String hpk=checkVO.getParentVO().getPrimaryKey();
		if(hpk==null||hpk.length()<=0){
			hpk=HYPubBO_Client.insert((SuperVO) checkVO.getParentVO());

			// write to database
//			checkVO = getBusinessAction().save(checkVO,
//					getUIController().getBillType(), _getDate().toString(),
//					getBillUI().getUserObject(), checkVO);
		}else{
			HYPubBO_Client.update((SuperVO) checkVO.getParentVO());
			
		}
		HYPubBO_Client.deleteByWhereClause(DataformatdefBVO.class, DataformatdefBVO.PK_DATAFORMATDEF_H+"='"+hpk+"'");
		List<DataformatdefBVO> bvol=new ArrayList<DataformatdefBVO>();
		int code=1;
		if(bvos!=null&&bvos.length>0){
			for(int i=0;i<bvos.length;i++){
				if(bvos[i].getDatakind()!=null){
					bvos[i].setPk_dataformatdef_h(hpk);
					bvos[i].setCode(code);
					code++;
					bvol.add(bvos[i]);
				}
			}
			HYPubBO_Client.insertAry(bvol.toArray(new DataformatdefBVO[bvol.size()]));
		}
//		IQueryField qryf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
//		String sql = "delete from dip_dataformatdef_b where pk_dataformatdef_h ='"+hpk+"' and nvl(datakind,' ') = ' '";
//		qryf.exesql(sql);
		//程莉  描述：保存后自动排序并刷新，避免上移下移时序号混乱 日期：2011-5-18
		this.getBillCardPanelWrapper().getBillCardPanel().getBillModel().sortByColumn("code", true);
//		super.onBoRefresh();
	}

	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		super.onBoAdd(bo);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("messflowtype").setEnabled(true);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("messflowdef").setEnabled(true);
	}
	/*
	 * 自定义按钮事件
	 * (non-Javadoc)
	 * @see nc.ui.jy.returnmess.AbstractMyEventHandler#onBoElse(int)
	 */
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		// TODO Auto-generated method stub
		super.onBoElse(intBtn);
	}

	public void onButtonAction(ButtonObject bo) throws Exception {
		int intBtn = Integer.parseInt(bo.getTag());
		long lngTime = System.currentTimeMillis();
		buttonActionBefore(getBillUI(), intBtn);
		switch (intBtn) {
		default:
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
		case 11: onBoLineAdd();break;
		case  IBillButton.InsLine:
			onBoLineIns();
			break;
		case IBtnDefine.Movedup:
			movedUp();
			//super.onBoRefresh();
			break;
		case IBtnDefine.Moveddown:
			movedDown();
			//super.onBoRefresh();
			break;
		}
	}
	public void lodDefaultVo(SuperVO vo,SuperVO[] vos,String type) {
		try {
			getBufferData().clear();
			MyBillVO mvo=new MyBillVO();
			mvo.setParentVO(vo);
			mvo.setChildrenVO(vos);
			getBufferData().clear();
			getBufferData().addVOToBuffer(mvo);
			/*
			addDataToBuffer(vo);
			updateBuffer();*/
			getBufferData().setCurrentRow(0);
			getBufferData().setCurrentVO(getBufferData().getCurrentVO());
			onBoEdit();
			int len=(vos==null?0:vos.length);
			for(int i=len;i<200;i++){
				onBoLineAdd();
				getSelfUI().getBillCardPanel().getBillModel().setValueAt((i+1), i, "code");
			}
			if(type.equals("消息总线")){

				getSelfUI().getBillCardPanel().getBillModel("dip_dataformatdef_b").setCellEditable(1, "datakind", false);
				getSelfUI().getBillCardPanel().getBillModel("dip_dataformatdef_b").setCellEditable(2, "datakind", false);
				getSelfUI().getBillCardPanel().getBillModel("dip_dataformatdef_b").setCellEditable(3, "datakind", false);
				getSelfUI().getBillCardPanel().getBillModel("dip_dataformatdef_b").setCellEditable(0, "datakind", true);
			}

			getBillCardPanelWrapper().getBillCardPanel().getBillTable().changeSelection(0, 0, true, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * 作者：程莉
	 * 功能：上移
	 * 如果是"消息总线"类型，则0,1,2,3 行不能上移
	 * 日期：2011-5-18
	 */
	protected void movedUp() throws Exception{
		DipDatarecHVO hvo=getSelfUI().getHvo();
		String sourcetype=hvo.getSourcetype();//数据来源类型存的主键
		//数据来源类型vo
		DipDataoriginVO ddvo=(DipDataoriginVO) HYPubBO_Client.queryByPrimaryKey(DipDataoriginVO.class, sourcetype);
		String type=ddvo.getName();//数据来源类型
		DataformatdefHVO dhvo=getSelfUI().getDatahvo();
//		if(SJUtil.isNull(dhvo)){
//			MessageDialog.showHintDlg(getSelfUI(), "提示", "请先录入数据保存后再做操作！");
//			return;
//		}else{
			//得到所有行
			int totalrow=this.getBillCardPanelWrapper().getBillCardPanel().getRowCount();
			//判断是否选中一行
			int selectedrow=this.getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().getTable().getSelectedRow();
			if(selectedrow==-1){
				MessageDialog.showHintDlg(getSelfUI(), "提示", "请选择一条数据！");
				return;
			}
					
			//得到当前是第几行，下标从0开始
			int row=this.getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
			if(totalrow>0){
				if(row==0){
					getSelfUI().getButtonManager().getButton(IBtnDefine.Movedup).setEnabled(false);
					MessageDialog.showHintDlg(getSelfUI(), "提示", "已经是第一行了！");
					return;
				}else {
					if(type.equals("消息总线")){				
						if(row==1 || row==2){
							getSelfUI().getButtonManager().getButton(IBtnDefine.Movedup).setEnabled(false);
							getSelfUI().getButtonManager().getButton(IBtnDefine.Moveddown).setEnabled(false);
							return;
						}
						//第四行不能上移
						//2011-6-21 程莉 第5行不能上移，即row=4
						if(row==3||row==4){
							getSelfUI().getButtonManager().getButton(IBtnDefine.Movedup).setEnabled(false);
							return;
						}
					}
					
					MyBillVO billvo=(MyBillVO) getSelfUI().getVOFromUI();
					
					DataformatdefBVO[] bvos=(DataformatdefBVO[]) billvo.getChildrenVO();
//					DataformatdefBVO vo1=bvos[row-1];
//					DataformatdefBVO vo2=bvos[row];
					
					DataformatdefBVO vo1= (DataformatdefBVO)getSelfUI().getBillCardPanel().getBillModel().getBodyValueRowVO(row-1, DataformatdefBVO.class.getName());
					DataformatdefBVO vo2= (DataformatdefBVO)getSelfUI().getBillCardPanel().getBillModel().getBodyValueRowVO(row, DataformatdefBVO.class.getName());
					String pk=vo1.getPrimaryKey();
					Integer code=vo1.getCode();//序号
					vo1.setPrimaryKey(vo2.getPrimaryKey());
					vo1.setCode(vo2.getCode());
					vo2.setPrimaryKey(pk);
					vo2.setCode(code);
					bvos[row-1]=vo2;
					bvos[row]=vo1;
					/*if(vo1.getPrimaryKey()!=null&&vo1.getPrimaryKey().length()>0){
						HYPubBO_Client.update(vo1);
					}
					if(vo2.getPrimaryKey()!=null&&vo2.getPrimaryKey().length()>0){
					HYPubBO_Client.update(vo2);
					}*/
					billvo.setChildrenVO(bvos);
					getBufferData().addVOToBuffer(billvo);
					getBufferData().setCurrentVO(billvo);

					getBillCardPanelWrapper().getBillCardPanel().getBillModel().setBodyRowVO(vo2, row-1);
					getBillCardPanelWrapper().getBillCardPanel().getBillModel().setBodyRowVO(vo1, row);

					String[] formula=getBillCardPanelWrapper().getBillCardPanel().getBodyItem("code").getLoadFormula();
					getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(row-1, formula);
					getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(row, formula);
					
					//this.getBillCardPanelWrapper().getBillCardPanel().getBillTable().changeSelection(totalrow-(totalrow-row)-1, row, true, true);
				}
			}
			//上移的时候，将鼠标焦点也随着上移
			this.getBillCardPanelWrapper().getBillCardPanel().getBillTable().changeSelection(row-1, 1, false, false);
//		}
	}

	/*
	 * 作者：程莉
	 * 功能：下移
	 * 如果是"消息总线",则0,1,2行不能下移,最后一行不能下移
	 * 日期：2011-5-18
	 */
	protected void movedDown() throws Exception{
		DipDatarecHVO hvo=getSelfUI().getHvo();
		String sourcetype=hvo.getSourcetype();//数据来源类型
		//数据来源类型vo
		DipDataoriginVO ddvo=(DipDataoriginVO) HYPubBO_Client.queryByPrimaryKey(DipDataoriginVO.class, sourcetype);
		String type=ddvo.getName();//数据来源类型
		DataformatdefHVO dhvo=getSelfUI().getDatahvo();
//		if(SJUtil.isNull(dhvo)){
//			MessageDialog.showHintDlg(getSelfUI(), "提示", "请先录入数据保存后再做操作！");
//			return;
//		}else{
			//得到当前有多少条数据
			int count=getBillCardPanelWrapper().getBillCardPanel().getRowCount();
			//得到当前所选是第几行，下标从0开始
			int row=this.getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
			if(row<0){
				MessageDialog.showHintDlg(getSelfUI(), "提示", "请选择一条数据！");
				return;
			}
			if(count >0){
				if(row+1==count){
					getSelfUI().getButtonManager().getButton(IBtnDefine.Movedup).setEnabled(false);
					MessageDialog.showHintDlg(getSelfUI(), "提示", "已经是最后一行了！");
					return;
				}else {
					if((type !=null && ! type.trim().equals("")) && type.equals("消息总线")){
						if(row==0 ||row==1 || row==2){
							getSelfUI().getButtonManager().getButton(IBtnDefine.Movedup).setEnabled(false);
							getSelfUI().getButtonManager().getButton(IBtnDefine.Moveddown).setEnabled(false);
							return;
						}
						//第四行不能上移
						if(row==3){
							getSelfUI().getButtonManager().getButton(IBtnDefine.Movedup).setEnabled(false);
							return;
						}
					}
//					MyBillVO billvo=(MyBillVO)getBufferData().getCurrentVO();
					MyBillVO billvo=(MyBillVO)getSelfUI().getVOFromUI();
					DataformatdefBVO[]bvo=(DataformatdefBVO[]) billvo.getChildrenVO();
//					DataformatdefBVO b=bvo[row];
//					DataformatdefBVO bb=bvo[row+1];
					DataformatdefBVO b= (DataformatdefBVO)getSelfUI().getBillCardPanel().getBillModel().getBodyValueRowVO(row, DataformatdefBVO.class.getName());
					DataformatdefBVO bb= (DataformatdefBVO)getSelfUI().getBillCardPanel().getBillModel().getBodyValueRowVO(row+1, DataformatdefBVO.class.getName());
					String pk=b.getPrimaryKey();
					Integer code=b.getCode();//序号
					b.setPrimaryKey(bb.getPrimaryKey());
					b.setCode(bb.getCode());
					bb.setPrimaryKey(pk);
					bb.setCode(code);
					bvo[row]=bb;
					bvo[row+1]=b;
//					HYPubBO_Client.update(b);
//					HYPubBO_Client.update(bb);
					billvo.setChildrenVO(bvo);
					getBufferData().addVOToBuffer(billvo);
					getBufferData().setCurrentVO(billvo);

					getBillCardPanelWrapper().getBillCardPanel().getBillModel().setBodyRowVO(bb, row);
					getBillCardPanelWrapper().getBillCardPanel().getBillModel().setBodyRowVO(b, row+1);

					String[] formula=getBillCardPanelWrapper().getBillCardPanel().getBodyItem("code").getLoadFormula();
					getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(row+1, formula);
					getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(row, formula);
				}
			}
			
			//2011-6-24 程莉 下移的时候，将鼠标焦点也随着下移
			this.getBillCardPanelWrapper().getBillCardPanel().getBillTable().changeSelection(row+1, 1, false, false);
//		}
	}
	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
		DataformatdefHVO hvo=getSelfUI().getDatahvo();
		if(SJUtil.isNull(hvo)){
			MessageDialog.showHintDlg(getSelfUI(), "提示", "请先录入数据保存后再做操作！");
			return;
		}
		super.onBoDelete();

	}


}		

