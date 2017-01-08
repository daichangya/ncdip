package nc.ui.dip.dataproce;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JPanel;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.itf.dip.pub.ITaskExecute;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.dip.dlg.AskDLG;
import nc.ui.dip.warningset.WarningSetClientUI;
import nc.ui.pub.IFuncWindow;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.util.NCOptionPane;
import nc.ui.pub.ddc.datadict.DDCBO_Client;
import nc.ui.pub.tools.BannerDialog;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.pub.VOTreeNode;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.ui.trade.treecard.TreeCardEventHandler;
import nc.ui.uap.sf.SFClientUtil;
import nc.util.dip.sj.RetMessage;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.dip.dataproce.DipDataproceBVO;
import nc.vo.dip.dataproce.DipDataproceHVO;
import nc.vo.dip.warningset.DipWarningsetBVO;
import nc.vo.dip.warningset.DipWarningsetVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.core.BizObject;
import nc.vo.pub.core.ObjectNode;
import nc.vo.pub.ddc.datadict.DDCData;
import nc.vo.pub.ddc.datadict.Datadict;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.trade.pub.IExAggVO;

/**
 *
 *该类是一个抽象类，主要目的是生成按钮事件处理的框架
 *@author author
 *@version tempProject version
 */

public class AbstractMyEventHandler 
extends TreeCardEventHandler{

	IUAPQueryBS queryBS=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());

	private DataProceUI  getSelfUI(){
		return (DataProceUI) getBillUI();
	}


	public AbstractMyEventHandler(BillTreeCardUI billUI, ICardController control){
		super(billUI,control);		
	}
	private CircularlyAccessibleValueObject[] getChildVO(AggregatedValueObject retVo){
		CircularlyAccessibleValueObject childVos[] = null;
		if(retVo instanceof IExAggVO)
			childVos = ((IExAggVO) retVo).getAllChildrenVO();
		else 
			childVos=retVo.getChildrenVO();
		return childVos;
	}

	protected void onBoElse(int intBtn) throws Exception {
		/*switch(intBtn){
		case IBtnDefine.DataProce:
			onBoDataPorce(intBtn);
			break;
		case IBtnDefine.CreateTable:
			onBoCreateImport(intBtn);
			break;*/
//		case IBtnDefine.DataValidate:
//		onBoValidate(intBtn);
//		break;
		//} 
		super.onBoElse(intBtn);
		switch (intBtn) {
		case IBtnDefine.addFolderBtn:
			onBoAddFolder();
			break;
		case IBtnDefine.editFolderBtn:
			onBoEditFolder();
			break;
		case IBtnDefine.delFolderBtn:
			onBoDeleteFolder();
			break;
		}

	}

	public void onBoEditFolder() throws Exception  {
		// TODO Auto-generated method stub
		
	}



	public void onBoDeleteFolder() throws Exception {
		// TODO Auto-generated method stub
		
	}



	public void onBoAddFolder() throws Exception {
		// TODO Auto-generated method stub
		
	}

//	private void onBoValidate(int intBtn) {

//	// TODO Auto-generated method stub
//	DataProceUI dpui=(DataProceUI) getBillUI();
//	int row=getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().getTable().getSelectedRow();
//	if(row==-1){
//	dpui.showWarningMessage("请在表体选择一天信息！");
//	return;
//	}else{
//	//弹出数据校验窗体
//	ToftPanel toft=SFClientUtil.showNode("H4H1H0",IFuncWindow.WINDOW_TYPE_DLG);
//	DataVerifyClientUI ui=(DataVerifyClientUI) toft;
//	}

