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
 * ������һ�������࣬��ҪĿ�������ɰ�ť�¼�����Ŀ��
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
	 * ȡ�õ�ǰUI��
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
	 * ����Ŀ��ƴsql��䣬�Զ������� 
	 * 1.�ж��Ƿ��д�Ҫ�����ı�����У����ѯ�Ƿ������ݣ��������������ʾ����������ֱ��ɾ��
	 * 2.�ж����ͣ�����Ŀǰ��int���޳����⣬����Ķ��г��ȣ����������С��������С��λ�� 
	 * 3.�ж��Ƿ������ 
	 * 4.�ж��Ƿ������� 
	 * 
	 * @author cl
	 * @throws Exception
	 */
	IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	public void creatTable() {
		Integer flags=0;//ɾ����־��1��������ȷ����ť
		// ��ȡ�ڵ�
		DataDefinitClientUI ui = (DataDefinitClientUI) this.getBillUI();
		String pk_node = ui.selectnode;
		if ("".equals(pk_node)) {
			NCOptionPane.showMessageDialog(this.getBillUI(), "��ѡ��һ���ڵ㡣");
			return;
		} else {
//			�޸�ʱ����"������"��ť����Ϊ������
			getSelfUI().getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(false);
			DipDatadefinitHVO hvo=(DipDatadefinitHVO) ui.getBillTreeSelectNode().getData();
			String fpkstr=hvo.getPk_xt();
			if(fpkstr!=null && fpkstr.equals("0001AA1000000001XQ1B")){
				getSelfUI().showWarningMessage("ncϵͳ�ڵ㲻�ܽ���"); 
				return;
			}
			String userdefined = hvo.getUserdefined();
			if("��".equals(userdefined)){
				getSelfUI().showWarningMessage("�Զ��������/��ͼ���ܽ���"); 
				return;
			}
			String pk=(String) ui.getBillTreeSelectNode().getNodeID();//����
			// �����ֶ�����ñ�ͷ�ı�����Ӧֵ
			String tableName=(String)this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("memorytable").getValueObject();
			// �����ֶ�����ñ����ֶζ�Ӧֵ���������ơ�Ӣ�����ơ����͡����ȡ�С��λ�����Ƿ�����

			int row = this.getBillCardPanelWrapper().getBillCardPanel().getRowCount();
			String cname = null;
			String ename = null;
			String type = null;
			int length = 0;
			int deciplace = 0;// С��λ��
			boolean ispk = false;// �Ƿ�����
			boolean issyspk=false;//�Ƿ�ϵͳԤ������

			String pkcolname = null;// ����������ָ����һ���������ı���
			boolean isExist=isTableExist(tableName);
			if(isExist){
				//1.��ѯ�Ƿ�������:�����ݸ�����ʾ��û������ɾ��
				StringBuffer buffer=new StringBuffer();
				buffer.append("select 1 from ");
				buffer.append(tableName);

				ArrayList al=null;
				try{
					al=(ArrayList)queryBS.executeQuery(buffer.toString(), new MapListProcessor());
					//���ڸñ���������
					if(al.size()>=1){
						AskDLG adlg=new AskDLG(getSelfUI(),"��ʾ","ϵͳ�Ѿ����������ݣ�ȷ���Ƿ�ɾ��?",new String[]{"ɾ���ṹ���ؽ���ṹ","ת�����±�ṹ����"});
						adlg.showModal();
						if(adlg.getRes()==0){
							//ɾ���ñ�,���´���
							StringBuffer delSql=new StringBuffer();
							delSql.append("drop table ");
							delSql.append(tableName);
							iq.exesql(delSql.toString());

							//2011-7-13 ɾ��NC�����ֵ�ı�
							String delNC="delete from pub_datadict where id='"+tableName+"' and nvl(dr,0)=0";
							iq.exesql(delNC);
						}else if(adlg.getRes()==1) {
							//ת�����±�ṹ��ֻ���������ֶΣ������Ѿ�����������4���ֶΣ�����������һ���ֶΣ����Ը��ģ������Ķ���֧�֡�
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
								if(ff==false){//ִ��sql���ɹ���
									createDatadict(tableName, fpkstr, pk);
									getSelfUI().showWarningMessage("�޸ĳɹ���");
									return;
								}
								
							}else{
								getSelfUI().showErrorMessage("���ֶ�û�и��ģ�");
								return;
							}
							
							
							
						}else{
							//���ȡ��
							super.onBoCancel();
							return;
						}
					}else{
						flags = MessageDialog.showOkCancelDlg(this.getBillUI(), "��ʾ", "ϵͳ�Ѿ�����û�����ݣ��Ƿ�ȷ��ɾ��?");
						if(flags==1){
							//���ڸñ���û���ݣ��Ѹñ�ɾ��
							StringBuffer delSql=new StringBuffer();
							delSql.append("drop table ");
							delSql.append(tableName);
							iq.exesql(delSql.toString());

							//2011-7-13 ɾ��NC�����ֵ�ı�
							String delNC="delete from pub_datadict where id='"+tableName+"' and nvl(dr,0)=0";
							iq.exesql(delNC);
						}else{
							//���ȡ��
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
			str.append("\n");// ����
			str.append("(");
			str.append("\n");
			if(row>=1){
				Map tsdrMap=new HashMap();
				for (int i = 0; i < row; i++) {
					cname = (String) this.getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel("dip_datadefinit_b")
					.getValueAt(i, "cname");//��������		

					ename = (String) this.getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel("dip_datadefinit_b")
					.getValueAt(i, "ename");//Ӣ������
					if(ename==null||ename.length()<=0){
						getBillUI().showErrorMessage("Ӣ�����Ʋ���Ϊ�գ�");
						return;
					}
					
					if(ename.toLowerCase().equals("ts")||ename.toLowerCase().equals("dr")){
						tsdrMap.put(ename.toLowerCase(), ename.toLowerCase());	
					}
					
					
					type = (String) this.getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_datadefinit_b").getValueAt(i, "type");//��������
					if(type==null||type.length()<=0){
						getBillUI().showErrorMessage("�������Ͳ���Ϊ�գ�");
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
					
					str.append(ename.toLowerCase());//Ӣ������ת��ΪСд
					str.append("\t");
					
					if(ispkcan||issyspkcan){
						pkcolname=ename;
					}
					//Char,Date,Decimal,Integer,Varchar
					if(type.equals("CHAR")){
						//�õ�����
						/*
						 * ���Ӹ����͵�Ĭ�ϳ��� char�� ��Integer��number��data�ͣ����û�賤�ȣ�Ĭ��lengthΪ100
						 * number������û����length Ĭ��Ϊ20��deciplace Ĭ��Ϊ8
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

						//�ж��Ƿ���������ϵͳԤ������
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
						//�ж��Ƿ���������ϵͳԤ������
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
						//����
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
						//�ж��Ƿ���������ϵͳԤ������
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
						//�ж��Ƿ���������ϵͳԤ������
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
						//varchar��:����
						
						if(lengthcan!=null){
							length = (Integer) lengthcan;
						}
						else{
							this.getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_datadefinit_b").setValueAt(100, i, "length");							
						}
						str.append(type);
						//�ж��Ƿ���������ϵͳԤ������
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
				if(tsdrMap.get("ts")==null&&tsdrMap.get("dr")==null){//ts��dr��û��
					str.append("ts");
					str.append("\t");
					str.append("char(19),");
					str.append("\n");
						str.append("dr");
						str.append("\t");
						str.append("smallint");
						flag=true;
					flag=true;
				}else if(tsdrMap.get("ts")!=null&&tsdrMap.get("dr")!=null){////ts��dr����
						String sb=str.substring(0, str.length()-2);
						str=new StringBuffer();
						str.append(sb);
				}else{//ts��dr��һ��û��
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
				//�����ݿ��д�����
//				IQueryField iqd=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
				boolean isscuess=iq.exectEverySql(str.toString());
				if(isscuess==false){
					//�����Ƿ��ѽ����־  858--885��
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
					MessageDialog.showHintDlg(getSelfUI(), "��ʾ", "�ɹ�������");
					createDatadict( tableName, fpkstr, pk);
//					try {
//						//д��NC�����ֵ�
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
					MessageDialog.showHintDlg(getSelfUI(), "��ʾ", "������ʧ�ܣ�");
					return;
				}
			}
		}

	}

	/**
	 * ���ߣ�����
	 * �ж�ϵͳ�����Ƿ�����׼�������Ĵ˱�
	 * �ڵ��������ťʱ�жϣ�
	 * 1.����д˱���ѯ�����Ƿ������ݣ���������ݣ�������ʾ��û�У���ֱ��ɾ�������´���
	 * 2.û�д˱���ֱ�Ӵ���
	 */
	public boolean isTableExist(String tableName){
		boolean isExist=false;//Ĭ��û�д˱�
		if(tableName.toLowerCase().startsWith("v_dip")){
			isExist=true;
		}else {
			String sql="select 1 from user_tables where table_name='"+tableName.toUpperCase()+"';";	
			try{
				ArrayList al=(ArrayList)queryBS.executeQuery(sql, new MapListProcessor());
				if(al.size()>=1){
					isExist=true;//�д˱�			
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return isExist;
	}
	/**
	 * @desc ���������ֵ�����ļ��еķ���
	 * @param String displayname �ļ�������
	 * @param id �ļ��е�id
	 * @param parentID ���ڵ��ID
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
			//д��NC�����ֵ�
			DipSysregisterHVO dshvo=(DipSysregisterHVO) HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, fpkstr);
			String code=dshvo.getCode();
			String sysname=dshvo.getExtname();
//			createForder("���ݽӿ�ƽ̨", "sjjkpt", "-1Datadict");
			createForder(IContrastUtil.DATADICTFATHER_NAME, IContrastUtil.DATADICTFATHER_CODE, "-1Datadict");
//			createForder("�ӿ�ƽ̨", "jkpt", "sjjkpt");
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

			//����ΨһԼ�����ֶν��н���Ψһ����
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
