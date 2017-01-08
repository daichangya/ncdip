package nc.ui.dip.datachange;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.itf.dip.pub.ITaskExecute;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.exception.DbException;
import nc.ui.bd.ref.DataDefinitTableTreeRefModel;
import nc.ui.bd.ref.DataDefinitbRefModel;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.dip.control.ControlDlg;
import nc.ui.dip.datachange.mb.AskMBDLG;
import nc.ui.dip.datarec.DatarecDlg;
import nc.ui.dip.dlg.AddFolderDlg;
import nc.ui.dip.dlg.AskDLG;
import nc.ui.dip.dlg.movefoleder.MovefolderDLG;
import nc.ui.dip.effectdef.EffSetDlg;
import nc.ui.dip.effectdef.EffectDlg;
import nc.ui.dip.warningset.WarningsetDlg;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.util.NCOptionPane;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.tools.BannerDialog;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.pub.VOTreeNode;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.RetMessage;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.control.ControlHVO;
import nc.vo.dip.credence.CredenceBVO;
import nc.vo.dip.credence.CredenceHVO;
import nc.vo.dip.datachange.DipDatachangeBVO;
import nc.vo.dip.datachange.DipDatachangeHVO;
import nc.vo.dip.view.VDipCrerefVO;
import nc.vo.dip.warningset.DipWarningsetBVO;
import nc.vo.dip.warningset.DipWarningsetVO;
import nc.vo.dip.warningset.MyBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.pub.IExAggVO;
import nc.vo.trade.summarize.Hashlize;
import nc.vo.trade.summarize.VOHashPrimaryKeyAdapter;

/**
 *
 *该类是AbstractMyEventHandler抽象类的实现类，
 *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 *@author author
 *@version tempProject version
 */

