package nc.ui.dip.datadefinit;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.dip.pub.IQueryField;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.ddc.IBizObjStorage;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.dip.dlg.AskDLG;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.util.NCOptionPane;
import nc.ui.pub.ddc.datadict.DDCBO_Client;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.ui.trade.treecard.TreeCardEventHandler;
import nc.util.dip.sj.IContrastUtil;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.dip.sysregister.DipSysregisterHVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.core.BizObject;
import nc.vo.pub.core.FolderNode;
import nc.vo.pub.core.FolderObject;
import nc.vo.pub.core.ObjectNode;
import nc.vo.pub.ddc.datadict.DDCData;
import nc.vo.pub.ddc.datadict.Datadict;
import nc.vo.trade.pub.IExAggVO;
/**
 * 
 * 该类是一个抽象类，主要目的是生成按钮事件处理的框架
 * 
 * @author author
 * @version tempProject version
 */

public class AbstractMyEventHandler extends TreeCardEventHandler {	

	IUAPQueryBS queryBS=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());

	public AbstractMyEventHandler(BillTreeCardUI billUI, ICardController control) {
		super(billUI, control);
	}

	/**
	 * 取得当前UI类
	 * 
	 * @return
	 */
	private DataDefinitClientUI getSelfUI() {
		return (DataDefinitClientUI) getBillUI();
	}


	@SuppressWarnings("unused")
	private CircularlyAccessibleValueObject[] getChildVO(
			AggregatedValueObject retVo) {
		CircularlyAccessibleValueObject childVos[] = null;
		if (retVo instanceof IExAggVO)
			childVos = ((IExAggVO) retVo).getAllChildrenVO();
		else
			childVos = retVo.getChildrenVO();
		return childVos;
	}

	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		switch (intBtn) {
		case IBtnDefine.CreateTable:
			creatTable();
			break;
		case IBtnDefine.addFolderBtn:
			onBoAddFolder();
			break;
		case IBtnDefine.editFolderBtn:
			onBoEditFolder();
			break;
		case IBtnDefine.delFolderBtn:
			onBoDeleteFolder();
			break;
		case IBtnDefine.moveFolderBtn:
			onBoMoveFolder();
			break;
		case IBtnDefine.MBZH:
			onBoMBZH();
			break;
		}
	}



	public void onBoMBZH() throws Exception{
		// TODO Auto-generated method stub
		
	}

	public void onBoMoveFolder() throws Exception{
		// TODO Auto-generated method stub
		
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
	public void creatTable() {
		Integer flags=0;//删除标志：1代表点击了确定按钮
		// 获取节点
		DataDefinitClientUI ui = (DataDefinitClientUI) this.getBillUI();
		String pk_node = ui.selectnode;
		if ("".equals(pk_node)) {
			NCOptionPane.showMessageDialog(this.getBillUI(), "请选择一个节点。");
			return;
		} else {
//			修改时，将"创建表"按钮设置为不可用
			getSelfUI().getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(false);
			DipDatadefinitHVO hvo=(DipDatadefinitHVO) ui.getBillTreeSelectNode().getData();
			String fpkstr=hvo.getPk_xt();
			if(fpkstr!=null && fpkstr.equals("0001AA1000000001XQ1B")){
				getSelfUI().showWarningMessage("nc系统节点不能建表！"); 
				return;
			}
			String userdefined = hvo.getUserdefined();
			if("是".equals(userdefined)){
				getSelfUI().showWarningMessage("自定义物理表/视图不能建表！"); 
				return;
			}
			String pk=(String) ui.getBillTreeSelectNode().getNodeID();//主键
			// 根据字段名获得表头的表名对应值
			String tableName=(String)this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("memorytable").getValueObject();
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
						AskDLG adlg=new AskDLG(getSelfUI(),"提示","系统已经建表！有数据！确认是否删除?",new String[]{"删除结构后重建表结构","转换更新表结构操作"});
						adlg.showModal();
						if(adlg.getRes()==0){
							//删除该表,重新创建
							StringBuffer delSql=new StringBuffer();
							delSql.append("drop table ");
							delSql.append(tableName);
							iq.exesql(delSql.toString());

							//2011-7-13 删除NC数据字典的表
							String delNC="delete from pub_datadict where id='"+tableName+"' and nvl(dr,0)=0";
							iq.exesql(delNC);
						}else if(adlg.getRes()==1) {
							//转换更新表结构，只允许增加字段，例如已经建表现在是4个字段，现在增加了一个字段，可以更改，其他的都不支持。
							String sql="select co.column_name from user_tab_cols co where co.table_name='"+tableName.toUpperCase()+"'";
							List list=iq.queryfieldList(sql);//
							List<DipDatadefinitBVO> add=new ArrayList<DipDatadefinitBVO>();
							Map<String,String> map=new HashMap<String,String>();
							if(list!=null){
								for(int i=0;i<list.size();i++){
									String ss=(String)list.get(i);
									if(ss!=null&&!"".equals(ss)){
										ss=ss.toLowerCase();
//										if(!("ts".equals(ss)||"dr".equals(ss))){
											
												map.put(ss.toLowerCase(), ss.toLowerCase());
										    
												
//										}
									}
									
									
								}	
							}
							//String sql1="";
							DipDatadefinitBVO []defbvo=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class,"pk_datadefinit_h='"+pk+"' and  nvl(dr,0)=0");	
							if(defbvo!=null){
								for(int i=0;i<defbvo.length;i++){
									String abc=defbvo[i].getEname();
									
									if(abc!=null&&map.get(abc.toLowerCase())==null){
										add.add(defbvo[i]);
									}
								}
							}
							List<String> alterAddSql=new ArrayList<String>();
							if(add!=null&&add.size()>0){
								String alterSql="alter table "+tableName+" add (";
								for(int i=0;i<add.size();i++){
									DipDatadefinitBVO dipdefbvo=add.get(i);
									String s1=dipdefbvo.getEname();
									String s2=dipdefbvo.getType();
									Integer s3=dipdefbvo.getLength();
									Integer s4=dipdefbvo.getDeciplace();
									StringBuffer sb=new StringBuffer();
									sb.append(s1+" ");
									sb.append(s2);
									if(s3!=null&&s3.intValue()>0){
										sb.append("("+s3.intValue());
										if(s4!=null&&s4.intValue()>0){
											sb.append(","+s4.intValue()+") )");
										}else{
											sb.append(") )");
										}
									}else{
										sb.append(")");
									}
									String s5=alterSql+sb.toString();
									alterAddSql.add(s5);
								}
								
								boolean ff=iq.exectEverySql(alterAddSql);
								if(ff==false){//执行sql语句成功。
									createDatadict(tableName, fpkstr, pk);
									getSelfUI().showWarningMessage("修改成功！");
									return;
								}
								
							}else{
								getSelfUI().showErrorMessage("表字段没有更改！");
								return;
							}
							
							
							
						}else{
							//点击取消
							super.onBoCancel();
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

							//2011-7-13 删除NC数据字典的表
							String delNC="delete from pub_datadict where id='"+tableName+"' and nvl(dr,0)=0";
							iq.exesql(delNC);
						}else{
							//点击取消
							super.onBoCancel();
							return;
						}
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			StringBuffer str = new StringBuffer();

			str.append("create table " + tableName);
			str.append("\n");// 换行
			str.append("(");
			str.append("\n");
			if(row>=1){
				Map tsdrMap=new HashMap();
				for (int i = 0; i < row; i++) {
					cname = (String) this.getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel("dip_datadefinit_b")
					.getValueAt(i, "cname");//中文名称		

					ename = (String) this.getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel("dip_datadefinit_b")
					.getValueAt(i, "ename");//英文名称
					if(ename==null||ename.length()<=0){
						getBillUI().showErrorMessage("英文名称不能为空！");
						return;
					}
					
					if(ename.toLowerCase().equals("ts")||ename.toLowerCase().equals("dr")){
						tsdrMap.put(ename.toLowerCase(), ename.toLowerCase());	
					}
					
					
					type = (String) this.getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_datadefinit_b").getValueAt(i, "type");//数据类型
					if(type==null||type.length()<=0){
						getBillUI().showErrorMessage("数据类型不能为空！");
						return;
					}
					boolean ispkcan=getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_datadefinit_b").getValueAt(i, "ispk")==null?false:(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_datadefinit_b").getValueAt(i, "ispk");
					boolean issyspkcan=getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_datadefinit_b").getValueAt(i, "issyspk")==null?false:(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_datadefinit_b").getValueAt(i, "issyspk");
					Object lengthcan=this.getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_datadefinit_b").getValueAt(i, "length");
					Object deciplaceobj=getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_datadefinit_b").getValueAt(i,"deciplace");
					boolean isimportobj=getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_datadefinit_b").getValueAt(i,"isimport")==null?false:(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_datadefinit_b").getValueAt(i,"isimport");
					Object defaultvalue = getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_datadefinit_b").getValueAt(i,"defaultvalue");
					if(null != defaultvalue && !"".equals(defaultvalue)){
						defaultvalue = defaultvalue.toString().replaceAll("\"", "\'");
					}
					
					str.append(ename.toLowerCase());//英文名称转换为小写
					str.append("\t");
					
					if(ispkcan||issyspkcan){
						pkcolname=ename;
					}
					//Char,Date,Decimal,Integer,Varchar
					if(type.equals("CHAR")){
						//得到长度
						/*
						 * 增加给类型的默认长度 char和 除Integer，number、data型，如果没设长度，默认length为100
						 * number、，若没设置length 默认为20，deciplace 默认为8
						 * 2011-5-16
						 * zlc*/
						if(lengthcan!=null){
							length = (Integer) lengthcan;
						}
						else{
							this.getBillCardPanelWrapper()
							.getBillCardPanel().getBillModel("dip_datadefinit_b")
							.setValueAt(100, i, "length");
						}
						str.append(type);

						//判断是否是主键、系统预置主键
						if (ispkcan||issyspkcan||isimportobj) {
							str.append("(");
							str.append(length);
							str.append(")");
							if(null != defaultvalue && !"".equals(defaultvalue)){
								str.append(" default ").append(defaultvalue).append(" not null,");
							}else{
								str.append(" not null,");
							}
							str.append("\n");
						} else {
							ispk = false;
							str.append("(");
							str.append(length);
							str.append(")");
							if(null != defaultvalue && !"".equals(defaultvalue)){
								str.append(" default ").append(defaultvalue).append(",");
							}else{
								str.append(",");
							}
							str.append("\n");
						}
					}else if(type.equals("DATE")){						
						//判断是否是主键、系统预置主键
						if (ispkcan||issyspkcan||isimportobj) {
							str.append("VARCHAR(19)");
							if(null != defaultvalue && !"".equals(defaultvalue)){
								str.append(" default ").append(defaultvalue).append(" not null,");
							}else{
								str.append(" not null,");
							}
						    str.append("\n");
						} else {
							ispk = false;
							str.append("VARCHAR(19)");
							if(null != defaultvalue && !"".equals(defaultvalue)){
								str.append(" default ").append(defaultvalue).append(",");
							}else{
								str.append(",");
							}
							str.append("\n");
					    }

					}else if(type.equals("NUMBER")){
						//长度
						if(lengthcan!=null){
							length = (Integer) lengthcan;
						}else{
							this.getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_datadefinit_b").setValueAt(20, i, "length");
						}
						if(deciplaceobj!=null){
							deciplace = (Integer) deciplaceobj;
						}else{
							this.getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_datadefinit_b").setValueAt(8, i, "deciplace");
						}
						str.append(type);
						//判断是否是主键、系统预置主键
						if (ispkcan||issyspkcan||isimportobj) {
							str.append("(");
							str.append(length);
							str.append(",");
							str.append(deciplace);
							str.append(")");
							if(null != defaultvalue && !"".equals(defaultvalue)){
								str.append(" default ").append(defaultvalue).append(" not null,");
							}else{
								str.append(" not null,");
							}
							str.append("\n");
						} else{
							ispk = false;
							str.append("(");
							str.append(length);
							str.append(",");
							str.append(deciplace);
							str.append(")");
							if(null != defaultvalue && !"".equals(defaultvalue)){
								str.append(" default ").append(defaultvalue).append(",");
							}else{
								str.append(",");
							}
							str.append("\n");
						}

					}else if(type.equals("INTEGER")||type.equals("LONG")||type.equals("BLOB")){
						str.append(type);
						//判断是否是主键、系统预置主键
						if (ispkcan||issyspkcan||isimportobj) {
							if(null != defaultvalue && !"".equals(defaultvalue)){
								str.append(" default ").append(defaultvalue).append(" not null,");
							}else{
								str.append(" not null,");
							}
							str.append("\n");
						} else{
							ispk = false;
							if(null != defaultvalue && !"".equals(defaultvalue)){
								str.append(" default ").append(defaultvalue).append(",");
							}else{
								str.append(",");
							}
							str.append("\n");
						}

					}else{
						//varchar型:长度
						
						if(lengthcan!=null){
							length = (Integer) lengthcan;
						}
						else{
							this.getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_datadefinit_b").setValueAt(100, i, "length");							
						}
						str.append(type);
						//判断是否是主键、系统预置主键
						if (ispkcan||issyspkcan||isimportobj) {
							str.append("(");
							str.append(length);
							str.append(")");
							if(null != defaultvalue && !"".equals(defaultvalue)){
								str.append(" default ").append(defaultvalue).append(" not null,");
							}else{
								str.append(" not null,");
							}
							str.append("\n");
						} else{
							ispk = false;
							str.append("(");
							str.append(length);
							str.append(")");
							if(null != defaultvalue && !"".equals(defaultvalue)){
								str.append(" default ").append(defaultvalue).append(",");
							}else{
								str.append(",");
							}
							str.append("\n");
						}
					}					
				}	 
				boolean flag=false;
				if(tsdrMap.get("ts")==null&&tsdrMap.get("dr")==null){//ts和dr都没有
					str.append("ts");
					str.append("\t");
					str.append("char(19),");
					str.append("\n");
						str.append("dr");
						str.append("\t");
						str.append("smallint");
						flag=true;
					flag=true;
				}else if(tsdrMap.get("ts")!=null&&tsdrMap.get("dr")!=null){////ts和dr都有
						String sb=str.substring(0, str.length()-2);
						str=new StringBuffer();
						str.append(sb);
				}else{//ts和dr有一个没有
					if(tsdrMap.get("ts")==null){
						
						str.append("ts");
						str.append("\t");
						str.append("char(19)");	
					}
					if(tsdrMap.get("dr")==null){
						str.append("dr");
						str.append("\t");
						str.append("smallint");
					}
					
				}
				if(pkcolname==null||pkcolname.equals("null")||pkcolname.length()<=0){}else{
					str.append(",");
					str.append("\n");
					str.append("constraint pk_" + tableName + " primary key");
					str.append(" (");
					str.append(pkcolname);
					str.append(")");
					str.append("\n");
				}
				str.append(");");
				//在数据库中创建表