//	}


	protected void onBoCreateImport(int intBtn) {
		createTable();
	}
	/**
	 * 最终目的拼sql语句，自动创建表 
	 * 1.判断是否有此要创建的表：如果有，则查询是否有数据，有数据则给予提示，无数据则直接删除
	 * 2.判断类型：类型目前除int型无长度外，其余的都有长度，如果类型是小数，得其小数位数 
	 * 3.判断是否必输项 
	 * 4.判断是否是主键 
	 * 
	 * @author cl
	 * @throws Exception
	 */
	IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	Integer flags=0;//删除标志：1代表点击了确定按钮
	public  void createTable() {
		// 获取节点
		DataProceUI ui=(DataProceUI) getBillUI();
		String pk_node = ui.selectnode;
		if ("".equals(pk_node)) {
			NCOptionPane.showMessageDialog(this.getBillUI(), "请选择一个节点。");
			return;
		} else {
			// 根据字段名获得表头的加工表名对应值
			String tableName=(String)this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("procetab").getValueObject();
			// 根据字段名获得表体字段对应值：中文名称、英文名称、类型、长度、小数位数、是否主键

			int row = this.getBillCardPanelWrapper().getBillCardPanel().getRowCount();
			String cname = null;
			String ename = null;
			String type = null;
			int length = 0;
			int deciplace = 0;// 小数位数
			boolean ispk = false;// 是否主键
			boolean issyspk=false;//是否系统预置主键

			String pkcolname = null;// 用来存放最后指定哪一列是主键的变量
			boolean isExist=isTableExist(tableName);
			if(isExist){
				//1.查询是否有数据:有数据给予提示，没数据则删除
				StringBuffer buffer=new StringBuffer();
				buffer.append("select 1 from ");
				buffer.append(tableName);

				ArrayList al=null;
				try{
					al=(ArrayList)queryBS.executeQuery(buffer.toString(), new MapListProcessor());
					//存在该表且有数据
					if(al.size()>=1){
						JPanel jp=new JPanel();
						AskDLG adlg=new AskDLG(jp,"提示","系统已经建表！有数据！确认是否删除?",new String[]{"删除结构后重建表结构","转换更新表结构操作"});
						adlg.showModal();
						if(adlg.getRes()==0){
							//删除该表,重新创建
							StringBuffer delSql=new StringBuffer();
							delSql.append("drop table ");
							delSql.append(tableName);
							iq.exesql(delSql.toString());
						}else if(adlg.getRes()==1) {
							//转换更新表结构
						}else{
							//点击取消
							getSelfUI().showWarningMessage("你点击了取消按钮，没有做任何操作！");
							return;
						}
					}else{
						flags = MessageDialog.showOkCancelDlg(this.getBillUI(), "提示", "系统已经建表！没有数据！是否确认删除?");
						if(flags==1){
							//存在该表，但没数据，把该表删除
							StringBuffer delSql=new StringBuffer();
							delSql.append("drop table ");
							delSql.append(tableName);
							iq.exesql(delSql.toString());
						}else{
							//点击取消
							getSelfUI().showWarningMessage("你点击了取消按钮，没有做任何操作！");
							return;
						}
					}
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
			StringBuffer str = new StringBuffer();

			str.append("create table " + tableName);
			str.append("\n");// 换行
			str.append("(");
			str.append("\n");
			if(row >=1){
				for (int i = 0; i < row; i++) {
					cname = (String) this.getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel("dip_dataproce_b")
					.getValueAt(i, "cname");//中文名称		

					ename = (String) this.getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel("dip_dataproce_b")
					.getValueAt(i, "ename");//英文名称
					if(ename==null||ename.length()<=0){
						getBillUI().showErrorMessage("英文名称不能为空！");
						return;
					}

					type = (String) this.getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel("dip_dataproce_b")
					.getValueAt(i, "type");//数据类型
					if(type==null||type.length()<=0){
						getBillUI().showErrorMessage("数据类型不能为空！");
						return;
					}

					str.append(ename.toUpperCase());//英文名称转换为大写
					str.append("\t");

					//Char,Date,Decimal,Integer,Varchar
					if(type.equals("CHAR")){
						//得到长度
						/*
						 * 增加给类型的默认长度 char和 除Integer，number、data型，如果没设长度，默认length为100
						 * number、，若没设置length 默认为20，deciplace 默认为8
						 * 2011-5-16
						 * zlc*/
						Object o=this.getBillCardPanelWrapper()
						.getBillCardPanel().getBillModel("dip_dataproce_b")
						.getValueAt(i, "length");
						if(o!=null){
							length = (Integer) o;
						}
						else{
							this.getBillCardPanelWrapper()
							.getBillCardPanel().getBillModel("dip_dataproce_b")
							.setValueAt(100, i, "length");
						}
						str.append(type);
						//判断是否是主键、系统预置主键
						if (this.getBillCardPanelWrapper().getBillCardPanel()
								.getBillModel("dip_dataproce_b").getValueAt(
										i, "ispk") == null && this.getBillCardPanelWrapper().getBillCardPanel()
										.getBillModel("dip_dataproce_b").getValueAt(
												i, "issyspk") == null) {
							ispk = false;
							str.append("(");
							str.append(length);
							str.append(")");
							str.append(",");
							str.append("\n");
						} else if(this.getBillCardPanelWrapper().getBillCardPanel()
								.getBillModel("dip_dataproce_b").getValueAt(
										i, "ispk") == null && this.getBillCardPanelWrapper().getBillCardPanel()
										.getBillModel("dip_dataproce_b").getValueAt(
												i, "issyspk") != null) {

							issyspk = Boolean.parseBoolean(this
									.getBillCardPanelWrapper()
									.getBillCardPanel().getBillModel(
									"dip_dataproce_b").getValueAt(i,
									"issyspk").toString());
							if (issyspk == false) {
								str.append("(");
								str.append(length);
								str.append(")");
								str.append(",");
								str.append("\n");
							} else {
								pkcolname = ename;
								str.append("(");
								str.append(length);
								str.append(")");
								str.append(" not null,");
								str.append("\n");

							}
						}else if(this.getBillCardPanelWrapper().getBillCardPanel()
								.getBillModel("dip_dataproce_b").getValueAt(
										i, "ispk") != null && this.getBillCardPanelWrapper().getBillCardPanel()
										.getBillModel("dip_dataproce_b").getValueAt(
												i, "issyspk") == null){
							ispk = Boolean.parseBoolean(this
									.getBillCardPanelWrapper()
									.getBillCardPanel().getBillModel(
									"dip_dataproce_b").getValueAt(i,
									"ispk").toString());
							if(ispk==false){
								str.append("(");
								str.append(length);
								str.append(")");
								str.append(",");
								str.append("\n");
							}else{
								pkcolname=ename;
								str.append("(");
								str.append(length);
								str.append(")");
								str.append(" not null,");
								str.append("\n");
							}

						}else if(this.getBillCardPanelWrapper().getBillCardPanel()
								.getBillModel("dip_dataproce_b").getValueAt(
										i, "ispk") != null && this.getBillCardPanelWrapper().getBillCardPanel()
										.getBillModel("dip_dataproce_b").getValueAt(
												i, "issyspk") != null){
							ispk = Boolean.parseBoolean(this
									.getBillCardPanelWrapper()
									.getBillCardPanel().getBillModel(
									"dip_dataproce_b").getValueAt(i,
									"ispk").toString());
							issyspk = Boolean.parseBoolean(this
									.getBillCardPanelWrapper()
									.getBillCardPanel().getBillModel(
									"dip_dataproce_b").getValueAt(i,
									"issyspk").toString());
							if (ispk == false && issyspk==false) {
								str.append("(");
								str.append(length);
								str.append(")");
								str.append(",");
								str.append("\n");
							}else if(ispk==true && issyspk==false){
								pkcolname = ename;
								str.append("(");
								str.append(length);
								str.append(")");
								str.append(" not null,");
								str.append("\n");
							}else if((ispk==false && issyspk==true)||(ispk==true && issyspk==true)){
								pkcolname=ename;
								str.append("(");
								str.append(length);
								str.append(")");
								str.append(" not null,");
								str.append("\n");
							}

						}


					}else if(type.equals("DATE")){						
						//判断是否是主键、系统预置主键
						if (this.getBillCardPanelWrapper().getBillCardPanel()
								.getBillModel("dip_dataproce_b").getValueAt(
										i, "ispk") == null && this.getBillCardPanelWrapper().getBillCardPanel()
										.getBillModel("dip_dataproce_b").getValueAt(
												i, "issyspk") == null) {
							ispk = false;
							str.append("VARCHAR(19)");
							str.append(",");
							str.append("\n");
						} else if(this.getBillCardPanelWrapper().getBillCardPanel()
								.getBillModel("dip_dataproce_b").getValueAt(
										i, "ispk") == null && this.getBillCardPanelWrapper().getBillCardPanel()
										.getBillModel("dip_dataproce_b").getValueAt(
												i, "issyspk") != null) {

							issyspk = Boolean.parseBoolean(this
									.getBillCardPanelWrapper()
									.getBillCardPanel().getBillModel(
									"dip_dataproce_b").getValueAt(i,
									"issyspk").toString());
							if (issyspk == false) {
								str.append("VARCHAR(19)");
								str.append(",");
								str.append("\n");
							} else {
								pkcolname = ename;
								str.append(" not null,");
								str.append("\n");

							}
						}else if(this.getBillCardPanelWrapper().getBillCardPanel()
								.getBillModel("dip_dataproce_b").getValueAt(
										i, "ispk") != null && this.getBillCardPanelWrapper().getBillCardPanel()
										.getBillModel("dip_dataproce_b").getValueAt(
												i, "issyspk") == null){
							ispk = Boolean.parseBoolean(this
									.getBillCardPanelWrapper()
									.getBillCardPanel().getBillModel(
									"dip_dataproce_b").getValueAt(i,
									"ispk").toString());
							if(ispk==false){
								str.append("VARCHAR(19)");
								str.append(",");
								str.append("\n");
							}else{
								pkcolname=ename;
								str.append("VARCHAR(19)");
								str.append(" not null,");
								str.append("\n");
							}

						}else if(this.getBillCardPanelWrapper().getBillCardPanel()
								.getBillModel("dip_dataproce_b").getValueAt(
										i, "ispk") != null && this.getBillCardPanelWrapper().getBillCardPanel()
										.getBillModel("dip_dataproce_b").getValueAt(
												i, "issyspk") != null){
							ispk = Boolean.parseBoolean(this
									.getBillCardPanelWrapper()
									.getBillCardPanel().getBillModel(
									"dip_dataproce_b").getValueAt(i,
									"ispk").toString());
							issyspk = Boolean.parseBoolean(this
									.getBillCardPanelWrapper()
									.getBillCardPanel().getBillModel(
									"dip_dataproce_b").getValueAt(i,
									"issyspk").toString());
							if (ispk == false && issyspk==false) {
								str.append("VARCHAR(19)");
								str.append(",");
								str.append("\n");
							}else if(ispk==true && issyspk==false){
								pkcolname = ename;
								str.append("VARCHAR(19)");
								str.append(" not null,");
								str.append("\n");
							}else if((ispk==false && issyspk==true)||(ispk==true && issyspk==true)){
								pkcolname=ename;
								str.append(" not null,");
								str.append("\n");
							}
						}

					}else if(type.equals("NUMBER")){
						//长度
						Object o=this.getBillCardPanelWrapper()
						.getBillCardPanel().getBillModel("dip_dataproce_b")
						.getValueAt(i, "length");
						if(o!=null){
							length = (Integer) o;
						}
						else{
							this.getBillCardPanelWrapper()
							.getBillCardPanel().getBillModel("dip_dataproce_b")
							.setValueAt(20, i, "length");
						}
						Object obj=this
						.getBillCardPanelWrapper()
						.getBillCardPanel().getBillModel(
						"dip_dataproce_b").getValueAt(i,
						"deciplace");
						if(obj!=null){
							deciplace = (Integer) obj;
						}
						else{
							this.getBillCardPanelWrapper()
							.getBillCardPanel().getBillModel("dip_dataproce_b")
							.setValueAt(8, i, "deciplace");
						}
						str.append(type);
						//判断是否是主键、系统预置主键
						if (this.getBillCardPanelWrapper().getBillCardPanel()
								.getBillModel("dip_dataproce_b").getValueAt(
										i, "ispk") == null && this.getBillCardPanelWrapper().getBillCardPanel()
										.getBillModel("dip_dataproce_b").getValueAt(
												i, "issyspk") == null) {
							ispk = false;
							str.append("(");
							str.append(length);
							str.append(",");
							str.append(deciplace);
							str.append(")");
							str.append(",");
							str.append("\n");
						} else if(this.getBillCardPanelWrapper().getBillCardPanel()
								.getBillModel("dip_dataproce_b").getValueAt(
										i, "ispk") == null && this.getBillCardPanelWrapper().getBillCardPanel()
										.getBillModel("dip_dataproce_b").getValueAt(
												i, "issyspk") != null) {

							issyspk = Boolean.parseBoolean(this
									.getBillCardPanelWrapper()
									.getBillCardPanel().getBillModel(
									"dip_dataproce_b").getValueAt(i,
									"issyspk").toString());
							if (issyspk == false) {
								str.append("(");
								str.append(length);
								str.append(",");
								str.append(deciplace);
								str.append(")");
								str.append(",");
								str.append("\n");
							} else {
								pkcolname = ename;
								str.append("(");
								str.append(length);
								str.append(")");
								str.append(" not null,");
								str.append("\n");

							}
						}else if(this.getBillCardPanelWrapper().getBillCardPanel()
								.getBillModel("dip_dataproce_b").getValueAt(
										i, "ispk") != null && this.getBillCardPanelWrapper().getBillCardPanel()
										.getBillModel("dip_dataproce_b").getValueAt(
												i, "issyspk") == null){
							ispk = Boolean.parseBoolean(this
									.getBillCardPanelWrapper()
									.getBillCardPanel().getBillModel(
									"dip_dataproce_b").getValueAt(i,
									"ispk").toString());
							if(ispk==false){
								str.append("(");
								str.append(length);
								str.append(",");
								str.append(deciplace);
								str.append(")");
								str.append(",");
								str.append("\n");
							}else{
								pkcolname=ename;
								str.append("(");
								str.append(length);
								str.append(",");
								str.append(deciplace);
								str.append(")");
								str.append(" not null,");
								str.append("\n");
							}

						}else if(this.getBillCardPanelWrapper().getBillCardPanel()
								.getBillModel("dip_dataproce_b").getValueAt(
										i, "ispk") != null && this.getBillCardPanelWrapper().getBillCardPanel()
										.getBillModel("dip_dataproce_b").getValueAt(
												i, "issyspk") != null){
							ispk = Boolean.parseBoolean(this
									.getBillCardPanelWrapper()
									.getBillCardPanel().getBillModel(
									"dip_dataproce_b").getValueAt(i,
									"ispk").toString());
							issyspk = Boolean.parseBoolean(this
									.getBillCardPanelWrapper()
									.getBillCardPanel().getBillModel(
									"dip_dataproce_b").getValueAt(i,
									"issyspk").toString());
							if (ispk == false && issyspk==false) {
								str.append("(");
								str.append(length);
								str.append(",");
								str.append(deciplace);
								str.append(")");
								str.append(",");
								str.append("\n");
							}else if(ispk==true && issyspk==false){
								pkcolname = ename;
								str.append("(");
								str.append(length);
								str.append(",");
								str.append(deciplace);
								str.append(")");
								str.append(" not null,");
								str.append("\n");
							}else if((ispk==false && issyspk==true)||(ispk==true && issyspk==true)){
								pkcolname=ename;
								str.append("(");
								str.append(length);
								str.append(")");
								str.append(" not null,");
								str.append("\n");
							}
						}



					}else if(type.equals("INTEGER")){
						str.append(type);
						//判断是否是主键、系统预置主键
						if (this.getBillCardPanelWrapper().getBillCardPanel()
								.getBillModel("dip_dataproce_b").getValueAt(
										i, "ispk") == null && this.getBillCardPanelWrapper().getBillCardPanel()
										.getBillModel("dip_dataproce_b").getValueAt(
												i, "issyspk") == null) {
							ispk = false;
							str.append(",");
							str.append("\n");
						} else if(this.getBillCardPanelWrapper().getBillCardPanel()
								.getBillModel("dip_dataproce_b").getValueAt(
										i, "ispk") == null && this.getBillCardPanelWrapper().getBillCardPanel()
										.getBillModel("dip_dataproce_b").getValueAt(
												i, "issyspk") != null) {

							issyspk = Boolean.parseBoolean(this
									.getBillCardPanelWrapper()
									.getBillCardPanel().getBillModel(
									"dip_dataproce_b").getValueAt(i,
									"issyspk").toString());
							if (issyspk == false) {
								str.append(",");
								str.append("\n");
							} else {
								pkcolname = ename;
								str.append(" not null,");
								str.append("\n");

							}
						}else if(this.getBillCardPanelWrapper().getBillCardPanel()
								.getBillModel("dip_dataproce_b").getValueAt(
										i, "ispk") != null && this.getBillCardPanelWrapper().getBillCardPanel()
										.getBillModel("dip_dataproce_b").getValueAt(
												i, "issyspk") == null){
							ispk = Boolean.parseBoolean(this
									.getBillCardPanelWrapper()
									.getBillCardPanel().getBillModel(
									"dip_dataproce_b").getValueAt(i,
									"ispk").toString());
							if(ispk==false){
								str.append(",");
								str.append("\n");
							}else{
								pkcolname=ename;
								str.append(" not null,");
								str.append("\n");
							}

						}else if(this.getBillCardPanelWrapper().getBillCardPanel()
								.getBillModel("dip_dataproce_b").getValueAt(
										i, "ispk") != null && this.getBillCardPanelWrapper().getBillCardPanel()
										.getBillModel("dip_dataproce_b").getValueAt(
												i, "issyspk") != null){
							ispk = Boolean.parseBoolean(this
									.getBillCardPanelWrapper()
									.getBillCardPanel().getBillModel(
									"dip_dataproce_b").getValueAt(i,
									"ispk").toString());
							issyspk = Boolean.parseBoolean(this
									.getBillCardPanelWrapper()
									.getBillCardPanel().getBillModel(
									"dip_dataproce_b").getValueAt(i,
									"issyspk").toString());
							if (ispk == false && issyspk==false) {
								str.append(",");
								str.append("\n");
							}else if(ispk==true && issyspk==false){
								pkcolname = ename;
								str.append(" not null,");
								str.append("\n");
							}else if((ispk==false && issyspk==true)||(ispk==true && issyspk==true)){
								pkcolname=ename;
								str.append(" not null,");
								str.append("\n");
							}
						}

					}else{
						//varchar型:长度
						Object o=this.getBillCardPanelWrapper()
						.getBillCardPanel().getBillModel("dip_dataproce_b")
						.getValueAt(i, "length");
						if(o!=null){
							length = (Integer) o;
						}
						else{
							this.getBillCardPanelWrapper()
							.getBillCardPanel().getBillModel("dip_dataproce_b")
							.setValueAt(100, i, "length");
						}
						type="VARCHAR";
						str.append(type);
						//判断是否是主键、系统预置主键
						if (this.getBillCardPanelWrapper().getBillCardPanel()
								.getBillModel("dip_dataproce_b").getValueAt(
										i, "ispk") == null && this.getBillCardPanelWrapper().getBillCardPanel()
										.getBillModel("dip_dataproce_b").getValueAt(
												i, "issyspk") == null) {
							ispk = false;
							str.append("(");
							str.append(length);
							str.append(")");
							str.append(",");
							str.append("\n");
						} else if(this.getBillCardPanelWrapper().getBillCardPanel()
								.getBillModel("dip_dataproce_b").getValueAt(
										i, "ispk") == null && this.getBillCardPanelWrapper().getBillCardPanel()
										.getBillModel("dip_dataproce_b").getValueAt(
												i, "issyspk") != null) {

							issyspk = Boolean.parseBoolean(this
									.getBillCardPanelWrapper()
									.getBillCardPanel().getBillModel(
									"dip_dataproce_b").getValueAt(i,
									"issyspk").toString());
							if (issyspk == false) {
								str.append("(");
								str.append(length);
								str.append(")");
								str.append(",");
								str.append("\n");
							} else {
								pkcolname = ename;
								str.append("(");
								str.append(length);
								str.append(")");
								str.append(" not null,");
								str.append("\n");

							}
						}else if(this.getBillCardPanelWrapper().getBillCardPanel()
								.getBillModel("dip_dataproce_b").getValueAt(
										i, "ispk") != null && this.getBillCardPanelWrapper().getBillCardPanel()
										.getBillModel("dip_dataproce_b").getValueAt(
												i, "issyspk") == null){
							ispk = Boolean.parseBoolean(this
									.getBillCardPanelWrapper()
									.getBillCardPanel().getBillModel(
									"dip_dataproce_b").getValueAt(i,
									"ispk").toString());
							if(ispk==false){
								str.append("(");
								str.append(length);
								str.append(")");
								str.append(",");
								str.append("\n");
							}else{
								pkcolname=ename;
								str.append("(");
								str.append(length);
								str.append(")");
								str.append(" not null,");
								str.append("\n");
							}

						}else if(this.getBillCardPanelWrapper().getBillCardPanel()
								.getBillModel("dip_dataproce_b").getValueAt(
										i, "ispk") != null && this.getBillCardPanelWrapper().getBillCardPanel()
										.getBillModel("dip_dataproce_b").getValueAt(
												i, "issyspk") != null){
							ispk = Boolean.parseBoolean(this
									.getBillCardPanelWrapper()
									.getBillCardPanel().getBillModel(
									"dip_dataproce_b").getValueAt(i,
									"ispk").toString());
							issyspk = Boolean.parseBoolean(this
									.getBillCardPanelWrapper()
									.getBillCardPanel().getBillModel(
									"dip_dataproce_b").getValueAt(i,
									"issyspk").toString());
							if (ispk == false && issyspk==false) {
								str.append("(");
								str.append(length);
								str.append(")");
								str.append(",");
								str.append("\n");
							}else if(ispk==true && issyspk==false){
								pkcolname = ename;
								str.append("(");
								str.append(length);
								str.append(")");
								str.append(" not null,");
								str.append("\n");
							}else if((ispk==false && issyspk==true)||(ispk==true && issyspk==true)){
								pkcolname=ename;
								str.append("(");
								str.append(length);
								str.append(")");
								str.append(" not null,");
								str.append("\n");
							}
						}
					}
				}	

				str.append("ts");
				str.append("\t");
				str.append("char(19),");
				str.append("\n");
				str.append("dr");
				str.append("\t");
				str.append("smallint");

				//2011-7-2 cl 增加了一个if判断，避免建表时没有主键，总是提示"建表错误"
				if(pkcolname !=null && !"".equals(pkcolname)&& pkcolname.length()>0){
					str.append(",");
					str.append("\n");
					str.append("constraint pk_" + tableName + " primary key");
					str.append(" (");
					str.append(pkcolname);
					str.append(")");				
				}

				str.append("\n");
				str.append(");");
				System.out.println(str);

				//在数据库中创建表
				IQueryField iqd=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
				boolean flag=iqd.exectEverySql(str.toString());
				if(flag==true){
					getSelfUI().showErrorMessage("建表错误！");
					return;
				}
				//插入NC数据字典
				try {
					Datadict m_dict = new Datadict();
					DDCData data = null;
					ObjectNode[] objNodes = null;
					BizObject[] objs = null;
					data=DDCBO_Client.fromDBMetaData(null,new String[]{tableName} , 0, "jkpt");
					objNodes = (ObjectNode[]) data.getNode().toArray(new ObjectNode[0]);
					objs = (BizObject[]) data.getTable().toArray(new BizObject[0]);
					m_dict.saveObjectNode(objNodes, objs, 20);
				} catch (Exception e) {
					e.printStackTrace();
				}

				//执行向数据定义表中插入数据
				AggregatedValueObject checkVO;
				try {
					checkVO = getBillTreeCardUI().getVOFromUI();
					DipDataproceHVO hvo=(DipDataproceHVO) checkVO.getParentVO();
					DipDataproceBVO[] bvo=(DipDataproceBVO[]) checkVO.getChildrenVO();
					boolean issucess=insertToDataDefinit(hvo,bvo);
					if(issucess){
						getSelfUI().showHintMessage("数据成功回写到数据定义表中！");

						//2011-6-22 cl 创建表后调用此方法，改变加工、创建表按钮状态
						VOTreeNode node=getSelectNode();
						onTreeSelected(node);

						return;
					}else{
						getSelfUI().showErrorMessage("数据回写失败,主键违反唯一约束："+hvo.getPk_dataproce_h());
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}else{
				ui.showWarningMessage("表体无数据，无法创建表！");
				return;
			}
		}
	}

	/**
	 * 作者：程莉
	 * 验证要创建的表名是否已存在
	 */
	protected boolean isTableExist(String procetab) {
		boolean isExist=false;//默认没有此表
		String sql="select 1 from user_tables where table_name='"+procetab.toUpperCase()+"';";
		try {
			ArrayList arr=(ArrayList)queryBS.executeQuery(sql,new MapListProcessor());

			if(arr.size()>=1){
				isExist=true;
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return isExist;
	}

	/*private void onBoWarning(int intBtn) throws Exception {

		VOTreeNode tempNode=getSelectNode();
		if(SJUtil.isNull(tempNode)){
			getSelfUI().showErrorMessage("请选择节点");
			return;
		}
		DipDataproceHVO hvo=(DipDataproceHVO) tempNode.getData();
		String  hpk=hvo.getPk_dataproce_h();
		hvo=(DipDataproceHVO) HYPubBO_Client.queryByPrimaryKey(DipDataproceHVO.class, hpk);
		if(SJUtil.isNull(hvo)){
			getSelfUI().showErrorMessage("此节点还没有保存，请编辑！");
			return;
		}
		//当前单据的预警业务类型
		String tasktype=hvo.getTasktype();
		if(SJUtil.isNull(tasktype)||tasktype.length()==0){
			getSelfUI().showErrorMessage("请选择预警类型！");
			return;
		}
		ToftPanel toft = SFClientUtil.showNode("H4H2H5", IFuncWindow.WINDOW_TYPE_DLG);

		WarningSetClientUI ui=(WarningSetClientUI) toft;
		String pk_dataproce_h=hvo.getPk_datadefinit_h();
		nc.vo.dip.warningset.MyBillVO mybillvo=new nc.vo.dip.warningset.MyBillVO() ;
		DipWarningsetVO[] warvo=(DipWarningsetVO[]) HYPubBO_Client.queryByCondition(DipWarningsetVO.class, " tasktype='"+tasktype+"' and pk_bustab='"+pk_dataproce_h+"'");

		DipWarningsetVO vo=null;
		boolean isadd=false;

		if(SJUtil.isNull(warvo)||warvo.length==0){
			vo=new  DipWarningsetVO();
			vo.setPk_bustab(pk_dataproce_h);
			vo.setTasktype(tasktype);
			isadd=true;
		}else{
			vo=warvo[0];
			DipWarningsetBVO[] children=(DipWarningsetBVO[]) HYPubBO_Client.queryByCondition(DipWarningsetBVO.class, "pk_warningset='"+vo.getPk_warningset()+"'");
			mybillvo.setChildrenVO(children);
			isadd=false;

		}
		String fpk=(String) tempNode.getParentnodeID();
		mybillvo.setParentVO(vo);
		ui.init(mybillvo, isadd,fpk,1);

	}*/
	private VOTreeNode getSelectNode() {
		// TODO Auto-generated method stub
		return getSelfUI().getBillTreeSelectNode();
	}
	String  jghpk;
	protected void onBoDataPorce(int intBtn) {
//		String proceCondition = this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("procecond").getValueObject() == null?"":this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("procecond").getValueObject().toString();
//		String procetab = this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("procetab").getValueObject()==null?"":this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("procetab").getValueObject().toString();
//		String procetype = this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("procetype").getValueObject()==null?"":this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("procetype").getValueObject().toString();
//		String sql = proceCondition;
		VOTreeNode tempNode=getSelectNode();
		if(SJUtil.isNull(tempNode)){
			getSelfUI().showErrorMessage("请选择节点");
			return;
		}
		if(tempNode.getParentnodeID()==null){
			getSelfUI().showErrorMessage("请选择子节点操作！");
			return;
		}
		DipDataproceHVO hvo=(DipDataproceHVO) tempNode.getData();
		jghpk=hvo.getPk_dataproce_h();
		new Thread() {
			@Override
			public void run() {
				BannerDialog dialog = new BannerDialog(getSelfUI());
				dialog.setTitle("正在加工，请稍候...");
				dialog.setStartText("正在加工，请稍候...");
				
				try {
					dialog.start();		
					ITaskExecute ite=(ITaskExecute) NCLocator.getInstance().lookup(ITaskExecute.class.getName());
					RetMessage rm = ite.doJGTask(jghpk);
					if(rm.getIssucc()){
						MessageDialog.showHintDlg(getSelfUI(), "数据加工", "加工完成");
					}else{
						getBillUI().showWarningMessage(rm.getMessage());
					}
				} catch (Throwable er) {
					er.printStackTrace();
				} finally {
					dialog.end();
				}
			}			
		}.start();
	}

	/**
	 * 调用指定方法
	 * cName 类名
	 * methodName 方法名
	 * type 参数类型
	 * param 参数值
	 * */
	public Object load(String cName,String methodName,String [] type,String [] param){
		try {
			//加载指定的java类
			Class c = Class.forName(cName);
			//获取指定对象的实例
			Constructor ct = c.getConstructor(null);
			Object obj = ct.newInstance(null);
			//构造方法参数的数据类型
			Class partypes [] = this.getMethodClass(type);
			//在指定类中获取指定方法
			Method m = c.getMethod(methodName, partypes);
			//构建方法的参数值
//			Object arglist = new Object[]{param[0].toString()};
			//调用指定的方法并获取返回值
			Object returnObj = m.invoke(obj, new Object[]{param[0].toString()});
			return returnObj;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取参数类型class的方法
	 * */
	public Class[] getMethodClass(String [] type){
		Class [] cs = new Class[type.length];
		for(int i = 0;i<cs.length;i++){
			if(!"".equals(type[i].trim()) || type[i]!=null){
				if("int".equals(type[i]) || "Integer".equals(type[i])){
					cs[i] = Integer.TYPE;
				}else if("float".equals(type[i]) || "Float".equals(type[i])){
					cs[i] = Float.TYPE;
				}else if("double".equals(type[i]) || "Double".equals(type[i])){
					cs[i] = Double.TYPE;
				}else if("boolean".equals(type[i]) || "Boolean".equals(type[i])){
					cs[i] = Boolean.TYPE;
				}else{
					cs[i] = String.class;
				}
			}
		}
		return cs;
	}

	/***
	 * 获取参数object的方法
	 * */
	public Object[] getMethodObject(String [] type,String [] param){
		Object obj[] = new Object[param.length];
		for(int i = 0;i<obj.length;i++){
			if(!"".equals(param[i].trim()) || param[i]!=null){
				if(!"".equals(type[i].trim()) || type[i]!=null){
					if("int".equals(type[i]) || "Integer".equals(type[i])){
						obj[i] = new Integer(param[i]);
					}else if("float".equals(type[i]) || "Float".equals(type[i])){
						obj[i] = new Float(param[i]);;
					}else if("double".equals(type[i]) || "Double".equals(type[i])){
						obj[i] = new Double(param[i]);
					}else if("boolean".equals(type[i]) || "Boolean".equals(type[i])){
						obj[i] = new Boolean(param[i]);
					}else{
						obj[i] = new String(param[i]);
					}
				}
			}

		}
		return obj;
	} 
	/**
	 * 版权所有：商佳科技
	 * 作者：程莉
	 * 功能：数据加工创建表后，将相关信息插入到数据定义对应的节点下
	 * 参数：数据加工主表vo和子表vo
	 * @throws Exception 
	 */
	public boolean insertToDataDefinit(DipDataproceHVO hvo,DipDataproceBVO[] bvo) throws Exception{
		boolean rst=false;
		String sql="select isdeploy from dip_sysregister_h h where h.pk_sysregister_h ='"+hvo.getFpk()+"' and nvl(dr,0)=0";
		String isdeploy=iq.queryfield(sql.toString());

		String sql2="select h.deploycode,h.deployname,h.busicode,h.datatype,h.memorytype from dip_datadefinit_h h where h.pk_datadefinit_h='"+hvo.getFirsttab()+"' and h.pk_sysregister_h='"+hvo.getFpk()+"' and nvl(h.dr,0)=0";
		SuperVO superVO=HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, hvo.getFirsttab());//(DipDatadefinitHVO.class,"h.pk_datadefinit_h='"+hvo.getFirsttab()+"' and h.pk_sysregister_h='"+hvo.getFpk()+"' and nvl(h.dr,0)=0" );
		DipDatadefinitHVO datahvo=(DipDatadefinitHVO) superVO;
		String deploycode=datahvo.getDeploycode()==null?"":datahvo.getDeploycode();//分布式编码
		String deployname=datahvo.getDeployname()==null?"":datahvo.getDeployname();//分布式名称
		String busicode=datahvo.getBusicode();//业务标识
		String datatype=datahvo.getDatatype()==null?"":datahvo.getDatatype();//数据类型
		String memorytype=datahvo.getMemorytype();//存储类型

		/*DipDatadefinitHVO hdvo=new DipDatadefinitHVO();
		hdvo=(DipDatadefinitHVO) iq.querySuperVO(sql2);
		String deploycode=hdvo.getDeploycode()==null?"":hdvo.getDeploycode();//分布式编码
		String deployname=hdvo.getDeployname()==null?"":hdvo.getDeployname();//分布式名称
		String busicode=hdvo.getBusicode();//业务标识
		String datatype=hdvo.getDatatype();//数据类型
		String memorytype=hdvo.getMemorytype();//存储类型
		 */		
		/*hdvo.setPk_datadefinit_h(hvo.getPrimaryKey());
		hdvo.setIsdeploy(UFBoolean.valueOf(deploycode));
		hdvo.setDeploycode(deploycode);
		hdvo.setDeployname(deployname);
		hdvo.setBusicode(busicode);
		hdvo.setDatatype(datatype);
		hdvo.setMemorytype(memorytype);
		hdvo.setSyscode(hvo.getCode());
		hdvo.setSysname(hvo.getName());
		hdvo.setMemorytable(hvo.getProcetab());
		hdvo.setDispostatus(hvo.getConstatus());
		hdvo.setEndstatus(hvo.getEndstatus());
		hdvo.setTabsoucetype("数据加工");
		hdvo.setTs(hvo.getTs().toString());
		hdvo.setDr(hvo.getDr());
		hdvo.setPk_sysregister_h(hvo.getFpk());

		String result=HYPubBO_Client.insert(hdvo);
		if(result !=null && !result.trim().equals("")){
			rst=true;
		}*/

		//2011-7-2 避免插入时主键违反唯一约束
		String k=hvo.getPk_dataproce_h();
		String qsql="select h.pk_datadefinit_h from dip_datadefinit_h h where h.pk_datadefinit_h='"+k+"'";
		String qrst=iq.queryfield(qsql);
		if(qrst !=null && !"".equals(qrst) && qrst.length()>0){
			return false;
		}
		
		StringBuffer strBuffer=new StringBuffer();
		strBuffer.append("insert into dip_datadefinit_h(isdeploy,deploycode,deployname,busicode,datatype,memorytype,syscode,sysname,memorytable,dispostatus,endstatus,tabsoucetype,ts,dr,pk_datadefinit_h,pk_sysregister_h) values");
		strBuffer.append("(");
		strBuffer.append("'");
		strBuffer.append(isdeploy);
		strBuffer.append("'");
		strBuffer.append(",");

		strBuffer.append("'");
		strBuffer.append(deploycode);
		strBuffer.append("'");
		strBuffer.append(",");

		strBuffer.append("'");
		strBuffer.append(deployname);
		strBuffer.append("'");
		strBuffer.append(",");

		strBuffer.append("'");
		strBuffer.append(busicode);
		strBuffer.append("'");
		strBuffer.append(",");

		strBuffer.append("'");
		strBuffer.append(datatype);
		strBuffer.append("'");
		strBuffer.append(",");

		strBuffer.append("'");
		strBuffer.append(memorytype);
		strBuffer.append("'");
		strBuffer.append(",");

		strBuffer.append("'");
		strBuffer.append(hvo.getCode());
		strBuffer.append("'");
		strBuffer.append(",");

		strBuffer.append("'");
		strBuffer.append(hvo.getName());
		strBuffer.append("'");
		strBuffer.append(",");
		strBuffer.append("'");
		strBuffer.append(hvo.getProcetab());
		strBuffer.append("'");
		strBuffer.append(",");
		strBuffer.append("'");
		strBuffer.append(hvo.getConstatus());
		strBuffer.append("'");
		strBuffer.append(",");
		strBuffer.append("'");
		strBuffer.append(hvo.getEndstatus());
		strBuffer.append("'");
		strBuffer.append(",");
		strBuffer.append("'");
		strBuffer.append("数据加工");
		strBuffer.append("'");
		strBuffer.append(",");
		strBuffer.append("'");
		strBuffer.append(new UFDateTime(new Date()));
		strBuffer.append("'");
		strBuffer.append(",");
		strBuffer.append("'");
		strBuffer.append(hvo.getDr());
		strBuffer.append("'");
		strBuffer.append(",");
		strBuffer.append("'");
		strBuffer.append(hvo.getPk_dataproce_h());
		strBuffer.append("'");
		strBuffer.append(",");
		strBuffer.append("'");
		strBuffer.append(hvo.getFpk());
		strBuffer.append("'");
		strBuffer.append(")");
		strBuffer.append(";");

		AggregatedValueObject checkVO=getBillTreeCardUI().getVOFromUI();
		bvo=(DipDataproceBVO[]) checkVO.getChildrenVO();

		try{
			boolean s=iq.exectEverySql(strBuffer.toString());
			if(s==false){
				rst=true;
				if(bvo !=null){
//					DipDatadefinitBVO[] hdbvo=new DipDatadefinitBVO[bvo.length];
					//2011-7-2 如果插入的主键已经存在了，就返回不再执行插入操作，避免主键违反唯一约束
					if(qrst !=null && !"".equals(qrst) && qrst.length()>0){
						return false;
					}
					for(int i=0;i<bvo.length;i++){
						/*hdbvo[i]=new DipDatadefinitBVO();
						hdbvo[i].setPk_datadefinit_h(bvo[i].getPk_dataproce_h());*/
						StringBuffer sbbvo=new StringBuffer();
						strBuffer.append("\n");
						sbbvo.append("insert into dip_datadefinit_b(cname,ename,type,length,deciplace,isimport,ispk,isonlyconst,isquote,quotetable,quotecolu,pk_datadefinit_h,pk_datadefinit_b,ts,dr) values");
						sbbvo.append("(");
						sbbvo.append("'");
						sbbvo.append(bvo[i].getCname());
						sbbvo.append("'");
						sbbvo.append(",");
						sbbvo.append("'");
						sbbvo.append(bvo[i].getEname());
						sbbvo.append("'");
						sbbvo.append(",");
						sbbvo.append("'");
						sbbvo.append(bvo[i].getType());
						sbbvo.append("'");
						sbbvo.append(",");
						sbbvo.append("'");
						sbbvo.append(bvo[i].getLength()==null?"":bvo[i].getLength());
						sbbvo.append("'");
						sbbvo.append(",");
						sbbvo.append("'");
						sbbvo.append(bvo[i].getDeciplace()==null?"":bvo[i].getDeciplace());
						sbbvo.append("'");
						sbbvo.append(",");

						sbbvo.append("'");
						sbbvo.append(bvo[i].getIsimport()==null?"N":bvo[i].getIsimport());
						sbbvo.append("'");
						sbbvo.append(",");

						sbbvo.append("'");
						sbbvo.append(bvo[i].getIspk()==null?"N":bvo[i].getIspk());
						sbbvo.append("'");
						sbbvo.append(",");

						sbbvo.append("'");
						sbbvo.append(bvo[i].getIsonlyconst()==null?"N":bvo[i].getIsonlyconst());
						sbbvo.append("'");
						sbbvo.append(",");
						sbbvo.append("'");
						sbbvo.append(bvo[i].getIsquote()==null?"N":bvo[i].getIsquote());
						sbbvo.append("'");
						sbbvo.append(",");
						sbbvo.append("'");
						sbbvo.append(bvo[i].getQuotetable()==null?"":bvo[i].getQuotetable());
						sbbvo.append("'");
						sbbvo.append(",");
						sbbvo.append("'");
						sbbvo.append(bvo[i].getQuotecolu()==null?"":bvo[i].getQuotecolu());
						sbbvo.append("'");
						sbbvo.append(",");

						sbbvo.append("'");
						sbbvo.append(bvo[i].getPk_dataproce_h());
						sbbvo.append("'");
						sbbvo.append(",");

						sbbvo.append("'");
						sbbvo.append(bvo[i].getPk_dataproce_b());
						sbbvo.append("'");
						sbbvo.append(",");

						sbbvo.append("'");
						sbbvo.append(new UFDateTime(new Date()));
						sbbvo.append("'");
						sbbvo.append(",");
						sbbvo.append("'");
						sbbvo.append(bvo[i].getDr());
						sbbvo.append("'");
						sbbvo.append(")");
						sbbvo.append(";");
						boolean ss=iq.exectEverySql(sbbvo.toString());
						if(ss==false){
							rst=true;
						}else{
							rst=false;
							break;
						}
					}
//					HYPubBO_Client.insertAry(hdbvo);
				}
			}else{
				rst=false ;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return rst;
	}

}