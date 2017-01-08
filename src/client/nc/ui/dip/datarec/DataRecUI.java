package nc.ui.dip.datarec;

import java.awt.event.ActionListener;

import nc.ui.bd.ref.GSWJRefModel;
import nc.ui.bd.ref.ReturnBMsgRefModel;
import nc.ui.bd.ref.SourceRegistRefModel;
import nc.ui.bd.ref.ZDJSRefModel;
import nc.ui.bd.ref.ZDZQRefModel;
import nc.ui.bd.ref.ZJBRefModel;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.CardEventHandler;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.pub.VOTreeNode;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.datarec.DipDatarecHVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.field.BillField;


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
public class DataRecUI extends AbstractDataRecUI{
	public DataRecUI(){
		super();
		SJUtil.setAllButtonsEnalbe(this.getButtons());
		getSplitPane().setDividerLocation(200);
	}
	/** 字段描述 */
	private static final long serialVersionUID = 5692169789554885827L;

	public String selectnode = "";//选择树的节点

	//2011-3-22 12:42 chengli 在制作左树右表时添加 begin
	protected CardEventHandler createEventHandler() {		
		return new MyEventHandler(this, getUIControl());
	}
	public String getRefBillType() {
		return null;
	}
	/** end */


	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
	throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
	throws Exception {	}

	protected void initSelfData() {	}

	/**
	 * 2011-3-22 14:45 chengli
	 * 修改此方法初始化模板控件数据
	 */
	@SuppressWarnings("unused")
	public void setDefaultData() throws Exception {
		BillField fileDef = BillField.getInstance();
		String billtype = getUIControl().getBillType();
		String pkCorp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
	}

//	2011-4-11 9:42 chengli 在制作左树右表时添加 begin_2
	@Override
	protected IVOTreeData createTableTreeData() {
		return null;
	}

	@Override
	protected IVOTreeData createTreeData() {
		return new nc.ui.dip.datarec.SampleTreeCardData();
	}
	//end_2

	public void afterInit() throws Exception {

		super.afterInit();
		//给根节点赋名称
		this.modifyRootNodeShowName("数据接收");

		//页面加载时,将"删除"按钮设置为不可用
		//getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
		//页面加载时,将"修改"按钮设置为不可用
		getButtonManager().getButton(IBillButton.Edit).setEnabled(false);

		updateButtonUI();
	}
	@Override
	protected void insertNodeToTree(CircularlyAccessibleValueObject arg0) throws Exception {
		super.insertNodeToTree(arg0);
	}

	@Override
	public boolean afterTreeSelected(VOTreeNode node) {
		selectnode = node.getNodeID().toString();

		System.out.println("node:"+selectnode);
		return super.afterTreeSelected(node);
	}