public class MyEventHandler 
extends AbstractMyEventHandler{
	/**
	 * @author cl
	 * @date2011-6-13
	 * @description 复制模板
	 */
//	Map<String,nc.vo.jyprj.credence.MyBillVO> copymap=new HashMap<String,nc.vo.jyprj.credence.MyBillVO>();
	nc.vo.dip.credence.MyBillVO copymap=null;
	@Override
	protected void onBoCopy() throws Exception {

		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			this.getSelfUI().showWarningMessage("请选择要操作的节点！");
			return;
		}
		//得到当前VO类
		DipDatachangeHVO hvo=(DipDatachangeHVO) node.getData();
		//得到主键值
		String hpk=hvo.getFpk();
		if(SJUtil.isNull(hpk)||hpk.length()<=0){
			getSelfUI().showErrorMessage("系统节点不能编辑！");
			return;
		}else{
			String sql="select h.pk_sysregister_h from dip_sysregister_h h where h.code='0000' and h.pk_sysregister_h='"+hpk+"' and nvl(h.dr,0)=0";
			String isNC = null;
			isNC=iq.queryfield(sql);
			if(isNC !=null && !"".equals(isNC.trim())){
				getSelfUI().showWarningMessage("NC系统下，只能浏览数据，不能复制！"); 
				return;
			}else{
				int row=getSelfUI().getBillCardPanel().getBillTable().getSelectedRow();
				if(row<0){
					getSelfUI().showErrorMessage("请选择需要操作的定义！");
					return ;
				}
				DipDatachangeBVO[] vos = null;
				try {
					vos = (DipDatachangeBVO[]) getBillTreeCardUI().getVOFromUI().getChildrenVO();
				} catch (Exception e) {
					e.printStackTrace();
				}
				DipDatachangeBVO bvo=vos[row];
				String pkbvo=bvo.getPrimaryKey();
				if(pkbvo==null||pkbvo.length()<=0){
					getSelfUI().showErrorMessage("您选择的模板定义还没有保存！请先保存！");
					return;
				}
				nc.vo.dip.credence.MyBillVO billvo=new nc.vo.dip.credence.MyBillVO();
				CredenceHVO[] chvo=(CredenceHVO[]) HYPubBO_Client.queryByCondition(CredenceHVO.class, "pk_datadefinit_h ='"+pkbvo+"' and nvl(dr,0)=0");
				if(chvo==null||chvo.length<=0){
					getSelfUI().showErrorMessage("没有定义凭证模板,不能复制！");
					return;
				}else{
					billvo.setParentVO(chvo[0]);
					CredenceBVO[] cbvos=(CredenceBVO[]) HYPubBO_Client.queryByCondition(CredenceBVO.class, "pk_credence_h='"+chvo[0].getPk_credence_h()+"' and nvl(dr,0)=0");
					billvo.setChildrenVO(cbvos);
					copymap=billvo;
//					copymap.put(hvo.getPrimaryKey(), billvo);
				}
			}
		}
	}
	protected void onBoCopynew(int row,String refpk) throws Exception {
		DipDatachangeHVO dchvo= (DipDatachangeHVO) getSelectNode().getData();
		DipDatachangeBVO bvo=(DipDatachangeBVO) getSelfUI().getBillCardPanel().getBillModel().getBodyValueRowVO(row, DipDatachangeBVO.class.getName());
		String isdy=bvo.getTempexist();//(String) getSelfUI().getBillCardPanel().getBodyValueAt(row, "tempexist");
		String ykey=bvo.getPrimaryKey();
		if(isdy!=null&&isdy.equals("已定义")){
			getSelfUI().showErrorMessage("模板已定义，请删除再做复制！");
			return;
		}else if((bvo.getDef_str_1()==null||bvo.getDef_str_1().length()==0)){
//			DataRefDLG dg=new DataRefDLG(getSelfUI(),getSelectNode().getParentnodeID().toString(),"模板复制",bvo.getDef_str_1());
//			dg.showModal();
			if(refpk!=null&&refpk.length()>0){
				VDipCrerefVO vo=(VDipCrerefVO) HYPubBO_Client.queryByPrimaryKey(VDipCrerefVO.class, refpk);
				String key=vo.getPrimaryKey();
				String sql="update dip_datachange_b set tempexist='已定义',Def_str_1=null where pk_datachange_b='"+ykey+"'";
				String sql2="delete from dip_credence_b where dip_credence_b.pk_credence_h in (select ch.pk_credence_h from dip_credence_h ch where ch.pk_datadefinit_h='"+ykey+"') ";
				String sql3="delete from dip_credence_h where pk_datadefinit_h='"+ykey+"'";
				IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
				try {
					iqf.exesql(sql);
					iqf.exesql(sql2);
					iqf.exesql(sql3);
					CredenceHVO[] hvos=(CredenceHVO[]) HYPubBO_Client.queryByCondition(CredenceHVO.class, "nvl(dr,0)=0 and pk_datadefinit_h='"+key+"'");
					if(hvos!=null&&hvos.length>0){
						CredenceHVO hvo=hvos[0];
						String hkey=hvo.getPrimaryKey();
						hvo.setPrimaryKey(null);
						hvo.setBusdata(dchvo.getBusidata());
						hvo.setCode(dchvo.getCode());
						hvo.setName(dchvo.getName());
						hvo.setAccoutbook(dchvo.getPk_glorg());
						hvo.setSysmodel(bvo.getIssystmp());
						hvo.setUnit(bvo.getOrgcode());
						hvo.setPk_datadefinit_h(ykey);
						String rettey=HYPubBO_Client.insert(hvo);
						CredenceBVO[] bvos=(CredenceBVO[]) HYPubBO_Client.queryByCondition(CredenceBVO.class, "nvl(dr,0)=0 and pk_credence_h='"+hkey+"'");
						if(bvos!=null&&bvos.length>0){
							for(int i=0;i<bvos.length;i++){
								bvos[i].setPrimaryKey(null);
								bvos[i].setPk_credence_h(rettey);
							}
							HYPubBO_Client.insertAry(bvos);
						}
					}
					getSelfUI().getBillCardPanel().setBodyValueAt(null, row, "def_str_1");
					getSelfUI().getBillCardPanel().setBodyValueAt("已定义", row, "tempexist");
					getSelfUI().getBillCardPanel().execBodyFormulas(row, getSelfUI().getBillCardPanel().getBodyItem("def_yy").getLoadFormula());
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				getSelfUI().showErrorMessage("请选择要复制的模板！");
			}
		}else{
			getSelfUI().showErrorMessage("已经定义模板引用，请选择没有定义模板的公司做复制操作");
			return;
		}
	}
	/**
	 * @author cl
	 * @date 2011-6-13
	 * @description 粘贴模板
	 */
	public void pasteModel() throws Exception {
		String node1=getSelfUI().selectnode;
		if(node1==null||node1.length()<=0){
			getSelfUI().showErrorMessage("请选择粘贴的节点！");
			return;
		}
		VOTreeNode node=getSelectNode();
		String fpk=(String) node.getParentnodeID();
		if(fpk==null || "".equals(fpk)){
			getSelfUI().showWarningMessage("系统节点无法粘贴！");
			return;
		}
		int row=getSelfUI().getBillCardPanel().getBillTable().getSelectedRow();
		if(row<0){
			getSelfUI().showWarningMessage("请选择一行进行模板粘贴！");
			return;
		}else{
			if(copymap==null){
				getSelfUI().showErrorMessage("请选择复制模板！");
				return;
			}
			String pk_credence_b=(String) getSelfUI().getBillCardPanel().getBodyValueAt(row, "pk_datachange_b");
			CredenceHVO hvo=(CredenceHVO) copymap.getParentVO();
			if(hvo==null){
				getSelfUI().showErrorMessage("无数据，无法进行粘贴操作！");
				return;
			}else{
				hvo.setPk_credence_h(null);
				hvo.setPk_datadefinit_h(pk_credence_b);

				DipDatachangeBVO dbvo=(DipDatachangeBVO) HYPubBO_Client.queryByPrimaryKey(DipDatachangeBVO.class, pk_credence_b);
				//String nczzname=dbvo.getOrgname();//NC组织名称
				//hvo.setUnit(nczzname);//单位
				UFBoolean sysmodel=dbvo.getIssystmp();//是否系统模板
				String nczzzd=dbvo.getOrgcode();//NC组织编码
				hvo.setUnit(nczzzd);//单位
				hvo.setSysmodel(sysmodel);//是否系统模板

				String pk_credence_h=dbvo.getPk_datachange_h();
				DipDatachangeHVO dhvo=(DipDatachangeHVO) HYPubBO_Client.queryByPrimaryKey(DipDatachangeHVO.class,pk_credence_h);
				String code=dhvo.getCode();//编码
				String name=dhvo.getName();//名称
				String systype=dhvo.getSystype();//系统类型
				String busdata=dhvo.getBusidata();//业务数据
				String pk_glorg=dhvo.getPk_glorg();//NC系统账簿
				hvo.setCode(code);
				hvo.setName(name);
				hvo.setSystype(systype);
				hvo.setBusdata(busdata);
				hvo.setAccoutbook(pk_glorg);

				CredenceHVO[] chvo=(CredenceHVO[]) HYPubBO_Client.queryByCondition(CredenceHVO.class, "pk_datadefinit_h ='"+pk_credence_b+"' and nvl(dr,0)=0");
				if(chvo !=null && chvo.length>0){
					MessageDialog.showHintDlg(getSelfUI(), "提示", "当前模板已定义!");
					return;

				}else{
					CredenceBVO[] bvos =(CredenceBVO[]) copymap.getChildrenVO();
					if(bvos==null||bvos.length<=0){
						getSelfUI().showWarningMessage("无数据,无法进行粘贴操作!");
						return;
					}else{
						String	pk = HYPubBO_Client.insert(hvo);
						for(int i=0;i<bvos.length;i++){
							bvos[i].setPk_credence_b(null);
							bvos[i].setPk_credence_h(pk);
						}
						String[] s=HYPubBO_Client.insertAry(bvos);
						if(s!=null && s.length>0){
							MessageDialog.showHintDlg(getSelfUI(), "粘贴", "粘贴成功！");
							String sql="update dip_datachange_b set tempexist='已定义' where pk_datachange_b='"+pk_credence_b+"'";
							iq.exesql(sql);
							//getSelfUI().getBillCardPanel().getBodyItem("tempexist").setValue("已定义");
							getSelfUI().getBillCardPanel().setBodyValueAt("已定义", row, "tempexist");
						}
					}
				}
			}
		}
	}

	/**
	 * @author 程莉
	 * @date 2011-6-17
	 * @description 系统模板：选择某个系统的某个节点的表体的某一行，点击"系统模板"按钮，则把"是否系统模板"变为true，且表体只能有一个系统模板
	 */
	public void sysModel() throws Exception{
		String node=getSelfUI().selectnode;
		if(node==null||node.length()<=0){
			getSelfUI().showErrorMessage("请选择表体的某一行！");
			return;
		}
		VOTreeNode treeNode=getSelectNode();
		if(treeNode !=null){
			String fpk=(String) treeNode.getParentnodeID();
			String pk=(String) treeNode.getNodeID();//主表主键
			if(pk !=null && pk.length()>0 && !"".equals(pk)){
				DipDatachangeBVO[] ddbvo= (DipDatachangeBVO[]) HYPubBO_Client.queryByCondition(DipDatachangeBVO.class, "pk_datachange_h='"+pk+"' and isnull(dr,0)=0");
				int count=0;
				String bpk=null;
				if(ddbvo !=null && ddbvo.length>0){		
					//被选择的行
					int row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
					if(row ==-1){
						getSelfUI().showWarningMessage("请选择要设置为系统模板的数据！");
						return;
					}
					for(int i=0;i<ddbvo.length;i++){
						//是否系统模板
						UFBoolean isSysModel=ddbvo[i].getIssystmp()==null?new UFBoolean(false):ddbvo[i].getIssystmp();
						if(isSysModel.booleanValue()){
							bpk=ddbvo[i].getPk_datachange_b();//记录先前的issystmp='Y'的数据的主键
							count ++;
						}
					}				
					if(count >0){
						//2011-7-12 begin
						//修改【是否系统模板】值，把其变为true
						String pk_datachange_b=(String) getSelfUI().getBillCardPanel().getBodyValueAt(row, "pk_datachange_b");//子表主键
						String sql="update dip_datachange_b set issystmp='Y' where pk_datachange_b='"+pk_datachange_b+"' and nvl(dr,0)=0";
						boolean sucess=iq.exectEverySql(sql);
						if(sucess==false){
							if(pk_datachange_b.equals(bpk)){
								String sql3="update dip_datachange_b set issystmp='N' where pk_datachange_b='"+pk_datachange_b+"' and nvl(dr,0)=0";
								iq.exesql(sql3);
								getSelfUI().getBillCardPanel().setBodyValueAt("N", row, "issystmp");
							}else{
								//把以前的系统模板设置为false
								String sql2="update dip_datachange_b set issystmp='N' where pk_datachange_h='"+pk+"' and pk_datachange_b='"+bpk+"' and nvl(dr,0)=0";
								iq.exesql(sql2);
								for(int i=0;i<ddbvo.length;i++){
									if(i !=row){							
										getSelfUI().getBillCardPanel().setBodyValueAt("N", i, "issystmp");
									}
								}

								//设置当前选择的为系统模板的issystmp='Y'
								getSelfUI().getBillCardPanel().setBodyValueAt("Y", row, "issystmp");
								MessageDialog.showHintDlg(getSelfUI(), "提示", "设置为系统模板完成！");
							}
						}
						//2011-7-12 end


						/*getSelfUI().showErrorMessage("当前系统当前节点下有且只能有一个【系统模板】！");
						return;*/
					}else{
						//修改【是否系统模板】值，把其变为true
						String pk_datachange_b=(String) getSelfUI().getBillCardPanel().getBodyValueAt(row, "pk_datachange_b");//子表主键
						String sql="update dip_datachange_b set issystmp='Y' where pk_datachange_b='"+pk_datachange_b+"' and nvl(dr,0)=0";
						boolean sucess=iq.exectEverySql(sql);
						if(sucess==false){
							getSelfUI().getBillCardPanel().setBodyValueAt("Y", row, "issystmp");
							MessageDialog.showHintDlg(getSelfUI(), "提示", "设置为系统模板完成！");
						}
					}
				}

				/*
				 * 多选nc组织编码（nczz）存值问题:避免凭证模板界面关闭后，nc组织编码无值
				 * 2011-6-29 
				 * 1136--1157行
				 */
				/*String orgName=null;//多个名称连接的字符串
				String[] arrayName=null;//名称数组
				String sql="";
				String unitcode=null;
				String nczz=null;
				for(int j=0;j<ddbvo.length;j++){
					orgName=ddbvo[j].getOrgname();
					arrayName=orgName.split(",");
					for(int k=0;k<arrayName.length;k++){
						if(k==0){
							nczz=arrayName[k];
							sql="select unitcode from bd_corp where unitname='"+nczz+"' and nvl(dr,0)=0";
							unitcode=iq.queryfield(sql);
						}else{
							nczz=arrayName[k];
							sql="select unitcode from bd_corp where unitname='"+nczz+"' and nvl(dr,0)=0";
							unitcode=unitcode+","+iq.queryfield(sql);
						}
					}
					this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(unitcode, j, "nczz");
				}*/

			}
		}
	}


	DataChangeClientUI myui=(DataChangeClientUI) this.getBillUI();
	public MyEventHandler(BillTreeCardUI billUI, ICardController control){
		super(billUI,control);		
	}

	/**
	 * 取得当前UI类
	 * 
	 * @return
	 */
	private DataChangeClientUI getSelfUI() {
		return (DataChangeClientUI) getBillUI();
	}

	/**
	 * 取得选中的节点对象
	 * 
	 * @return
	 */
	private VOTreeNode getSelectNode() {
		return getSelfUI().getBillTreeSelectNode();
	}

	/**
	 * 该方法在增行和插行后被调用，可以在该方法中为新增的行设置一些默认值
	 * 
	 * @param newBodyVO
	 * @return TODO
	 */
	protected CircularlyAccessibleValueObject processNewBodyVO(CircularlyAccessibleValueObject newBodyVO) {

		VOTreeNode parent = this.getSelectNode();

		// 将主表主键写到子表中
		newBodyVO.setAttributeValue("pk_datachange_h", parent == null ? null : parent.getNodeID());

		// 添加结束
		return newBodyVO;
	}

	/**
	 * 
	 * <P>此方法为覆盖父类方法
	 * <BR>将父类中的ISingleController判断去掉，否则保存时，表头为空
	 * @throws Exception
	 * @see nc.ui.trade.treecard.TreeCardEventHandler#onBoSave()
	 */
	protected void onBoSave() throws Exception {
		//校验页面数据是否为空
		int kk=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		List<Integer> list=new ArrayList<Integer>();
		for(int i=0;i<kk;i++){
			String code=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_datachange_b").getValueAt(i, "nczz");
			String name=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_datachange_b").getValueAt(i, "orgname");
			if((code==null||code.trim().equals(""))&&(name==null||name.trim().equals(""))){
				//getBillCardPanelWrapper().getBillCardPanel().getBillTable("dip_datachange_b").changeSelection(i, 0, true, true);
				list.add(i);
				//getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_datachange_b").delLine(w);
			}
		}
		
		if(list!=null&&list.size()>0){
			int[] w=new int[list.size()];
			for(int i=0;i<list.size();i++){
				w[i]=list.get(i);
			}
			getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_datachange_b").delLine(w);
		}
		
		BillData bd=getBillCardPanelWrapper().getBillCardPanel().getBillData();
		if(bd!=null){
			bd.dataNotNullValidate();
		}

		//校验编码唯一 2011-7-4 cl
		DataChangeClientUI ui=(DataChangeClientUI) getBillUI();
		String pk=(String) ui.getBillCardPanel().getHeadItem("pk_datachange_h").getValueObject();
		if(pk==null || pk.trim().equals("")){
			pk=" ";
		}
		VOTreeNode node=getSelectNode();
		String pk_xt=(String) ui.getBillCardPanel().getHeadItem("pk_xt").getValueObject();
		String code=(String) ui.getBillCardPanel().getHeadItem("code").getValueObject();
		IUAPQueryBS bs=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//		Collection ccode=bs.retrieveByClause(DipDatachangeHVO.class,"code='"+code+"' and pk_datachange_h <> '"+pk+"' and pk_xt='"+pk_xt+"' and isfolder='N' and nvl(dr,0)=0");
		Collection ccode=bs.retrieveByClause(DipDatachangeHVO.class,"code='"+code+"' and pk_datachange_h <> '"+pk+"' and pk_xt='"+pk_xt+"' and nvl(dr,0)=0");
		if(ccode !=null){
			if(ccode.size()>=1){
				ui.showWarningMessage(IContrastUtil.getCodeRepeatHint("编码", code));
				return;
			}
		}

		//获得界面原始数据的vo
		AggregatedValueObject billVO = getBillTreeCardUI().getChangedVOFromUI();
		setTSFormBufferToVO(billVO);


		//获得界面新数据vo
		AggregatedValueObject checkVO = getBillTreeCardUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);

		DipDatachangeHVO hvo=(DipDatachangeHVO) checkVO.getParentVO();
		DipDatachangeBVO[] bvo=(DipDatachangeBVO[]) checkVO.getChildrenVO();

		Boolean gu=hvo.getGuding()!=null&&hvo.getGuding().equals("Y");
		int row=getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().getTable().getRowCount();

		if(gu){
			if(row<1){
				this.getSelfUI().showWarningMessage("固定组织，必须增加一行数据");
				//this.getBillUI().showErrorMessage("固定组织为是，必须增加一行数据");
				return;
			}
			if(row>1){
				this.getSelfUI().showWarningMessage("固定组织格式，只能增加一行数据");
				return;	
			}

			//2011-7-6 cl 为固定组织，表体NC组织编码不能多选
			String str=null;
			String[] arrStr=null;
			if(bvo!=null){
				for(int i=0;i<bvo.length;i++){
					str=bvo[i].getOrgname();
					arrStr=str.split(",");
					if(arrStr.length>1){
						getSelfUI().showErrorMessage("固定组织格式，NC组织编码不能多选!");
						return;
					}
				}
			}
		}else{
			if(hvo.getOrg()==null||"".equals(hvo.getOrg())){
				getSelfUI().showErrorMessage("组织字段不能为空！");
				return;
			}
		}

		if(bvo!=null&&bvo.length>0){
			for(int i=0;i<bvo.length;i++){
				for(int j=0;j<i;j++){
					if(bvo[j].getOrgname().equals(bvo[i].getOrgname())){
						getSelfUI().showErrorMessage("NC组织名称不可重复！");
						return ;
					}
				}
			}
		}

		//2011-7-5 cl
		deleteLine();

		Object o = null;
		ISingleController sCtrl = null;

		boolean isSave = true;
		if (billVO.getParentVO() == null && (billVO.getChildrenVO() == null || billVO.getChildrenVO().length == 0))
			isSave = false;
		else if (getBillUI().isSaveAndCommitTogether())
			billVO = getBusinessAction().saveAndCommit(billVO, getUIController().getBillType(), _getDate().toString(), getBillUI().getUserObject(), checkVO);
		else
			billVO = getBusinessAction().save(checkVO, getUIController().getBillType(), _getDate().toString(), getBillUI().getUserObject(), checkVO);

		if (sCtrl != null && sCtrl.isSingleDetail())
			billVO.setParentVO((CircularlyAccessibleValueObject) o);
		int nCurrentRow = -1;
		if (isSave) {

			/* 2011-6-28 避免多选或者单选保存后，再次修改保存后，存的还是原始值*/
//			if(bvo !=null && bvo.length>0){
//				HYPubBO_Client.updateAry(bvo);
//				billVO.setChildrenVO(bvo);
//			}
			/* end */

			//修改
			if (isEditing())
				if (getBufferData().isVOBufferEmpty()) {	
					getBufferData().addVOToBuffer(billVO);
					nCurrentRow = 0;
				} else {
					getBufferData().setCurrentVO(billVO);
					nCurrentRow = getBufferData().getCurrentRow();
				}

			//2011-7-9 cl 修改后，避免凭证模板中 单位 字段还以修改前的值的问题
			if(isEditing()){
				if(bvo !=null && bvo.length>0){
					String cpk=null;
					String unit=null;
					for(int i=0;i<bvo.length;i++){
						cpk=bvo[i].getPk_datachange_b();
						unit=bvo[i].getOrgcode();//公司主键
					}
					CredenceHVO[] chvo=(CredenceHVO[]) HYPubBO_Client.queryByCondition(CredenceHVO.class, " pk_datadefinit_h='"+cpk+"'");
					if(chvo !=null && chvo.length>0){
						//先把以前的值清空，更新vo，再填充值，再更新vo
						chvo[0].setUnit("");
						HYPubBO_Client.updateAry(chvo);
						chvo[0].setUnit(unit);
						HYPubBO_Client.updateAry(chvo);
					}
				}
			}
			setAddNewOperate(isAdding(), billVO);

		}



		// 退出保存，恢复浏览状态
		setSaveOperateState();
		if (nCurrentRow >= 0)
			getBufferData().setCurrentRow(nCurrentRow);

		//如果该单据增加保存时是自动维护树结构，则执行如下操作
		if (getUITreeCardController().isAutoManageTree()) {		

			getSelfUI().insertNodeToTree(billVO.getParentVO());

		}

		/*
		 * 多选nc组织编码（nczz）存值问题
		 * 2011-6-28 
		 * 418--440行
		 */
		String orgName=null;//多个名称连接的字符串
		String[] arrayName=null;//名称数组
		String sql="";
		String unitcode=null;
		String nczz=null;
		if(bvo !=null){
			for(int j=0;j<bvo.length;j++){
				orgName=bvo[j].getOrgname();
				arrayName=orgName.split(",");
				for(int k=0;k<arrayName.length;k++){
					if(k==0){
						nczz=arrayName[k];
						sql="select unitcode from bd_corp where unitname='"+nczz+"' and nvl(dr,0)=0";
						unitcode=iq.queryfield(sql);
					}else{
						nczz=arrayName[k];
						sql="select unitcode from bd_corp where unitname='"+nczz+"' and nvl(dr,0)=0";
						unitcode=unitcode+","+iq.queryfield(sql);
					}
				}
				this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(unitcode, j, "nczz");
			}
		}
		
//		getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(false);
//		getSelfUI().getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(false);
		getSelfUI().onTreeSelectSetButtonState(getSelectNode());
	}


	@SuppressWarnings("unused")
	private CircularlyAccessibleValueObject[] getChildVO(AggregatedValueObject retVo) {
		CircularlyAccessibleValueObject childVos[] = null;
		if (retVo instanceof IExAggVO)
			childVos = ((IExAggVO) retVo).getAllChildrenVO();
		else
			childVos = retVo.getChildrenVO();
		return childVos;
	}