//				IQueryField iqd=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
				boolean isscuess=iq.exectEverySql(str.toString());
				if(isscuess==false){
					//更新是否已建表标志  858--885行
					String sql="update dip_datadefinit_h set iscreatetab='Y' where pk_sysregister_h='"+getSelfUI().getBillCardPanel().getHeadItem("pk_sysregister_h").getValueObject().toString()+"' and pk_datadefinit_h='"+getSelfUI().selectnode+"' and nvl(dr,0)=0";
					try {
						iq.exesql(sql);
						this.getBufferData().refresh();
						DipDatadefinitBVO[] bvos=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, "pk_datadefinit_h='"+getSelfUI().selectnode+"'");
						AggregatedValueObject billVO = getBillTreeCardUI().getChangedVOFromUI();
						billVO.setChildrenVO(bvos);
						getBufferData().setCurrentVO(billVO);
						int nCurrentRow=-1;
						nCurrentRow=getBufferData().getCurrentRow();
						onTreeSelected(getSelfUI().getBillTreeSelectNode());
						if (nCurrentRow >= 0){
							getBufferData().setCurrentRow(nCurrentRow);
							getBillCardPanelWrapper().getBillCardPanel().getHeadItem("deploycode").setEdit(false);
						}
					} catch (BusinessException e2) {
						e2.printStackTrace();
					} catch (SQLException e2) {
						e2.printStackTrace();
					} catch (DbException e2) {
						e2.printStackTrace();
					}catch (Exception e2) {
						e2.printStackTrace();
					}
					MessageDialog.showHintDlg(getSelfUI(), "提示", "成功创建表！");
					createDatadict( tableName, fpkstr, pk);