	@Override
	protected void onTreeSelectSetButtonState(TableTreeNode snode) {

		if ("root".equals(snode.getNodeID().toString().trim())){
			selectnode = "";
		}
		super.onTreeSelectSetButtonState(snode);
         String str = (String) snode.getParentnodeID();
        

 		this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(true);
 		this.getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
 		this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(false);
 		this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(false);
 		this.getButtonManager().getButton(IBtnDefine.moveFolderBtn).setEnabled(false);
         if(str!=null && str.length()>0){//除了系统节点以外的节点
        	 DipDatarecHVO hvo=(DipDatarecHVO) ((VOTreeNode)snode).getData();
        	 if(hvo.getIsfolder()!=null&&hvo.getIsfolder().booleanValue()){//是文件夹
            	 this.getButtonManager().getButton(IBillButton.Add).setEnabled(true);
            	 this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
            	 this.getButtonManager().getButton(IBillButton.Copy).setEnabled(false);
            	 this.getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
            	 this.getButtonManager().getButton(IBtnDefine.DataCheck).setEnabled(false);
            	 this.getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(false);
            	 this.getButtonManager().getButton(IBtnDefine.JSDYDATAFORMAT).setEnabled(false);

            	 this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(true);
            	 this.getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
            	 this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(true);
            	 this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(true);
            	 this.getButtonManager().getButton(IBtnDefine.MBZH).setEnabled(false);
        	 }else{//不是文件夹
        		 this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
            	 this.getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
            	 this.getButtonManager().getButton(IBillButton.Copy).setEnabled(true);
            	 this.getButtonManager().getButton(IBillButton.Delete).setEnabled(true);
            	 if(hvo.getIoflag()!=null&&hvo.getIoflag().equals("输入")){
            		 this.getButtonManager().getButton(IBtnDefine.DataCheck).setEnabled(true);
            	 }else{
            		 this.getButtonManager().getButton(IBtnDefine.DataCheck).setEnabled(false);
            	 }
            	 this.getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(true);
            	 this.getButtonManager().getButton(IBtnDefine.JSDYDATAFORMAT).setEnabled(true);
            	 
            	 this.getButtonManager().getButton(IBtnDefine.moveFolderBtn).setEnabled(true);
        		 
        	 }
        	 try {
				updateButtonUI();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         }else if(snode==snode.getRoot()){//根节点
        	 this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
        	 this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
        	 this.getButtonManager().getButton(IBillButton.Copy).setEnabled(false);
        	 this.getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
        	 this.getButtonManager().getButton(IBillButton.Refresh).setEnabled(true);
        	 this.getButtonManager().getButton(IBtnDefine.DataCheck).setEnabled(false);
        	 this.getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(false);
        	 this.getButtonManager().getButton(IBtnDefine.JSDYDATAFORMAT).setEnabled(false);
        	 this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(false);
        	 try {
				updateButtonUI();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         }else {//系统节点
        	 this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
        	 this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
        	 this.getButtonManager().getButton(IBillButton.Copy).setEnabled(false);
        	 this.getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
        	 this.getButtonManager().getButton(IBtnDefine.DataCheck).setEnabled(false);
        	 this.getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(false);
        	 this.getButtonManager().getButton(IBtnDefine.JSDYDATAFORMAT).setEnabled(false);
        	 
        	 this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(true);
        	 this.getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(true);
        	 this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(false);
        	 this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(false);
        	 this.getButtonManager().getButton(IBtnDefine.MBZH).setEnabled(false);
        	 
        	 try {
 				updateButtonUI();
 			} catch (Exception e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
         }
	}


	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		//系统
		String pk_sysregister_h =getBillCardPanel().getHeadItem("pk_xt").getValueObject().toString();
		/*
		 * 修改业务表名的限制条件，将原来的参照数据定义主表的参照类的限制条件去掉
		 * 2011-06-09
		 * zlc*/
		if(e.getKey().equals("memorytable")){//系统标志
			getBillCardPanel().execHeadFormulas(getBillCardPanel().getHeadItem("memorytable").getEditFormulas());
		}else if(e.getKey().equals("backcon")){
			Object ob=getBillCardPanel().getHeadItem("backcon").getValueObject();
			if(SJUtil.isNull(ob)){
				controHZstats(false);
			}else{
				String bool=(String) ob;
				if(bool.equals("Y")){
					controHZstats(true);
				}else{
					controHZstats(false);
				}
			}
		}else if(e.getKey().equals("sourcecon")){
			getBillCardPanel().execHeadFormulas(getBillCardPanel().getHeadItem("sourcecon").getEditFormulas());
		}
		BillItem item = getBillCardPanel().getHeadItem("sourceparam");//数据来源参数
		//2011-6-3 根据数据来源类型的不同，数据来源连接参照出不同的结果 begin
		if(e.getKey().equals("sourcetype")){
			Object sourcetype=getBillCardPanel().getHeadItem("sourcetype").getValueObject();
			UIRefPane pane=(UIRefPane) getBillCardPanel().getHeadItem("sourcecon").getComponent();//数据来源连接
			
//			UIRefPane paneParam=(UIRefPane) getBillCardPanel().getHeadItem("sourceparam").getComponent();//数据来源参数
			if(!SJUtil.isNull(sourcetype)){
//				DipDataoriginVO vo=null;
				try {
					/*liyunzhe modify 把数据来源类型比较由名称改成pk主键比较 start 2012-06-05*/					
//					vo = (DipDataoriginVO) HYPubBO_Client.queryByPrimaryKey(DipDataoriginVO.class, sourcetype.toString());
//					String name=vo.getName();
					String name=sourcetype.toString();
//					if(name.equals("主动抓取")){
					if(name.equals(IContrastUtil.DATAORIGIN_ZDZQ)){
						ZDZQRefModel model3=new ZDZQRefModel();
						model3.addWherePart(" and nvl(dr,0)=0");
						pane.setRefModel(model3);
						setParameter(item,IContrastUtil.DATAORIGIN_ZDZQ,IContrastUtil.DATAORIGIN_ZDZQ_PARAMETAR,true);
//					}else if(name.equals("消息总线")){
					}else if(name.equals(IContrastUtil.DATAORIGIN_XXZX)){
						ZDJSRefModel model4=new ZDJSRefModel();
						model4.addWherePart(" and nvl(dip_msr.dr,0)=0");
						pane.setRefModel(model4);
//					}else if(name.equals("格式文件")){
					}else if(name.equals(IContrastUtil.DATAORIGIN_GSWJ)){
						GSWJRefModel model5=new GSWJRefModel();
						model5.addWherePart(" and nvl(dr,0)=0");
						pane.setRefModel(model5);
						setParameter(item,IContrastUtil.DATAORIGIN_GSWJ,IContrastUtil.DATAORIGIN_GSWJ_PARAMETAR,true);
//					}else if(name.equals("中间表")){
					}else if(name.equals(IContrastUtil.DATAORIGIN_ZJB)){
						//中间表
						ZJBRefModel model6=new ZJBRefModel();
						model6.addWherePart(" and nvl(dr,0)=0");
						pane.setRefModel(model6);
						setParameter(item,IContrastUtil.DATAORIGIN_ZJB,IContrastUtil.DATAORIGIN_ZJB_PARAMETAR,true);
//					}else if(name.equals("webservice抓取")){
					}else if(name.equals(IContrastUtil.DATAORIGIN_WEBSERVICEZQ)){
						SourceRegistRefModel modet=new SourceRegistRefModel();
						pane.setRefModel(modet);
						setParameter(item,IContrastUtil.DATAORIGIN_WEBSERVICEZQ,IContrastUtil.DATAORIGIN_WEBSERVICEZQ_PARAMETAR,true);
					}
					getBillCardPanel().setHeadItem("sourcecon", null);
					getBillCardPanel().setHeadItem("databakfile", null);
					/*
					 * @author程莉
					 * @date 2011-6-21
					 * @description:由于只有数据来源类型为“消息总线”时，才有"传输标志"，即“传输控制标志”、“回执控制标志”处于可编辑状态，所以在修改后:
					 * 如果数据来源类型改变为非“消息总线”且“传输控制标志”、“回执控制标志”为true时，则应把“传输控制标志”、“回执控制标志”变为false,即值清空
					 */	
					String ioflag=(String) getBillCardPanel().getHeadItem("ioflag").getValueObject();
					String iotype=(String) getBillCardPanel().getHeadItem("databakfile").getValueObject();
//					if(name.equals("消息总线")){
					if(name.equals(IContrastUtil.DATAORIGIN_XXZX)){
						getBillCardPanel().getHeadItem("databakfile").setEdit(true);
						getBillCardPanel().getHeadItem("trancon").setEdit(true);//.setEnabled(true);
						getBillCardPanel().getHeadItem("backcon").setEdit(true);
						if(ioflag!=null&&ioflag.equals("输出")&&iotype!=null&&iotype.equals("文件流")){
							getBillCardPanel().getHeadItem("format").setEdit(true);
							getBillCardPanel().getHeadItem("pk_datadefinit_h").setEdit(true);
							getBillCardPanel().setHeadItem("format", "m1");
						}else{
							getBillCardPanel().getHeadItem("format").setEdit(false);
							getBillCardPanel().setHeadItem("format", null);
						}
						getBillCardPanel().getHeadItem("ioflag").setEdit(true);
						getBillCardPanel().getHeadItem("databakfile").setEdit(true);
						getBillCardPanel().setHeadItem("databakfile","消息流");
						if(ioflag!=null&&ioflag.equals("输入")&&iotype!=null&&iotype.equals("文件流")){
							setParameter(item,IContrastUtil.DATAORIGIN_XXZX,IContrastUtil.DATAORIGIN_XXZX_IN_FILE_PARAMETAR,true);
						}else{
							item.setValue("");
							item.setEdit(false);
						}
					}else{
						getBillCardPanel().setHeadItem("databakfile","");
						getBillCardPanel().getHeadItem("databakfile").setEdit(false);
//						if(name.equals("格式文件")&&ioflag!=null&&ioflag.equals("输出")){
						if(name.equals(IContrastUtil.DATAORIGIN_GSWJ)&&ioflag!=null&&ioflag.equals("输出")){
							getBillCardPanel().getHeadItem("format").setEdit(true);
							getBillCardPanel().setHeadItem("format", "m1");
						}else{
							getBillCardPanel().getHeadItem("format").setEdit(false);
							getBillCardPanel().setHeadItem("format", null);
						}
						getBillCardPanel().getHeadItem("trancon").setValue(null);
						getBillCardPanel().getHeadItem("trancon").setEdit(false);
						getBillCardPanel().getHeadItem("pk_datadefinit_h").setEdit(false);
						getBillCardPanel().setHeadItem("pk_datadefinit_h", null);
						
						getBillCardPanel().getHeadItem("backcon").setValue(null);
						getBillCardPanel().getHeadItem("backcon").setEdit(false);
//						if(name.equals("主动抓取")){
						if(name.equals(IContrastUtil.DATAORIGIN_ZDZQ)||name.equals(IContrastUtil.DATAORIGIN_WEBSERVICEZQ)){
							getBillCardPanel().setHeadItem("ioflag", "输入");
							getBillCardPanel().getHeadItem("ioflag").setEdit(false);
						}else{
//							getBillCardPanel().setHeadItem("ioflag", "输入");
							getBillCardPanel().getHeadItem("ioflag").setEdit(true);
						}
						getBillCardPanel().getHeadItem("def_str_1").setValue(null);
						getBillCardPanel().getHeadItem("def_str_1").setEdit(false);
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		if(e.getKey().equals("ioflag")||e.getKey().equals("sourcecon")||e.getKey().equals("databakfile")){
			String ioflag=(String) getBillCardPanel().getHeadItem("ioflag").getValueObject();
			String iotype=(String) getBillCardPanel().getHeadItem("databakfile").getValueObject();
			if(ioflag!=null){
				Object sourcetype=getBillCardPanel().getHeadItem("sourcetype").getValueObject();
				if(sourcetype==null){
					return;
				}
				String name=sourcetype.toString();
				if(ioflag.equals("输入")){
					getBillCardPanel().setHeadItem("format", null);
					getBillCardPanel().getHeadItem("format").setEdit(false);
					
					
				}else{
//					if(!SJUtil.isNull(sourcetype)){
						try {
//							if((name.equals("消息总线")&&iotype!=null&&iotype.equals("文件流"))||name.equals("格式文件")){
							if((name.equals(IContrastUtil.DATAORIGIN_XXZX)&&iotype!=null&&iotype.equals("文件流"))||name.equals(IContrastUtil.DATAORIGIN_WEBSERVICEZQ)||name.equals(IContrastUtil.DATAORIGIN_GSWJ)){
								getBillCardPanel().getHeadItem("format").setEdit(true);
								getBillCardPanel().setHeadItem("format", "m1");
//								if(name.equals("消息总线")){
								if(name.equals(IContrastUtil.DATAORIGIN_XXZX)){
									getBillCardPanel().getHeadItem("pk_datadefinit_h").setEdit(true);
								}
							}else{
								getBillCardPanel().getHeadItem("pk_datadefinit_h").setEdit(false);
								getBillCardPanel().setHeadItem("pk_datadefinit_h", null);
								getBillCardPanel().getHeadItem("format").setEdit(false);
								getBillCardPanel().setHeadItem("format", null);
							}
						}catch (Exception ee) {
							ee.printStackTrace();
						}
//					}
				}
				
				if(name.equals(IContrastUtil.DATAORIGIN_XXZX)){
					if(ioflag!=null&&ioflag.equals("输入")&&iotype!=null&&iotype.equals("文件流")){
						setParameter(item,IContrastUtil.DATAORIGIN_XXZX,IContrastUtil.DATAORIGIN_XXZX_IN_FILE_PARAMETAR,true);
					}else{
						item.setEdit(false);
					}
				}

				
				
			}
		}
		if(e.getKey().equals("backerrtab")){
			UIRefPane uir2=(UIRefPane) getBillCardPanel().getHeadItem("backerrinfo").getComponent();
			Object ob=getBillCardPanel().getHeadItem("backerrtab").getValueObject();
			String ss=ob==null?"":ob.toString();
			ReturnBMsgRefModel m2=new ReturnBMsgRefModel();
			m2.addWherePart(" and nvl(dr,0)=0 and pk_returnmess_h='"+ss+"'");
			uir2.setRefModel(m2);
		}
		/*end*/
		/*liyunzhe modify 把数据来源类型比较由名称改成pk主键比较 end 2012-06-05*/			
	}
	private void controHZstats(boolean con){
		//回执错误码表
		getBillCardPanel().getHeadItem("backerrtab").setEnabled(con);
		//回执错误码表信息
		getBillCardPanel().getHeadItem("backerrinfo").setEnabled(con);
		//回执消息服务器
		getBillCardPanel().getHeadItem("def_str_1").setEnabled(con);
		if(!con){
			getBillCardPanel().getHeadItem("backerrtab").setValue(null);
			getBillCardPanel().getHeadItem("backerrinfo").setValue(null);
			getBillCardPanel().getHeadItem("def_str_1").setValue(null);
		}
	}
	
	@Override
	protected UITree getBillTree() {
		// TODO Auto-generated method stub
		return super.getBillTree();
	}
	/**
	 * 
	 * @param item 设置参数billitem
	 * @param dataorgin_type 设置参数数据来源类型
	 * @param dataorgin_parameter 设置参数显示名称和个数
	 * @param flag 是否清除item的界面显示值，（因为在切换数据来源类型时候如果不清除item的值，这样会把切换前item的值显示到切换后item的值）
	 */
	public void setParameter(BillItem item,String dataorgin_type,String dataorgin_parameter,boolean flag){
		if (item != null) {
			item.setEdit(true);
			if(flag){
				item.setValue("");
			}
			UIRefPane ref = (UIRefPane) item.getComponent();
			if (ref != null) { 
				ref.getUIButton().removeActionListener(ref.getUIButton().getListeners(ActionListener.class)[0]);
				ref.getUIButton().addActionListener(new DataRecActionListener(item,dataorgin_type,dataorgin_parameter));
				ref.setAutoCheck(false);
				ref.setEditable(false);
			}
		}
	}
	
}