//	@Override
//	protected void onBoDel() throws Exception {
//	// TODO Auto-generated method stub
//	super.onBoDel();

//	}
	/*
	 * 数据转换中的删除行操作
	 * lx 2011-5-23
	 */

	@Override
	protected void onBoLineDel() throws Exception {
		VOTreeNode node=getSelectNode();
		DipDatachangeHVO hvo=(DipDatachangeHVO) node.getData();
		String pk= hvo.getPk_datachange_h();
		hvo=(DipDatachangeHVO) HYPubBO_Client.queryByPrimaryKey(DipDatachangeHVO.class, pk);
		if(SJUtil.isNull(hvo)){
			this.getBillUI().showWarningMessage("此节点还在编辑，请先保存！");
			return;
		}
		//得到数据行
		int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		if(row<0){
			return;
		}
		//得到当前行号
		int rowno=getSelfUI().getBillCardPanel().getBillTable().getSelectedRow();
		if(rowno<0){
			this.getBillUI().showWarningMessage("请选择要操作的行！");
			return;
		}
//		if(isEditing()){
//		this.getBillUI().showWarningMessage("此节点还在编辑，请先保存！");
//		return;
//		}

		//2011-7-4 cl 解决删行取消时，值被删除掉问题
		String ob=getSelfUI().getBillCardPanel().getBillModel().getValueAt(rowno, "pk_datachange_b")==null?"":getSelfUI().getBillCardPanel().getBillModel().getValueAt(rowno, "pk_datachange_b").toString();
		if(ob==null || "".equals(ob)){
			super.onBoLineDel();
			getSelfUI().delstr = getSelfUI().delstr +",'"+ob.toString()+"'";
		}else{
			super.onBoLineDel();
			getSelfUI().delstr = getSelfUI().delstr +",'"+ob.toString()+"'";	
		}

		/*nc.vo.dip.datachange.MyBillVO billvo=(nc.vo.dip.datachange.MyBillVO) getSelfUI().getBufferData().getCurrentVO();
		DipDatachangeBVO[] vos=(DipDatachangeBVO[]) billvo.getChildrenVO();
		for(int i=0;i<row;i++){
			//getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(i+1+"", i, "orgcode");
			if(rowno==i)
				HYPubBO_Client.delete(vos[i]);
			billvo.setChildrenVO(vos);
			getBufferData().setCurrentVO(billvo);
			getBufferData().addVOToBuffer(billvo);
		}
		super.onBoLineDel();*/		

	}

	/**
	 * 功能：处理删除行上的数据，如果有删行，则在保存时，把真正要删除的删掉
	 * 作者：cl
	 * 日期:2011-07-04
	 * */
	public void deleteLine(){
		String sql =" delete from dip_datachange_b where pk_datachange_b in ('11122aabb'"+getSelfUI().delstr+")";
		try {
			iq.exesql(sql);
		} catch (BusinessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DbException e) {
			e.printStackTrace();
		}
		getSelfUI().delstr = "";

	}
	@Override
	protected void setTSFormBufferToVO(AggregatedValueObject setVo) throws Exception {
		if (setVo == null)
			return;
		AggregatedValueObject vo = getBufferData().getCurrentVO();
		if (vo == null)
			return;
		if (getBillUI().getBillOperate() == 0) {
			if (vo.getParentVO() != null && setVo.getParentVO() != null)
				setVo.getParentVO().setAttributeValue("ts", vo.getParentVO().getAttributeValue("ts"));
			SuperVO changedvos[] = (SuperVO[]) (SuperVO[]) getChildVO(setVo);
			if (changedvos != null && changedvos.length != 0) {
				HashMap bufferedVOMap = null;
				SuperVO bufferedVOs[] = (SuperVO[]) (SuperVO[]) getChildVO(vo);
				if (bufferedVOs != null && bufferedVOs.length != 0) {
					bufferedVOMap = Hashlize.hashlizeObjects(bufferedVOs, new VOHashPrimaryKeyAdapter());
					for (int i = 0; i < changedvos.length; i++) {
						if (changedvos[i].getPrimaryKey() == null)
							continue;
						ArrayList bufferedAl = (ArrayList) bufferedVOMap.get(changedvos[i].getPrimaryKey());
						if (bufferedAl != null) {
							SuperVO bufferedVO = (SuperVO) bufferedAl.get(0);
							changedvos[i].setAttributeValue("ts", bufferedVO.getAttributeValue("ts"));
						}
					}

				}
			}
		}
	}
	@Override
	public  void  onTreeSelected(VOTreeNode arg0){
		try {
			SuperVO  vo=(SuperVO) arg0.getData();
			String pk_datachange_h=(String) vo.getAttributeValue("pk_datachange_h");
			SuperVO[] vos=HYPubBO_Client.queryByCondition(nc.vo.dip.datachange.DipDatachangeBVO.class,"pk_datachange_h='"+pk_datachange_h+"' and  isnull(dr,0)=0");
			nc.vo.dip.datachange.MyBillVO billvo=new nc.vo.dip.datachange.MyBillVO();


			DipDatachangeHVO hvo = (DipDatachangeHVO)HYPubBO_Client.queryByPrimaryKey(DipDatachangeHVO.class, pk_datachange_h);
			if(hvo==null&&vo!=null&&vo instanceof DipDatachangeHVO){
				hvo=(DipDatachangeHVO) vo;
			}

			//设置主VO
			billvo.setParentVO(hvo);
			//设置VO
			billvo.setChildrenVO(vos);				
			//显示数据
			getBufferData().addVOToBuffer(billvo);

			getBillTreeCardUI().getTreeToBuffer().put(arg0.getNodeID(),"" + (getBufferData().getVOBufferSize() - 1));
			if(hvo!=null){
				String pk_xt=hvo.getPk_xt()==null?"":hvo.getPk_xt();
				if(pk_xt!=null&&pk_xt.length()>0){
					UIRefPane uir=(UIRefPane)this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("busidata").getComponent();
					/*liyunzhe modify 业务表修改成树形参照 2012-06-04 strat*/
					DataDefinitTableTreeRefModel model=new DataDefinitTableTreeRefModel();
					model.setClassWherePart(model.getClassWherePart() +" and pk_xt='"+hvo.getPk_xt()+"'");
					model.addWherePart(" and tabsoucetype='自定义'");
					uir.setRefModel(model);
					/*liyunzhe modify 业务表修改成树形参照 2012-06-04 end*/
//					DataDefinitRefModel model2=(DataDefinitRefModel) uir.getRefModel();
//					model2.addWherePart(" and dip_datadefinit_h.pk_datadefinit_h <>'"+pk_datachange_h+"' and Pk_Sysregister_h='"+pk_xt+"' and dip_datadefinit_h.iscreatetab='Y' and nvl(dip_datadefinit_h.dr,0)=0 and nvl(dip_datadefinit_h.isfolder,'N')='N' ");
//					getBillCardPanelWrapper().getBillCardPanel().execHeadFormulas(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("fpk").getEditFormulas());
				}
			}
		} catch (UifException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onBoDelete() throws Exception {
		VOTreeNode tempNode=getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("请选择要删除的节点！");
			return ;
		}
		String pk_node=(String)tempNode.getParentnodeID();
		if(pk_node==null||pk_node.equals("")){
			getSelfUI().showErrorMessage("系统节点不能做删除操作！");
			return ;
		}else{
			/* 2011-5-23 NC系统下的节点不能删除 */
			String sql="select h.pk_sysregister_h from dip_sysregister_h h where h.code='0000' and h.pk_sysregister_h='"+tempNode.getNodeID()+"' and nvl(h.dr,0)=0";
			String isNC=iq.queryfield(sql);
			if(isNC !=null && !"".equals(isNC.trim())){
				getSelfUI().showWarningMessage("NC系统下，只能浏览数据，不能删除！"); 
				return;
			}
			/* end */
		}
//		getBufferData().setCurrentVO();
		Integer flag = MessageDialog.showOkCancelDlg(this.getBillUI(), "提示", "是否确认要删除?");
		
		if(flag == 1){
			pk_node = ((DataChangeClientUI) getBillUI()).selectnode;
			if("".equals(pk_node)){
				NCOptionPane.showMessageDialog(this.getBillUI(),"请选择要删除的节点。");
				return ;
			}
			VOTreeNode  node1=(VOTreeNode) tempNode.getParent();
			DipDatachangeBVO[] vos = (DipDatachangeBVO[])HYPubBO_Client.queryByCondition(DipDatachangeBVO.class, " pk_datachange_h='"+pk_node+"' and isnull(dr,0)=0  ");
			if(vos!=null && vos.length>0){
				for(DipDatachangeBVO bvo : vos ){
					HYPubBO_Client.delete(bvo);
				}
			}	
			DipDatachangeHVO vo = (DipDatachangeHVO) HYPubBO_Client.queryByPrimaryKey(DipDatachangeHVO.class, pk_node);
			if(vo!=null)
				HYPubBO_Client.delete(vo);

//			更新树
			this.getBillTreeCardUI().getBillTreeData().deleteNodeFromTree(this.getBillTreeCardUI().getBillTreeSelectNode());
			if(node1!=null){
				getSelfUI().getBillTree().setSelectionPath(new javax.swing.tree.TreePath(node1
									.getPath()));
				}
		}


	}
	private boolean isnew=false;
	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		VOTreeNode node=getSelectNode();
		if(node==null){
			getSelfUI().showErrorMessage("请选择要修改的节点！");
			return ;
		}
		String str=(String) node.getParentnodeID();
		if(str ==null ||str.equals("")){
			getSelfUI().showWarningMessage("系统节点不能做修改操作！");
			return;
		}else{
			/*查询NC系统下的节点，不允许做修改操作 2011-5-23 程莉 begin */
			String sql="select h.pk_sysregister_h from dip_sysregister_h h where h.code='0000' and h.pk_sysregister_h='"+str+"' and nvl(h.dr,0)=0";
			String isNC=iq.queryfield(sql);
			if(isNC !=null && !"".equals(isNC.trim())){
				getSelfUI().showWarningMessage("NC系统下，只能浏览数据，不能修改！"); 
				return;
			}
			/* end */
		}
		//2011-4-27 程莉 当点击修改时 启用和停用 按钮不可用
		//getSelfUI().getButtonManager().getButton(IBtnDefine.Stop).setEnabled(false);
		//getSelfUI().getButtonManager().getButton(IBtnDefine.Start).setEnabled(false);
		super.onBoEdit();
		//String cpk=(String) node.getNodeID();


		DataChangeClientUI ui= (DataChangeClientUI) getBillUI();

		String pk=(String) ui.getBillCardWrapper().getBillCardPanel().getHeadItem("pk_datachange_h").getValueObject();
		if(pk==null&&pk.equals("")){
			isnew=true;
		}else {
			DipDatachangeHVO vo=(DipDatachangeHVO) HYPubBO_Client.queryByPrimaryKey(DipDatachangeHVO.class, pk);
			if(SJUtil.isNull(vo)){
//				getBillUI().setBillOperate(IBillOperate.OP_ADD);
				isnew=true;
			}else{
				isnew=false;
			}

		}
		String pkk=(String) node.getNodeID();
		DipDatachangeHVO hvoo=(DipDatachangeHVO) HYPubBO_Client.queryByPrimaryKey(DipDatachangeHVO.class, pkk);
		String kk=hvoo.getBusidata();
		//如果是固定组织，那么组织字段不能编辑，如果不是固定组织，那么选择字段的参照，并且，给表体增加一行
		String guding=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("guding").getValueObject()==null?"N":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("guding").getValueObject().toString();
		if(guding!=null&&guding.length()>0&&new UFBoolean(guding).booleanValue()){
			
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("org").setEnabled(false);
		}else{
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("org").setEnabled(true);
			UIRefPane pane=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("org").getComponent();
			DataDefinitbRefModel model=(DataDefinitbRefModel) pane.getRefModel();//new DataDefinitbRefModel();
			model.addWherePart("and dip_datadefinit_h.pk_datadefinit_h='"+kk+"' and nvl(dip_datadefinit_h.dr,0)=0");
			pane.setRefModel(model);
			onBoLineAdd();
		}
	}
	@Override
	protected void onBoCancel() throws Exception{
		//2011-4-27 程莉 点击取消按钮时，将“启用”和“停用”按钮设置为可用
//		getSelfUI().getButtonManager().getButton(IBtnDefine.Stop).setEnabled(true);
//		getSelfUI().getButtonManager().getButton(IBtnDefine.Start).setEnabled(true);
		super.onBoCancel();

		//2011-6-28 程莉 修改取消时，nc组织编码值显示问题
		VOTreeNode node=getSelectNode();
		if(node !=null){
			String selectnode=(String) node.getNodeID();
			if(selectnode !=null && !"".equals(selectnode) && selectnode.length()>0){
				try {
					DipDatachangeBVO[] bvo=(DipDatachangeBVO[]) HYPubBO_Client.queryByCondition(DipDatachangeBVO.class, " pk_datachange_h='"+selectnode+"' and isnull(dr,0)=0");
					String orgName=null;//多个名称连接的字符串
					String[] arrayName=null;//名称数组
					String sql="";
					String unitcode=null;//编码
					String nczz=null;
					if(bvo!=null && bvo.length>0){
						for(int j=0;j<bvo.length;j++){
							orgName=bvo[j].getOrgname();
							arrayName=orgName.split(",");
							for(int k=0;k<arrayName.length;k++){
								if(k==0){
									nczz=arrayName[k];
									sql="select unitcode from bd_corp where unitname='"+nczz+"' and nvl(dr,0)=0";
									try {
										unitcode=iq.queryfield(sql);
									} catch (BusinessException e) {
										e.printStackTrace();
									} catch (SQLException e) {
										e.printStackTrace();
									} catch (DbException e) {
										e.printStackTrace();
									}
								}else{
									nczz=arrayName[k];
									sql="select unitcode from bd_corp where unitname='"+nczz+"' and nvl(dr,0)=0";
									try {
										unitcode=unitcode+","+iq.queryfield(sql);
									} catch (BusinessException e) {
										e.printStackTrace();
									} catch (SQLException e) {
										e.printStackTrace();
									} catch (DbException e) {
										e.printStackTrace();
									}
								}
							}
							this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(unitcode, j, "nczz");
						}
					}
				} catch (UifException e) {
					e.printStackTrace();
				}
			}
			
			
			
			DipDatachangeHVO hvo=(DipDatachangeHVO) node.getData();
			getSelfUI().onTreeSelectSetButtonState(getSelectNode());
//			if(hvo.getIsfolder()!=null&&hvo.getIsfolder().booleanValue()){
//				//把按钮关闭。
//				this.getButtonManager().getButton(IBtnDefine.CRESET).setEnabled(false);
//				this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
//				this.getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
//				this.getButtonManager().getButton(IBtnDefine.MBZH).setEnabled(false);
//				
//				this.getButtonManager().getButton(IBtnDefine.Conversion).setEnabled(false);
//				this.getButtonManager().getButton(IBtnDefine.YuJing).setEnabled(false);
//				this.getButtonManager().getButton(IBtnDefine.DataValidate).setEnabled(false);
//				
//				this.getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(false);
//				this.getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
//			}else{
//				this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
//				this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(false);
//
//			}
//			getSelfUI().updateButtonUI();
		}

		getSelfUI().delstr ="";//cl 2011-07-04 取消清空删行缓存
	}






	@Override
	protected void onBoLinePaste() throws Exception {
		super.onBoLinePaste();
		int row=getSelfUI().getBillCardPanel().getBillTable().getSelectedRow();
		getSelfUI().getBillCardPanel().getBodyValueAt(row, new DipDatachangeBVO().DISABLE);		
		getSelfUI().getBillCardPanel().setBodyValueAt(null,row, new DipDatachangeBVO().getPKFieldName());
		getSelfUI().getBillCardPanel().setBodyValueAt(null,row, new DipDatachangeBVO().TEMPEXIST);

	}

	@Override
	protected void onBoLineAdd() throws Exception {
		//增行：默认为非系统模板，停用标志不可用
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("disable").setEnabled(false);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("issystmp").setEnabled(false);
		VOTreeNode node=getSelectNode();
		Object obj=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("guding").getValueObject();
		if(obj!=null){

			UFBoolean gu=new UFBoolean( (String)obj);
			int row=getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().getTable().getSelectedRow();

			if(gu!=null&&gu.booleanValue()){
				if(row>=0){
					getSelfUI().showErrorMessage("固定组织只能增加一行数据！");
					return;
				}

			}
		}


		super.onBoLineAdd();
	}