//					try {
//						//写入NC数据字典
//						DipSysregisterHVO dshvo=(DipSysregisterHVO) HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, fpkstr);
//						String code=dshvo.getCode();
//						String sysname=dshvo.getExtname();
//						createForder(sysname, code, "jkpt");
//
//						Datadict m_dict = new Datadict();
//						DDCData data = null;
//						ObjectNode[] objNodes = null;
//						BizObject[] objs = null;
//						data=DDCBO_Client.fromDBMetaData(null,new String[]{tableName} , 1, code);
//						objNodes = (ObjectNode[]) data.getNode().toArray(new ObjectNode[0]);
//						objs = (BizObject[]) data.getTable().toArray(new BizObject[0]);
//						m_dict.saveObjectNode(objNodes, objs, 20);
//
//						
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
				}else{
					MessageDialog.showHintDlg(getSelfUI(), "提示", "创建表失败！");
					return;
				}
			}
		}

	}

	/**
	 * 作者：程莉
	 * 判断系统里面是否有正准备创建的此表
	 * 在点击创建表按钮时判断：
	 * 1.如果有此表，查询表里是否有数据，如果有数据，给予提示，没有，则直接删掉，重新创建
	 * 2.没有此表，则直接创建
	 */
	public boolean isTableExist(String tableName){
		boolean isExist=false;//默认没有此表
		if(tableName.toLowerCase().startsWith("v_dip")){
			isExist=true;
		}else {
			String sql="select 1 from user_tables where table_name='"+tableName.toUpperCase()+"';";	
			try{
				ArrayList al=(ArrayList)queryBS.executeQuery(sql, new MapListProcessor());
				if(al.size()>=1){
					isExist=true;//有此表			
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return isExist;
	}
	/**
	 * @desc 创建数据字典里边文件夹的方法
	 * @param String displayname 文件夹名称
	 * @param id 文件夹的id
	 * @param parentID 父节点的ID
	 * @author wyd
	 * @time 2011-07-12
	 * */
	public void createForder(String displayname,String id,String parentID){
		String sql="select id from pub_datadict where id='"+id+"'";
		String res=null;
		try {
			res=iq.queryfield(sql);
			if(res!=null&&res.equals(id)){
				return;
			}
			ObjectNode node=new FolderNode();
			FolderObject fo=new FolderObject();
			fo.setNode(node);
			fo.setDisplayName(displayname);
			fo.setGUID(id);
			fo.setID(id);
			fo.setKind("Folder");
			fo.setParentGUID(parentID);

			IBizObjStorage storage = (IBizObjStorage) NCLocator.getInstance().lookup(
					IBizObjStorage.class.getName());

			storage.saveObject(IContrastUtil.DESIGN_CON, "nc.bs.pub.ddc.datadict.DatadictStorage", node, fo);
		} catch (BusinessException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (DbException e1) {
			e1.printStackTrace();
		}catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessRuntimeException(e.getMessage());
		}

	}
	
	
	public void createDatadict(String tableName,String fpkstr,String pk){
		try {
			DataDefinitClientUI ui = (DataDefinitClientUI) this.getBillUI();
			String pk_node = ui.selectnode;
//			tableName=pk_node;
			//写入NC数据字典
			DipSysregisterHVO dshvo=(DipSysregisterHVO) HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, fpkstr);
			String code=dshvo.getCode();
			String sysname=dshvo.getExtname();
//			createForder("数据接口平台", "sjjkpt", "-1Datadict");
			createForder(IContrastUtil.DATADICTFATHER_NAME, IContrastUtil.DATADICTFATHER_CODE, "-1Datadict");
//			createForder("接口平台", "jkpt", "sjjkpt");
			createForder(IContrastUtil.DATADICTFORDER_NAME, IContrastUtil.DATADICTFORDER_CODE, IContrastUtil.DATADICTFATHER_CODE);
//			createForder(sysname, code, "jkpt");
			createForder(sysname, code, IContrastUtil.DATADICTFORDER_CODE);
			Datadict m_dict = new Datadict();
			DDCData data = null;
			ObjectNode[] objNodes = null;
			BizObject[] objs = null;
			data=DDCBO_Client.fromDBMetaData(IContrastUtil.DESIGN_CON,new String[]{pk_node} , 1, code);
			objNodes = (ObjectNode[]) data.getNode().toArray(new ObjectNode[0]);
			objs = (BizObject[]) data.getTable().toArray(new BizObject[0]);
			m_dict.saveObjectNode(objNodes, objs, 20);

			//对有唯一约束的字段进行建立唯一索引
			DipDatadefinitBVO[] bvos=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, " pk_datadefinit_h='"+pk+"' and Isonlyconst='Y' and nvl(dr,0)=0");
			String isql="";
			String colname=null;
			if(bvos !=null){
				for(int i=0;i<bvos.length;i++){
					colname=bvos[i].getEname();
					isql="create unique index I"+tableName+colname+" on "+tableName+" ("+colname+")";
					iq.exesql(isql);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 

}