//	public void set(){
//	VOTreeNode node=getSelectNode();
//	if(SJUtil.isNull(node)){
//	this.getSelfUI().showWarningMessage("请选择节点！");
//	return;
//	}
//	String []str={"相同科目+相同辅助核算项","相同科目+相同辅助核算项+相同借贷方向","相同科目+相同辅助核算项+相同借贷方向+相同摘要"};
//	AskDLG dlg=new AskDLG(getSelfUI(),"分录合并设置",null,str);
//	dlg.show();
//	int result=dlg.getRes();
//	this.getSelfUI().showOkCancelMessage(""+result);
//	}
//	分录合并设置
	public void set(){
		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			this.getSelfUI().showWarningMessage("请选择要操作的节点！");
			return;
		}
		DipDatachangeHVO hvo=(DipDatachangeHVO) node.getData();
		String fpk=hvo.getFpk();
		String mpk=hvo.getPrimaryKey();
		if(SJUtil.isNull(fpk)||fpk.length()<=0){
			this.getSelfUI().showWarningMessage("系统节点不能编辑！");
			return;
		}

//		int row=this.getSelfUI().getBillCardPanel().getBillTable().getSelectedRow();
//		int bodyrow=this.getBillCardPanelWrapper().getBillCardPanel().getRowCount();
////		if(row<0){
////		this.getSelfUI().showWarningMessage("请选择需要操作的定义！");
////		return;
////		}
//		DipDatachangeBVO []bvo=null;
//		try {
//		bvo=(DipDatachangeBVO[]) getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
//		DipDatachangeBVO bvoo=bvo[bodyrow];
//		String pk=bvoo.getPrimaryKey();
//		if(pk==null||pk.length()<=0){
//		this.getSelfUI().showWarningMessage("此节点还没有保存，请先保存！");
//		return;
//		}
//		} catch (Exception e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//		}

		int i=hvo.getResults()==null?2:hvo.getResults();
		String []str={"相同科目+相同辅助核算项","相同科目+相同辅助核算项+相同借贷方向","相同科目+相同辅助核算项+相同借贷方向+相同摘要"};
		AskDLG dlg=new AskDLG(getSelfUI(),"分录合并设置",null,str);
		dlg.setIsSelect(i);
		dlg.show();

		int result=dlg.getRes();
		if(result<0){
			result=2;
		}
		if(i==result){
			return;
		}
		int ret=getSelfUI().showOkCancelMessage("修改分录合并设置会影响凭证生成，是否继续？");
		if(ret!=1){
			return;
		}
		String sql="update dip_datachange_h set results='"+(result)+"' where pk_datachange_h ='"+mpk+"'";
		IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
		try{
			iq.exesql(sql);
		}catch (Exception e) {
			e.printStackTrace();
		}
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("results").setValue(result);
		getBufferData().getCurrentVO().getParentVO().setAttributeValue("results", result);
		try {
			getSelfUI().insertNodeToTree(getBufferData().getCurrentVO().getParentVO());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		hvo.setResults(result);
//		try {
//		super.onBoSave();
//		} catch (Exception e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//		}
	}
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		switch (intBtn) {
		case IBtnDefine.DataValidate://数据校验
			dataCheck();
			break;
		case IBtnDefine.YuJing://预警
			yuJing();
			break;
		case IBtnDefine.MBZH://大合并的模板按钮
			onBoMBZH();
			break;
		//2011-7-13
		case IBtnDefine.CRESET://大合并的设置按钮
			onBoCreSet();
			break;
		case IBtnDefine.AddEffectFactor://影响因素，暂时不用了
			factor();
			break;
		case IBtnDefine.CREDENCE://大概是合并凭证把，暂时不用了
			credence();
			break;
		case IBtnDefine.HBSET://合并凭证设置，暂时不用了
			set();
			break;
		case IBtnDefine.Conversion://转换
			conversion();
			break;
			//2011-6-13
		case IBtnDefine.PASTEMODEL://粘贴模板
			pasteModel();
			break;
			//2011-6-17
		case IBtnDefine.SYSMODEL://系统模板，暂时不用了
			sysModel();
			break;
		case IBtnDefine.Model://模板，暂时不用了
			onBoModel();
			break;
		case IBtnDefine.moveFolderBtn://模板，暂时不用了
			onBoMoveFolder();
			break;
		case IBtnDefine.CONTROL://模板，暂时不用了
			onBoControl();
			break;
		}
	}


	/**
		 * @desc 文件移动
		 * */
		public void onBoMoveFolder() throws Exception{
			
			nc.vo.dip.datachange.MyBillVO billvo=(nc.vo.dip.datachange.MyBillVO) getBufferData().getCurrentVO();
			if(billvo!=null&&billvo.getParentVO()!=null){
				DipDatachangeHVO hvo=(DipDatachangeHVO) billvo.getParentVO();
				MovefolderDLG dlg=new MovefolderDLG(getBillUI(),IContrastUtil.YWLX_ZH,hvo);
				dlg.showModal();
				String ret=dlg.getRes();
				if(ret!=null){
					hvo.setAttributeValue((String)IContrastUtil.getFpkMap().get(IContrastUtil.YWLX_ZH), ret);
					HYPubBO_Client.update(hvo);
					hvo=(DipDatachangeHVO) HYPubBO_Client.queryByPrimaryKey(DipDatachangeHVO.class, hvo.getPrimaryKey());
					billvo.setParentVO(hvo);
					getBufferData().addVOToBuffer(billvo);
					getBufferData().setCurrentVO(billvo);
					getSelfUI().getBillTree().updateUI();
					onBoRefresh();
					TableTreeNode node=(TableTreeNode) getSelfUI().getBillTree().getModel().getRoot();
					Vector v=new Vector();
					getAllNode(node, v);
					if(v!=null&&v.size()>0){
						TableTreeNode tempnode=null;
						for(int i=0;i<v.size();i++){
							String pkf=(String)((TableTreeNode)v.get(i)).getNodeID();
							if(pkf!=null&&pkf.equals(hvo.getPrimaryKey())){
								tempnode=(TableTreeNode) v.get(i);
								break;
							}
						}
						if(tempnode!=null){
							getSelfUI().getBillTree().setSelectionPath(new javax.swing.tree.TreePath( tempnode.getPath()));
						}
					}
				}
			}
			
		}
		private void getAllNode(Object node, Vector v){
		    v.add(node);
		    int childCount = getSelfUI().getBillTree().getModel().getChildCount(node);
		    for (int i = 0; i < childCount; i++) {
	            Object child = getSelfUI().getBillTree().getModel().getChild(node, i);
	            getAllNode(child, v);
	        }
		}
	public void onBoControl(){

		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			getSelfUI().showErrorMessage("请选择要操作的节点");
			return;
		}
		DipDatachangeHVO hvo=null;
		try {
			hvo = (DipDatachangeHVO) HYPubBO_Client.queryByPrimaryKey(DipDatachangeHVO.class, node.getNodeID().toString());
		} catch (UifException e1) {
			e1.printStackTrace();
		}
		if(SJUtil.isNull(hvo)){
			getSelfUI().showErrorMessage("此节点还没有保存，请编辑！");
			return;
		}

		ControlHVO chvo=new ControlHVO();
		chvo.setBustype(SJUtil.getYWnameByLX(IContrastUtil.YWLX_ZH));
		chvo.setCode(hvo.getCode());
		chvo.setName(hvo.getName());

		ControlDlg cd=new ControlDlg(getSelfUI(),chvo,hvo.getPrimaryKey(),IContrastUtil.YWLX_ZH,hvo.getBusidata()+","+hvo.getOutpath());
		cd.showModal();
	}
	/**
	 * @desc 设置 
	 * @author 程丽
	 * */
	protected void onBoCreSet(){
		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			this.getBillUI().showErrorMessage("请选择要操作的节点！");
			return;
		}
		DipDatachangeHVO hvo=(DipDatachangeHVO) node.getData();
		String fpk=hvo.getFpk();
		String pk=hvo.getPrimaryKey();
		if(SJUtil.isNull(fpk)||fpk.length()<=0){

			getSelfUI().showErrorMessage("系统节点不可编辑！");
			return;
		}
		
		EffSetDlg dlg=new EffSetDlg(getSelfUI(),new UFBoolean(true),getSelectNode().getNodeID().toString(),true,hvo);
		dlg.show();
		try {
			DipDatachangeBVO[] vos=(DipDatachangeBVO[]) HYPubBO_Client.queryByCondition(DipDatachangeBVO.class, "pk_datachange_h ='"+pk+"' and nvl(dr,0)=0");
			DipDatachangeBVO bvo =vos[0];
			//发送方改变，设置窗口关闭后，数据转换主表界面上发送方应该也随之立即改变
			this.getBufferData().refresh(); 
			//避免刷新数据后，NC组织编码无值显示
			setNCZZCode(bvo, vos);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 多选nc组织编码（nczz）存值
	 * @param bvo
	 * @param vos
	 * @throws Exception
	 */
	public void setNCZZCode(DipDatachangeBVO bvo,DipDatachangeBVO[] vos ) throws Exception{
		/*
		 * 多选nc组织编码（nczz）存值问题:避免凭证模板界面关闭后，nc组织编码无值
		 * 2011-6-29 
		 * 1136--1157行
		 */
		String orgName=null;//多个名称连接的字符串
		String[] arrayName=null;//名称数组
		String sql="";
		String unitcode=null;
		String nczz=null;
		if(bvo !=null){
			for(int j=0;j<vos.length;j++){
				orgName=vos[j].getOrgname();
				arrayName=orgName.split(",");
				for(int k=0;k<arrayName.length;k++){
					if(k==0){
						nczz=arrayName[k];
						sql="select unitcode from bd_corp where unitname='"+nczz+"' and nvl(dr,0)=0";
						unitcode=iq.queryfield(sql);
					}else{
						nczz=arrayName[k];
						sql="select unitcode from bd_corp where unitname='"+nczz+"' and nvl(dr,0)=0";
						unitcode=unitcode+","+iq.queryfield(sql);
					}
				}
				this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(unitcode, j, "nczz");
			}
		}
	}
	/**
	 * @desc 大合并的模板按钮
	 * @author 程丽
	 * */
	private void onBoMBZH() {
		int row=getSelfUI().getBillCardPanel().getBillTable().getSelectedRow();
		if(row<0){
			getSelfUI().showErrorMessage("请选择公司进行模板操作！");
			return;
		}
		AskMBDLG ask=new AskMBDLG(getSelfUI(),getSelectNode().getParentnodeID().toString(),"模板","        请选择操作类型?",new String[]{"增加/修改模板","删除模板/取消模板引用","升级/解除系统模板","停用/启用模板","复制模板","模板引用"});
		ask.showModal();
		int result=ask.getRes();
		String ss=ask.getOrgnizeRefPnl().getRefPK();
		//如果是-1就是取消，如果是>=0那么，就是上边的String[]的第index个选择
		//getSelfUI().showErrorMessage(result+"");
		try{
			if(result==0){
				//打开 模板
				onBoModel();
			}else if(result==1){
				onDelOrConcelYy(row);
				//删除模板
			}else if(result==2){
				// 升级/解除系统模板
				sysModel();
			}else if(result==3){
				//停用/启用模板
				boolean dis=Boolean.parseBoolean(this.getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(row, "disable")==null?"":this.getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(row, "disable").toString());
				if(dis==false){
					onBoStop();
				}else{
					onBoStart();
				}
			}else if(result==4){
				//复制模板
				//原复制功能，已经不能满足现在需求
//				onBoCopy();
				onBoCopynew(row,ss);
//			}else if(result==5){
				//粘贴模板
//				pasteModel();
			}else if(result==5){
				//模板引用
				onBoMBYY(row,ss);
			}else{
				return;
			}

		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void onDelOrConcelYy(int row){
		DipDatachangeBVO bvo=(DipDatachangeBVO) getSelfUI().getBillCardPanel().getBillModel().getBodyValueRowVO(row, DipDatachangeBVO.class.getName());
		String pk=bvo.getPrimaryKey();
		try{
			CredenceHVO[] hvo=(CredenceHVO[]) HYPubBO_Client.queryByCondition(CredenceHVO.class, "pk_datadefinit_h='"+pk+"' and nvl(dr,0)=0");
			if(hvo!=null&&hvo.length>0){
			delete();
			}else{
				onBoConcelMBYY(row);
			}
		}catch (Exception e){
			e.printStackTrace();
			getSelfUI().showErrorMessage(e.getMessage());
		}
	}
	private void onBoConcelMBYY(int row){
		DipDatachangeBVO bvo=(DipDatachangeBVO) getSelfUI().getBillCardPanel().getBillModel().getBodyValueRowVO(row, DipDatachangeBVO.class.getName());
		String sql="update dip_datachange_b set Def_str_1=null where  pk_datachange_b='"+bvo.getPrimaryKey()+"'";
		IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
		try {
			iqf.exesql(sql);
			getSelfUI().getBillCardPanel().setBodyValueAt(null, row, "def_str_1");
			getSelfUI().getBillCardPanel().execBodyFormulas(row, getSelfUI().getBillCardPanel().getBodyItem("def_yy").getLoadFormula());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
/**
 * @author wyd
 * @desc 模板引用 就是引用那些已经定义模板的模板。本系统下的所有的
 * */
	private void onBoMBYY(int row,String refpk) {
		DipDatachangeBVO bvo=(DipDatachangeBVO) getSelfUI().getBillCardPanel().getBillModel().getBodyValueRowVO(row, DipDatachangeBVO.class.getName());
		String isdy=bvo.getTempexist();//(String) getSelfUI().getBillCardPanel().getBodyValueAt(row, "tempexist");
		if(isdy==null||!isdy.equals("已定义")){
//			DataRefDLG dg=new DataRefDLG(getSelfUI(),getSelectNode().getParentnodeID().toString(),"凭证模板引用",bvo.getDef_str_1());
//			dg.showModal();
			if(refpk!=null&&refpk.length()>0){
				VDipCrerefVO vo = null;
				try {
					vo = (VDipCrerefVO) HYPubBO_Client.queryByPrimaryKey(VDipCrerefVO.class, refpk);
				} catch (UifException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(vo==null){
					return;
				}
				String sql="update dip_datachange_b set tempexist=null,Def_str_1='"+vo.getPrimaryKey()+"' where pk_datachange_b='"+bvo.getPrimaryKey()+"' and (tempexist is null or tempexist<>'已定义')";
				String sql2="delete from dip_credence_b where dip_credence_b.pk_credence_h in (select ch.pk_credence_h from dip_credence_h ch where ch.pk_datadefinit_h='"+bvo.getPrimaryKey()+"')";
				String sql3="delete from dip_credence_h where pk_datadefinit_h='"+bvo.getPrimaryKey()+"'";
				IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
				try {
					iqf.exesql(sql);
					iqf.exesql(sql2);
					iqf.exesql(sql3);
					getSelfUI().getBillCardPanel().setBodyValueAt(vo.getPrimaryKey(), row, "def_str_1");
					getSelfUI().getBillCardPanel().setBodyValueAt(null, row, "tempexist");
					getSelfUI().getBillCardPanel().execBodyFormulas(row, getSelfUI().getBillCardPanel().getBodyItem("def_yy").getLoadFormula());
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				getSelfUI().showErrorMessage("请选择引用的模板！");
			}
		}else{
			getSelfUI().showErrorMessage("已经定义模板，请选择没有定义模板的公司做引用操作");
			return;
		}
	}
	/**
	 * 删除模板
	 * 日期：2011-7-12
	 * 作者：程莉
	 */
	public void delete(){
		VOTreeNode node=getSelectNode();
		if(node !=null){
			String pk=(String) node.getNodeID();
			if(pk !=null && !"".equals(pk) && pk.length()>0){
				try {
					DipDatachangeBVO[] ddbvo= (DipDatachangeBVO[]) HYPubBO_Client.queryByCondition(DipDatachangeBVO.class, "pk_datachange_h='"+pk+"' and isnull(dr,0)=0");
					if(ddbvo !=null){
						int row=this.getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
						if(row==-1){
							getSelfUI().showWarningMessage("请选择要操作的数据！");
							return;
						}
						String bpk=(String) this.getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(row, "pk_datachange_b");
						CredenceHVO[] hvo=(CredenceHVO[]) HYPubBO_Client.queryByCondition(CredenceHVO.class, "pk_datadefinit_h='"+bpk+"'");
						if(hvo !=null&&hvo.length>0){
							String hpk=hvo[0].getPk_credence_h();
							CredenceBVO[] bvo=(CredenceBVO[]) HYPubBO_Client.queryByCondition(CredenceBVO.class, "pk_credence_h='"+hpk+"'");
							HYPubBO_Client.deleteByWhereClause(CredenceHVO.class, " pk_datadefinit_h='"+bpk+"'");
							HYPubBO_Client.deleteByWhereClause(CredenceBVO.class, " pk_credence_h='"+hpk+"'");
							String update1="update dip_datachange_b set def_str_1=null where def_str_1='"+bpk+"'";
							String updatesql = "update dip_datachange_b set tempexist=null where nvl(dr,0)=0 and pk_datachange_b='"+bpk+"'";
							iq.exesql(updatesql);
							iq.exesql(update1);
							this.getSelfUI().getBillCardPanel().setBodyValueAt("", row, "tempexist");
						}else{
							String updatesql = "update dip_datachange_b set tempexist=null where nvl(dr,0)=0 and pk_datachange_b='"+bpk+"'";
							iq.exesql(updatesql);
							getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(null, row, "tempexist");
							getSelfUI().showWarningMessage("该模板还未定义!");
							return;
						}
						int rows=this.getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
						String[] ss=getBillCardPanelWrapper().getBillCardPanel().getBodyItem("def_yy").getLoadFormula();
						for(int i=0;i<rows;i++){
							Object obj=this.getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "def_str_1");
							if(obj!=null){
								String def=obj.toString();
								if(def!=null&&def.equals(bpk)){
									this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(null, i, "def_str_1");
									this.getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(i, ss);
								}
							}
						}
					}
				} catch (UifException e) {
					e.printStackTrace();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	String datachangepk;
	public void conversion(){
		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			this.getBillUI().showErrorMessage("请选择要操作的节点！");
			return;
		}
		DipDatachangeHVO hvo=(DipDatachangeHVO) node.getData();
		String fpk=hvo.getFpk();
		if(SJUtil.isNull(fpk)||fpk.length()<=0){

			getSelfUI().showErrorMessage("系统节点不可编辑！");
			return;
		}
		datachangepk=hvo.getPrimaryKey();
		new Thread() {
			@Override
			public void run() {
				BannerDialog dialog = new BannerDialog(getSelfUI());
				dialog.setTitle("正在转换，请稍候...");
				dialog.setStartText("正在转换，请稍候...");
							
				try {
					dialog.start();		
					ITaskExecute ite=(ITaskExecute) NCLocator.getInstance().lookup(ITaskExecute.class.getName());
					RetMessage rm=ite.doDataChangeTask(datachangepk);
					if(rm.getIssucc()){
						MessageDialog.showHintDlg(getSelfUI(), "数据转换", "转换成功！");
					}else{
						getSelfUI().showErrorMessage(rm.getMessage());
					}
				} catch (Throwable er) {
					er.printStackTrace();
				} finally {
					dialog.end();
				}
			}			
		}.start();
		
	}
	//凭证模板设置
	public void credence(){
		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			this.getBillUI().showErrorMessage("请选择要操作的节点！");
			return;
		}
		DipDatachangeHVO hvo=(DipDatachangeHVO) node.getData();
		String fpk=hvo.getFpk();
		if(SJUtil.isNull(fpk)||fpk.length()<=0){
			getSelfUI().showErrorMessage("系统节点不可操作！");
			return;
		}
		EffectDlg dlg=new EffectDlg(getSelfUI(),new UFBoolean(true),getSelectNode().getNodeID().toString(),false);
		dlg.flag=2;
		dlg.show();
		dlg.flag=0;

//		CdSbodyVO vo=(CdSbodyVO) node.getData();
//		String pk=vo.getPrimaryKey();
//		getSelfUI2().initdef(pk, 1);
	}
	//添加影响因素
	public void factor() throws BusinessException, SQLException, DbException{
//		HashMap map=new HashMap();
//		map.put("pk_datachange_h", getSelfUI().selectnode);
		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){

			getSelfUI().showErrorMessage("请选择要操作的节点！");
			return;
		}

		DipDatachangeHVO hvo=(DipDatachangeHVO) node.getData();
		String fpk=hvo.getFpk();
		if(SJUtil.isNull(fpk)||fpk.length()<=0){

			getSelfUI().showErrorMessage("系统节点不可编辑！");
			return;
		}
		/*int ret=getSelfUI().showOkCancelMessage("添加影响因素会影响凭证模板，是否继续？");
		if(ret!=1){
			return;
		}*/
		EffectDlg dlg=new EffectDlg(getSelfUI(),new UFBoolean(true),getSelectNode().getNodeID().toString(),true);
		dlg.flag=1;
		dlg.show();
		dlg.flag=0;
//		getSelfUI2().initdef(pk, 1);
//		DipDatachangeHVO change=(DipDatachangeHVO) HYPubBO_Client.queryByPrimaryKey(DipDatachangeHVO.class, getSelfUI().selectnode);
//		IQueryField query=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
//		String sql="";
//		sql="select";
//		StringBuffer buffer=new StringBuffer();
//		if(!"".equals(buffer.toString())){
//		sql = sql+""+buffer.toString().subSequence(0, buffer.toString().length()-1)+" from dip_effectdef "+" where 1=1 "+dlg.getReturnSql();
//		List list=query.queryfieldList(sql);
//		String []str=buffer.toString().split(",");
//		getSelfUI().getBillCardPanel().getBillModel().clearBodyData();
//		if(list!=null&&list.size()>0){
//		for(int i=0;i<list.size();i++){
//		HashMap hmap=(HashMap) list.get(i);

//		getSelfUI().getBillCardPanel().getBillModel().addLine();
//		for(int j = 0;j<str.length;j++){
//		getSelfUI().getBillCardPanel().getBillModel().setValueAt(hmap.get(str[j].toUpperCase()), i, ""+String.valueOf(j+1));
//		}
//		}
//		}
//		}
//		getSelfUI().getBillCardPanel().setBillData(getSelfUI().getBillCardPanel().getBillData());
		//getBillCardPanelWrapper().getBillCardPanel().getHeadItem("effectname").setEnabled(true);
//		ui.init(billvo, isadd);
//		myui.getBillCardPanel().getHeadItem("effectfiled").setEdit(true);
	}
	//预警弹出框
	public void yuJing() throws UifException{
		//获取选中的节点
		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			this.getSelfUI().showWarningMessage("请选择要操作的节点！");
			return;
		}
		//得到当前VO类
		DipDatachangeHVO dvo=(DipDatachangeHVO) node.getData();
		//得到主键值
		String fpk=dvo.getFpk();
		String pk=dvo.getPk_datachange_h();
		dvo=(DipDatachangeHVO) HYPubBO_Client.queryByPrimaryKey(DipDatachangeHVO.class, pk);
		if(SJUtil.isNull(dvo)){
			this.getSelfUI().showWarningMessage("此节点还没有保存，请编辑");
			return;
		}
		//当前单据的预警业务类型
		String type=dvo.getTasktype();
		if(SJUtil.isNull(type)|| type.length()==0){
			this.getSelfUI().showWarningMessage("请选择预警业务类型！");
			return;
		}
		/*//弹出预警窗口
		ToftPanel toft=SFClientUtil.showNode("H4H2H5",IFuncWindow.WINDOW_TYPE_DLG);
		//得到预警的ClientUI
		WarningSetClientUI ui=(WarningSetClientUI) toft;*/
		
		
		
		//得到主键值
		String pk1=dvo.getPk_datachange_h();
		//得到MyBillVO(warningset)
		MyBillVO bill=new MyBillVO();
		DipWarningsetVO [] vo=(DipWarningsetVO[]) HYPubBO_Client.queryByCondition(DipWarningsetVO.class, "tasktype='"+type+"' and pk_bustab='"+pk+"'");
		DipWarningsetVO dwvo=null;
		boolean isadd=false;
		if(SJUtil.isNull(vo)||vo.length==0){
			dwvo=new DipWarningsetVO();
			dwvo.setTasktype(type);
			dwvo.setPk_bustab(pk);

			dwvo.setPk_sys(fpk);
			isadd=true;
		}
		else{
			//如果vo不是空的
			dwvo=vo[0];
			DipWarningsetBVO[]bvos=(DipWarningsetBVO[]) HYPubBO_Client.queryByCondition(DipWarningsetBVO.class,"pk_warningset='"+dwvo.getPk_warningset()+"'");
			bill.setChildrenVO(bvos);
			isadd=false;
		}
		dwvo.setWcode(dvo.getCode());
		dwvo.setWname(dvo.getName());
		bill.setParentVO(dwvo);

		WarningsetDlg wd=new WarningsetDlg(getSelfUI(),bill, isadd,dvo.getPk_xt(),3);		
		wd.showModal();		
//		new WarningSetClientUI().init(bill, isadd,node.getParentnodeID().toString(),3);
	}
	//数据模板弹出框
	public void onBoModel() throws UifException{
		BillData bd=getBillCardPanelWrapper().getBillCardPanel().getBillData();
		if(bd!=null){
			try {
				bd.dataNotNullValidate();
			} catch (ValidationException e) {
				e.printStackTrace();
			}
		}
		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			this.getSelfUI().showWarningMessage("请选择要操作的节点！");
			return;
		}
		//得到当前VO类
		DipDatachangeHVO hvo=(DipDatachangeHVO) node.getData();
		//得到主键值
//		String pk=hvo.getPk_datachange_h();
		String hpk=hvo.getFpk();
		if(SJUtil.isNull(hpk)||hpk.length()<=0){
			getSelfUI().showErrorMessage("系统节点不能编辑！");
			return;
		}
		int row=getSelfUI().getBillCardPanel().getBillTable().getSelectedRow();
		if(row<0){
			getSelfUI().showErrorMessage("请选择需要操作的定义！");
			return ;
		}
		DipDatachangeBVO[] vos = null;
		try {
			vos = (DipDatachangeBVO[]) getBillTreeCardUI().getVOFromUI().getChildrenVO();
		} catch (Exception e) {
			e.printStackTrace();
		}
		DipDatachangeBVO bvo=vos[row];
		String pkbvo=bvo.getPrimaryKey();
		if(pkbvo==null||pkbvo.length()<=0){
			getSelfUI().showErrorMessage("您选择模板定义还没有保存！请先保存！");
			return;
		}
		if(bvo.getDef_str_1()!=null&&bvo.getDef_str_1().length()==20){
			pkbvo=bvo.getDef_str_1();
		}
		nc.ui.dip.credence.CredenceDlg dlg = new nc.ui.dip.credence.CredenceDlg(getSelfUI(),new UFBoolean(true),pkbvo,hvo.getBusidata());
		dlg.show();
		//得到当前所选的节点

//		//弹出数据校验窗口
//		ToftPanel toft=SFClientUtil.showNode("H4H1H0", IFuncWindow.WINDOW_TYPE_DLG);
//		DataVerifyClientUI ui=(DataVerifyClientUI) toft;
//		nc.vo.jyprj.dataverify.MyBillVO bill=new nc.vo.jyprj.dataverify.MyBillVO();
//		DataverifyHVO []svo=(DataverifyHVO[]) HYPubBO_Client.queryByCondition(DipWarningsetVO.class, " pk_dataverify_h='"+pk+"'");
//		DataverifyHVO svoo=null;
//		boolean isadd=false;
//		if(SJUtil.isNull(svo)){
//		svoo=new DataverifyHVO();
////		svoo.setVector(newVector);
////		svoo.setPk_sys(hvo.getPk_datadefinit_h());
////		svoo.setTasktype(type);
//		isadd=true;
//		}
//		else{
//		svoo=svo[0];
//		DataverifyBVO [] bvos=(DataverifyBVO[]) HYPubBO_Client.queryByCondition(DipWarningsetBVO.class, " pk_dataverify_h='"+svoo.getPk_dataverify_h()+"'" );
//		bill.setChildrenVO(bvos);
//		isadd=false;
//		}
//		bill.setParentVO(svoo);
//		ui.init(bill, isadd);
		try {
			this.getBufferData().refresh();
			System.out.println("*********************");
			/*
			 * 多选nc组织编码（nczz）存值问题:避免凭证模板界面关闭后，nc组织编码无值
			 * 2011-6-29 
			 * 1136--1157行
			 */
			String orgName=null;//多个名称连接的字符串
			String[] arrayName=null;//名称数组
			String sql="";
			String unitcode=null;
			String nczz=null;
			if(bvo !=null){
				for(int j=0;j<vos.length;j++){
					orgName=vos[j].getOrgname();
					arrayName=orgName.split(",");
					for(int k=0;k<arrayName.length;k++){
						if(k==0){
							nczz=arrayName[k];
							sql="select unitcode from bd_corp where unitname='"+nczz+"' and nvl(dr,0)=0";
							unitcode=iq.queryfield(sql);
						}else{
							nczz=arrayName[k];
							sql="select unitcode from bd_corp where unitname='"+nczz+"' and nvl(dr,0)=0";
							unitcode=unitcode+","+iq.queryfield(sql);
						}
					}
					this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(unitcode, j, "nczz");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//弹出数据校验窗口
	public void dataCheck () {
		HashMap map = new HashMap();
		map.put("pk", getSelfUI().selectnode);
		map.put("type", SJUtil.getYWnameByLX(IContrastUtil.YWLX_ZH));
		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			getSelfUI().showWarningMessage("请选择节点！");
			return;
		}
		DipDatachangeHVO hvo=(DipDatachangeHVO) node.getData();
		if(hvo!=null){
			String fpk=hvo.getFpk();
			if(fpk==null||fpk.length()<=0){
				getSelfUI().showWarningMessage("系统节点不可以编辑！");
				return;
			}
		}
		/*将当前页面的表头code和name放到map中，用于当弹出数据校验窗口时，编码和名称为空，自动填充
		 * 2011-06-14
		 * zlc*/
		map.put("code", hvo.getCode());
		map.put("name", hvo.getName());
		DatarecDlg dlg = new DatarecDlg(getSelfUI(),new UFBoolean(true),map);
		dlg.show();

		/*将本页面的表头编码名称，传到数据校验的编码和名称上
		 * 2011-06-14
		 * */
		map.put("code", hvo.getCode());
		map.put("name", hvo.getName());
	}

	IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
//		ContDataClientUI ui = (ContDataClientUI) getBillUI();
//		String selectnode = ui.selectnode;



		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("请选择要增加的系统节点！");
			return ;
		}
		String ss=(String) tempNode.getParentnodeID();
		DipDatachangeHVO hvo=(DipDatachangeHVO) tempNode.getData();
		if(!(hvo!=null&hvo.getIsfolder()!=null&&hvo.getIsfolder().booleanValue())){
			getSelfUI().showErrorMessage("不是能做增加操作！");
			return ;
		}
		
//		if(ss!=null&&ss.length()>0){
//			getSelfUI().showErrorMessage("不是系统节点不能做增加操作！");
//			return ;
//		}else{
			//2011-5-23 程莉 判断是否是NC系统:如果是，则不能做任何的操作，只能浏览 begin
			String ncsql="select code from dip_sysregister_h h where h.pk_sysregister_h='"+hvo.getPk_xt()+"' and nvl(h.dr,0)=0";
			try {
				String code=iq.queryfield(ncsql);
				if("0000".equals(code)){
					getSelfUI().showErrorMessage("NC系统不能作增加操作！");
					return;
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			} catch (DbException e1) {
				e1.printStackTrace();
			}
			/* end */
//		}
		super.onBoAdd(bo);
		getSelfUI().getBillCardPanel().setHeadItem("fpk", tempNode.getNodeID());
		getSelfUI().getBillCardPanel().setHeadItem("isfolder", new UFBoolean(false));
		getSelfUI().getBillCardPanel().setHeadItem("pk_xt", hvo.getPk_xt());
		

//		DipDatachangeHVO hvo=(DipDatachangeHVO) tempNode.getData();
		String fpk=(String) tempNode.getNodeID();
		String pk=null;
		if(hvo.getPk_datachange_h()!=null){
			pk=hvo.getPk_datachange_h();
		}

		UIRefPane uir=(UIRefPane)this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("busidata").getComponent();
//		DataDefinitRefModel model2=new DataDefinitRefModel();
//		model2.addWherePart(" and pk_xt='"+hvo.getPk_xt()+"' and dip_datadefinit_h.iscreatetab='Y' and nvl(dip_datadefinit_h.dr,0)=0 and nvl(dip_datadefinit_h.isfolder,'N')='N' ");
//		uir.setRefModel(model2);
		/*liyunzhe modify 业务表修改成树形参照 2012-06-04 strat*/
		DataDefinitTableTreeRefModel model=new DataDefinitTableTreeRefModel();
		model.setClassWherePart(model.getClassWherePart() +" and pk_xt='"+hvo.getPk_xt()+"'");
		model.addWherePart(" and tabsoucetype='自定义'");
		uir.setRefModel(model);
		/*liyunzhe modify 业务表修改成树形参照 2012-06-04 end*/
//		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("NCorg").setValue("公司主键");
		getBillCardPanelWrapper().getBillCardPanel().execHeadFormulas(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("fpk").getEditFormulas());
		getSelfUI().getBillCardPanel().setHeadItem("systype", hvo.getSystype());
		onBoLineAdd();
	}
	@Override
	protected void onBoRefresh() throws Exception {
		// TODO Auto-generated method stub
		super.onBoRefresh();
		getButtonManager().getButton(IBillButton.Add).setEnabled(false);
		getSelfUI().updateButtonUI();
	}

    
	public void onBoEditFolder() throws Exception {
		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("请选择要修改的节点！");
			return ;
		}
		String str=(String)tempNode.getParentnodeID();
		if(str ==null || str.equals("")){
			getSelfUI().showWarningMessage("系统节点不能做修改操作！"); 
			return;
		}
		DipDatachangeHVO vo =(DipDatachangeHVO) tempNode.getData();
		if(vo.getIsfolder().booleanValue()){
			DipDatachangeHVO[] listvos=(DipDatachangeHVO[]) HYPubBO_Client.queryByCondition(DipDatachangeHVO.class, "pk_xt='"+vo.getPk_xt()+"' and isfolder='Y' and pk_datachange_h<>'"+vo.getPrimaryKey()+"' and nvl(dr,0)=0");
			List<String> listcode=new ArrayList<String>();
			List<String> listname=new ArrayList<String>();
			if(listvos!=null&&listvos.length>0){
				for(int i=0;i<listvos.length;i++){
					listcode.add(listvos[i].getCode());
					listname.add(listvos[i].getName());
				}
			}
				
			AddFolderDlg adlg=new AddFolderDlg(getBillUI(),listcode,listname,vo.getCode(),vo.getName());
			adlg.showModal();
			if(adlg.isOk()){
				String tempc=adlg.getCode();
				String tempn=adlg.getName();
				if(!tempc.equals(vo.getCode())||!tempn.equals(vo.getName())){
					vo.setCode(tempc);
					vo.setName(tempn);
					HYPubBO_Client.update(vo);
					vo=(DipDatachangeHVO) HYPubBO_Client.queryByPrimaryKey(DipDatachangeHVO.class, vo.getPrimaryKey());
					if (getUITreeCardController().isAutoManageTree()) {	
						getSelfUI().insertNodeToTree(vo);
						getBillTreeCardUI().updateUI();
						getBillCardPanelWrapper().getBillCardPanel().setHeadItem("code", tempc);
						getBillCardPanelWrapper().getBillCardPanel().setHeadItem("name", tempn);
					}
				}
			}
			return;
		}
	}
//
	public void onBoDeleteFolder() throws Exception {
		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("请选择要修改的节点！");
			return ;
		}
		String str=(String)tempNode.getParentnodeID();
		if(str ==null || str.equals("")){
			getSelfUI().showWarningMessage("系统节点不能做修改操作！"); 
			return;
		}
		VOTreeNode node1=(VOTreeNode) tempNode.getParent();

		DipDatachangeHVO vo =(DipDatachangeHVO) tempNode.getData();
		if(vo.getIsfolder().booleanValue()){
			SuperVO[] hvos=HYPubBO_Client.queryByCondition(DipDatachangeHVO.class, "fpk='"+vo.getPrimaryKey()+"'");
			if(hvos!=null&&hvos.length>0){
				throw new Exception("文件夹下已经有数据定义，不能删除！");
			}else{
				HYPubBO_Client.delete(vo);
				getBillTreeCardUI().getBillTreeData().deleteNodeFromTree(this.getBillTreeCardUI().getBillTreeSelectNode());
				if(node1!=null){
					getSelfUI().getBillTree().setSelectionPath(new javax.swing.tree.TreePath(node1
										.getPath()));
					}
			}
		}
	}
	public void onBoAddFolder() throws Exception {
		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null||tempNode.getData()==null){
			getSelfUI().showErrorMessage("请选择要增加的节点！");
			return ;
		}
		DipDatachangeHVO hvo=(DipDatachangeHVO) tempNode.getData();
		if(!hvo.getIsfolder().booleanValue()){
			getSelfUI().showErrorMessage("不是文件夹不能做增加文件夹操作！");
			return ;
		}
		DipDatachangeHVO newhvo=new DipDatachangeHVO();
		newhvo.setFpk(hvo.getPrimaryKey());
		newhvo.setPk_xt(hvo.getPk_xt());
		newhvo.setIsfolder(new UFBoolean(true));
		newhvo.setSystype(hvo.getName());
		DipDatachangeHVO[] listvos=(DipDatachangeHVO[]) HYPubBO_Client.queryByCondition(DipDatachangeHVO.class, "pk_xt='"+hvo.getPk_xt()+"' and isfolder='Y' and nvl(dr,0)=0");
		List<String> listcode=new ArrayList<String>();
		List<String> listname=new ArrayList<String>();
		if(listvos!=null&&listvos.length>0){
			for(int i=0;i<listvos.length;i++){
				listcode.add(listvos[i].getCode());
				listname.add(listvos[i].getName());
			}
		}
			
		AddFolderDlg addlg=new AddFolderDlg(getBillUI(),listcode,listname,null,null);
		addlg.showModal();
		if(addlg.isOk()){
			newhvo.setCode(addlg.getCode());
			newhvo.setName(addlg.getName());
			String pk = null;
			try {
				pk = HYPubBO_Client.insert(newhvo);
			} catch (UifException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				newhvo=(DipDatachangeHVO) HYPubBO_Client.queryByPrimaryKey(DipDatachangeHVO.class, pk);
				
				if (getUITreeCardController().isAutoManageTree()) {	
//					MyBillVO mvo=new MyBillVO();
//					mvo.setParentVO(newhvo);
					getSelfUI().insertNodeToTree(newhvo);
				}
			} catch (UifException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}